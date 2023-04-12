package org.hbrs.thesis.springboot_demo.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class PowerConsumption {
    private double pkgPowerConsumption;
    private double pp0PowerConsumption;
    private double dramPowerConsumption;

    public PowerConsumption() {
        startReadingPowerConsumptionValues();
    }

    public double getPkgPowerConsumption() {
        return pkgPowerConsumption;
    }

    public double getPp0PowerConsumption() {
        return pp0PowerConsumption;
    }

    public double getDramPowerConsumption() {
        return dramPowerConsumption;
    }

    private void startReadingPowerConsumptionValues() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/bash", "-c", "rdmsr -d 0x611; rdmsr -d 0x639; rdmsr -d 0x619;");
        double conversionPowerUnit = getConversionPowerUnit();
        Thread thread = new Thread(() -> {
            double previousPkgEnergy = 0.0;
            double actualPkgEnergy = 0.0;
            double previousPP0Energy = 0.0;
            double actualPP0Energy = 0.0;
            double previousDRAMEnergy = 0.0;
            double actualDRAMEnergy = 0.0;
            while (true) {
                try {
                    Process process = processBuilder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    int readerIndex = 0;
                    while ((line = reader.readLine()) != null) {
                        if (readerIndex == 0) {
                            actualPkgEnergy = Long.parseLong(line) * conversionPowerUnit;
                            if (previousPkgEnergy != 0.0) {
                                pkgPowerConsumption = actualPkgEnergy - previousPkgEnergy;
                            }
                        }
                        if (readerIndex == 1) {
                            actualPP0Energy = Long.parseLong(line) * conversionPowerUnit;
                            if (previousPP0Energy != 0.0) {
                                pp0PowerConsumption = actualPP0Energy - previousPP0Energy;
                            }
                        }
                        if (readerIndex == 2) {
                            actualDRAMEnergy = Long.parseLong(line) * conversionPowerUnit;
                            if (previousDRAMEnergy != 0.0) {
                                dramPowerConsumption = actualDRAMEnergy - previousDRAMEnergy;
                            }
                        }
                        ++readerIndex;
                    }
                    previousPkgEnergy = actualPkgEnergy;
                    previousPP0Energy = actualPP0Energy;
                    previousDRAMEnergy = actualDRAMEnergy;
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();
    }

    private static double getConversionPowerUnit() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 0x606 is the msr RAPL power unit
        processBuilder.command("/bin/bash", "-c", "rdmsr -d 0x606");
        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    int powerUnit = Integer.parseInt(line);
                    return 1.0 / twoToPowerOf(extractBits(powerUnit, 8, 12));
                }
                return -1.0;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return -1.0;
        }
    }

    private static Long twoToPowerOf(int value) {
        return (long) (1 << value);
    }

    private static int extractBits(int data, int lo, int hi) {
        return (data & (~0 << lo) & ~(~0 << hi + 1)) >> lo;
    }
}

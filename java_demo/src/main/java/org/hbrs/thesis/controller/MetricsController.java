package org.hbrs.thesis.controller;

import static spark.Spark.get;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.TimeUnit;

import com.sun.management.OperatingSystemMXBean;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.common.TextFormat;
import spark.Request;
import spark.Response;

public class MetricsController {
    private static final CollectorRegistry registry = CollectorRegistry.defaultRegistry;
    private static final String PROMETHEUS_NAMESPACE = "java_spark";
    private static final Gauge CPU_LOAD_GAUGE = Gauge.build()
            .namespace(PROMETHEUS_NAMESPACE).name("cpu_load").help("CPU load in %")
            .register();
    private static final Gauge PROCESS_CPU_LOAD_GAUGE = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("process_cpu_load")
            .help("CPU utilization in %").register();
    private static final Gauge INITIAL_MEMORY_GAUGE = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("initial_memory")
            .help("Initial memory").register();
    private static final Gauge MAX_HEAP_MEMORY_GAUGE = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("max_heap_memory")
            .help("Max heap memory").register();
    private static final Gauge USED_HEAP_MEMORY_GAUGE = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("used_heap_memory")
            .help("Used heap memory ").register();
    private static final Gauge COMMITTED_MEMORY_GAUGE = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("committed_memory")
            .help("Committed Memory").register();
    private static final Gauge CPU_PACKAGE_POWER_CONSUMPTION = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("cpu_package_power_consumption").help("CPU package power consumption").register();
    private static final Gauge CPU_PP0_POWER_CONSUMPTION = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("cpu_pp0_power_consumption").help("CPU PP0 power consumption").register();
    private static final Gauge DRAM_POWER_CONSUMPTION = Gauge.build().namespace(PROMETHEUS_NAMESPACE)
            .name("dram_power_consumption").help("DRAM power consumption").register();

    private static Long twoToPowerOf(int value) {
        return (long) (1 << value);
    }

    private static int extractBits(int data, int lo, int hi) {
        return (data & (~0 << lo) & ~(~0 << hi + 1)) >> lo;
    }

    private static double getConversionPowerUnit() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 0x606 is the msr RAPL power unit
        processBuilder.command("/bin/bash", "-c", "rdmsr -d 0x606");
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine();
            if (line != null) {
                int powerUnit = Integer.parseInt(line);
                return 1.0 / twoToPowerOf(extractBits(powerUnit, 8, 12));
            }
            return 0.0;
        }
    }

    public void exposePrometheusMetrics() throws IOException {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        double conversionPowerUnit = getConversionPowerUnit();
        ProcessBuilder processBuilder = new ProcessBuilder();
        // 0x611 is the msr pkg energy status
        // 0x639 is the msr PP0 energy status
        // 0x619 is the msr DRAM energy status
        processBuilder.command("/bin/bash", "-c", "rdmsr -d 0x611; rdmsr -d 0x639; rdmsr -d 0x619;");
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
                                CPU_PACKAGE_POWER_CONSUMPTION.set(actualPkgEnergy - previousPkgEnergy);
                                System.out.println("pkg: " + (actualPkgEnergy - previousPkgEnergy) + "");
                            }
                        }
                        if (readerIndex == 1) {
                            actualPP0Energy = Long.parseLong(line) * conversionPowerUnit;
                            if (previousPP0Energy != 0.0) {
                                CPU_PP0_POWER_CONSUMPTION.set(actualPP0Energy - previousPP0Energy);
                                System.out.println("pp0: " + (actualPP0Energy - previousPP0Energy) + "" );
                            }
                        }
                        if (readerIndex == 2) {
                            actualDRAMEnergy = Long.parseLong(line) * conversionPowerUnit;
                            if (previousDRAMEnergy != 0.0) {
                                DRAM_POWER_CONSUMPTION.set(actualDRAMEnergy - previousDRAMEnergy);
                                System.out.println("dram: " + (actualDRAMEnergy - previousDRAMEnergy) + "" );
                            }
                        }
                        ++readerIndex;
                    }
                    previousPkgEnergy = actualPkgEnergy;
                    previousPP0Energy = actualPP0Energy;
                    previousDRAMEnergy = actualDRAMEnergy;
                    double processCpuLoad = osBean.getProcessCpuLoad();
                    CPU_LOAD_GAUGE.set(osBean.getCpuLoad() * 100);
                    PROCESS_CPU_LOAD_GAUGE.set(processCpuLoad * 100);
                    MAX_HEAP_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getMax() / 1048576);
                    INITIAL_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getInit() / 1048576);
                    USED_HEAP_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576);
                    COMMITTED_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        get("/metrics", this::prometheusMetricsEndpoint);
        get("/healthz", (req, res) -> "UP");
        thread.start();
    }

    private String prometheusMetricsEndpoint(Request request, Response response) throws IOException {
        response.status(200);
        response.type(TextFormat.CONTENT_TYPE_004);
        final StringWriter writer = new StringWriter();
        TextFormat.write004(writer, registry.metricFamilySamples());
        return writer.toString();
    }
}

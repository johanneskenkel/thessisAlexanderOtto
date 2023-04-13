package org.hbrs.thesis.springboot_demo.config;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class PowerConsumptionMetrics {
    public PowerConsumptionMetrics(MeterRegistry meterRegistry) {
        PowerConsumption powerConsumption = new PowerConsumption();
        Gauge.builder("spring_boot_cpu_package_power_consumption", powerConsumption, value -> powerConsumption.getPkgPowerConsumption())
                .register(meterRegistry);
        Gauge.builder("spring_boot_cpu_pp0_power_consumption", powerConsumption, value ->

        powerConsumption.getPp0PowerConsumption()

        ).register(meterRegistry);
        Gauge.builder("spring_boot_dram_power_consumption", powerConsumption, value -> powerConsumption.getDramPowerConsumption()

        ).register(meterRegistry);
    }
}

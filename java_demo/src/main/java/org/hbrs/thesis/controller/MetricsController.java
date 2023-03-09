package org.hbrs.thesis.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.concurrent.TimeUnit;
import com.sun.management.OperatingSystemMXBean;
import static spark.Spark.get;

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

    public void exposePrometheusMetrics() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    double processCpuLoad = osBean.getProcessCpuLoad();
                    CPU_LOAD_GAUGE.set(osBean.getCpuLoad() * 100);
                    PROCESS_CPU_LOAD_GAUGE.set(processCpuLoad * 100);
                    // 45W is the TDP of an Intel® Core™ i7-9750H Processor     
                    MAX_HEAP_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getMax() / 1048576);
                    INITIAL_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getInit() / 1048576);
                    USED_HEAP_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576);
                    COMMITTED_MEMORY_GAUGE.set((double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
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

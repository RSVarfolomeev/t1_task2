package t1.school.producer.service;

import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;
import t1.school.producer.dto.MetricDto;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MetricsCollectorService {

    private final Set<String> metricNames = Set.of(
            "disk.free",
            "disk.total",
            "jvm.buffer.memory.used",
            "jvm.memory.max",
            "jvm.memory.used",
            "system.cpu.usage"
    );

    private final MetricsEndpoint metricsEndpoint;

    public MetricsCollectorService(MetricsEndpoint metricsEndpoint) {
        this.metricsEndpoint = metricsEndpoint;
    }

    public MetricDto getMetric(String metricName) {
        return extractMetric(metricName);
    }

    public List<MetricDto> getMetricsForScheduler() {
        List<MetricDto> metrics = new ArrayList<>();
        metricNames.forEach(mn -> metrics.add(extractMetric(mn)));
        return metrics;
    }

    private MetricDto extractMetric(String metricName) {
        MetricsEndpoint.MetricDescriptor metricDescriptor = metricsEndpoint.metric(metricName, null);

        if (metricDescriptor == null) {
            throw new IllegalArgumentException("Недопустимое имя метрики: " + metricName);
        }

        return new MetricDto(metricDescriptor.getName(),
                metricDescriptor.getDescription(),
                metricDescriptor.getBaseUnit(),
                new Timestamp(System.currentTimeMillis()),
                metricDescriptor.getMeasurements().stream()
                        .map(MetricsEndpoint.Sample::getValue)
                        .findFirst()
                        .orElse((double) 0));
    }
}

package t1.school.producer.service;

import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;
import t1.school.producer.dto.MetricDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<MetricDto> getMetrics() {
        List<MetricDto> metrics = new ArrayList<>();

        metricNames.forEach(mn ->{
            MetricsEndpoint.MetricDescriptor metricDescriptor = metricsEndpoint.metric(mn, null);
            metrics.add(new MetricDto(mn,
                    metricDescriptor.getDescription(),
                    metricDescriptor.getBaseUnit(),
                    LocalDateTime.now(),
                    metricDescriptor.getMeasurements().stream()
                            .map(MetricsEndpoint.Sample::getValue)
                            .findFirst()
                            .orElse((double) 0)));
        });

        return metrics;
    }
}

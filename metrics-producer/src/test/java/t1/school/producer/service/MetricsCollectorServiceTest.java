package t1.school.producer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import t1.school.producer.dto.MetricDto;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MetricsCollectorService.class)
class MetricsCollectorServiceTest {

    @MockBean
    private MetricsEndpoint metricsEndpoint;

    @Autowired
    private MetricsCollectorService metricsCollectorService;

    @Test
    void getMetric() {
        when(metricsEndpoint.metric("disk.free", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));

        MetricDto metricDto = metricsCollectorService.getMetric("disk.free");
        Assertions.assertNotNull(metricDto);
    }

    @Test
    void getMetricsForScheduler() {
        when(metricsEndpoint.metric("disk.free", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));
        when(metricsEndpoint.metric("disk.total", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));
        when(metricsEndpoint.metric("jvm.buffer.memory.used", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));
        when(metricsEndpoint.metric("jvm.memory.max", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));
        when(metricsEndpoint.metric("jvm.memory.used", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));
        when(metricsEndpoint.metric("system.cpu.usage", null)).thenReturn(mock(MetricsEndpoint.MetricDescriptor.class));

        List<MetricDto> metrics = metricsCollectorService.getMetricsForScheduler();
        Assertions.assertNotNull(metrics);
    }
}
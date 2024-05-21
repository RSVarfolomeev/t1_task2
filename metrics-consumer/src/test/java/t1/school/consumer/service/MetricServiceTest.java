package t1.school.consumer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import t1.school.consumer.model.Metric;
import t1.school.consumer.repository.MetricRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = MetricService.class)
class MetricServiceTest {

    @MockBean
    private MetricRepository repository;
    @Autowired
    private MetricService metricService;

    @Test
    void getAllMetrics() {
        Metric metric = Metric.builder()
                .id(1L)
                .name("disk.free")
                .description("Usable space for path")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(11241697280.0)
                .build();

        Metric metric_2 = Metric.builder()
                .id(2L)
                .name("jvm.memory.max")
                .description("The maximum amount of memory in bytes that can be used for memory management")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(2118123517.0)
                .build();

        given(repository.findAll())
                .willReturn(List.of(metric, metric_2));

        List<Metric> metrics = metricService.getAllMetrics();

        Assertions.assertNotNull(metrics);
        Assertions.assertEquals(2, metrics.size());
        Assertions.assertEquals("disk.free", metrics.get(0).getName());
        Assertions.assertEquals("Usable space for path", metrics.get(0).getDescription());
        Assertions.assertEquals("bytes", metrics.get(0).getBaseUnit());
        Assertions.assertEquals(11241697280.0, metrics.get(0).getValue());

        Assertions.assertEquals("jvm.memory.max", metrics.get(1).getName());
        Assertions.assertEquals("The maximum amount of memory in bytes that can be used for memory management", metrics.get(1).getDescription());
        Assertions.assertEquals("bytes", metrics.get(1).getBaseUnit());
        Assertions.assertEquals(2118123517.0, metrics.get(1).getValue());
    }

    @Test
    void getMetricById() {
        Metric metric = Metric.builder()
                .id(1L)
                .name("disk.free")
                .description("Usable space for path")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(11241697280.0)
                .build();

        given(repository.findById(any()))
                .willReturn(Optional.ofNullable(metric));

        Metric metricById = metricService.getMetricById(1L);

        Assertions.assertNotNull(metricById);
        Assertions.assertEquals("disk.free", metricById.getName());
        Assertions.assertEquals("Usable space for path", metricById.getDescription());
        Assertions.assertEquals("bytes", metricById.getBaseUnit());
        Assertions.assertEquals(11241697280.0, metricById.getValue());
    }
}
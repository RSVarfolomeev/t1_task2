package t1.school.producer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import t1.school.producer.dto.MetricDto;
import t1.school.producer.dto.MetricRequestDto;

import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MetricsSenderService.class)
class MetricsSenderServiceTest {

    @MockBean
    private KafkaTemplate<String, MetricDto> kafkaTemplate;
    @MockBean
    private MetricsCollectorService metricsCollectorService;
    @Autowired
    private MetricsSenderService metricsSenderService;

    @Test
    void sendMetric() {
        MetricRequestDto metricRequestDto = MetricRequestDto.builder()
                .metricName("disk.free")
                .build();

        MetricDto metricDto = MetricDto.builder()
                .name("disk.free")
                .description("Usable space for path")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(11241697280.0)
                .build();
        CompletableFuture<SendResult<String, MetricDto>> future = mock(CompletableFuture.class);
        when(metricsCollectorService.getMetric(metricRequestDto.getMetricName())).thenReturn(metricDto);
        when(kafkaTemplate.send("metrics-topic", metricDto)).thenReturn(future);

        String result = metricsSenderService.sendMetric(metricRequestDto);
        Assertions.assertEquals("Метрика disk.free отправлена в Кафку.", result);
    }
}
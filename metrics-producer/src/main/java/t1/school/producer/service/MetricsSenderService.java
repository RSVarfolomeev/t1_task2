package t1.school.producer.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import t1.school.producer.dto.MetricDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class MetricsSenderService {
    @Value("${topic.name}")
    private String metricTopic;

    private final KafkaTemplate<String, List<MetricDto>> kafkaTemplate;
    private final MetricsCollectorService metricsCollectorService;

    public MetricsSenderService(KafkaTemplate<String, List<MetricDto>> kafkaTemplate,
                                MetricsCollectorService metricsCollectorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.metricsCollectorService = metricsCollectorService;
    }

    @Scheduled(initialDelay = 2_000, fixedDelay = 10_000)
    public void sendAllMetricsToKafka() {
        try {
            List<MetricDto> metrics = metricsCollectorService.getMetrics();
            CompletableFuture<SendResult<String, List<MetricDto>>> completableFuture = kafkaTemplate.send(metricTopic, metrics);
            completableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Отправлено сообщение=[{}] со смещением=[{}]", metrics, result.getRecordMetadata().offset());
                } else {
                    log.info("Невозможно отправить сообщение=[{}] из-за ошибки: {}", metrics, ex.getMessage());
                }
            });
        } catch (Exception ex) {
            log.error("Возникла ошибка в периодической задаче при отправке метрик в Кафку: ", ex);
        }
    }
}

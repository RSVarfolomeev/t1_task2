package t1.school.producer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import t1.school.producer.dto.MetricDto;
import t1.school.producer.dto.MetricRequestDto;

import java.util.List;

@Service
@Slf4j
public class MetricsSenderService {
    @Value("${topic.name}")
    private String metricTopic;

    private final KafkaTemplate<String, MetricDto> kafkaTemplate;
    private final MetricsCollectorService metricsCollectorService;

    public MetricsSenderService(KafkaTemplate<String, MetricDto> kafkaTemplate,
                                MetricsCollectorService metricsCollectorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.metricsCollectorService = metricsCollectorService;
    }

    /**
     * Периодическая задача, которая отправляет метрики в Кафку.
     */
    @Scheduled(initialDelay = 2_000, fixedDelay = 10_000)
    public void sendAllMetrics() {
        sendMetricsToKafka(metricsCollectorService.getMetricsForScheduler());
    }

    public String sendMetric(MetricRequestDto metricRequestDto) {
        String metricName = metricRequestDto.getMetricName();
        sendMetricsToKafka(List.of(metricsCollectorService.getMetric(metricName)));
        return "Метрика " + metricName + " отправлена в Кафку.";
    }

    private void sendMetricsToKafka(List<MetricDto> metrics) {
        try {
            metrics.forEach(metric -> kafkaTemplate.send(metricTopic, metric).whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Отправлено сообщение=[{}] со смещением=[{}]", metric, result.getRecordMetadata().offset());
                } else {
                    log.info("Невозможно отправить сообщение=[{}] из-за ошибки: {}", metric, ex.getMessage());
                }
            }));
        } catch (Exception ex) {
            log.error("Возникла ошибка при отправке метрик в Кафку: ", ex);
        }
    }
}

package t1.school.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import t1.school.consumer.model.Metric;
import t1.school.consumer.model.dto.MetricDto;
import t1.school.consumer.repository.MetricRepository;

@Service
@Slf4j
public class KafkaService {

    private final MetricRepository repository;

    public KafkaService(MetricRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(id = "metric-consumer", topics = "${topic.name}")
    public void listenMetrics(MetricDto metric) {
        try {
            log.info("Получена метрика из Kafka: {}", metric);

            repository.save(Metric.builder()
                    .name(metric.getName())
                    .description(metric.getDescription())
                    .baseUnit(metric.getBaseUnit())
                    .time(metric.getTime())
                    .value(metric.getValue())
                    .build());

        } catch (Exception ex) {
            log.error("Ошибка при попытке десериализации сообщения: ", ex);
        }
    }
}

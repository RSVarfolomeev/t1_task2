package t1.school.consumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import t1.school.consumer.dto.MetricDto;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    @KafkaListener(topics = "${topic.name}", id = "metric-consumer")
    public void listenMetrics(List<MetricDto> metrics) {
        log.info("Получены метрики из Kafka, {} ед.: {}", metrics.size(), metrics);
    }

//DLT

}

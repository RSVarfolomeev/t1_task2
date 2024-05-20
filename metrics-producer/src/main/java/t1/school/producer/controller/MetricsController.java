package t1.school.producer.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1.school.producer.dto.MetricRequestDto;
import t1.school.producer.service.MetricsSenderService;


@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsSenderService metricsSenderService;

    public MetricsController(MetricsSenderService metricsSenderService) {
        this.metricsSenderService = metricsSenderService;
    }

    @Operation(summary = """
Отправка указанной метрики в Кафку. Список возможных значений параметра 'metricName' в JSON можно посмотреть
 в Spring Boot Actuator по адресу: http://localhost:8080/actuator/metrics.
""")
    @PostMapping()
    public ResponseEntity<String> sendMetric(@RequestBody MetricRequestDto metricRequestDto) {
        return ResponseEntity.ok(metricsSenderService.sendMetric(metricRequestDto));
    }
}

package t1.school.consumer.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import t1.school.consumer.model.Metric;
import t1.school.consumer.service.MetricService;

import java.util.List;


@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricService metricService;

    public MetricsController(MetricService metricService) {
        this.metricService = metricService;
    }

    @Operation(summary = "Получение списка со значениями всех метрик.")
    @GetMapping()
    public ResponseEntity<List<Metric>> getAllMetrics() {
        return ResponseEntity.ok(metricService.getAllMetrics());
    }

    @Operation(summary = "Получение значения метрики по заданному Id.")
    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetricById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(metricService.getMetricById(id));
    }
}

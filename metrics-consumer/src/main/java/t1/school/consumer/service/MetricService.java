package t1.school.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import t1.school.consumer.model.Metric;
import t1.school.consumer.repository.MetricRepository;

import java.util.List;

@Service
@Slf4j
public class MetricService {

    private final MetricRepository repository;

    public MetricService(MetricRepository repository) {
        this.repository = repository;
    }

    public List<Metric> getAllMetrics() {
        return repository.findAll().stream().toList();
    }

    public Metric getMetricById(Long id) {
        return repository.findById(id).orElse(null);
    }
}

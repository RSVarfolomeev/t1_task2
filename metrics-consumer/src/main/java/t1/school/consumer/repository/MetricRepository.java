package t1.school.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import t1.school.consumer.model.Metric;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
}
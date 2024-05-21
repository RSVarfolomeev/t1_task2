package t1.school.consumer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import t1.school.consumer.model.dto.MetricDto;
import t1.school.consumer.repository.MetricRepository;

import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = KafkaService.class)
class KafkaServiceTest {

    @MockBean
    private MetricRepository repository;
    @Autowired
    private KafkaService kafkaService;


    @Test
    void listenMetrics() {
        MetricDto metricDto = MetricDto.builder()
                .name("disk.free")
                .description("Usable space for path")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(11241697280.0)
                .build();
        kafkaService.listenMetrics(metricDto);

        verify(repository, times(1)).save(any());
    }
}
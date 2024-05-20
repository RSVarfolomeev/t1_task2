package t1.school.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaProducerConfig {
    @Value("${topic.name}")
    private String metricsTopic;
    @Value("${topic.name-dlt}")
    private String metricsTopicDlt;

    @Bean
    public NewTopic metricsTopic() {
        return new NewTopic(metricsTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic dltMetricsTopic() {
        return new NewTopic(metricsTopicDlt, 1, (short) 1);
    }

    @Bean
    public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> kafkaOperations) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaOperations), new FixedBackOff(1000L, 1));
    }
}

package t1.school.consumer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {
    @Value("${topic.name}")
    private String metricsTopic;

    private static final String DLT_TOPIC_SUFFIX = ".DLT";

    @Bean
    public NewTopic metricsTopic() {
        return new NewTopic(metricsTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic dltMetricsTopic() {
        return new NewTopic(metricsTopic + DLT_TOPIC_SUFFIX, 1, (short) 1);
    }

    @Bean
    public RecordMessageConverter converter(){
        return new JsonMessageConverter();
    }

    @Bean
    public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> kafkaOperations) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaOperations), new FixedBackOff(1000L, 2));
    }
}

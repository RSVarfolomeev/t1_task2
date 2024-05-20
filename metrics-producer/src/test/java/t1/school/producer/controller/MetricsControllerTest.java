package t1.school.producer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import t1.school.producer.dto.MetricRequestDto;
import t1.school.producer.service.MetricsSenderService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetricsController.class)
class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricsSenderService metricsSenderService;

    @Test
    void sendMetric() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MetricRequestDto metricRequestDto = MetricRequestDto.builder()
                .metricName("disk.free")
                .build();
        String metricRequestDtoJson = objectMapper.writeValueAsString(metricRequestDto);
        String expectResponse = "Метрика " + metricRequestDto.getMetricName() + " отправлена в Кафку.";

        //Что нам вернет сервис
        when(metricsSenderService.sendMetric(Mockito.any(MetricRequestDto.class)))
                .thenReturn(expectResponse);

        //Что нам вернет наш контроллер
        mockMvc.perform(MockMvcRequestBuilders.post("/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(metricRequestDtoJson))
                        .andExpect(status().isOk())
                        .andExpect(content().string(expectResponse));

        //Проверяем, что у сервиса был вызван метод sendMetric 1 раз
        verify(metricsSenderService, times(1)).sendMetric(any(MetricRequestDto.class));
    }
}
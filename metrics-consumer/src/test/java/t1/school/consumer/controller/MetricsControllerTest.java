package t1.school.consumer.controller;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import t1.school.consumer.model.Metric;
import t1.school.consumer.service.MetricService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MetricsController.class)
class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricService metricService;

    @Test
    void getAllMetrics() throws Exception {

        Metric metric_1 = Metric.builder()
                .id(1L)
                .name("disk.free")
                .description("Usable space for path")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(11241697280.0)
                .build();

        Metric metric_2 = Metric.builder()
                .id(2L)
                .name("jvm.memory.max")
                .description("The maximum amount of memory in bytes that can be used for memory management")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(2118123517.0)
                .build();

        Metric metric_3 = Metric.builder()
                .id(3L)
                .name("jvm.memory.used")
                .description("The amount of used memory")
                .baseUnit("bytes")
                .time(new Timestamp(System.currentTimeMillis()))
                .value(113538440.0)
                .build();


        when(metricService.getAllMetrics())
                .thenReturn(List.of(metric_1, metric_2, metric_3));

        mockMvc.perform(get("/metrics"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].name", is("disk.free")))
                .andExpect(jsonPath("$.[1].name", is("jvm.memory.max")))
                .andExpect(content().string(containsString("The amount of used memory")))
                .andExpect(jsonPath("$.[0].['value']", is(1.124169728E10)));
    }

    @Test
    void getMetricById() throws Exception {

        Metric metric_1 = Metric.builder()
                .id(1L)
                .name("disk.free")
                .description("Usable space for path")
                .baseUnit("bytes")
                .time(new Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse("2024-05-20 20:31:53.642").getTime()))
                .value(1.124169728E10)
                .build();

        when(metricService.getMetricById(1L))
                .thenReturn(metric_1);

        mockMvc.perform(get("/metrics/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name", is("disk.free")))
                .andExpect(jsonPath("$.description", is("Usable space for path")))
                .andExpect(jsonPath("$.baseUnit", is("bytes")))
                .andExpect(jsonPath("$.time", IsNull.notNullValue()))
                .andExpect(jsonPath("$.value", is(1.124169728E10)));
    }
}
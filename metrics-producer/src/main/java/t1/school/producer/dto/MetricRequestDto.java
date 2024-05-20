package t1.school.producer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetricRequestDto {

    @Schema(description = "Наименование метрики", example = "disk.free")
    private String metricName;
}

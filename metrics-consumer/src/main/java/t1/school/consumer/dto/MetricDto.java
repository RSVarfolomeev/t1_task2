package t1.school.consumer.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetricDto {

    private String name;
    private String description;
    private String baseUnit;
    private LocalDateTime time;
    private Double value;
}

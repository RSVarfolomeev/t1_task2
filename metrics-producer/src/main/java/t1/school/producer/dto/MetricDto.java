package t1.school.producer.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetricDto {

    private String name;
    private String description;
    private String baseUnit;
    private Timestamp time;
    private Double value;
}

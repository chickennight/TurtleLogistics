package class2.a204.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisGetDTO {
    private Integer productNum;
    private Long todayAmount;
    private Long weekAvg;
    private Long monthAvg;
    private Long yearAvg;
}

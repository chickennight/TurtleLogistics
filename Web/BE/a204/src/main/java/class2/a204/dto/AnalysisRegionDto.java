package class2.a204.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnalysisRegionDto {
    private Integer region;
    private Long count;

    public AnalysisRegionDto(Integer region, Long count) {
        this.region = region;
        this.count = count;
    }
}

package class2.a204.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AnalysisRegionDTO {
    private Integer region;
    private Long count;

    public AnalysisRegionDTO(Integer region, Long count){
        this.region = region;
        this.count = count;
    }
}

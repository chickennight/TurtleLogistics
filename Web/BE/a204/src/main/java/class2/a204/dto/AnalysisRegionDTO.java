package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRegionDTO {

    @JsonProperty("주문 접수")
    private long countOrder;
    @JsonProperty("포장 과정")
    private long countPackage;
    @JsonProperty("분류 과정")
    private long countClass;
    @JsonProperty("분류 완료")
    private long countComplete;
    @JsonProperty("배송 과정")
    private long countDelivery;
    @JsonProperty("이상 발생")
    private long countError;

    public AnalysisRegionDTO(List<Long[]> list) {
        for (Long[] l : list)
            switch (l[0].intValue()) {
                case 1:
                    this.countOrder = l[1];
                    break;
                case 2:
                    this.countPackage = l[1];
                    break;
                case 3:
                    this.countClass = l[1];
                    break;
                case 4:
                    this.countComplete = l[1];
                    break;
                case 5:
                    this.countDelivery = l[1];
                    break;
                default:
                    this.countError = l[1];
                    break;
            }
    }
}

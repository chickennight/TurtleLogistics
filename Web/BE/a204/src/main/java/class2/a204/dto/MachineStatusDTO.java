package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MachineStatusDTO {
    @JsonProperty("order_num")
    private Long orderNum;

    private Integer type;
    private Integer result;
}

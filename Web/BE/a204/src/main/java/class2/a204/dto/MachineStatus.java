package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineStatus {
    @JsonProperty("order_num")
    private Long orderNum;

    private int type;
    private int result;
}

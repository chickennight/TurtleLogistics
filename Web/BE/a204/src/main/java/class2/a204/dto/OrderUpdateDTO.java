package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class OrderUpdateDTO {

    @JsonProperty("order_num")
    private Long orderNum;

    private Integer type;

    private Integer result;

}

package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {

    @JsonProperty("order_num")
    private Long orderNum;

    @JsonProperty("product_num")
    private Integer ProductNum = 0;

    private Integer type;

    private Integer result;

}

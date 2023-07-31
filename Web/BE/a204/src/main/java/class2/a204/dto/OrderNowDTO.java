package class2.a204.dto;

import class2.a204.entity.OrderNow;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderNowDTO {
    @JsonProperty("order_num")
    private Long orderNum;

    private Integer status;

    public OrderNowDTO(OrderNow orderNow) {
        this.orderNum = orderNow.getOrder().getOrderNum();
        this.status = orderNow.getStatus();
    }
}

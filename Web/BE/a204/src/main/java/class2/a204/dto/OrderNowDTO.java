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

    private String status;

    public OrderNowDTO(OrderNow orderNow) {
        this.orderNum = orderNow.getOrder().getOrderNum();
        String status = "";
        switch (orderNow.getStatus()) {
            case 1:
                status = "주문 접수";
                break;
            case 2:
                status = "포장 과정";
                break;
            case 3:
                status = "분류 과정";
                break;
            case 4:
                status = "분류 완료";
                break;
            case 5:
                status = "배송 과정";
                break;
            case 0:
                status = "이상 발생";
        }
        this.status = status;
    }
}

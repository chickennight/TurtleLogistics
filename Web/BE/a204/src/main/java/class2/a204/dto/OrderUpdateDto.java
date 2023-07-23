package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderUpdateDto {

    @JsonProperty("order_num")
    private Long orderNum;

    private Integer type;

    private Integer result;

    public OrderUpdateDto() {
    }

    public OrderUpdateDto(Long orderNum, Integer type, Integer result) {
        this.orderNum = orderNum;
        this.type = type;
        this.result = result;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}

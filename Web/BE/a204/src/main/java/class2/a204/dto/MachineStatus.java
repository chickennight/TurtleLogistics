package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MachineStatus {
    @JsonProperty("order_num")
    private Long orderNum;

    private int type;
    private int result;

    public MachineStatus() {
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long order_num) {
        this.orderNum = order_num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}

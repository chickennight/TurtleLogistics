package class2.a204.model;

import javax.persistence.*;

@Entity
@Table(name = "ordernow")
public class OrderNow {
    @Id
    private String orderNum;

    private Boolean status;

    @OneToOne
    @MapsId
    @JoinColumn(name = "order_num", nullable = false)
    private Orders orders;

    //기본생성자
    public OrderNow() {
    }

    //getters and setters
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
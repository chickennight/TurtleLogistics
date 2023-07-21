package class2.a204.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "ordernow")
public class OrderNow {
    @Id
    @Column(name = "order_num")
    @NotNull
    private Integer orderNum;

    @Column(name = "status")
    @NotNull
    private byte status;

    @OneToOne
    @MapsId
    private Order order;

    //기본생성자
    public OrderNow() {
    }

    //getters and setters

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
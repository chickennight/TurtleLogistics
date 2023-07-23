package class2.a204.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "ordernow")
public class OrderNow {

    @Id
    @Column(name = "id")
    @NotNull
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_num")
    @NotNull
    private Order orderNum;

    @Column(name = "status")
    @NotNull
    private Integer status;


    //기본생성자
    public OrderNow() {
    }

    //getters and setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Order orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
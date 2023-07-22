package class2.a204.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_num")
    @NotNull
    private Long orderNum;

    @Column(name = "order_date")
    @NotNull
    private LocalDateTime orderData;

    @Column(name = "address")
    @NotNull
    private String address;

    @ManyToOne
    @JoinColumn(name = "customer_num")
    @NotNull
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    //기본생성자
    public Order() {
    }

    //getters and setters

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public LocalDateTime getOrderData() {
        return orderData;
    }

    public void setOrderData(LocalDateTime orderData) {
        this.orderData = orderData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

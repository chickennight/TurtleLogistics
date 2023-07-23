package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @JsonProperty("order_num")
    @Id
    @Column(name = "order_num")
    @NotNull
    private Long orderNum;

    @JsonProperty("order_date")
    @Column(name = "order_date", insertable = false)
    @NotNull
    private LocalDateTime orderDate;

    @Column(name = "address")
    @NotNull
    private String address;

    @JsonProperty("customer_num")
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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderNum=" + orderNum +
                ", orderDate=" + orderDate +
                ", address='" + address + '\'' +
                ", customer=" + customer +
                ", orderDetails=" + orderDetails +
                '}';
    }
}

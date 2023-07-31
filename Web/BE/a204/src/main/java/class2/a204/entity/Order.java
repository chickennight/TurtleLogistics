package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @JsonProperty("detail_address")
    @Column(name = "detail_address")
    private String detailAddress;

    private Integer address;

    @JsonProperty("customer_num")
    @ManyToOne
    @JoinColumn(name = "customer_num")
    @NotNull
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    public Order(Long orderNum, String detailAddress, Integer address, Customer customer) {
        this.orderNum = orderNum;
        this.detailAddress = detailAddress;
        this.address = address;
        this.customer = customer;
    }


}

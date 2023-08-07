package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orderdetail")
public class OrderDetail {

    @JsonProperty("order_detail_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @Column(name = "amount")
    @NotNull
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_num")
    @NotNull
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_num")
    @NotNull
    private Order order;

    @JoinColumn(name = "order_date")
    @NotNull
    private Integer orderDate;

    public OrderDetail(Order order, Product product, Integer amount, Integer orderDate) {
        this.order = order;
        this.product = product;
        this.amount = amount;
        this.orderDate = orderDate;
    }

}
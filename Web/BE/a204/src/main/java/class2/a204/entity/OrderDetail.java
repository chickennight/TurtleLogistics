package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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

    @ManyToOne
    @JoinColumn(name = "product_num")
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_num")
    @NotNull
    private Order order;

}
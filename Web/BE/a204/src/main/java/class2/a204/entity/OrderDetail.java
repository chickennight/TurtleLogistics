package class2.a204.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "orderdetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    @NotNull
    private Integer orderDetailId;

    @Column(name = "amount")
    @NotNull
    private Integer amount;
;
    @ManyToOne
    @JoinColumn(name = "product_num")
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_num")
    @NotNull
    private Order order;

    //기본생성자
    public OrderDetail() {
    }

    //getters and setters

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
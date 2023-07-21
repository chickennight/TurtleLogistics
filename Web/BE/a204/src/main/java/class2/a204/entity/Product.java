package class2.a204.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_num", nullable = false)
    private Integer productNum;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "stock")
    @NotNull
    private Integer stock;

    @Column(name = "detail")
    private String detail;

    @Column(name = "price")
    @NotNull
    private Integer price;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;
    //기본생성자
    public Product() {
    }

    //getters and setters

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
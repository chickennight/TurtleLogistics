package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @JsonProperty("product_num")
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

}
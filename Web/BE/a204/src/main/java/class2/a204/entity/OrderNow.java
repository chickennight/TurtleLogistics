package class2.a204.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ordernow")
public class OrderNow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_num")
    @NotNull
    private Order order;

    @Column(name = "status")
    @NotNull
    private Integer status;

    public OrderNow(Order order, Integer status){
        this.order = order;
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
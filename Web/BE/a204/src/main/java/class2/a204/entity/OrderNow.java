package class2.a204.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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
}
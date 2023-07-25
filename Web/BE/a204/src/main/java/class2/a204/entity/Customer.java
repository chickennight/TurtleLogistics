package class2.a204.entity;

import class2.a204.jwt.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "customer")
public class Customer {

    @JsonProperty("customer_num")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_num")
    @NotNull
    private Integer customerNum;

    @JsonProperty("customer_id")
    @Column(name = "customer_id",unique = true)
    @NotNull
    private String customerId;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "address")
    @NotNull
    private String address;

    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
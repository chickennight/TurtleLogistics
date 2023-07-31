package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
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

    //CustomerDTO에서 toEntity() 를 위한 생성자
    public Customer(String customerId, String password, String address, String phoneNumber){
        this.customerId = customerId;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    //비밀번호 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }



}
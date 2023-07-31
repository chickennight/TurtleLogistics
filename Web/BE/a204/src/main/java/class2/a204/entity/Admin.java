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
@Table(name = "admin")
public class Admin {
    @JsonProperty("admin_num")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_num")
    @NotNull
    private int adminNum;

    @JsonProperty("admin_id")
    @Column(name = "admin_id", unique = true)
    @NotNull
    private String adminId;

    @Column(name = "password")
    @NotNull
    private String password;

    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;

    //AdminDTO에서 toEntity() 를 위한 생성자
    public Admin(String adminId, String password, String phoneNumber) {
        this.adminId= adminId;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    //비밀번호 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }



}

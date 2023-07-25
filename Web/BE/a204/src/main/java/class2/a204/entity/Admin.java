package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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

}

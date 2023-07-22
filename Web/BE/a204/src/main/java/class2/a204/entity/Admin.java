package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {
    @JsonProperty("admin_id")
    @Id
    @Column(name = "admin_id")
    @NotNull
    private String adminId;

    @Column(name = "password")
    @NotNull
    private String password;

    @JsonProperty("phone_number")
    @Column(name = "phone_number")
    @NotNull
    private String phoneNumber;

    //기본생성자
    public Admin() {
    }



    //getters and setters

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
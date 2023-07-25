package class2.a204.entity;

import class2.a204.jwt.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
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

    //기본생성자
    public Customer() {
    }

    //getters and setters

    public Integer getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(Integer customerNum) {
        this.customerNum = customerNum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
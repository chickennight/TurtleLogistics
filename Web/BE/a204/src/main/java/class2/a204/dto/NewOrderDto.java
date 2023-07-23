package class2.a204.dto;

import class2.a204.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NewOrderDto {

    @JsonProperty("customer_num")
    private Integer customerNum;

    private List<Product> products;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "NewOrderDto{" +
                "customerNum=" + customerNum +
                ", products=" + products +
                ", address='" + address + '\'' +
                '}';
    }

    public NewOrderDto() {
    }

    public Integer getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(Integer customerNum) {
        this.customerNum = customerNum;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

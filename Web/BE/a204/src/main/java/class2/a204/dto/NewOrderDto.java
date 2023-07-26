package class2.a204.dto;

import class2.a204.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NewOrderDto {

    @JsonProperty("customer_num")
    private Integer customerNum;

    private List<Product> products;

    @JsonProperty("detail_address")
    private String detailAddress;

    private Integer address;

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

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }
}

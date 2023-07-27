package class2.a204.dto;

import class2.a204.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderDto {

    @JsonProperty("customer_num")
    private Integer customerNum;

    private List<Product> products;

    @JsonProperty("detail_address")
    private String detailAddress;

    private Integer address;


}

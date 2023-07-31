package class2.a204.dto;

import class2.a204.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderDTO {

    @JsonProperty("customer_num")
    private Integer customerNum;

    private List<Product> products;

    @JsonProperty("detail_address")
    private String detailAddress;

    private Integer address;


}

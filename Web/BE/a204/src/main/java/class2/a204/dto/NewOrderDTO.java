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

    @JsonProperty("customer_id")
    private String customerId;

    private List<Product> products;

    @JsonProperty("detail_address")
    private String detailAddress;

    private Integer address;


}

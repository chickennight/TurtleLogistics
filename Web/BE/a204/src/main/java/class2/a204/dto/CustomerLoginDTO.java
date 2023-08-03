package class2.a204.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoginDTO {
    @ApiModelProperty(value = "아이디", required = true, example = "Customer")
    @JsonProperty("customer_id")
    private String customerId;
    @ApiModelProperty(value = "비밀번호", required = true, example = "Customer")
    private String password;
}

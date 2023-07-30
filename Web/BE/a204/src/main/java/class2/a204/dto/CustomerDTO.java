package class2.a204.dto;

import class2.a204.entity.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDTO {
    @ApiModelProperty(value = "사용자 아이디", required = true, example = "Customer")
    private String customerId;
    @ApiModelProperty(value = "사용자 비밀번호", required = true, example = "Customer")
    private String password;
    @ApiModelProperty(value = "사용자 주소", required = true, example = "임시주소")
    private String address;
    @ApiModelProperty(value = "사용자 전화번호", required = true, example = "01012341234")
    private String phoneNumber;

    public Customer toEntity(){
        return new Customer(this.customerId, this.password, this.address, this.phoneNumber);
    }



}

package class2.a204.dto;

import class2.a204.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDTO {
    private String customerId;
    private String password;
    private String address;
    private String phoneNumber;

    public Customer toEntity(){
        return new Customer(this.customerId, this.password, this.address, this.phoneNumber);
    }



}

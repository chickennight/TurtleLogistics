package class2.a204.dto;

import class2.a204.entity.Admin;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AdminDTO {
    private String adminId;
    private String password;
    private String phoneNumber;

    public Admin toEntity() {
        return new Admin(this.adminId, this.password, this.phoneNumber);
    }


}

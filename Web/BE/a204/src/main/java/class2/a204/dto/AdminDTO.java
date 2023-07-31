package class2.a204.dto;

import class2.a204.entity.Admin;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AdminDTO {
    @ApiModelProperty(value = "관리자 아이디", required = true, example = "Admin")
    @JsonProperty("admin_id")
    private String adminId;

    @ApiModelProperty(value = "관리자 비밀번호", required = true, example = "Admin")
    private String password;

    @ApiModelProperty(value = "관리자 전화번호", required = true, example = "01011111111")
    @JsonProperty("phone_number")
    private String phoneNumber;

    public Admin toEntity() {
        return new Admin(this.adminId, this.password, this.phoneNumber);
    }


}

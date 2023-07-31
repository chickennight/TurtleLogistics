package class2.a204.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDTO {
    @ApiModelProperty(value = "아이디", required = true, example = "Admin")
    @JsonProperty("admin_id")
    private String adminId;
    @ApiModelProperty(value = "비밀번호", required = true, example = "Admin")
    private String password;
}

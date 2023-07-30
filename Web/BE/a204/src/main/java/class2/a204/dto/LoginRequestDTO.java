package class2.a204.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {
    @ApiModelProperty(value = "아이디", required = true, example = "Admin")
    private String id;
    @ApiModelProperty(value = "비밀번호", required = true, example = "Admin")
    private String password;
}

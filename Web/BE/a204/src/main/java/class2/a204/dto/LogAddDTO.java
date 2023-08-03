package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogAddDTO {

    @JsonProperty("machine_id")
    @ApiModelProperty(value = "기기ID", required = true, example = "1001")
    private Integer machineId;
    @ApiModelProperty(value = "기기상태", required = true, example = "1")
    private Integer type;

}

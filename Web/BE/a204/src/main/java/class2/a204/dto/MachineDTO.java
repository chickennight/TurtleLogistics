package class2.a204.dto;

import class2.a204.entity.Machine;
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
public class MachineDTO {
    @ApiModelProperty(value = "기기명", required = true, example = "피스톤1")
    @JsonProperty("machine_detail")
    private String machineDetail;
    @ApiModelProperty(value = "고장남", required = true, example = "true")
    private Boolean broken;


    public Machine toEntity(){
        return new Machine(this.machineDetail, this.broken);
    }
}

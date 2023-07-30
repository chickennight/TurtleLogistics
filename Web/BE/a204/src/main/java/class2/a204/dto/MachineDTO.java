package class2.a204.dto;

import class2.a204.entity.Machine;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MachineDTO {
    @ApiModelProperty(value = "기기명", required = true, example = "피스톤1")
    private String machineDetail;
    @ApiModelProperty(value = "고장남", required = true, example = "true")
    private Boolean broken;


    public Machine toEntity(){
        return new Machine(this.machineDetail, this.broken);
    }
}
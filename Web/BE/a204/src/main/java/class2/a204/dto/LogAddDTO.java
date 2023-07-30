package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class LogAddDTO {

    @JsonProperty("machine_id")
    private Integer machineId;

    private Integer type;

}

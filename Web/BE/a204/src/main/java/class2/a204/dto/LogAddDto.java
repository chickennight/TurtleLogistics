package class2.a204.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAddDto {

    @JsonProperty("machine_id")
    private Integer machineId;

    private Integer type;

}

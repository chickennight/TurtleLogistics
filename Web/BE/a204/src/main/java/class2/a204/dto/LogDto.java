package class2.a204.dto;

import class2.a204.entity.Log;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDto {
    @JsonProperty("log_num")
    private Integer logNum;
    @JsonProperty("error_date")
    private String errorDate;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("machine_id")
    private Integer machineId;

    public LogDto(Log l) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.logNum = l.getLogNum();
        this.errorDate = l.getErrorDate().format(formatter);
        this.errorMessage = l.getErrorMessage();
        this.machineId = l.getMachine().getMachineId();
    }
}

package class2.a204.dto;

import class2.a204.entity.Log;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.format.DateTimeFormatter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO {
    @JsonProperty("log_num")
    private Integer logNum;
    @JsonProperty("error_date")
    private String errorDate;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("machine_id")
    private Integer machineId;
    private Boolean recorded;

    public LogDTO(Log l) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.logNum = l.getLogNum();
        this.errorDate = l.getErrorDate().format(formatter);
        this.errorMessage = l.getErrorMessage();
        this.machineId = l.getMachine().getMachineId();
        this.recorded = l.getRecorded();
    }
}

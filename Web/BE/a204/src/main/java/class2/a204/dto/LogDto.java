package class2.a204.dto;

import class2.a204.entity.Log;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.format.DateTimeFormatter;


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

    public LogDto() {
    }

    public Integer getLogNum() {
        return logNum;
    }

    public void setLogNum(Integer logNum) {
        this.logNum = logNum;
    }

    public String getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(String errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }
}

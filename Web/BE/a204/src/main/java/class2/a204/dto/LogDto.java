package class2.a204.dto;

import class2.a204.entity.Log;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class LogDto {
    private Integer logNum;
    private String errorDate;
    private String errorMessage;
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

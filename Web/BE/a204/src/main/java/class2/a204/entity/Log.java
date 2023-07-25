package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log")
public class Log {
    @JsonProperty("log_num")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_num")
    @NotNull
    private Integer logNum;

    @JsonProperty("error_date")
    @Column(name = "error_date", insertable = false)
    @NotNull
    private LocalDateTime errorDate;

    @JsonProperty("error_message")
    @Column(name = "error_message")
    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    @NotNull
    private Machine machine;

    //기본생성자
    public Log() {
    }

    public Log(Machine machine2, String logForBrokenMachine) {
    }

    //getters and setters

    public Integer getLogNum() {
        return logNum;
    }

    public void setLogNum(Integer logNum) {
        this.logNum = logNum;
    }

    public LocalDateTime getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(LocalDateTime errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}
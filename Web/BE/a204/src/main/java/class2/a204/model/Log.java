package class2.a204.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_num")
    @NotNull
    private Integer logNum;

    @Column(name = "error_date")
    @NotNull
    private Timestamp errorDate;

    @Column(name = "error_message")
    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    @NotNull
    private Machine machine;
    //기본생성자
    public Log() {
    }

    //getters and setters

    public Integer getLogNum() {
        return logNum;
    }

    public void setLogNum(Integer logNum) {
        this.logNum = logNum;
    }

    public Timestamp getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(Timestamp errorDate) {
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
package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    @NotNull
    private Machine machine;

//    public Log(Machine machine2, String logForBrokenMachine) {
//    }

    public void updateMachine(Machine machine) {
        this.machine = machine;
    }

    public void updateErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
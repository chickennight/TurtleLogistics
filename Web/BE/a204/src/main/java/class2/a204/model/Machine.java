package class2.a204.model;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "machine")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    @NotNull
    private Integer machineId;

    @Column(name = "machine_detail")
    @NotNull
    private String machineDetail;

    @Column(name = "broken")
    private Boolean broken;

    @OneToMany(mappedBy = "machine")
    private List<Log> logs;

    //기본생성자
    public Machine() {
    }

    //getters and setters

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getMachineDetail() {
        return machineDetail;
    }

    public void setMachineDetail(String machineDetail) {
        this.machineDetail = machineDetail;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}
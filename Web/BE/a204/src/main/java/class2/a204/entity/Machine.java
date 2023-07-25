package class2.a204.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "machine")
public class Machine {

    @JsonProperty("machine_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    @NotNull
    private Integer machineId;

    @JsonProperty("machine_detail")
    @Column(name = "machine_detail")
    @NotNull
    private String machineDetail;

    @Column(name = "broken")
    private Boolean broken;

    @OneToMany(mappedBy = "machine")
    @JsonIgnore
    private List<Log> logs;

    public Machine(Integer machineId, String machineDetail, Boolean broken, List<Log> logs) {
        this.machineId = machineId;
        this.machineDetail = machineDetail;
        this.broken = broken;
        this.logs = logs;
    }

    public Machine(Integer machineId, String machineDetail, Boolean broken) {
        this.machineId = machineId;
        this.machineDetail = machineDetail;
        this.broken = broken;
    }

    public Machine(Integer machineId, String machineDetail) {
        this.machineId = machineId;
        this.machineDetail = machineDetail;
    }

    public Machine(Boolean broken) {
        this.broken = broken;
    }

}
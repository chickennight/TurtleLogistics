package class2.a204.model;

import javax.persistence.*;

@Entity
@Table(name = "machine")
public class Machine {
    @Id
    @Column(name = "machine_id")
    private Integer id;

    @Column(name = "machine_detail")
    private String detail;

    private Boolean broken = false;

    //기본생성자
    public Machine() {
    }

    //getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }
}
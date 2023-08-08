package class2.a204.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer id;

    @OneToOne
    @JoinColumn(name = "log_num")
    private Log log;

    private String contentType;

    public Image(Log log, String contentType) {
        this.log = log;
        this.contentType = contentType;
    }
}

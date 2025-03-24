package uz.asadbek.course.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.course.model.TestOption;


@Entity
@Table(name = "Tests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private String title;

    @ManyToOne
    @JoinColumn(name = "science_id")
    private Science science;

    private int duration;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

}

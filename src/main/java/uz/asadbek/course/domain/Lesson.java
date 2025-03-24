package uz.asadbek.course.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Lessons")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Lesson {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer number;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String videoLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "lesson")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "lesson")
    private Set<Test> tests;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonProgress> lessonProgress;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<LessonFile> extraMaterials;

}

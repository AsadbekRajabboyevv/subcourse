package uz.asadbek.course.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.course.model.DurationType;


@Entity
@Table(name = "Courses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Course {

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

    @Column(nullable = false,unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Integer duration;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private DurationType durationType;

    private Integer price;

    @ManyToMany
    @JoinTable(
            name = "UserCourses",
            joinColumns = @JoinColumn(name = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;

    @Column(nullable = false)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_grade_id")
    private CourseGrade courseGrade;

    @Column(nullable = false)
    private boolean hasCertificate;

    @Column(nullable = false)
    private boolean hasVideos;

    @Column(nullable = false)
    private boolean hasTests;

    @Column(nullable = false)
    private boolean hasExtraMaterials;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "science_id")
    private Science science;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}

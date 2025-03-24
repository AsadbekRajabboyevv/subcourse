package uz.asadbek.course.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.asadbek.course.model.UserPositions;


@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

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
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column
    private String description;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column
    @Enumerated(EnumType.STRING)
    private UserPositions position;

    @ManyToMany(mappedBy = "users")
    private Set<Course> courses;

    @OneToMany(mappedBy = "owner")
    private Set<Course> ownerCourses;

    @OneToMany(mappedBy = "owner")
    private Set<Comment> comments;


    @Column(nullable = false)
    private boolean enabled;

    @Column(unique = true)
    private String confirmationToken;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column
    private String phone;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}

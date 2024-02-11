package org.once_a_day.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "sso_id", unique = true)
    private UUID ssoId;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "profile_picture_id")
    FileDetails avatar;

    @OneToMany(mappedBy = "user")
    private Set<LabelWeight> weights;

    @OneToMany(mappedBy = "user")
    private Set<FileDetails> files;

    @OneToMany
    private Set<User> matches;
}

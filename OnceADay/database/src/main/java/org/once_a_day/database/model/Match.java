package org.once_a_day.database.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "matches", uniqueConstraints = { @UniqueConstraint(name = "UniqueNumberAndStatus", columnNames = { "user_one_id", "user_two_id", "active" })})
@NoArgsConstructor
@AllArgsConstructor
public class Match extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_one_id")
    private User userOne;

    @ManyToOne
    @JoinColumn(name = "user_two_id")
    private User userTwo;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdAt;
}

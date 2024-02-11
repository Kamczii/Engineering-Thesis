package org.once_a_day.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@Table(name = "label_weights")
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "active = true")
public class LabelWeight extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "label_weight_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false, updatable = false)
    RekognitionLabel label;

    @Column(name = "weight")
    private Long weight;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDate updatedAt;

    @Column(name = "active", nullable = false)
    private boolean active = Boolean.TRUE;
}

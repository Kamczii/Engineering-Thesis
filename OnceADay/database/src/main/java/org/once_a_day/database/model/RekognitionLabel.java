package org.once_a_day.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.once_a_day.database.enums.AvailableLabel;

@Entity
@Table(name = "rekognition_labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RekognitionLabel extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "value", nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailableLabel value;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private LabelCategory category;
}

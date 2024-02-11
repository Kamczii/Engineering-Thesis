package org.once_a_day.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.once_a_day.database.enums.AvailableLabelCategory;

@Entity
@Table(name = "label_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelCategory extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "value", nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailableLabelCategory value;
}

package org.once_a_day.repository;

import org.once_a_day.database.model.LabelWeight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelWeightRepository extends JpaRepository<LabelWeight, Long> {
    Optional<LabelWeight> findByUserIdAndLabelId(Long userId, Long labelId);
}

package org.once_a_day.repository;

import org.once_a_day.database.model.RekognitionLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RekognitionLabelRepository extends JpaRepository<RekognitionLabel, Long> {
    Optional<RekognitionLabel> findByLabel(String label);
}

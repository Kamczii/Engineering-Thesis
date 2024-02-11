package org.once_a_day.repository;

import org.once_a_day.database.model.LabelCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelCategoryRepository extends JpaRepository<LabelCategory, Long> {
    Optional<LabelCategory> findByLabel(String label);
}

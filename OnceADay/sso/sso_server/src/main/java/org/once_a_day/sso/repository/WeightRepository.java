package org.once_a_day.sso.repository;


import org.once_a_day.database.model.LabelWeight;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRepository extends JpaRepository<LabelWeight, Long> {

    List<LabelWeight> findAllByUserIdAndLabelActiveTrueOrderByWeightDesc(Long id);
//    List<LabelWeight> findAllByLabelActiveTrueUserIdOrderByWeightDesc(Long id, final Pageable pageable);

}

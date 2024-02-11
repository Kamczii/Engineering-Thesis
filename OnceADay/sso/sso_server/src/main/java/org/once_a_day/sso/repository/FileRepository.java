package org.once_a_day.sso.repository;


import org.once_a_day.database.enums.FileType;
import org.once_a_day.database.model.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileDetails, Long> {
    List<FileDetails> findAllByUserIdAndType(Long userId, FileType fileType);
}

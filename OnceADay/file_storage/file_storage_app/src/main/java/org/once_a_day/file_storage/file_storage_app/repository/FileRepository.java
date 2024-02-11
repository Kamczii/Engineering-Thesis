package org.once_a_day.file_storage.file_storage_app.repository;

import org.once_a_day.database.enums.FileType;
import org.once_a_day.database.model.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileDetails, Long> {
    Optional<FileDetails> findByIdAndTypeIn(Long fileId, FileType ...types);
}

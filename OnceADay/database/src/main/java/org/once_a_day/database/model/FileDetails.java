package org.once_a_day.database.model;


import jakarta.persistence.*;
import lombok.*;
import org.once_a_day.database.enums.FileType;

@Entity
@Getter
@Setter
@Builder
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails extends AbstractEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "file_id")
    Long id;

    @Column(name = "file_name")
    String fileName;

    @Column(name = "bucket")
    String bucket;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    FileType type;
}


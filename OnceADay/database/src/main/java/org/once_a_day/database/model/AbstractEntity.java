package org.once_a_day.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@MappedSuperclass
@SQLDelete(sql = "UPDATE table_product SET active = true WHERE id=?")
@Where(clause = "active=false")
public abstract class AbstractEntity implements Serializable {
    @Column(name = "active", nullable = false)
    private boolean active = Boolean.TRUE;
}

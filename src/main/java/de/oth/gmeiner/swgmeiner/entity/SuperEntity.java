package de.oth.gmeiner.swgmeiner.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SuperEntity implements Serializable {

    private static final long serialVersionUID = 0L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public SuperEntity() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SuperEntity other = (SuperEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " (id=" + id + ')';
    }

}

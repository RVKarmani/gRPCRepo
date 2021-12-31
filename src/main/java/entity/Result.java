package entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
public class Result {
    @Id
    private String studentId;

    private String maths;
    private String art;
    private String chemistry;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Result result = (Result) o;
        return studentId != null && Objects.equals(studentId, result.studentId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

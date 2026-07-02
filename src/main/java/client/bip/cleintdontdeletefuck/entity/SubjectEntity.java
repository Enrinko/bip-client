package client.bip.cleintdontdeletefuck.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectEntity {
    private Long id;
    private String subjectName;

    public SubjectEntity(Long id, String subjectName) {
        this.id = id;
        this.subjectName = subjectName;
    }

    public SubjectEntity(String subjectName) {
        this.subjectName = subjectName;
    }
}

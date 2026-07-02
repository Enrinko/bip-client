package client.bip.cleintdontdeletefuck.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinalLoadEntity {
    private TeacherEntity teacherInLoad;
    private SubjectEntity subjectInLoad;
    private GroupEntity groupInLoad;
    private Integer examHours;
    private Integer diplomaHours;
    private Integer consultationHours;
    private Integer courseworkHours;
    private Integer semesterFirstHours;
    private Integer semesterSecondHours;

    public FinalLoadEntity(TeacherEntity teacherInLoad, SubjectEntity subject, GroupEntity group, Integer exam, Integer diploma, Integer consultation, Integer coursework, Integer semesterFirst, Integer semesterSecond) {
        this.subjectInLoad = subject;
        this.groupInLoad = group;
        this.semesterFirstHours = semesterFirst;
        this.semesterSecondHours = semesterSecond;
        this.teacherInLoad = teacherInLoad;
        this.examHours = exam;
        this.diplomaHours = diploma;
        this.consultationHours = consultation;
        this.courseworkHours = coursework;
    }
}

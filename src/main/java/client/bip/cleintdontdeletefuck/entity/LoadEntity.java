package client.bip.cleintdontdeletefuck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadEntity {
    private Long id;
    private Integer semesterFirstHours;
    private Integer semesterSecondHours;
    private Integer examHours;
    private Integer courseworkHours;
    private Integer diplomaHours;
    private Integer consultationHours;
    private String teacherInLoad;
    private String subjectInLoad;
    private String groupInLoad;

    public LoadEntity(Integer semesterFirstHours, Integer semesterSecondHours, Integer examHours, Integer courseworkHours, Integer diplomaHours, Integer consultationHours, String initials, String subjectName, String groupName) {
        this.semesterFirstHours = semesterFirstHours;
        this.semesterSecondHours = semesterSecondHours;
        this.examHours = examHours;
        this.courseworkHours = courseworkHours;
        this.diplomaHours = diplomaHours;
        this.consultationHours = consultationHours;
        this.teacherInLoad = initials;
        this.subjectInLoad = subjectName;
        this.groupInLoad = groupName;
    }

    public List<LoadEntity> listOfAllSubjectsThisTeacher(List<LoadEntity> workload) {
        List<LoadEntity> loads = new ArrayList<>();
        for (LoadEntity load : workload) if (this.teacherInLoad.equals(load.teacherInLoad)) loads.add(load);
        return loads;
    }

    public Integer totalHours(List<Integer> semesters) {
        return semesters.get(0) + semesters.get(1) + semesters.get(2) + semesters.get(3) + semesters.get(4) + semesters.get(5);
    }

    public List<Integer> semesterHours(List<LoadEntity> loads) {
        List<Integer> allNumbers = new ArrayList<>();
        allNumbers.add(0);
        allNumbers.add(0);
        allNumbers.add(0);
        allNumbers.add(0);
        allNumbers.add(0);
        allNumbers.add(0);
        for (LoadEntity load : loads) {
            allNumbers.set(0, allNumbers.get(0) + load.semesterFirstHours);
            allNumbers.set(1, allNumbers.get(1) + load.diplomaHours);
            allNumbers.set(2, allNumbers.get(2) + load.semesterSecondHours);
            allNumbers.set(3, allNumbers.get(3) + load.courseworkHours);
            allNumbers.set(4, allNumbers.get(4) + load.consultationHours);
            allNumbers.set(5, allNumbers.get(5) + load.examHours);
        }
        return allNumbers;
    }
}

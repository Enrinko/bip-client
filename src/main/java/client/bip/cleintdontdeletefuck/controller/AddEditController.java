package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.entity.GroupEntity;
import client.bip.cleintdontdeletefuck.entity.LoadEntity;
import client.bip.cleintdontdeletefuck.entity.SubjectEntity;
import client.bip.cleintdontdeletefuck.entity.TeacherEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

import static client.bip.cleintdontdeletefuck.ClientApplication.errorMessage;
import static client.bip.cleintdontdeletefuck.ClientApplication.infoMessage;
import static client.bip.cleintdontdeletefuck.controller.MainController.*;

public class AddEditController {
    @FXML
    private Button cancelButton;
    @FXML
    private Label changeTheLabel;
    @FXML
    private TextField consultationsInput;
    @FXML
    private TextField courseworkInput;
    @FXML
    private TextField diplomaInput;
    @FXML
    private TextField examInput;
    @FXML
    private TextField firstSemseterInput;
    @FXML
    private ComboBox<String> groupComboBox;
    @FXML
    private TextField changingInputForSmallTables;
    @FXML
    private AnchorPane loadAnchorPane;
    @FXML
    private TextField secondSemesterInput;
    @FXML
    private ComboBox<String> subjectComboBox;
    @FXML
    private ComboBox<String> teacherComboBox;
    @FXML
    private GridPane smallTablesGridPane;

    @FXML
    void initialize() {
        switch (whatTableIsSelected) {
            case "groups" -> {
                loadAnchorPane.setVisible(false);
                smallTablesGridPane.setVisible(true);
                if ("".equals(selectedFromComboBoxSelectTable)) break;
                changeTheLabel.setText("Введите название группы");
                changingInputForSmallTables.setText(selectedFromComboBoxSelectTable);
            }
            case "loads" -> {
                loadAnchorPane.setVisible(true);
                smallTablesGridPane.setVisible(false);
                if (selectedFromLoadTable == null) break;
                consultationsInput.setText(selectedFromLoadTable.getConsultationHours().toString());
                courseworkInput.setText(selectedFromLoadTable.getCourseworkHours().toString());
                examInput.setText(selectedFromLoadTable.getExamHours().toString());
                diplomaInput.setText(selectedFromLoadTable.getDiplomaHours().toString());
                firstSemseterInput.setText(selectedFromLoadTable.getSemesterFirstHours().toString());
                secondSemesterInput.setText(selectedFromLoadTable.getSemesterSecondHours().toString());
                teacherComboBox.setValue(selectedFromLoadTable.getTeacherInLoad());
                subjectComboBox.setValue(selectedFromLoadTable.getSubjectInLoad());
                groupComboBox.setValue(selectedFromLoadTable.getGroupInLoad());
            }
            case "subjects" -> {
                loadAnchorPane.setVisible(false);
                smallTablesGridPane.setVisible(true);
                if ("".equals(selectedFromComboBoxSelectTable)) break;
                changeTheLabel.setText("Введите название предмета");
                changingInputForSmallTables.setText(selectedFromComboBoxSelectTable);
            }
            case "teachers" -> {
                loadAnchorPane.setVisible(false);
                smallTablesGridPane.setVisible(true);
                if ("".equals(selectedFromComboBoxSelectTable)) break;
                changeTheLabel.setText("Введите инициалы учителя");
                changingInputForSmallTables.setText(selectedFromComboBoxSelectTable);
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        try {
            switch (whatTableIsSelected) {
                case "groups" -> {
                    GroupEntity group = new GroupEntity();
                    group.setId(forSelectedComboBoxTable);
                    group.setGroupName(changingInputForSmallTables.getText());
                    if (!"".equals(selectedFromComboBoxSelectTable)) {
                        System.out.println(http.put(api.concat("groups/edit"), gson.toJson(group, GroupEntity.class)));
                        infoMessage("Запись успешно обновлена!");
                        return;
                    }
                    http.post(api.concat("groups/add"), gson.toJson(group, GroupEntity.class));
                    infoMessage("Запись успешно добавлена!");
                    selectedFromComboBoxSelectTable = group.getGroupName();
                }
                case "loads" -> {
                    LoadEntity load = new LoadEntity(Integer.parseInt(firstSemseterInput.getText()), Integer.parseInt(secondSemesterInput.getText()), Integer.parseInt(examInput.getText()), Integer.parseInt(courseworkInput.getText()), Integer.parseInt(diplomaInput.getText()), Integer.parseInt(consultationsInput.getText()), teacherComboBox.getSelectionModel().getSelectedItem(), subjectComboBox.getSelectionModel().getSelectedItem(), groupComboBox.getSelectionModel().getSelectedItem());
                    if (selectedFromLoadTable != null) {
                        http.put(api.concat("loads/edit"), gson.toJson(load, LoadEntity.class));
                        infoMessage("Запись успешно обновлена!");
                        return;
                    }
                    http.post(api.concat("loads/add"), gson.toJson(load, LoadEntity.class));
                    infoMessage("Запись успешно добавлена!");
                    selectedFromLoadTable = load;
                }
                case "subjects" -> {
                    SubjectEntity subject = forSelectedComboBoxTable == -1 ? new SubjectEntity(changingInputForSmallTables.getText()) : new SubjectEntity(forSelectedComboBoxTable, changingInputForSmallTables.getText());
                    if (!"".equals(selectedFromComboBoxSelectTable)) {
                        http.put(api.concat("subjects/edit"), gson.toJson(subject, SubjectEntity.class));
                        infoMessage("Запись успешно обновлена!");
                        return;
                    }
                    http.post(api.concat("subjects/add"), gson.toJson(subject, SubjectEntity.class));
                    infoMessage("Запись успешно добавлена!");
                    selectedFromComboBoxSelectTable = subject.getSubjectName();
                }
                case "teachers" -> {
                    TeacherEntity teachers = forSelectedComboBoxTable == -1 ? new TeacherEntity(changingInputForSmallTables.getText()) : new TeacherEntity(forSelectedComboBoxTable, changingInputForSmallTables.getText());
                    if (!"".equals(selectedFromComboBoxSelectTable)) {
                        http.put(api.concat("teachers/edit"), gson.toJson(teachers, TeacherEntity.class));
                        infoMessage("Запись успешно обновлена!");
                        return;
                    }
                    http.post(api.concat("teachers/add"), gson.toJson(teachers, TeacherEntity.class));
                    infoMessage("Запись успешно добавлена!");
                    selectedFromComboBoxSelectTable = teachers.getInitials();
                }
            }
        } catch (IOException e) {
            errorMessage("Сервер выключен. Пожалуйста, попробуйте позже!");
        }
    }
}
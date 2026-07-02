package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.ClientApplication;
import client.bip.cleintdontdeletefuck.entity.GroupEntity;
import client.bip.cleintdontdeletefuck.entity.LoadEntity;
import client.bip.cleintdontdeletefuck.entity.SubjectEntity;
import client.bip.cleintdontdeletefuck.entity.TeacherEntity;
import client.bip.cleintdontdeletefuck.utils.HTTPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import static client.bip.cleintdontdeletefuck.ClientApplication.*;
import static client.bip.cleintdontdeletefuck.controller.LogicController.*;

public class MainController {
    public static String fileDirectory = "D:\\bipApi\\cleintDONTDELETEFUCK\\src\\workload.xlsx";
    public static String api = "http://localhost:28242/bipapi/";
    public static Gson gson = new Gson();
    public static HTTPUtils http = new HTTPUtils();
    public static String whatNeedToSum = "111111";
    public static ObservableList<String> toComboBox = FXCollections.observableArrayList();
    public static ObservableList<SubjectEntity> subjectComboBox = FXCollections.observableArrayList();
    public static ObservableList<TeacherEntity> teachersComboBox = FXCollections.observableArrayList();
    public static ObservableList<GroupEntity> groupsComboBox = FXCollections.observableArrayList();
    public static ObservableList<LoadEntity> loadEntityTableList = FXCollections.observableArrayList();
    public static String whatTableIsSelected = "";
    public static String whatTeacherNeedToCount = "";
    public static String whatGroupNeedToCount = "";
    public static String whatSubjectNeedToCount = "";
    public static String selectedFromComboBoxSelectTable = "";
    public static LoadEntity selectedFromLoadTable;
    public static long forSelectedComboBoxTable = -1;
    @FXML
    private ComboBox<String> SubjectComboBox;
    @FXML
    private ComboBox<String> comboBoxSelectTable;
    @FXML
    private TableColumn<LoadEntity, Integer> consultationsColumn;
    @FXML
    private TableColumn<LoadEntity, Integer> courseworkColumn;
    @FXML
    private TableColumn<LoadEntity, Integer> diplomaColumn;
    @FXML
    private TableColumn<LoadEntity, Integer> examColumn;
    @FXML
    private AnchorPane finalLoadProperties;
    @FXML
    private TableColumn<LoadEntity, String> groupColumn;
    @FXML
    private ComboBox<String> groupComboBox;
    @FXML
    private TableView<LoadEntity> loadTable;
    @FXML
    private TableColumn<LoadEntity, Integer> semesterFirstColumn;
    @FXML
    private TableColumn<LoadEntity, Integer> semesterSecondColumn;
    @FXML
    private GridPane smallTablesInside;
    @FXML
    private TableColumn<LoadEntity, String> subjectColumn;
    @FXML
    private GridPane tablesGridPane;
    @FXML
    private TableColumn<LoadEntity, String> teacherColumn;
    @FXML
    private ComboBox<String> teacherComboBox;
    @FXML
    private CheckBox withoutConsultations;
    @FXML
    private CheckBox withoutCoursework;
    @FXML
    private CheckBox withoutDiploma;
    @FXML
    private CheckBox withoutExam;
    @FXML
    private CheckBox withoutFirstSemester;
    @FXML
    private CheckBox withoutSecondSemester;

    @FXML
    void addButton(ActionEvent event) {
        comboBoxSelectTable.getSelectionModel().clearSelection();
        loadTable.getSelectionModel().clearSelection();
        showPersonEditDialog("addEdit.fxml", "Добавление записи");
        if ("groups".equals(whatTableIsSelected) || "subjects".equals(whatTableIsSelected) || "teachers".equals(whatTableIsSelected)) {
            initializeComboboxSelectTable();
            return;
        }
        initializeLoadTable();
    }

    @FXML
    void changeFinalEdit(ActionEvent event) {
        whatNeedToSum = "";
        if (withoutDiploma.isSelected()) whatNeedToSum = whatNeedToSum.concat("1");
        else whatNeedToSum = whatNeedToSum.concat("0");
        if (withoutConsultations.isSelected()) whatNeedToSum = whatNeedToSum.concat("1");
        else whatNeedToSum = whatNeedToSum.concat("0");
        if (withoutExam.isSelected()) whatNeedToSum = whatNeedToSum.concat("1");
        else whatNeedToSum = whatNeedToSum.concat("0");
        if (withoutCoursework.isSelected()) whatNeedToSum = whatNeedToSum.concat("1");
        else whatNeedToSum = whatNeedToSum.concat("0");
        if (withoutFirstSemester.isSelected()) whatNeedToSum = whatNeedToSum.concat("1");
        else whatNeedToSum = whatNeedToSum.concat("0");
        if (withoutSecondSemester.isSelected()) whatNeedToSum = whatNeedToSum.concat("1");
        else whatNeedToSum = whatNeedToSum.concat("0");
    }

    @FXML
    void deleteButton(ActionEvent event) {
        try {
            switch (whatTableIsSelected) {
                case "groups" -> {
                    if (comboBoxSelectTable.getSelectionModel().isEmpty()) {
                        warningMessage("Для удаления нужно выбрать запись!");
                        return;
                    }
                    for (GroupEntity groups : groupsComboBox)
                        if (groups.getGroupName().equals(comboBoxSelectTable.getSelectionModel().getSelectedItem())) {
                            http.delete(api, "groups/delete".concat(groups.getId().toString()));
                            groupsComboBox.clear();
                            initializeComboboxSelectTable();
                            return;
                        }
                }
                case "loads" -> {
                    if (loadTable.getSelectionModel().isEmpty()) {
                        warningMessage("Для удаления нужно выбрать запись!");
                        return;
                    }
                    http.delete(api, "loads/delete".concat(loadTable.getSelectionModel().getSelectedItem().getId().toString()));
                    loadEntityTableList.clear();
                    initializeLoadTable();
                }
                case "subjects" -> {
                    if (comboBoxSelectTable.getSelectionModel().isEmpty()) {
                        warningMessage("Для удаления нужно выбрать запись!");
                        return;
                    }
                    for (SubjectEntity subject : subjectComboBox)
                        if (subject.getSubjectName().equals(comboBoxSelectTable.getSelectionModel().getSelectedItem())) {
                            http.delete(api, "subjects/delete".concat(subject.getId().toString()));
                            subjectComboBox.clear();
                            initializeComboboxSelectTable();
                            return;
                        }
                }
                case "teachers" -> {
                    if (comboBoxSelectTable.getSelectionModel().isEmpty()) {
                        warningMessage("Для удаления нужно выбрать запись!");
                        return;
                    }
                    for (TeacherEntity teacher : teachersComboBox)
                        if (teacher.getInitials().equals(comboBoxSelectTable.getSelectionModel().getSelectedItem())) {
                            http.delete(api, "teachers/delete".concat(teacher.getId().toString()));
                            teachersComboBox.clear();
                            initializeComboboxSelectTable();
                            return;
                        }
                }
            }
        } catch (IOException e) {
            errorMessage("Сервер выключен. Пожалуйста, попробуйте позже!");
        }
    }

    @FXML
    void editButton(ActionEvent event) {
        if (comboBoxSelectTable.getSelectionModel().isEmpty() && loadTable.getSelectionModel().isEmpty()) {
            warningMessage("Для редактирования нужно выбрать запись!");
            return;
        }
        switch (whatTableIsSelected) {
            case "groups" -> {
                selectedFromComboBoxSelectTable = comboBoxSelectTable.getSelectionModel().getSelectedItem();
                for (GroupEntity group : groupsComboBox)
                    if (selectedFromComboBoxSelectTable.equals(group.getGroupName()))
                        forSelectedComboBoxTable = group.getId();
            }
            case "loads" -> selectedFromLoadTable = loadTable.getSelectionModel().getSelectedItem();
            case "subjects" -> {
                selectedFromComboBoxSelectTable = comboBoxSelectTable.getSelectionModel().getSelectedItem();
                for (SubjectEntity subject : subjectComboBox)
                    if (selectedFromComboBoxSelectTable.equals(subject.getSubjectName()))
                        forSelectedComboBoxTable = subject.getId();
            }
            case "teachers" -> {
                selectedFromComboBoxSelectTable = comboBoxSelectTable.getSelectionModel().getSelectedItem();
                for (TeacherEntity teacher : teachersComboBox)
                    if (selectedFromComboBoxSelectTable.equals(teacher.getInitials()))
                        forSelectedComboBoxTable = teacher.getId();
            }
        }
        showPersonEditDialog("addEdit.fxml", "Редактирование записи");
        if (whatTableIsSelected.equals("groups") || whatTableIsSelected.equals("subjects") || whatTableIsSelected.equals("teachers")) {
            comboBoxSelectTable.getSelectionModel().clearSelection();
            selectedFromComboBoxSelectTable = "";
            initializeComboboxSelectTable();
        } else {
            loadTable.getSelectionModel().clearSelection();
            selectedFromLoadTable = null;
            initializeLoadTable();
        }
    }

    @FXML
    void finalLoadOpenButton(ActionEvent event) {
        if (finalLoadProperties.isVisible()) {
            finalLoadProperties.setVisible(false);
            return;
        }
        initializeFinalLoadProperies();
        finalLoadProperties.setVisible(true);
    }

    private void initializeFinalLoadProperies() {
        toComboBox.clear();
        loadTable.setVisible(false);
        try {
            JsonObject baseTeachers = gson.fromJson(http.get(api, "teachers/all"), JsonObject.class);
            JsonArray dataTeacher = baseTeachers.getAsJsonArray("data");
            for (int i = 0; i < dataTeacher.size(); i++) {
                TeacherEntity teacher = gson.fromJson(dataTeacher.get(i).toString(), TeacherEntity.class);
                toComboBox.add(teacher.getInitials());
            }
        } catch (IOException e) {
            errorMessage("Сервер выключен. Пожалуйста, попробуйте позже!");
        }
        toComboBox.sort(Comparator.comparing(String::toString));
        teacherComboBox.getItems().setAll(toComboBox);
    }

    @FXML
    void workWithExcel(ActionEvent event) {
        String fileDirectoryFromUser = ClientApplication.dialogMessage(fileDirectory);
        if (!new File(fileDirectoryFromUser).exists()) {
            ClientApplication.errorMessage("Для считывания с файла он должен существовать!");
            return;
        }
        getValues(fileDirectoryFromUser);
        try {
            http.post(api.concat("fromexcel/conversion"), gson.toJson(convertToServerLoadEntity()));
            boolean answer = applyMessage("Хотите использовать заданные параметры для подсчёта нагрузки?");
            if (answer) {
                createSheet(fileDirectoryFromUser, "workloadForBip");
                whatTeacherNeedToCount = teacherComboBox.getSelectionModel().getSelectedItem();
                setValues(fileDirectoryFromUser, "workloadForBip", whatNeedToSum);
            } else {
                createSheet(fileDirectoryFromUser, "workloadForBip");
                whatTeacherNeedToCount = "";
                setValues(fileDirectoryFromUser, "workloadForBip", "111111");
            }
            initializeFinalLoadProperies();
        } catch (IOException e) {
            errorMessage("Сервер выключен. Пожалуйста, попробуйте позже!");
        }
    }

    @FXML
    void groupsOpenButton(ActionEvent event) {
        if (smallTablesInside.isVisible() && whatTableIsSelected.equals("groups")) {
            smallTablesInside.setVisible(false);
            comboBoxSelectTable.setVisible(false);
            return;
        }
        whatTableIsSelected = "groups";
        comboBoxSelectTable.setVisible(true);
        initializeComboboxSelectTable();
    }

    private void initializeComboboxSelectTable() {
        try {
            switch (whatTableIsSelected) {
                case "groups" -> {
                    toComboBox.clear();
                    comboBoxSelectTable.getEditor().clear();
                    smallTablesInside.setVisible(true);
                    JsonObject base = gson.fromJson(http.get(api, "groups/all"), JsonObject.class);
                    JsonArray dataArr = base.getAsJsonArray("data");
                    for (int i = 0; i < dataArr.size(); i++) {
                        GroupEntity subject = gson.fromJson(dataArr.get(i).toString(), GroupEntity.class);
                        groupsComboBox.add(subject);
                        toComboBox.add(subject.getGroupName());
                    }
                    toComboBox.sort(Comparator.comparing(String::toString));
                    comboBoxSelectTable.getItems().setAll(toComboBox);
                    toComboBox.clear();
                }
                case "teachers" -> {
                    JsonObject base = gson.fromJson(http.get(api, "teachers/all"), JsonObject.class);
                    JsonArray dataArr = base.getAsJsonArray("data");
                    for (int i = 0; i < dataArr.size(); i++) {
                        TeacherEntity teacher = gson.fromJson(dataArr.get(i).toString(), TeacherEntity.class);
                        toComboBox.add(teacher.getInitials());
                        teachersComboBox.add(teacher);
                    }
                    toComboBox.sort(Comparator.comparing(String::toString));
                    comboBoxSelectTable.getItems().setAll(toComboBox);
                    toComboBox.clear();
                }
                case "subjects" -> {
                    JsonObject base = gson.fromJson(http.get(api, "subjects/all"), JsonObject.class);
                    JsonArray dataArr = base.getAsJsonArray("data");
                    for (int i = 0; i < dataArr.size(); i++) {
                        SubjectEntity subject = gson.fromJson(dataArr.get(i).toString(), SubjectEntity.class);
                        toComboBox.add(subject.getSubjectName());
                        subjectComboBox.add(subject);
                    }
                    toComboBox.sort(Comparator.comparing(String::toString));
                    comboBoxSelectTable.getItems().setAll(toComboBox);
                    toComboBox.clear();
                }
            }
        } catch (IOException e) {
            errorMessage("Сервер выключен. Пожалуйста, попробуйте позже!");
        }
    }

    @FXML
    void loadOpenButton(ActionEvent event) {
        whatTableIsSelected = "loads";
        loadEntityTableList.clear();
        if (loadTable.isVisible()) {
            loadTable.setVisible(false);
            smallTablesInside.setVisible(false);
            return;
        }
        smallTablesInside.setVisible(true);
        loadTable.setVisible(true);
        initializeLoadTable();
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherInLoad"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectInLoad"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("groupInLoad"));
        semesterFirstColumn.setCellValueFactory(new PropertyValueFactory<>("semesterFirstHours"));
        semesterSecondColumn.setCellValueFactory(new PropertyValueFactory<>("semesterSecondHours"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examHours"));
        courseworkColumn.setCellValueFactory(new PropertyValueFactory<>("courseworkHours"));
        diplomaColumn.setCellValueFactory(new PropertyValueFactory<>("diplomaHours"));
        consultationsColumn.setCellValueFactory(new PropertyValueFactory<>("consultationHours"));
        loadTable.getItems().setAll(loadEntityTableList);
        loadEntityTableList.clear();
    }

    private void initializeLoadTable() {
        try {
            JsonObject base = gson.fromJson(http.get(api, "loads/all"), JsonObject.class);
            JsonArray dataArr = base.getAsJsonArray("data");
            for (int i = 0; i < dataArr.size(); i++) {
                LoadEntity subject = gson.fromJson(dataArr.get(i).toString(), LoadEntity.class);
                loadEntityTableList.add(subject);
            }
        } catch (IOException e) {
            errorMessage("Сервер выключен. Пожалуйста, попробуйте позже!");
        }
    }

    @FXML
    void subjectsOpenButton(ActionEvent event) {
        if (smallTablesInside.isVisible() && whatTableIsSelected.equals("subjects")) {
            smallTablesInside.setVisible(false);
            comboBoxSelectTable.setVisible(false);
            return;
        }
        whatTableIsSelected = "subjects";
        toComboBox.clear();
        comboBoxSelectTable.getEditor().clear();
        smallTablesInside.setVisible(true);
        comboBoxSelectTable.setVisible(true);
        initializeComboboxSelectTable();
    }

    @FXML
    void tablesOpenButton(ActionEvent event) {
        if (tablesGridPane.isVisible()) tablesGridPane.setVisible(false);
        else tablesGridPane.setVisible(true);
    }

    @FXML
    void teachersOpernButton(ActionEvent event) {
        if (smallTablesInside.isVisible() && whatTableIsSelected.equals("teachers")) {
            smallTablesInside.setVisible(false);
            comboBoxSelectTable.setVisible(false);
            return;
        }
        whatTableIsSelected = "teachers";
        toComboBox.clear();
        comboBoxSelectTable.getEditor().clear();
        smallTablesInside.setVisible(true);
        comboBoxSelectTable.setVisible(true);
        initializeComboboxSelectTable();
    }
}
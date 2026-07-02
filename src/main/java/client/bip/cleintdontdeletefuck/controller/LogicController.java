package client.bip.cleintdontdeletefuck.controller;

import client.bip.cleintdontdeletefuck.entity.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static client.bip.cleintdontdeletefuck.controller.MainController.whatTeacherNeedToCount;

public class LogicController {
    public static List<LoadEntity> workload = new ArrayList<>();

    public static void getValues(String filePath) {
        try {
            FileInputStream fileIN = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileIN);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<Row> rowIterator = new ArrayList<>();
            for (Row row : sheet) rowIterator.add(row);
            for (int i = 5; i < rowIterator.size(); i++) {
                Row thisRow = rowIterator.get(i);
                if (thisRow.getCell(0) == null || thisRow.getCell(0).getStringCellValue().isEmpty() || thisRow.getCell(0).getStringCellValue().isBlank())
                    continue;
                System.out.println(thisRow.getRowNum());/*              Инициалы учителя*/
                String teacher = thisRow.getCell(0).getStringCellValue().trim();/*              Предмет*/
                String subject = thisRow.getCell(1).getStringCellValue().trim().replace("ё", "е");/*              Группа*/
                String group = thisRow.getCell(2).getStringCellValue().trim();/*            Экзамен*/
                int exam = 0;/*            Диплом*/
                int diplom_hours = 0;/*            Консультации*/
                int consultations = 0;/*            Курсовые*/
                int coursework = 0;/*            Семестры*/
                int term = thisRow.getCell(4).getNumericCellValue() == 1 ? 1 : 2;/*            Первый*/
                int semesterFirst = 0;/*            Второй*/
                int semesterSecond = 0;
                if (thisRow.getCell(5).getStringCellValue().equalsIgnoreCase("э")) {
                    exam = (int) thisRow.getCell(6).getNumericCellValue();
                    consultations = (int) thisRow.getCell(8).getNumericCellValue();
                }
                coursework = (int) thisRow.getCell(9).getNumericCellValue();
                if (term == 1) semesterFirst = (int) thisRow.getCell(10).getNumericCellValue();
                else semesterSecond = (int) thisRow.getCell(11).getNumericCellValue();
                diplom_hours = (int) thisRow.getCell(7).getNumericCellValue();
                workload.add(new LoadEntity(semesterFirst, semesterSecond, exam, coursework, diplom_hours, consultations, teacher, subject, group));
            }
            semestersConnect();
            fileIN.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void semestersConnect() {
        workload.sort(Comparator.comparing(LoadEntity::getTeacherInLoad));
        for (int i = 0; i < workload.size(); i++) {
            LoadEntity load = workload.get(i);
            if (i + 1 >= workload.size()) return;
            LoadEntity nextLoad = workload.get(i + 1);
            if (!nextLoad.getTeacherInLoad().equals(load.getTeacherInLoad()))
                continue;
            if (!nextLoad.getSubjectInLoad().equals(load.getSubjectInLoad())) continue;
            if (load.getTeacherInLoad().isBlank() || load.getTeacherInLoad().isEmpty() || load.getTeacherInLoad() == null) {
                workload.remove(load);
                continue;
            }
            load.setConsultationHours(load.getConsultationHours() + nextLoad.getConsultationHours());
            load.setExamHours(load.getExamHours() + nextLoad.getExamHours());
            load.setDiplomaHours(load.getDiplomaHours() + nextLoad.getDiplomaHours());
            load.setCourseworkHours(load.getCourseworkHours() + nextLoad.getCourseworkHours());
            load.setSemesterFirstHours(load.getSemesterFirstHours() + nextLoad.getSemesterFirstHours());
            load.setSemesterSecondHours(load.getSemesterSecondHours() + nextLoad.getSemesterSecondHours());
            workload.remove(nextLoad);
        }
    }

    public static void setValues(String filePath, String sheetName, String whatIsNeed) {
        try {
            FileInputStream fileIN = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(fileIN);
            fileIN.close();
            OutputStream fileOUT = new FileOutputStream(filePath);
            Sheet sheet = wb.getSheet(sheetName);
            List<String> header = new ArrayList<>();
            header.add("№ п/п");
            header.add("ФИО");
            header.add("Читаемые дисциплины");
            header.add("Диплом");
            header.add("Консультация");
            header.add("Экзамен");
            header.add("Курсовая");
            header.add("1 сем");
            header.add("2 сем");
            header.add("ВСЕГО");
            sheet.autoSizeColumn(0, true);
            sheet.setColumnWidth(1, 8 * 1000);
            sheet.setColumnWidth(2, 15 * 1000);
            sheet.autoSizeColumn(3, true);
            sheet.autoSizeColumn(4, true);
            sheet.autoSizeColumn(5, true);
            sheet.autoSizeColumn(6, true);
            sheet.autoSizeColumn(7, true);
            sheet.autoSizeColumn(8, true);
            sheet.autoSizeColumn(9, true);/*             "№ п/п", "ФИО", "Читаемые дисциплины", "Диплом", "Консультация", "Экзамен", "Курсовая", "1 сем", "2 сем", "Итого по дисциплинам", "ВСЕГО"*/
            Row row = sheet.createRow(0);
            for (int i = 0; i < header.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header.get(i));
            }
            List<String> cache = new ArrayList<>();
            Integer numberOfTeacher = 0;
            int cellNum = 1; /* номер ячейки с начала таблицы (без учёта шапки)*/
            for (int i = 0; i < workload.size(); i++) {
                LoadEntity load = workload.get(i);
                if (!"".equals(whatTeacherNeedToCount) && !(whatTeacherNeedToCount == null))
                    load.setTeacherInLoad(whatTeacherNeedToCount);
                if (cache.contains(load.getTeacherInLoad())) continue;
                numberOfTeacher++;
                List<LoadEntity> sameTeacher = load.listOfAllSubjectsThisTeacher(workload);
                List<Integer> allNumbers = load.semesterHours(sameTeacher);
                Integer totalHours = load.totalHours(allNumbers);
                Row rowTable = sheet.createRow(cellNum);
                CellStyle myCellStyle = wb.createCellStyle();
                BorderStyle bs_medium = BorderStyle.THIN;
                myCellStyle.setBorderTop(bs_medium);
                Cell rowNumber = rowTable.createCell(0);
                rowNumber.setCellValue(numberOfTeacher);
                rowNumber.setCellStyle(myCellStyle);
                Cell initials = rowTable.createCell(1);
                initials.setCellValue(load.getTeacherInLoad());
                initials.setCellStyle(myCellStyle);
                cache.add(load.getTeacherInLoad());
                Cell subjects = rowTable.createCell(2);
                subjects.setCellValue(load.getSubjectInLoad());
                subjects.setCellStyle(myCellStyle);
                if ('1' == whatIsNeed.charAt(0)) {
                    Cell diploma = rowTable.createCell(3);
                    diploma.setCellValue(allNumbers.get(1));
                    diploma.setCellStyle(myCellStyle);
                } else totalHours -= allNumbers.get(1);
                if ('1' == whatIsNeed.charAt(1)) {
                    Cell consultations = rowTable.createCell(4);
                    consultations.setCellValue(allNumbers.get(4));
                    consultations.setCellStyle(myCellStyle);
                } else totalHours -= allNumbers.get(4);
                if ('1' == whatIsNeed.charAt(2)) {
                    Cell exam = rowTable.createCell(5);
                    exam.setCellValue(allNumbers.get(5));
                    exam.setCellStyle(myCellStyle);
                } else totalHours -= allNumbers.get(5);
                if ('1' == whatIsNeed.charAt(3)) {
                    Cell coursework = rowTable.createCell(6);
                    coursework.setCellValue(allNumbers.get(3));
                    coursework.setCellStyle(myCellStyle);
                } else totalHours -= allNumbers.get(3);
                if ('1' == whatIsNeed.charAt(4)) {
                    Cell semesterFirst = rowTable.createCell(7);
                    semesterFirst.setCellValue(load.getSemesterFirstHours());
                    semesterFirst.setCellStyle(myCellStyle);
                } else totalHours -= allNumbers.get(1);
                if ('1' == whatIsNeed.charAt(5)) {
                    Cell semesterSecond = rowTable.createCell(8);
                    semesterSecond.setCellValue(load.getSemesterSecondHours());
                    semesterSecond.setCellStyle(myCellStyle);
                } else totalHours -= allNumbers.get(2);
                Cell total = rowTable.createCell(9);
                total.setCellValue(totalHours);
                total.setCellStyle(myCellStyle);
                if (sameTeacher.size() <= 1) {
                    cellNum++;
                    continue;
                }
                for (int d = 1; d < sameTeacher.size(); d++) {
                    Row rowDist = sheet.createRow(cellNum + 1);
                    rowDist.createCell(2).setCellValue(sameTeacher.get(d).getSubjectInLoad());
                    rowDist.createCell(7).setCellValue(sameTeacher.get(d).getSemesterFirstHours());
                    rowDist.createCell(8).setCellValue(sameTeacher.get(d).getSemesterSecondHours());
                    cellNum++;
                }
                cellNum++;
            }
            wb.write(fileOUT);
            fileOUT.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<FinalLoadEntity> convertToServerLoadEntity() {
        List<FinalLoadEntity> server = new ArrayList<>();
        for (LoadEntity toServer : workload)
            server.add(new FinalLoadEntity(new TeacherEntity(toServer.getTeacherInLoad()), new SubjectEntity(toServer.getSubjectInLoad()), new GroupEntity(toServer.getGroupInLoad()), toServer.getExamHours(), toServer.getDiplomaHours(), toServer.getConsultationHours(), toServer.getCourseworkHours(), toServer.getSemesterFirstHours(), toServer.getSemesterSecondHours()));
        return server;
    }

    public static void createSheet(String filePath, String sheetName) {
        try {
            File file = new File(filePath);
            XSSFWorkbook wb = new XSSFWorkbook();
            if (file.exists()) {
                FileInputStream fileIN = new FileInputStream(filePath);
                wb = new XSSFWorkbook(fileIN);
                fileIN.close();
            }
            FileOutputStream fileOut = new FileOutputStream(filePath);
            if (wb.getSheet(sheetName) == null) wb.createSheet(sheetName);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception err) {
            System.out.println("ERROR: " + err.getMessage());
        }
    }
}
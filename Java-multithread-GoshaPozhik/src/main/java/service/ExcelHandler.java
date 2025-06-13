package service;

import model.Employee;
import model.Task;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelHandler {
    public static List<Employee> loadEmployees(String filename) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filename);
            Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; 
                String name = row.getCell(0).getStringCellValue();
                String taskDesc = row.getCell(1).getStringCellValue();
                int hours = (int) row.getCell(2).getNumericCellValue();

                Employee emp = employees.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
                if (emp == null) {
                    emp = new Employee(name);
                    employees.add(emp);
                }
                emp.getTasks().add(new Task(taskDesc, hours));
            }
        }
        return employees;
    }

    public static void saveStatistics(List<Employee> employees, String filename) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stats");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Name");
        header.createCell(1).setCellValue("Рабочие часы (задачи)");
        header.createCell(2).setCellValue("Простой (часы)");
        header.createCell(3).setCellValue("Всего на работе");
        header.createCell(4).setCellValue("Эффективность (%)");

        int rowIndex = 1;
        for (Employee e : employees) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(e.getName());
            row.createCell(1).setCellValue(e.getTotalWorkedHours());
            row.createCell(2).setCellValue(e.getIdleHours());
            row.createCell(3).setCellValue(e.getTotalPresenceHours());
            row.createCell(4).setCellValue((int) (e.getEfficiency() * 100));
        }

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
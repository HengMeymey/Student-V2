package students.student_management.spring_web.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import students.student_management.spring_web.model.Department;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class DepartmentExcelExportService {
    public ByteArrayOutputStream exportDepartmentsToExcel(List<Department> departments) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Departments");

        // Create header row
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("ID");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Name");

        // Populate data rows
        int rowNum = 1;
        for (Department department : departments) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(department.getId());
            row.createCell(1).setCellValue(department.getName());
        }

        // Auto-size columns
        for (int i = 0; i < 2; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream;
    }
}

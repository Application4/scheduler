package com.javatechie.service;

import com.javatechie.model.CourseEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {


    public byte[] generateReport(List<CourseEntity> data) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        writeHeaderLine(sheet);
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (CourseEntity course : data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, course.getCourseId(), style);
            createCell(row, columnCount++, course.getName(), style);
            createCell(row, columnCount++, course.getTrainerName(), style);
            createCell(row, columnCount++, course.getCourseType(), style);
            createCell(row, columnCount++, course.getCourseFees(), style);
            createCell(row, columnCount++, course.getDuration(), style);
            createCell(row, columnCount++, course.getStartDate(), style);
            createCell(row, columnCount++, course.isCertificationAvailable(), style);
            createCell(row, columnCount++, course.getSupportEmailId(), style);
            createCell(row, columnCount++, course.getContact(), style);

        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
        }
        return bos.toByteArray();

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("COURSE_ID");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("COURSE_NAME");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("TRAINER_NAME");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("COURSE_TYPE");

        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("COURSE_FEES");

        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("DURATION");

        headerCell = headerRow.createCell(6);
        headerCell.setCellValue("START_DT");

        headerCell = headerRow.createCell(7);
        headerCell.setCellValue("CERTIFICATE_AVAILABLE");

        headerCell = headerRow.createCell(8);
        headerCell.setCellValue("EMAIL");

        headerCell = headerRow.createCell(9);
        headerCell.setCellValue("PHONE_NO");

    }

}

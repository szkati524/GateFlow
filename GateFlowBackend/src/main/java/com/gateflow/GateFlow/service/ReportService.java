package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Visit;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final VisitService visitService;

    public ReportService(VisitService visitService) {
        this.visitService = visitService;
    }

    public byte[] generateExcelReport(LocalDate from, LocalDate to, String company) throws IOException {

        List<VisitDto> visits = visitService.getVisitsForReport(from, to, company);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Raport Wjazdów");


            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);


            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nr Rejestracyjny", "Kierowca", "Firma", "Data Wjazdu", "Ładunek", "Status"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }


            int rowIdx = 1;
            for (VisitDto dto : visits) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dto.getId());
                row.createCell(1).setCellValue(dto.getRegistrationNumber());
                row.createCell(2).setCellValue(dto.getDriverFullName());
                row.createCell(3).setCellValue(dto.getCompanyName());
                row.createCell(4).setCellValue(dto.getEntryTime() != null ? dto.getEntryTime().toString() : "");
                row.createCell(5).setCellValue(dto.getEntryCargo());
                row.createCell(6).setCellValue(dto.getStatus());
            }


            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}




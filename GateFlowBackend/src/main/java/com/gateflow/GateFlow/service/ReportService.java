package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Visit;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.lowagie.text.Document;
import com.lowagie.text.Element;

import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

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
    public byte[] generatePdfReport(LocalDate from, LocalDate to, String company) {
        List<VisitDto> visits = visitService.getVisitsForReport(from, to, company);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, out);
            document.open();

            com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(
                    com.lowagie.text.FontFactory.HELVETICA_BOLD, 18
            );

            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph("Raport Ruchu Pojazdów - GateFlow", titleFont);
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            document.add(title);

            document.add(title);

            document.add(new Paragraph("Zakres: " + from + " do " + to));
            document.add(new Paragraph("Firma: " + (company != null ? company : "Wszystkie")));
            document.add(new Paragraph(" "));


            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            String[] headers = {"Nr Rejestracyjny", "Firma", "Kierowca", "Wjazd", "Ladunek", "Status"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }


            for (VisitDto dto : visits) {
                table.addCell(dto.getRegistrationNumber());
                table.addCell(dto.getCompanyName());
                table.addCell(dto.getDriverFullName());
                table.addCell(dto.getEntryTime() != null ? dto.getEntryTime().toString() : "-");
                table.addCell(dto.getEntryCargo() != null ? dto.getEntryCargo() : "-");
                table.addCell(dto.getStatus());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}




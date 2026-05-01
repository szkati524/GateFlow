package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport(
            @RequestParam String format,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dateFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) String company) {
        try {
            byte[] data;
            String fileName;
            MediaType mediaType;
            if ("excel".equalsIgnoreCase(format)){
                data = reportService.generateExcelReport(dateFrom,dateTo,company);
                fileName="raport_" + LocalDate.now() + ".xlsx";
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            } else if ("pdf".equalsIgnoreCase(format)){
                data = reportService.generatePdfReport(dateFrom,dateTo,company);
                fileName="raport_" + LocalDate.now() + ".pdf";
                mediaType = MediaType.APPLICATION_PDF;

            }else {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"")
                    .contentType(mediaType)
                    .contentLength(data.length)
                    .body(data);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}

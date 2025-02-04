package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.dtos.ExportFileDTO;
import com.example.codewalker.kma.dtos.ListDataDTO;
import com.example.codewalker.kma.dtos.UpdateExcelDTO;
import com.example.codewalker.kma.services.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("semester") String semester) {
        try {
            excelService.processExcelFile(file, semester);
            return ResponseEntity.ok("File processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> update() {
        try {
            excelService.updateData();
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/student")
    public ResponseEntity<?> addStudent(
            @RequestBody ListDataDTO listDataDTO) {
        try {
            excelService.addStudent(listDataDTO);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/export")
    public ResponseEntity<?> exportExcelFile(
            @RequestBody ExportFileDTO exportFileDTO) throws IOException {
        try {
            byte[] reportBytes = excelService.exportToExcel(exportFileDTO.getSemester());
            if (exportFileDTO.getPath()!=null) {
                String directory = exportFileDTO.getPath();
                String filename = "quychuan_preview.xlsx";
                Path filePath = Paths.get(directory + filename);
                Files.write(filePath, reportBytes);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("quychuan_preview.xlsx")
                    .build());
            headers.setContentLength(reportBytes.length);

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error exporting Excel file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/filter")
    public ResponseEntity<?> filterData(
            @RequestParam("semester") String semester) throws IOException {
       return ResponseEntity.ok(excelService.findAllData(semester));
    }
    @GetMapping("/available")
    public ResponseEntity<?> getSemester(){
        return ResponseEntity.ok(this.excelService.getSemesters());
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBySemester(
            @RequestParam("semester") String semester
    ){
        this.excelService.deleteBySemester(semester);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateNewData(
            @RequestBody UpdateExcelDTO updateExcelDTO){
        this.excelService.updateSemesterFile(updateExcelDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
    @PostMapping("/export/real")
    public ResponseEntity<?> exportExcelRealFile(
            @RequestBody ExportFileDTO exportFileDTO) throws IOException {
        try {
            byte[] reportBytes = excelService.exportFileToExcel(exportFileDTO.getSemester());
            if (exportFileDTO.getPath()!=null) {
                String directory = exportFileDTO.getPath();
                String filename = "quychuan_preview_" + UUID.randomUUID().toString() + ".xlsx";
                Path filePath = Paths.get(directory + filename);
                Files.write(filePath, reportBytes);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("quychuan_preview.xlsx")
                    .build());
            headers.setContentLength(reportBytes.length);

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error exporting Excel file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/export/workbook")
    public ResponseEntity<?> exportWorkBookExcelRealFile(
            @RequestBody ExportFileDTO exportFileDTO) throws IOException {
        try {
            byte[] reportBytes = excelService.exportWorkbookToExcel(exportFileDTO.getSemester());
            if (exportFileDTO.getPath()!=null) {
                String directory = exportFileDTO.getPath();
                String filename = "quychuan_preview_" + UUID.randomUUID().toString() + ".xlsx";
                Path filePath = Paths.get(directory + filename);
                Files.write(filePath, reportBytes);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("quychuan_preview.xlsx")
                    .build());
            headers.setContentLength(reportBytes.length);

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error exporting Excel file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/export/another")
    public ResponseEntity<?> exportAnotherExcel(
            @RequestBody ExportFileDTO exportFileDTO) throws IOException {
        try {
            byte[] reportBytes = excelService.exportAnother(exportFileDTO.getSemester());
            if (exportFileDTO.getPath()!=null) {
                String directory = exportFileDTO.getPath();
                String filename = "quychuan_preview_" + UUID.randomUUID().toString() + ".xlsx";
                Path filePath = Paths.get(directory + filename);
                Files.write(filePath, reportBytes);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("quychuan_preview.xlsx")
                    .build());
            headers.setContentLength(reportBytes.length);

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error exporting Excel file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


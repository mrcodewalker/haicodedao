package com.example.codewalker.kma.services;

import com.example.codewalker.kma.dtos.DataDTO;
import com.example.codewalker.kma.dtos.DataInputDTO;
import com.example.codewalker.kma.dtos.ListDataDTO;
import com.example.codewalker.kma.dtos.UpdateExcelDTO;
import com.example.codewalker.kma.models.CourseScheduleDetail;
import com.example.codewalker.kma.models.HocPhan;
import com.example.codewalker.kma.models.KiTuBatDau;
import com.example.codewalker.kma.repositories.CourseScheduleDetailRepository;
import com.example.codewalker.kma.repositories.HocPhanRepository;
import com.example.codewalker.kma.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final CourseScheduleDetailRepository repository;
    private final KiTuBatDauService kiTuBatDauService;
    private final HocPhanRepository hocPhanRepository;
    @Transactional
    public void updateData(){
        List<CourseScheduleDetail> list = this.repository.findAll();
        list.stream()
                .map(this::totalMap)
                .collect(Collectors.toList());
        this.repository.saveAll(list);
    }
    public List<CourseScheduleDetail> findAllData(String semester){
        return this.repository.findByCourseName(semester);
    }
    public void addStudent(ListDataDTO listDataDTO){
        List<DataInputDTO> dtos = listDataDTO.getList();
        List<CourseScheduleDetail> list = this.repository.findAll();
        Map<String, Pair<String, Long>> map = new HashMap<>();
        for (DataInputDTO data: dtos){
            map.put(data.getCourseName(), Pair.of(data.getDescription(),data.getStudentQuantity()));
        }
        List<CourseScheduleDetail> clone = new ArrayList<>();
        for (CourseScheduleDetail detail: list){
            if (map.get(detail.getCourseName())==null)
            {
                clone.add(detail);
                continue;
            }
            if (map.get(detail.getCourseName()).getRight()!=0){
                detail.setStudentQuantity(map.get(detail.getCourseName()).getRight());
                if (map.get(detail.getCourseName()).getLeft()!=null){
                    detail.setDescription(map.get(detail.getCourseName()).getLeft());
                }
            } else {
                clone.add(detail);
            }
        }
        this.repository.saveAll(clone);
    }
    @Transactional
    public List<String> processExcelFile(MultipartFile file, String semester) throws IOException {
        Workbook workbook = getWorkbook(file);
        List<String> sheetNames = new ArrayList<>();
        List<KiTuBatDau> allKiTuBatDau = kiTuBatDauService.getAll();
        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                sheetNames.add(sheet.getSheetName());
                processSheet(sheet, sheet.getSheetName(), semester, allKiTuBatDau);
            }
        } finally {
            workbook.close();
        }
        return sheetNames;
    }
    private void processSheet(Sheet sheet, String classSection, String semester, List<KiTuBatDau> allKiTuBatDau) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

            List<CourseScheduleDetail> allDetails = new ArrayList<>();
            Map<String, List<CourseScheduleDetail>> courseMap = new HashMap<>();
            String currentCourseName = null;
            Integer currentCreditHours = null;
            String currentLlCode = null;
            String currentStudyFormat = null;
            Integer currentPeriodsPerWeek = null;
            LocalDate currentStartDate = null;
            LocalDate currentEndDate = null;
            String currentLecturer = null;
            String currentCourseId = null;

            for (int rowNum = 4; rowNum < sheet.getLastRowNum() + 1; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;
                String courseId = getMergedRegionValue(sheet, rowNum, 1);
                String courseName = getMergedRegionValue(sheet, rowNum, 4);
                String creditHoursStr = getMergedRegionValue(sheet, rowNum, 2);
                String llCode = getMergedRegionValue(sheet, rowNum, 3);
                String studyFormat = getMergedRegionValue(sheet, rowNum, 5);
                String periodsPerWeekStr = getMergedRegionValue(sheet, rowNum, 6);
                String startDateStr = getMergedRegionValue(sheet, rowNum, 10);
                String endDateStr = getMergedRegionValue(sheet, rowNum, 11);
                String lecturer = getMergedRegionValue(sheet, rowNum, 12);

                if (courseName != null && !courseName.trim().isEmpty()) {
                    currentCourseName = courseName.trim();
                }
                if (courseId != null && !courseId.trim().isEmpty()) {
                    currentCourseId = courseId.trim();
                }
                if (creditHoursStr != null && !creditHoursStr.trim().isEmpty()) {
                    try {
                        currentCreditHours = Integer.parseInt(creditHoursStr.trim());
                    } catch (NumberFormatException ignored) {}
                }
                if (llCode != null && !llCode.trim().isEmpty()) {
                    currentLlCode = llCode.trim();
                }
                if (studyFormat != null && !studyFormat.trim().isEmpty()) {
                    currentStudyFormat = studyFormat.trim();
                }
                if (periodsPerWeekStr != null && !periodsPerWeekStr.trim().isEmpty()) {
                    try {
                        currentPeriodsPerWeek = Integer.parseInt(periodsPerWeekStr.trim());
                    } catch (NumberFormatException ignored) {}
                }
                if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                    try {
                        currentStartDate = LocalDate.parse(startDateStr.trim(), formatter);
                    } catch (Exception ignored) {}
                }
                if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                    try {
                        currentEndDate = LocalDate.parse(endDateStr.trim(), formatter);
                    } catch (Exception ignored) {}
                }
                if (lecturer != null && !lecturer.trim().isEmpty()) {
                    currentLecturer = lecturer.trim();
                }

                if (currentCourseName != null) {
                    String dayOfWeek = getStringCellValue(row.getCell(7));
                    String periodRange = getStringCellValue(row.getCell(8));
                    String classroom = getStringCellValue(row.getCell(9));

                    if (!dayOfWeek.isEmpty() && !periodRange.isEmpty()) {
                        CourseScheduleDetail detail = new CourseScheduleDetail();
                        detail.setCourseId(currentCourseId);
                        detail.setCourseName(currentCourseName);
                        detail.setCourseCode(extractCourseCode(currentCourseName));
                        detail.setCreditHours(currentCreditHours);
                        detail.setLlCode(currentLlCode);
                        detail.setClassSection(classSection);
                        detail.setStudyFormat(currentStudyFormat);
                        detail.setPeriodsPerWeek(currentPeriodsPerWeek);
                        detail.setSemester(semester);

                        try {
                            detail.setDayOfWeek(Integer.parseInt(dayOfWeek.trim()));
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        String[] periods = periodRange.split("->");
                        if (periods.length == 2) {
                            detail.setPeriodStart(periods[0].trim());
                            detail.setPeriodEnd(periods[1].trim());
                        }

                        detail.setClassroom(classroom);
                        detail.setStartDate(currentStartDate);
                        detail.setEndDate(currentEndDate);
                        detail.setLecturer(currentLecturer);
                        float bonusTimeFactor = calculateBonusTimeFactor(detail);
                        detail.setBonusTime(bonusTimeFactor);
                        double bonusTotal = kiTuBatDauService.calculateBonusTotal(detail.getCourseCode(), allKiTuBatDau);
                        detail.setBonusTeacher((float) bonusTotal * bonusTimeFactor);
                        detail.setBonusTotal((float) (bonusTimeFactor * bonusTotal));
                        detail.setStudentBonus((float)0.0);
                        detail.setStudentQuantity(0L);
                        detail.setDescription("");
                        detail.setMajor(getMajor(detail.getCourseCode()));
                        allDetails.add(this.calculateStudentBonus(detail));
                        courseMap.computeIfAbsent(currentCourseName, k -> new ArrayList<>()).add(detail);
                    }
                }
            }
        for (Map.Entry<String, List<CourseScheduleDetail>> entry : courseMap.entrySet()) {
            List<CourseScheduleDetail> courseDetails = entry.getValue();
            if (!courseDetails.isEmpty()) {
                int totalActualPeriods = calculateTotalActualPeriods(courseDetails);
                courseDetails.forEach(detail -> {
                    detail.setLlTotal(totalActualPeriods + "");
                    detail.setQc(detail.getBonusTotal() * Integer.parseInt(detail.getLlTotal()));
                });
                allDetails.addAll(courseDetails);
            }
        }
        repository.saveAll(allDetails);
    }
    private float calculateBonusTimeFactor(CourseScheduleDetail detail) {
        int periodStart = Integer.parseInt(detail.getPeriodStart());
        DayOfWeek dayOfWeek = DayOfWeek.of(detail.getDayOfWeek()-1);

        if (periodStart > 12 || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return 1.5f;
        }
        return 1.0f;
    }
    public static String getMajor(String courseCode) {
        if (courseCode == null || courseCode.isEmpty()) {
            return "";
        }

        if (courseCode.matches(".*A.*C.*D.*|.*A.*D.*C.*|.*C.*A.*D.*|.*C.*D.*A.*|.*D.*A.*C.*|.*D.*C.*A.*")) {
            return "CB";
        }

        StringBuilder result = new StringBuilder();
        boolean hasA = courseCode.contains("A");
        boolean hasC = courseCode.contains("C");
        boolean hasD = courseCode.contains("D");

        if (hasA && !hasC && !hasD) {
            return "ATTT";
        }
        if (!hasA && hasC && !hasD) {
            return "CNTT";
        }
        if (!hasA && !hasC && hasD) {
            return "ĐTVT";
        }

        // Handle combinations of two majors
        if (hasA && hasC) {
            result.append("ATTT, CNTT");
        } else if (hasA && hasD) {
            result.append("ATTT, ĐTVT");
        } else if (hasC && hasD) {
            return "CNTT";
        }

        return result.toString();
    }
    private int calculateTotalActualPeriods(List<CourseScheduleDetail> courseDetails) {
        if (courseDetails.isEmpty() || courseDetails.get(0).getStartDate() == null || courseDetails.get(0).getEndDate() == null) {
            return 0;
        }
        int totalPeriods = 0;
        for (CourseScheduleDetail clone: courseDetails) {
            LocalDate startDate = clone.getStartDate();
            LocalDate endDate = clone.getEndDate();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                int day = dayOfWeek.getValue();
                int targetDay = clone.getDayOfWeek();
                if (day==targetDay){
                    int periodsCount = Integer.parseInt(clone.getPeriodEnd()) - Integer.parseInt(clone.getPeriodStart()) + 1;
                    totalPeriods+=periodsCount;
                }
            }
        }

        return totalPeriods;
    }
    private String extractCourseCode(String courseName) {
        if (courseName == null) return "";
        int startIndex = courseName.lastIndexOf("(");
        int endIndex = courseName.lastIndexOf(")");
        if (startIndex >= 0 && endIndex > startIndex) {
            return courseName.substring(startIndex + 1, endIndex);
        }
        return courseName;
    }

    private String getMergedRegionValue(Sheet sheet, int rowNum, int colNum) {
        for (CellRangeAddress region : sheet.getMergedRegions()) {
            if (region.isInRange(rowNum, colNum)) {
                Row firstRow = sheet.getRow(region.getFirstRow());
                if (firstRow != null) {
                    Cell firstCell = firstRow.getCell(region.getFirstColumn());
                    return getStringCellValue(firstCell);
                }
            }
        }

        Row row = sheet.getRow(rowNum);
        if (row != null) {
            Cell cell = row.getCell(colNum);
            return getStringCellValue(cell);
        }

        return null;
    }

    private Workbook getWorkbook(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.endsWith(".xls")) {
            return new HSSFWorkbook(file.getInputStream());
        } else if (filename != null && filename.endsWith(".xlsx")) {
            return new XSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("The file is not an Excel file");
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        try {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        } catch (IllegalStateException e) {
            try {
                return String.valueOf((int) cell.getNumericCellValue());
            } catch (IllegalStateException ex) {
                return "";
            }
        }
    }
    private CourseScheduleDetail calculateStudentBonus(CourseScheduleDetail courseScheduleDetail){
        Long quantity = courseScheduleDetail.getStudentQuantity();
        float target = 1.0f;
        if (quantity>=41&&quantity<=50) target = 1.1f;
        if (quantity>=51&&quantity<=65) target=1.2f;
        if (quantity>=66&&quantity<=80) target=1.3f;
        if (quantity>=81&&quantity<=100) target=1.4f;
        if (quantity>=101) target=1.5f;
        if (quantity==0) target=0f;
        courseScheduleDetail.setStudentBonus(target);
        return courseScheduleDetail;
    }
    private CourseScheduleDetail totalMap(CourseScheduleDetail courseScheduleDetail){
        float bonusTimeFactor = calculateBonusTimeFactor(courseScheduleDetail);
        courseScheduleDetail.setBonusTime(bonusTimeFactor);
        List<KiTuBatDau> allKiTuBatDau = this.kiTuBatDauService.getAll();
        double bonusTotal = kiTuBatDauService.calculateBonusTotal(courseScheduleDetail.getCourseCode(), allKiTuBatDau);
        courseScheduleDetail.setBonusTeacher((float) bonusTotal * bonusTimeFactor);
        courseScheduleDetail.setStudentBonus(this.calculateStudentBonus(courseScheduleDetail).getStudentBonus());
        if (courseScheduleDetail.getStudentBonus()!=0) {
            courseScheduleDetail.setBonusTotal((float) (bonusTimeFactor * bonusTotal * courseScheduleDetail.getStudentBonus()));
        }
        courseScheduleDetail.setQc(Integer.parseInt(courseScheduleDetail.getLlTotal())*courseScheduleDetail.getBonusTotal());
        return this.calculateStudentBonus(courseScheduleDetail);
    }
    public byte[] exportToExcel(String semester) throws IOException {
        List<CourseScheduleDetail> details = repository.findGroupedByCourseName(semester);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Course Schedule Details");

            sheet.setFitToPage(true);
            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLandscape(true);
            printSetup.setFitHeight((short) 0);
            printSetup.setFitWidth((short) 1);

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle sectionHeaderStyle = createSectionHeaderStyle(workbook);
            CellStyle dataCellStyle = createDataCellStyle(workbook);
            CellStyle numberCellStyle = createNumberCellStyle(workbook);
            CellStyle centerStyle = createCenterStyle(workbook);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BẢNG THỐNG KÊ KHỐI LƯỢNG GIẢNG DẠY");
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
            titleRow.setHeight((short) 600);

            Row headerRow = sheet.createRow(1);
            createHeaderRow(headerRow, headerStyle);
            headerRow.setHeight((short) 1200);

            Row sectionRow = sheet.createRow(2);
            Cell sectionCell = sectionRow.createCell(0);
            sectionCell.setCellValue("I. Các học phần thuộc Khoa Cơ bản");
            sectionCell.setCellStyle(sectionHeaderStyle);
            sectionCell.setCellStyle(centerStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 10));
            sectionRow.setHeight((short) 400);

            int rowNum = 3;
            for (CourseScheduleDetail detail : details) {
                Row row = sheet.createRow(rowNum);
                populateDataRow(row, detail, rowNum - 2, dataCellStyle, numberCellStyle);
                row.setHeight((short) 400);
                rowNum++;
            }

            for (int i = 0; i < 11; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                // Ensure minimum and maximum widths
                currentWidth = Math.max(3000, Math.min(currentWidth, 8000));
                sheet.setColumnWidth(i, currentWidth);
            }

            sheet.setRepeatingRows(CellRangeAddress.valueOf("1:2"));

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private void createHeaderRow(Row headerRow, CellStyle headerStyle) {
        String[] columns = {
                "TT", "Số TC", "Lớp học phần", "Giáo Viên", "Số tiết theo CTĐT",
                "Số SV", "Số tiết lên lớp theo TKB",
                "Hệ số lên lớp ngoài giờ HC/ Thạc sĩ/ Tiến sĩ",
                "Hệ số lớp đông", "QC", "Ghi chú"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
    }
    private CellStyle createCenterStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        return style;
    }
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        style.setFont(headerFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        setBorders(style);
        // Add background color
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createSectionHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        return style;
    }
    public ApiResponse getSemesters(){
        return ApiResponse.builder()
                .data(this.repository.findDistinctSemesters())
                .build();
    }
    @Transactional
    public void updateSemesterFile(UpdateExcelDTO updateExcelDTO) {
        List<CourseScheduleDetail> courseDetails = repository.findCourseCodeIn(updateExcelDTO.getCourseCode());

        Map<String, String> courseMajorMap = new HashMap<>();
        for (int i = 0; i < updateExcelDTO.getCourseCode().size(); i++) {
            String courseCode = updateExcelDTO.getCourseCode().get(i);
            String major = updateExcelDTO.getMajor().get(i);
            courseMajorMap.put(courseCode, major);
        }

        courseDetails.forEach(detail -> {
            String major = courseMajorMap.get(detail.getCourseId());
            detail.setMajor(major);
        });

        repository.saveAll(courseDetails);
    }
    public void deleteBySemester(String semester){
        this.repository.deleteBySemester(semester);
    }
    private CellStyle createDataCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        return style;
    }

    private CellStyle createNumberCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorders(style);
        return style;
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private void populateDataRow(Row row, CourseScheduleDetail detail, int index,
                                 CellStyle dataCellStyle, CellStyle numberCellStyle) {
        // Index column
        Cell indexCell = row.createCell(0);
        indexCell.setCellValue(index);
        indexCell.setCellStyle(numberCellStyle);

        // Create and style other cells
        Cell[] cells = new Cell[11];
        for (int i = 0; i < cells.length; i++) {
            cells[i] = row.createCell(i);
            cells[i].setCellStyle(i == 2 || i == 3 || i == 10 ? dataCellStyle : numberCellStyle);
        }

        // Populate data
        cells[0].setCellValue(index);
        cells[1].setCellValue(detail.getCreditHours());
        cells[2].setCellValue(detail.getCourseName());
        cells[3].setCellValue(detail.getLecturer());
        cells[4].setCellValue(detail.getLlCode());
        cells[5].setCellValue(detail.getStudentQuantity());
        cells[6].setCellValue(detail.getLlTotal());
        cells[7].setCellValue(detail.getBonusTeacher());
        cells[8].setCellValue(detail.getStudentBonus());
        cells[9].setCellValue(detail.getQc());
        cells[10].setCellValue(detail.getDescription());
    }
    public byte[] exportFileToExcel(String semester) throws IOException {
        List<CourseScheduleDetail> details = repository.findGroupedByCourseName(semester);
        Map<String, List<CourseScheduleDetail>> majorGroups = details.stream()
                .collect(Collectors.groupingBy(CourseScheduleDetail::getMajor));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Course Schedule Details");

            // Setup page settings
            sheet.setFitToPage(true);
            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLandscape(true);
            printSetup.setFitHeight((short) 0);
            printSetup.setFitWidth((short) 1);

            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle sectionHeaderStyle = createSectionHeaderStyle(workbook);
            CellStyle dataCellStyle = createDataCellStyle(workbook);
            CellStyle numberCellStyle = createNumberCellStyle(workbook);
            CellStyle centerStyle = createCenterStyle(workbook);

            // Create title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("BẢNG THỐNG KÊ KHỐI LƯỢNG GIẢNG DẠY");
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
            titleRow.setHeight((short) 600);

            // Create header
            Row headerRow = sheet.createRow(1);
            createHeaderRow(headerRow, headerStyle);
            headerRow.setHeight((short) 1200);

            int rowNum = 2;
            int sectionNum = 1;

            // Process CB (Cơ bản) first
            if (majorGroups.containsKey("CB")) {
                Row sectionRow = sheet.createRow(rowNum++);
                Cell sectionCell = sectionRow.createCell(0);
                sectionCell.setCellValue(convertToRomanNumeral(sectionNum++) + ". Các học phần thuộc Khoa Cơ bản");
                sectionCell.setCellStyle(sectionHeaderStyle);
                sectionCell.setCellStyle(centerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 10));
                sectionRow.setHeight((short) 400);

                for (CourseScheduleDetail detail : majorGroups.get("CB")) {
                    Row row = sheet.createRow(rowNum);
                    populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
                    row.setHeight((short) 400);
                    rowNum++;
                }
                majorGroups.remove("CB");
            }

            // Process CNTT (Công nghệ thông tin)
            if (majorGroups.containsKey("CNTT")) {
                Row sectionRow = sheet.createRow(rowNum++);
                Cell sectionCell = sectionRow.createCell(0);
                sectionCell.setCellValue(convertToRomanNumeral(sectionNum++) + ". Các học phần thuộc Khoa Công nghệ thông tin");
                sectionCell.setCellStyle(sectionHeaderStyle);
                sectionCell.setCellStyle(centerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 10));
                sectionRow.setHeight((short) 400);

                for (CourseScheduleDetail detail : majorGroups.get("CNTT")) {
                    Row row = sheet.createRow(rowNum);
                    populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
                    row.setHeight((short) 400);
                    rowNum++;
                }
                majorGroups.remove("CNTT");
            }

            // Process ATTT (An toàn thông tin)
            if (majorGroups.containsKey("ATTT")) {
                Row sectionRow = sheet.createRow(rowNum++);
                Cell sectionCell = sectionRow.createCell(0);
                sectionCell.setCellValue(convertToRomanNumeral(sectionNum++) + ". Các học phần thuộc Khoa An toàn thông tin");
                sectionCell.setCellStyle(sectionHeaderStyle);
                sectionCell.setCellStyle(centerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 10));
                sectionRow.setHeight((short) 400);

                for (CourseScheduleDetail detail : majorGroups.get("ATTT")) {
                    Row row = sheet.createRow(rowNum);
                    populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
                    row.setHeight((short) 400);
                    rowNum++;
                }
                majorGroups.remove("ATTT");
            }

            // Process ĐTVT (Điện tử viễn thông)
            if (majorGroups.containsKey("ĐTVT")) {
                Row sectionRow = sheet.createRow(rowNum++);
                Cell sectionCell = sectionRow.createCell(0);
                sectionCell.setCellValue(convertToRomanNumeral(sectionNum++) + ". Các học phần thuộc Khoa Điện tử viễn thông");
                sectionCell.setCellStyle(sectionHeaderStyle);
                sectionCell.setCellStyle(centerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 10));
                sectionRow.setHeight((short) 400);

                for (CourseScheduleDetail detail : majorGroups.get("ĐTVT")) {
                    Row row = sheet.createRow(rowNum);
                    populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
                    row.setHeight((short) 400);
                    rowNum++;
                }
                majorGroups.remove("ĐTVT");
            }

            // Process remaining groups (if any)
            for (Map.Entry<String, List<CourseScheduleDetail>> entry : majorGroups.entrySet()) {
                Row sectionRow = sheet.createRow(rowNum++);
                Cell sectionCell = sectionRow.createCell(0);
                sectionCell.setCellValue(convertToRomanNumeral(sectionNum++) + ". Các học phần thuộc " + entry.getKey());
                sectionCell.setCellStyle(sectionHeaderStyle);
                sectionCell.setCellStyle(centerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 10));
                sectionRow.setHeight((short) 400);

                for (CourseScheduleDetail detail : entry.getValue()) {
                    Row row = sheet.createRow(rowNum);
                    populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
                    row.setHeight((short) 400);
                    rowNum++;
                }
            }

            // Auto-size columns
            for (int i = 0; i < 11; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                currentWidth = Math.max(3000, Math.min(currentWidth, 8000));
                sheet.setColumnWidth(i, currentWidth);
            }

            sheet.setRepeatingRows(CellRangeAddress.valueOf("1:2"));

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private String convertToRomanNumeral(int num) {
        String[] romanNumerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return num <= 10 ? romanNumerals[num - 1] : String.valueOf(num);
    }
    public byte[] exportWorkbookToExcel(String semester) throws IOException {
        List<CourseScheduleDetail> details = repository.findGroupedByCourseName(semester);
        Map<String, List<CourseScheduleDetail>> majorGroups = details.stream()
                .collect(Collectors.groupingBy(CourseScheduleDetail::getMajor));

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            for (Map.Entry<String, List<CourseScheduleDetail>> entry : majorGroups.entrySet()) {
                String majorName = getMajorFullName(entry.getKey());
                XSSFSheet sheet = workbook.createSheet(majorName);

                sheet.setFitToPage(true);
                PrintSetup printSetup = sheet.getPrintSetup();
                printSetup.setLandscape(true);
                printSetup.setFitHeight((short) 0);
                printSetup.setFitWidth((short) 1);

                CellStyle headerStyle = createHeaderStyle(workbook);
                CellStyle dataCellStyle = createDataCellStyle(workbook);
                CellStyle numberCellStyle = createNumberCellStyle(workbook);

                Row titleRow = sheet.createRow(0);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("BẢNG THỐNG KÊ KHỐI LƯỢNG GIẢNG DẠY " + majorName.toUpperCase());
                titleCell.setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
                titleRow.setHeight((short) 600);

                Row headerRow = sheet.createRow(1);
                createHeaderRow(headerRow, headerStyle);
                headerRow.setHeight((short) 1200);

                int rowNum = 2;
                for (CourseScheduleDetail detail : entry.getValue()) {
                    Row row = sheet.createRow(rowNum);
                    populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
                    row.setHeight((short) 400);
                    rowNum++;
                }

                for (int i = 0; i < 11; i++) {
                    sheet.autoSizeColumn(i);
                    int currentWidth = sheet.getColumnWidth(i);
                    currentWidth = Math.max(3000, Math.min(currentWidth, 8000));
                    sheet.setColumnWidth(i, currentWidth);
                }

                sheet.setRepeatingRows(CellRangeAddress.valueOf("1:2"));
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
    public byte[] exportAnother(String semester) throws IOException {
        List<CourseScheduleDetail> details = repository.findGroupedByCourseName(semester);
        List<HocPhan> hocPhans = hocPhanRepository.findAll();

        Map<String, String> departmentMappings = createDepartmentMappings(hocPhans);
        Map<String, List<CourseScheduleDetail>> departmentGroups = groupByDepartment(details, departmentMappings);

        return createWorkbook(departmentGroups);
    }

    private Map<String, String> createDepartmentMappings(List<HocPhan> hocPhans) {
        Map<String, String> mappings = new HashMap<>();
        for (HocPhan hp : hocPhans) {
            mappings.put(hp.getMaHocPhan(), hp.getKhoa());
            mappings.put(hp.getMaBoMon(), hp.getKhoa());
        }
        return mappings;
    }

    private Map<String, List<CourseScheduleDetail>> groupByDepartment(
            List<CourseScheduleDetail> details,
            Map<String, String> departmentMappings) {
        Map<String, List<CourseScheduleDetail>> groups = new HashMap<>();

        for (CourseScheduleDetail detail : details) {
            String department = departmentMappings.getOrDefault(detail.getCourseId(), "Khác");
            groups.computeIfAbsent(department, k -> new ArrayList<>()).add(detail);
        }

        return groups;
    }

    private byte[] createWorkbook(Map<String, List<CourseScheduleDetail>> departmentGroups) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            for (Map.Entry<String, List<CourseScheduleDetail>> entry : departmentGroups.entrySet()) {
                String departmentName = entry.getKey();
                XSSFSheet sheet = workbook.createSheet(departmentName);

                configureSheet(sheet);

                CellStyle headerStyle = createHeaderStyle(workbook);
                CellStyle dataCellStyle = createDataCellStyle(workbook);
                CellStyle numberCellStyle = createNumberCellStyle(workbook);

                createTitleRow(sheet, headerStyle, departmentName);
                createHeaderRow(sheet.createRow(1), headerStyle);

                fillData(sheet, entry.getValue(), dataCellStyle, numberCellStyle);
                autoSizeColumns(sheet);
            }

            return writeToByteArray(workbook);
        }
    }

    private void configureSheet(XSSFSheet sheet) {
        sheet.setFitToPage(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setFitHeight((short) 0);
        printSetup.setFitWidth((short) 1);
        sheet.setRepeatingRows(CellRangeAddress.valueOf("1:2"));
    }

    private void createTitleRow(XSSFSheet sheet, CellStyle headerStyle, String departmentName) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("BẢNG THỐNG KÊ KHỐI LƯỢNG GIẢNG DẠY " + departmentName.toUpperCase());
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
        titleRow.setHeight((short) 600);
    }

    private void fillData(XSSFSheet sheet, List<CourseScheduleDetail> details,
                          CellStyle dataCellStyle, CellStyle numberCellStyle) {
        int rowNum = 2;
        for (CourseScheduleDetail detail : details) {
            Row row = sheet.createRow(rowNum);
            populateDataRow(row, detail, rowNum - 1, dataCellStyle, numberCellStyle);
            row.setHeight((short) 400);
            rowNum++;
        }
    }

    private void autoSizeColumns(XSSFSheet sheet) {
        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
            int currentWidth = sheet.getColumnWidth(i);
            currentWidth = Math.max(3000, Math.min(currentWidth, 8000));
            sheet.setColumnWidth(i, currentWidth);
        }
    }

    private byte[] writeToByteArray(XSSFWorkbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String getMajorFullName(String majorCode) {
        switch (majorCode) {
            case "CB":
                return "Khoa Cơ bản";
            case "CNTT":
                return "Khoa Công nghệ thông tin";
            case "ATTT":
                return "Khoa An toàn thông tin";
            case "ĐTVT":
                return "Khoa Điện tử viễn thông";
            default:
                return majorCode;
        }
    }
}


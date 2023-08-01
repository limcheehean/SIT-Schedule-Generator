package sitschedule;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class Excel {

    private final Workbook workbook;
    private final Sheet sheet;
    private Row row;
    private int rowIndex, cellIndex;
    private final Map<String, CellStyle> cellStyles;

    void nextRow() {
        row = sheet.createRow(++rowIndex);
        cellIndex = 0;
    }

    void mergeCells(int start, int size) {
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, start, start + size - 1));
    }

    void setCellStyles(String type) {
        for (int i = 0; i < row.getLastCellNum(); i++)
            setCellStyles(i, type);
    }

    void setCellStyles(int cellStart, int size, String type) {
        for (int i = cellStart; i < cellStart + size; i++)
            setCellStyles(i, type);
    }

    void setCellStyles(int cellNum, String type) {
        row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellStyle(cellStyles.get(type));
    }

    void setCells(Object... values) {
        for (Object value: values) {
            Cell cell = row.createCell(cellIndex++);
            if (value instanceof String s)
                cell.setCellValue(s);
            else if (value instanceof LocalDate d)
                cell.setCellValue(d);
        }
    }

    void autoFit() {
        int maxRowSize = 0;
        for (int i = 0; i < sheet.getLastRowNum(); i++)
            maxRowSize = Math.max(sheet.getRow(i).getLastCellNum(), maxRowSize);
        for (int i = 0; i < maxRowSize; i++)
            sheet.autoSizeColumn(i);
        sheet.setFitToPage(true);
    }

    void save(String filename) {
        try {
            workbook.write(new FileOutputStream(filename));
            workbook.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    private CellStyle createCellStyle(String specs) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        if (specs.contains("|bold|")) {
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
        }
        if (specs.contains("|date|"))
            style.setDataFormat(workbook.createDataFormat().getFormat("dd MMM"));
        if (specs.contains("|day|"))
            style.setDataFormat(workbook.createDataFormat().getFormat("ddd"));
        if (specs.contains("|center|"))
            style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    Excel() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        rowIndex = -1;
        cellIndex = 0;
        cellStyles = Map.of(
                "border", createCellStyle(""),
                "bold", createCellStyle("|bold|"),
                "date", createCellStyle("|date|"),
                "day", createCellStyle("|day|"),
                "center", createCellStyle("|bold|center|")
        );
    }


}

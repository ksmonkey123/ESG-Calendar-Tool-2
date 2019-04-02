package ch.awae.esgcal.xssf;

import ch.awae.esgcal.api.spreadsheet.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

class ExcelCell implements Cell {
    private final XSSFCell cell;

    ExcelCell(XSSFCell cell) {
        this.cell = cell;
    }

    @Override
    public void write(String value) {
        cell.setCellValue(value);
    }
}

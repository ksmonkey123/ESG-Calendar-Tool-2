package ch.awae.esgcal.xssf;

import ch.awae.esgcal.api.spreadsheet.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.time.LocalDate;

class ExcelCell implements Cell {
    private final XSSFCell cell;

    ExcelCell(XSSFCell cell) {
        this.cell = cell;
    }

    @Override
    public void write(String value) {
        cell.setCellValue(value);
    }

    @Override
    public void write(LocalDate date) {
        LocalDate offset = cell.getSheet().getWorkbook().isDate1904()
                ? LocalDate.of(1903, 12, 31)
                : LocalDate.of(1899, 12, 31);
        cell.setCellValue(date.toEpochDay() - offset.toEpochDay() + 1);
    }

    @Override
    public void write(int number) {
        cell.setCellValue(number);
    }
}

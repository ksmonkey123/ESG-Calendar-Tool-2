package ch.awae.esgcal.xssf;

import ch.awae.esgcal.api.spreadsheet.Cell;
import ch.awae.esgcal.api.spreadsheet.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

class ExcelSheet implements Sheet {

    private final XSSFSheet sheet;

    ExcelSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public Cell cell(int row, int column) {
        XSSFRow xssfRow = sheet.getRow(row);
        if (xssfRow == null)
            xssfRow = sheet.createRow(row);

        XSSFCell cell = xssfRow.getCell(column);
        if (cell == null)
            cell = xssfRow.createCell(column);

        return new ExcelCell(cell);
    }
}

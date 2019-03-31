package ch.awae.esgcal.xssf;

import ch.awae.esgcal.core.export.spreadsheet.Sheet;
import ch.awae.esgcal.core.export.spreadsheet.Workbook;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ExcelWorkbook implements Workbook {

    @Getter
    private final XSSFWorkbook workbook;

    ExcelWorkbook() {
        this.workbook = new XSSFWorkbook();
    }

    @Override
    public Sheet getSheet(String name) {
        XSSFSheet sheet = workbook.getSheet(name);
        if (sheet == null) {
            sheet = workbook.createSheet(name);
        }
        return new ExcelSheet(sheet);
    }

}

package ch.awae.esgcal.xssf;

import ch.awae.esgcal.api.spreadsheet.Sheet;
import ch.awae.esgcal.api.spreadsheet.Workbook;
import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class ExcelWorkbook implements Workbook {

    @Getter
    private final XSSFWorkbook workbook;

    ExcelWorkbook() {
        this.workbook = new XSSFWorkbook();
    }

    ExcelWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    @Override
    public Sheet getSheet(String name) {
        XSSFSheet sheet = workbook.getSheet(name);
        if (sheet == null) {
            sheet = workbook.createSheet(name);
        }
        return new ExcelSheet(sheet);
    }

    @Override
    public void evaluateFormulas() {
        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
    }
}

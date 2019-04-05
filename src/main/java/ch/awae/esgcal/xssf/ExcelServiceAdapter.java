package ch.awae.esgcal.xssf;

import ch.awae.esgcal.api.spreadsheet.SpreadsheetException;
import ch.awae.esgcal.api.spreadsheet.SpreadsheetService;
import ch.awae.esgcal.api.spreadsheet.Workbook;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ExcelServiceAdapter implements SpreadsheetService {

    private final ExcelService excelService;

    @Override
    public Workbook emptyWorkbook() {
        return new ExcelWorkbook();
    }

    @Override
    public void saveWorkbook(Workbook workbook, String file) throws SpreadsheetException {
        try {
            excelService.saveWorkbook((ExcelWorkbook) workbook, file);
        } catch (Exception e) {
            throw new SpreadsheetException(e);
        }
    }

    @Override
    public Workbook fromResource(String file) throws SpreadsheetException {
        try {
            return new ExcelWorkbook(new XSSFWorkbook(
                    Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(file))));
        } catch (Exception e) {
            throw new SpreadsheetException(e);
        }
    }
}

package ch.awae.esgcal.xssf;

import ch.awae.esgcal.api.spreadsheet.SpreadsheetException;
import ch.awae.esgcal.api.spreadsheet.SpreadsheetService;
import ch.awae.esgcal.api.spreadsheet.Workbook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}

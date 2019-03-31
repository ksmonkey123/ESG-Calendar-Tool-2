package ch.awae.esgcal.xssf;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
class ExcelService {

    void saveWorkbook(ExcelWorkbook workbook, String file) throws IOException {
        workbook.getWorkbook().write(new FileOutputStream(file));
    }
}

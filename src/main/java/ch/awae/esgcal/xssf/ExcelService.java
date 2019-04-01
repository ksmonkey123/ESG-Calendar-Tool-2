package ch.awae.esgcal.xssf;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Log
@Service
class ExcelService {

    void saveWorkbook(ExcelWorkbook workbook, String file) throws IOException {
        log.info("saving workbook to file: " + file);
        workbook.getWorkbook().write(new FileOutputStream(file));
    }
}

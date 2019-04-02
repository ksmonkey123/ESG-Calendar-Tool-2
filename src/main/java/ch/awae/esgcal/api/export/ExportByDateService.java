package ch.awae.esgcal.api.export;

import java.time.LocalDate;

public interface ExportByDateService {

    void performExport(ExportByDateType export, String file, LocalDate fromDate, LocalDate toDate) throws ExportException;

}

package ch.awae.esgcal.core.export;

import java.time.LocalDate;

public interface ExportByDateService {

    void performExport(ExportByDateType export, LocalDate fromDate, LocalDate toDate) throws ExportException;

}

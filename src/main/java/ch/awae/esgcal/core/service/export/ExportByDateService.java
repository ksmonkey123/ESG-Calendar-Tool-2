package ch.awae.esgcal.core.service.export;

import java.time.LocalDate;

public interface ExportByDateService {

    void performExport(ExportByDateType export, LocalDate fromDate, LocalDate toDate) throws ExportException;

}

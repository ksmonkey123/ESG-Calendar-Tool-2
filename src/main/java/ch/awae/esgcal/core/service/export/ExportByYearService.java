package ch.awae.esgcal.core.service.export;

public interface ExportByYearService {

    void performExport(ExportByYearType export, int year) throws ExportException;

}

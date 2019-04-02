package ch.awae.esgcal.api.export;

public interface ExportByYearService {

    void performExport(ExportByYearType export, String file, int year) throws ExportException;

}

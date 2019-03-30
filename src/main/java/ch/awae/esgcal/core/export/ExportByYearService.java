package ch.awae.esgcal.core.export;

public interface ExportByYearService {

    void performExport(ExportByYearType export, int year) throws ExportException;

}

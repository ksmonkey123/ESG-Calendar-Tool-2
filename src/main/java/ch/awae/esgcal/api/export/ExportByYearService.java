package ch.awae.esgcal.api.export;

public interface ExportByYearService {

    boolean performExport(ExportByYearType export, int year) throws ExportException;

}

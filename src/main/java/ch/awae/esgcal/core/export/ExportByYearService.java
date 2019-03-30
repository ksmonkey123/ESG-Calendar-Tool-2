package ch.awae.esgcal.core.export;

public interface ExportByYearService {

    boolean performExport(ExportByYearType export, int year) throws ExportException;

}

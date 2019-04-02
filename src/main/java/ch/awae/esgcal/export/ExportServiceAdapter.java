package ch.awae.esgcal.export;

import ch.awae.esgcal.api.export.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExportServiceAdapter implements ExportByYearService, ExportByDateService {

    private final ExportDispatcher dispatcher;

    @Override
    public void performExport(ExportByDateType export, String file, LocalDate fromDate, LocalDate toDate) throws ExportException {
        try {
            dispatcher.performExport(export, file, fromDate, toDate);
        } catch (Exception e) {
            throw new ExportException(e);
        }
    }

    @Override
    public void performExport(ExportByYearType export, String file, int year) throws ExportException {
        try {
            dispatcher.performExport(export, file, year);
        } catch (Exception e) {
            throw new ExportException(e);
        }
    }
}

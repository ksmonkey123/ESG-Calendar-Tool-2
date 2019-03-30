package ch.awae.esgcal.export;

import ch.awae.esgcal.core.export.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExportServiceAdapter implements ExportByYearService, ExportByDateService {

    private final ExportDispatcher dispatcher;

    @Override
    public boolean performExport(ExportByDateType export, LocalDate fromDate, LocalDate toDate) throws ExportException {
        try {
            return dispatcher.performExport(export, fromDate, toDate);
        } catch (Exception e) {
            throw new ExportException(e);
        }
    }

    @Override
    public boolean performExport(ExportByYearType export, int year) throws ExportException {
        try {
            return dispatcher.performExport(export, year);
        } catch (Exception e) {
            throw new ExportException(e);
        }
    }
}

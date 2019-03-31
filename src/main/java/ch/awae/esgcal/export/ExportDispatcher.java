package ch.awae.esgcal.export;

import ch.awae.esgcal.core.export.ExportByDateType;
import ch.awae.esgcal.core.export.ExportByYearType;
import ch.awae.esgcal.export.impl.GanztagExport;
import ch.awae.esgcal.export.impl.ProbenplanExport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
class ExportDispatcher {

    private final GanztagExport ganztagExport;
    private final ProbenplanExport probenplanExport;

    boolean performExport(ExportByDateType export, LocalDate fromDate, LocalDate toDate) throws Exception {
        switch (export) {
            case GANZTAG:
                return ganztagExport.export(fromDate, toDate);
            case BERN:
                return probenplanExport.export(fromDate, toDate, ExportCalendar.BERN);
            case ZUERICH:
                return probenplanExport.export(fromDate, toDate, ExportCalendar.ZUERICH);
            default:
                throw new UnsupportedOperationException();
        }
    }

    boolean performExport(ExportByYearType export, int year) throws Exception {
        throw new UnsupportedOperationException();
    }

}

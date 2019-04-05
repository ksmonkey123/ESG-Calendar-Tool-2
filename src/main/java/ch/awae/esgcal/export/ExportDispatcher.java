package ch.awae.esgcal.export;

import ch.awae.esgcal.api.export.ExportByDateType;
import ch.awae.esgcal.api.export.ExportByYearType;
import ch.awae.esgcal.export.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
class ExportDispatcher {

    private final GanztagExport ganztagExport;
    private final ProbenplanExport probenplanExport;
    private final JahresplanExportZH jahresplanExportZH;
    private final JahresplanExportBE jahresplanExportBE;
    private final JahresplanExportESG jahresplanExportESG;

    void performExport(ExportByDateType export, String file, LocalDate fromDate, LocalDate toDate) throws Exception {
        switch (export) {
            case GANZTAG:
                ganztagExport.export(file, fromDate, toDate);
                return;
            case BERN:
                probenplanExport.export(file, fromDate, toDate, ExportCalendar.BERN);
                return;
            case ZUERICH:
                probenplanExport.export(file, fromDate, toDate, ExportCalendar.ZUERICH);
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }

    void performExport(ExportByYearType export, String file, int year) throws Exception {
        switch (export) {
            case BERN:
                jahresplanExportBE.export(file, year);
                return;
            case ZUERICH:
                jahresplanExportZH.export(file, year);
                return;
            case ESG:
                jahresplanExportESG.export(file, year);
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }

}

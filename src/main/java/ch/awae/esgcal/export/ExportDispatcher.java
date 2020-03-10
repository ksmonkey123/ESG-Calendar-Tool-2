package ch.awae.esgcal.export;

import ch.awae.esgcal.api.export.ExportByDateType;
import ch.awae.esgcal.api.export.ExportByYearType;
import ch.awae.esgcal.export.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
class ExportDispatcher {

    private final GanztagExport ganztagExport;
    private final ProbenplanExport probenplanExportBE;
    private final ProbenplanExport probenplanExportZH;
    private final JahresplanExportZH jahresplanExportZH;
    private final JahresplanExportBE jahresplanExportBE;
    private final JahresplanExportESG jahresplanExportESG;

    @Autowired
    public ExportDispatcher(
            GanztagExport ganztagExport,
            ProbenplanExportFactory probenplanExportFactory,
            JahresplanExportZH jahresplanExportZH,
            JahresplanExportBE jahresplanExportBE,
            JahresplanExportESG jahresplanExportESG) {
        this.ganztagExport = ganztagExport;
        this.probenplanExportBE = probenplanExportFactory.getForCalendar(ExportCalendar.BERN);
        this.probenplanExportZH = probenplanExportFactory.getForCalendar(ExportCalendar.ZUERICH);
        this.jahresplanExportZH = jahresplanExportZH;
        this.jahresplanExportBE = jahresplanExportBE;
        this.jahresplanExportESG = jahresplanExportESG;
    }

    void performExport(ExportByDateType export, String file, LocalDate fromDate, LocalDate toDate) throws Exception {
        switch (export) {
            case GANZTAG:
                ganztagExport.export(file, fromDate, toDate);
                return;
            case BERN:
                probenplanExportBE.export(file, fromDate, toDate);
                return;
            case ZUERICH:
                probenplanExportZH.export(file, fromDate, toDate);
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

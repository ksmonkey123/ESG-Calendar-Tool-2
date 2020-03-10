package ch.awae.esgcal.export.impl;

import ch.awae.esgcal.api.spreadsheet.SpreadsheetService;
import ch.awae.esgcal.export.DecoratedEventService;
import ch.awae.esgcal.export.ExportCalendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProbenplanExportFactory {

    private final DecoratedEventService eventService;
    private final SpreadsheetService spreadsheetService;

    public ProbenplanExport getForCalendar(ExportCalendar calendar) {
        return new ProbenplanExport(eventService, spreadsheetService, calendar);
    }

}

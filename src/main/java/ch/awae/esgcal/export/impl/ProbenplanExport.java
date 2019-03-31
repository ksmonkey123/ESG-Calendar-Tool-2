package ch.awae.esgcal.export.impl;

import ch.awae.esgcal.PostConstructBean;
import ch.awae.esgcal.core.api.ApiException;
import ch.awae.esgcal.core.api.Event;
import ch.awae.esgcal.core.export.spreadsheet.Sheet;
import ch.awae.esgcal.core.export.spreadsheet.SpreadsheetException;
import ch.awae.esgcal.core.export.spreadsheet.SpreadsheetService;
import ch.awae.esgcal.core.export.spreadsheet.Workbook;
import ch.awae.esgcal.core.fx.modal.SaveLocationService;
import ch.awae.esgcal.export.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProbenplanExport implements ExportPipelineSpecification<ProbenplanExport.Entry>, PostConstructBean {

    private final DecoratedEventService eventService;
    private final SaveLocationService saveLocationService;
    private final SpreadsheetService spreadsheetService;

    private String fileSuffix;

    @Override
    public void postContruct(ApplicationContext context) {
        fileSuffix = context.getEnvironment().getRequiredProperty("export.format", String.class);
    }

    @Override
    public Map<ExportCalendar, List<Event>> fetchEvents(LocalDate fromDate, LocalDate toDate) throws ApiException {
        return eventService.listEvents(fromDate, toDate, ExportCalendar.BERN);
    }

    @Override
    public Entry extractData(ExportCalendar exportCalendar, Event event) {
        return new Entry(event.getStart().toLocalTime(), event.getTitle());
    }

    @Override
    public List<Entry> mergeEvents(LocalDate localDate, List<Entry> events) {
        List<Entry> list = new ArrayList<>(events);
        list.sort(Comparator.comparing(Entry::getTime));
        return list;
    }

    @Data
    static class Entry {
        private final LocalTime time;
        private final String event;
    }

    public boolean export(LocalDate dateFrom, LocalDate dateTo, ExportCalendar calendar) throws ApiException, SpreadsheetException {
        Optional<String> saveFile = saveLocationService.prompt("Probenplan" + calendar.getTitle(), fileSuffix);
        if (!saveFile.isPresent())
            return false;

        List<ProcessedDate<Entry>> dates = new ExportPipeline<>(this).execute(dateFrom, dateTo);

        writeSpreadsheet(dates, saveFile.get());

        return true;
    }

    private void writeSpreadsheet(List<ProcessedDate<Entry>> dates, String file) throws SpreadsheetException {
        Workbook workbook = spreadsheetService.emptyWorkbook();
        Sheet sheet = workbook.getSheet("Probenplan");

        DateTimeFormatter dow = DateTimeFormatter.ofPattern("E");
        DateTimeFormatter day = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");

        int row = 0;

        for (ProcessedDate<Entry> date : dates) {
            sheet.cell(row, 0).write(date.getDate().format(dow));
            sheet.cell(row, 1).write(date.getDate().format(day));
            for (Entry event : date.getEvents()) {
                sheet.cell(row, 2).write(event.time.format(time));
                sheet.cell(row, 3).write(event.event);
                row++;
            }
        }

        for (int i = 0; i < 100; i++) {
            sheet.cell(row + i, 0).write("    ");
            sheet.cell(row + i, 1).write("    ");
            sheet.cell(row + i, 2).write("    ");
            sheet.cell(row + i, 3).write("    ");
        }

        spreadsheetService.saveWorkbook(workbook, file);
    }

}

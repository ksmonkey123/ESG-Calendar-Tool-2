package ch.awae.esgcal.export.impl;

import ch.awae.esgcal.api.calendar.ApiException;
import ch.awae.esgcal.api.calendar.Event;
import ch.awae.esgcal.api.spreadsheet.Sheet;
import ch.awae.esgcal.api.spreadsheet.SpreadsheetException;
import ch.awae.esgcal.api.spreadsheet.SpreadsheetService;
import ch.awae.esgcal.api.spreadsheet.Workbook;
import ch.awae.esgcal.export.*;
import ch.awae.esgcal.fx.modal.SaveLocationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log
@Service
@RequiredArgsConstructor
public class GanztagExport implements ExportPipelineSpecification<GanztagExport.Entry> {

    private final DecoratedEventService eventService;
    private final SaveLocationService saveLocationService;
    private final SpreadsheetService spreadsheetService;

    @Override
    public Map<ExportCalendar, List<Event>> fetchEvents(LocalDate fromDate, LocalDate toDate) throws ApiException {
        return eventService.listEvents(fromDate, toDate, ExportCalendar.BERN, ExportCalendar.ZUERICH, ExportCalendar.KONZERTE);
    }

    @Override
    public Entry extractData(ExportCalendar exportCalendar, Event event) {
        return new Entry(Collections.singletonList(exportCalendar), event.getTitle());
    }

    @Override
    public List<Entry> mergeEvents(LocalDate localDate, List<Entry> events) {
        Map<String, List<ExportCalendar>> map = new HashMap<>();

        for (Entry event : events) {
            List<ExportCalendar> calendars = map.computeIfAbsent(event.event, e -> new ArrayList<>());
            for (ExportCalendar calendar : event.calendars) {
                if (!calendars.contains(calendar))
                    calendars.add(calendar);
            }
        }

        List<Entry> results = new ArrayList<>();
        for (Map.Entry<String, List<ExportCalendar>> entry : map.entrySet()) {
            results.add(new Entry(entry.getValue(), entry.getKey()));
        }
        return results;
    }

    @Data
    static class Entry {
        private final List<ExportCalendar> calendars;
        private final String event;

        String getCalendarList() {
            return calendars.stream().map(ExportCalendar::getTitle).reduce((a, b) -> a + " / " + b).orElse("");
        }
    }

    public void export(String file, LocalDate dateFrom, LocalDate dateTo) throws ApiException, SpreadsheetException {
        List<ProcessedDate<Entry>> dates = new ExportPipeline<>(this).execute(dateFrom, dateTo);
        writeSpreadsheet(dates, file);
    }

    private void writeSpreadsheet(List<ProcessedDate<Entry>> dates, String file) throws SpreadsheetException {
        Workbook workbook = spreadsheetService.emptyWorkbook();
        Sheet sheet = workbook.getSheet("Übersicht");

        sheet.cell(0, 0).write("Datum");
        sheet.cell(0, 1).write("Kalender");
        sheet.cell(0, 2).write("Termin");
        sheet.cell(0, 3).write("von");
        sheet.cell(0, 4).write("bis");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd.MM.yyyy");

        int row = 1;

        for (ProcessedDate<Entry> date : dates) {
            for (Entry entry : date.getEvents()) {
                sheet.cell(row, 0).write(date.getDate().format(dtf));
                sheet.cell(row, 1).write(entry.getCalendarList());
                sheet.cell(row, 2).write(entry.getEvent());
                row++;
            }
        }

        spreadsheetService.saveWorkbook(workbook, file);
    }

}

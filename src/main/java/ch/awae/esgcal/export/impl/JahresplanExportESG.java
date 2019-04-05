package ch.awae.esgcal.export.impl;

import ch.awae.esgcal.DateService;
import ch.awae.esgcal.PostConstructBean;
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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ch.awae.esgcal.export.impl.JahresplanExportESG.Category.*;

@Log
@Service
@RequiredArgsConstructor
public class JahresplanExportESG implements ExportPipelineSpecification<JahresplanExportESG.Entry>, PostConstructBean {

    private final DecoratedEventService eventService;
    private final SaveLocationService saveLocationService;
    private final SpreadsheetService spreadsheetService;
    private final DateService dateService;

    private String sourceFile;

    @Override
    public void postContruct(ApplicationContext context) {
        this.sourceFile = context.getEnvironment().getRequiredProperty("export.fileESG");
    }

    @Override
    public Map<ExportCalendar, List<Event>> fetchEvents(LocalDate fromDate, LocalDate toDate) throws ApiException {
        return eventService.listEvents(fromDate, toDate,
                ExportCalendar.BERN, ExportCalendar.ZUERICH, ExportCalendar.GASTVESPERN,
                ExportCalendar.ABWESENHEITEN_ML, ExportCalendar.FERIEN_BE,
                ExportCalendar.FERIEN_ML, ExportCalendar.FERIEN_ZH);
    }

    @Override
    public Entry extractData(ExportCalendar exportCalendar, Event event) {
        String title = event.getTitle();
        if (title.endsWith(" ?")) {
            Entry parsed = parse(exportCalendar, title.substring(0, title.length() - 2));
            return parsed == null ? null : new Entry(parsed.category, parsed.event + " ?");
        } else {
            return parse(exportCalendar, title);
        }
    }

    private Entry parse(ExportCalendar exportCalendar, String event) {
        switch (exportCalendar) {
            case ABWESENHEITEN_ML:
                return e(Termin, event);
            case FERIEN_BE:
                return e(FerienBE, "x");
            case FERIEN_ZH:
                return e(FerienZH, "x");
            case FERIEN_ML:
                return e(FerienML, "x");
            case BERN:
                return parseBE(event.trim());
            case ZUERICH:
                return parseZH(event.trim());
            case GASTVESPERN:
                return parseGASTV(event.trim());
            default:
                throw new AssertionError("unreachable");
        }
    }

    private Entry parseGASTV(String event) {
        if ("Gastvesper".equals(event))
            return e(Termin, "GastV");
        else
            return e(Termin, event);
    }

    private Entry parseBE(String event) {
        switch (event) {
            case "Probe und Vesper":
            case "Probe und Kantatenvesper":
                return e(Termin, "na");
            case "Abendprobe":
                return e(Termin, "ab");
            case "Offenes Singen":
                return e(Termin, "off. Singen BE");
            case "Jahresversammlung Bern":
                return e(Termin, "JV BE");
            case "Probe und Vesper Chor 50+":
                return e(Termin, "50+");
            case "Probe und Vesper Vertretung":
                return e(Termin, "na Vertr.");
            case "Gottesdienst":
                return e(Termin, "GD BE");
            case "Probe und Vesper Junge Kantorei ad hoc":
                return e(Termin, "na Junge");
            default:
                return parseCommon(event);
        }
    }

    private Entry parseZH(String event) {
        switch (event) {
            case "Offenes Singen":
                return e(Termin, "off. Singen ZH");
            case "Vesper":
            case "Probe und Vesper":
                return e(Termin, "zk V");
            case "Probe":
                return e(Termin, "zk");
            case "keine Probe":
                return null;
            case "Probe mit Vertretung":
                return e(Termin, "zk Vertr.");
            case "Jahresversammlung Zürich":
                return e(Termin, "JV ZH");
            case "Gottesdienst":
                return e(Termin, "GD ZH");
            default:
                return parseCommon(event);
        }
    }

    private Entry parseCommon(String event) {
        if (event.endsWith("Registerprobe"))
            return null;
        switch (event) {
            case "Einsingen":
            case "Ensemblesingen":
                return null;
            case "Jahresversammlung ESG":
                return e(Termin, "JV ESG");
            case "Probentag in Bern mit Vesper":
                return e(Termin, "PT BE + na");
            case "Probentag in Bern":
                return e(Termin, "PT BE");
            case "Probentag in Zürich":
                return e(Termin, "PT ZH");
            case "Probentag in Zürich mit Gottesdienst":
                return e(Termin, "PT ZH + GD");
            case "Generalprobe Zürich":
                return e(Termin, "GP ZH");
            case "Generalprobe Zürich mit Vesper":
                return e(Termin, "zk V + GP ZH");
            case "Generalprobe Bern":
                return e(Termin, "GP BE");
            case "Passionsmusik Zürich":
                return e(Termin, "PM ZH");
            case "Passionsmusik Bern":
                return e(Termin, "PM BE");
            case "Abendmusik Bern":
                return e(Termin, "AM BE");
            case "Abendmusik Zürich":
                return e(Termin, "AM ZH");
            case "Weihnachtsmusik Bern":
                return e(Termin, "WM BE");
            case "Weihnachtsmusik Zürich":
                return e(Termin, "WM ZH");
            case "Herbstmusik Bern":
                return e(Termin, "HM BE");
            case "Herbstmusik Zürich":
                return e(Termin, "HM ZH");
            case "Singwochenende":
                return e(Termin, "SingWoE");
            default:
                return e(Termin, event);

        }
    }

    private Entry e(Category category, String event) {
        return new Entry(category, event);
    }

    @Override
    public List<Entry> mergeEvents(LocalDate localDate, List<Entry> events) {
        Map<Category, List<Entry>> map = events.stream().collect(Collectors.groupingBy(Entry::getCategory));
        return map.entrySet().stream().map(entry -> e(entry.getKey(), doMerge(entry.getValue()))).collect(Collectors.toList());
    }

    private String doMerge(List<Entry> value) {
        String raw = value.stream().map(Entry::getEvent).distinct().reduce((a, b) -> a + " / " + b).orElse("");
        raw = raw.replaceAll("zk V / zk", "zk V");
        return raw.replaceAll("na / ab", "na + ab");
    }

    @Data
    static class Entry {
        private final Category category;
        private final String event;
    }

    enum Category {
        Termin, FerienBE, FerienZH, FerienML
    }

    public void export(String file, int year) throws ApiException, SpreadsheetException {
        LocalDate start = dateService.date(year, 1, 1);
        LocalDate end = dateService.date(year, 12, 31);
        List<ProcessedDate<Entry>> dates = new ExportPipeline<>(this).execute(start, end);

        writeSpreadsheet(dates, file, year);
    }

    private void writeSpreadsheet(List<ProcessedDate<Entry>> dates, String file, int year) throws SpreadsheetException {
        Workbook workbook = spreadsheetService.fromResource(this.sourceFile);
        Sheet sheet = workbook.getSheet("Basistabelle");

        sheet.cell(1, 19).write(dateService.date(year, 1, 1));

        long baseRow = 5 - dateService.date(year, 1, 1).toEpochDay();

        for (ProcessedDate<Entry> date : dates) {
            int row = (int) (date.getDate().toEpochDay() + baseRow);
            if (date.getDate().getYear() != year)
                continue;
            for (Entry event : date.getEvents()) {
                switch (event.category) {
                    case FerienZH:
                        sheet.cell(row, 3).write(event.event);
                        break;
                    case FerienBE:
                        sheet.cell(row, 4).write(event.event);
                        break;
                    case FerienML:
                        sheet.cell(row, 5).write(event.event);
                        break;
                    case Termin:
                        sheet.cell(row, 6).write(event.event);
                        break;
                }
            }
        }

        for (LocalDate d = dateService.date(year, 1, 1); d.getYear() == year; d = d.plusDays(1)) {
            sheet.cell(4 + d.getDayOfYear(), 1).write(parseDoW(d.getDayOfWeek()));
            if (d.getDayOfMonth() == 1)
                sheet.cell(4 + d.getDayOfYear(), 0).write(parseMonth(d.getMonthValue()));
        }

        Sheet table = workbook.getSheet("Jahresübersicht");

        for (LocalDate d = dateService.date(year, 1, 1); d.getYear() == year; d = d.plusDays(1)) {
            int dow = d.withDayOfMonth(1).getDayOfWeek().getValue();
            int row_offset = dow < 3 ? dow + 8 : dow + 1;
            int row = row_offset + d.getDayOfMonth() - 1;
            int col = 1 + (d.getMonthValue() - 1) * 8;
            table.cell(row, col).write(d.getDayOfMonth());
        }

        for (int month = 1; month <= 12; month++) {
            int col = 2 + (month - 1) * 8;
            int daysTillHere = dateService.date(year, month, 1).getDayOfYear() - 1;
            table.cell(0, col).write(daysTillHere);
        }

        workbook.evaluateFormulas();

        spreadsheetService.saveWorkbook(workbook, file);

    }

    private String parseMonth(int month) {
        switch (month) {
            case 1:
                return "Januar";
            case 2:
                return "Februar";
            case 3:
                return "März";
            case 4:
                return "April";
            case 5:
                return "Mai";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            case 12:
                return "Dezember";
            default:
                throw new AssertionError("unreachable");
        }
    }

    private String parseDoW(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "Mo";
            case TUESDAY:
                return "Di";
            case WEDNESDAY:
                return "Mi";
            case THURSDAY:
                return "Do";
            case FRIDAY:
                return "Fr";
            case SATURDAY:
                return "Sa";
            case SUNDAY:
                return "So";
            default:
                throw new AssertionError("unreachable");
        }
    }

}

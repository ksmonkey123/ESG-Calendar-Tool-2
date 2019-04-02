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
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log
@Service
@RequiredArgsConstructor
public class JahresplanExportZH implements ExportPipelineSpecification<String>, PostConstructBean {

    private final DecoratedEventService eventService;
    private final SaveLocationService saveLocationService;
    private final SpreadsheetService spreadsheetService;
    private final DateService dateService;

    private String fileSuffix;

    @Override
    public void postContruct(ApplicationContext context) {
        fileSuffix = context.getEnvironment().getRequiredProperty("export.format", String.class);
        log.config("fileSuffix = " + fileSuffix);
    }

    @Override
    public Map<ExportCalendar, List<Event>> fetchEvents(LocalDate fromDate, LocalDate toDate) throws ApiException {
        return eventService.listEvents(fromDate, toDate, ExportCalendar.ZUERICH);
    }

    @Override
    public String extractData(ExportCalendar exportCalendar, Event event) {
        String title = event.getTitle();
        if (title.endsWith(" ?")) {
            String parsed = parse(title.substring(0, title.length() - 2));
            return parsed == null ? null : parsed + " ?";
        } else {
            return parse(title);
        }
    }

    private String parse(String title) {
        if (title.endsWith("Registerprobe"))
            return null;
        switch (title) {
            case "Ensemblesingen":
            case "Einsingen":
                return null;
            case "Offenes Singen":
                return "off. Singen ZH";
            case "Probe und Vesper":
                return "Vesper + Probe";
            case "Probe mit Vertretung":
                return "Probe";
            case "Jahresversammlung Zürich":
                return "Jahresversammlung ZKP";
            case "Probentag in Bern mit Vesper":
                return "Probentag in BE + Vesper";
            case "Probentag in Bern":
                return "Probentag in BE";
            case "Probentag in Zürich mit Gottesdienst":
                return "Probentag in ZH + GD";
            case "Probentag in Zürich":
                return "Probentag in ZH";
            case "Generalprobe Zürich":
                return "Generalprobe in ZH";
            case "Generalprobe Zürich mit Vesper":
                return "Generalprobe in ZH";
            case "Generalprobe Bern":
                return "Generalprobe in BE";
            case "Passionsmusik Zürich":
                return "Passionsmusik in ZH";
            case "Passionsmusik Bern":
                return "Passionsmusik in BE";
            case "Abendmusik Zürich":
                return "Abendmusik in ZH";
            case "Abendmusik Bern":
                return "Abendmusik in BE";
            case "Weihnachtsmusik Zürich":
                return "Weihnachtsmusik in ZH";
            case "Weihnachtsmusik Bern":
                return "Weihnachtsmusik in BE";
            case "Herbstmusik Zürich":
                return "Herbstmusik in ZH";
            case "Herbstmusik Bern":
                return "Herbstmusik in BE";
            default:
                return title;
        }
    }

    @Override
    public List<String> mergeEvents(LocalDate localDate, List<String> events) {
        String merged = events.stream().reduce((a, b) -> a + " / " + b).orElse(null);
        if ("Vesper / Probe".equals(merged))
            merged = "Vesper + Probe";
        return Collections.singletonList(merged);
    }


    public boolean export(int year) throws ApiException, SpreadsheetException {
        Optional<String> saveFile = saveLocationService.prompt("JahresplanZH", fileSuffix);
        if (!saveFile.isPresent())
            return false;

        LocalDate start = dateService.date(year, 1, 1);
        LocalDate end = dateService.date(year, 12, 31);
        List<ProcessedDate<String>> dates = new ExportPipeline<>(this).execute(start, end);

        writeSpreadsheet(dates, saveFile.get());
        return true;
    }

    private void writeSpreadsheet(List<ProcessedDate<String>> dates, String file) throws SpreadsheetException {
        Workbook workbook = spreadsheetService.emptyWorkbook();
        Sheet sheet = workbook.getSheet("Jahresplan ZH");

        DateTimeFormatter dow = DateTimeFormatter.ofPattern("E");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy");

        int row = 0;
        for (ProcessedDate<String> date : dates) {
            sheet.cell(row, 0).write(date.getDate().format(dow));
            sheet.cell(row, 1).write(date.getDate().format(dtf));
            sheet.cell(row, 2).write(date.getEvents().get(0));
            row++;
        }
        for (int i = 0; i < 100; i++) {
            sheet.cell(row + i, 0).write("    ");
            sheet.cell(row + i, 1).write("    ");
            sheet.cell(row + i, 2).write("    ");
        }

        spreadsheetService.saveWorkbook(workbook, file);
    }
}

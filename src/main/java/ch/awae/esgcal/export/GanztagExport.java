package ch.awae.esgcal.export;

import ch.awae.esgcal.core.api.ApiException;
import ch.awae.esgcal.core.api.Event;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
class GanztagExport implements ExportPipelineSpecification<GanztagExport.Entry> {

    private final DecoratedEventService eventService;

    boolean export(LocalDate dateFrom, LocalDate dateTo) throws ApiException {
        List<ProcessedEvent<Entry>> events = new ExportPipeline<>(this).execute(dateFrom, dateTo);

        for (ProcessedEvent<Entry> event : events) {
            System.out.println(" - " + event);
        }
        return false;
    }

    @Override
    public Map<ExportCalendar, List<Event>> fetchEvents(LocalDate fromDate, LocalDate toDate) throws ApiException {
        return eventService.listEvents(fromDate, toDate, ExportCalendar.BERN, ExportCalendar.ZUERICH, ExportCalendar.KONZERTE);
    }

    @Override
    public Entry extractData(ExportCalendar exportCalendar, Event event) {
        return event.isFullDay() ? new Entry(Collections.singletonList(exportCalendar), event.getTitle()) : null;
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
    }

}

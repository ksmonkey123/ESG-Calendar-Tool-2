package ch.awae.esgcal.export;

import ch.awae.esgcal.core.api.ApiException;
import ch.awae.esgcal.core.api.Event;
import ch.awae.utils.functional.T2;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ExportPipeline<T> {

    private final ExportPipelineSpecification<T> specification;

    public List<ProcessedDate<T>> execute(LocalDate fromDate, LocalDate toDate) throws ApiException {
        return getRawEventList(fromDate, toDate).parallelStream()
                // extract event date
                .map(tuple -> T2.of(tuple._2, specification.extractData(tuple._1, tuple._2)))
                // filter out null data
                .filter(tuple -> tuple._2 != null)
                // fragment events if they overlap midnight
                .flatMap(this::fragment)
                // filter data
                .filter(tuple -> specification.filterData(tuple._1, tuple._2))
                // group by date
                .collect(Collectors.groupingBy(t -> t._1)).entrySet().stream()
                // conversion Entry<Date, T2<Date, List<T>>> ==> T2<Date, Stream<T>>
                .map(entry -> T2.of(entry.getKey(), entry.getValue().stream().map(t -> t._2).collect(Collectors.toList())))
                // allow specification to merge elements within the same date
                .map(tuple -> T2.of(tuple._1, specification.mergeEvents(tuple._1, tuple._2)))
                // sort by date
                .sorted(Comparator.comparing(tuple -> tuple._1))
                // collect into pretty object
                .map(ProcessedDate::new)
                // collect into a list
                .collect(Collectors.toList());

    }

    private Stream<T2<LocalDate, T>> fragment(T2<Event, T> event) {
        int secondsPerDay = 86400;
        long start = event._1.getStart().toEpochSecond(ZoneOffset.UTC);
        long end = event._1.getEnd().toEpochSecond(ZoneOffset.UTC);

        Stream<LocalDate> dates = ((end - start) % secondsPerDay == 0)
            ? doFragmentation(event._1.getStart().toLocalDate(), event._1.getEnd().minusDays(1).toLocalDate())
            : doFragmentation(event._1.getStart().toLocalDate(), event._1.getEnd().toLocalDate());

        return dates.map(date -> T2.of(date, event._2));
    }

    private Stream<LocalDate> doFragmentation(LocalDate start, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();

        dates.add(start);

        for (LocalDate date = start.plusDays(1); date.isBefore(end); date = date.plusDays(1))
            dates.add(date);

        return dates.stream();
    }

    private List<T2<ExportCalendar, Event>> getRawEventList(LocalDate fromDate, LocalDate toDate) throws ApiException {
        Map<ExportCalendar, List<Event>> rawMap = specification.fetchEvents(fromDate, toDate);
        List<T2<ExportCalendar, Event>> rawList = new ArrayList<>();

        for (Map.Entry<ExportCalendar, List<Event>> mapEntry : rawMap.entrySet()) {
            for (Event event : mapEntry.getValue()) {
                rawList.add(T2.of(mapEntry.getKey(), event));
            }
        }
        return rawList;
    }

}

package ch.awae.esgcal.core;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateService {

    public List<Integer> getYearsForSelection() {
        List<Integer> years = new ArrayList<>();

        int year = LocalDate.now().getYear();

        for (int i = 0; i < 10; i++) {
            years.add(year + i);
        }
        return years;

    }

    public LocalDate getBeginningOfYear() {
        return LocalDate.now().withDayOfYear(1);
    }

    public LocalDate date(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}

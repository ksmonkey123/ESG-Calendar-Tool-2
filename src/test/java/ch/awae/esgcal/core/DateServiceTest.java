package ch.awae.esgcal.core;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class DateServiceTest {

    private DateService service;

    @Before
    public void setup() {
        service = new DateService();
    }

    @Test
    public void testDateFactory() {
        LocalDate date = service.date(2019, 3, 14);
        assertThat(date.getYear()).isEqualTo(2019);
        assertThat(date.getMonth()).isEqualTo(Month.MARCH);
        assertThat(date.getDayOfMonth()).isEqualTo(14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFactoryMonthZero() {
        service.date(2019, 0, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFactoryMonth13() {
        service.date(2019, 13, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFactoryDayZero() {
        service.date(2019, 3, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFactoryDayTooLarge() {
        service.date(2019, 3, 32);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateFactoryDayFebruary29NotAllowedNormally() {
        service.date(2019, 2, 29);
    }

    @Test
    public void testDateFactoryDayFebruary29AllowedOnLeapYear() {
        service.date(2020, 2, 29);
    }

    @Test
    public void beginningOfYearCorrect() {
        int currentYear = LocalDate.now().getYear();
        LocalDate date = service.getBeginningOfYear();
        assertThat(date.getYear()).isEqualTo(currentYear);
        assertThat(date.getMonth()).isEqualTo(Month.JANUARY);
        assertThat(date.getDayOfMonth()).isEqualTo(1);
    }

    @Test
    public void testYearList() {
        int currentYear = LocalDate.now().getYear();
        assertThat(service.getYearsForSelection(1)).hasSize(1).containsSequence(currentYear);
        assertThat(service.getYearsForSelection(2)).hasSize(2).containsSequence(currentYear, currentYear + 1);
        assertThat(service.getYearsForSelection(3)).hasSize(3).containsSequence(currentYear, currentYear + 1, currentYear + 2);
    }

}

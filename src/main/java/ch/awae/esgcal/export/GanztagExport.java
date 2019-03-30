package ch.awae.esgcal.export;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
class GanztagExport {

    

    boolean export(LocalDate dateFrom, LocalDate dateTo) {
        throw new UnsupportedOperationException();
    }

}

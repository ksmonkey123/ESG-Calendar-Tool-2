package ch.awae.esgcal.google;

import ch.awae.esgcal.api.calendar.ApiException;
import ch.awae.esgcal.api.calendar.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class AuthorizationServiceAdapter implements LoginService {

    private final AuthorizationService authorizationService;

    @Override
    public void login() throws ApiException {
        try {
            authorizationService.authorize();
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }
}

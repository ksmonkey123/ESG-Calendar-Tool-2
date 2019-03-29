package ch.awae.esgcal.google;

import ch.awae.esgcal.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class AuthorizationServiceAdapter implements LoginService {

    private final AuthorizationService authorizationService;

    @Override
    public void login() throws Exception {
        authorizationService.authorize();
    }
}

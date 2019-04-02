package ch.awae.esgcal.google;

import ch.awae.esgcal.api.calendar.ApiException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class AuthorizationServiceAdapterTest {

    private AuthorizationServiceAdapter adapter;
    private AuthorizationService service;

    @Before
    public void setup() {
        service = mock(AuthorizationService.class);
        adapter = new AuthorizationServiceAdapter(service);
    }

    @Test(expected = ApiException.class)
    public void loginError() throws InterruptedException, GeneralSecurityException, IOException, ApiException {
        doThrow(new RuntimeException()).when(service).authorize();
        adapter.login();
    }

}
package ch.awae.esgcal.google;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.mockito.Mockito.*;

public class AuthorizationServiceTest {

    private AuthorizationService service;
    private BrowserOpeningService browserOpeningService;
    private HttpServer httpServer;

    @Before
    public void setup() {
        browserOpeningService = mock(BrowserOpeningService.class);
        httpServer = mock(HttpServer.class);
        service = new AuthorizationService(browserOpeningService, httpServer);

        ApplicationContext context = mock(ApplicationContext.class);
        Environment environment = mock(Environment.class);

        when(context.getEnvironment()).thenReturn(environment);

        when(environment.getRequiredProperty("google.login.enable", boolean.class)).thenReturn(false);
    }

    @Test(expected = IllegalStateException.class)
    public void testDisabled() throws InterruptedException, GeneralSecurityException, IOException {
        service.authorize();
        verifyNoMoreInteractions(browserOpeningService, httpServer);
        service.getCredentials();
    }

    @Test(expected = IllegalStateException.class)
    public void testUninitialized() {
        service.getCredentials();
    }

}
package ch.awae.esgcal.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
class AuthorizationService {

    private final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private final BrowserOpeningService browserOpeningService;
    private final HttpServer server;
    private final int port;
    private final long timeout;
    private final boolean enable;

    @Getter
    private Credential credentials = null;

    @Autowired
    public AuthorizationService(HttpServer server,
                                BrowserOpeningService browserOpeningService,
                                @Value("${google.login.port}") int port,
                                @Value("${google.login.timeout}") long timeout,
                                @Value("${google.login.enable}") boolean enable) {
        this.server = server;
        this.browserOpeningService = browserOpeningService;
        this.port = port;
        this.timeout = timeout;
        this.enable = enable;
    }

    void authorize() throws GeneralSecurityException, IOException, InterruptedException {
        if (!enable) {
            System.out.println("authentication service disabled. google api will not work");
            return;
        }
        val flow = directUserToLogin(port);
        val code = server.getCode(port, timeout);
        this.credentials = getToken(flow, code, port);
    }

    private Credential getToken(GoogleAuthorizationCodeFlow flow, String code, int port) throws IOException {
        val resp = flow.newTokenRequest(code).setRedirectUri("http://127.0.0.1:" + port).execute();
        return flow.createAndStoreCredential(resp, null);
    }


    private GoogleAuthorizationCodeFlow directUserToLogin(int port) throws GeneralSecurityException, IOException {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        val clientSecrets = GoogleClientSecrets.load(jsonFactory,
                new InputStreamReader(this.getClass().getResourceAsStream("/client_secrets.json")));
        // set up authorization code flow
        val flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR))
                .build();
        val authURL = flow.newAuthorizationUrl().setRedirectUri("http://127.0.0.1:" + port);
        browserOpeningService.openBrowser(authURL.toString());
        return flow;
    }

    void verifyAuthenticated() {
        if (credentials == null) {
            throw new IllegalStateException("Google Calendar API not authorized!");
        }
    }
}

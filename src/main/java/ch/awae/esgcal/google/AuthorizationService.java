package ch.awae.esgcal.google;

import ch.awae.esgcal.PostConstructBean;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Log
@Service
@RequiredArgsConstructor
class AuthorizationService implements PostConstructBean {

    private final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private final BrowserOpeningService browserOpeningService;
    private final HttpServer server;
    private int port;
    private long timeout;
    private boolean enable;

    private Credential credentials = null;

    @Override
    public void postContruct(ApplicationContext context) {
        Environment env = context.getEnvironment();
        port = env.getRequiredProperty("google.login.port", int.class);
        timeout = env.getRequiredProperty("google.login.timeout", long.class);
        enable = env.getRequiredProperty("google.login.enable", boolean.class);
    }

    synchronized void authorize() throws GeneralSecurityException, IOException, InterruptedException {
        if (!enable) {
            log.warning("authentication service disabled. google api will not work");
            return;
        }
        log.info("starting authorization process with Google API");
        val flow = directUserToLogin(port);
        val code = server.getCode(port, timeout);
        this.credentials = getToken(flow, code, port);
        log.info("login completed");
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

    synchronized Credential getCredentials() {
        if (credentials == null) {
            throw new IllegalStateException("Google Calendar API not authorized!");
        }
        return credentials;
    }
}

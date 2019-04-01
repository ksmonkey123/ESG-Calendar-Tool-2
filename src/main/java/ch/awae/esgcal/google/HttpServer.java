package ch.awae.esgcal.google;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;

@Log
@Service
class HttpServer {

    private final Object LOCK = new Object();

    /**
     * launches a local http server and captures the google authentication code.
     * The google authentication form is redirected to http://127.0.0.1:port/ where
     * this local server can receive the authentication code. In any case the local
     * server will be shut down after receiving a GET request.
     */
    String getCode(int port, long timeout) throws IOException, InterruptedException {
        log.info("starting http server at: http://127.0.0.1:" + port + "/");
        val server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        RequestHandler handler = new RequestHandler(LOCK);
        synchronized (LOCK) {
            server.createContext("/", handler);
            server.setExecutor(null);
            server.start();
            log.info("waiting for authorization code");
            LOCK.wait(timeout * 1000);
            server.stop(0);
            log.info("stopped http server");
        }
        return handler.getToken();
    }

    /**
     * The http server handler for the "/" route extracting the authentication code from
     * the GET request redirected to the local server. Terminates the local http server
     * as soon as the first request is handled.
     */
    @RequiredArgsConstructor
    static class RequestHandler implements HttpHandler {

        private final Object LOCK;

        private String token;

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            val uri = httpExchange.getRequestURI().toString();
            token = uri.split("\\s")[0].split("code=")[1];

            val response = "OK. Fenster kann geschlossen werden.";
            httpExchange.sendResponseHeaders(200, response.length());
            val os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            log.info("received authorization code");

            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }

        String getToken() {
            if (token == null) {
                throw new NullPointerException("no token received");
            }
            return token;
        }
    }

}

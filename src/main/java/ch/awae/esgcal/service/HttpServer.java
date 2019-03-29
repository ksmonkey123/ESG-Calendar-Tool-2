package ch.awae.esgcal.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;

@Service
public class HttpServer {

    private final Object LOCK = new Object();

    /**
     * launches a local http server and captures the google authentication code.
     * The google authentication form is redirected to http://127.0.0.1:port/ where
     * this local server can receive the authentication code. In any case the local
     * server will be shut down after receiving a GET request.
     */
    public String getCode(int port, long timeout) throws IOException, InterruptedException {
        val server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
        MyHandler handler = new MyHandler(LOCK);
        synchronized (LOCK) {
            server.createContext("/", handler);
            server.setExecutor(null);
            server.start();
            LOCK.wait(timeout * 1000);
            server.stop(0);
        }
        return handler.getToken();
    }

    /**
     * The http server handler for the "/" route extracting the authentication code from
     * the GET request redirected to the local server. Terminates the local http server
     * as soon as the first request is handled.
     */
    @RequiredArgsConstructor
    static class MyHandler implements HttpHandler {

        private final Object LOCK;

        @Getter
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


            synchronized (LOCK) {
                LOCK.notifyAll();
            }
        }
    }

}

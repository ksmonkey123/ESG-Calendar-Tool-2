package ch.awae.esgcal.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
class BrowserOpeningService {

    void openBrowser(String url) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();

        if (os.contains("win")) {
            // this doesn't support showing urls in the form of "page.html#nameLink"
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (os.contains("mac")) {
            rt.exec("open " + url);
        } else if (os.contains("nix") || os.contains("nux")) {

            // Do a best guess on unix until we get a platform independent way
            // Build a list of browsers to try, in this order.
            String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

            // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
            StringBuilder cmd = new StringBuilder();
            for (int i = 0; i < browsers.length; i++)
                cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
            rt.exec(new String[]{"sh", "-c", cmd.toString()});
        } else {
            throw new UnsupportedOperationException("Unsupported Operating System: " + os);
        }
    }

}

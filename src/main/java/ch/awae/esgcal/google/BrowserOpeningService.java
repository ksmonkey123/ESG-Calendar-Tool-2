package ch.awae.esgcal.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log
@Service
@RequiredArgsConstructor
class BrowserOpeningService {

    void openBrowser(String url) throws IOException {
        Runtime rt = Runtime.getRuntime();
        OperatingSystem os = detectOS();
        log.info("detected OS: " + os);

        Process proc;
        switch (os) {
            case WINDOWS:
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                break;
            case MAC:
                rt.exec("open " + url);
                break;
            case LINUX:
                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape", "opera", "links", "lynx"};

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++)
                    cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
                rt.exec(new String[]{"sh", "-c", cmd.toString()});
                break;
        }
    }

    private static OperatingSystem detectOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
            return OperatingSystem.WINDOWS;
        if (os.contains("mac"))
            return OperatingSystem.MAC;
        if (os.contains("nix") || os.contains("nux"))
            return OperatingSystem.LINUX;
        throw new UnsupportedOperationException("unsupported operating system: " + os);
    }

    enum OperatingSystem {
        WINDOWS, MAC, LINUX
    }

}

package ch.awae.esgcal.service.status;

import org.springframework.stereotype.Service;

@Service
public class ConsoleStatusService implements StatusService {

    @Override
    public void idle() {
        System.out.println("System State: IDLE");
    }

    @Override
    public void busy(String message) {
        System.out.println("System State: BUSY (" + message + ")");
    }

    @Override
    public void working(int progress, String message) {
        System.out.println("System State: WORKING " + progress + "% (" + message + ")");
    }

}

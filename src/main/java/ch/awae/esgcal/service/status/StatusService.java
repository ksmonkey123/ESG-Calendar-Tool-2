package ch.awae.esgcal.service.status;

public interface StatusService {

    void idle();

    void busy(String message);

    default void busy() {
        busy("");
    }

    void working(int progress, String message);

    default void working(int progress) {
        working(progress, "");
    }

    default void working(int job, int jobCount, String message) {
        working(100 * job / jobCount, message);
    }

}
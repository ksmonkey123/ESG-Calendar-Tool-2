package ch.awae.esgcal.fx;

import ch.awae.esgcal.fx.export.ExportRootController;
import ch.awae.esgcal.fx.publish.PublishingRootController;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;

@Log
@Controller
@RequiredArgsConstructor
public class MenuController implements FxController {

    private final RootController rootController;
    private final ExportRootController exportRootController;
    private final PublishingRootController publishingRootController;

    public void onExport() {
        log.info("transitioning to export menu");
        rootController.showExport();
        exportRootController.reset();
    }

    public void onPublish() {
        log.info("transistioning to publishing");
        rootController.showPublishing();
        publishingRootController.reset(false);
    }

    public void onUnpublish() {
        log.info("transitioning to unpublishing");
        rootController.showPublishing();
        publishingRootController.reset(true);
    }

}

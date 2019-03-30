package ch.awae.esgcal.core.fx;

import ch.awae.esgcal.core.fx.export.ExportRootController;
import ch.awae.esgcal.core.fx.publish.PublishingRootController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MenuController implements FxController {

    private final RootController rootController;
    private final ExportRootController exportRootController;
    private final PublishingRootController publishingRootController;

    public void onExport() {
        rootController.showExport();
        exportRootController.reset();
    }

    public void onPublish() {
        rootController.showPublishing();
        publishingRootController.reset(false);
    }

    public void onUnpublish() {
        rootController.showPublishing();
        publishingRootController.reset(true);
    }

}
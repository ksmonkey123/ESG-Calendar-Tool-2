package ch.awae.esgcal.fx;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.export.ExportRootController;
import ch.awae.esgcal.fx.publish.PublishingRootController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MenuController extends FxController {

    private final ApplicationContext context;
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

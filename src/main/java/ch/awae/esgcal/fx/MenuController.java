package ch.awae.esgcal.fx;

import ch.awae.esgcal.FxController;
import ch.awae.esgcal.fx.export.ExportRootController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MenuController extends FxController {

    private final ApplicationContext context;
    private final RootController rootController;

    public void onExport() {
        rootController.showExport();
        context.getBean(ExportRootController.class).showMenu();
    }

    public void onPublish() {
    }

    public void onUnpublish() {
    }

}

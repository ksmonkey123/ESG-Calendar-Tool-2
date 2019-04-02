package ch.awae.esgcal.core.fx.export;

import ch.awae.esgcal.DateService;
import ch.awae.esgcal.JavaFxInit;
import ch.awae.esgcal.api.export.ExportByYearService;
import ch.awae.esgcal.api.export.ExportByYearType;
import ch.awae.esgcal.api.export.ExportException;
import ch.awae.esgcal.fx.RootController;
import ch.awae.esgcal.fx.export.ExportByYearController;
import ch.awae.esgcal.fx.export.ExportRootController;
import ch.awae.esgcal.fx.modal.ErrorReportService;
import ch.awae.esgcal.fx.modal.PopupService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ExportByYearControllerTest {

    private ExportByYearController controller;
    private ExportRootController exportRootController;
    private RootController rootController;
    private ExportByYearService exportByYearService;
    private ErrorReportService errorReportService;
    private PopupService popupService;

    @Before
    public void before() throws InterruptedException {
        JavaFxInit.initFX();
        rootController = mock(RootController.class);
        exportRootController = mock(ExportRootController.class);
        exportByYearService = mock(ExportByYearService.class);
        popupService = mock(PopupService.class);
        errorReportService = mock(ErrorReportService.class);

        controller = new ExportByYearController(new DateService(), exportRootController, exportByYearService,
                popupService, errorReportService, rootController);

        controller.year = new ComboBox<>();
        controller.pane = new BorderPane();
        controller.title = new Label();
    }

    @After
    public void after() {
        verifyNoMoreInteractions(exportRootController, rootController, exportByYearService, errorReportService, popupService);
    }

    @Test(expected = NullPointerException.class)
    public void testNullReset() {
        controller.reset(null);
    }

    @Test
    public void testGoodReset() {
        controller.reset(ExportByYearType.ZUERICH);
        assertThat(controller.year.getValue()).isEqualTo(LocalDate.now().getYear() + 1);
    }

    @Test
    public void testExecuteExportAborted() throws ExportException {
        controller.reset(ExportByYearType.BERN);
        controller.onExecute();
        verify(exportByYearService).performExport(ExportByYearType.BERN, controller.year.getValue());
    }

    @Test
    public void testExecuteExportCompleted() throws ExportException {
        controller.reset(ExportByYearType.BERN);
        when(exportByYearService.performExport(any(), anyInt())).thenReturn(true);
        controller.onExecute();
        verify(exportByYearService).performExport(ExportByYearType.BERN, controller.year.getValue());
        verify(popupService).info(any());
        verify(rootController).showMenu();
    }

    @Test
    public void testExecuteError() throws ExportException {
        controller.reset(ExportByYearType.BERN);
        when(exportByYearService.performExport(any(), anyInt())).thenThrow(new ExportException(new RuntimeException()));
        controller.onExecute();
        verify(exportByYearService).performExport(ExportByYearType.BERN, controller.year.getValue());
        verify(errorReportService).report(any());
    }
}

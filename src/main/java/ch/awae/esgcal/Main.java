package ch.awae.esgcal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
        Locale.setDefault(Locale.GERMANY);
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        runPostConstruct();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Root.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    private void runPostConstruct() {
        Collection<PostConstructBean> beans = springContext.getBeansOfType(PostConstructBean.class).values();
        log.info("running postConstruct on " + beans.size() + " beans");
        for (PostConstructBean bean : beans) {
            log.info(" - " + bean);
            bean.postContruct(springContext);
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(rootNode));
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.show();
        String version = springContext.getEnvironment().getProperty("version");
        stage.setTitle("ESG Tool " + version);
        System.gc();
    }

}

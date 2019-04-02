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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        log.info("spring context initialized");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Root.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    private void runPostConstruct() {
        List<PostConstructBean> beans = new ArrayList<>(springContext.getBeansOfType(PostConstructBean.class).values());
        log.info("running postConstruct on " + beans.size() + " beans");
        int digits = 1 + (int) Math.floor(Math.log10(beans.size()));
        for (int i = 0; i < beans.size(); i++) {
            PostConstructBean bean = beans.get(i);
            log.info(String.format(" (%" + digits + "d) ", i + 1) + bean.getClass().getName());
            bean.postContruct(springContext);
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(rootNode));
        stage.setOnCloseRequest(windowEvent -> {
            log.info("shutdown requested");
            System.exit(0);
        });
        stage.show();
        String version = springContext.getEnvironment().getProperty("version");
        stage.setTitle("ESG Tool " + version);
        System.gc();
    }

}

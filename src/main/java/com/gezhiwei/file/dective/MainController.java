package com.gezhiwei.file.dective;

import com.gezhiwei.file.dective.service.IFileScan;
import com.gezhiwei.file.dective.view.MainStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;


/**
 * @author gezhiwei
 * @Title: MainController
 * @ProjectName file-dective
 * @Description: TODO
 * @date 2019/6/320:37
 */
@SpringBootApplication
public class MainController extends AbstractJavaFxApplicationSupport {


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(MainController.class, MainStageView.class, args);
    }

    /**
     * Start.
     *
     * @param stage the stage
     * @throws Exception the exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.setOnCloseRequest(event -> {
            //close event
        });
    }


}
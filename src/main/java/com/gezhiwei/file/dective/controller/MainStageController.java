package com.gezhiwei.file.dective.controller;

import com.gezhiwei.file.dective.MainController;
import com.gezhiwei.file.dective.service.IFileScan;
import com.gezhiwei.file.dective.util.CalacFileMd5Util;
import com.gezhiwei.file.dective.vo.FileInfo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gezhiwei
 * @Title: MainStageController
 * @ProjectName file-dective
 * @Description: TODO
 * @date 2019/6/320:31
 */
@FXMLController
public class MainStageController implements Initializable {

    private final Desktop desktop = Desktop.getDesktop();

    public static final BigDecimal DIVIDE_NUM = BigDecimal.valueOf(1024 * 1024L);


    public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), r -> new Thread(r, "file_deceive"), (r, executor) -> {
        BlockingQueue<Runnable> queue = executor.getQueue();
        queue.offer(r);
    });

    private static final BlockingQueue<FileInfo> FILE_QUE = new ArrayBlockingQueue<>(100000);

    public TextArea inputText;
    public TextArea outputText;
    public TableView tView;
    public TableColumn fileName;
    public TableColumn filePath;
    public TableColumn fileMd5;
    public TableColumn fileSize;
    public Pagination pagination;

    @Autowired
    IFileScan iFileScan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println(resources);
    }

    public void selectDir(ActionEvent actionEvent) {
        final DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(new Stage());
        if (null == file) {
            return;
        }
        String path = file.getPath();
        inputText.appendText(path + System.lineSeparator());
    }

    private void openFile(File file) {
        EventQueue.invokeLater(() -> {
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Logger.getLogger(MainController.
                        class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
    }

    public void fileAnlyiz(ActionEvent actionEvent) throws InterruptedException {
        String text = inputText.getText();
        if (null == text || StringUtils.isBlank(text)) {
            outputText.appendText("请先选择文件" + System.lineSeparator());
            return;
        }
        String[] split = text.split("\n");
        if (split.length <= 0) {
            outputText.appendText("请先选择文件" + System.lineSeparator());
            return;
        }
        outputText.appendText("已经选择:" + System.lineSeparator());
        for (String dir : split) {
            outputText.appendText(dir + System.lineSeparator());
        }
        outputText.appendText("开始进行文件扫描" + System.lineSeparator());
        List<File> files = iFileScan.scanPath(Arrays.asList(split), outputText);
        for (File file : files) {
            outputText.appendText("计算文件: " + file.getAbsolutePath() + System.lineSeparator());
            EXECUTOR.submit(new FileTask(file));
        }
        pagination = new Pagination(files.size(), 0);
        pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(this::createPage);
        ObservableList<FileInfo> list = FXCollections.observableArrayList();
        fileName.setCellValueFactory(new PropertyValueFactory("fileName"));
        filePath.setCellValueFactory(new PropertyValueFactory("filePath"));
        fileMd5.setCellValueFactory(new PropertyValueFactory("fileMd5"));
        fileSize.setCellValueFactory(new PropertyValueFactory("fileSize"));
        for (FileInfo fileInfo : FILE_QUE) {
            list.add(fileInfo);
            if (list.size() > 8) {
                break;
            }
        }

        tView.setItems(list);
    }

    public void fetchData(MouseEvent mouseEvent) {
        PickResult pickResult = mouseEvent.getPickResult();
        int intersectedFace = pickResult.getIntersectedFace();
        System.out.println(intersectedFace);
    }

    private class FileTask implements Runnable {

        private File file;

        public FileTask(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(file.getName());
            fileInfo.setFilePath(file.getAbsolutePath());
            fileInfo.setFileMd5(CalacFileMd5Util.getFileMD5(file));
            fileInfo.setFileSize(compute(file.length()));
            FILE_QUE.offer(fileInfo);
        }

        private String compute(long length) {
            BigDecimal bigDecimal = new BigDecimal(length);
            BigDecimal divide = bigDecimal.divide(DIVIDE_NUM, 2, BigDecimal.ROUND_DOWN);
            return divide.toString();
        }
    }

    public int itemsPerPage() {
        return 8;
    }

    public VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();
        for (int i = page; i < page + itemsPerPage(); i++) {
            VBox element = new VBox();
            Hyperlink link = new Hyperlink("Item " + (i + 1));
            link.setVisited(true);
            Label text = new Label("Search results\nfor " + link.getText());
            element.getChildren().addAll(link, text);
            box.getChildren().add(element);
        }
        return box;
    }
}

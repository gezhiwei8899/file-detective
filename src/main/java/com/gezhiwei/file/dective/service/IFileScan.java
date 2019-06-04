package com.gezhiwei.file.dective.service;

import javafx.scene.control.TextArea;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author gezhiwei
 * @Title: IFileScan
 * @ProjectName file-dective
 * @Description: TODO
 * @date 2019/6/416:09
 */
public interface IFileScan {
    /**
     *
     * @param paths
     * @param outputText
     * @return
     */
    List<File> scanPath(List<String> paths, TextArea outputText);

    Set<File> findFile(File file, TextArea outputText);

    void clear();

}

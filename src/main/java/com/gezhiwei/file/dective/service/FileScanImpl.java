package com.gezhiwei.file.dective.service;

import javafx.scene.control.TextArea;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author gezhiwei
 * @Title: FileScanImpl
 * @ProjectName file-dective
 * @Description: TODO
 * @date 2019/6/416:09
 */
@Service
public class FileScanImpl implements IFileScan {


    @Override
    public List<File> scanPath(List<String> paths, TextArea outputText) {
        List<File> files = new ArrayList<>(100000);
        for (String path : paths) {
            files.addAll(findFile(new File(path), outputText));
        }
        return files;
    }

    @Override
    public Set<File> findFile(File file, TextArea outputText) {
        Set<File> result = new HashSet<>();
        File[] files = file.listFiles();
        if (files.length <= 0) {
            return result;
        }
        for (File subFile : files) {
            if (subFile.isDirectory()) {
                result.addAll(findFile(subFile, outputText));
            } else if (subFile.isFile()) {
                if (file.getName().startsWith("$RECY") || file.getName().startsWith("System V")) {
                    continue;
                }
                result.add(subFile);
            }
        }
        return result;
    }

    @Override
    public void clear() {
        System.out.println("sadfasfgastorwetjlkgnerf");
    }

}

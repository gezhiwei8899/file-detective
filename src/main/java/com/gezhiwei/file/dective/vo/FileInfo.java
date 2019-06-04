package com.gezhiwei.file.dective.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * @author gezhiwei
 * @Title: FileInfo
 * @ProjectName file-dective
 * @Description: TODO
 * @date 2019/6/416:42
 */
@Getter
@Setter
public class FileInfo {

    private String fileName;

    private String filePath;

    private String fileMd5;

    private String fileSize;
}

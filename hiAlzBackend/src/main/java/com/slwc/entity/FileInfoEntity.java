package com.slwc.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileInfoEntity {
    private String userName;

    private String paperTitle;

    private String filePath;

    private MultipartFile[] files;
}

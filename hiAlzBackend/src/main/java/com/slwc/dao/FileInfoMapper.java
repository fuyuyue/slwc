package com.slwc.dao;

import com.slwc.entity.FileInfoEntity;

public interface FileInfoMapper {
    String getFilePath(String userName, String title);

    void updateFilePath(String userName, String title, String path);

    boolean upload(FileInfoEntity fileInfoEntity);

    void deleteFileInfo(String userName, String title);
}

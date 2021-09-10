package com.slwc.service.impl;

import com.slwc.dao.FileInfoMapper;
import com.slwc.entity.FileInfoEntity;
import com.slwc.utils.FileUtils;
import com.slwc.vo.PaperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl {
    @Autowired
    FileInfoMapper fileInfoMapper;

    public String getFilesPath(String userName, String title) {
        String path = fileInfoMapper.getFilePath(userName, title);
        if (path == null) {
            path = "";
        }
        return path;
    }

    public boolean handleLocalFilesRemove(PaperVo paperVo) {
        String filePath = getFilesPath(paperVo.getUserName(), paperVo.getTitle());
        List<String> oldFilesPath = Arrays.asList(filePath.split(";"));
        List<String> curFilesPath = Arrays.asList(paperVo.getFilesPath().split(";"));
        // 当前路径没有的要全部删除
        List<String> needDeletePaths = oldFilesPath.stream().filter(path -> !curFilesPath.contains(path))
                .collect(Collectors.toList());
        for (String path : needDeletePaths) {
            if (path.isEmpty()) {
                continue;
            }
            if (!FileUtils.deleteOneFile(paperVo.getUserName(), paperVo.getTitle(),
                    FileUtils.getFileName(path))) {
                return false;
            }
        }
        return true;
    }

    public void insertFilesPath(String userName, String paperTitle, String path) {
        FileInfoEntity entity = new FileInfoEntity();
        entity.setUserName(userName);
        entity.setPaperTitle(paperTitle);
        entity.setFilePath(path);
        // 插入文件路径信息到表中
        fileInfoMapper.upload(entity);
    }

    public void updateFilePath(String userName, String title, String path) {
        fileInfoMapper.updateFilePath(userName, title, path);
    }

    public boolean uploadToLocalPath(String userName, String paperTitle, MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                // 保存文件到本地路径
                FileUtils.saveFileByIo((FileInputStream) file.getInputStream(),
                                userName, paperTitle, fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void download(HttpServletResponse response, String filePath) {
        try {
            FileUtils.download(response, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteLocalFile(String userName, String title) {
        return FileUtils.deleteFileDirectory(userName, title);
    }

    public void deleteFileInfo(String userName, String title) {
        fileInfoMapper.deleteFileInfo(userName, title);
    }
}

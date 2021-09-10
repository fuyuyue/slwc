package com.slwc.controller;

import com.slwc.entity.FileInfoEntity;
import com.slwc.service.impl.FileServiceImpl;
import com.slwc.vo.FileInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class FileController {
    @Autowired
    FileServiceImpl fileService;

    @PostMapping("/upload")
    public boolean uploadFile(@RequestParam(value = "files") MultipartFile[] files, FileInfoVo fileInfoVo) {
        FileInfoEntity entity = new FileInfoEntity();
        entity.setUserName(fileInfoVo.getUserName());
        entity.setPaperTitle(fileInfoVo.getPaperTitle());
        entity.setFiles(files);
        fileService.updateFilePath(entity.getUserName(), entity.getPaperTitle(), entity.getFilePath());
        return fileService.uploadToLocalPath(entity.getUserName(), entity.getPaperTitle(), files);
    }

    @RequestMapping("/download")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("filePath") String filePath) {
        fileService.download(response, filePath);
    }
}

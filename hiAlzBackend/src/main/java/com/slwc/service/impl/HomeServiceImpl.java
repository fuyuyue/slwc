package com.slwc.service.impl;

import com.slwc.dao.FileInfoMapper;
import com.slwc.dao.PaperMapper;
import com.slwc.dao.UserInfoMapper;
import com.slwc.entity.PaperEntity;
import com.slwc.entity.UserInfoEntity;
import com.slwc.service.HomeService;
import com.slwc.vo.PageInfoVo;
import com.slwc.vo.PaperVo;
import com.slwc.vo.UserInfoVo;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    FileServiceImpl fileService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    FileInfoMapper fileInfoMapper;

    @Override
    public List<UserInfoVo> getRegisterList() {
        return userInfoMapper.getRegisterList();
    }

    @Override
    public boolean approve(String id) {
        userInfoMapper.approve(id);
        return true;
    }

    @Override
    public boolean deleteById(String id) {
        userInfoMapper.deleteById(id);
        return true;
    }

    public void invokeTrans() throws Exception {
        ((HomeServiceImpl)AopContext.currentProxy()).testTrans();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void testTrans() throws Exception {
        System.out.println("te");
        throw new Exception();
    }

    @Override
    public List<PaperVo> getPaperInfoList(String userName) {
        List<PaperVo> paperVos = new ArrayList<>();
        List<PaperEntity> entities = paperMapper.getPaperInfoList(userName);
        entities.forEach(entity -> {
            String filePath = fileInfoMapper.getFilePath(userName, entity.getTitle());
            PaperVo paperVo = new PaperVo();
            paperVo.setTitle(entity.getTitle());
            paperVo.setAuthor(entity.getAuthor());
            paperVo.setPublishTime(entity.getPublishTime());
            paperVo.setJournalTitle(entity.getJournalTitle());
            paperVo.setJournalNo(entity.getJournalNo());
            paperVo.setPageNo(entity.getPageNo());
            paperVo.setFilesPath(filePath);
            paperVos.add(paperVo);
        });
        return paperVos;
    }

    public List<PaperVo> getSeminarList(String userName) {
        List<PaperVo> paperVos = new ArrayList<>();
        UserInfoEntity userInfo = userInfoMapper.getUserByName(userName);
        List<PaperEntity> entities;
        if ("??????".equals(userInfo.getRole())) {
            entities = paperMapper.getAllSeminar();
        } else {
            entities = paperMapper.getSeminarByUserName(userName);
        }
        entities.forEach(entity -> {
            String filePath = fileInfoMapper.getFilePath(entity.getUserName(), entity.getTitle());
            PaperVo paperVo = new PaperVo();
            paperVo.setTitle(entity.getTitle());
            paperVo.setPublishTime(entity.getPublishTime());
            paperVo.setFilesPath(filePath);
            paperVo.setPublisher(entity.getUserName());
            paperVos.add(paperVo);
        });
        return paperVos;
    }

    public PageInfoVo<PaperVo> getAllStudentsPaperInfo(int pageIndex, int pageSize, String searchContent) {
        PageInfoVo<PaperVo> pageInfoVo = new PageInfoVo<>();
        List<PaperVo> papers = paperMapper.getAllStudentsPaperInfo();
        if (!searchContent.isEmpty()) {
            papers = papers.stream().filter(paper -> (
                    paper.getTitle().contains(searchContent)
                    || paper.getAuthor().contains(searchContent)
                    || paper.getJournalTitle().contains(searchContent)
                    || paper.getUserName().contains(searchContent)
            )).collect(Collectors.toList());
        }
        pageInfoVo.setTotalNum(papers.size());
        int begin = (pageIndex - 1) * pageSize;
        if (begin >= papers.size()) {
            begin = 0;
        }
        int end = Math.min(begin + pageSize, papers.size());
        pageInfoVo.setData(papers.subList(begin, end));
        return pageInfoVo;
    }

    @Override
    public PaperVo getPaperInfo(String title) {
        return paperMapper.getPaperInfo(title);
    }

    @Override
    @Transactional
    public boolean updatePaperInfo(MultipartFile[] files, PaperVo paperVo) {
        PaperVo alreadyExistInDB = getPaperInfo(paperVo.getTitle());
        if (alreadyExistInDB == null) {
            paperMapper.insertPaperInfo(paperVo);
            fileService.insertFilesPath(paperVo.getUserName(), paperVo.getTitle(), paperVo.getFilesPath());
        } else {
            paperMapper.updatePaperInfo(paperVo);
            if (!fileService.handleLocalFilesRemove(paperVo)) {
                return false;
            }
            fileService.updateFilePath(paperVo.getUserName(), paperVo.getTitle(), paperVo.getFilesPath());
        }
        boolean fileInsertRes = true;
        if (files != null && files.length > 0) {
            fileInsertRes = fileService.uploadToLocalPath(paperVo.getUserName(), paperVo.getTitle(), files);
        }
        return fileInsertRes;
    }

    @Override
    @Transactional
    public boolean deletePaperInfo(String title, String userName) {
        paperMapper.deletePaperInfo(title);
        fileService.deleteFileInfo(userName, title);
        return fileService.deleteLocalFile(userName, title);
    }

    @Transactional
    public boolean updatePwd(String newPwd, String userName) {
        userInfoMapper.updatePwd(newPwd, userName);
        return true;
    }
}

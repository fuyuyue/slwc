package com.slwc.service;

import com.slwc.vo.PaperVo;
import com.slwc.vo.UserInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomeService {
    List<UserInfoVo> getRegisterList();

    boolean approve(String id);

    boolean deleteById(String id);

    List<PaperVo> getPaperInfoList(String userName);

    PaperVo getPaperInfo(String title);

    boolean updatePaperInfo(MultipartFile[] files, PaperVo paperVo);

    boolean deletePaperInfo(String title, String userName);
}

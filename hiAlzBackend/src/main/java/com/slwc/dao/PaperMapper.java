package com.slwc.dao;

import com.slwc.entity.PaperEntity;
import com.slwc.vo.PaperVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperMapper {
    List<PaperEntity> getPaperInfoList(String userName);

    PaperVo getPaperInfo(String title);

    void insertPaperInfo(PaperVo paperVo);

    void updatePaperInfo(PaperVo paperVo);

    void deletePaperInfo(String title);
}

package com.slwc.controller;

import com.slwc.service.impl.HomeServiceImpl;
import com.slwc.vo.PaperVo;
import com.slwc.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/home")
public class HomeController {
    @Autowired
    HomeServiceImpl homeService;

    @GetMapping("/get-register-list")
    public List<UserInfoVo> getRegisterList() {
        return homeService.getRegisterList();
    }

    @PostMapping("/approve")
    public boolean approve(@RequestBody String id) {
        return homeService.approve(id);
    }

    @PostMapping("/delete")
    public boolean deleteUserInfoById(@RequestBody String id) {
        return homeService.deleteById(id);
    }

    @GetMapping("/userHome/get-paper-list")
    public List<PaperVo> getPaperInfoList(@RequestParam("userName") String userName) {
        return homeService.getPaperInfoList(userName);
    }

    @GetMapping("/userHome/get-paper")
    public PaperVo getPaperInfo(String title) {
        return homeService.getPaperInfo(title);
    }

    @PostMapping("/userHome/modify-paper")
    public boolean modifyPaperInfo(@RequestParam(value = "files") MultipartFile[] files, PaperVo paperVo) {
        return homeService.updatePaperInfo(files, paperVo);
    }

    @PostMapping("/userHome/modify-paper-no-files")
    public boolean modifyPaperInfoNoFiles(PaperVo paperVo) {
        return homeService.updatePaperInfo(null, paperVo);
    }

    @PostMapping("/userHome/delete-paper")
    public boolean deletePaperInfo(@RequestBody PaperVo paperVo) {
        return homeService.deletePaperInfo(paperVo.getTitle(), paperVo.getUserName());
    }

    @PostMapping("/updatePwd")
    public boolean updatePwd(@RequestBody UserInfoVo userInfoVo) {
        return homeService.updatePwd(userInfoVo.getPwd(), userInfoVo.getUserName());
    }

    @GetMapping("/userHome/get-all-student-paper-list")
    public List<PaperVo> getAllStudentsPaperList(@RequestParam(value = "pageIndex") int pageIndex,
                                                 @RequestParam(value = "searchContent") String searchContent) {

    }
}

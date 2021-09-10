package com.slwc.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jetbrains.annotations.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class PaperVo {

    @NotNull
    private String title;

    private String author;

    private String journalTitle;

    private String journalNo;

    private String pageNo;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String publishTime;

    private String publisher;

    private String userName;

    private String filesPath;

    public PaperVo() {

    }
}

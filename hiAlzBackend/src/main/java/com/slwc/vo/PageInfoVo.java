package com.slwc.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageInfoVo<T> {
    private int totalNum;

    private List<T> data;
}

package com.luoying.once;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData {
    /**
     * 成员编号
     */
    @ExcelProperty("成员编号")
    private String authCode;

    /**
     * 用户昵称
     */
    @ExcelProperty("成员昵称")
    private String username;

}
package com.luoying.once;

import com.alibaba.excel.EasyExcel;

import java.util.List;
import java.util.Map;

public class ImportExcel {
    public static void main(String[] args) {
        // readByListener();
        synchronousRead();
    }

    /**
     * 监听器读取
     */
    private static void readByListener() {
        // 写法1
        String fileName = "prodExcel.xlsx";
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    /**
     * 同步的读取，不推荐使用，如果数据量大会把数据放到内存里面
     */
    public static void synchronousRead() {
        // 写法2
        String fileName = "prodExcel.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<DemoData> toatlDataList = EasyExcel.read(fileName).head(DemoData.class).sheet().doReadSync();
        for (DemoData demoData : toatlDataList) {
            System.out.println(demoData);
        }
    }

}

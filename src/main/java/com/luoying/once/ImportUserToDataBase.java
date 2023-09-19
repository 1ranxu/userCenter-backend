package com.luoying.once;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImportUserToDataBase {
    public static void main(String[] args) {
        String fileName = "E:\\Learn\\workspace\\UserCenter\\userCenter-backend\\src\\main\\resources\\devExcel.xlsx";
        List<DemoData> toatlDataList = EasyExcel.read(fileName).head(DemoData.class).sheet().doReadSync();
        System.out.println("总数 = " + toatlDataList.size());
        //key是昵称，value是昵称相同的所有用户对象
        Map<String, List<DemoData>> listMap = toatlDataList.stream().filter(demoData -> {
            return StringUtils.isNotEmpty(demoData.getUsername());
        }).collect(Collectors.groupingBy(DemoData::getUsername));
        System.out.println("不重复昵称数 = " + listMap.keySet().size());

        for (Map.Entry<String, List<DemoData>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1) {
                System.out.println("username=" + stringListEntry.getKey());
            }
        }
    }
}


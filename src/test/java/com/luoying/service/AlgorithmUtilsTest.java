package com.luoying.service;

import com.luoying.utils.AlgorithmUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class AlgorithmUtilsTest {

    @Test
    void testString(){
        String str1="落樱是樱花";
        String str2="落樱不是樱花";
        String str3="落樱是樱花不是菊花";
        String str4="落樱是菊花";
        int score1= AlgorithmUtil.minDistance(str1,str2);
        int score2= AlgorithmUtil.minDistance(str1,str3);
        int score3= AlgorithmUtil.minDistance(str1,str4);
        System.out.println(score1);
        System.out.println(score2);
        System.out.println(score3);
    }

    @Test
    void testTagList(){
        List<String> stringList1 = Arrays.asList("Java", "大一", "男");
        List<String> stringList2 = Arrays.asList("Java", "大一", "女");
        List<String> stringList3 = Arrays.asList("Python", "大二", "女");
        int score1= AlgorithmUtil.minDistance1(stringList1,stringList2);
        int score2= AlgorithmUtil.minDistance1(stringList1,stringList3);
        System.out.println(score1);
        System.out.println(score2);

    }

}

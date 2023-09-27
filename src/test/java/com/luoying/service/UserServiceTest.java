package com.luoying.service;

import com.luoying.model.domain.User;
import com.luoying.model.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户服务测试
 *
 * @author 落樱的悔恨
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUserAccount("123");
        user.setUsername("luoying");
        user.setAvatarUrl("https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1fFyD2.img?w=768&h=722&m=6&x=452&y=204&s=108&d=108");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("12345678901");
        user.setEmail("123@gmail.com");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {
        String userAccount = "";
        String userPassword = "";
        String checkPassword = "";
        String authCode = "1";
        long l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertEquals(-1, l);

        userAccount = "luo";
        userPassword = "0123456789";
        checkPassword = "0123456789";
        l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertEquals(-1, l);

        userAccount = "luoying";
        userPassword = "0123456";
        checkPassword = "0123456";
        l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertEquals(-1, l);

        userAccount = "luoying";
        userPassword = "0123456789";
        checkPassword = "0123456789";
        l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertEquals(-1, l);

        userAccount = "luo ying";
        userPassword = "0123456789";
        checkPassword = "0123456789";
        l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertEquals(-1, l);

        userAccount = "luoying";
        userPassword = "0123456789";
        checkPassword = "0123456780";
        l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertEquals(-1, l);

        userAccount = "rxer";
        userPassword = "0123456789";
        checkPassword = "0123456789";
        l = userService.userRegister(userAccount, userPassword, checkPassword,authCode);
        Assertions.assertTrue(l > 0);
    }
    @Test
    void userDelete(){
        boolean b = userService.removeById(15);
        System.out.println(b);
    }
    @Test
    void userUpdate(){
        User user = new User();
        user.setId(15l);
        user.setUsername("rxer");
        userService.updateById(user);
    }

    @Test
    void queryUsersByTags() {
        List<String> tagList=new ArrayList<>();
        Collections.addAll(tagList,"Java","C++","Go");
        List<UserVO> userVOS = userService.queryUsersByTagsByMemory(tagList);
        Assertions.assertNotNull(userVOS);
        System.out.println(userVOS);
    }
}
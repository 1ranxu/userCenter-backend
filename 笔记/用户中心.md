# 用户中心

`为什么要做？`

因为任何的系统，无论免费的还是收费的，都有登录的环节。有登录就会有用户，我们就需要把用户信息存储下来。以后我们可能会做很多系统，不可能给每个系统单独去建一套用户体系，最好就是有一个统一的用户管理系统，之后所有的系统，都可以去用户中心取数据，不用每个项目都单独去注册一遍账号，减少重复工作。

功能可以简单理解为一方面支持用户登录，另一方面管理员可以对用户进行统一的管理。

## 项目流程

### 需求分析 

### 设计

### 技术选型

### 初始化/引入需要的技术

### 写Demo

### 写代码实现业务逻辑

### 测试（单元测试/系统测试）

所谓的单元测试，就是比较细粒度和非常简单的测试，例如对增删改查的代码进行测试。系统测试则是前后端开发人员共同进行的测试，包括但不限于接口联调。

测试的目的就是不要把能提前发现的问题拖到线上，假如在线上发现bug之后，然后再从头来修改，这个成本会比你之前就做好测试要高很多

### 代码提交/代码评审

把写好的代码发布到一个所有人都能看见的远程仓库。通过领导或同事的评审，确定没有问题后，再把你的代码上线，防止有人把一段恶意代码/bug提交上线。

### 部署

把开发好的前后端项目放到服务器上或容器环境

### 发布

对已经部署了项目的服务器或容器进行有选择地发布，比如说有九台部署了项目的机器，不一定一次九台全部发完，可以先发布一台，验证没有问题之后，再去发布其余的。



## 需求分析

我们可能会做很多系统，不可能给每个系统单独去建一套用户体系，最好就是有一个统一的用户管理系统，之后所有的系统，都可以去用户中心取数据，每个用户登录，校验密码的时候都从这个用户中心里去取，就相当于用户只要用过我们的系统，用户再用我们其他的系统，就不用再去不用每个系统都单独去注册一遍账号。就好比腾讯，你的QQ号，微信号，一个账号就可以登录很多不同的平台。这也是一个比较重要的思想：`可以集中管理的东西就把它抽出来`

功能可以简单理解为一方面支持用户登录，另一方面管理员可以对用户进行统一的管理。

1. **登录/注册**

2. **用户管理（仅有管理员可见）（对用户的查询或者更改）**

3. **用户校验（目前面向所有人，后续可以仅付费用户可以注册）** 

## 技术选型

1. **前端：**

   - (HTML+CSS+JavaScript)
   - React框架（对JavaScript的封装） 
   - Ant Design组件库
   - Umi（对React框架的封装）
   - Ant Design Pro(现成的管理系统)

2. **后端**：

   - Java （编程语言）

   - Spring（依赖注入框架，管理Java对象，集成一些其他内容） 
   - SpringMvc （Web框架，提供接口访问，restful接口等能力）
   - Mybatis（Java操作数据库的框架，持久层框架，对JDBC的封装） 
   - Mybatis-Plus（对mybatis的增强，不用写SQL也能实现增删改查） 
   - SpringBoot（快速启动/快速集成项目，帮助管理Spring的配置，帮助整合框架） 
   - MySQL（数据库）

3. **部署**：服务器/容器（平台）



## 计划

1. **初始化项目**
   1. **前端初始化** 
      1. 初始化项目 
      2. 引入组件
      3. 框架介绍/项目瘦身(去除Ant Design Pro中用不到的默认的功能的代码)
   2. **后端初始化** 
      1. 准备环境（MySQL数据库)
      2. 引入框架（整合框架）
2. **数据库设计** 
3. **登录/注册** 
   1. **后端** 
      1. 规整项目目录 2 min
      2. 实现基本数据库操作（操作 user 表）
      3. 实现注册逻辑
      4. 实现登录逻辑
      5. 控制层 UserController 封装请求
      6. 补充用户注册校验逻辑
   2. **前端** 
      1. 删除多余代码
      2. 实现用户登录注册
      3. 实现用户注册
      4. 补充用户注册校验逻辑
4. **用户管理（仅管理员可见）**
   1. **后端**
      1. 实现用户管理逻辑（查询用户/查询用户）
         1.  查询用户

         2.  查询用户
      2. 实现获取当前登录用户信息

   2. **前端**
      1. 获取用户的登录态 
      2. 实现用户管理

5. **用户注销**
   1. **后端**

   2. **前端**

6. **前后端代码优化** 
   1. **后端代码优化**
   2. **前端代码瘦身&优化**
7. **部署上线**
   1. 买服务器
   2. 原生部署
   3. 容器部署
   4. 绑定域名
   5. 排查问题

8. **开发付费用户校验**
9. **项目扩展思路**




## 初始化

### 前端环境准备

1. [Node.js (nodejs.org)](https://nodejs.org/zh-cn) 版本16

2. [安装 | Yarn 中文文档 (bootcss.com)](https://yarn.bootcss.com/docs/install.html#windows-stable)

   ![image-20230822204457808](assets\image-20230822204457808.png)



###  前端初始化

#### 初始化Ant Design Pro

[开始使用 - Ant Design Pro](https://pro.ant.design/zh-CN/docs/getting-started/)

#### 使用yarn包管理器安装依赖

![image-20230822220825939](assets/image-20230822220825939.png)

#### 启动

![image-20230822224336905](assets/image-20230822224336905.png)

![image-20230822224409132](assets/image-20230822224409132.png)

#### 开启Umi UI

```
 yarn add @umijs/preset-ui -D
```

![image-20230823134025299](assets/image-20230823134025299.png)

#### 使用Umi UI添加分析页模板到项目 

![image-20230823134829152](assets/image-20230823134829152.png)

#### 项目瘦身(去除Ant Design Pro中用不到的默认的功能的代码)

##### 去除国际化

![image-20230823140950973](assets/image-20230823140950973.png)

![image-20230823142003411](assets/image-20230823142003411.png)

##### 去除集成测试

![image-20230823142515370](assets/image-20230823142515370.png)

##### 去除分析页

![image-20230823142642984](assets/image-20230823142642984.png)

##### 去除分析页路由

![image-20230823142927368](assets/image-20230823142927368.png)

##### 去除接口文档工具

![image-20230823143119074](assets/image-20230823143119074.png)

##### 去除测试

![image-20230823200238377](assets/image-20230823200238377.png)

![image-20230823200757807](assets/image-20230823200757807.png)

### 后端初始化

#### 准备环境（MySQL数据库)

[MySQL :: Download MySQL Community Server (Archived Versions)](https://downloads.mysql.com/archives/community/)

#### 三种初始化Java项目的方法

1. GitHub现成的模板

2. SpringBoot官方模板生成器

3. IDEA开发工具生成（首选）  

#### 引入框架（整合框架）

1. MybatisPlus [快速开始 | MyBatis-Plus (baomidou.com)](https://baomidou.com/pages/226c21/#初始化工程)

## 数据库设计

什么是设计数据库？

有哪些表（模型）？有哪些字段？字段的类型？数据库字段添加索引？表与表之间的关系 

**用户表**：

- id  （主键）bigint
- userAccount 登录账号 varchar
- username 用户昵称 varchar
- avatarUrl 用户头像 varchar
- gender 用户性别 tinyint 0 1
- userPassword 用户密码 varchar
-  phone 用户电话 varchar
- email 用户邮箱 varchar
- userStatus 用户状态 int  0-正常 
- createTime 用户创建时间 datetime
- updateTime 用户更新时间 datetime
- isDeleted 用户是否删除(逻辑删除) tinyint 0 1
- userRole 用户角色 

```mysql
DROP TABLE IF EXISTS user;

create table user_center.user
(
    id           bigint auto_increment comment '(主键) '
        primary key,
    userAccount  varchar(256)                       null comment '登录账号',
    username     varchar(256)                       null comment '用户昵称',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '用户性别',
    userPassword varchar(256)                       not null comment '用户密码',
    phone        varchar(128)                       null comment '用户电话',
    email        varchar(512)                       null comment '用户邮箱',
    userStatus   int      default 0                 not null comment '用户状态 0-正常 ',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '用户创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '用户更新时间',
    isDeleted    tinyint  default 0                 not null comment '用户是否删除',
    userRole         int      default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    authCode     varchar(512)                       null comment '付费系统编号，用于校验用户'
)
    comment '用户表';
```

## **登录/注册** 

### 后端

#### 规整项目目录

![image-20230824200929227](assets/image-20230824200929227.png)

#### 实现基本的数据库操作（操作User表）

1. 创建实体User类 => 与数据库的字段关联  

2. 创建UserMapper映射类 => 实现对user表的增删改查

3. 创建UserMapper.xml => 定义了UserMapper和数据库的关联，还可以用来写一些UserMapper无法完成的复杂SQL

4. 创建UserService接口及其实现类UserServiceImpl => 实现用户业务

   建议使用MybatisX插件自动根据数据库生成，减少重复工作

![image-20230824171617887](assets/image-20230824171617887.png)

![image-20230824171748781](assets/image-20230824171748781.png)

![image-20230824202022420](assets/image-20230824202022420.png)

把generator中自动生成的文件放到对应的包下面

![image-20230824202906943](assets/image-20230824202906943.png)

  4. 为UserService创建一个测试类

     鼠标点击接口类名UserService，使用快捷键Alt + Enter，选择Create Test

     ![image-20230824203336761](assets/image-20230824203336761.png)

     ![image-20230824203504937](assets/image-20230824203504937.png)

     使用GenerateAllSetter插件帮助快速生成所有set方法

     鼠标点击user对象，使用快捷键Alt +Enter 

     ![image-20230824205129488](assets/image-20230824205129488.png)

     `踩坑`：因为Mybatis-Plus会自动把User实体对象中的驼峰属性名，转换成下划线连接的字段，然后执行sql，但我们的字段也是驼峰，所以会报错。可以在application.yml中把修改配置，关掉自动转换。从这点可以看出，我们不用把表字段特意设计成驼峰，字段中的单词就用下划线连接，Mybatis会帮我们自动开启转换，也就不用写下面的配置。

     ```
     mybatis-plus:
       configuration:
         map-underscore-to-camel-case: false
     ```

     [使用配置 | MyBatis-Plus (baomidou.com)](https://baomidou.com/pages/56bac0/#mapunderscoretocamelcase)

     ![image-20230824211655691](assets/image-20230824211655691.png)

#### 实现注册逻辑

**注册接口定义**

1. 接收参数：账号，密码，二次密码

2. 请求类型：POST

   > 请求参数很长时，不建议用GET

3. 请求体：JSON格式

4. 返回值：用户id

 **逻辑**

1. 用户在前端输入账号和密码，二次密码以及校验码（todo)
   1. 非空
   2. 账户长度**不小于**4位
   3. 密码长度**不小于**8位（可以去网上搜一些强校验的正则表达式)
   4. 账户不能和已有用户重复
   5. 账户不能有特殊字符
   6. 密码和二次密码相同  

2. 对密码进行加密（密码不能明文存入数据库，防止用户隐私暴露给公司的员工）
3. 向数据库插入用户数据  

~~~ java
public long userRegister(String userAccount, String userPassword, String checkPassword) {
    // 1 校验
    if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
        return -1;
    }
    if (userAccount.length() < 4) {
        return -1;
    }
    if (userPassword.length() < 8 || checkPassword.length() < 8) {
        return -1;
    }
    //密码和二次密码是否相同
    if (!StringUtils.equals(userPassword, checkPassword)) {
        return -1;
    }
    //账户不能包含特殊字符
    String regex = "^[a-zA-Z0-9]{4,20}$";
    Matcher matcher = Pattern.compile(regex).matcher(userAccount);
    if (!matcher.find()) {
        return -1;
    }
    // 账户不能重复
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(User::getUserAccount, userAccount);
    User user = userMapper.selectOne(wrapper);
    if (user != null) {
        return -1;
    }
    // 2 对密码进行加密
    String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    // 3 向数据库插入数据
    User user1 = new User();
    user1.setUserAccount(userAccount);
    user1.setUserPassword(encryptPassword);
    boolean result = this.save(user1);
    if (!result) {
        return -1;
    }
    return user1.getId();
}
~~~

#### 实现登录逻辑(单机登录=>后续改造为分布式/第三方登录)

**登录接口定义**

1. 接收参数：账号，密码

2. 请求类型：POST

   > 请求参数很长时，不建议用GET

3. 请求体：JSON格式

4. 返回值：基本的用户信息（脱敏）

 **逻辑**

1. 校验用户账号，密码是否合法
   1. 非空
   2. 账户长度**不小于**4位
   3. 密码长度**不小于**8位（可以去网上搜一些强校验的正则表达式)
   4. 账户不能有特殊字符
2. 校验密码是否输入正确
3. 用户信息（脱敏）    
4. 记录用户的登录状态，存在SpringBoot框架封装的TomCat服务器上或者redis中

   1. 浏览器连接服务后，得到一个 session 状态（匿名会话），返回给前端
   2. 登录成功后，得到了登录成功的 session，并且给该session设置一些值（比如用户信息），返回给前端一个设置 cookie 的 ”命令“ 

      **session => cookie** 

   3. 前端接收到后端的命令后，设置 cookie，保存到浏览器内

   4. 前端再次请求后端的时候（相同的域名），在请求头中带上cookie去请求

   5. 后端拿到前端传来的 cookie，找到对应的 session

   6. 后端从 session 中可以取出基于该 session 存储的变量（用户的登录信息、登录名）

5. 返回脱敏后的信息  

> [逻辑删除 | MyBatis-Plus (baomidou.com)](https://baomidou.com/pages/6b03c5/)删除用户记录时，并不是将其移除数据库，而是修改这条记录的isDelete字段的值0为1，标记此纪录已无效，好处是查询时会将此记录从业务中排除，但以后有需要还能设置条件再查询出来，有助于帮助开发者定位问题，防止恶意删除。如果用户记录已被逻辑删除，就不能被查询到，具体配置查询官网

```java
public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
    // 1 校验
    if (StringUtils.isAnyBlank(userAccount, userPassword)) {
        return null;
        //    todo 修改为只定义异常
    }
    if (userAccount.length() < 4) {
        return null;
    }
    if (userPassword.length() < 8) {
        return null;
    }
    //账户不能包含特殊字符
    String regex = "^[a-zA-Z0-9]{4,20}$";
    Matcher matcher = Pattern.compile(regex).matcher(userAccount);
    if (!matcher.find()) {
        return null;
    }
    // 2 对密码进行加密
    String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    // 判断用户是否存在
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
     wrapper.eq(User::getUserAccount, userAccount);
    wrapper.eq(User::getUserPassword, encryptPassword);
    User user = userMapper.selectOne(wrapper);
    // 用户不存在
    if (user == null) {
        log.info("user login failed,for not exists user!");
        return null;
    }
    // 3 用户信息（脱敏）
    User safetyUser = getSafetyUser(user);
    // 4 记录用户的登录状态
    HttpSession session = request.getSession();
    session.setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
    // 5 返回
    return safetyUser;
}
/**
 * 用户拓明
 * @param user
 * @return
 */
@Override
public User getSafetyUser(User user) {
    User safetyUser = new User();
    safetyUser.setId(user.getId());
    safetyUser.setUserAccount(user.getUserAccount());
    safetyUser.setUsername(user.getUsername());
    safetyUser.setAvatarUrl(user.getAvatarUrl());
    safetyUser.setGender(user.getGender());
    safetyUser.setPhone(user.getPhone());
    safetyUser.setEmail(user.getEmail());
    safetyUser.setUserStatus(user.getUserStatus());
    safetyUser.setCreateTime(user.getCreateTime());
    safetyUser.setUserRole(user.getUserRole());
    return safetyUser;
}
```

#### 控制层 Controller 封装请求

> @RestController 适用于编写 restful 风格的 api，返回值默认为 json 类型
>
> controller 层倾向于对请求参数本身的校验，不涉及业务逻辑本身（越少越好）
>
> service 层是对业务逻辑的校验（有可能被 controller 之外的类调用）

使用Auto Filling Java call arguements插件帮助快速生成方法所需要的参数，鼠标点击方法括号，使用快捷键Alt +Enter

创建UserRegisterRequest类来封装注册请求里请求体的参数

使用GenerateSerialVersionUID插件给UserRegisterRequest类生成一个序列化Id，防止序列化时产生冲突。使用快捷键Alt + Insert

![image-20230826143454136](assets/image-20230826143454136.png)

```java
package com.luoying.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 落樱的悔恨
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -1847968683181293735L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
```

创建UserLoginRequest类来封装注册请求里请求体的参数

```java
package com.luoying.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 落樱的悔恨
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -2043028023742840036L;
    private String userAccount;
    private String userPassword;
}
```

创建UserController类来处理前端请求

```java
package com.luoying.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoying.constant.UserConstant;
import com.luoying.model.domain.User;
import com.luoying.model.request.UserLoginRequest;
import com.luoying.model.request.UserRegisterRequest;
import com.luoying.service.UserService;
import com.luoying.service.impl.UserServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author 落樱的悔恨
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return -1L;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1L;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/query")
    public List<User> userListQuery(String username, HttpServletRequest request) {
        // 1 鉴权，仅管理员可查询
        if (!isAdmin(request)) return new ArrayList<>();
        // 2 查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(User::getUsername, username);
        }
        List<User> userList = userService.list(wrapper);
        return userList.stream().map(userService::getSafetyUser).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public Boolean userDelete(@RequestBody long id, HttpServletRequest request) {
        // 1 鉴权，仅管理员可删除
        if (!isAdmin(request)) return false;
        //2 删除
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(UserConstant.USER_LOGIN_STATE);
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}

```

测试

使用IDEA自带的请求工具，向服务器发请求，搭配断点排错

![image-20230826152416228](assets/image-20230826152416228.png)

也可点击绿色图标来发请求 

![image-20230826152748975](assets/image-20230826152748975.png)

发请求

![image-20230826152456817](assets/image-20230826152456817.png)

查看返回结果

![image-20230826152630680](assets/image-20230826152630680.png)

#### 补充用户注册校验逻辑

主要适用于需要预先付费的系统，我现在还用不到，也没积累用户，所以谁都可以注册，先把功能开发出来，以后再用。

先让已经在另一个系统付费的用户（已经积累的可信的系统内部的用户，不是谁都可以注册），且想在我们这个用户中心注册的，输入自己在付费系统中的编号：2-5位

然后在用户中心后端对这个编号进行校验：长度校验，惟一性校验

> 后期从付费系统中拉取数据，清除违规用户  

以下为修改过的代码

```java
package com.luoying.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author 落樱的悔恨
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -1847968683181293735L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String authCode;
}
```

```java
@PostMapping("/register")
public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
    if (userRegisterRequest == null) {
        return -1L;
    }
    String userAccount = userRegisterRequest.getUserAccount();
    String userPassword = userRegisterRequest.getUserPassword();
    String checkPassword = userRegisterRequest.getCheckPassword();
    String authCode = userRegisterRequest.getAuthCode();
    if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, authCode)) {
        return -1L;
    }
    return userService.userRegister(userAccount, userPassword, checkPassword, authCode);
}
```

```java
@Override
public long userRegister(String userAccount, String userPassword, String checkPassword, String authCode) {
    // 1 校验
    if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, authCode)) {
        return -1;
    }
    if (userAccount.length() < 4) {
        return -1;
    }
    if (userPassword.length() < 8 || checkPassword.length() < 8) {
        return -1;
    }
    if (authCode.length()>5){
        return -1;
    }
    //密码和二次密码是否相同
    if (!StringUtils.equals(userPassword, checkPassword)) {
        return -1;
    }
    //账户不能包含特殊字符
    String regex = "^[a-zA-Z0-9]{4,20}$";
    Matcher matcher = Pattern.compile(regex).matcher(userAccount);
    if (!matcher.find()) {
        return -1;
    }
    // 账户不能重复
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(User::getUserAccount, userAccount);
    User user = userMapper.selectOne(wrapper);
    if (user != null) {
        return -1;
    }
    // 付费用户编号不能重复
    LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
    wrapper.eq(User::getAuthCode, authCode);
    user = userMapper.selectOne(wrapper);
    if (user != null) {
        return -1;
    }
    // 2 对密码进行加密
    String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    // 3 向数据库插入数据
    User user1 = new User();
    user1.setUserAccount(userAccount);
    user1.setUserPassword(encryptPassword);
    user1.setAuthCode(authCode);
    boolean result = this.save(user1);
    if (!result) {
        return -1;
    }
    return user1.getId();
}
```

```java
@Override
public User getSafetyUser(User user) {
    if (user == null) {
        return null;
    }
    User safetyUser = new User();
    safetyUser.setId(user.getId());
    safetyUser.setUserAccount(user.getUserAccount());
    safetyUser.setUsername(user.getUsername());
    safetyUser.setAvatarUrl(user.getAvatarUrl());
    safetyUser.setGender(user.getGender());
    safetyUser.setPhone(user.getPhone());
    safetyUser.setEmail(user.getEmail());
    safetyUser.setUserStatus(user.getUserStatus());
    safetyUser.setCreateTime(user.getCreateTime());
    safetyUser.setUserRole(user.getUserRole());
    safetyUser.setAuthCode(user.getAuthCode());
    return safetyUser;
}
```



### 前端

1. 修改登录页面版权信息

![image-20230826202752567](assets/image-20230826202752567.png)

![image-20230826202926935](assets/image-20230826202926935.png)

![image-20230826202948975](assets/image-20230826202948975.png)

2. 修改logo，主标题，副标题

![image-20230826210048372](assets/image-20230826210048372.png)

#### 删除多余代码（蓝色区域即删除区域）

![image-20230826210459209](assets/image-20230826210459209.png)

 ![image-20230826210607115](assets/image-20230826210607115.png)

![image-20230826210701070](assets/image-20230826210701070.png)

修改内容

![image-20230826210944934](assets/image-20230826210944934.png)

  ![image-20230826211526616](assets/image-20230826211526616.png)

![image-20230826211943311](assets/image-20230826211943311.png)

#### 实现用户登录

1. 修改参数

![image-20230826213122509](assets/image-20230826213122509.png)

![image-20230826212931609](assets/image-20230826212931609.png)

![image-20230827113002894](assets/image-20230827113002894.png)

2. 配置路径前缀，修改请求路径

![image-20230827160401540](assets/image-20230827160401540.png)

使用代理解决跨域问题（前端服务器不能直接访问后端服务器）

正向代理： 前端服务器请求代理服务器，代理服务器再请求后端服务器

反向代理：前端服务器请求代理服务器，代理服务器把请求负载均衡到多台后端服务器中的一台

![image-20230827110713744](assets/image-20230827110713744.png)

![image-20230827105115174](assets/image-20230827105115174.png)

#### 实现用户注册

1. 添加注册组件

![image-20230827145244471](assets/image-20230827145244471.png)

2. 修改组件名称为Register

![image-20230827150047564](assets/image-20230827150047564.png)

3. 添加注册路由

![image-20230827150812170](assets/image-20230827150812170.png)

	4. 防止在发起注册请求时，因为Ant Design Pro后台管理系统的特性（用户未登录，会跳到登录页），重定向到登录页

![image-20230827164239848](assets/image-20230827164239848.png)

![image-20230827164706326](assets/image-20230827164706326.png)

5. 删除Register中不需要的代码 

![image-20230827165056030](assets/image-20230827165056030.png)

![image-20230827165206856](assets/image-20230827165206856.png)

6. 修改注册页面

![image-20230827165530308](assets/image-20230827165530308.png)

![image-20230827165815220](assets/image-20230827165815220.png)

![image-20230827170736607](assets/image-20230827170736607.png)

![image-20230827173044311](assets/image-20230827173044311.png)

![image-20230827173216898](assets/image-20230827173216898.png)

![image-20230827172928441](assets/image-20230827172928441.png)

7. 修改注册参数

![image-20230828202350614](assets/image-20230828202350614.png)

![image-20230828202702537](assets/image-20230828202702537.png)

![image-20230828203037682](assets/image-20230828203037682.png)

8. 校验两次输入的密码是否一致

![image-20230828204728656](assets/image-20230828204728656.png)

9. 编写注册请求函数register

![image-20230828205157290](assets/image-20230828205157290.png)

![image-20230828205602965](assets/image-20230828205602965.png)

![image-20230828205927539](assets/image-20230828205927539.png)

10. 简单修改一下逻辑，把和注册无关的全去掉

![image-20230828213426342](assets/image-20230828213426342.png)

11. 在Login组件中添加新用户注册的链接

![image-20230828215749708](assets/image-20230828215749708.png)

#### 补充用户注册校验逻辑

前端补充一个输入框，适配后端

![image-20230831165915043](assets/image-20230831165915043.png)

![image-20230831170038037](assets/image-20230831170038037.png)

![image-20230831170126149](assets/image-20230831170126149.png)

![image-20230831170313046](assets/image-20230831170313046.png)

![image-20230831170407583](assets/image-20230831170407583.png)



## 用户管理（仅有管理员可见）

### 后端

#### 实现用户管理逻辑（查询用户/删除用户）

##### 查询用户

**查询接口定义**

1. 接收参数：用户名username
2. 请求类型：GET

3. 返回值：所有username相同的用户的用户信息

**逻辑**

1. 鉴权
2. 查询

```java
@GetMapping("/query")
public List<User> userListQuery(String username, HttpServletRequest request) {
    // 1 鉴权，仅管理员可查询
    if (!isAdmin(request)) return new ArrayList<>();
    // 2 查询
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.isNotBlank(username)) {
        wrapper.like(User::getUsername, username);
    }
    List<User> userList = userService.list(wrapper);
    return userList.stream().map(userService::getSafetyUser).collect(Collectors.toList());
}
```

##### 删除用户

**删除接口定义**

1. 接收参数：用户id
2. 请求类型：POST
3. 返回值：是否删除成功

**逻辑**

1. 鉴权
2. 删除 

```java
@PostMapping("/delete")
public Boolean userDelete(@RequestBody long id, HttpServletRequest request) {
    // 1 鉴权，仅管理员可删除
    if (!isAdmin(request)) return false;
    //2 删除
    if (id <= 0) {
        return false;
    }
    return userService.removeById(id);
}
/**是否为管理员
 *
 * @param request
 * @return
 */
private boolean isAdmin(HttpServletRequest request) {
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute(UserConstant.USER_LOGIN_STATE);
    return user != null && user.getRole() == UserConstant.ADMIN_ROLE;
}
```

测试与用户登录测试一样

#### 实现获取当前登录用户信息

 ~~~ java
 @GetMapping("/current")
 public User getCurrentUser(HttpServletRequest request) {
     HttpSession session = request.getSession();
     User currentUser = (User) session.getAttribute(USER_LOGIN_STATE);
     if (currentUser == null) {
         return null;
     }
     Long userId = currentUser.getId();
     //todo校验用户是否合法
     User user = userService.getById(userId);
     return userService.getSafetyUser(user);
 }
 ~~~

### 前端

#### 获取用户的登录态

![image-20230829152444642](assets/image-20230829152444642.png)

![image-20230829151151152](assets/image-20230829151151152.png)

![image-20230829152855521](assets/image-20230829152855521.png)

![image-20230829153130302](assets/image-20230829153130302.png)

![image-20230829153316330](assets/image-20230829153316330.png)

![image-20230829153855963](assets/image-20230829153855963.png)

![image-20230829154757013](assets/image-20230829154757013.png)

![image-20230829155441141](assets/image-20230829155441141.png)

![image-20230829155605446](assets/image-20230829155605446.png)

![image-20230829155851564](assets/image-20230829155851564.png)



#### 实现用户管理

1. 创建用户管理组件

![image-20230829164933329](assets/image-20230829164933329.png)

2. 添加用户管理组件的路由

![image-20230829165641589](assets/image-20230829165641589.png)

![image-20230829170556007](assets/image-20230829170556007.png)

![image-20230829170914844](assets/image-20230829170914844.png)

3. 鉴权

![image-20230829172315282](assets/image-20230829172315282.png)

![image-20230829172931058](assets/image-20230829172931058.png)

4. 修改用户管理界面（组件）

![image-20230829195153621](assets/image-20230829195153621.png)

![image-20230829194804304](assets/image-20230829194804304.png)

![image-20230829195525242](assets/image-20230829195525242.png)

![image-20230829195348355](assets/image-20230829195348355.png)

![image-20230829195616263](assets/image-20230829195616263.png)

5. 删除高级表格中不需要的东西

![image-20230829200755780](assets/image-20230829200755780.png)

![image-20230829201508416](assets/image-20230829201508416.png)

![image-20230829202841848](assets/image-20230829202841848.png)

6. 对高级表格中的列进行修改

   ​	ProComponents 高级表单

   1. 通过 columns 定义表格有哪些列
   2. columns 属性
      - dataIndex 对应返回数据对象的属性
      - title 表格列的描述
      - copyable 是否允许复制
      - ellipsis 是否允许缩略
      - tip 是提示
      - valueType：用于声明这一列的类型（dateTime（日期）、select（枚举））

   

![image-20230829201224664](assets/image-20230829201224664.png)

![image-20230829204048662](assets/image-20230829204048662.png)

![image-20230829204126646](assets/image-20230829204126646.png)

![image-20230829204249708](assets/image-20230829204249708.png)

![image-20230829205811888](assets/image-20230829205811888.png)

![image-20230829210016768](assets/image-20230829210016768.png)

![image-20230829210057777](assets/image-20230829210057777.png)

![image-20230829213127408](assets/image-20230829213127408.png)

![image-20230829213403531](assets/image-20230829213403531.png)

![image-20230829215732155](assets/image-20230829215732155.png)

## 用户注销

### 后端

1. **service层代码**

~~~ java
/**
 * 用户注销
 *
 * @param request
 * @return
 */
@Override
public int userLogout(HttpServletRequest request) {
    //移除登录态
    request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
    return 1;    
}
~~~

2. controller层代码

~~~ java
@PostMapping("/loginout")
public Integer userLogout(HttpServletRequest request) {
    if (request==null){
        return null;
    }
    return userService.userLogout(request);
}
~~~

### 前端

![image-20230830211924927](assets/image-20230830211924927.png)

![image-20230830212557790](assets/image-20230830212557790.png)

![image-20230830212910796](assets/image-20230830212910796.png)

![image-20230830212955788](assets/image-20230830212955788.png)

## 前后端代码优化

### 后端代码优化

#### 封装通用结果返回类

目的：给返回结果补充一些信息，告诉前端这个请求在业务层面上是成功还是失败	

```json
//失败
{
	"code": 0, //状态码
    "data": null,
    "message:"error"//具体的执行信息
}
//成功
{
	"code": 1, //状态码
    "data": {
        "name":"rx"
    },
    "message: "ok"//具体的执行信息
}
```

1. 自定义错误码类

```java
package com.luoying.common;

import lombok.Data;


public enum ErrorCode {

    SUCCESS(1, "OK", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据空值", ""),
    JDBC_ERROR(40002, "数据库操作错误", ""),
    NO_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码信息
     */
    private final int code;
    /**
     * 状态码描述
     */
    private final String message;
    /**
     * 具体描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
```

2. 自定义返回结果类

```java
package com.luoying.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用结果返回类
 *
 * @param
 * @author 落樱的悔恨
 */
@Data
public class Result implements Serializable {
    private int code;

    private Object data;

    private String message;

    private String description;

    public Result(int code, Object data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static Result success(Object data) {
        return new Result(1, data, "ok", "");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static Result error(ErrorCode errorCode) {
        return new Result(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public static Result error(ErrorCode errorCode, String description) {
        return new Result(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public static Result error(ErrorCode errorCode, String message, String description) {
        return new Result(errorCode.getCode(), null, message, description);
    }

    public static Result error(int code, Object data, String message, String description) {
        return new Result(code, data, message, description);
    }
}
```



#### 封装全局异常处理类

1. 自定义业务异常类

   作用：

   1. 相对于 java 的异常类，支持更多字段
   2. 自定义构造函数，更灵活 / 快捷的设置字段

```java
package com.luoying.exception;

import com.luoying.common.ErrorCode;

/**
 * 自定义业务异常类
 *
 * @author 落樱的悔恨
 */
public class BusinessException extends RuntimeException {
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
```

1. 编写全局异常处理器

   作用：

   1. 捕获代码中所有的异常，内部消化，让前端 
   2. 同时屏蔽掉项目框架本身的异常（不暴露服务器内部状态）
   3. 集中处理，比如记录日志

   实现：

   1. Spring AOP：在调用方法前后进行额外的处理

```java
package com.luoying.exception;

import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author 落樱的悔恨
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.error("BusinessException:{}", e.getMessage(), e);
        return Result.error(e.getCode(), null, e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException:{}", e);
        return Result.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
```

#### 修改后的UserController和UserServiceImpl

```java
package com.luoying.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import com.luoying.constant.UserConstant;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.User;
import com.luoying.model.request.UserLoginRequest;
import com.luoying.model.request.UserRegisterRequest;
import com.luoying.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.luoying.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author 落樱的悔恨
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户注册请求对象空值");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String authCode = userRegisterRequest.getAuthCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,authCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户注册请求对象属性空值");
        }
        long userId = userService.userRegister(userAccount, userPassword, checkPassword, authCode);
        return Result.success(userId);
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.JDBC_ERROR,"用户登录请求对象空值");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.JDBC_ERROR,"用户登录请求对象属性空值");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return Result.success(user);
    }


    @PostMapping("/loginout")
    public Result userLogout(HttpServletRequest request) {
        if (request==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"HttpServletRequest请求空值");
        }
        int result = userService.userLogout(request);
        return Result.success(result);
    }

    @GetMapping("/current")
    public Result getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN,"用户未登录");
        }
        Long userId = currentUser.getId();
        //todo校验用户是否合法
        User user = userService.getById(userId);

        User safetyUser = userService.getSafetyUser(user);
        return Result.success(safetyUser);
    }

    @GetMapping("/query")
    public Result userListQuery(String username, HttpServletRequest request) {
        // 1 鉴权，仅管理员可查询
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH,"用户无权限");
        // 2 查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(User::getUsername, username);
        }
        List<User> userList = userService.list(wrapper);
        List<User> collect = userList.stream().map(userService::getSafetyUser).collect(Collectors.toList());
        return Result.success(collect);
    }

    @PostMapping("/delete")
    public Result userDelete(@RequestBody long id, HttpServletRequest request) {
        // 1 鉴权，仅管理员可删除
        if (!isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH,"用户无权限");
        //2 删除
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"删除用户的id不能为小于等于0");
        }
        boolean result = userService.removeById(id);
        return Result.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}
```

```java
package com.luoying.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoying.common.ErrorCode;
import com.luoying.constant.UserConstant;
import com.luoying.exception.BusinessException;
import com.luoying.model.domain.User;
import com.luoying.service.UserService;
import com.luoying.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 *
 * @author 落樱的悔恨
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-08-24 20:17:28
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    //盐值混淆密码
    private static final String SALT = "luoying";

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String authCode) {
        // 1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, authCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户注册请求对象属性空值");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码或二次密码长度小于8位");
        }
        if (authCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "权限编号长度大于5位");
        }
        //密码和二次密码是否相同
        if (!StringUtils.equals(userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        //账户不能包含特殊字符
        String regex = "^[a-zA-Z0-9]{4,20}$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号含有特殊字符");
        }
        // 账户不能重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号重复");
        }
        // 付费用户编号不能重复
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAuthCode, authCode);
        count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "权限编号重复");
        }
        // 2 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3 向数据库插入数据
        User user1 = new User();
        user1.setUserAccount(userAccount);
        user1.setUserPassword(encryptPassword);
        user1.setAuthCode(authCode);
        boolean result = this.save(user1);
        if (!result) {
            throw new BusinessException(ErrorCode.JDBC_ERROR, "数据库保存注册用户失败");
        }
        return user1.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.JDBC_ERROR, "用户登录请求对象属性空值");
            //    todo 修改为只定义异常
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度小于8位");
        }
        //账户不能包含特殊字符
        String regex = "^[a-zA-Z0-9]{4,20}$";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号含有特殊字符");
        }
        // 2 对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 判断用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserAccount, userAccount);
        wrapper.eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(wrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed,for not exists user!");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 3 用户信息（脱敏）
        User safetyUser = getSafetyUser(user);
        // 4 记录用户的登录状态
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        // 5 返回
        return safetyUser;
    }

    /**
     * 用户拓明
     *
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户空值");
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setAuthCode(user.getAuthCode());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }
}
```

#### todo全局请求日志和登录校验



### 前端代码瘦身&优化

#### 对接后端的返回值，取 data

![image-20230901145204874](assets/image-20230901145204874.png)

1. 对接注册的返回值

![image-20230901145447006](assets/image-20230901145447006.png)

![image-20230901145917328](assets/image-20230901145917328.png)

2. 全局响应处理：

​		应用场景：我们需要对接口的 **通用响应** 进行统一处理，比如从 response 中取出 data；或者根据 code 去集中处理错误，比如用户未登录、没权限之类的。

​		优势：不用在每个接口请求中都去写相同的逻辑，就比前面对接注册的返回值

​		实现：参考你用的请求封装工具的官方文档，比如 umi-request（https://github.com/umijs/umi-request#interceptor、https://blog.csdn.net/huantai3334/article/details/116780020

）如果你用 **axios**，参考 axios 的文档。创建新的文件，在该文件中配置一个全局请求类。在发送请求时，使用我们自己的定义的全局请求类。

1. 我们参考csdn的那篇文章，写一个请求封装工具

```tsx
/**
 * request 网络请求工具
 * 更详细的 api 文档: https://github.com/umijs/umi-request
 */
import {extend} from 'umi-request';
import {message} from "antd";
import {history} from "@@/core/history";
import {stringify} from "querystring";

const request = extend({})
/**
 * 所以请求拦截器
 */
request.interceptors.request.use((url, options): any => {
    console.log(`do request url=${url}`)
    return {
        url,
        options: {
            ...options,
            headers: {},
        },
    };
});

/**
 * 所有响应拦截器
 */
request.interceptors.response.use(async (response, options): Promise<any> => {
    const res = await response.clone().json();
    console.log('全局响应拦截器', res);
    if (res.code === 40100) {
        message.error('请先登录')
        history.replace({
            pathname: '/user/login',
            search: stringify({
                redirect: location.pathname,
            }),
        });
    } else if (res.code !== 1) {
        message.error(res.description)
    }
    return res.data;
});

export default request;
```

![image-20230901220459363](assets/image-20230901220459363.png)

![image-20230901220714957](assets/image-20230901220714957.png)

![image-20230901221727720](assets/image-20230901221727720.png)

## 部署

### 多环境

[多环境设计_程序员鱼皮的博客-CSDN博客](https://blog.csdn.net/weixin_41701290/article/details/120173283)

多环境：指同一套项目代码在不同的阶段需要根据实际情况来调整配置并且部署到不同的机器上。

为什么需要？

1. 每个环境互不影响
2. 区分不同的阶段：开发 / 测试 / 生产
3. 对项目进行优化：
   1. 本地日志级别
   2. 精简依赖，节省项目体积
   3. 项目的环境 / 参数可以调整，比如 JVM 参数

针对不同环境做不同的事情。



多环境分类：

1. 本地环境（自己的电脑）localhost
2. 开发环境（远程开发）大家连同一台机器，为了大家开发方便
3. 测试环境（测试）开发 / 测试 / 产品，单元测试 / 性能测试 / 功能测试 / 系统集成测试，独立的数据库、独立的服务器
4. 预发布环境（体验服）：和正式环境一致，正式数据库，更严谨，查出更多问题
5. 正式环境（线上，公开对外访问的项目）：尽量不要改动，保证上线前的代码是 “完美” 运行
6. 沙箱环境（实验环境）：为了做实验

#### 前端多环境实战

1. 请求地址

   1. 开发环境：localhost:8000
   2. 线上环境：192.168.253.128:8000(没钱买服务器，用的虚拟机)

   ```js
   //修改环境就相当于在启动项目时调用一个函数，并给这个函数传一个参数，告诉它要使用什么环境
   startFront(env){
   	if(env=='prod'){
   		// 不输出注释
           // 项目优化
           // 修改请求地址
   	}else{
           //保持本地开发逻辑
       }
   }
   ```

   使用了umi框架，build时会自动传入NODE_ENV==production参数，start时会自动传入NODE_ENV==development参数

2. 启动方式

   1. 开发环境：npm run start（本地启动，监听端口、自动更新）

   2. 线上环境：npm run build（项目构建打包），可以使用 serve 工具启动（使用npm i -g serve来安装）

      ![image-20230902150058755](assets/image-20230902150058755.png)

      ![image-20230902150144328](assets/image-20230902150144328.png)

      ![image-20230902150237904](assets/image-20230902150237904-16936381592733.png)

      ![image-20230905154740428](assets/image-20230905154740428.png)

3. 项目的配置

   不同的项目（框架）都有不同的配置文件，umi 的配置文件是 config，可以在配置文件后添加对应的环境名称后缀来区分开发环境和生产环境。参考文档：https://umijs.org/zh-CN/docs/deployment

   1. 开发环境：config.dev.ts
   2. 生产环境：config.prod.ts
   3. 公共配置：config.ts 不带后缀

#### 后端多环境实战

1. 保存建表sql

![image-20230902155336263](assets/image-20230902155336263.png)

2. 在远程服务器上准备一台数据库，并连接

![image-20230902155429937](assets/image-20230902155429937.png)

3. 把本地的数据库复制到远程数据库

   1. 可以在开发工具，navicat或远程数据库上执行sql

   ```sql
   create database if not exists user_center;
   
   use user_center;
   
   create table user
   (
       id           bigint auto_increment comment '(主键) '
           primary key,
       userAccount  varchar(256)                       null comment '登录账号',
       username     varchar(256)                       null comment '用户昵称',
       avatarUrl    varchar(1024)                      null comment '用户头像',
       gender       tinyint                            null comment '用户性别',
       userPassword varchar(256)                       not null comment '用户密码',
       phone        varchar(128)                       null comment '用户电话',
       email        varchar(512)                       null comment '用户邮箱',
       userStatus   int      default 0                 not null comment '用户状态 0-正常 ',
       createTime   datetime default CURRENT_TIMESTAMP not null comment '用户创建时间',
       updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '用户更新时间',
       isDeleted    tinyint  default 0                 not null comment '用户是否删除',
       userRole     int      default 0                 not null comment '用户角色 0-普通用户 1-管理员',
       authCode     varchar(512)                       null comment '付费系统编号，用于校验用户'
   )
       comment '用户表';
   ```

   2. 如果表中数据也要复制，建议就忽略第一步，直接在navicat上操作

   ![image-20230902160340463](assets/image-20230902160340463.png)

   ![image-20230902160440472](assets/image-20230902160440472.png)

   ![image-20230902160512593](assets/image-20230902160512593.png)

4. 修改配置文件

   1. 公共配置

      ![image-20230905154816416](assets/image-20230905154816416.png)

   2. 生产配置

      ![image-20230905154952980](assets/image-20230905154952980.png)

   3. 开发配置

      ![image-20230905155027964](assets/image-20230905155027964.png)

5. 打包

   ![image-20230902164625755](assets/image-20230902164625755.png)

   ![image-20230902164651437](assets/image-20230902164651437.png)

   6. 启动项目

      ​			可以在启动项目时传入环境变量：

      ```bash
      #生产环境
      java -jar .\userCenter-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
      #开发环境
      java -jar .\userCenter-backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
      ```

      ![image-20230902165011544](assets/image-20230902165011544.png)

      ![image-20230902165319246](assets/image-20230902165319246.png)	

主要是改：

- 依赖的环境地址

  - 数据库地址

  - 缓存地址

  - 消息队列地址

  - 项目端口号

- 服务器配置

### 项目部署上线

准备一台Linux服务器（CentOS 8 / 7.6以上），没钱买就用虚拟机

#### 原始部署	

1. **前端**

   1. 安装web服务器（**nginx首选**,apache,tomcat)

   2. 上传前端项目

      ![image-20230902175204687](assets/image-20230902175204687.png)

      ![image-20230902175310433](assets/image-20230902175310433.png)

   3. 配置网页路径

      ![image-20230902180347579](assets/image-20230902180347579.png)

      ![image-20230902180722638](assets/image-20230902180722638.png)

      ![image-20230902181001159](assets/image-20230902181001159.png)

2. **后端**

   1. 安装java,maven

   2. 上传后端项目jar包到服务器

      ![image-20230902181813560](assets/image-20230902181813560.png)

      ![image-20230902183452534](assets/image-20230902183452534.png)

#### 宝塔Linux部署

Linux 运维面板

官方安装教程：https://www.bt.cn/new/download.html

方便管理服务器、方便安装软件

#### Docker容器部署

docker 是容器，可以将项目的环境（比如 java、nginx）和项目的代码一起打包成镜像,再启动项目时，不需要敲一大堆命令，而是直接下载镜像、启动镜像就可以了。

docker 可以理解为软件安装包。

Dockerfile 用于指定构建 Docker 镜像的方法

Dockerfile 一般情况下不需要完全从 0 自己写，建议去 github、gitee 等托管平台参考同类项目（比如 springboot）

Dockerfile 编写：

- FROM 依赖的基础镜像
- WORKDIR 工作目录
- COPY 从本机复制文件
- RUN 执行命令
- CMD / ENTRYPOINT（附加额外参数）指定运行容器时默认执行的命令

```dockerfile
FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY pom.xml
COPY src ./src
RUN mvn package -DskipTests
CMD ["java","-jar","/app/target/userCenter-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
```

```dockerfile
FROM nginx

WORKDIR /usr/local/nginx/html/
USER root

COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf

COPY ./dist  /usr/local/nginx/html/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

```
server {
    listen 80;

    # gzip config
    gzip on;
    gzip_min_length 1k;
    gzip_comp_level 9;
    gzip_types text/plain text/css text/javascript application/json application/javascript application/x-javascript application/xml;
    gzip_vary on;
    gzip_disable "MSIE [1-6]\.";

    root /usr/share/nginx/html;
    include /etc/nginx/mime.types;

    location / {
        try_files $uri /index.html;
    }

}
```

根据 Dockerfile 构建镜像：

```
# 后端
docker build -t userCenter-backend:v0.0.1 .

# 前端
docker build -t userCenter-frontend:v0.0.1 .
```

Docker 构建优化：减少尺寸、减少构建时间（比如多阶段构建，可以丢弃之前阶段不需要的内容）

#### 容器平台

1. 云服务商的容器平台（腾讯云、阿里云）
2. 面向某个领域的容器平台（前端 / 后端微信云托管）**要花钱！**



容器平台的好处：

1. 不用输命令来操作，更方便省事
2. 不用在控制台操作，更傻瓜式、更简单
3. 大厂运维，比自己运维更省心
4. 额外的能力，比如监控、告警、其他（存储、负载均衡、自动扩缩容、流水线）

### 绑定域名

前端项目访问：用户输入网址 => 域名解析服务器（把网址解析为 ip 地址 / 交给其他的域名解析服务） => 

服务器 =>（防火墙）=> nginx 接收请求，找到对应的文件，返回文件给前端 => 前端加载文件到浏览器中（js、css） => 渲染页面



后端项目访问：用户输入网址 => 域名解析服务器 => 服务器 => nginx 接收请求 => 后端项目（比如 8080端口）



nginx 反向代理：替服务器接收请求，转发请求



## 跨域问题解决

浏览器为了用户的安全，仅允许向 **同域名、同端口** 的服务器发送请求。

如果解决跨域

1. 把域名、端口改成相同的

让服务器告诉浏览器：允许跨域（返回 cross-origin-allow 响应头）

2. 网关支持（Nginx）

   ```nginx
   location  /api/ {
       proxy_pass http://127.0.0.1:8080/api/;
       add_header 'Access-Control-Allow-Origin' $http_origin;
       add_header 'Access-Control-Allow-Credentials' 'true';
       add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
       add_header Access-Control-Allow-Headers '*';
       if ($request_method = 'OPTIONS') {
           add_header 'Access-Control-Allow-Credentials' 'true';
           add_header 'Access-Control-Allow-Origin' $http_origin;
           add_header 'Access-Control-Allow-Methods' 'GET, POST, OPTIONS';
           add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range';
           add_header 'Access-Control-Max-Age' 1728000;
           add_header 'Content-Type' 'text/plain; charset=utf-8';
           add_header 'Content-Length' 0;
           return 204;
       }
   }
   ```

   

3. 修改后端服务

   1. 在Controller上配置 @CrossOrigin 注解（**origins**的值就是发起跨域请求的域名**{"http://192.168.253.128:80"}**，**methods**就是请求方式**{RequestMethod.GET,RequestMethod.POST}**,**allowCredentials**=**true**）

   2. 添加 web 全局请求拦截器

      ```java
      @Configuration
      public class WebMvcConfg implements WebMvcConfigurer {
       
          @Override
          public void addCorsMappings(CorsRegistry registry) {
              //设置允许跨域的路径
              registry.addMapping("/**")
                      //设置允许跨域请求的域名
                      //当**Credentials为true时，**Origin不能为星号，需为具体的ip地址【如果接口不带cookie,ip无需设成具体ip】
                      .allowedOrigins("http://localhost:9527", "http://127.0.0.1:9527", "http://127.0.0.1:8082", "http://127.0.0.1:8083")
                      //是否允许证书 不再默认开启
                      .allowCredentials(true)
                      //设置允许的方法
                      .allowedMethods("*")
                      //跨域允许时间
                      .maxAge(3600);
          }
      } 
      ```

   3. 定义新的 corsFilter Bean，参考：https://www.jianshu.com/p/b02099a435bd

## 用户中心项目扩展和规划

1. 功能扩充
   1. 管理员创建用户、修改用户信息、删除用户
   2. 上传头像
   3. 按照更多的条件去查询用户
   4. 更改权限
2. 修改 Bug
3. 项目登录改为分布式 session（单点登录 - redis）
4. 通用性
   1. set-cookie domain 域名更通用，比如改为 *.xxx.com
   2. 把用户管理系统 => 用户中心（之后所有的服务都请求这个后端）
5. 后台添加全局请求拦截器（统一去判断用户权限、统一记录请求日志）



 





 

package com.luoying.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 自定义Swagger接口文档使用的配置
 *
 * @author 落樱的悔恨
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})
public class Swagger2Configuration {
    @Bean(value = "defaultApi2")
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //这里标注controller包的位置
                .apis(RequestHandlerSelectors.basePackage("com.luoying.controller"))
                .paths(PathSelectors.any())
                .build();
    }
 
    //api基本信息的配置，信息会在api文档上显示
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("LUOYING用户中心")
                .description("LUOYING用户中心相关接口的文档")
                .termsOfServiceUrl("http://github.com/1ranxu")
                .contact(new Contact("ranxu","http://github.com/1ranxu","1574925401@qq.com"))
                .version("1.0")
                .build();
    }
}
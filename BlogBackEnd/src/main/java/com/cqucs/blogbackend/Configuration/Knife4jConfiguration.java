package com.cqucs.blogbackend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
//.title("swagger‐bootstrap‐ui‐demo RESTful APIs")
                        .description("博客项目")
                        .termsOfServiceUrl("http://localhost:8080")
                        .contact("xxxx@qq.com")
                        .version("1.0")
                        .build())
//分组名称
                .groupName("博客后端")
                .select()
//这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.cqucs.blogbackend.Controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
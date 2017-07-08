package com.huawei.xdu.cm.config.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.huawei.xdu.cm.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("OpenSource MicroService REST APIs(ContentManager)")
                .description("The APIs here demonstrate creating a service built with Spring Boot")
                .license("HUAWEI")
                .licenseUrl("http://localhost:8088")
                .contact(new Contact("邓文彬", "dengwenbin 00190167", "dengwenbin@huawei.com"))
                .version("1.0")
                .build();

        return apiInfo;
    }
}

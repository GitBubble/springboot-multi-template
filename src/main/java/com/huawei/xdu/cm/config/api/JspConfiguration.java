package com.huawei.xdu.cm.config.api;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JspConfiguration {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JspConfiguration.class);
    }

}
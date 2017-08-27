package com.huawei.ptn;

import com.huawei.ptn.opensourceDigger.task.SendMail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.TemplateEngine;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.huawei.ptn.opensourceDigger.task.sendIsourceMailUti;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@EnableSwagger2
@SpringBootApplication(exclude={FreeMarkerAutoConfiguration.class})
@EnableDiscoveryClient
@EnableScheduling

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class);


        JavaMailSender mailSender = (JavaMailSenderImpl) context.getBean("MailSender");
        TemplateEngine engine = (TemplateEngine) context.getBean("emailTemplateEngine");
        sendIsourceMailUti.delegate(mailSender,engine);
        SendMail.delegate(engine);


    }
}

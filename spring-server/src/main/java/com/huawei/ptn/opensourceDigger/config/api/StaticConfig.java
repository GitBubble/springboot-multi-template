package com.huawei.ptn.opensourceDigger.config.api;


/**
 * Created by d00190167 on 2017/6/26.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.thymeleaf.TemplateEngine;

import org.thymeleaf.spring4.SpringTemplateEngine;

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collections;





@Configuration
@EnableWebMvc
public class StaticConfig extends WebMvcConfigurerAdapter {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"
        };
    private String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    @Override
    public void addResourceHandlers(
        ResourceHandlerRegistry resourceHandlerRegistry) {
        resourceHandlerRegistry.addResourceHandler("/static/**")
                               .addResourceLocations("classpath:/static/");
        resourceHandlerRegistry.addResourceHandler("swagger-ui.html")
                               .addResourceLocations("classpath:/META-INF/resources/");

        if (!resourceHandlerRegistry.hasMappingForPattern("/webjars/**")) {
            resourceHandlerRegistry.addResourceHandler("/webjars/**")
                                   .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }

        if (!resourceHandlerRegistry.hasMappingForPattern("/**")) {
            resourceHandlerRegistry.addResourceHandler("/**")
                                   .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        }

        super.addResourceHandlers(resourceHandlerRegistry);
    }

    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("resources");

        return messageSource;
    }

    /*  //add by dengwenbin , you can add @Bean annotation to load static resource field from file
        @Bean
        public JavaMailSender mailSender() throws IOException {
    
            final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    
            // Basic mail sender configuration, based on emailconfig.properties
            //mailSender.setHost(this.environment.getProperty(HOST));
            //mailSender.setPort(Integer.parseInt(this.environment.getProperty(PORT)));
            //mailSender.setProtocol(this.environment.getProperty(PROTOCOL));
            //mailSender.setUsername(this.environment.getProperty(USERNAME));
            //mailSender.setPassword(this.environment.getProperty(PASSWORD));
    
            // JavaMail-specific mail sender configuration, based on javamail.properties
            final Properties javaMailProperties = new Properties();
            javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());
            mailSender.setJavaMailProperties(javaMailProperties);
    
            return mailSender;
    
        }
    */
    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();

        // Resolver for TEXT emails
        //templateEngine.addTemplateResolver(textTemplateResolver());
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        // Resolver for HTML editable emails (which will be treated as a String)
        //templateEngine.addTemplateResolver(stringTemplateResolver());
        // Message source, internationalization specific to emails
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());

        return templateEngine;
    }

    private ITemplateResolver textTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(1));
        templateResolver.setResolvablePatterns(Collections.singleton("*"));
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        templateResolver.setOrder(Integer.valueOf(1));
        templateResolver.setResolvablePatterns(Collections.singleton("*"));
        templateResolver.setPrefix("/templates/");
        //templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    private static class TemplateMode {
        static String HTML = "HTML";
        static String TEXT = "TEXT";
    }
}

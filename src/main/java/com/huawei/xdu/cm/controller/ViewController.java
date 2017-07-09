package com.huawei.xdu.cm.controller;

import com.alibaba.fastjson.JSON;

import com.huawei.xdu.cm.service.ContentManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hibernate.loader.custom.sql.SQLQueryReturnProcessor.log;

@Controller
public class ViewController {

    @Autowired
    private ContentManagerService service;
    @Value("${name.message:test}")
    private String message = "Hello World";

    @RequestMapping(value = "/cm")
    public String cm() {
        return "Javis and Maven welcome you ~~~";
    }


    @RequestMapping(value = "/")
    public String home(Model model, Locale locale) {
        String lang = locale.getLanguage();
        log.debug("Current Language: " + lang);
        model.addAttribute("settings", lang);
        return lang;
        //return propertyService.getTemplate() + "/index";
    }


    @RequestMapping(value = "/name", method = RequestMethod.GET)
    public String getGroups(HttpServletRequest request,Model model) {

       // List<Map<String, String>> groups = service.queryProjectTops();
/*
                StringUtil.getStringFromObject(request.getParameter("select")),
                StringUtil.getStringFromObject(request.getParameter("where"))*/


        //String jsonRet = JSON.toJSONString(groups);

        //model.addAttribute("retvalue",jsonRet);
        model.addAttribute("message",this.message);

        return "index";

    }

}

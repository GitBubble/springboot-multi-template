package com.huawei.xdu.cm.controller;

/**
 * Created by d00190167 on 2017/6/26.
 */

/**
 * Created by d00190167 on 2017/6/26.
 */

import com.alibaba.fastjson.JSON;
import com.huawei.ipm.base.utils.string.StringUtil;
import com.huawei.xdu.cm.service.ContentManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@RestController
public class ServiceController {


    @Autowired
    private ContentManagerService service;

    @RequestMapping(value = "/restapi", method = RequestMethod.GET)
    public String getGroups(HttpServletRequest request,Model model) {

        List<Map<String, String>> groups = service.queryProjectTops();
/*
                StringUtil.getStringFromObject(request.getParameter("select")),
                StringUtil.getStringFromObject(request.getParameter("where"))*/

        String jsonRet = JSON.toJSONString(groups);

        model.addAttribute(jsonRet);

        return "name";

    }


}
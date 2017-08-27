package com.huawei.ptn.opensourceDigger.controller;

import com.huawei.ipm.base.utils.date.DateUtil;
import com.huawei.ptn.opensourceDigger.model.ContributionTopBean;
import com.huawei.ptn.opensourceDigger.service.ContentManagerService;
import com.huawei.ptn.opensourceDigger.service.ContributionTopService;
import com.huawei.ptn.opensourceDigger.service.impl.ContributionTopServiceImpl;
import com.huawei.ptn.opensourceDigger.utils.DiggerHelper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.hibernate.loader.custom.sql.SQLQueryReturnProcessor.log;

@Controller
@Api(value = "REST API页面控制", description = "REST API入口")
public class ViewController {

    @Autowired
    private ContentManagerService service;

    @Value("${name.message:text}")
    private String message = "Hello World";

    @Autowired
    private ContributionTopService topService;

    @Autowired
    private DiggerHelper helper;

    @Value(value="${config.default.organization}")
    private String default_organization;


    @RequestMapping(value = "/cm")
    public String cm() {
        return "index";
    }


    @RequestMapping(value = "/")
    public String home(Model model, Locale locale) {
        String lang = locale.getLanguage();
        log.debug("Current Language: " + lang);
        model.addAttribute("settings", lang);
        return lang;
        //return propertyService.getTemplate() + "/index";
    }


    @RequestMapping(value = "/web", method = RequestMethod.GET)
    public String getGroups(HttpServletRequest request,Model model) {

        String weekAgoDate = DateUtil.obtainOverTime(DateUtil.getDate(), -7);
        String nextDate = DateUtil.obtainOverTime(DateUtil.getDate(), 0);

        //List<ContributionTopBean> szTops = topService.queryContributionSZTops();
        /*
        //DiggerHelper helper = new DiggerHelper();
        Map<String,List> teamSquad = helper.loadOurTeamSquad(default_organization);
        Map<String,String> condition = new HashMap<String, String>();
        condition.put("sortScope","模型算法开发部-深圳");
        condition.put("period","7");
        condition.put("sortCondition","personalScore");
        condition.put("org",default_organization);

        List<ContributionTopBean> tops = topService.queryContributionDomainTops(teamSquad,condition);

        model.addAttribute("qos",topService.queryGroupRank(tops,"QOS与安全"));
        model.addAttribute("cxb",topService.queryGroupRank(tops,"子卡OM组"));
        model.addAttribute("frm",topService.queryGroupRank(tops,"交叉组"));

        model.addAttribute("projectRank",topService.queryProjectRank(tops));
        */
        model.addAttribute("period",weekAgoDate+"~"+nextDate);
        model.addAttribute("baseUrl","http://10.61.16.223:8088/web");

        return "/rank";

    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String updateAdmin(HttpServletRequest request,Model model)
    {

        return "/admin";
    }

}

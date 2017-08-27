package com.huawei.ptn.opensourceDigger.controller;

/**
 * Created by d00190167 on 2017/6/26.
 */

/**
 * Created by d00190167 on 2017/6/26.
 */


import com.google.gson.Gson;
import com.huawei.ipm.base.utils.string.StringUtil;
import com.huawei.ptn.opensourceDigger.model.ContributionTopBean;
import com.huawei.ptn.opensourceDigger.service.ContentManagerService;
import com.huawei.ptn.opensourceDigger.service.ContributionTopService;


import com.huawei.ptn.opensourceDigger.service.impl.ContributionTopServiceImpl;
import com.huawei.ptn.opensourceDigger.utils.DiggerHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.huawei.ptn.opensourceDigger.task.SendMail;
import java.net.URLDecoder;


@RestController
@Api(value = "REST API页面控制", description = "REST API入口")
public class ServiceController {

    @Value(value= "${config.json.squad.path}")
    private String squad_list_path;

    @Autowired
    private ContentManagerService service;

    @Autowired
    private ContributionTopService topService;

    @Autowired
    private DiggerHelper helper;

    @Autowired
    private SendMail mailer;

    @Value(value="${config.default.organization}")
    private  String  default_organization;

    @RequestMapping(value = "/query", method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})

    public String getGroups(HttpServletRequest request,OutputStream os) {

        String squadName = URLDecoder.decode(StringUtil.getStringFromObject(request.getParameter("id")));
        String sortScopeLevel = URLDecoder.decode(StringUtil.getStringFromObject(request.getParameter("dept")));
        String cycleId  = StringUtil.getStringFromObject(request.getParameter("cycle"));
        String sortId = StringUtil.getStringFromObject(request.getParameter("sort"));
        //DiggerHelper helper = new DiggerHelper();
        Map<String,List> teamSquad = helper.loadOurTeamSquad(squadName);
        Map<String,String> condition = new HashMap<String, String>();
        condition.put("sortScope",sortScopeLevel);
        condition.put("period",cycleId);
        condition.put("sortCondition",sortId);
        condition.put("org",squadName);

        List<ContributionTopBean> tops = topService.queryContributionDomainTops(teamSquad,condition);


        //String rankData = JSON.toJSONString(groups);
        Gson gObj = (new Gson());
        String result = gObj.toJson(tops);

        return result;

        //return rankData;

    }

    @ApiOperation(value = "查找用户", notes = "查找用户", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "唯一id", required = true, dataType = "Long", paramType = "path"),
    })

    /**
     * @Attention:    Do not use modify this funtion entry, every geo-based team should specify their own EMAIL-TEMPLATES
     * @Description:  please reference WolverinE.json to load your own team name by using queryGroupRank
     * @author:       jack.deng
     */

    @RequestMapping(value = "/send")
    public String send(HttpServletRequest request,Model model) {

        ;
  /*      try{*/


            //SendMail post = new SendMail(topService);

            //String jsonRet = JSON.toJSONString(groups);
            //List<ContributionTopBean> szTops = topService.queryContributionSZTops();


            //DiggerHelper helper = new DiggerHelper();
            Map<String,List> teamSquad = helper.loadOurTeamSquad(default_organization);
            Map<String,String> condition = new HashMap<String, String>();
            condition.put("sortScope","模型算法开发部-深圳");
            condition.put("period","7");
            condition.put("sortCondition","personalScore");
            condition.put("org",default_organization);

            List<ContributionTopBean> tops = topService.queryContributionDomainTops(teamSquad,condition);
            //邮件要针对团队设计图片，这里queryGroupRank直接填写 JSON 文件中的团队名称
            topService.createPersonalRankPng("qos",topService.queryGroupRank(tops,"QOS与安全"));
            topService.createPersonalRankPng("cxb",topService.queryGroupRank(tops,"子卡OM组"));
            topService.createPersonalRankPng("frm",topService.queryGroupRank(tops,"交叉组"));
            topService.createProjectRankPng("project",topService.queryProjectRank(tops));
            try
            {
                Thread.currentThread().sleep(10000);//毫秒
            }

            catch(Exception e){}

            List<String> userList = topService.querySendMailUsers(tops);
            mailer.sendMailx(userList,tops);
            return "sent";


    }


    @RequestMapping(value = "/updateConfigList", method = RequestMethod.POST)
    public String updateConfigList(@RequestBody String listString) {

            try {
                //Whatever the file path is.
                File configList = new File(squad_list_path);
                FileOutputStream is = new FileOutputStream(configList);
                OutputStreamWriter osw = new OutputStreamWriter(is);
                Writer w = new BufferedWriter(osw);
                w.write(listString);
                w.close();
            } catch (IOException e) {
                System.err.println("Problem writing to the file configList.txt");
            }


        return "success";


    }

}
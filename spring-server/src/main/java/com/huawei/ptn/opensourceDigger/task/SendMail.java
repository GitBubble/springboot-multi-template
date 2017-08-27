package com.huawei.ptn.opensourceDigger.task;



import com.huawei.ipm.base.utils.date.DateUtil;
import com.huawei.ptn.opensourceDigger.config.api.HttpRestCallApi;
import com.huawei.ptn.opensourceDigger.model.ContributionTopBean;
import com.huawei.ptn.opensourceDigger.service.ContributionTopService;
import com.huawei.ptn.opensourceDigger.service.impl.ContributionTopServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import java.lang.Runnable;
import java.util.*;
import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



/**
 * @class:邮件发送任务
 * @Description:自己定义cron表达式确定邮件发送周期
 * @author:jack.deng.00190167
 * @time:2017年7月26日 上午11:39
 * @param @param time
 * @throws
 */

@Component
@Service
public class SendMail implements Runnable{

    @Value(value="${config.png-server.root.dir}")
    private String rootDir;
    @Value(value="${config.png-server.addr}")
    private String pngCreateServer;
    @Value(value="${config.web.root.dir}")
    private String webBottomUrlLink;

    private sendIsourceMailUti mailUtil = new sendIsourceMailUti();
    private static Logger log = Logger.getLogger(SendMail.class);
    private static TemplateEngine  engine;
    private static ContributionTopService service;
    public static void delegate(TemplateEngine enginX)
    {
        engine = enginX;
    }




    @Override
    //@Scheduled(fixedDelay = 5000)
    //@Scheduled(cron = "1 1 14 1/1 * ?")
    @Scheduled(cron = "0 0 0 ? * MON") //每周一零点开始发邮件
    public void run()
    {

        Map<String,String> postData = new HashMap<String,String>();
        postData.put("pra","task_is_comming");
        Map<String,String> hearder = new HashMap<String,String>();

        HttpRestCallApi.callHttpUtf8PostMethod(pngCreateServer,null,null,null,null,hearder,postData);

    }

    public void sendMailx(List<String> toUserList,List<ContributionTopBean> szTops ){

        //String[] cc = new String[toUserList.size()];
        //cc = toUserList.toArray(cc);
        Map<String, String[]> sendPerson = new  HashMap<String, String[]>();

        String weekAgoDate = DateUtil.obtainOverTime(DateUtil.getDate(), -7);
        String nextDate = DateUtil.obtainOverTime(DateUtil.getDate(), 0);

        String subject = " 流控软件本周内源周报 ";

        //String tox[] = {"qianxiao@huawei.com","widdy.wu@huawei.com","yanchun.liu@huawei.com","yaoyuepeng@huawei.com","linjisheng@huawei.com","xiaolijun1@huawei.com"};
        //String bcc[] = {"d00190167@notesmail.huawei.com.cn"};
        String tox[] = {"d00190167@notesmail.huawei.com.cn"};
        String bcc[] = {"d00190167@notesmail.huawei.com.cn"};
        String cc[] = {"d00190167@notesmail.huawei.com.cn"};
        //add basic infos
        Locale loc = new Locale("zh");
        final Context ctx = new Context(loc);

        //ctx.setVariable("projectRank",service.queryProjectRank(szTops));
        ctx.setVariable("banner","banner");
        ctx.setVariable("qos","qos");
        ctx.setVariable("cxb","cxb");
        ctx.setVariable("frm","frm");
        ctx.setVariable("project","project");
        ctx.setVariable("period",weekAgoDate+"~"+nextDate);
        ctx.setVariable("baseUrl",webBottomUrlLink);//修改底部URL链接地址
        String text = engine.process("html/moutainTop.html", ctx);

        sendPerson.put("toUsers",tox);
        sendPerson.put("ccUsers",cc);
        sendPerson.put("bccUsers",bcc);


        //如果要添加附件，请在这里添加，使用绝对路径 --dengwenbin 2017
        Map<String, FileSystemResource> affixes = new HashMap<String, FileSystemResource>() ;
        FileSystemResource bootstrap = new FileSystemResource(rootDir+"qos.png");
        FileSystemResource dashboard = new FileSystemResource(rootDir+"cxb.png");
        FileSystemResource chartbundle = new FileSystemResource(rootDir+"frm.png");
        FileSystemResource prj = new FileSystemResource(rootDir+"project.png");
        FileSystemResource banner = new FileSystemResource(rootDir+"banner.png");
        affixes.put("qos",bootstrap);
        affixes.put("cxb",dashboard);
        affixes.put("frm",chartbundle);
        affixes.put("project",prj);
        affixes.put("banner",banner);

        mailUtil.sendIsourceEmail(subject,text,sendPerson,affixes);

    }




}


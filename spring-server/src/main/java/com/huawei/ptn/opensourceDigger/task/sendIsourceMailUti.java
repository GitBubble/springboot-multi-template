package com.huawei.ptn.opensourceDigger.task;

/**
 * Created by d00190167 on 2017/7/7.
 */

import com.huawei.ipm.base.container.DefaultSpringHelper;
import com.huawei.ipm.base.container.SpringHelper;
import com.huawei.ipm.base.utils.constant.ConstantFactory;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;

import java.io.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;


import org.apache.commons.io.*;

public class sendIsourceMailUti {

    private static JavaMailSender mailSender;

    private static Logger log = Logger.getLogger(SendMail.class);

    /**
     * @Description:发送邮件的公共方法
     * @author:sWX329996
     * @time:2016年10月26日 下午4:55:32
     * @param @param subject
     * @param @param text
     * @param @param sendPerson
     * @return:void
     * @throws
     */
    public static void delegate(JavaMailSender sender,TemplateEngine engine)
    {
        mailSender = sender;

    }

    private ByteArrayResource getFileData(String filename) {

        final InputStream attachmentInputStream = sendIsourceMailUti.class.getResourceAsStream(filename);
        //log("Resource not found: " + filename, attachmentInputStream);

        ByteArrayResource byteArrayResource = null;

        try {
            byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(attachmentInputStream));
            attachmentInputStream.close();
        }
        catch (IOException e1) {
            //Assert.fail();
        }

        return byteArrayResource;

    }

    public void sendIsourceEmail(String subject, String text,
                                        Map<String, String[]> sendPerson, Map<String, FileSystemResource> affixes) {



        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


            // Create the HTML body using Thymeleaf
            // Prepare the evaluation context, you can add extra context variable here
            /*
            String rootDir = "/static/img/";
            final String banner = "banner";
            final String qos = "qos";
            final String frm = "frm";
            final String cxb = "cxb";
            final String project = "project";
            final String suffix = ".png";

            final ByteArrayResource bannerArray = getFileData(rootDir+banner+suffix);
            final ByteArrayResource qosArray = getFileData(rootDir+qos+suffix);
            final ByteArrayResource frmArray = getFileData(rootDir+frm+suffix);
            final ByteArrayResource cxbArray = getFileData(rootDir+cxb+suffix);
            final ByteArrayResource projectArray = getFileData(rootDir+project+suffix);


            messageHelper.addInline(banner, bannerArray, "image/png");
            messageHelper.addInline(qos, qosArray, "image/png");
            messageHelper.addInline(frm, frmArray, "image/png");
            messageHelper.addInline(cxb, cxbArray, "image/png");
            messageHelper.addInline(project, projectArray, "image/png");
            */

            messageHelper.setFrom(ConstantFactory.EMAIL_FROM);
            String[] sendTo = sendPerson.get("toUsers");
            if(null != sendTo && sendTo.length > 0){
                messageHelper.setTo(sendTo);
            }
            String[] sendCc = sendPerson.get("ccUsers");
            if(null != sendCc && sendCc.length > 0){
                messageHelper.setCc(sendCc);
            }
            String[] sendBcc = sendPerson.get("bccUsers");
            if(null != sendBcc && sendBcc.length > 0){
                messageHelper.setBcc(sendBcc);
            }
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
            if(affixes != null){
                Set<Map.Entry<String, FileSystemResource>> entrySt = affixes.entrySet();
                for(Map.Entry<String, FileSystemResource> entry : entrySt){
                    messageHelper.addInline(entry.getKey(),entry.getValue());
                }
            }
            mailSender.send(mimeMessage);
            log.info("<sendIsourceActivityDetail>--send Email success");
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("send mail failed，the content wrong：", e);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("send mail failed，the send process wrong：", e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send mail failed，undefined wrong：", e);
        }

    }
    /**
     *
     * @Description:发送邮件的公共方法，增加附件文件路径
     * @author:w00356126
     * @time:2017年2月16日 下午12:07:06
     * @param subject
     * @param text
     * @param sendPerson
     * @param affixes
     * @param files
     * @return:void
     * @throws
     */
    public void sendIsourceEmail(Map<String, String> params,String subject, String text,
                                        Map<String, String[]> sendPerson,Map<String, FileSystemResource> affixes,String [] files) {
        SpringHelper helper = DefaultSpringHelper.getInstance();
        String deptThreeName = MapUtils.getString(params, "deptThreeName","");
        JavaMailSenderImpl mail = (JavaMailSenderImpl) helper.getBean("MailSender");;
        if(ConstantFactory.VRPSYSTEMTYPE.equals(deptThreeName)){
            mail = (JavaMailSenderImpl) helper.getBean("VrpMailSender");
        }
        MimeMessage mimeMessage = mail.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            if(ConstantFactory.VRPSYSTEMTYPE.equals(deptThreeName)){
                messageHelper.setFrom(ConstantFactory.EMAIL_FROM_VRP);
            }else{
                messageHelper.setFrom(ConstantFactory.EMAIL_FROM);
            }
            String[] sendTo = sendPerson.get("toUsers");
            if(null != sendTo && sendTo.length > 0){
                messageHelper.setTo(sendTo);
            }
            String[] sendCc = sendPerson.get("ccUsers");
            if(null != sendCc && sendCc.length > 0){
                messageHelper.setCc(sendCc);
            }
            String[] sendBcc = sendPerson.get("bccUsers");
            if(null != sendBcc && sendBcc.length > 0){
                messageHelper.setBcc(sendBcc);
            }
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
            if(affixes != null){
                Set<Map.Entry<String, FileSystemResource>> entrySt = affixes.entrySet();
                for(Map.Entry<String, FileSystemResource> entry : entrySt){
                    messageHelper.addInline(entry.getKey(),entry.getValue());
                }
            }
            FileSystemResource file;
            // 添加附件
            for (String s : files)
            {
                String[] pathArray = s.split("\\\\");
                String fileName = pathArray[pathArray.length-1];
                // 读取附件
                file = new FileSystemResource(new File(s));
                // 向email中添加附件
                messageHelper.addAttachment(MimeUtility.encodeWord(fileName), file);
            }
            mail.send(mimeMessage);
            log.error("<sendIsourceActivityDetail>--send Email success");
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("send mail failed，the content wrong：", e);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("send mail failed，the send process wrong：", e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send mail failed，undefined wrong：", e);
        }

    }
    /**
     * @Description:创建树形图
     * @author:sWX329996
     * @time:2016年11月7日 上午11:36:26
     * @param
     * @return:void
     * @throws
     */
    public static void createTree(){
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("D:/phantomjs.exe D:/workSpace/xdu/web/src/main/webapp/self/duIsourceNew/weeklyMailPhantomjs.js"
                    + " http://localhost:8081/web/self/duIsourceNew/weeklyMail.jsp D:/sendIsourceMail/Tree/result.png");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}


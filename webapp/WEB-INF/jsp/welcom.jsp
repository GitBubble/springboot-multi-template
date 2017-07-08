<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${request.locale}" />
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.net.URLDecoder"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML lang=zh-CN>
<HEAD>
    <META content="IE=8.0000" http-equiv="X-UA-Compatible">
    <TITLE>基于S-F的价值活力曲线</TITLE>
    <META charset=utf-8>
    <%@include file="/common/commonlinks.jsp"%>
   <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
   <META content="IE=8.0000" http-equiv="X-UA-Compatible">
   <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
   <meta http-equiv="pragma" content="no-cache" />
   <meta http-equiv="cache-control" content="no-cache" />
   <meta http-equiv="expires" content="0" />
   <meta name="keywords" content="华为,华为技术,huawei,huawei technology" />
   <META name=GENERATOR content="MSHTML 8.00.7601.18595"><

    <base href="${path}/" />
    <link rel="stylesheet" type="text/css" href="thirdparty/extjs/resources/css/ext-all.css">
    <script type="text/javascript" src="thirdparty/extjs/ext-all-debug.js"></script>
    <script src="self/community/js/init.js"></SCRIPT>
    <script src="self/community/js/service/issueTrend.js"></SCRIPT>
    <STYLE>
        BODY {
            FONT-FAMILY: "ff-tisa-web-pro-1", "ff-tisa-web-pro-2", "Lucida Grande",
            "Helvetica Neue", Helvetica, Arial, "Hiragino Sans GB",
            "Hiragino Sans GB W3", "Microsoft YaHei UI", "Microsoft YaHei",
            "WenQuanYi Micro Hei", sans-serif;
        }
        table {
            font-family:'微软雅黑,宋体,Times';font-weight:bolder;font-size:12px
        }
        .table_link td {
            padding: 8px;
            line-height: 20px;
            text-align: left;
            vertical-align: top;
            border-top: 0px;
        }
        .baseBlockIn1 {
            box-shadow:3px 3px 2px #444;
            -moz-box-shadow:3px 3px 2px #444;
            -webkit-box-shadow:3px 3px 2px #444;
            position:relative;
            z-index:1;
        }
    </STYLE>
    <META name=GENERATOR content="MSHTML 8.00.7600.17267">
</HEAD>
<BODY>
<div class="container" style="width:1200px;left: 200px">
    <div class="container-fluid" style="height: 50px;width:100%">
        <div class="span2"><img alt="" src="self/mainboard/img/logo_huawei.png"></div>
        <div class="span4 offset2">
            <p class="p">
            <h3>特性研发活力曲线</h3>
            </p>
        </div>
        <div class="span4">
            <div style="top: 50px">
                <br/>
                <p style="font-size:15px;" align="right">
                    <fmt:message bundle="${projectmngRes}" key="projectmng_desk_welcome" />${user.name}&nbsp;${user.employId}&nbsp;&nbsp;&nbsp;
                	<a href=community/x-du.action>HOME</a>
                </p>
            </div>
        </div>
    </div>
    <hr/>
    <div class="container-fluid" style="font-family: '微软雅黑'">
        <div class="row-fluid" >
            <div class="row-fluid" style="margin-top: 10px">
                <div class="span3" style="width:150px; margin-top: 10px; margin-left: 20px; height:100px;background-color: #808080;padding-top: 0px; box-shadow:3px 3px 2px #444;-moz-box-shadow:3px 3px 2px #444;-webkit-box-shadow:3px 3px 2px #444;position:relative;z-index:1;">
                    <div class="span12">
                        <p style="vertical-align: middle;margin-top: 40px" align="center" ><font size="3" color="white">GA+3活力曲线（正在规划...） </font></p>
                    </div>
                </div>
                <div class="span3" style="width:150px; margin-top: 10px;margin-left: 20px; height:100px;background-color: #808080;padding-top: 0px; box-shadow:3px 3px 2px #444;-moz-box-shadow:3px 3px 2px #444;-webkit-box-shadow:3px 3px 2px #444;position:relative;z-index:1;" >
                    <div class="span12">
                        <p style="vertical-align: middle;margin-top: 40px" align="center" ><font size="3" color="white">GA+6活力曲线（正在规划...）</font></p>
                    </div>
                </div>
                <div class="span3" style="width:150px; margin-top: 10px;margin-left: 20px; height:100px;background-color: #808080;padding-top: 0px; box-shadow:3px 3px 2px #444;-moz-box-shadow:3px 3px 2px #444;-webkit-box-shadow:3px 3px 2px #444;position:relative;z-index:1;">
                    <div class="span12">
                        <p style="vertical-align: middle;margin-top: 40px" align="center" ><font size="3" color="white">GA+6--GA+12<br>(成长期)活力曲线（正在规划...）</font></p>
                    </div>
                </div>
                <div class="span3" style="width:150px; margin-top: 10px;margin-left: 20px; height:100px;background-color: #808080;padding-top: 0px; box-shadow:3px 3px 2px #444;-moz-box-shadow:3px 3px 2px #444;-webkit-box-shadow:3px 3px 2px #444;position:relative;z-index:1;">
                    <div class="span12">
                        <p style="vertical-align: middle;margin-top: 40px" align="center" ><font size="3" color="white">GA+12--GA+24<br>(成熟期)活力曲线（正在规划...）</font></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</BODY>
</HTML>

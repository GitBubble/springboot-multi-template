#OpenSource挖掘机

[![DEMO](https://github.com/GitBubble/springboot-multi-template/blob/dev2/demo.png)](https://github.com/GitBubble/springboot-multi-template/blob/dev2/demo.png)

##重要更新

2017年7月31日：零点发出第一封内源周报。
2017年7月29日：增加标题彩蛋功能。
2017年7月26日：自动发送邮件功能上线。
2017年7月25日：前台视图排名可以访问。


##功能
1，自动设置周期发送流控深圳团队的内源分析可视化图表数据。
2，WEB展示流控深圳团队内源分析图表数据。
3，可拓展分析iSource网站上所有内源数据并生成可视化图表。

##演示地址:
[http://10.61.16.223:8088/web](http://10.61.16.223:8088/web "DEMO")

##服务器
本项目基于Maven，使用时需要启用两个服务器

1, 基于JAVA的邮件发送和web页面访问服务。
2，基于Node.js的图片生成服务器。


##技术架构

1, 项目包管理采用Maven
2, 框架采用springboot
3, 前台采用Thymeleaf+HTML

##使用指导

1,下载并安装JDK([http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html "下载地址"))

2,设置环境变量 JAVA_HOME 为JDK安装路径，一般情况下安装路径为:C:\Program Files\Java\jdk1.8.0_144\jre 

3,下载并安装Maven: [http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip](http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip "下载地址")

4,设置环境变量，将Maven安装目录配置到PATH变量。安装路径举例：E:\apache-maven-3.5.0

5,如果步骤6、步骤7出问题，运行 mvn --version。 确认命令运行结果和下面类似。

   
    > mvn --version
    Apache Maven 3.0.3 (r1075438; 2011-03-01 01:31:09+0800)
    Maven home: E:\apache-maven-3.5.0\bin\..
    Java version: 1.8.0_144, vendor: Oracle Corporation
    Java home: C:\Program Files\Java\jdk1.8.0_144\jre
    Default locale: zh_CN, platform encoding: GBK
    OS name: "windows 7", version: "6.1", arch: "x86", family: "dos"
	
6,拷贝maven-setting目录的settings.xml到 c:/users/{工号：如d00190167}/.m2/ 目录中。

7,修改SendMail.java 中 **rootDir** 变量为 nodejs-server 的目录路径：
> private static String rootDir="D:\\\opensource-digger\\\nodejs-server\\\";

8,进入spring-server目录，运行 **mvn spring-boot:run** 跑起项目。

9,进入nodejs-server目录，运行** nodejs-export.bat**

10,访问  http://localhost:8088/web  查看web端显示数据。

11, 推荐用 **Intellij IDEA** 调试代码。
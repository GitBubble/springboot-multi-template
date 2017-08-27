var http=require("http");
var url=require("url");
var path=require("path");
var qs=require("querystring");
var process=require("child_process");
var exportChart=require("./exportChart.js");

var route={
    "/": "/",
    test: "/test",
    test1: "/test1"
};

var port=3000;

var accessOriginArray=new Array();
accessOriginArray.push("http://127.0.0.1:8088");
accessOriginArray.push("http://10.130.168.171:8088");
accessOriginArray.push("10.130.147.194");
accessOriginArray.push("10.130.167.47");
accessOriginArray.push("10.74.190.126");
accessOriginArray.push("127.0.0.1");

var isValid=function (reqPath) {
    for (var key in route) {
        if (route[key]==reqPath) {
            return true;
        }
    }
    return false;
};

var writeOutJson=function (query, res) {
    res.write(JSON.stringify(query));
    res.end();
}
var writeOut=function (query, res) {
    res.write(query);
    res.end();
}

http.createServer(function (req, res) {
    console.log(req.connection.remoteAddress);
    // var origin=req.headers.origin;
    var origin=req.connection.remoteAddress.slice(7);
    var httpHeader={"Content-Type": "text/plain;charset=utf-8"};
    var allow=false;
    if (accessOriginArray.indexOf(origin)!=-1) {
        httpHeader["Access-Control-Allow-Origin"]=origin;
        allow=true;
    }
    var pathname=url.parse(req.url).pathname;
    if (!isValid(pathname)) {
        res.writeHead(404, httpHeader);
        res.write("{'errcode':400,'errmsg':'404 页面不见啦'}");
        res.end();
    }
    else if (!allow) {
        res.writeHead(200, httpHeader);
        res.write("{'errcode':404,'errmsg':'No \'Access-Control-Allow-Origin\' header is present on the requested resource.'}");
        res.end();
    }
    else {
        res.writeHead(200, httpHeader);
        if (req.method.toUpperCase()=="POST"||req.method.toUpperCase()=="GET") {
            var postData="";
            req.addListener("data", function (data) {
                postData+=data;
            });
            req.addListener("end", function () {
                var query=qs.parse(postData);
                console.log(query);
                try {
                    switch (pathname) {
                        case route.test: {
                            var chartName=query.chartName;
                            var chartData=JSON.parse(query.chartData);
                            switch (query.chartType) {
                                case "issueNum":
                                    var param=new Object();
                                    param.chartType=query.chartType;
                                    param.chartTitle=query.chartTitle;
                                    param.chartData=query.chartData;
                                    param.chartWidth=query.chartWidth;
                                    param.chartHeight=query.chartHeight;
                                    param.chartName=query.chartName;
                                    process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.issueNum(param))) + " -outfile "+query.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                        console.log(err, stdout, stderr);
                                        console.log("issueNum ok");
                                        writeOut("ok", res);
                                    });
                                    break;
                                case "codePerson":
                                    var param=new Object();
                                    param.chartType=query.chartType;
                                    param.chartTitle=query.chartTitle;
                                    param.chartData=query.chartData;
                                    param.chartWidth=query.chartWidth;
                                    param.chartHeight=query.chartHeight;
                                    param.chartName=query.chartName;
                                    param.deptRelationship=query.deptRelationship;
                                    process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.codePerson(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                        console.log(err, stdout, stderr);
                                        console.log("codePerson ok");
                                        writeOut("ok", res);
                                    });
                                    break;
                                case "codeTrend":
                                    var param=new Object();
                                    param.chartType=query.chartType;
                                    param.chartTitle=query.chartTitle;
                                    param.chartData=query.chartData;
                                    param.chartWidth=query.chartWidth;
                                    param.chartHeight=query.chartHeight;
                                    param.chartName=query.chartName;
                                    param.legend=query.legend;
                                    param.chartY0=query.chartY0;
                                    param.chartY1=query.chartY1;
                                    process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.codeTrend(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                        console.log(err, stdout, stderr);
                                        console.log("codeTrend ok");
                                        writeOut("ok", res);
                                    });
                                    break;
                                    case "dtsOverdue":
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartWidth=query.chartWidth;
                                        param.chartHeight=query.chartHeight;
                                        param.chartName=query.chartName;
                                        param.legend=query.legend;
                                        param.chartY0=query.chartY0;
                                        param.chartY1=query.chartY1;
                                        param.chartXKey=query.chartXKey;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.dtsOverdue(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("dtsOverdue ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                    case "codePerson2":
                                    console.log("codePerson2 start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartWidth=query.chartWidth;
                                        param.chartHeight=query.chartHeight;
                                        param.chartName=query.chartName;
                                        param.legend=query.legend;
                                        param.chartY0=query.chartY0;
                                        param.chartY1=query.chartY1;
                                        // var fs = require('fs');
                                        // fs.writeFile(__dirname+"/1.txt", JSON.stringify(param), (err, data)=>{
                                        //     console.log("ooo");
                                        // });
                                        // param.deptRelationship=query.deptRelationship;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.codePerson2(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("codePerson2 ok");
                                            writeOut("ok", res);
                                        });
                                        break;
									                  case "dtsInResearchAndMaintenance":
                                    console.log("dtsInResearchAndMaintenance start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartWidth=query.chartWidth;
                                        param.chartHeight=query.chartHeight;
                                        param.chartName=query.chartName;
                                        param.legend=query.legend;
                                        param.chartY0=query.chartY0;
                                        param.chartY1=query.chartY1;
                                        // var fs = require('fs');
                                        // fs.writeFile(__dirname+"/1.txt", JSON.stringify(param), (err, data)=>{
                                        //     console.log("ooo");
                                        // });
                                        // param.deptRelationship=query.deptRelationship;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.dtsInResearchAndMaintenance(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("dtsInResearchAndMaintenance ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                    case "personMRTrend":
                                        console.log("personMRTrend start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartName=query.chartName;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.personMRTrend(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("personMRTrend ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                    case "commiterMrNum":
                                        console.log("commiterMrNum start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartName=query.chartName;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.commiterMrNum(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("commiterMrNum ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                    case "receiveIssuePie":
                                    case "receiveMRPie":
                                    case "contributionIssuePie":
                                    case "contributionMRPie":
                                        console.log( query.chartType + " start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartName=query.chartName;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.pieChart(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log(query.chartType + " ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                    case "mrColumnChart":
                                        console.log("mrColumnChart start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartName=query.chartName;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.mrColumnChart(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("mrColumnChart ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                    case "issueColumnChart":
                                        console.log("issueColumnChart start");
                                        var param=new Object();
                                        param.chartType=query.chartType;
                                        param.chartTitle=query.chartTitle;
                                        param.chartData=query.chartData;
                                        param.chartName=query.chartName;
                                        process.exec("phantomjs.exe ./highcharts506/highcharts-convert.js -input " + encodeURIComponent(JSON.stringify(exportChart.issueColumnChart(param))) + " -outfile "+param.chartName+" -constr Chart", {cwd: "./"}, function (err, stdout, stderr) {
                                            console.log(err, stdout, stderr);
                                            console.log("issueColumnChart ok");
                                            writeOut("ok", res);
                                        });
                                        break;
                                default:
                                    writeOut("error type", res);
                            }
                            break;
                        }
                        default:
                        writeOut(query, res);
                    }
                } catch (e) {
                  console.error(e);
                } finally {

                }
            });
        }
        else {
            console.log("not post or get");
        }
    }
}).listen(port, function () {
    console.log("listen on port "+port);
});

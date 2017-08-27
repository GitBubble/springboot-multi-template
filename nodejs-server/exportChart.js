var __exportChartInstance=null;
function exportChart() {

}
exportChart.getInstance=function () {
    if (null==__exportChartInstance) {
        __exportChartInstance=new exportChart();
    }
    return __exportChartInstance;
}
exportChart.prototype.dtsOverdue=function (param) {

}
exportChart.prototype.dtsOverdue=function (param) {
    var colorsArray=["#C00000", "#0070C0", "#70AD47"];
    var categories =new Array();
    var chartData=JSON.parse(param.chartData);
	console.log(param + "111111");
    var dataArray=new Array();
    var legend=JSON.parse(param.legend);
    var series=new Array();
    for (var i = 0; i < legend.length; i++) {
        var data=new Array();
        for (var j = 0; j < chartData.length; j++) {
            data.push(chartData[j][legend[i].key]);
        }
        var type="";
        if (legend[i].type=="column") {
            type="column";
        }
        else if (legend[i].type=="line") {
            type="line";
        }
        else if (legend[i].type=="baseline") {
            type="line";
        }
        var s={
            name: legend[i].name,
            type: type,
            color: colorsArray[i],
            data: data,
            yAxis: parseInt(legend[i].y)
        };
        series.push(s);
    }

    for (var j = 0; j < chartData.length; j++) {
        categories.push(chartData[j][param.chartXKey]);
    }

    var result={
            title: {
                text:  "<b>"+param.chartTitle+"</b>"
            },
            credits: {
                enabled: false
            },
            chart: {
                renderTo: "chart-basic",
                width: parseInt(param.chartWidth),
                height: parseInt(param.chartHeight)
            },
            legend: {
                verticalAlign: "top",
                y: 25
            },
            plotOptions: {
                column: {
                    stacking: "normal",
                    dataLabels: {
                        enabled: true
                    }
                },
                line: {
                    dataLabels: {
                        enabled: true,
                        allowOverlap: true
                    }
                }
            },
            series: series,
            yAxis: [
                {
                    title: {
                        text: param.chartY0
                    },
                    lineWidth: 1,
                    tickWidth: 1
                }
            ],
            xAxis: {
                labels: {
                    style: {
                        "padding-left": "10px"
                    },
                    formatter: function () {
                        if (this.value=="路由器业务开发部"||this.value=="CR软件部"||this.value=="盒式驱动开发部"||this.value=="底层基础特性开发部") {
                            return "<div style=\'color:red\'>"+this.value+"</div>";
                        }
                        return this.value;
                    },
                    useHTML: true,
                    groupedOptions: [
                        {
                            rotation: 0,
                            style: {
                                "padding-left": "0px"
                            }
                        }
                    ],
                    rotation: -60
                },
                categories: categories
            }
        };
    return result;
}
exportChart.prototype.dtsInResearchAndMaintenance=function (param) {
   var colorsArray=["#C00000", "#0070C0", "#70AD47"];
    var chartTitle=param.chartTitle;
    var chartData=JSON.parse(param.chartData);
    var chartWidth=parseInt(param.chartWidth);
    var chartHeight=parseInt(param.chartHeight);
    var legend=JSON.parse(param.legend);
    var chartY0=param.chartY0;
    var chartY1=param.chartY1;
    var rotation=-90;
    //去除五级部门为空的记录
    var dataArray=new Array();
    if (null!=chartData[0].deptFour) {
        for (var i = chartData.length-1; i >= 0; i--) {
            if (chartData[i].deptData.length==0) {
              chartData.splice(i, 1);
            }
        }
    }
    else {
        rotation=-90;
    }
    if (chartData.length==0) {
        return;
    }
    var dataArray=new Array();
    var categories=new Array();
    //组合data
    if (null!=chartData[0].dtsType) {
        for (var i = 0; i < chartData.length; i++) {
            for (var j = 0; j < chartData[i].deptData.length; j++) {
                dataArray.push(chartData[i].deptData[j]);
            }
        }
    }
    else {
        for (var i = 0; i < chartData.length; i++) {
            dataArray.push(chartData[i]);
        }
    }
    //生成x轴categories
    if (null!=chartData[0].dtsType) {
        for (var i = 0; i < chartData.length; i++) {
            var item=new Object();
            item.name=chartData[i].dtsType;
            item.categories=new Array();
            for (var j = 0; j < chartData[i].deptData.length; j++) {
                item.categories.push(chartData[i].deptData[j].deptName);
            }
            categories.push(item);
        }
    }
    else {
        for (var i = 0; i < chartData.length; i++) {
            categories.push(chartData[i].deptName);
        }
    }
    //生成y轴
    var yAxis=new Array();
    if (null!=chartY0) {
        yAxis.push({
            title: {
                text: chartY0
            },
            lineWidth: 1,
            tickWidth: 1
        });
    }
    if (null!=chartY1) {
        yAxis.push({
            title: {
                text: chartY1,
                rotation: -90
            },
            lineWidth: 1,
            tickWidth: 1,
            opposite: true
        });
    }
    //生成series
    var series=new Array();
    for (var i = 0; i < legend.length; i++) {
        var item=new Object();
        item.name=legend[i].name;
        item.yAxis=parseInt(legend[i].y);
        item.color=colorsArray[parseInt(legend[i].color)];
        var data=new Array();
		if (null!=chartData[0].dtsType) {
        for (var j = 0; j < chartData.length; j++) {
            for (var k = 0; k < chartData[j].deptData.length; k++) {
			   // var legendKey = chartData[j].dtsType;
			   // data.push(chartData[j].deptData[k][legend[i].legendKey]);
               if(chartData[j].dtsType=="在研"){
				  data.push(chartData[j].deptData[k][legend[i].key1]);
			   }else if (chartData[j].dtsType=="维护") {
				  data.push(chartData[j].deptData[k][legend[i].key2]);
			   }
            }
        }
        }
        item.data=data;
        switch (legend[i].type) {
            case "column":
                {
                    item.type="column";
                }
                break;
            case "line":
                {
                    item.type="line";
                }
                break;
            case "baseline":
                {
                    item.type="line";
                    item.dashStyle="Dash";
                    item.data[0]={y: item.data[0], dataLabels: {
                        enabled: true,
                        borderRadius: 5,
                        backgroundColor: 'rgba(252, 255, 197, 0.7)',
                        borderWidth: 1,
                        borderColor: '#AAA',
                        y: -6,
                        format: "基线:{y}"
                    }}
                    item.dataLabels={
                        enabled: false
                    }
                }
                break;

            default:

        }
        series.push(item);
    }
	var plotOptionsSeriesPoint = {};
    if(dataArray.length<8){
		plotOptionsSeriesPoint.pointWidth = 60;
	}
    //生成配置
    var result={
        title: {
            text: chartTitle
        },
        credits: {
            enabled: false
        },
        chart: {
            renderTo: "chart-basic",
            width: chartWidth,
            height: chartHeight
        },
        legend: {
            verticalAlign: "top",
            y: 25
        },
        plotOptions: {
            column: {
                stacking: "normal",
                dataLabels: {
                    enabled: true
                }
            },
            line: {
                dataLabels: {
                    enabled: true,
                    allowOverlap: true
                }
            },
			series:plotOptionsSeriesPoint
        },
        series: series,
        yAxis: yAxis,
        xAxis: {
            labels: {
                style: {
                    "padding-left": "10px"
                },
                useHTML: true,
                groupedOptions: [
                    {
                        rotation: 0,
                        style: {
                            "padding-left": "0px"
                        }
                    }
                ],
                rotation: rotation
            },
            categories: categories
        }
    };
    return result;
}
exportChart.prototype.codeTrend=function (param) {
    // console.log(param);
    var chartData=JSON.parse(param.chartData);

    // console.log(chartData);
    var colorsArray=["#C00000", "#C00000", "#0070C0", "#0070C0", "#70AD47", "#70AD47"];
    var categories=new Array();
    for (var i = 0; i < chartData.length; i++) {
        categories.push(chartData[i].deptDate);
    }
    // var issueNewNum=new Array();
    // var issueCodeRate=new Array();
    var legend=param.legend;

    var series=new Array();
    legend=JSON.parse(legend)
    for (var i = 0; i < legend.length; i+=2) {
        var data1=new Array();
        for (var j = 0; j < chartData.length; j++) {
            data1.push(chartData[j][legend[i].key]);
        }
        series.push({
            name: legend[i].name,
            type: "line",
            color: colorsArray[i],
            data: data1,
            yAxis: parseInt(legend[i].y)
        });
        var data2=new Array();
        for (var j = 0; j < chartData.length; j++) {
            data2.push(chartData[j][legend[i+1].key]);
        }
        data2[0]={y: data2[0], dataLabels: {
            enabled: true,
            borderRadius: 5,
            backgroundColor: 'rgba(252, 255, 197, 0.7)',
            borderWidth: 1,
            color: colorsArray[i],
            borderColor: '#AAA',
            y: -6,
            format: "基线:{y}"
        }};
        series.push({
            name: legend[i+1].name,
            type: "line",
            color: colorsArray[i+1],
            data: data2,
            yAxis: parseInt(legend[i+1].y),
            dashStyle: "Dash",
            dataLabels: {
                enabled: false
            }
        });
    }
    var yAxis=new Array();
    yAxis.push({
            title: {
                // text: "issue数(个)"
                text: param.chartY0
            },
            lineWidth: 1,
            tickWidth: 1,
            min: 0
        });
    if (param.chartY1!=null) {
        yAxis.push({
                title: {
                    // text: "issue/代码量(个/kloc)",
                    text: param.chartY1,
                    rotation: -90,
                },
                lineWidth: 1,
                tickWidth: 1,
                opposite: true,
                min: 0
            });
    }

    var result={
        title: {
            text: "<b>"+param.chartTitle+"</b>"
        },
        credits: {
            enabled: false
        },
        chart: {
            renderTo: "chart-basic2",
            width: param.chartWidth,
            height: param.chartHeight
        },
        legend: {
            verticalAlign: "top",
            y: 25
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    allowOverlap: true
                }
            }
        },
        xAxis: {
            categories: categories,
            rotation: -90
        },
        yAxis: yAxis,
        series: series
    };
    return result;
}
exportChart.prototype.issueNum=function (param) {
    var categories=new Array();
    var issueNewNum=new Array();
    var issueCodeRate=new Array();
    var chartData=JSON.parse(param.chartData);
    for (var i = 0; i < chartData.length; i++) {
        categories.push(chartData[i].deptName);
        issueNewNum.push(parseFloat(chartData[i].issueNewNum));
        issueCodeRate.push(parseFloat(chartData[i].issueCodeRate));
    }
    var result={
        title: {
            text:  "<b>"+param.chartTitle+"</b>"
        },
        credits: {
            enabled: false
        },
        chart: {
            width: param.chartWidth,
            height: param.chartHeight
        },
        legend: {
            verticalAlign: "top",
            y: 25
        },
        plotOptions: {
            column: {
                stacking: "normal",
                dataLabels: {
                    enabled: true,
                    color: "black",
                    inside: false
                }
            },
            line: {
                dataLabels: {
                    enabled: true,
                    allowOverlap: true
                }
            }
        },
        xAxis: {
            labels: {rotation: -45},
            categories: categories
        },
        yAxis: [
            {
                title: {
                    text: "issue数(个)"
                },
                lineWidth: 1,
                tickWidth: 1
            },
            {
                title: {
                    text: "issue/代码量(个/kloc)",
                    rotation: -90
                },
                lineWidth: 1,
                tickWidth: 1,
                opposite: true
            }
        ],
        series: [
            {
                name: "issue数",
                type: 'column',
                color: "#0070C0",
                data: issueNewNum
            },
            {
                name: "issue/代码量",
                type: "line",
                yAxis: 1,
                color: "#C00000",
                data: issueCodeRate
            }
        ]
    };
    return result;
}
exportChart.prototype.codePerson2=function (param) {
    var colorsArray=["#C00000", "#0070C0", "#70AD47"];
    var chartTitle=param.chartTitle;
    var chartData=JSON.parse(param.chartData);
    var chartWidth=parseInt(param.chartWidth);
    var chartHeight=parseInt(param.chartHeight);
    var legend=JSON.parse(param.legend);
    var chartY0=param.chartY0;
    var chartY1=param.chartY1;
    var rotation=-90;
    //去除五级部门为空的记录
    var dataArray=new Array();
    if (null!=chartData[0].deptFour) {
        for (var i = chartData.length-1; i >= 0; i--) {
            if (chartData[i].dataFive.length==0) {
              chartData.splice(i, 1);
            }
        }
    }
    else {
        rotation=-60;
    }
    if (chartData.length==0) {
        return;
    }
    var dataArray=new Array();
    var categories=new Array();
    //组合data
    if (null!=chartData[0].deptFour) {
        for (var i = 0; i < chartData.length; i++) {
            for (var j = 0; j < chartData[i].dataFive.length; j++) {
                dataArray.push(chartData[i].dataFive[j]);
            }
        }
    }
    else {
        for (var i = 0; i < chartData.length; i++) {
            dataArray.push(chartData[i]);
        }
    }
    //生成x轴categories
    if (null!=chartData[0].deptFour) {
        for (var i = 0; i < chartData.length; i++) {
            var item=new Object();
            item.name=chartData[i].deptFour;
            item.categories=new Array();
            for (var j = 0; j < chartData[i].dataFive.length; j++) {
                item.categories.push(chartData[i].dataFive[j].deptName);
            }
            categories.push(item);
        }
    }
    else {
        for (var i = 0; i < chartData.length; i++) {
            categories.push(chartData[i].deptName);
        }
    }
    //生成y轴
    var yAxis=new Array();
    if (null!=chartY0) {
        yAxis.push({
            title: {
                text: chartY0
            },
            lineWidth: 1,
            tickWidth: 1
        });
    }
    if (null!=chartY1) {
        yAxis.push({
            title: {
                text: chartY1,
                rotation: -90
            },
            lineWidth: 1,
            tickWidth: 1,
            opposite: true
        });
    }
    //生成series
    var series=new Array();
    for (var i = 0; i < legend.length; i++) {
        var item=new Object();
        item.name=legend[i].name;
        item.yAxis=parseInt(legend[i].y);
        item.color=colorsArray[parseInt(legend[i].color)];
        var data=new Array();
        for (var j = 0; j < dataArray.length; j++) {
            data.push(parseFloat(dataArray[j][legend[i].key]));
        }
        item.data=data;
        switch (legend[i].type) {
            case "column":
                {
                    item.type="column";
                }
                break;
            case "line":
                {
                    item.type="line";
                }
                break;
            case "baseline":
                {
                    item.type="line";
                    item.dashStyle="Dash";
                    item.data[0]={y: item.data[0], dataLabels: {
                        enabled: true,
                        borderRadius: 5,
                        backgroundColor: 'rgba(252, 255, 197, 0.7)',
                        borderWidth: 1,
                        borderColor: '#AAA',
                        y: -6,
                        format: "基线:{y}"
                    }}
                    item.dataLabels={
                        enabled: false
                    }
                }
                break;

            default:

        }
        series.push(item);
    }
	var plotOptionsSeriesPoint = {};
    if(dataArray.length<8){
		plotOptionsSeriesPoint.pointWidth = 60;
	}
    //生成配置
    var result={
        title: {
            text: chartTitle
        },
        credits: {
            enabled: false
        },
        chart: {
            renderTo: "chart-basic",
            width: chartWidth,
            height: chartHeight
        },
        legend: {
            verticalAlign: "top",
            y: 25
        },
        plotOptions: {
            column: {
                stacking: "normal",
                dataLabels: {
                    enabled: true
                }
            },
            line: {
                dataLabels: {
                    enabled: true,
                    allowOverlap: true
                }
            },
			series:plotOptionsSeriesPoint
        },
        series: series,
        yAxis: yAxis,
        xAxis: {
            labels: {
                style: {
                    "padding-left": "10px"
                },
                useHTML: true,
                groupedOptions: [
                    {
                        rotation: -20,
                        style: {
                            "padding-left": "0px"
                        }
                    }
                ],
                rotation: rotation
            },
            categories: categories
        }
    };
    return result;
}
exportChart.prototype.codePerson=function (param) {
    var actualCodePerson=new Array();
    var deviatePerson=new Array();
    var productionEffciency=new Array();
    var chartData=JSON.parse(param.chartData);
    var deptRelationship=JSON.parse(param.deptRelationship);
    var dataArray=new Array();
    for (var i = 0; i < deptRelationship.length; i++) {
        if (deptRelationship[i].deptFive.length==0) {
            continue;
        }
        for (var j = 0; j < deptRelationship[i].deptFive.length; j++) {
            dataArray.push(deptRelationship[i].deptFive[j]);
        }
    }
    for (var i = 0; i < dataArray.length; i++) {
        for (var j = 0; j < chartData.length; j++) {
            if (dataArray[i]==chartData[j].deptName) {
                actualCodePerson.push(chartData[j].actualCodePerson);
                deviatePerson.push(chartData[j].deviatePerson);
                productionEffciency.push(chartData[j].productionEffciency);
            }
        }
    }
    for (var i = deptRelationship.length-1; i >= 0 ; i--) {
        if (deptRelationship[i].deptFive.length==0) {
            deptRelationship.splice(i, 1);
        }
    }
    for (var i = 0; i < deptRelationship.length; i++) {
        deptRelationship[i].name=deptRelationship[i].deptFour;
        deptRelationship[i].categories=deptRelationship[i].deptFive;
        delete deptRelationship[i].deptFour;
        delete deptRelationship[i].deptFive;
    }
    var result={
            title: {
                text:  "<b>"+param.chartTitle+"</b>"
            },
            credits: {
                enabled: false
            },
            chart: {
                renderTo: "chart-basic",
                width: parseInt(param.chartWidth),
                height: parseInt(param.chartHeight)
            },
            legend: {
                verticalAlign: "top",
                y: 25
            },
            plotOptions: {
                column: {
                    stacking: "normal",
                    dataLabels: {
                        enabled: true
                    }
                },
                line: {
                    dataLabels: {
                        enabled: true,
                        allowOverlap: true
                    }
                }
            },
            series: [
                {
                    name: "偏差人力",
                    type: 'column',
                    color: "#C00000",
                    data: deviatePerson
                },
                {
                    name: "实际编码人力",
                    type: 'column',
                    color: "#0070C0",
                    data: actualCodePerson
                },
                {
                    name: "生产效率",
                    type: "line",
                    yAxis: 1,
                    color: "#70AD47",
                    data: productionEffciency
                }
            ],
            yAxis: [
                {
                    title: {
                        text: "/人"
                    },
                    lineWidth: 1,
                    tickWidth: 1
                },
                {
                    title: {
                        text: "loc/人月",
                        rotation: -90
                    },
                    lineWidth: 1,
                    tickWidth: 1,
                    opposite: true
                },
            ],
            xAxis: {
                labels: {
                    style: {
                        "padding-left": "10px"
                    },
                    formatter: function () {
                        if (this.value=="路由器业务开发部"||this.value=="CR软件部"||this.value=="盒式驱动开发部"||this.value=="底层基础特性开发部") {
                            return "<div style=\'color:red\'>"+this.value+"</div>";
                        }
                        return this.value;
                    },
                    useHTML: true,
                    groupedOptions: [
                        {
                            rotation: 0,
                            style: {
                                "padding-left": "0px"
                            }
                        }
                    ],
                    rotation: -90
                },
                // categories: [
                //     {
                //         name: "电信以太产品部",
                //         categories: ["电信以太框式软件开发部", "电信以太盒式软件开发部", "电信以太运维支撑部", "电信以太集成与验证部"]
                //     },
                //     {
                //         name: "路由器产品部",
                //         categories: ["路由器业务开发部", "转发适配", "CR软件部", "转发流控", "路由器产品支撑部", "路由器运维支撑部", "路由器项目支撑部", "路由器集成测试部", "路由器SDV测试部"]
                //     },
                //     {
                //         name: "驱动组件产品部",
                //         categories: ["盒式驱动开发部", "底层基础特性开发部", "转发芯片驱动开发部"]
                //     },
                //     {
                //         name: "业务策略网关产品部",
                //         categories: ["业务策略网关VNE集成开发部", "业务策略网关业务开发部", "业务策略网关运维支撑部", "业务策略网关集成与验证部"]
                //     },
                //     {
                //         name: "业务组件产品部",
                //         categories: ["承载业务开发部", "二层与基础业务开发部", "IPRAN集成开发部", "管道业务开发部", "业务SDV测试部", "业务集成测试部"]
                //     }
                // ]
                categories: deptRelationship
            }
        };
    return result;
}

exportChart.prototype.personMRTrend=function (param) {
    var chartData=JSON.parse(param.chartData);
    var categories=new Array();
    for (var i = 0; i < chartData.length; i++) {
        categories.push(chartData[i].date);
    }

    var series=new Array();
    var data1=new Array();
    var data2=new Array();
    for (var j = 0; j < chartData.length; j++) {
        data1.push(chartData[j].submitMr);
        data2.push(chartData[j].mergedMr);
    }
    series.push({
        name: "提交MR",
        type: "line",
        color: "#C00000",
        data: data1,
        yAxis: 0
    });
    series.push({
        name: "合入MR",
        type: "line",
        color: "#0070C0",
        data: data2,
        yAxis: 0
    });

    var yAxis=new Array();
    yAxis.push({
            title: {
                text: "数量"
            },
            lineWidth: 1,
            tickWidth: 1,
            min: 0
        });

    var result={
        title: {
            text: "<b>"+param.chartTitle+"</b>"
        },
        credits: {
            enabled: false
        },
        chart: {
            renderTo: "chart-basic2",
            width: "600",
            height: "300"
        },
        legend: {
            verticalAlign: "top",
            y: 25
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    allowOverlap: true
                }
            }
        },
        xAxis: {
            categories: categories,
            rotation: -90
        },
        yAxis: yAxis,
        series: series
    };
    return result;
}

exportChart.prototype.commiterMrNum=function (param) {
    var chartData=JSON.parse(param.chartData);
    var categories=new Array();
    for (var i = 0; i < chartData.length; i++) {
        categories.push(chartData[i].commiter);
    }

    var data1=new Array();
    var data2=new Array();
    var data3=new Array();
    for (var j = 0; j < chartData.length; j++) {
        data1.push(chartData[j].totalMrNum);
        data2.push(chartData[j].mergeMrNum);
        data3.push(chartData[j].rejectMrNum);
    }

    var series=new Array();
    series.push({
        name: "处理MR总数",
        data: data1
    });
    series.push({
        name: "合入MR数",
        data: data2
    });
    series.push({
        name: "驳回MR数",
        data: data3
    });

    var result = {
        chart: {
            type: 'column',
            renderTo: "chart-basic2",
            width: "600",
            height: "300"
        },
        title: {
            text: param.chartTitle
        },
        xAxis: {
            categories: categories,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: '个数'
            }
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series:series
    };
    return result;
}

exportChart.prototype.pieChart=function (param) {
    var chartData=JSON.parse(param.chartData);
    var piedata = new Array();
    for (var i = 0; i < chartData.length; i++) {
        for (var key in chartData[i]){
            piedata.push({
                y:chartData[i][key],
                name:key
            });
        }
    }

    var result = {
        chart: {
           plotBackgroundColor: null,
           plotBorderWidth: null,
           plotShadow: false,
           type: 'pie',
           renderTo: "chart-basic2",
           width: "600",
           height: "300"
         },
         title: {
             text: param.chartTitle
         },
         plotOptions: {
             pie: {
                 allowPointSelect: true,
                 cursor: 'pointer',
                 dataLabels: {
                     enabled: false
                 },
                 showInLegend: true
             }
         },
         series: [{
             name: param.chartTitle,
             colorByPoint: true,
             data: piedata
         }]
    };

    return result;
}

exportChart.prototype.mrColumnChart=function (param) {
    var chartData=JSON.parse(param.chartData);

    var result = {
        chart: {
            type: 'column',
            renderTo: "chart-basic2",
            width: "600",
            height: "300"
        },
        title: {
            text: param.chartTitle
        },
        xAxis: {
            categories: [
                '提交MR个数',
                '提交MR被评论数',
                'MR评论数'
            ],
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: '个数'
            }
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: '个人信息',
            data: [chartData[0].mrNum, chartData[0].bnmrNum, chartData[0].nmrNum]

        }, {
            name: '五级部门均值',
            data: [chartData[1].mrNum, chartData[1].bnmrNum, chartData[1].nmrNum]

        }, {
            name: '四级部门均值',
            data: [chartData[2].mrNum, chartData[2].bnmrNum, chartData[2].nmrNum]

        }, {
            name: '三级部门均值',
            data: [chartData[3].mrNum, chartData[3].bnmrNum, chartData[3].nmrNum]
        }]
    };

    return result;
}

exportChart.prototype.issueColumnChart=function (param) {
    var chartData=JSON.parse(param.chartData);
    var topColors = ['#7cb5ec', '#434348', '#90ed7d'];
					 
	var backgroundColor = [].fill.call(new Array(chartData[0]["name"].length),'#91e8e1');
	for(var i=0;i<topColors.length;i++){backgroundColor[i] = topColors[i];}
	
    var result = {
		chart: {
            type: 'bar',
            //renderTo: "chart-basic2",
            width: "500",
            height: "600"
        },
        title: {
            text: param.chartTitle
        },
		subtitle:{
			text: param.subTitle
		},
        xAxis: {
            categories: chartData[0]["name"]
            //crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: '分数'
            }
        },
        plotOptions: {
            column: {
                pointPadding: 0.3,
                borderWidth: 0
            }
        },
        series: [{
            name: '内源分数',
			colorByPoint:true,
			//colors:backgroundColor,
            data: chartData[0]["scores"].map(Number),
        }]
    };

    return result;
}

module.exports=exportChart.getInstance();

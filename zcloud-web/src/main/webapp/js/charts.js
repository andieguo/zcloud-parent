//获取系统当前时间
function getTheDate() {
    var myDate = new Date();
    var year = (myDate.getFullYear());
    var month = (myDate.getMonth() + 1);
    var date = (myDate.getDate());
    var hours = (myDate.getHours());
    var minutes = (myDate.getMinutes());
    var seconds = (myDate.getSeconds());
    return (year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds);
} 

function onclick_2(obj){
            $("#qy").attr("style", "z-index:4");
            $("#qj").attr("style", "z-index:3");
            $("#zj").attr("style", "z-index:2");
            $("#yh").attr("style", "z-index:1");
            $($(obj).attr("href")).attr("style", "z-index:5");
}

//加载sensor
function loadSensor(userid) {
	if(userid == 0) {
		var sensorSelect = $("#sensorSelect");
		sensorSelect.empty(); //初始化否则会追加 
		sensorSelect.append("<option value='0'>请选择...</option>");
		return;
	}
	var url = "rest/sensor/userid/" + userid;//要么使用相对路径"rest/sensor/userid/"，要么使用绝对路径："/zcloud-web/rest/sensor/userid/"
	$.ajax({//调用JQuery提供的Ajax方法 
		type : "GET",
		url : url,
		dataType : "json",
		success : function(data){//回调函数 
			console.log('data：',data);
			console.log('length：',data.sensor.length);
			console.log("title：",data.sensor[0].title);
			var sensorSelect = $("#sensorSelect");
			sensorSelect.empty(); //初始化否则会追加 
			for (var i = 0; i < data.sensor.length; i++) {
				sensorSelect.append("<option channal='"+data.sensor[i].channal+"' unit='"+data.sensor[i].unit+"'>"
						+ data.sensor[i].title + "</option>");
			}
		},
		error : function() {
			alert("系统出现问题");
		}
	});
}

//解析JSON格式数据为图表数据
function analyze(data){
	function Result(name, data) {
		this.name = name;
		this.data = data;
	}
	var at=[],min=[],max=[],avg=[],classify=[],curveSeries = [],columnSeries = [],result = [];
	var result = data.queryResult;
	console.log("boolean:",result instanceof Array);
	if(result instanceof Array){
		for(var i=0;i<result.length;i++){
			at[i] = result[i].at;
			min[i] = parseFloat(result[i].min);
			max[i] = parseFloat(result[i].max);
			avg[i] = parseFloat(result[i].avg);
			classify[i] = result[i].classify;
		}
	}else{
		at[0] = result.at;
		min[0] = parseFloat(result.min);
		max[0] = parseFloat(result.max);
		avg[0] = parseFloat(result.avg);
		classify[0] = result.classify;
	}
	console.log("at:",at);
	curveSeries[0] = new Result("min",min); 
	curveSeries[1] = new Result("max",max); 
	curveSeries[2] = new Result("avg",avg); 
	console.log("curveSeries",curveSeries);
	console.log("curveSeries",JSON.stringify(curveSeries));

	//解析classify
	var god = [[],[],[],[],[],[],[]];
	var cls = ["0-50","50-100","100-150","150-200","200-250","250-300","300-!"];
	for(var i=0;i<classify.length;i++){
		var odd = JSON.parse(classify[i]);
		god[0][i] = odd["0-50"];
		god[1][i] = odd["50-100"];
		god[2][i] = odd["100-150"];
		god[3][i] = odd["150-200"];
		god[4][i] = odd["200-250"];
		god[5][i] = odd["250-300"];
		god[6][i] = odd["300-!"];
	}
	for(var i=0;i<cls.length;i++){
		columnSeries[i] = new Result(cls[i],god[i]);
	}
	console.log("columnSeries:",columnSeries);
	console.log("columnSeries:",JSON.stringify(columnSeries));
	result[0] = at;result[1] = curveSeries;result[2] = columnSeries;
	return result;
}

function convert(num){
	if(num > 1000){
		num = Math.round(( num/1000) * 100) / 100;
		num = num + "k";
	}
	if(num > 10000){
		num = Math.round(( num/10000) * 100) / 100;
		num = num + "w";
	}
	return num;
}

//绘制曲线
function drawCurve(unit,categories,curveSeries) {
    $('#container01').highcharts({
        chart:{
            margin: [ 15, 110, 30, 60],
            type: 'spline'
        },
        title: {
            text: null
        },
        credits: {
            enabled: false
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            tickInterval: 10,
            title: {
                text: "单位："+unit
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: unit
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        plotOptions: {
            spline: {
                lineWidth: 2,
                states: {
                    hover: {
                        lineWidth: 3
                    }
                }
            }
        },
        series: curveSeries
    });
}

// 绘制柱状图
function drawColumn(categories,columnSeries) {
    $('#container02').highcharts({
        chart: {
            margin: [ 15, 15, 45, 60],
            type: 'column'
        },
        title: {
            text: null
        },
        credits: {
            enabled: false
        },
        subtitle: {
            text: 'Source: WorldClimate.com'
        },
        legend: {
            y: 20
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            tickInterval: 50,
            title: {
                text: '单位：个'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        subtitle: {
            text: null
        },
        series: columnSeries
    });
}

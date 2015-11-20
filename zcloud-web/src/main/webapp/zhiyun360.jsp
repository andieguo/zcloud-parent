<%@ page language="java" import="java.util.*,org.zonesion.hadoop.base.bean.*,org.zonesion.webapp.sensor.service.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="viewport"
	content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>zhiyun360</title>
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bootstrap.css" />
<script src="js/jquery.min.js" type="text/javascript" ></script>
<script src="js/bootstrap.min.js" type="text/javascript" ></script>
<script src="js/charts.js" type="text/javascript"></script>
<script src="js/highcharts.js" type="text/javascript" ></script>
<script src="js/drawcharts.js" type="text/javascript" ></script>
<script src="js/WSNHistory.js" type="text/javascript" ></script>
<script src="js/WSNRTConnect.js" type="text/javascript" ></script>

<style>
.panel {
	position: absolute;
	width: 794px;
}

.panel-heading h3 {
	font-size: 12px;
	color: #333;
}

.table {
	margin-bottom: 0;
}

#qy {
	z-index: 4
}
</style>
<script>
	function onclick_1(){
		var getHeight =  $("#qy").height() + 30;
      var contentHeight = "height:" + getHeight + "px";
      $("#qy").parent().attr("style", contentHeight);
	}
	
	function onclick_2(obj) {
		$("#qy").attr("style", "z-index:4");
		//$("#qj").attr("style", "z-index:3");
		$("#zj").attr("style", "z-index:2");
		$("#yh").attr("style", "z-index:1");
		$($(obj).attr("href")).attr("style", "z-index:99");
      var getHeight = $($(obj).attr("href")).height() + 30;
      var contentHeight = "height:" + getHeight + "px";
      $($(obj).attr("href")).parent().attr("style", contentHeight);
	}
	
	function onclick_3(obj){
		var attr = $(obj).next().attr("class");
		if(attr == "collapse in"){
			$(obj).next().attr("class","collapse ");
		}else{
			$(obj).next().attr("class","collapse in");
		}
		onclick_1();
	}

	function ForDight(Dight, How) {
		Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);
		return Dight;
	}

	//显示网关和传感器信息
	function listGates(data) {
		for (var i = 0; i < data.gate.length; i++) {
			var tr1 = '<tr onclick="onclick_3(this)">';
			tr1 += "<td>" + data.gate[i].serverAddr + "</td>";
			tr1 += "<td>" + data.gate[i].userid + "</td>";
			tr1 += "<td>" + data.gate[i].userkey + "</td>";
			tr1 += "</tr>";
			var tr2 = '<tr class="collapse " style="background: #ede7e7;">';
			tr2 += '<td colspan="3">';
			tr2 += '<table class="table table-bordered table-condensed">'
			tr2 += '<tr><td>通道名</td><td>通道地址</td><td>单位</td></tr>'
			var sensors = data.gate[i].sensors;
			for (var j = 0; j < sensors.length; j++) {
				var tr = "<tr>";
				tr += "<td>" + sensors[j].title + "</td>";
				tr += "<td>" + sensors[j].channal + "</td>";
				tr += "<td>" + sensors[j].unit + "</td>";
				tr += "</tr>";
				tr2 += tr;
			}
			tr2 += "</table></td></tr>";
			$("#tab_idkey").append(tr1);
			$("#tab_idkey").append(tr2);
		}
	}
	
	//加载表格
	function loadSensors(gateid){
		$("#tab_sensors tr:not(:first)").empty();
		for(var i=0;i<gatesJSON.gate.length;i++){
			if(gatesJSON.gate[i].userid == gateid){
				var sensors = gatesJSON.gate[i].sensors;
				for (var j = 0; j < sensors.length; j++) {
					var tr = "<tr>";
					tr += "<td >" + sensors[j].title + "</td>";
					tr += "<td class='datamac' mac='"+sensors[j].channal+"'>" + sensors[j].channal + "</td>";
					tr += "<td class='newdata'>正在获取...</td>";
					tr += "<td>" + sensors[j].unit + "</td>";
					tr += "<td class='newtime'>...</td>";
					tr += "</tr>";
					$("#tab_sensors").append(tr);
				}
			}
		}
	}
	//更新最新值
	function updateRealData(mac,data){
		$("tr").each(function(){
			if($(this).attr("mac") == mac){
				$(this).children('td').eq(2).html(data);
				$(this).children('td').eq(4).html(new Date());
			}
		});
	}
	//刷新查询实时数据
	function refresh(){
		$(".datamac").each(function () {//感知设备查询
			var mac=$(this).attr("mac");
			if(mac!=null){
				var data_mac=mac.split('_');
				rtc.sendMessage(data_mac[0],"{"+data_mac[1]+"=?}");
			};
		});
	}
	
	//查询实时数据
	var rtc;
	function startConnection(){
		var myZCloudID = $("#realgateSelect").find("option:selected").val();//ID
		var myZCloudKey = $("#realgateSelect").find("option:selected").attr("userkey");//KEY
		var serverAddr = $("#realgateSelect").find("option:selected").attr("serveraddr");//服务器地址
		//加载表格
		loadSensors(myZCloudID);
		rtc = new WSNRTConnect(myZCloudID,myZCloudKey);//创建数据连接服务对象
		if(serverAddr != null && serverAddr != ""){
			rtc.setServerAddr(serverAddr+":28080");
		}
		rtc.connect();//数据推送服务连接
		
		rtc.onConnect = function(){//连接成功回调函数
			console.log("数据服务连接成功！");
			$(".info").html("数据服务连接成功！");
			$(".datamac").each(function () {//感知设备查询
				var mac=$(this).attr("mac");
				if(mac!=null){
					var data_mac=mac.split('_');
					rtc.sendMessage(data_mac[0],"{"+data_mac[1]+"=?}");
				};
			});
		};
		
		rtc.onConnectLost = function(){//数据服务掉线回调函数
			console.log("数据服务掉线！");
			$(".info").html("数据服务掉线！");
		};
		
		rtc.onmessageArrive = function(mac, dat) {//消息处理回调函数
			console.log("mac",mac);//00:12:4B:00:02:CB:A8:52
			console.log("dat",dat);//{A0=1,A1=3}
			if ((dat[0] == '{' )&& (dat[dat.length-1] == '}')) { //{A0=1,A1=3}
				var data = dat.substr(1, dat.length-2);//A0=1,A1=3
				var tags = data.split(",");//["A0=1","A1=3"]
				for (var i=0; i<tags.length; i++) {
					var t = tags[i];//"A0=1"
					var v = t.split("=");//["A0",2]
					if(v.length == 2){
						var dat_mac=mac+"_"+v[0];
						$(".datamac").each(function (){//遍历所有的mac标签
							var d = new Date();
							var time = d.toLocaleString(); 
	                   if ($(this).attr("mac") == dat_mac){
								$(this).siblings('.newdata').html(v[1]);
								$(this).siblings('.newtime').html(time);
							}
	                    })
					}
				}
			}
		};
	}
	//断开连接
	function stopConnection(){
		if(rtc != null) rtc.disconnect();
	}
	
	//查询历史数据
	function searchHistory(){
		var myZCloudID = $("#gateSelect").find("option:selected").val();//ID
		var myZCloudKey = $("#gateSelect").find("option:selected").attr("userkey");//KEY
		var serverAddr = $("#gateSelect").find("option:selected").attr("serveraddr");//服务器地址
		var channal = $("#sensorSelect").find("option:selected").attr("channal");//通道
		var unit = $("#sensorSelect").find("option:selected").attr("unit");//单位
		var typeid = $("#typeSelect").find("option:selected").val();//类型
		if(myZCloudID == 0) {alert("请选择网关!");return;}
		if(channal == 0) {alert("请选择通道!");return;}
		
		var myHisData = new WSNHistory(myZCloudID,myZCloudKey);//建立对象,并初始化
		myHisData.setServerAddr(serverAddr+":8080");
	
		//查询最近历史数据
		$("#curve").html("<img class='loading-img' style='margin:125px 324px;' src='images/Loading.gif' />");
		myHisData[typeid](channal,function(dat){
			if(dat!=""){
				var str = JSON.stringify(dat);//将接收到的json数据对象转换成字符串
				//$("#hisData").text(JSON.stringify(dat));//显示接收到的原始数据
				var data = DataAnalysis(dat);//将接收到的json数据转换成二维数组形式在曲线图中显示
				showChart('#curve', 'spline', unit, false,eval(data));
			}else{
				$("#curve").text("你查询的时间段没有数据！");
			}
		});
	}
	
	var gatesJSON;
	$(function() {
		$.ajax({//调用JQuery提供的Ajax方法 
			type : "GET",
			url : "rest/gate",
			dataType : "json",
			complete: onclick_1,
			success : function(data) {//回调函数 
				console.log('data：', data);
				listGates(data);
				//将建立实时连接的按钮设置为可用，其默认是不可用的
				gatesJSON = data;
			},
			error : function() {
				alert("系统出现问题");
			}
		});
		
		
	});
</script>
</head>
<body>

</body>
<!--页头S-->
<div id="header">
	<h1>zhiyun360</h1>
	<!--一级导航栏S-->
	<div id="nav">
		<ul id="nav-title">
			<li><a href="index.jsp"><span>CloudStack</span></a></li>
			<li><a href="hadoop.jsp"><span>hadoop</span></a></li>
			<li><a href="zhiyun360.jsp"><span>zhiyun360</span></a></li>
			<li><a href="wm.jsp"><span>DataAnalysis</span></a></li>
		</ul>
	</div>
	<!--一级导航栏E-->
</div>
<!--页头E-->

<!--页中S-->
<div id="main">
	<!--二级导航栏S-->
	<div id="subnav">
		<ul class="subnav-title">
			<li><a onClick="onclick_2(this)" href="#qy" data-toggle="tab"><span>ID&KEY</span></a></li>
			<li><a onClick="onclick_2(this)" href="#zj" data-toggle="tab"><span>历史数据</span></span></a></li>
			<li><a onClick="onclick_2(this)" href="#yh" data-toggle="tab"><span>实时监控</span></span></a></li>
		</ul>
	</div>
	<!--二级导航栏E-->
	<!--内容S-->
	<div class="content">
		<!--ID&KEY-->
		<div class="panel panel-default fade in active" id="qy">
			<div class="panel-heading">
				<h3 class="panel-title">zhiyun360 > ID&KEY</h3>
			</div>
			<div class="panel-body">
				<table id="tab_idkey"
					class="table table-bordered table-condensed table-hover">
					<thead>
						<tr>
							<th width="15%">服务器地址</th>
							<th width="15%">应用ID</th>
							<th width="15%">KEY</th>
						</tr>
					</thead>
					<tbody>
				</table>
			</div>
		</div>
		<!--ID&KEY-->
		<!--历史数据-->
		<div class="panel panel-default fade" id="zj">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">zhiyun360 > 历史数据</h3>
				</div>
				<%
					String path = request.getContextPath();
					String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
							+ path + "/";
					GateService gateService = new GateServiceImpl();
					List<Gate> gates = gateService.findGates();
				%>
				<div class="panel-body">
                    <div class="row">
                        <div class="col-xs-3">
                            <div class="input-group">
                                <span class="input-group-addon btn-sm">网关</span>
                                <select name="gateSelect" id="gateSelect" class="form-control btn-sm" onchange="loadSensor(this.options[this.options.selectedIndex].value)">
                                    	<option value="0">请选择...</option> 
													<%
														if(gates != null){
															for(Gate gate:gates){
													%>
																<option value="<%=gate.getUserid()%>" userkey="<%=gate.getUserkey()%>" serveraddr="<%=gate.getServerAddr()%>")><%=gate.getUserid()%></option>
													<% 	
															}
														}
													%>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group">
                                <span class="input-group-addon">通道</span>
                                <select name="sensorSelect" id="sensorSelect" class="form-control btn-sm">
                                   	<option value="0">请选择...</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group">
                                <span class="input-group-addon">类型</span>
                                <select id="typeSelect" class="form-control btn-sm">
                                    <option value="queryLast3M">最近3月</option>
                                    <option value="queryLast1M">最近1月</option>
                                    <option value="queryLast14D">最近2周</option>
                                    <option value="queryLast7D">最近1周</option>
                                    <option value="queryLast1D">最近1天</option>
                                    <option value="queryLast1H">最近1小时</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <button id="search" class="btn btn-default btn-block btn-sm" type="submit" onclick="searchHistory()">搜索</button>
                        </div>
                    </div>
                </div>
				<div class="panel-body">
					<div id="curve" >
		   	 		</div>
				</div>
			</div>
		</div>
		<!--历史数据-->
		<!--实时监控-->
		<div class="panel panel-default fade" id="yh">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">zhiyun360 > 实时监控</h3>
				</div>
				<div class="panel-body">
                    <div class="row">
                        <div class="col-xs-3">
                            <div class="input-group">
                                <span class="input-group-addon btn-sm">网关</span>
                                <select name="realgateSelect" id="realgateSelect" class="form-control btn-sm" onchange="loadSensor(this.options[this.options.selectedIndex].value)">
                                    	<option value="0">请选择...</option> 
													<%
														if(gates != null){
															for(Gate gate:gates){
													%>
																<option value="<%=gate.getUserid()%>" userkey="<%=gate.getUserkey()%>" serveraddr="<%=gate.getServerAddr()%>")><%=gate.getUserid()%></option>
													<% 	
															}
														}
													%>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <button id="search" class="btn btn-default btn-block btn-sm" type="submit" onclick="startConnection()">建立连接</button>
                        </div>
                        <div class="col-xs-3">
                            <button id="search" class="btn btn-default btn-block btn-sm" type="submit" onclick="stopConnection()">断开连接</button>
                        </div>
                        <div class="col-xs-3">
                            <button id="search" class="btn btn-default btn-block btn-sm" type="submit" onclick="refresh()">刷新</button>
                        </div>
                         <div class="info">
                        </div>
                    </div>
					<table id="tab_sensors"
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th>名称</th>
								<th>通道</th>
								<th>最新数值</th>
								<th>单位</th>
								<th>更新时间</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!--实时监控-->
	</div>
	<div style="clear: both"></div>
	<!--内容E-->
</div>
<!--页中E-->

<!--页尾S-->
<div id="footer"></div>
<!--页尾S-->
</html>
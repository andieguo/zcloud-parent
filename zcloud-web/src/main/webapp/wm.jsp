<%@ page language="java" import="java.util.*,org.zonesion.hadoop.base.bean.*,org.zonesion.webapp.sensor.service.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="UTF-8">
	<title>数据分析结果展示</title>
	<link rel="stylesheet" href="css/bootstrap.css"/>
	<link rel="stylesheet" href="css/style.css"/>
	<link rel="stylesheet" href="css/wm-style.css"/>
	<!-- <script src="js/bootstrap.min.js"></script> -->
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/charts.js"></script>
	<script type="text/javascript" src="js/highcharts.js"></script>
	<script type="text/javascript" src="js/json2.js"></script>
	<script type="text/javascript">
	
		function search(){
				var gateid = $("#gateSelect").find("option:selected").val();
				var sensorid = $("#sensorSelect").find("option:selected").val();
				var typeid = $("#typeSelect").find("option:selected").val();
				if(gateid == 0) {alert("请选择网关!");return;}
				if(sensorid == 0) {alert("请选择通道!");return;}
				sensorid = sensorid.replace(new RegExp(/:/g),'_');
				var url = 'rest/result/userid/'+gateid+'/channal/'+sensorid+'/type/'+typeid;
				console.log("url:",url);
			   $.ajax({
					url : url,
					type: "get",
					dataType: "json",
			      contentType:"application/json",  
					success:function(data){//返回的msg是一个json对象
						console.log('data：',data);
						var result = analyze(data);
						drawCurve(result[0],result[1]);
						drawColumn(result[0],result[2]);
					},
					error:function()
					{  
				    		alert("系统出现问题");      
					} 
			});
		}
	   
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
						sensorSelect.append("<option value='"+data.sensor[i].channal+"'>"
								+ data.sensor[i].title + "</option>");
					}
				},
				error : function() {
					alert("系统出现问题");
				}
			});
		}
	</script>
</head>
<body>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ path + "/";
		GateService gateService = new GateServiceImpl();
		List<Gate> gates = gateService.findGates();
	%>
</body>
<!--页头S-->
<div id="header">
    <h1>DataAnalysis</h1>
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
            <li><a onClick="onclick_2(this)" href="#qy" data-toggle="tab"><span>数据分析</span></a></li>
        </ul>
    </div>
    <!--二级导航栏E-->
    <!--内容S-->
    <div class="content">
        <!--区域S-->
        <div class="fade in active" id="qy">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        数据分析
                    </h3>
                </div>
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
																<option value="<%=gate.getUserid()%>"><%=gate.getUserid()%></option>
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
                                <select name="sensorSelect" id="sensorSelect" class="form-control">
                                   	<option value="0">请选择...</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group">
                                <span class="input-group-addon">类型</span>
                                <select id="typeSelect" class="form-control">
                                    <option value="year">年</option>
                                    <option value="month">月</option>
                                    <option value="day">天</option>
                                    <option value="hour">小时</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <button id="search" class="btn btn-default btn-block btn-sm" type="submit" onclick="search()">搜索</button>
                        </div>
                    </div>
                </div>
                <div class="panel-body pt0">
                    <table id="tab_zone" class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>数据分析信息展示</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td height="200px">
                                <div id="container01" style="min-width:500px;height:200px"></div>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel-body pt0">
                    <table id="tab_zone" class="table table-bordered table-condensed">
                        <thead>
                        <tr>
                            <th>数据分类信息展示</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td height="200px">
                                <div id="container02" style="min-width:700px;height:200px"></div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <!--区域E-->
    </div>
    <div style="clear: both"></div>
    <!--内容E-->
</div>
<!--页中E-->

<!--页尾S-->
<div id="footer">

</div>
<!--页尾S-->
</html>
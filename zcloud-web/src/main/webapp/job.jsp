<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="viewport"
	content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta charset="UTF-8">
<title>job</title>
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bootstrap.css" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<style>
.fade {
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

.progress {
	margin-bottom: 0;
	height: 17px;
}

#qy {
	z-index: 4
}
.panel-title-right {
	float: right;
}
.panel-title-right form {
	display: inline-block;
	margin: -3px 0 0 0;
}
</style>
<script>
        function onclick_2(obj){
            $("#qy").attr("style", "z-index:4");
            $("#qj").attr("style", "z-index:3");
            $("#zj").attr("style", "z-index:2");
            $("#yh").attr("style", "z-index:1");
            $($(obj).attr("href")).attr("style", "z-index:5");
            var getHeight = $($(obj).attr("href")).height() + 30;
            var contentHeight = "height:" + getHeight + "px";
            $($(obj).attr("href")).parent().attr("style", contentHeight);
        }
        
        function ForDight(Dight,How){
            Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);
            return Dight;
        }
       
        var mr = {"running": [{"map_completed": "442", "reduce_completed": " 0", "name": "PiEstimator", "started": "Tue Apr 28 14:24:21 CST 2015", "reduce_total": "1", "map_total": "500", "user": "hadoop", "id": "job_201504251425_0007"}], "completed": [{"started": "Tue Apr 28 13:52:48 CST 2015", "user": "hadoop", "id": "job_201504251425_0005", "name": "PiEstimator"}, {"started": "Tue Apr 28 14:01:26 CST 2015", "user": "hadoop", "id": "job_201504251425_0006", "name": "PiEstimator"}, {"started": "Sat Apr 25 18:26:59 CST 2015", "user": "hadoop", "id": "job_201504251425_0004", "name": "PiEstimator"}, {"started": "Sat Apr 25 18:17:21 CST 2015", "user": "hadoop", "id": "job_201504251425_0003", "name": "PiEstimator"}, {"started": "Sat Apr 25 14:40:20 CST 2015", "user": "hadoop", "id": "job_201504251425_0002", "name": "PiEstimator"}, {"started": "Sat Apr 25 14:30:14 CST 2015", "user": "hadoop", "id": "job_201504251425_0001", "name": "PiEstimator"}]};
        
        function printRunJobTable(data,tab){
			tab.html('<tr><th width="15%">ID</th><th width="15%">名称</th><th width="15%">所属用户</th><th width="15%">开始时间</th><th width="20%" colspan="2">map进度</th><th width="20%" colspan="2">reduce进度</th></tr>');
			for (var i=0; i<data.length; i++) {
			   var mapProgress = (data[i].mapProgress).toFixed(2)*100;
			   var reduceProgress = (data[i].reduceProgress).toFixed(2)*100;
			   console.log("mapProgress",mapProgress);
				var tr1 = "<tr>"; 
				tr1 += "<td>"+data[i].jobId+"</td>";
				tr1 += "<td>"+data[i].jobName+"</td>";
				tr1 += "<td>"+data[i].userName+"</td>";
				tr1 += "<td>"+data[i].starttime+"</td>"; 
				tr1 += '<td width="15%">';
				tr1 +=     '<div class="progress">';
				tr1 +=           '<div class="progress-bar" role="progressbar" aria-valuenow="'+mapProgress+'"';
				tr1 +=                             'aria-valuemin="0" aria-valuemax="100" style="width: '+mapProgress+'%;">';
				tr1 +=                            '<span class="sr-only">'+mapProgress+'%</span>';
				tr1 +=    '</div></div></td>';
				tr1 += '<td>'+mapProgress+'%</td>';
				tr1 += '<td width="15%">';
				tr1 +=     '<div class="progress">';
				tr1 +=           '<div class="progress-bar" role="progressbar" aria-valuenow="'+reduceProgress+'"';
				tr1 +=                             'aria-valuemin="0" aria-valuemax="100" style="width: '+reduceProgress+'%;">';
				tr1 +=                            '<span class="sr-only">'+reduceProgress+'%</span>';
				tr1 +=    '</div></div></td>';
				tr1 += '<td>'+reduceProgress+'%</td></tr>';
				tab.append(tr1);  
		  }
          }
        
       function printJobTable(data,tab){
    	   for (var i=0; i<data.length; i++) {
     	      var tr1 = "<tr>"; 
	         tr1 += "<td>"+data[i].jobId+"</td>";
	         tr1 += "<td>"+data[i].jobName+"</td>";
	         tr1 += "<td>"+data[i].userName+"</td>";
	         tr1 += "<td>"+data[i].starttime+"</td>"; 
	         tr1 += '</tr>';    
	         tab.append(tr1);                          
     		}   
         }
        
      function getRuningJob(){
    	  $.ajax({//调用JQuery提供的Ajax方法 
				type : "GET",
				url : "servlet/jobtracker",
				dataType : "json",
				success : function(data){//回调函数 
					console.log(data);
					if(data.running.length > 0){
						console.log("执行了getRuningJob方法");
						window.setTimeout(getRuningJob,1000);//休息1S后执行getRuningJob方法
						printRunJobTable(data.running,$("#tab_running"));
					}
				},
				error : function() {
					alert("系统出现问题");
				}
			});
         }
      
      function getOtherJob(){
    	  $.ajax({//调用JQuery提供的Ajax方法 
				type : "GET",
				url : "servlet/jobtracker",
				dataType : "json",
				success : function(data){//回调函数 
					//console.log(data);
					printRunJobTable(data.failed,$("#tab_failed"));
		        	printJobTable(data.completed,$("#tab_completed"));
		        	printJobTable(data.killed,$("#tab_killed"));
				},
				error : function() {
					alert("系统出现问题");
				}
			});
         }

	  $(function(){
		   getRuningJob();
		   getOtherJob();
    	});
    </script>
</head>
<body>

</body>
<%@ include file="/share/top.jsp"%>

<!--页中S-->
<div id="main">
	<!--二级导航栏S-->
	<div id="subnav">
		<ul class="subnav-title">
			<li><a onClick="onclick_2(this)" href="#qy" data-toggle="tab"><span>数据计算</span></a></li>
		</ul>
	</div>
	 <!--内容S-->
	 <div class="content">
	<!--二级导航栏E-->
		<!--Map/Reduce S-->
		<div class="fade in active" id="zj">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">
						job > 数据计算
						<span class="panel-title-right">
						<form action="servlet/hbaseJob" method="get">
							点击启动数据计算：
							<input class="button button-blue" name="" type="submit" value="启动" />
						</form>
						</span>
					</h3>
				</div>
				<div class="panel-heading">
					<h3 class="panel-title">正在进行的任务</h3>
				</div>
				<div class="panel-body">
					<table id='tab_running'
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th width="15%">ID</th>
								<th width="15%">名称</th>
								<th width="15%">所属用户</th>
								<th width="15%">开始时间</th>
								<th width="20%" colspan="2">map进度</th>
								<th width="20%" colspan="2">reduce进度</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">已完成任务</h3>
				</div>
				<div class="panel-body">
					<table id='tab_completed'
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th>ID</th>
								<th>名称</th>
								<th>所属用户</th>
								<th>开始时间</th>

							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">失败任务</h3>
				</div>
				<div class="panel-body">
					<table id='tab_failed'
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th width="25px">ID</th>
								<th width="15%">名称</th>
								<th width="15%">所属用户</th>
								<th width="15%">开始时间</th>
								<th width="20%" colspan="2">map进度</th>
								<th width="20%" colspan="2">reduce进度</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">已杀死任务</h3>
				</div>
				<div class="panel-body">
					<table id='tab_killed'
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th>ID</th>
								<th>名称</th>
								<th>所属用户</th>
								<th>开始时间</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div style="clear: both"></div>
	<!--内容E-->
</div>
<!--页中E-->

<!--页尾S-->
<div id="footer"></div>
<!--页尾S-->
</html>
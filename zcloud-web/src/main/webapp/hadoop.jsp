<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="viewport"
	content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta charset="UTF-8">
<title>hadoop</title>
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
        var dfs = {"nondfsused":8.403468288E9,"capacity":1.0568937472E11,"dfsused":3.9806976E8,"datanode":[{"nondfsused":4201721856,"name":"192.168.100.136:50010","state":"In Service","capacity":52844687360,"dfsused":199049216},{"nondfsused":4201746432,"name":"192.168.100.134:50010","state":"In Service","capacity":52844687360,"dfsused":199020544}]};
        var mr = {"running": [{"map_completed": "442", "reduce_completed": " 0", "name": "PiEstimator", "started": "Tue Apr 28 14:24:21 CST 2015", "reduce_total": "1", "map_total": "500", "user": "hadoop", "id": "job_201504251425_0007"}], "completed": [{"started": "Tue Apr 28 13:52:48 CST 2015", "user": "hadoop", "id": "job_201504251425_0005", "name": "PiEstimator"}, {"started": "Tue Apr 28 14:01:26 CST 2015", "user": "hadoop", "id": "job_201504251425_0006", "name": "PiEstimator"}, {"started": "Sat Apr 25 18:26:59 CST 2015", "user": "hadoop", "id": "job_201504251425_0004", "name": "PiEstimator"}, {"started": "Sat Apr 25 18:17:21 CST 2015", "user": "hadoop", "id": "job_201504251425_0003", "name": "PiEstimator"}, {"started": "Sat Apr 25 14:40:20 CST 2015", "user": "hadoop", "id": "job_201504251425_0002", "name": "PiEstimator"}, {"started": "Sat Apr 25 14:30:14 CST 2015", "user": "hadoop", "id": "job_201504251425_0001", "name": "PiEstimator"}]};
        
		function printHdfsTable(err, dfs) {
         var diskBytes = 1024 * 1024 * 1024;
        	var capacity = dfs.capacity;
        	var nondfsused = dfs.nondfsused;
        	var dfsused = dfs.dfsused;
        	var tr1 = '<tr data-toggle="collapse" data-target="#11">';
        	tr1 += '<td>'+ForDight(capacity/diskBytes, 2)+'</td>';
        	tr1 += '<td>'+ForDight(nondfsused/diskBytes, 2)+'</td>';
        	tr1 += '<td>'+ForDight(dfsused/diskBytes, 2)+'</td>';
        	tr1 += '<td>'+ForDight((capacity-nondfsused-dfsused)/diskBytes, 2)+'</td>';
        	tr1 += '<td>'+100*ForDight((capacity-nondfsused-dfsused)/capacity, 2)+'%</td>';
        	tr1 += '</tr>';
        	
        	var tr2 = '<tr class="collapse in" style="background: #ede7e7;">';
        	tr2 += '<td colspan="5">';
            tr2 += '<table class="table table-bordered table-condensed">'     
            tr2 += '<tr><td>节点名字</td><td>节点状态</td><td>总量(GB)</td><td>NON DFS已使用(GB)</td><td>DFS已使用(GB)</td><td>剩余(GB)</td><td>剩余(%)</td></tr>'           
            for (var i=0; i<dfs.datanode.length; i++) {
            	var tr = "<tr>";
            	var name = dfs.datanode[i].name;
            	var state = dfs.datanode[i].state;
            	var capacity = dfs.datanode[i].capacity;
            	var nondfsused = dfs.datanode[i].nondfsused;
            	var dfsused = dfs.datanode[i].dfsused;
            	tr += "<td>"+name+"</td>";
            	tr += "<td>"+state+"</td>";
            	tr += "<td>"+ForDight(capacity/diskBytes, 2)+"</td>";
            	tr += "<td>"+ForDight(nondfsused/diskBytes, 2)+"</td>";
            	tr += "<td>"+ForDight(dfsused/diskBytes, 2)+"</td>";
            	tr += "<td>"+ForDight((capacity-nondfsused-dfsused)/diskBytes,2)+"</td>";
            	tr += "<td>"+100*ForDight(((capacity-nondfsused-dfsused)/capacity), 2)+"%</td>";
            	tr += "</tr>";
            	tr2 += tr;
            }
            tr2 += "</table></td></tr>";
            $("#tab_dfs").append(tr1); 
            $("#tab_dfs").append(tr2); 
        }
        
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
      
      function getNameNode(){
			
          $.ajax({//调用JQuery提供的Ajax方法 
  				type : "GET",
  				url : "servlet/namenode",
  				dataType : "json",
				complete:function(){$("#load").hide();},
  				success : function(data){//回调函数 
  					console.log(data);
  					printHdfsTable(null, data);
  				},
  				error : function() {
  					alert("系统出现问题");
  				}
  			});
      }
	  
	  function getFileSystem(type,parentDir){
		  $("#parentDirText").val(parentDir);//改变文本框的值
		  if(type == 'FILE'){
			 $.ajax({//调用JQuery提供的Ajax方法 
				type : "GET",
				url : "servlet/filesystem",
				data : {command:'OPEN',parentDir:parentDir},
				dataType : "text",
				success : function(data){//回调函数 
					console.log('data：',data);
					$("#tab_filesystem").html("<textarea style='margin: 0px; height: 206px; width: 760px;'>"+data+"</textarea>");
				},
				error : function() {
					alert("系统出现问题");
				}
			});
		  }else if(type == 'DIRECTORY'){
			 $.ajax({//调用JQuery提供的Ajax方法 
				type : "GET",
				url : "servlet/filesystem",
				data : {command:'LISTSTATUS',parentDir:parentDir},
				dataType : "json",
				success : function(data){//回调函数 
					console.log('data：',data);
					printFileSystem(parentDir,data.FileStatuses.FileStatus);
				},
				error : function() {
					alert("系统出现问题");
				}
			});
		  }
		  
	  }
	  
	  function printFileSystem(parentDir,data){
		   $("#tab_filesystem").html("<thead><tr><th>文件名</th><th>文件类型</th><th>文件大小</th><th>备份因子</th><th>修改时间</th><th>文件权限</th><th>拥有者</th><th>属组</th></tr></thead>");
    	   for (var i=0; i<data.length; i++) {
     	      var tr1 = "<tr>"; 
	         tr1 += "<td><a id='fileName' onClick=getFileSystem($(this).parent().next().html(),$('#parentDirText').val()+$(this).html()+'/')>"+data[i].pathSuffix+"</a></td>";
	         tr1 += "<td>"+data[i].type+"</td>";
	         tr1 += "<td>"+data[i].blockSize+"</td>";
	         tr1 += "<td>"+data[i].replication+"</td>"; 
			 tr1 += "<td>"+data[i].modificationTime+"</td>"; 
			 tr1 += "<td>"+data[i].permission+"</td>"; 
			 tr1 += "<td>"+data[i].owner+"</td>"; 
			 tr1 += "<td>"+data[i].group+"</td>"; 
	         tr1 += '</tr>';    
	         $("#tab_filesystem").append(tr1);                          
     		}   
       }
	   
	   function goToParentDir(){
		   var parentDir = $('#parentDirText').val();// eg. /user/hadoop/zcloud/
		   parentDir = parentDir.substring(0,parentDir.length-1); // eg. /user/hadoop/zcloud
		   var dir = parentDir.substring(0,parentDir.lastIndexOf('/')+1);// eg. /user/hadoop/
		   //console.log("dir:"+dir);
		   getFileSystem('DIRECTORY',dir);
	   }

	  $(function(){
		   getRuningJob();
		   getOtherJob();
		   getNameNode();
		   //var url = 'http://192.168.100.141:50070/webhdfs/v1/user/hadoop/?op=LISTSTATUS';
           getFileSystem('DIRECTORY','/user/hadoop/');
    	});
    </script>
</head>
<body>

</body>
<!--页头S-->
<div id="header">
	<h1>hadoop</h1>
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
			<li><a onClick="onclick_2(this)" href="#qy" data-toggle="tab"><span>DFS</span></a></li>
			<li><a onClick="onclick_2(this)" id="mapreduceId" href="#zj"data-toggle="tab"><span>Map/Reduce</span></a></li>
			<li><a onClick="onclick_2(this)" href="#yh"data-toggle="tab"><span>FileSystem</span></a></li>
		</ul>
	</div>
	<!--二级导航栏E-->
	<!--内容S-->
	<div class="content">
		<!--DFS S-->
		<div class="fade in active" id="qy">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">hadoop > DFS</h3>
				</div>
				<div class="panel-body">
					<table id="tab_dfs"
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th>总量(GB)</th>
								<th>NON DFS已使用(GB)</th>
								<th>DFS已使用(GB)</th>
								<th>剩余(GB)</th>
								<th>剩余(%)</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!--DFS E-->
		<!--Map/Reduce S-->
		<div class="fade active" id="zj">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">hadoop > Reduce</h3>
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
		<!--HDFS文件系统		E-->
		<!--二级导航栏E-->
		<!--内容S-->
		<!--DFS S-->
		<div class="fade active" id="yh">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">hadoop > FileSystem</h3>
				</div>
				<div style='margin: 0px; height: 206px; width: 760px;'>
					<input id="parentDirText" type="text" value="/user/hadoop/"></input>
					<input type="submit" value="Go" onClick="getFileSystem('DIRECTORY',$('#parentDirText').val())"></input>
					<input type="submit" value="Go to parent directory" onClick="goToParentDir()"></input>
					<input type="submit" value="Go back to DFS home" onClick="getFileSystem('DIRECTORY','/user/hadoop/')"></input>
				</div>
				<div class="panel-body">
					<table id="tab_filesystem"
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th>文件名</th>
								<th>文件类型</th>
								<th>文件大小</th>
								<th>备份因子</th>
								<th>修改时间</th>
								<th>文件权限</th>
								<th>拥有者</th>
								<th>属组</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		<!--DFS E-->
	</div>
	<div style="clear: both"></div>
	<!--内容E-->
</div>
<!--页中E-->

<!--页尾S-->
<div id="footer"></div>
<!--页尾S-->
</html>
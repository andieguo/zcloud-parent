<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta name="viewport"
	content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta charset="UTF-8">
<title>zhiyun360</title>
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/bootstrap.css" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

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
	function onclick_2(obj) {
		$("#qy").attr("style", "z-index:4");
		//$("#qj").attr("style", "z-index:3");
		$("#zj").attr("style", "z-index:2");
		//$("#yh").attr("style", "z-index:1");
		$($(obj).attr("href")).attr("style", "z-index:99");
	}

	function ForDight(Dight, How) {
		Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);
		return Dight;
	}

	function idkey_add(data) {
		for (var i = 0; i < data.gate.length; i++) {
			var tr1 = '<tr>';
			tr1 += "<td>" + data.gate[i].serverAddr + "</td>";
			tr1 += "<td>" + data.gate[i].userid + "</td>";
			tr1 += "<td>" + data.gate[i].userkey + "</td>";
			tr1 += "</tr>";
			var tr2 = '<tr class="collapse in" style="background: #ede7e7;">';
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
			console.log("----------------",$("#tab_idkey").html());
		}
	}

	var data_row_count = 0;
	var data_row_down = 0;
	function data_add(d) {
		var tr = "<tr id="+data_row_count+">" + "<td>" + d.aid + "</td>"
				+ "<td>" + d.mac + "</td>" + "<td>" + d.type + "</td>" + "<td>"
				+ d.dat + "</td>" + "<td>" + d.time + "</td>" + "</tr>";
		if (data_row_count != 0) {
			$("#tab_data #" + (data_row_count - 1)).before(tr)
		} else {
			$("#tab_data").append(tr);
		}
		data_row_count++;
		if (data_row_count > 20) {
			$('#' + (data_row_count - 20 - 1)).remove();
		}
	}
	function on_data(d) {
		data_add(d);
	}

	var aid;
	function on_get_key(err, key) {
		var mik = {};
		for (var i = 0; i < aid.length; i++) {
			mik[aid[i].feed_id] = mik[aid[i].feed_id] || {};
			mik[aid[i].feed_id]['info'] = aid[i].info;
			mik[aid[i].feed_id]['feed_id'] = aid[i].feed_id;
			var ks = [];
			for (var j = 0; j < key.length; j++) {
				if (key[j].feed_id == aid[i].feed_id) {
					var ke = {};
					ke['key'] = key[j].key;
					ke['info'] = key[j].info;
					ke['power'] = key[j].power;
					ks.push(ke);
				}
			}
			mik[aid[i].feed_id]['key'] = ks;
		}
		for ( var f in mik) {
			idkey_add(mik[f]);
		}
	}
	function on_get_feed(err, aids) {
		//window.rest.d("xxxxxxxxxxxxxxxxxxxxxxxxxx on_get_feed()");
		//aid = aids;
		//window.rest.get("http://192.168.1.250:8000/keys", "on_get_key");
	}

	//////////////////////  以下为测试代码 ///////////////////////////////////
	$(function() {
		//window.wsn.start("on_data");
		//$(window).bind('beforeunload', function() {
		//	window.wsn.stop();
		//});

		//var aid = [{"info": "\u57ce\u5e02\u73af\u5883\u76d1\u6d4b", "feed_id": "9908857871"}, {"info": "\u667a\u80fd\u5c0f\u8f66", "feed_id": "9973113857"}, {"info": "\u667a\u80fd\u5bb6\u5c45\u6f14\u793a", "feed_id": "1643647921"}, {"info": "\u8d44\u4ea7\u7ba1\u7406\u4eea\u5668\u9884\u7ea6", "feed_id": "1024333938"}];

		// 		on_get_feed(null, [ {
		// 			"info" : "\u57ce\u5e02\u73af\u5883\u76d1\u6d4b",
		// 			"feed_id" : "9908857871"
		// 		}, {
		// 			"info" : "\u667a\u80fd\u5c0f\u8f66",
		// 			"feed_id" : "9973113857"
		// 		}, {
		// 			"info" : "\u667a\u80fd\u5bb6\u5c45\u6f14\u793a",
		// 			"feed_id" : "1643647921"
		// 		}, {
		// 			"info" : "\u8d44\u4ea7\u7ba1\u7406\u4eea\u5668\u9884\u7ea6",
		// 			"feed_id" : "1024333938"
		// 		} ]);

		//	window.rest.get("http://192.168.1.250:8000/feeds", "on_get_feed");

		$.ajax({//调用JQuery提供的Ajax方法 
			type : "GET",
			url : "rest/gate",
			dataType : "json",
			success : function(data) {//回调函数 
				console.log('data：', data);
				idkey_add(data);
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
			<li><a onClick="onclick_2(this)" href="#zj" data-toggle="tab"><span>数据监控</span></span></a></li>
		</ul>
	</div>
	<!--二级导航栏E-->
	<!--内容S-->
	<div class="content">
		<!--区域S-->
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
		<!--区域E-->
		<!--主机信息S-->
		<div class="panel panel-default fade" id="zj">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">zhiyun360 > 数据监控</h3>
				</div>
				<div class="panel-body">
					<table id="tab_data"
						class="table table-bordered table-condensed table-hover">
						<thead>
							<tr>
								<th>应用ID</th>
								<th>地址</th>
								<th>类型</th>
								<th>数据</th>
								<th>时间</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		</div>
		<!--主机信息E-->
	</div>
	<div style="clear: both"></div>
	<!--内容E-->
</div>
<!--页中E-->

<!--页尾S-->
<div id="footer"></div>
<!--页尾S-->
</html>
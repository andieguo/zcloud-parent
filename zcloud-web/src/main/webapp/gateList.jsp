<%@ page language="java" import="java.util.*,org.zonesion.hadoop.base.bean.*,org.zonesion.webapp.sensor.service.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>模拟下拉列表二级菜单联动</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript">
   
	function loadDeptChild(userid) {
		if(userid == 0) {
			var deptChild = $("#deptChild");
			deptChild.empty(); //初始化否则会追加 
			deptChild.append("<option value='0'>请选择...</option>");
			return;
		}
		var url = "rest/sensor/userid/" + userid;//要么使用相对路径"rest/sensor/userid/"，要么使用绝对路径："/zcloud-web/rest/sensor/userid/"
		//调用JQuery提供的Ajax方法 
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",//此处要设置成jason 
			success : callback,
			error : function() {
				alert("系统出现问题");
			}
		});//回调函数 
	}
	function callback(msg) {
		console.log('length：',msg.sensor.length);
		console.log("title：",msg.sensor[0].title);
		var deptChild = $("#deptChild");
		deptChild.empty(); //初始化否则会追加 
		for (var i = 0; i < msg.sensor.length; i++) {
			deptChild.append("<option value='"+msg.sensor[i].channal+"'>"
					+ msg.sensor[i].title + "</option>");
		}
	}
	//requestServlet();
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

	<tr>
		<td align="right"><font color="red">*</font>一级服务类型：</td>
		<td>
			<select name="dept" id="dept" onchange="loadDeptChild(this.options[this.options.selectedIndex].value)" />
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
			<span style="color: red"></span>
		</td>
	</tr>
	<tr>
		<td align="right">二级服务类型：</td>
		<td>
			<select name="deptChild" id="deptChild">
				<option value="0">请选择...</option>
			</select>
		</td>
	</tr>

</body>
</html>

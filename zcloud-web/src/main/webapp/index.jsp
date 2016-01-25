<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta charset="UTF-8">
    <title>CloudStack</title>
    <link rel="stylesheet" href="css/style.css"/>
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <style>
        .panel{
            position: absolute;
            width: 794px;
        }
        .panel-heading h3{
            font-size: 12px;
            color: #333;
        }
        .table{margin-bottom: 0;}
        #qy{z-index: 4}
        .nc,.cpu,.cc,.yfpzcc,.fzcc{height: 160px;}
    </style>

    <script type="text/javascript" src="js/highcharts.js"></script>
    <script>
        function onclick_2(obj){
            $("#qy").attr("style", "z-index:4");
            $("#qj").attr("style", "z-index:3");
            $("#zj").attr("style", "z-index:2");
            $("#yh").attr("style", "z-index:1");
            $($(obj).attr("href")).attr("style", "z-index:5");
        }
        function ForDight(Dight,How){
            Dight = Math.round(Dight*Math.pow(10,How))/Math.pow(10,How);
            return Dight;
        }
        function zone_add(z) {
            var ram_used, ram_total;
            var cpu_used, cpu_total;
            var storage_used, storage_total;
            var storage_1_used, storage_1_total;
            var storage_2_used, storage_2_total;
            for (var i=0; i<z.capacity.length; i++) {
                if (z.capacity[i].type == 0) { //rma
                    ram_used = z.capacity[i].capacityused/1024/1024/1024;
                    ram_total = z.capacity[i].capacitytotal/1024/1024/1024;
                }
                if (z.capacity[i].type == 1) { //cpu
                    cpu_used = z.capacity[i].capacityused/1000;
                    cpu_total = z.capacity[i].capacitytotal/1000;
                }
                if (z.capacity[i].type == 2) { //存储
                    storage_used = z.capacity[i].capacityused/1024/1024/1024;
                    storage_total = z.capacity[i].capacitytotal/1024/1024/1024;
                }
                if (z.capacity[i].type == 3) { //主存储
                    storage_1_used = z.capacity[i].capacityused/1024/1024/1024;
                    storage_1_total = z.capacity[i].capacitytotal/1024/1024/1024;
                }
                if (z.capacity[i].type == 6) { //辅助存储
                    storage_2_used = z.capacity[i].capacityused/1024/1024/1024;
                    storage_2_total = z.capacity[i].capacitytotal/1024/1024/1024;
                }
            }
            var xid = Math.floor(Math.random() * ( 100000 + 1))
            $("#tab_zone").html("");
            var tr ="<thead>"
                    +"<tr>"
                    +"<th colspan='3'>"+z.name+"</th>"
                    +"</tr>"
                    +"</thead>"
                    +"<tbody>"
                    +"<tr>"
                    +"<td width='33%'><div class='nc' id='nc_"+xid+"'></div><div style='text-align: center'>"+ForDight(ram_used, 1)+"GB/"+ForDight(ram_total,1)+"GB</div></td>"
                    +"<td width='33%'><div class='cpu' id='cpu_"+xid+"'></div><div style='text-align: center'>"+ForDight(cpu_used, 1)+"GHz/"+ForDight(cpu_total,1)+"GHz</div></td>"
                    +"<td><div class='cc' id='cc_"+xid+"'></div><div style='text-align: center'>"+ForDight(storage_used,1)+"GB/"+ForDight(storage_total,1)+"GB</div></td>"
                    +"</tr>"
                    +"<tr>"
                    +"<td><div class='yfpzcc' id='yfpzcc_"+xid+"'></div><div style='text-align: center'>"+ForDight(storage_1_used,1)+"GB/"+ForDight(storage_1_total,1)+"GB</div></td>"
                    +"<td><div class='fzcc' id='fzcc_"+xid+"'></div><div style='text-align: center'>"+ForDight(storage_2_used,1)+"GB/"+ForDight(storage_2_total,1)+"GB</div></td>"
                    +"<td></td>"
                    +"</tr>";
            $("#tab_zone").append(tr);
            
            //绘制饼图
        	//内存(GB)
	      
	            $('#nc_'+xid).highcharts({
	                chart:{
	                    margin: [ 28, 0, 0, 0],
	                },
                    colors: [
                        '#0e2d5b','#cac8c8'
                    ],
	                credits: {
	                    enabled: false
	                },
	                title: {
	                    text: '内存(GB)',
                        style:{
                            "fontSize": "14px"
                        }
	                },
	                tooltip: {
	                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	                },
	                plotOptions: {
	                    pie: {
	                        dataLabels: {
	                            enabled: false
	                        }
	                    }
	                },
	                series: [{
	                    type: 'pie',
	                    name: '内存',
	                    data: [
	                        {
	                            name: '已使用',
	                            y: ram_used,
	                            sliced: true,
	                            selected: true
	                        },
	                        ['未使用', ram_total-ram_used]
	                    ]
	                }]
	            });
	      
	        //CPU(GHz)
	        
	            $('#cpu_'+xid).highcharts({
	                chart:{
	                    margin: [ 28, 0, 0, 0],
	                },
                    colors: [
                        '#0e2d5b','#cac8c8'
                    ],
	                credits: {
	                    enabled: false
	                },
	                title: {
	                    text: 'CPU(GHz)',
                        style:{
                            "fontSize": "14px"
                        }
	                },
	                tooltip: {
	                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	                },
	                plotOptions: {
	                    pie: {
	                        dataLabels: {
	                            enabled: false
	                        }
	                    }
	                },
	                series: [{
	                    type: 'pie',
	                    name: 'CPU',
	                    data: [
	                        {
	                            name: '已使用',
	                            y: cpu_used,
	                            sliced: true,
	                            selected: true
	                        },
	                        ['未使用', cpu_total - cpu_used]
	                    ]
	                }]
	            });
	  
	        //存储(GB)
	     
	            $('#cc_'+xid).highcharts({
	                chart:{
	                    margin: [ 28, 0, 0, 0],
	                },
                    colors: [
                        '#0e2d5b','#cac8c8'
                    ],
	                credits: {
	                    enabled: false
	                },
	                title: {
	                    text: '存储(GB)',
                        style:{
                            "fontSize": "14px"
                        }
	                },
	                tooltip: {
	                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	                },
	                plotOptions: {
	                    pie: {
	                        dataLabels: {
	                            enabled: false
	                        }
	                    }
	                },
	                series: [{
	                    type: 'pie',
	                    name: '存储',
	                    data: [
	                        {
	                            name: '已使用',
	                            y: storage_used,
	                            sliced: true,
	                            selected: true
	                        },
	                        ['未使用', storage_total-storage_used]
	                    ]
	                }]
	            });
	     
	        //已分配主存储(GB)
	     
	            $('#yfpzcc_'+xid).highcharts({
	                chart:{
	                    margin: [ 28, 0, 0, 0],
	                },
                    colors: [
                        '#0e2d5b','#cac8c8'
                    ],
	                credits: {
	                    enabled: false
	                },
	                title: {
	                    text: '主存储GB)',
                        style:{
                            "fontSize": "14px"
                        }
	                },
	                tooltip: {
	                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	                },
	                plotOptions: {
	                    pie: {
	                        dataLabels: {
	                            enabled: false
	                        }
	                    }
	                },
	                series: [{
	                    type: 'pie',
	                    name: '主存储',
	                    data: [
	                        {
	                            name: '已使用',
	                            y: storage_1_used,
	                            sliced: true,
	                            selected: true
	                        },
	                        ['未使用', storage_1_total-storage_1_used]
	                    ]
	                }]
	            });
	      
	        //辅助存储(GB)
	            $('#fzcc_'+xid).highcharts({
	                chart:{
	                    margin: [ 28, 0, 0, 0],
	                },
                    colors: [
                        '#0e2d5b','#cac8c8'
                    ],
	                credits: {
	                    enabled: false
	                },
	                title: {
	                    text: '辅助存储(GB)',
                        style:{
                            "fontSize": "14px"
                        }
	                },
	                tooltip: {
	                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	                },
	                plotOptions: {
	                    pie: {
	                        dataLabels: {
	                            enabled: false
	                        }
	                    }
	                },
	                series: [{
	                    type: 'pie',
	                    name: '辅助存储',
	                    data: [
	                        {
	                            name: '已使用',
	                            y: storage_2_used,
	                            sliced: true,
	                            selected: true
	                        },
	                        ['未使用', storage_2_total-storage_2_used]
	                    ]
	                }]
	            });
        }

        function cluster_add(c) {
            var tr = "<tr>"
                    + "<td>"+c.name+"</td>"
                    + "<td>"+c.hypervisortype+"</td>"
                    + "<td>"+c.clustertype+"</td>"
                    + "<td>"+c.allocationstate+"</td>"
                    +"</tr>";
            $("#tab_cluster").append(tr);
        }

        function host_add(h) {
            var dtid= "demo_h_"+Math.floor(Math.random() * ( 1000 + 1));
            var tr1 = '<tr data-toggle="collapse" data-target="#'+dtid+'" >';
            tr1 += "<td>"+h.name+"</td>"; //name
            tr1 += "<td>"+h.ipaddress+"</td>"; //ip
            tr1 += "<td>"+h.state+"</td>"; //status
            tr1 += "</tr>";
            var tr2 = '<tr id="'+dtid+'" class="collapse" style="background: #ede7e7;">';
            tr2 += '<td colspan="3">';
            tr2 += '<table class="table table-bordered table-condensed">'
            tr2 += '<tr><td>CPU个数</td><td>CPU速率(GHz)</td><td>已分配CPU</td><td>内存总量(GB)</td><td>已分配内嫿GB)</td><td>已使用内嫿GB)</td></tr>';
            tr2 += '<td>'+h.cpunumber+'</td>';
            tr2 += '<td>'+ForDight(h.cpuspeed/1024,1)+'</td>';
            tr2 += '<td>'+h.cpuallocated+'</td>';
            tr2 += '<td>'+ForDight(h.memorytotal/1024/1024/1024, 1)+'</td>';
            tr2 += '<td>'+ForDight(h.memoryallocated/1024/1024/1024, 1)+'</td>';
            tr2 += '<td></td>';
            tr2 += '</table>';
            tr2 += "</td>";
            tr2 += "</tr>";
            $("#tab_host").append(tr1);
            $("#tab_host").append(tr2);
        }
        function vm_add(v) {
            var dtid= "demo_v_"+Math.floor(Math.random() * ( 1000 + 1));
            var tr1 = '<tr data-toggle="collapse" data-target="#'+dtid+'" >';
            tr1 += "<td>"+v.name+"</td>"; //name
            tr1 += "<td>"+v.nic[0].ipaddress+"</td>"; //ip
            tr1 += "<td>"+v.state+"</td>"; //status
            tr1 += "</tr>";
            var tr2 = '<tr id="'+dtid+'" class="collapse" style="background: #ede7e7;">';
            tr2 += '<td colspan="3">';
            tr2 += '<table class="table table-bordered table-condensed">'
            tr2 += '<tr><td>CPU个数</td><td>CPU速率(MHz)</td><td>内存总量(GB)</td><td>存储总量(GB)</td></tr>';
            tr2 += '<td>'+v.cpunumber+'</td>';
            tr2 += '<td>'+v.cpuspeed+'</td>';
            tr2 += '<td>'+ForDight(v.memory/1024,1)+'</td>';
            var st = 0;
            for (var i=0; i<v.volume.length; i++) {
                st += v.volume[i].size;
            }
            tr2 += '<td>'+ForDight(st/1024/1024/1024,1)+'</td>';
            tr2 += '</table>';
            tr2 += "</td>";
            tr2 += "</tr>";
            $("#tab_vm").append(tr1);
            $("#tab_vm").append(tr2);
        }
        
        function on_get_zone(err, z) {
        	if (err) return;
        	  	
        	for (var i=0; i<z["listzonesresponse"]["count"]; i++) {
                zone_add(z["listzonesresponse"]["zone"][i]);
            } 
        }
        function on_get_cluster(err, c) {
        	for (var i=0; i<c["listclustersresponse"]["count"]; i++) {
                cluster_add(c["listclustersresponse"]["cluster"][i]);
            }
        }
        function on_get_host(err, h) {
			for (var i=0; i<h["listhostsresponse"]["count"]; i++) {
                var x = h["listhostsresponse"]["host"][i];
                if ("Routing" === x.type) {
                    host_add(x);
                }
            }
    	}
    	
    	var volume = {};
    	function on_get_virtualmachine(err, v) {
    		for (var i=0; i<v.listvirtualmachinesresponse.count; i++) {
                var x = v.listvirtualmachinesresponse.virtualmachine[i];
                x['volume'] = volume[x['id']]||[];
                vm_add(x);
            }
    	}
    	function on_get_volume(err, d) {
    		for (var i=0; i<d.listvolumesresponse.count; i++) {
                var h = d.listvolumesresponse.volume[i];
                var vid = h['virtualmachineid'];
                volume[vid] = volume[vid]||[];
                volume[vid].push(h);
            }
        // var url = 'http://192.168.100.10:8096/client/api?command=listVirtualMachines&details=all&account=admin&response=json';
         getRest('listVirtualMachines',on_get_virtualmachine);
    	}
    	
    	function getRest(command,callback){
    		var errortag = true;
    		$.ajax({//调用JQuery提供的Ajax方法 
				type : "GET",
				url : "servlet/cloudstack",
				data : {command:command},
				dataType : "json",
				success : function(data){//回调函数 
					errortag = false;
					console.log('data：',data);
					callback(errortag,data);
				},
				error : function() {
					errortag = true;
					alert("系统出现问题");
				}
			});
    	}
    	
        //////////////////////  以下为测试代瞿 ///////////////////////////////////
        $(function(){
    		  var tr ="<tr><td><img class='loading-img' src='images/Loading.gif'/></td></tr>";    
            $("#tab_zone").append(tr);
            //var z = { "listzonesresponse" : { "count":1 ,"zone" : [  {"id":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","name":"Zone1","dns1":"192.168.1.1","internaldns1":"192.168.1.1","networktype":"Basic","securitygroupsenabled":true,"allocationstate":"Enabled","zonetoken":"5988a037-301f-3185-88f2-fac86dbe8f59","dhcpprovider":"VirtualRouter","capacity":[{"type":5,"capacityused":5,"capacitytotal":20,"percentused":"25"},{"type":8,"capacityused":6,"capacitytotal":30,"percentused":"20"},{"type":6,"capacityused":10753146880,"capacitytotal":462179794944,"percentused":"2.33"},{"type":0,"capacityused":6039797760,"capacitytotal":15591078336,"percentused":"38.74"},{"type":3,"capacityused":215045595648,"capacitytotal":3871151751168,"percentused":"5.56"},{"type":2,"capacityused":16931356672,"capacitytotal":1935575875584,"percentused":"0.87"},{"type":1,"capacityused":9500,"capacitytotal":25536,"percentused":"37.2"}],"localstorageenabled":false,"tags":[]} ] } };
            //on_get_zone(null, z);
            //var url = 'http://192.168.100.10:8096/client/api?command=listZones&showcapacities=true&response=json';
            getRest('listZones',on_get_zone);
            
            //var c = { "listclustersresponse" : { "count":2 ,"cluster" : [  {"id":"16151f69-caff-4e08-a6d1-446818e6b483","name":"KvmCluster1","podid":"40815a50-ed33-4179-a83e-bec067cc60a9","podname":"Pod1","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","hypervisortype":"KVM","clustertype":"CloudManaged","allocationstate":"Enabled","managedstate":"Managed","capacity":[{"type":2,"capacityused":0,"capacitytotal":0,"percentused":"0"},{"type":1,"capacityused":1500,"capacitytotal":12768,"percentused":"11.75"},{"type":0,"capacityused":1744830464,"capacitytotal":8145260544,"percentused":"21.42"}],"cpuovercommitratio":"1.0","memoryovercommitratio":"1.0"}, {"id":"76599c2a-63fb-4c69-b2b9-b2b11bda9219","name":"XenCluster1","podid":"40815a50-ed33-4179-a83e-bec067cc60a9","podname":"Pod1","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","hypervisortype":"XenServer","clustertype":"CloudManaged","allocationstate":"Enabled","managedstate":"Managed","capacity":[{"type":2,"capacityused":10934026240,"capacitytotal":967787937792,"percentused":"1.13"},{"type":1,"capacityused":8000,"capacitytotal":12768,"percentused":"62.66"},{"type":0,"capacityused":4294967296,"capacitytotal":7445817792,"percentused":"57.68"},{"type":3,"capacityused":214748364800,"capacitytotal":1935575875584,"percentused":"11.09"}],"cpuovercommitratio":"1.0","memoryovercommitratio":"1.0"} ] } };
            //on_get_cluster(null, c);
            //var url = 'http://192.168.100.10:8096/client/api?command=listClusters&showcapacities=true&response=json';
            getRest('listClusters',on_get_cluster);
            
            //var h = { "listhostsresponse" : { "count":4 ,"host" : [  {"id":"8bdd64f1-1f73-4ed2-a3d6-799864d01e43","name":"xen.zonesion.com.cn","state":"Up","type":"Routing","ipaddress":"192.168.1.12","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","podid":"40815a50-ed33-4179-a83e-bec067cc60a9","podname":"Pod1","version":"4.3.0","hypervisor":"XenServer","cpusockets":1,"cpunumber":4,"cpuspeed":3192,"cpuallocated":"31.33%","cpuwithoverprovisioning":"12768.0","memorytotal":7445817792,"memoryallocated":2147483648,"capabilities":"xen-3.0-x86_64 , xen-3.0-x86_32p , hvm-3.0-x86_32 , hvm-3.0-x86_32p , hvm-3.0-x86_64","lastpinged":"1970-01-17T11:50:40+0800","managementserverid":128454786796685,"clusterid":"76599c2a-63fb-4c69-b2b9-b2b11bda9219","clustername":"XenCluster1","clustertype":"CloudManaged","islocalstorageactive":false,"created":"2015-04-22T17:38:53+0800","resourcestate":"Enabled","hypervisorversion":"6.2.0","hahost":false}, {"id":"3c5d7cf6-e022-434f-8a75-63412a963e51","name":"s-1-VM","state":"Up","disconnected":"2015-04-22T17:28:40+0800","type":"SecondaryStorageVM","ipaddress":"192.168.1.29","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","podid":"40815a50-ed33-4179-a83e-bec067cc60a9","podname":"Pod1","version":"4.3.0","lastpinged":"1970-01-17T11:49:46+0800","managementserverid":128454786796685,"islocalstorageactive":false,"created":"2015-04-22T15:45:03+0800","resourcestate":"Enabled"}, {"id":"d1ac7b86-8bcc-4011-8260-b5d17b1fa518","name":"v-2-VM","state":"Up","disconnected":"2015-04-22T17:28:40+0800","type":"ConsoleProxy","ipaddress":"192.168.1.19","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","podid":"40815a50-ed33-4179-a83e-bec067cc60a9","podname":"Pod1","version":"4.3.0","lastpinged":"1970-01-17T11:49:46+0800","managementserverid":128454786796685,"islocalstorageactive":false,"created":"2015-04-22T15:44:39+0800","resourcestate":"Enabled"}, {"id":"89a1f813-03fd-4b40-b0cc-c1caa51194ad","name":"kvm.zonesion.com.cn","state":"Up","disconnected":"2015-04-22T17:28:40+0800","type":"Routing","ipaddress":"192.168.1.13","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","podid":"40815a50-ed33-4179-a83e-bec067cc60a9","podname":"Pod1","version":"4.3.0","hypervisor":"KVM","cpusockets":1,"cpunumber":4,"cpuspeed":3192,"cpuallocated":"11.75%","cpuwithoverprovisioning":"12768.0","memorytotal":8145260544,"memoryallocated":1744830464,"capabilities":"hvm,snapshot","lastpinged":"1970-01-17T11:49:46+0800","managementserverid":128454786796685,"clusterid":"16151f69-caff-4e08-a6d1-446818e6b483","clustername":"KvmCluster1","clustertype":"CloudManaged","islocalstorageactive":false,"created":"2015-04-22T15:42:38+0800","resourcestate":"Enabled","hahost":false} ] } };
            //on_get_host(null, h);
            //var url = 'http://192.168.100.10:8096/client/api?command=listHosts&details=capacity&response=json';
            getRest('listHosts',on_get_host);
            
            //var v = { "listvirtualmachinesresponse" : { "count":3 ,"virtualmachine" : [  {"id":"bc99301f-1cae-4c71-b090-fc4a501fc3ff","name":"win7","displayname":"win7","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","created":"2015-04-23T10:36:45+0800","state":"Running","haenable":false,"zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","hostid":"8bdd64f1-1f73-4ed2-a3d6-799864d01e43","hostname":"xen.zonesion.com.cn","templateid":"1c81ab15-715d-42da-a424-a01525405f16","templatename":"win7","templatedisplaytext":"x64","passwordenabled":false,"isoid":"1c81ab15-715d-42da-a424-a01525405f16","isoname":"win7","isodisplaytext":"x64","serviceofferingid":"85ef6382-8597-422b-aedd-739890c289a3","serviceofferingname":"Master","cpunumber":2,"cpuspeed":2000,"memory":2048,"cpuused":"5.9%","networkkbsread":14,"networkkbswrite":0,"diskkbsread":0,"diskkbswrite":0,"diskioread":0,"diskiowrite":0,"guestosid":"ecaff1a4-e8bc-11e4-b86d-74d435efcc8d","rootdeviceid":0,"rootdevicetype":"ROOT","securitygroup":[{"id":"510fdd80-e8bd-11e4-b86d-74d435efcc8d","name":"default","description":"Default Security Group","account":"admin","ingressrule":[],"egressrule":[],"tags":[]}],"nic":[{"id":"cc23928e-10c0-48c1-8608-86989b239a4f","networkid":"cf854351-0444-4bfb-bdca-5848589b2604","networkname":"defaultGuestNetwork","netmask":"255.255.255.0","gateway":"192.168.1.1","ipaddress":"192.168.1.36","broadcasturi":"vlan://untagged","traffictype":"Guest","type":"Shared","isdefault":true,"macaddress":"06:a5:c4:00:00:1b"}],"hypervisor":"XenServer","instancename":"i-2-19-VM","tags":[],"affinitygroup":[],"displayvm":true,"isdynamicallyscalable":false}, {"id":"e0273ed9-dea8-46dd-ad4a-3a3ebdc66086","name":"testss","displayname":"testss","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","created":"2015-04-23T09:02:59+0800","state":"Running","haenable":false,"zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","hostid":"8bdd64f1-1f73-4ed2-a3d6-799864d01e43","hostname":"xen.zonesion.com.cn","templateid":"3ef8e7e1-20c3-4657-9d7a-6f317ce5968b","templatename":"CentOS-6.5(mini)","templatedisplaytext":"x64","passwordenabled":false,"isoid":"3ef8e7e1-20c3-4657-9d7a-6f317ce5968b","isoname":"CentOS-6.5(mini)","isodisplaytext":"x64","serviceofferingid":"85ef6382-8597-422b-aedd-739890c289a3","serviceofferingname":"Master","cpunumber":2,"cpuspeed":2000,"memory":2048,"cpuused":"0.02%","networkkbsread":1556,"networkkbswrite":0,"diskkbsread":0,"diskkbswrite":0,"diskioread":0,"diskiowrite":0,"guestosid":"f29adbf6-e8bc-11e4-b86d-74d435efcc8d","rootdeviceid":0,"rootdevicetype":"ROOT","securitygroup":[{"id":"510fdd80-e8bd-11e4-b86d-74d435efcc8d","name":"default","description":"Default Security Group","account":"admin","ingressrule":[],"egressrule":[],"tags":[]}],"nic":[{"id":"21256a9d-4d7a-49f3-956f-c743efc3fab1","networkid":"cf854351-0444-4bfb-bdca-5848589b2604","networkname":"defaultGuestNetwork","netmask":"255.255.255.0","gateway":"192.168.1.1","ipaddress":"192.168.1.35","broadcasturi":"vlan://untagged","traffictype":"Guest","type":"Shared","isdefault":true,"macaddress":"06:42:d6:00:00:1a"}],"hypervisor":"XenServer","instancename":"i-2-18-VM","tags":[],"affinitygroup":[],"displayvm":true,"isdynamicallyscalable":false}, {"id":"eb3cf6c1-d227-4ed1-b982-6006e820ee65","name":"centos01","displayname":"centos01","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","created":"2015-04-22T18:23:30+0800","state":"Stopped","haenable":false,"zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","templateid":"3ef8e7e1-20c3-4657-9d7a-6f317ce5968b","templatename":"CentOS-6.5(mini)","templatedisplaytext":"x64","passwordenabled":false,"isoid":"3ef8e7e1-20c3-4657-9d7a-6f317ce5968b","isoname":"CentOS-6.5(mini)","isodisplaytext":"x64","serviceofferingid":"85ef6382-8597-422b-aedd-739890c289a3","serviceofferingname":"Master","cpunumber":2,"cpuspeed":2000,"memory":2048,"cpuused":"0.03%","networkkbsread":12604,"networkkbswrite":0,"diskkbsread":0,"diskkbswrite":0,"diskioread":0,"diskiowrite":0,"guestosid":"f29adbf6-e8bc-11e4-b86d-74d435efcc8d","rootdeviceid":0,"rootdevicetype":"ROOT","securitygroup":[{"id":"510fdd80-e8bd-11e4-b86d-74d435efcc8d","name":"default","description":"Default Security Group","account":"admin","ingressrule":[],"egressrule":[],"tags":[]}],"nic":[{"id":"d597f226-53f4-41c2-9d4f-7021f1a81db8","networkid":"cf854351-0444-4bfb-bdca-5848589b2604","networkname":"defaultGuestNetwork","netmask":"255.255.255.0","gateway":"192.168.1.1","ipaddress":"192.168.1.31","broadcasturi":"vlan://untagged","traffictype":"Guest","type":"Shared","isdefault":true,"macaddress":"06:9b:f2:00:00:16"}],"hypervisor":"XenServer","instancename":"i-2-16-VM","tags":[],"affinitygroup":[],"displayvm":true,"isdynamicallyscalable":false} ] } };
            //var d = { "listvolumesresponse" : { "count":4 ,"volume" : [  {"id":"36686dde-a5b8-48bc-bd83-54a368c80196","name":"x","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","type":"DATADISK","deviceid":1,"virtualmachineid":"eb3cf6c1-d227-4ed1-b982-6006e820ee65","vmname":"centos01","vmdisplayname":"centos01","vmstate":"Stopped","size":5368709120,"created":"2015-04-23T11:26:12+0800","state":"Ready","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","storagetype":"shared","hypervisor":"XenServer","diskofferingid":"0e4916e0-4a79-421e-b4c3-9c8c4f815154","diskofferingname":"Small","diskofferingdisplaytext":"Small Disk, 5 GB","storage":"XenPrimary","attached":"2015-04-23T11:27:52+0800","destroyed":false,"isextractable":true,"tags":[],"displayvolume":true,"path":"461a6075-ca6f-405d-81dd-098442d486fb","storageid":"fe72566c-9098-3129-9c39-dd96aa6ba773","quiescevm":false}, {"id":"71c21bcc-da34-436a-81e0-521366bc9f0f","name":"ROOT-19","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","type":"ROOT","deviceid":0,"virtualmachineid":"bc99301f-1cae-4c71-b090-fc4a501fc3ff","vmname":"win7","vmdisplayname":"win7","vmstate":"Running","size":107374182400,"created":"2015-04-23T10:36:45+0800","state":"Ready","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","storagetype":"shared","hypervisor":"XenServer","storage":"XenPrimary","destroyed":false,"serviceofferingid":"9ee21390-9554-4da5-88e6-e1ddfad28caa","serviceofferingname":"Large","serviceofferingdisplaytext":"Large Disk, 100 GB","isextractable":true,"tags":[],"displayvolume":true,"path":"f3b1f827-ea93-4f50-833d-cb2b6eb2d042","storageid":"fe72566c-9098-3129-9c39-dd96aa6ba773","quiescevm":false}, {"id":"effaf0f6-c763-40de-bf61-97b6f7ab0443","name":"ROOT-18","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","type":"ROOT","deviceid":0,"virtualmachineid":"e0273ed9-dea8-46dd-ad4a-3a3ebdc66086","vmname":"testss","vmdisplayname":"testss","vmstate":"Running","size":107374182400,"created":"2015-04-23T09:02:59+0800","state":"Ready","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","storagetype":"shared","hypervisor":"XenServer","storage":"XenPrimary","destroyed":false,"serviceofferingid":"9ee21390-9554-4da5-88e6-e1ddfad28caa","serviceofferingname":"Large","serviceofferingdisplaytext":"Large Disk, 100 GB","isextractable":true,"tags":[],"displayvolume":true,"path":"2b227452-4d80-4484-9b43-7614dc09efde","storageid":"fe72566c-9098-3129-9c39-dd96aa6ba773","quiescevm":false}, {"id":"969f5861-49f3-4a78-971b-982bb27a992a","name":"ROOT-16","zoneid":"e8e6af7b-8b46-4626-8a9a-771acf2fdeb6","zonename":"Zone1","type":"ROOT","deviceid":0,"virtualmachineid":"eb3cf6c1-d227-4ed1-b982-6006e820ee65","vmname":"centos01","vmdisplayname":"centos01","vmstate":"Stopped","size":107374182400,"created":"2015-04-22T18:23:30+0800","state":"Ready","account":"admin","domainid":"e9584646-e8bc-11e4-b86d-74d435efcc8d","domain":"ROOT","storagetype":"shared","hypervisor":"XenServer","storage":"XenPrimary","destroyed":false,"serviceofferingid":"9ee21390-9554-4da5-88e6-e1ddfad28caa","serviceofferingname":"Large","serviceofferingdisplaytext":"Large Disk, 100 GB","isextractable":true,"tags":[],"displayvolume":true,"path":"6ad09fbd-eee7-4202-92d6-0ad54bfc492d","storageid":"fe72566c-9098-3129-9c39-dd96aa6ba773","quiescevm":false} ] } };
            //var url = 'http://192.168.100.10:8096/client/api?command=listVolumes&account=admin&response=json';
            getRest('listVolumes',on_get_volume);
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
            <li><a onClick="onclick_2(this)" href="#qy" data-toggle="tab"><span>区域</span></a></li>
            <li><a onClick="onclick_2(this)" href="#qj" data-toggle="tab"><span>群集</span></a></li>
            <li><a onClick="onclick_2(this)" href="#zj" data-toggle="tab"><span>主机</span></a></li>
            <li><a onClick="onclick_2(this)" href="#yh" data-toggle="tab"><span>虚拟机</span></a></li>
        </ul>
    </div>
    <!--二级导航栏E-->
    <!--内容S-->
    <div class="content">
        <!--区域S-->
        <div class="panel panel-default fade in active" id="qy">
            <div class="panel-heading">
                <h3 class="panel-title">
                    CloudStack > 区域
                </h3>
            </div>
            <div class="panel-body">
                <table id="tab_zone" class="table table-bordered table-condensed">
							
                </table>
            </div>
        </div>
        <!--区域E-->
        <!--群集信息S-->
        <div class="panel panel-default fade" id="qj">
            <div class="panel-heading">
                <h3 class="panel-title">
                    CloudStack > 群集
                </h3>
            </div>
            <div class="panel-body">
                <table id="tab_cluster" class="table table-bordered table-condensed table-hover">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>虚拟机管理程序</th>
                        <th>群集类型</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody>

                </table>
            </div>
        </div>
        <!--群集信息E-->
        <!--主机信息S-->
        <div class="panel panel-default fade" id="zj">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        CloudStack > 主机
                    </h3>
                </div>
                <div class="panel-body">
                    <table id="tab_host" class="table table-bordered table-condensed table-hover">
                        <thead>
                        <tr>
                            <th width="25%">名称</th>
                            <th width="50%">IP地址</th>
                            <th width="25%">状态</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!--主机信息E-->
        <!--用户虚拟机信息S-->
        <div class="panel panel-default fade" id="yh">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        CloudStack > 虚拟机
                    </h3>
                </div>
                <div class="panel-body">
                    <table id="tab_vm" class="table table-bordered table-condensed table-hover">
                        <thead>
                        <tr>
                            <th width="25%">名称</th>
                            <th width="50%">IP地址</th>
                            <th width="25%">状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr data-toggle="collapse" data-target="#21" >

                        </tr>
                        <tr id="21" class="collapse" style="background: #ede7e7;">
                            <td colspan="3">
                                <table class="table table-bordered table-condensed">

                                </table>
                            </td>
                        </tr>
                        <tr data-toggle="collapse"  data-target="#22" >

                        </tr>
                        <tr id="22" class="collapse" style="background: #ede7e7;">
                            <td colspan="3">
                                <table class="table table-bordered table-condensed">

                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <!--用户虚拟机信息E-->
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

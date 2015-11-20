//表盘中用到的颜色定义
green = '#55BF3B';
yellow = '#DDDF0D';
red = '#DF5353';
//绘制表盘函数
function getDial(id, data_id, title, unit, min, max, bandArea) {
    $(id).highcharts({

        chart: {
            type: 'gauge'
        },

        title: {
            text: title
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        pane: {
            startAngle: -150,
            endAngle: 150,
            background: [{
                backgroundColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                    stops: [
	                    [0, '#FFF'],
	                    [1, '#333']
                    ]
                },
                borderWidth: 0,
                outerRadius: '109%'
            }, {
                backgroundColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                    stops: [
	                    [0, '#333'],
	                    [1, '#FFF']
                    ]
                },
                borderWidth: 1,
                outerRadius: '107%'
            }, {
                // default background
            }, {
                backgroundColor: '#DDD',
                borderWidth: 0,
                outerRadius: '105%',
                innerRadius: '103%'
            }]
        },
        // the value axis
        yAxis: {
            min: min,
            max: max,

            minorTickInterval: 'auto',
            minorTickWidth: 1,
            minorTickLength: 10,
            minorTickPosition: 'inside',
            minorTickColor: '#666',

            tickPixelInterval: 30,
            tickWidth: 2,
            tickPosition: 'inside',
            tickLength: 10,
            tickColor: '#666',
            labels: {
                step: 2,
                rotation: 'auto'
            },
            title: {
                text: unit
            },
            plotBands: [{
                from: bandArea.layer1.from,
                to: bandArea.layer1.to,
                color: bandArea.layer1.color
            }, {
                from: bandArea.layer2.from,
                to: bandArea.layer2.to,
                color: bandArea.layer2.color
            }, {
                from: bandArea.layer3.from,
                to: bandArea.layer3.to,
                color: bandArea.layer3.color
            }]
        },

        series: [{
            name: title,
            data: [0],
            tooltip: {
                valueSuffix: unit
            }
        }]
    }
	);
}
//表盘赋值函数
function setDialData(showid,newVal) {
    var chart = $(showid).highcharts();
    var point = chart.series[0].points[0];
    point.update(newVal);
};

//画曲线图的方法
function showChart(sid, ctype, unit, step, data) {
    $(sid).highcharts({
        chart: {
            //renderTo: 'chart_1',
            type: ctype,
            animation: false,
            zoomType: 'x',
            height:300
        },
        credits: {
            enabled: false
        },
        legend: {
            enabled: false
        },
        title: {
            text: ''
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: ''
            },

            minorGridLineWidth: 0,
            gridLineWidth: 1,
            alternateGridColor: null
        },
        tooltip: {
            formatter: function () {
                return '' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br><b>' + this.y + unit + '</b>';
            }
        },
        plotOptions: {
            spline: {
                lineWidth: 2,
                states: {
                    hover: {
                        lineWidth: 3
                    }
                },
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            symbol: 'circle',
                            radius: 3,
                            lineWidth: 1
                        }
                    }
                }

            },
            line: {
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            symbol: 'circle',
                            radius: 3,
                            lineWidth: 1
                        }
                    }
                }

            }
        },
        series: [{
			marker: {
                symbol: 'square'
            },
            data: data,
            color:'#0066CC',
            step: step

        }]
        ,
        navigation: {
            menuItemStyle: {
                fontSize: '10px'
            }
        }
    });
}

//将JSON格式的数据转换成[x1,y1],[x2,y2],[x3,y3]...格式的数组
function DataAnalysis(data,timezone)
{
	var str='';
	var temp;
	var len=data.datapoints.length;
	if(timezone == null)
	{
		timezone = "+8";
	}
	var zoneOp = timezone.substring(0,1);
	var zoneVal = timezone.substring(1);
	//var tzSecond = zoneVal*3600000; 修改于2015年2月1日 连接自己的数据服务器没用到时区参数
	var tzSecond = 0;
	$.each(data.datapoints,function(i,ele){
		if(zoneOp =='+')
		{
			temp = Date.parse(ele.at)+tzSecond;
		}
		if(zoneOp =='-')
		{
			temp = Date.parse(ele.at)-tzSecond;
		}
		if(ele.value.indexOf("http") != -1)
		{
			str=str+'['+temp+',"'+ele.value+'"]';
		}else{
			str=str+'['+temp+','+ele.value+']';
		}
		if(i!=len-1)
			str=str+',';
	});
	return "["+ str+"]";
}
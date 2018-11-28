<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
	<script src="${ctxStatic}/snowLeopard/js/echarts.min.js"></script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<form:form id="inputForm" modelAttribute="oaLegalAidCount" action=""
	method="post" class="form-horizontal">
<div class="row">
	<div class="col-xs-1">
		<div class="form-group">
			<form:select path="year" class="input-xlarge form-control required" >
				<form:option value="2018" label="2018"/>
				<form:option value="2019" label="2019"/>
				<form:option value="2020" label="2020"/>
				<form:option value="2021" label="2021"/>
				<form:option value="2022" label="2022"/>
				<form:option value="2023" label="2023"/>
				<form:option value="2024" label="2024"/>
				<form:option value="2025" label="2025"/>
				<form:option value="2026" label="2026"/>
				<form:option value="2027" label="2027"/>
				<form:option value="2028" label="2028"/>
			</form:select>
		</div>
	</div>
	<div class="col-xs-1">
		<div class="form-group">
			<label class="input-xlarge control-label">年度</label>
		</div>
	</div>
</div>
</form:form>
<div id="legalAid_countYearArea" style="width:100%;height:200px;"></div>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var countYearArea = echarts.init(document.getElementById('legalAid_countYearArea'));
// ajax异步获取数据
function getData(){
	var year = $("#year").val();
	var areaId = $("#area").val();
	countYearArea.showLoading();
	$.post("${ctx}/oa/act/oaLegalAid/countYearArea",{"year":year},function(data){
		var dc = new Array();
		var an = new Array();
		for(var i=0;i<data.length;i++){
			dc[i]=data[i].count;
			an[i]=data[i].area.name;
		}
		countYearArea.hideLoading();
		// 使用指定的配置项和数据显示图表。
		countYearArea.setOption({
			title: {
				text: '法律援助申请案件咨询数量'
			},
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
			xAxis : {
				type : 'category',
				data : an,
			    axisTick: {
			        alignWithLabel: true
			    },
			    axisLabel:{
			    	interval: 0
			    }
			},
		    yAxis: {
		        type: 'value'
		    },
			series : [{
	            name:'申请数量',
	            type:'bar',
	            barWidth: '60%',
				data : dc,
                itemStyle:{
                    normal:{
                    	color: function(params) { 
							//首先定义一个数组 
                    		var colorList = [ 
                    		'#C33531','#EFE42A','#64BD3D','#EE9201','#29AAE3', 
                    		'#B74AE5','#0AAF9F','#C33531','#EFE42A','#64BD3D',
                    		'#EE9201','#29AAE3','#B74AE5','#0AAF9F',
                    		]; 
                    		return colorList[params.dataIndex] 
                    	}
                    }//end普通颜色设置
                },
			}]//end series
		});
	});
}
$(function(){
	getData();
	$("#year").click(getData);
});
</script>
</body>
</html>
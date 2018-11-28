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
<form:form id="inputForm" modelAttribute="correctUserAnalysis" action="" method="post" class="form-horizontal">
<div class="row">
	<div class="col-xs-12 bg-info" style="padding:5px">
		<div class="form-group" style="margin-bottom: 0px;">
		  <input id="idCard" name="idCard" type="text" maxlength="20" class="form-control required"
				 style="width:200px" value="${correctUserAnalysis.idCard }"/>
			<input id="analysisDate" name="analysisDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="<fmt:formatDate value="${correctUserAnalysis.analysisDate}" pattern="yyyy-MM-dd"/>" style="width:100px" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
		   <button type="button" id="search" class="btn btn-primary" value="查询">查询</button><br><br>
		</div>
	</div>
</div>
</form:form>
<div id="correctUserAnalysis" style="width:990px;height:90%;"></div>
<script type="text/javascript">
var countCaseType = echarts.init(document.getElementById('correctUserAnalysis'));
$(function(){
	getData1();
	$("#search").click(getData1);
});
function getData1(){
	var idCard = $("#idCard").val();
	var analysisDate = $("#analysisDate").val();//
	countCaseType.showLoading();
	$.post("${ctx}/info/correctUserAnalysis/analysisOneDay",{"idCard":idCard,"analysisDate":analysisDate},
		function(data){
		var cddata=new Array();
		for(var i=0;i<data.length;i++){
			cddata.push(data[i].averageValue);
		}
		countCaseType.hideLoading();
		// 使用指定的配置项和数据显示图表。
		countCaseType.setOption({
		    tooltip : {
		        trigger: 'axis',
				
		    },
		    legend: {
		        x : 'center',
		        showLegendSymbol:false,
		        data:['']
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    polar : [
		        {
		            indicator : [
		                {text : '攻击性',min:20, max  : 50},
		                {text : '焦虑',min:15, max  : 40},
		                {text : '平衡',min:20, max  : 100},
		                {text : '活力',min:10, max  : 40},
		                {text : '抑郁',min:10, max  : 25},
		                {text : '压力',min:20, max  : 40},
		                {text : '可疑',min:20, max  : 50},
		                {text : '自信',min:40, max  :100},
		                {text : '自我调节',min:50, max  : 100},
		                {text : '神经质',min:10, max  : 50}
		            ],
		            center : ['50%',200],
		            radius : 80,
		        },
		    ],
		    series : [
		        {
		            type: 'radar',
		             tooltip : {
		                trigger: 'item'
		            },
		            areaStyle: {
		            	normal: {
		            		color:'transparent'
		            	}
		            },
		            itemStyle: {normal: {areaStyle: {type: 'default'}}},
		            data : [
		                {
		                	name:'参数',
		                    value : cddata
		                }
		            ]
		        }
		    ]
		});
	});
}
</script>
</body>
</html>
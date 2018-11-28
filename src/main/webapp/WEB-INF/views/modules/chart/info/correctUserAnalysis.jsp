<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
	<script src="${ctxStatic}/snowLeopard/js/echarts.min.js"></script>
	<script type="text/javascript">
	var js_date=new Date(),js_year=js_date.getFullYear(),
	js_month=(js_date.getMonth()<9)?("0"+(js_date.getMonth()+1)):(js_date.getMonth()+1),
	js_day=(js_date.getDate()<10)?("0"+js_date.getDate()):js_date.getDate();
    $(function(){
		bindDate();
		$("#timeType").change(function(){bindDate($("#timeType option:selected").val())});
    });
    function bindDate(){
		//解除绑定事件
		$("#beginDate").unbind("click");
		$("#endDate").unbind("click");
		//设置默认时间
		$("#beginDate").val(js_year+"-"+js_month+"-"+js_day);
		$("#endDate").val(js_year+"-"+js_month+"-"+js_day);
		$("#idCard").val("${correctUserAnalysis.idCard }")
		//根据新的格式重新绑定
		$("#beginDate").bind("click",function(){WdatePicker({onpicked:function(){endDate.click();},dateFmt:'yyyy-MM-dd',isShowClear:false,maxDate:'#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'})})
		$("#endDate").bind("click",function(){WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'%y-%M-%d'})});
	}
	</script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<form:form id="inputForm" modelAttribute="correctUserAnalysis" action="" method="post" class="form-horizontal">
<div class="row">
	<div class="col-xs-12 bg-info" style="padding:5px">
		<div class="form-group" style="margin-bottom: 0px;">
		  <input id="idCard" name="idCard" type="text" maxlength="20" class="form-control required"
				 style="width:200px" value="${correctUserAnalysis.idCard }"/>
			<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="2018" style="width:100px" />
			<span>-</span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="2018" style="width:100px" />
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
	var beginDate = $("#beginDate").val();//查询开始日期
	var endDate = $("#endDate").val();//查询结束日期
	countCaseType.showLoading();
	$.post("${ctx}/info/correctUserAnalysis/analysisList",{"idCard":idCard,"beginDate":beginDate,"endDate":endDate},
		function(data){
		var ct = new Array();//x轴数据
		var ctD = new Array();//y轴数据
		var legend=new Array();
		var i=0;
		$.each(data,function(key,analysisList){
			legend.push(key);
			var yData={"name":key,"type":"line",'smooth': true};
			var aData=new Array();
			for(var k=0;k<analysisList.length;k++){
				aData.push(analysisList[k].averageValue);
				if(i==0){
					ct.push(analysisList[k].analysisDate);	
				}
			}
			i++;
 			yData.data=aData;
			ctD.push(yData);
		})
		//console.log(ct);
		//console.log(ctD);
		//console.log(ctD);
		countCaseType.hideLoading();
		// 使用指定的配置项和数据显示图表。
		countCaseType.setOption({
			   tooltip: {
			       trigger: "axis",
			       /* formatter: "{a} <br/>{b} : {c}" */
			   },
			 /*   tooltip: {
			       trigger: "item",
			       formatter: "{a} <br/>{b} : {c}" 
			   }, */
			   grid: {
				   	width:990,
			    	top : '6%',		    	
			        left: '3%',
			        right: '3%',
			        bottom: '6%',
			        containLabel: true,
			        backgroundColor:'#ccc'
			   },
			   legend: {
			       x: 'left',
			       data: legend
			      
			   },
			   xAxis: [
			       {
			           type: "category",
			           name: "x",
			           splitLine: {show: false},
			           data: ct
			       }
			   ],
			   yAxis: [
			       {
			           type: "log",
			           name: ""
			       }
			   ],
			   toolbox: {
			       show: true,
			       feature: {
			           mark: {
			               show: true
			           },
			           dataView: {
			               show: true,
			               readOnly: true
			           },
			           restore: {
			               show: true
			           },
			           saveAsImage: {
			               show: true
			           }
			       }
			   }, 
			   calculable: true, 
			   series:ctD
		});
	});
}
</script>
</body>
</html>
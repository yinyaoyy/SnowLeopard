<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/echarts.min.js"></script>
	<!-- 引入select2插件 -->
	<script type="text/javascript" src="${ctxStatic}/jquery-select2/3.4/select2.min.js"></script>
	<!-- 引入select2样式 -->
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-select2/3.4/select2.min.css" />
	<script type="text/javascript">
	//获取当前年月日
	var js_date=new Date(),js_year=js_date.getFullYear(),
		js_month=(js_date.getMonth()<9)?("0"+(js_date.getMonth()+1)):(js_date.getMonth()+1),
		js_day=(js_date.getDate()<10)?("0"+js_date.getDate()):js_date.getDate();
	$(function(){
		$("#areaIds").select2({
			closeOnSelect: false
		});
		bindDate($("#timeType option:selected").val());
		$("#timeType").change(function(){bindDate($("#timeType option:selected").val())});
	});
	//绑定日历控件时间
	function bindDate(fmt){
		//解除绑定事件
		$("#startDate").unbind("click");
		$("#endDate").unbind("click");
		//设置默认时间
		if(fmt=="yyyy"){
			$("#startDate").val(js_year);
			$("#endDate").val(js_year);
		}
		else if(fmt=="yyyy-MM"){
			$("#startDate").val(js_year+"-"+js_month);
			$("#endDate").val(js_year+"-"+js_month);
		}
		else{
			$("#startDate").val(js_year+"-"+js_month+"-"+js_day);
			$("#endDate").val(js_year+"-"+js_month+"-"+js_day);
		}
		//根据新的格式重新绑定
		$("#startDate").bind("click",function(){WdatePicker({onpicked:function(){endDate.click();},dateFmt:fmt,isShowClear:false,maxDate:'#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'})})
		$("#endDate").bind("click",function(){WdatePicker({dateFmt:fmt,isShowClear:false,minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'%y-%M-%d'})});
	}
	</script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<form:form id="inputForm" modelAttribute="articleCount" action=""
	method="post" class="form-horizontal">
<div class="row">
	<div class="col-xs-12 bg-info" style="padding:5px">
		<div class="form-group"  style="margin-bottom: 0px;height:40px;line-height:40px;">
			<select id="timeType" class="input-xlarge form-control required" style="width:50px">
				<option value="yyyy">年</option>
				<option value="yyyy-MM">月</option>
				<option value="yyyy-MM-dd">日</option>
			</select>
			<input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="2018" style="width:100px" />
			<span>-</span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="2018" style="width:100px" />
			<input id="areaParentId" type="hidden" value="5"/>
			<select id="areaIds" multiple="multiple" style="width:150px;height:30px;overflow:hidden;vertical-align:middle" >
				<c:forEach items="${areaList }" var="area">
				<option value="${area.id }">${area.name }</option>
				</c:forEach>
			</select>
			<button type="button" id="search" class="btn btn-primary" value="查询">查询</button>
			<span style="font-size:16px;font-weight:bold;">各旗县普法宣传文章统计图</span>
		</div>
	</div>
</div>
</form:form>
<div id="chart_countArticle" style="width:100%;height:400;"></div>
<table id="table_countArticle" class="table table-striped table-bordered table-condensed">
</table>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var countArticle = echarts.init(document.getElementById('chart_countArticle'));
// ajax异步获取数据
function getData(){
	var datePattern = $("#timeType").val();//日期格式
	var startDate = $("#startDate").val();//查询开始日期
	var endDate = $("#endDate").val();//查询结束日期
	var areaIds = $("#areaIds").val();//查询地区集合
	areaIds=(areaIds==null)?null:areaIds+"";
	countArticle.showLoading();
	$.post("${ctx}/chart/article/countArticle",{"datePattern":datePattern,
		"startDate":startDate,"endDate":endDate,"areaIds":areaIds},function(data){
		countArticle.hideLoading();
		// 使用指定的配置项和数据显示图表。
		countArticle.setOption({
			/* title: {
				text: '法律援助申请案件咨询数量'
			}, */
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    grid: {
		    	top : '6%',		    	
		        left: '3%',
		        right: '4%',
		        bottom: '6%',
		        containLabel: true
		    },
			xAxis : {
				type : 'category',
				data : data.names,
			    axisTick: {
			        alignWithLabel: true
			    },
			    axisLabel:{
			    	interval: -10,
                    rotate:-20
			    }
			},
		    yAxis: {
		        type: 'value'
		    },
			series : [{
	            name:'发布数量',
	            type:'bar',
	            barWidth: '60%',
				data : data.counts,
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
                    	},
                    	label : {show: true, position: 'top'}
                    }//end普通颜色设置
                },
			}]//end series
		});
		//加载表格内容
		getTableData(datePattern,startDate,endDate,areaIds);
	});
}
$(function(){
	getData();
	$("#search").click(getData);
});
function getTableData(datePattern,startDate,endDate,areaIds){
	$.post("${ctx}/chart/article/countArticleForTable",{"datePattern":datePattern,
		"startDate":startDate,"endDate":endDate,"areaIds":areaIds},function(data){
		var theadhtml = "<thead><tr>";
		for(var i=0;i<data[0].length;i++){
			theadhtml += "<td>"+data[0][i]+"</td>";
		}
		theadhtml+="</tr><thead>";
		var tbody="<tbody>";
		for(var i=1;i<data.length;i++){
			tbody += "<tr>";
			for(var j=0;j<data[i].length;j++){
				tbody += "<td>"+data[i][j]+"</td>";
			}
			tbody += "</tr>";
		}
		tbody+="</tbody>";
		$('#table_countArticle').html("");
		$('#table_countArticle').append(theadhtml+tbody);
		//修改父层iframe的高度
		var doc=window.parent.document;
		var iframe=doc.getElementById("aiframe4");
		if(data.length <= 4){
			iframe.style.height="610px";
		}else{
			iframe.style.height="900px";
		}
	});
}
</script>
</body>
</html>
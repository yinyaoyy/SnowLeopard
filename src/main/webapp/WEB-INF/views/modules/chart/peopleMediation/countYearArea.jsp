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
	//获取当前年月日
	var js_date=new Date(),js_year=js_date.getFullYear(),
		js_month=(js_date.getMonth()<9)?("0"+(js_date.getMonth()+1)):(js_date.getMonth()+1),
		js_day=(js_date.getDate()<10)?("0"+js_date.getDate()):js_date.getDate();
	$(function(){
		bindDate($("#timeType option:selected").val());
		$("#timeType").change(function(){bindDate($("#timeType option:selected").val())});
	});
	//绑定日历控件时间
	function bindDate(fmt){
		//解除绑定事件
		$("#beginDate").unbind("click");
		$("#endDate").unbind("click");
		//设置默认时间
		if(fmt=="yyyy"){
			$("#beginDate").val(js_year);
			$("#endDate").val(js_year);
		}
		else if(fmt=="yyyy-MM"){
			$("#beginDate").val(js_year+"-"+js_month);
			$("#endDate").val(js_year+"-"+js_month);
		}
		else{
			$("#beginDate").val(js_year+"-"+js_month+"-"+js_day);
			$("#endDate").val(js_year+"-"+js_month+"-"+js_day);
		}
		//根据新的格式重新绑定
		$("#beginDate").bind("click",function(){WdatePicker({onpicked:function(){endDate.click();},dateFmt:fmt,isShowClear:false,maxDate:'#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'})})
		$("#endDate").bind("click",function(){WdatePicker({dateFmt:fmt,isShowClear:false,minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'%y-%M-%d'})});
	}
	</script>
</head>
<body>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<form:form id="inputForm" modelAttribute="oaPeopleMediationApplyCount" action=""
	method="post" class="form-horizontal">
<div class="row">
	<div class="col-xs-12 bg-info" style="padding:5px">
		<div class="form-group"  style="margin-bottom: 0px;height:40px;line-height:40px;">
			<select id="timeType" class="input-xlarge form-control required" style="width:50px">
				<option value="yyyy">年</option>
				<option value="yyyy-MM">月</option>
				<option value="yyyy-MM-dd">日</option>
			</select>
			<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="2018" style="width:100px" />
			<span>-</span>
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="10" class="form-control required"
				value="2018" style="width:100px" />
			<button type="button" id="search" class="btn btn-primary" value="查询">查询</button>
			<span style="font-size:16px;font-weight:bold;">旗县调解案件数量</span>
		</div>
	</div>
</div>
</form:form>
<div id="peopleMediation_countYearArea" style="width:100%;height:250px;"></div>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var countYearArea = echarts.init(document.getElementById('peopleMediation_countYearArea'));
// ajax异步获取数据
/* function getData(){
	var year = $("#year").val();
	var areaId = $("#area").val();
	countYearArea.showLoading();
	$.post("${ctx}/chart/peopleMediation/countByYearArea",{"year":year},function(data){
		var ar = new Array();//区域集合
		var ac = new Array();//区域数量
		var total = 0;
		for(var i=0;i<data.length;i++){
			ar[i]=data[i].area.name;
			ac[i]={name:data[i].area.name,value:data[i].count};
			total+=data[i].count;
		}
		countYearArea.hideLoading();
		// 使用指定的配置项和数据显示图表。
		countYearArea.setOption({
			title: {
				text: '年度旗县调解案件占比'
			},
	    tooltip: {
	        trigger: 'item',
	        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        x: 'right',
	        y: 'middle',
	        data: ar, //区域
	        formatter: function (name) {
	        	for(var i=0;i<ac.length;i++){
	        		if(i==0&&name==ac[i].name){
	        			return '总计 : '+total+'项\n\n'+name+' : '+ac[i].value+'项';
	        		}
	        		else if(name==ac[i].name){
	        			return name+' : '+ac[i].value+'项';
	        		}
	        	}
	        	return name;
	        },
	        style:{
		        orient: 'vertical',
		        y: 'bottom',
	        }
	    },
	    series: [{
	        name:'调解数量',
	        type:'pie',
	        radius: ['50%', '70%'],
            label: {
                normal: {
                    formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                    backgroundColor: '#eee',
                    borderColor: '#aaa',
                    borderWidth: 1,
                    borderRadius: 4,
                    rich: {
                        a: {
                            color: '#999',
                            lineHeight: 22,
                            align: 'center'
                        },
                        hr: {
                            borderColor: '#aaa',
                            width: '100%',
                            borderWidth: 0.5,
                            height: 0
                        },
                        b: {
                            fontSize: 16,
                            lineHeight: 33
                        },
                        per: {
                            color: '#eee',
                            backgroundColor: '#334455',
                            padding: [2, 4],
                            borderRadius: 2
                        }
                    }
                }
            },
	        data: ac //区域数量
	    }]//end series
	});//end function
	});
} */
$(function(){
	 getData1();
	$("#search").click(getData1);
});
function getData1(){
	var beginDate = $("#beginDate").val();//查询开始日期
	var endDate = $("#endDate").val();//查询结束日期
	countYearArea.showLoading();
	$.post("${ctx}/chart/peopleMediation/countByYearArea",{"beginDate":beginDate,"endDate":endDate},function(data){
		var ar = new Array();//区域集合
		var ac = new Array();//区域数量
		var total = 0;
		for(var i=0;i<data.length;i++){
			ar[i]=data[i].area.name;
			ac[i]={name:data[i].area.name,value:data[i].count};
			total+=data[i].count;
		}
		countYearArea.hideLoading();
		
		
		var dc = new Array();
		var an = new Array();
		for(var i=0;i<data.length;i++){
			dc[i]=data[i].count;
			an[i]=data[i].area.name;
		}
		countYearArea.hideLoading();
		
		countYearArea.setOption({
			/* title: {
				text: '年度旗县严重等级数量'
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
				data : ar,
			    axisTick: {
			        alignWithLabel: true
			    },
			    axisLabel:{
			    	interval: -10,
                    rotate:-20
			    }
			},
		    yAxis: {
		        type: 'value',
		        splitNumber:3
		    },
			series : [{
	            name:'调解数量',
	            type:'bar',
	            barWidth: '60%',
				data : ac,
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
		/* countYearArea.setOption({
			
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
				data : ar,
			    axisTick: {
			        alignWithLabel: true
			    },
			    axisLabel:{
			    	 interval:0
                     
			    }
			},
		    yAxis: {
		        type: 'value',
		    },
			series : [{
	            name:'调解数量',
	            type:'bar',
	            barWidth: '60%',
				data : ac,
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
		}); */
	});
}
</script>
</body>
</html>
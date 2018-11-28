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
<form:form id="inputForm" modelAttribute="oaPeopleMediationApplyCount" action=""
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
<div id="peopleMediation_countYearArea" style="width:100%;height:500px;"></div>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var countYearArea = echarts.init(document.getElementById('peopleMediation_countYearArea'));
// ajax异步获取数据
function getData(){
	var year = $("#year").val();
	var areaId = $("#area").val();
	countYearArea.showLoading();
	$.post("${ctx}/oa/act/oaPeopleMediationApply/countByYearArea",{"year":year},function(data){
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
}
$(function(){
	getData();
	$("#year").click(getData);
});
</script>
</body>
</html>
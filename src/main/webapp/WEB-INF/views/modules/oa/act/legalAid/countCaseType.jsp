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
	<div class="col-xs-2">
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
	<div class="col-xs-2">
		<div class="form-group">
			<label class="input-xlarge control-label">年度</label>
		</div>
	</div>
	<div class="col-xs-4">
		<div class="form-group">
			<form:select path="area" class="input-xlarge form-control required">
				<form:options items="${areaList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
			</form:select>
		</div>
	</div>
</div>
</form:form>
<div id="legalAid_countCaseType" style="width:100%;height:300px;"></div>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var countCaseType = echarts.init(document.getElementById('legalAid_countCaseType'));
var caseTypes = ${caseTypes};
// ajax异步获取数据
function getData(){
	var year = $("#year").val();
	var areaId = $("#area").val();
	countCaseType.showLoading();
	$.post("${ctx}/oa/act/oaLegalAid/countCaseType",{"year":year,"area.id":areaId},function(data){
		var ct = new Array();
		var ctD = new Array();
		var total = 0;
		var hasVal = false;
		for(var i=0;i<caseTypes.length;i++){
			for(var j=0;j<data.length;j++){
				hasVal = false;
				if(caseTypes[i].value==data[j].caseType){
					ct[i]=data[i].caseTypeDesc;
					ctD[i]={name:data[i].caseTypeDesc,value:data[i].count};
					total = total + data[j].count;
					hasVal = true;
					break;
				}
			}
			if(!hasVal){
				ct[i]=caseTypes[i].label;
				ctD[i]={name:caseTypes[i].label,value:0};
			}
		}
		countCaseType.hideLoading();
		// 使用指定的配置项和数据显示图表。
		countCaseType.setOption({
			title: {
				text: '法律援助案件类型受理数量占比'
			},
	    tooltip: {
	        trigger: 'item',
	        formatter: "{a} <br/>{b}: {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        x: 'right',
	        y: 'middle',
	        data: ct, //案件类型
	        formatter: function (name) {
	        	for(var i=0;i<ctD.length;i++){
	        		if(i==0&&name==ctD[i].name){
	        			return '总计 : '+total+'项\n\n'+name+' : '+ctD[i].value+'项';
	        		}
	        		else if(name==ctD[i].name){
	        			return name+' : '+ctD[i].value+'项';
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
	        name:'受理数量',
	        type:'pie',
	        radius: ['50%', '70%'],
	        avoidLabelOverlap: false,
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
            labelLine: {
                normal: {
                    show: true
                }
            },
	        data: ctD //案件类型及数量
	    }]//end series
	});//end function
	});//end post
}
$(function(){
	getData();
	$("#area").click(getData);
});
</script>
</body>
</html>
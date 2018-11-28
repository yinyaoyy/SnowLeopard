<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>社区矫正心理生理分析管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出分析数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/correctUserAnalysis/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/correctUserAnalysis");
						//$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/correctUserAnalysis/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/correctUserAnalysis/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
					'</form>' +
					'<p style="text-align: center;" class="fileName" id="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>' +
					'</div>', {
						title: "导入数据",
						buttons: {},
						bottomText: "导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
					});
				$('#btnImportSubmit').bind('click',function(){
					$('#importForm').submit();
					var files = document.getElementById("uploadFile").files[0];
					if(files == null || files == undefined || files == "") {
						toVail("请上传文件","error");
						this.attr('disabled',false)
					}else{
						loading('正在提交，请稍等...');
						$(this).attr('disabled','disabled')
					}
				})
			});
			$("#btnAnalysis").click(function(){
				var idCard=$("#idCard").val();
				if(typeof(idCard)=="undefined"||idCard==""){ 
					toVail("身份证号不能为空","error"); 
				}else{
					top.$.jBox.open("iframe:${ctx}/info/correctUserAnalysis/analysis?idCard="+idCard, "心理生理状态分析",1000,600,{
						buttons:{"关闭":true}, bottomText:"通过身份证号,时间区间分析用户。",submit:function(v, h, f){
						}, loaded:function(h){
							$(".jbox-content", top.document).css("overflow-y","hidden");
						}
					});
				}
			});
			$(".analysisDate").click(function(){
				var idCard=$("#idCard").val();
				var analysisDate=$(this).data('name');
				top.$.jBox.open("iframe:${ctx}/info/correctUserAnalysis/analysisOneDayPage?idCard="+idCard+"&analysisDate="+analysisDate, "心理生理状态分析",1000,600,{
					buttons:{"关闭":true}, bottomText:"通过身份证号,时间分析用户。",submit:function(v, h, f){
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function changeFileName() {
			var file = document.getElementById("uploadFile").files[0];
			document.getElementById("fileName").innerHTML = file.name;
		}

		function checkLanguage() {
			var files = document.getElementById("uploadFile").files[0];
			if(files == null || files == undefined || files == "") {
				return false;
			} else {
				return true;
			}
		}		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/info/correctUserAnalysis/">社区矫正心理生理分析列表</a></li>
		<shiro:hasPermission name="info:correctUserAnalysis:edit"><li><a href="${ctx}/info/correctUserAnalysis/form">社区矫正心理生理分析添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="correctUserAnalysis" action="${ctx}/info/correctUserAnalysis/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		 <div class="form-group">
			<label>身份证号：</label>
			<form:input path="idCard" htmlEscape="false" maxlength="20" class="form-control"/>
		</div>
		 <div class="form-group">
	        <input id="btnSubmit" class="btn btn-primary" style="margin-left:10px;" type="submit" value="查询"/>
	        &nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
	        &nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
	        &nbsp;<input id="btnAnalysis" class="btn btn-primary" type="button" value="分析"/>
	       </div>
	</form:form>
	<sys:message content="${message}"/>
	
	<div> 
	   <p>姓名：${correctUser.name } 性别: ${fns:getDictLabel(correctUser.sex, 'sex', '')}  年龄：${ correctUser.age} </p>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>分析日期</th>
				<th>误差率</th>
				<th>参数</th>
				<th>最小值</th>
				<th>平均值</th>
				<th>最大值</th>
				<th>影像值</th>
				<shiro:hasPermission name="info:correctUserAnalysis:edit">
				<th >操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="correctUserAnalysisVo">
		     <tr>
		     <td rowspan="${fn:length(correctUserAnalysisVo.list)+1 }" class="analysisDate" data-name="<fmt:formatDate value="${correctUserAnalysisVo.analysisDate}" pattern="yyyy-MM-dd"/>" style="cursor:pointer;">
					<fmt:formatDate value="${correctUserAnalysisVo.analysisDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td rowspan="${fn:length(correctUserAnalysisVo.list)+1 }">
					${correctUserAnalysisVo.errorRate}
				</td>
		     </tr>
             <c:forEach items="${correctUserAnalysisVo.list}" var="correctUserAnalysis">
             	<tr>
			    <td>
				   ${fns:getDictLabel(correctUserAnalysis.type, 'info_correct_analysis_type', '无')} 
				</td>
				<td>
					${correctUserAnalysis.minValue}
				</td>
				<td>
					${correctUserAnalysis.averageValue}
				</td>
				<td>
					${correctUserAnalysis.maxValue}
				</td>
				<td>
					${correctUserAnalysis.imageValue}
				</td>
				<shiro:hasPermission name="info:correctUserAnalysis:edit"><td>
    				<a href="${ctx}/info/correctUserAnalysis/form?id=${correctUserAnalysis.id}">修改</a>
					<a href="${ctx}/info/correctUserAnalysis/delete?id=${correctUserAnalysis.id}" onclick="return confirmx('确认要删除该社区矫正心理生理分析吗？', this.href)">删除</a>
				</td></shiro:hasPermission> 
			</tr>    
             </c:forEach>	    
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
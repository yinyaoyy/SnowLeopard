<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法援中心管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出法援中心数据吗？", "系统提示", function(v, h, f) {
					if(v == "ok") {
						$("#searchForm").attr("action", "${ctx}/info/lawAssistance/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
					}
				}, {
					buttonsFocus: 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/lawAssistance/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/lawAssistance/import/template" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
		<li class="active"><a href="${ctx}/info/lawAssistance/">法援中心列表</a></li>
		<shiro:hasPermission name="info:lawAssistance:edit"><li><a href="${ctx}/info/lawAssistance/form">法援中心添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="lawAssistance" action="${ctx}/info/lawAssistance/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="control-label">名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" />
			</div>
			<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
				<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="overflow-x:auto;">
		<thead>
			<tr>
				<th>名称</th>
				<th width="140">电话</th>
				<th>办公时间</th>
				<th width="100">邮政编码</th>
				<th>联系地址</th>
				<shiro:hasPermission name="info:lawAssistance:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="lawAssistance">
			<tr>
				<td title="${lawAssistance.name}">
					${(fn:length(lawAssistance.name)>15)?(fn:substring(lawAssistance.name,1,17)).concat('...'):lawAssistance.name }
				</td>
				<td>
					${lawAssistance.mobile}
				</td>
				<td >
					${lawAssistance.worktime}
				</td>
				<td >
					${lawAssistance.zipcode}
				</td>
				<td title="${lawAssistance.address }">
				     ${(fn:length(lawAssistance.address)>15)?(fn:substring(lawAssistance.address,1,15)).concat('...'):lawAssistance.address }
				</td>
				<shiro:hasPermission name="info:lawAssistance:edit"><td style="width:165px">
    				<a href="${ctx}/info/lawAssistance/form?id=${lawAssistance.id}" >修改</a>
					<a href="${ctx}/info/lawAssistance/delete?id=${lawAssistance.id}" onclick="return confirmx('确认要删除该法援中心吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
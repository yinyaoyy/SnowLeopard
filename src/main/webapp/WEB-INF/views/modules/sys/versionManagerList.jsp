<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统版本管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/sys/versionManager/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/sys/versionManager/import/template" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
					'</form>' +
					'<p style="text-align: center;" class="fileName" id="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>' +
					'</div>', {
						title: "导入数据",
						buttons: {},
						bottomText: "导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
					});
				$('#btnImportSubmit').bind('click',function(){
					$('#importForm').submit();
					$(this).attr('disabled','disabled');
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
				toVail("请上传文件");
				return false;
			} else {
				return true;
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/versionManager/">系统版本管理列表</a></li>
		<shiro:hasPermission name="sys:versionManager:edit"><li><a href="${ctx}/sys/versionManager/form">系统版本管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="versionManager" action="${ctx}/sys/versionManager/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group" >
                <label class="control-label" >版本：</label>
				<form:input path="version" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
				<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
		</div>
		
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th>版本</th>
			    <th>描述</th>
			    <th>ios安装包</th>
			    <th>安卓安装包</th>
				<!-- <th>更新时间</th> -->
				<th>备注信息</th>
				
				<shiro:hasPermission name="sys:versionManager:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="versionManager">
			<tr>
				<%-- <td><a href="${ctx}/sys/versionManager/form?id=${versionManager.id}">
					<fmt:formatDate value="${versionManager.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td> --%>
				<td>
				  ${ empty versionManager.version || versionManager.version==""? '无' : versionManager.version}
				</td>
				<td>
				  ${ empty versionManager.represent || versionManager.represent==""? '无' : versionManager.represent}
				</td>
				<td>
				  ${ empty versionManager.iosUrl || versionManager.iosUrl==""? '无' : versionManager.iosUrl}
				</td>
				<td>
				  ${ empty versionManager.androidUrl || versionManager.androidUrl==""? '无' : versionManager.androidUrl}
				</td>
				<td>
					${versionManager.remarks}
				</td>
				<shiro:hasPermission name="sys:versionManager:edit"><td>
    				<a href="${ctx}/sys/versionManager/form?id=${versionManager.id}">修改</a>
					<a href="${ctx}/sys/versionManager/delete?id=${versionManager.id}" onclick="return confirmx('确认要删除该系统版本管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
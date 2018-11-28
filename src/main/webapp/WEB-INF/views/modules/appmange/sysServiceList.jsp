<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>web服务管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/appmange/sysService/">web服务列表</a></li>
		<shiro:hasPermission name="appmange:sysService:edit"><li><a href="${ctx}/appmange/sysService/form">web服务添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysService" action="${ctx}/appmange/sysService/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
				<label class="control-label">服务名：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" />
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
			<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
			<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>服务名</th>
				<th>人员机构</th>
				<th>排序</th>
				<th>修改时间</th>
				<shiro:hasPermission name="appmange:sysService:edit">
				<th >操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysService">
			<tr>
				<td><a href="${ctx}/appmange/sysService/form?id=${sysService.id}">
					${sysService.name}
				</a></td>
				<td>
				<c:set value="${ fn:split('sysService.officeId', ',') }" var="officeIds" />
					<c:forEach items="${officeIds }" var="ids"> 
					${fns:getDictLabel(ids, 'sys_office_category', '')}   
					</c:forEach>
				</td>
				<td>
					${sysService.sort}
				</td>
				<td>
					<fmt:formatDate value="${sysService.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
				<shiro:hasPermission name="appmange:sysService:edit"><td>
    				<a href="${ctx}/appmange/sysService/form?id=${sysService.id}">修改</a>
					<a href="${ctx}/appmange/sysService/delete?id=${sysService.id}" onclick="return confirmx('确认要删除该web服务吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
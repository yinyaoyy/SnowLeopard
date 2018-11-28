<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程节点管理</title>
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
		<li class="active"><a href="${ctx}/sys/nodeManager/">流程节点列表</a></li>
		<shiro:hasPermission name="sys:nodeManager:edit"><li><a href="${ctx}/sys/nodeManager/form">流程节点添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="nodeManager" action="${ctx}/sys/nodeManager/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
                 <label class="control-label">操作名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
                <label class="control-label">流程标识：</label>
				<form:input path="procDefKey" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
                <label class="control-label">节点标识：</label>
				<form:input path="taskDefKey" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
                <label class="control-label">版本：</label>
				<form:input path="version" htmlEscape="false" maxlength="50" class="form-control"/>
		</div>
		<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
		</div>
		
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>操作名称</th>
				<th>流程标识</th>
				<th>任务节点id</th>
				<th>pc页面</th>
				<th>ios页面</th>
				<th>安卓页面</th>
				<th width="50">版本</th>
				<th width="50">排序</th>
				<th width="150">更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="sys:nodeManager:edit">
				<th width="140">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="nodeManager">
			<tr>
				<td><a href="${ctx}/sys/nodeManager/form?id=${nodeManager.id}">	${nodeManager.name}</a></td>
				<td>${nodeManager.procDefKey}</td>
				<td title="${nodeManager.taskDefKey}">${nodeManager.taskDefKey}</td>
				<td>${nodeManager.pcUrl}</td>
				<td>${nodeManager.iosUrl}</td>
				<td>${nodeManager.androidUrl}</td>
				<td>${nodeManager.version}</td>
				<td>${nodeManager.sort}</td>
				<td><fmt:formatDate value="${nodeManager.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${nodeManager.remarks}</td>
				<shiro:hasPermission name="sys:nodeManager:edit"><td>
    				<a href="${ctx}/sys/nodeManager/form?id=${nodeManager.id}">修改</a>
					<a href="${ctx}/sys/nodeManager/delete?id=${nodeManager.id}" onclick="return confirmx('确认要删除该流程节点吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
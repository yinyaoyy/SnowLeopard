<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息推送管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/sysUserPush/">消息推送列表</a></li>
		<shiro:hasPermission name="sys:sysUserPush:edit">
			<li><a href="${ctx}/sys/sysUserPush/form">消息推送添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysUserPush"
		action="${ctx}/sys/sysUserPush/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>通知标题：</label>
			<form:input path="pushMessage.title" htmlEscape="false"
				maxlength="100" class="form-control w200" />
		</div>
		<div class="form-group">
			<label>通知内容：</label>
			<form:input path="pushMessage.msgContent" htmlEscape="false"
				maxlength="300" class="form-control w200" />
		</div>
		<%-- <div class="form-group">
			    <label>业务类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="100" class="input-medium"/>
			</div> --%>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>通知标题</th>
				<th>通知内容</th>
				<th width="150">发送时间</th>
				<th width="150">更新时间</th>
				<shiro:hasPermission name="sys:sysUserPush:edit">
					<th width="100">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="sysUserPush">
				<tr>
					<td><a href="${ctx}/sys/sysUserPush/form?id=${sysUserPush.id}">
							${sysUserPush.pushMessage.title} </a></td>
					<td>${sysUserPush.pushMessage.msgContent}</td>
					<td><fmt:formatDate value="${sysUserPush.sendTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${sysUserPush.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<shiro:hasPermission name="sys:sysUserPush:edit">
						<td>
							<%-- <a href="${ctx}/sys/sysUserPush/form?id=${sysUserPush.id}">修改</a> --%>
							<a href="${ctx}/sys/sysUserPush/delete?id=${sysUserPush.id}"
							onclick="return confirmx('确认要删除该消息推送吗？', this.href)">删除</a>
						</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法援申请管理</title>
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
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a href="${ctx}/oa/act/oaLegalAid/">法援申请列表</a></li>
		<shiro:hasPermission name="oa:act:oaLegalAid:edit"><li><a href="${ctx}/oa/act/oaLegalAid/form">法援申请添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="oaLegalAid" action="${ctx}/oa/act/oaLegalAid/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li><label>案情标题：</label>
				<form:input path="caseTitle" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>案情标题</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="oa:act:oaLegalAid:edit">
				<th >操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaLegalAid">
			<tr>
				<td><a href="${ctx}/oa/act/oaLegalAid/form?id=${oaLegalAid.id}">
					${oaLegalAid.name}
				</a></td>
				<td>
					${oaLegalAid.caseTitle}
				</td>
				<td>
					<fmt:formatDate value="${oaLegalAid.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${oaLegalAid.remarks}
				</td>
				<shiro:hasPermission name="oa:act:oaLegalAid:edit"><td>
    				<a href="${ctx}/oa/act/oaLegalAid/form?id=${oaLegalAid.id}">修改</a>
					<a href="${ctx}/oa/act/oaLegalAid/delete?id=${oaLegalAid.id}" onclick="return confirmx('确认要删除该法援申请吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
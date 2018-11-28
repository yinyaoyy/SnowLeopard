<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>专题管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
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
		<li class="active"><a href="${ctx}/cms/site/">专题列表</a></li>
		<shiro:hasPermission name="cms:site:edit">
			<li><a href="${ctx}/cms/site/form">专题添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="site"
		action="${ctx}/cms/site/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label class="control-label">名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="50"
				class="form-control w200" />
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" class="form-control w200" />
		</div>
		<div class="form-group">
			<label class="control-label">状态：</label>
			<c:forEach items="${fns:getDictList('del_flag')}" var="role">
				<label class="checkbox-inline"> <input type="radio"
					name="delFlag" value="${role.value }"
					<c:if test="${ site.delFlag==role.value}"> 
						               checked="checked"
						            </c:if>
					onclick="$('#searchForm').submit();" />${role.label}
				</label>
			</c:forEach>
		</div>


	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>标题</th>
				<th>描述</th>
				<th>关键字</th>
				<th>主题</th>
				<shiro:hasPermission name="cms:site:edit">
					<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="site">
				<tr>
					<td><a href="${ctx}/cms/site/form?id=${site.id}"
						title="${site.name}">${fns:abbr(site.name,40)}</a></td>
					<td>${fns:abbr(site.title,40)}</td>
					<td>${fns:abbr(site.description,40)}</td>
					<td>${fns:abbr(site.keywords,40)}</td>
					<td>${site.theme}</td>
					<shiro:hasPermission name="cms:site:edit">
						<td><a href="${ctx}/cms/site/form?id=${site.id}" title="修改">修改</a>
							<a
							href="${ctx}/cms/site/delete?id=${site.id}${site.delFlag ne 0?'&isRe=true':''}"
							onclick="return confirmx('确认要${site.delFlag ne 0?'恢复':''}删除该站点吗？', this.href)"
							title="${site.delFlag ne 0?'恢复':''}删除">删除</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
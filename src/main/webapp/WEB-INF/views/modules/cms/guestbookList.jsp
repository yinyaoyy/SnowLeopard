<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>留言管理</title>
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
		<li class="active"><a href="${ctx}/cms/guestbook/">留言列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="guestbook"
		action="${ctx}/cms/guestbook/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label class="control-label">标题 ：</label>
			<form:input path="title" htmlEscape="false" maxlength="50"
				class="form-control w200" />
			<label class="control-label">统计起期：</label>
			<form:input path="beginDate" htmlEscape="false" maxlength="20"
				class="form-control w200"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<label class="control-label">统计止期：</label>
			<form:input path="endDate" htmlEscape="false" maxlength="20"
				class="form-control w200"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label class="control-label">业务分类：</label>
			<form:select id="type" path="type" class="form-control w200">
				<form:option value=""
					label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getOneDictList('cms_guestbook_type')}"
					itemValue="value" itemLabel="label" htmlEscape="false" />
			</form:select>
		</div>

		<div class="form-group">
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />&nbsp;&nbsp;
		</div>
		<div class="form-group">
			<label class="control-label">状态：</label>
			<c:forEach items="${fns:getDictList('cms_del_flag')}" var="role">
				<label class="checkbox-inline"> <input type="radio"
					name="delFlag" value="${role.value }"
					<c:if test="${ guestbook.delFlag==role.value}"> 
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
				<th>业务分类</th>
				<th>问题分类</th>
				<th>留言标题</th>
				<th>留言人</th>
				<th>指定人处理</th>
				<th>留言时间</th>
				<th>是否有回复</th>
				<th>是否有追问</th>
				<shiro:hasPermission name="cms:guestbook:edit">
					<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="guestbook">
				<tr>
					<td>${guestbook.typeDesc}</td>
					<td>${guestbook.problemTypeDesc}</td>
					<td title="${fns:abbr(guestbook.title,40)}"><a href="${ctx}/cms/guestbook/form?id=${guestbook.id}">${fns:abbr(guestbook.title,40)}</a></td>
					<td>${guestbook.name}</td>
					<td>${guestbook.user.name}</td>
					<td><fmt:formatDate value="${guestbook.createDate}"
							pattern="yyyy-MM-dd" /></td>
					<td>${fns:getDictLabel(guestbook.isComment, 'cms_guestbook_type_inquiries', '')}</td>
					<td>${fns:getDictLabel(guestbook.isInquiries, 'cms_guestbook_type_inquiries', '')}</td>
					<shiro:hasPermission name="cms:guestbook:edit">
						<td><c:if test="${guestbook.delFlag eq '0'}">
								<a href="${ctx}/cms/guestbook/form?id=${guestbook.id}"
									title="回复">回复</a>
								<a
									href="${ctx}/cms/guestbook/delete?id=${guestbook.id}${guestbook.delFlag ne 0?'&isRe=true':''}"
									onclick="return confirmx('确认要${guestbook.delFlag ne 0?'恢复审核':'删除'}该留言吗？', this.href)"
									title="${guestbook.delFlag ne 0?'恢复审核':'删除'}">${guestbook.delFlag ne 0?'恢复审核':'删除'}</a>
							</c:if> <c:if test="${guestbook.delFlag eq '1'}">
								<a
									href="${ctx}/cms/guestbook/delete?id=${guestbook.id}${guestbook.delFlag ne 0?'&isRe=true':''}"
									onclick="return confirmx('确认要${guestbook.delFlag ne 0?'恢复审核':'删除'}该留言吗？', this.href)"
									title="${guestbook.delFlag ne 0?'恢复审核':'删除'}">${guestbook.delFlag ne 0?'恢复审核':'删除'}</a>
							</c:if> <c:if test="${guestbook.delFlag eq '2'}">
								<a href="${ctx}/cms/guestbook/form?id=${guestbook.id}"
									title="审核">审核</a>
							</c:if></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
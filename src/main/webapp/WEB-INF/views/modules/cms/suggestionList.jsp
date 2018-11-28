<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>留言管理</title>
	<meta name="decorator" content="default"/>
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
		<li class="active"><a href="${ctx}/cms/suggestion/">意见反馈列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="suggestion" action="${ctx}/cms/suggestion/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
				<label class="control-label">标题 ：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="form-control w200"/>
				<label class="control-label">统计起期：</label>
				<form:input path="beginDate" htmlEscape="false" maxlength="20" class="form-control w200"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				/>
				<label class="control-label">统计止期：</label>
				<form:input path="endDate" htmlEscape="false" maxlength="20" class="form-control w200"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				/>
			</div>
		<div class="form-group">
					&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
			</div>
		<div class="form-group">
				<label class="control-label">状态：</label>
				<c:forEach items="${fns:getDictList('cms_del_flag')}" var="role">
								<label class="checkbox-inline">
						    <input type="radio" name="delFlag" value="${role.value }" 
						            <c:if test="${ suggestion.delFlag==role.value}"> 
						               checked="checked"
						            </c:if> onclick="$('#searchForm').submit();"
						    />${role.label}
						    </label>
							</c:forEach>
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>意见反馈人</th><th>意见反馈标题</th><th>反馈时间</th><th>是否有回复</th><th>是否有追问</th><shiro:hasPermission name="cms:suggestion:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="suggestion">
			<tr>
				<td>${suggestion.name}</td>
				<td><a href="${ctx}/cms/suggestion/form?id=${suggestion.id}">${fns:abbr(suggestion.title,40)}</a></td>
				<td><fmt:formatDate value="${suggestion.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${fns:getDictLabel(suggestion.isComment, 'cms_guestbook_type_inquiries', '')}</td>
				<td>${fns:getDictLabel(suggestion.isInquiries, 'cms_guestbook_type_inquiries', '')}</td>
				<shiro:hasPermission name="cms:suggestion:edit"><td>
					<c:if test="${suggestion.delFlag eq '0'}">
					<a href="${ctx}/cms/suggestion/form?id=${suggestion.id}" title="评论"><i class="fa fa-fw fa-file-text-o"></i></a>
					<a href="${ctx}/cms/suggestion/delete?id=${suggestion.id}${suggestion.delFlag ne 0?'&isRe=true':''}" 
						onclick="return confirmx('确认要${suggestion.delFlag ne 0?'恢复审核':'删除'}该投诉建议吗？', this.href)" title="${suggestion.delFlag ne 0?'恢复审核':'删除'}">${suggestion.delFlag ne 0?'<i class="fa fa-fw fa-hand-o-left"></i>':'<i class="fa fa-fw fa-trash"></i>'}</a>
						</c:if>
					<c:if test="${suggestion.delFlag eq '1'}">
					<a href="${ctx}/cms/suggestion/delete?id=${suggestion.id}${suggestion.delFlag ne 0?'&isRe=true':''}" 
						onclick="return confirmx('确认要${suggestion.delFlag ne 0?'恢复审核':'删除'}该投诉建议吗？', this.href)" title="${suggestion.delFlag ne 0?'恢复审核':'删除'}">${suggestion.delFlag ne 0?'<i class="fa fa-fw fa-hand-o-left"></i>':'<i class="fa fa-fw fa-trash"></i>'}</a>
						</c:if>
					<c:if test="${suggestion.delFlag eq '2'}"><a href="${ctx}/cms/suggestion/form?id=${suggestion.id}" title="审核"><i class="fa fa-check"></i></a></c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
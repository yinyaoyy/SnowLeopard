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
		<li class="active"><a href="${ctx}/cms/complaint/">意见投诉列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="complaint"
		action="${ctx}/cms/complaint/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label class="control-label" style="width: 100px;">标题 ：</label>
			<form:input path="title" htmlEscape="false" maxlength="50"
				class="form-control w200" />
			<label class="control-label" style="width: 100px;">统计起期：</label> <input
				path="beginDate" htmlEscape="false" maxlength="20"
				class="form-control w200"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			<label class="control-label" style="width: 100px;">统计止期：</label>
			<form:input path="endDate" htmlEscape="false" maxlength="20"
				class="form-control w200"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
		</div>
		<div class="form-group">
			<label class="control-label" style="width: 100px;">意见投诉事项：</label>
			<form:select id="suchComplaints" path="suchComplaints"
				class="form-control w200">
				<form:option value=""
					label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options
					items="${fns:getOneDictList('cms_complaint_shixiang')}"
					itemValue="value" itemLabel="label" htmlEscape="false" />
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label" style="margin-left: -5px; width: 100px;">工作人员分类：</label>
			<form:select id="classWorker" path="classWorker"
				class="form-control w200">
				<form:option value=""
					label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getOneDictList('cms_complaint_worker')}"
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
					<c:if test="${ complaint.delFlag==role.value}"> 
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
				<th>意见投诉事项</th>
				<th>工作人员类别
				<th>意见投诉人</th>
				<th>意见投诉标题</th>
				<th>意见投诉时间</th>
				<th>是否有回复</th>
				<th>是否有追问</th>
				<shiro:hasPermission name="cms:complaint:edit">
					<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="complaint">
				<tr>
					<td>${fns:getDictLabel(complaint.suchComplaints, 'cms_complaint_shixiang', '')}</td>
					<td>${fns:getDictLabel(complaint.classWorker, 'cms_complaint_worker', '')}</td>
					<td>${complaint.name}</td>
					<td title="${fns:abbr(complaint.title,40)}"><a href="${ctx}/cms/complaint/form?id=${complaint.id}">${fns:abbr(complaint.title,40)}</a></td>
					<td><fmt:formatDate value="${complaint.createDate}"
							pattern="yyyy-MM-dd" /></td>
					<td>${fns:getDictLabel(complaint.isComment, 'cms_guestbook_type_inquiries', '')}</td>
					<td>${fns:getDictLabel(complaint.isInquiries, 'cms_guestbook_type_inquiries', '')}</td>
					<shiro:hasPermission name="cms:complaint:edit">
						<td><c:if test="${complaint.delFlag eq '0'}">
								<a href="${ctx}/cms/complaint/form?id=${complaint.id}"
									title="回复">回复</a>
								<a
									href="${ctx}/cms/complaint/delete?id=${complaint.id}${complaint.delFlag ne 0?'&isRe=true':''}"
									onclick="return confirmx('确认要${complaint.delFlag ne 0?'恢复审核':'删除'}该投诉建议吗？', this.href)"
									title="${complaint.delFlag ne 0?'恢复审核':'删除'}">${complaint.delFlag ne 0?'恢复审核':'删除'}</a>
							</c:if> <c:if test="${complaint.delFlag eq '1'}">
								<a
									href="${ctx}/cms/complaint/delete?id=${complaint.id}${complaint.delFlag ne 0?'&isRe=true':''}"
									onclick="return confirmx('确认要${complaint.delFlag ne 0?'恢复审核':'删除'}该投诉建议吗？', this.href)"
									title="${complaint.delFlag ne 0?'恢复审核':'删除'}">${complaint.delFlag ne 0?'恢复审核':'删除'}</a>
							</c:if> <c:if test="${complaint.delFlag eq '2'}">
								<a href="${ctx}/cms/complaint/form?id=${complaint.id}"
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
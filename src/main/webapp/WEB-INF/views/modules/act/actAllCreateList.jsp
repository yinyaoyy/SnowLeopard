<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>待办任务</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	/**
	 * 签收任务
	 */
	function page(n, s) {
		location = '${ctx}/act/task/allCreater?pageNo=' + n + '&pageSize=' + s;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/task/allCreater">我的申请</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="act"
		action="${ctx}/act/task/allCreater" method="get" class="form-inline">
		<div class="form-group">
			<label class="control-label">流程名称：</label>
			<form:select path="procDefKey"
				cssClass="form-control form-control w200">
				<form:option value=""
					label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getDictList('act_type')}"
					itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label" for="birthday">创建时间：</label> <input
				type="text" readonly="readonly" maxlength="20"
				class="form-control  w200" name="beginDate" id="beginDate"
				readonly="readonly"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value='${act.beginDate}' pattern="yyyy-MM-dd"/>"
				style="background-color: white; cursor: auto;"> -- <input
				type="text" readonly="readonly" maxlength="20"
				class="form-control  w200" name="endDate" id="endDate"
				readonly="readonly"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				value="<fmt:formatDate value='${act.endDate}' pattern="yyyy-MM-dd"/>"
				style="background-color: white; cursor: auto;">
		</div>
		<div class="form-group">
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th width="150">申请时间</th>
				<th width="150">结束时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="act">
				<c:set var="hisProcIns" value="${act.hisProcIns}" />
				<c:set var="status" value="${act.status}" />
				<tr>
					<td title="${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</td>
					<td><fmt:formatDate value="${act.hisProcIns.startTime}" type="both" /></td>
					<td><fmt:formatDate value="${act.hisProcIns.endTime}" type="both" /></td>
					<td><a href="${ctx}/act/task/form?taskId=${hisProcIns.id}&taskName=${fns:urlEncode(hisProcIns.name)}&procInsId=${hisProcIns.processInstanceId}&procDefId=${hisProcIns.processDefinitionId}&status=view&zt=2" title="详情">详情</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

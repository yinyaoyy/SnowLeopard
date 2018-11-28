<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>综合查询</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
	    	location = '${ctx}/act/task/archives?pageNo='+n+'&pageSize='+s;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/task/archives">综合查询</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="act" action="${ctx}/act/task/archives/" method="get" class="form-inline">
		<div class="form-group">
			<label class="control-label">流程名称：</label>
			<form:select path="procDefKey" cssClass="form-control form-control w200">
				<form:option value="" label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getDictList('act_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label">流程进度：</label>
			<form:select path="status" cssClass="form-control form-control w200">
				<form:option value="" label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getDictList('oa_act_progress')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
						<label class="control-label" for="birthday">创建时间：</label>
						<input type="text"  readonly="readonly" maxlength="20" class="form-control  w200" name="beginDate" id="beginDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${act.beginDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;"> --
						<input type="text"  readonly="readonly" maxlength="20" class="form-control  w200" name="endDate" id="endDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${act.endDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;">
		</div>
		<div class="form-group">
			<label class="control-label">案件标题：</label>
			<form:input path="title"  htmlEscape="false" maxlength="64" class="form-control form-control w200"/>
		</div>
		<div class="form-group">
			<label class="control-label" style="margin-left:10px;">承办人：</label>
			<form:input path="assigneeName"  htmlEscape="false" maxlength="64" class="form-control form-control w200"/>
		</div>
		<div class="form-group">
		&nbsp;	<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form><table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="550">标题</th>
				<th>承办人</th>
				<th>当前环节</th>
				<th width="150">创建时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="act">
				<c:set var="title" value="${act.title}" />
				<c:set var="status" value="${act.status}" />
				<c:set var="beginDate" value="${act.beginDate}" />
				<c:set var="procInsId" value="${act.procInsId}" />
				<c:set var="id" value="${act.taskId}" />
				<c:set var="assigneeName" value="${act.assigneeName}" />
				<tr>
					<td title="${title}">${title}</td>
					<td>${assigneeName}</td>
					<td>${status}</td>
					<td><fmt:formatDate value="${beginDate}" type="both"/></td>
					<td>
						<c:choose> 
							<c:when test="${act.procDefKey eq 'legal' }">
								<a href="${ctx}/oa/act/oaLegalAid/form?procInsId=${procInsId}&status=view&id=${id}&zt=3" title="详情" >详情</a>
							</c:when>
							<c:when test="${act.procDefKey eq 'mediation' }">
								<a href="${ctx}/oa/act/oaPeopleMediationAcceptRegister/form?id=${id}&status=view&zt=3" title="详情" >详情</a>
							</c:when>
				   		 </c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

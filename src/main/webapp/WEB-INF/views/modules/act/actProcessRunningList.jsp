<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>运行中的流程</title>
<meta name="decorator" content="default" />
 <script type="text/javascript">
	$(document).ready(function() {
		top.$.jBox.tip.mess = null;
	});
	function page(n, s) {
		location = '${ctx}/act/process/running/?pageNo=' + n + '&pageSize=' + s;
	}
/* 	function updateCategory(id, category) {
		$.jBox($("#categoryBox").html(), {
			title : "设置分类",
			buttons : {
				"关闭" : true
			},
			submit : function() {
			}
		});
		$("#categoryBoxId").val(id);
		$("#categoryBoxCategory").val(category);
	} */
</script> 
<%-- <script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/process/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search" onsubmit="loading('正在设置，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="procDefId" value="" />
			<select id="categoryBoxCategory" name="category">
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
		</form>
	</script>--%>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/process/">流程管理</a></li>
		<li><a href="${ctx}/act/process/deploy/">部署流程</a></li>
		<li class="active"><a href="${ctx}/act/process/running/">运行中的流程</a></li>
	</ul>
<form:form id="searchForm" modelAttribute="act" action="${ctx}/act/process/running/" method="get" class="form-inline">
			<div class="form-group">
			<label class="control-label">流程名称：</label>
			<form:select path="procDefKey" cssClass="form-control form-control w200">
				<form:option value="" label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getDictList('act_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
						<label class="control-label" for="birthday">创建时间：</label>
						<input type="text"  readonly="readonly" maxlength="20" class="form-control  w200" name="beginDate" id="beginDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${act.beginDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;"> --
						<input type="text"  readonly="readonly" maxlength="20" class="form-control  w200" name="endDate" id="endDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${act.endDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;">
		</div>
		<div class="form-group">
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>当前环节</th>
				<th width="150">创建时间</th>
				<th width="120">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="act">
				<c:set var="task" value="${act.task}" />
				<c:set var="vars" value="${act.vars}" />
				<c:set var="procDef" value="${act.procDef}" />
				<c:set var="status" value="${act.status}" />
				<tr>
					<td>
						${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}
					</td>
					<td>
						${task.name}
					</td>
					<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
					<td>
					    <shiro:hasPermission name="act:process:edit">
							<a
								href="${ctx}/act/process/deleteProcIns?procInsId=${task.processInstanceId}&reason="
								onclick="return promptx('删除流程','删除原因',this.href);" title="删除流程">删除流程</a>
						</shiro:hasPermission>&nbsp;
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<%-- 	<form id="searchForm" action="${ctx}/act/process/running/"
		method="post" class="form-inline">
		<div class="form-group">
			<label class="control-label">流程实例ID：</label> <input type="text" id="procInsId"
				name="procInsId" value="${procInsId}" class="form-control w200" />
		</div>
		<div class="form-group">
			<label class="control-label">流程定义Key：</label> <input type="text" id="procDefKey"
				name="procDefKey" value="${procDefKey}" class="form-control w200" />
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
		</div>
	</form>
	<sys:message content="${message}" />
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>执行ID</th>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>当前环节</th>
				<th>是否挂起</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="procIns">
				<tr>
					<td>${procIns.id}</td>
					<td>${procIns.processInstanceId}</td>
					<td>${procIns.processDefinitionId}</td>
					<td>${procIns.activityId}</td>
					<td>${procIns.suspended}</td>
					<td><shiro:hasPermission name="act:process:edit">
							<a
								href="${ctx}/act/process/deleteProcIns?procInsId=${procIns.processInstanceId}&reason="
								onclick="return promptx('删除流程','删除原因',this.href);" title="删除流程">删除流程</a>
						</shiro:hasPermission>&nbsp;</td>
				</tr>
			</c:forEach>
		</tbody>
	</table> --%>
	<div class="pagination">${page}</div>
</body>
</html>

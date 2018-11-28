<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待办任务</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		/**
		 * 签收任务
		 */
		function claim(taskId) {
			$.get('${ctx}/act/task/claim' ,{taskId: taskId}, function(data) {
				if (data == 'true'){
		        	top.$.jBox.tip('签收完成');
		            location = '${ctx}/act/task/todo/';
				}else{
		        	top.$.jBox.tip('签收失败');
				}
		    });
		}
		function page(n,s){
        	location = '${ctx}/act/task/all?pageNo='+n+'&pageSize='+s;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li ><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li class="active"><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="act" action="${ctx}/act/task/all/" method="get" class="form-inline">
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
				<th>申请人</th>
				<th width="200">当前环节</th>
				<th width="150">创建时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="act">
				<c:set var="task" value="${act.task}" />
				<c:set var="histTask" value="${act.histTask}" />
				<c:set var="vars" value="${act.vars}" />
				<c:set var="procDef" value="${act.procDef}" />
				<c:set var="status" value="${act.status}" />
				<tr>
				<c:choose> 
				       <c:when test="${status== 'finish'}">
				       	 <td title="${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : histTask.id, 60)}">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : histTask.id, 60)}</td>
					     <td>${act.applyUser.name}</td>
					     <td>${histTask.name}</td>
					     <td><fmt:formatDate value="${histTask.createTime}" type="both"/></td>
				         <td><a href="${ctx}/act/task/form?taskId=${histTask.id}&taskName=${fns:urlEncode(histTask.name)}&taskDefKey=${histTask.taskDefinitionKey}&procInsId=${histTask.processInstanceId}&procDefId=${histTask.processDefinitionId}&status=view" title="详情">详情</a></td>
				       </c:when>
				 		<c:otherwise>
				       	<td title="${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</td>
					    <td>${act.applyUser.name}</td>
					    <td>${task.name}</td>
					    <td><fmt:formatDate value="${task.createTime}" type="both"/></td>
				        <td><a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}" title="任务办理">任务办理</a></td>
				       </c:otherwise>
				</c:choose>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>第三方系统对接配置管理</title>
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
		<li class="active"><a href="${ctx}/tripartite/tripartiteSystemConfig/">第三方系统对接配置列表</a></li>
		<shiro:hasPermission name="tripartite:tripartiteSystemConfig:edit"><li><a href="${ctx}/tripartite/tripartiteSystemConfig/form">第三方系统对接配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tripartiteSystemConfig" action="${ctx}/tripartite/tripartiteSystemConfig/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="control-label">类型：</label>
			<form:select path="type" class="form-control" style="width:100px">
				<form:option value="" label="全部"/>
				<form:options items="${fns:getDictList('tripartite_config_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label">系统/接口地址：</label>
			<form:input path="value" htmlEscape="false" maxlength="300" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label">中文说明：</label>
			<form:input path="description" htmlEscape="false" maxlength="300" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label">暂停执行：</label>
			<form:select path="isPause" class="form-control" style="width:100px">
				<form:option value="" label="全部"/>
				<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型</th>
				<th width="100">系统/接口地址</th>
				<th width="100">中文说明</th>
				<th>请求<br>方式</th>
				<th>处理类名</th>
				<th>请求频率<br>(默认1天)</th>
				<th>是否<br>暂停</th>
				<th width="150">上次执行时间</th>
				<th>前置任务</th>
				<th>备注信息</th>
				<shiro:hasPermission name="tripartite:tripartiteSystemConfig:edit">
				<th width="270">操作处理</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tripartiteSystemConfig">
			<tr>
				<td>
					${fns:getDictLabel(tripartiteSystemConfig.type, 'tripartite_config_type', '')}
				</td>
				<td>
					${(tripartiteSystemConfig.type==1)?tripartiteSystemConfig.value:tripartiteSystemConfig.parent.value}
					<br>${(tripartiteSystemConfig.type==1)?'':tripartiteSystemConfig.value}
				</td>
				<td><a href="${ctx}/tripartite/tripartiteSystemConfig/form?id=${tripartiteSystemConfig.id}">
					${(tripartiteSystemConfig.type==1)?tripartiteSystemConfig.description:tripartiteSystemConfig.parent.description}
					<br>${(tripartiteSystemConfig.type==1)?'':tripartiteSystemConfig.description}
				</a></td>
				<td>
					${tripartiteSystemConfig.method}
				</td>
				<td>
					${tripartiteSystemConfig.serviceName}
				</td>
				<td>
					${tripartiteSystemConfig.requestRate}
				</td>
				<td>
					${fns:getDictLabel(tripartiteSystemConfig.isPause, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${tripartiteSystemConfig.lastRequestDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${(tripartiteSystemConfig.beforeTask==null or tripartiteSystemConfig.beforeTask.description=='')?'':tripartiteSystemConfig.beforeTask.description }
				</td>
				<td>
					${tripartiteSystemConfig.remarks}
				</td>
				<td>
				<shiro:hasPermission name="tripartite:tripartiteSystemConfig:edit">
    				<a href="${ctx}/tripartite/tripartiteSystemConfig/form?id=${tripartiteSystemConfig.id}">修改</a>
    				<a href="${ctx}/tripartite/tripartiteParamConfig/list?parentId=${tripartiteSystemConfig.id}">参数</a>
					<a href="${ctx}/tripartite/tripartiteSystemConfig/delete?id=${tripartiteSystemConfig.id}" onclick="return confirmx('确认要删除该第三方系统对接配置吗？', this.href)">删除</a>
				</shiro:hasPermission>
				<c:if test="${tripartiteSystemConfig.type==2 }">
				<shiro:hasPermission name="tripartite:scheduling:run">
					<a href="${ctx}/tripartite/scheduling/runTask?systemId=${tripartiteSystemConfig.parent.id}&taskId=${tripartiteSystemConfig.id}" onclick="return confirmx('确认要执行“${tripartiteSystemConfig.description}”吗？', this.href)">执行</a>
				</shiro:hasPermission>
				</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
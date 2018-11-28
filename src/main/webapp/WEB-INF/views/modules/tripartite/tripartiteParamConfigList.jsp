<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>与第三方系统对接请求头、参数配置表管理</title>
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
		<li class="active"><a href="${ctx}/tripartite/tripartiteParamConfig/">与第三方系统对接请求头、参数配置表列表</a></li>
		<shiro:hasPermission name="tripartite:tripartiteParamConfig:edit"><li><a href="${ctx}/tripartite/tripartiteParamConfig/form">与第三方系统对接请求头、参数配置表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tripartiteParamConfig" action="${ctx}/tripartite/tripartiteParamConfig/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>请求头或参数名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li><label>中文说明：</label>
				<form:input path="description" htmlEscape="false" maxlength="300" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>类型，字典为tripartite_config_type</th>
				<th>上级id(系统或接口id)</th>
				<th>请求头或参数名称</th>
				<th>中文说明</th>
				<th>解析默认值的方式tripartite_config_value_type</th>
				<th>参数默认值</th>
				<th>更新时间</th>
				<th>备注信息</th>
				<shiro:hasPermission name="tripartite:tripartiteParamConfig:edit">
				<th >操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tripartiteParamConfig">
			<tr>
				<td><a href="${ctx}/tripartite/tripartiteParamConfig/form?id=${tripartiteParamConfig.id}">
					${fns:getDictLabel(tripartiteParamConfig.type, 'tripartite_config_type', '')}
				</a></td>
				<td>
					${tripartiteParamConfig.parent.id}
				</td>
				<td>
					${tripartiteParamConfig.name}
				</td>
				<td>
					${tripartiteParamConfig.description}
				</td>
				<td>
					${fns:getDictLabel(tripartiteParamConfig.valueType, 'tripartite_config_value_type', '')}
				</td>
				<td>
					${tripartiteParamConfig.defaultValue}
				</td>
				<td>
					<fmt:formatDate value="${tripartiteParamConfig.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${tripartiteParamConfig.remarks}
				</td>
				<shiro:hasPermission name="tripartite:tripartiteParamConfig:edit"><td>
    				<a href="${ctx}/tripartite/tripartiteParamConfig/form?id=${tripartiteParamConfig.id}">修改</a>
					<a href="${ctx}/tripartite/tripartiteParamConfig/delete?id=${tripartiteParamConfig.id}" onclick="return confirmx('确认要删除该与第三方系统对接请求头、参数配置表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
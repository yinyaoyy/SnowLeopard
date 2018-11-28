<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>三定方案管理</title>
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
		<li class="active"><a href="${ctx}/oa/oaAgreement/">三定方案列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="oaAgreement" action="${ctx}/oa/oaAgreement/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<div class="form-group">
				<label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="200" class="form-control"/>
			</div>
			<div class="form-group">
				<label>所属地区</label>
				<form:select path="area.id" class="form-control w200">
					<option value="">全部</option>
					<form:options items="${areaList }" itemValue="id" itemLabel="name"/>
				</form:select>
			</div>
			<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<!-- <th>类型</th> -->
				<th>标题</th>
				<th width="150">所属机构</th>
				<th width="150">所属地区</th>
				<th width="150">状态</th>
				<shiro:hasPermission name="oa:oaAgreement:edit">
				<th width="100">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaAgreement">
			<tr>
				<%-- <td><a href="${ctx}/oa/oaAgreement/form?id=${oaAgreement.id}">
					${fns:getDictLabel(oaAgreement.type, 'oa_agreement_type', '')==""? '无' : fns:getDictLabel(oaAgreement.type, 'oa_agreement_type', '')}
				</a></td> --%>
				<td>
					${oaAgreement.title}
				</td>
				<td>
					${oaAgreement.office.name}
				</td>
				<td>
					${oaAgreement.area.name}
				</td>
				<td>
					${oaAgreement.actname}
				</td>
				<shiro:hasPermission name="oa:oaAgreement:edit"><td>
    				<a href="${ctx}/oa/oaAgreement/form?id=${oaAgreement.id}&act.procInsId=${oaAgreement.procInsId}&act.status=view">详情</a>
    				<!-- 
					<a href="${ctx}/oa/oaAgreement/delete?id=${oaAgreement.id}" onclick="return confirmx('确认要删除该三定方案吗？', this.href)">删除</a>
					 -->
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
]<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>角色管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function updateSort() {
		loading('正在提交，请稍等...');
		$("#listForm").attr("action", "${ctx}/sys/role/updateSort");
		$("#listForm").submit();
	}
</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/role/">角色列表</a></li>
		<shiro:hasPermission name="sys:role:edit">
			<li><a href="${ctx}/sys/role/form">角色添加</a></li>
		</shiro:hasPermission>
	</ul>
	<sys:message content="${message}" />
	<form id="listForm" method="post">
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>角色名称</th>
					<th>英文名称</th>
					<th>归属机构</th>
					<th>数据范围</th>
					<th width="100">排序</th>
					<shiro:hasPermission name="sys:role:edit">
						<th width="255">操作</th>
					</shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="role">
					<tr>
						<td><a href="form?id=${role.id}">${role.name}</a></td>
						<td><a href="form?id=${role.id}">${role.enname}</a></td>
						<td>${role.office.name}</td>
						<td>${fns:getDictLabel(role.dataScope, 'sys_data_scope', '无')}</td>
						<td style="text-align: center;"><shiro:hasPermission
								name="sys:role:edit">
								<input type="hidden" name="ids" value="${role.id}" />
								<input name="sorts" type="text" value="${role.sort}"
									style="width: 50px; margin: 0; padding: 0; text-align: center;">
							</shiro:hasPermission>
							<shiro:lacksPermission name="sys:role:edit">
						${role.sort}
					</shiro:lacksPermission></td>
						<shiro:hasPermission name="sys:role:edit">
							<td><a href="${ctx}/sys/role/assign?id=${role.id}"
								title="分配">分配</a> <c:if
									test="${(role.sysData eq fns:getDictValue('', 'yes_no','是', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('', 'yes_no','是', '1'))}">
									<a href="${ctx}/sys/role/form?id=${role.id}" title="修改">修改</a>
								</c:if> <a href="${ctx}/sys/role/delete?id=${role.id}"
								onclick="return confirmx('确认要删除该角色吗？', this.href)" title="删除">删除</a>
							</td>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<shiro:hasPermission name="sys:role:edit">
			<div class="form-actions pagination-left">
				<input id="btnSubmit" class="btn btn-primary" type="button"
					value="保存排序" onclick="updateSort();" />
			</div>
		</shiro:hasPermission>
	</form>
</body>
</html>
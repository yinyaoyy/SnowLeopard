<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>服务管理管理</title>
    <meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
			$("#treeTable").treeTable({expandLevel : 3}).show();
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/sys/serverSourceManage/">服务管理列表</a></li>
    <shiro:hasPermission name="sys:serverSourceManage:edit">
        <li><a href="${ctx}/sys/serverSourceManage/form">服务管理添加</a></li>
    </shiro:hasPermission>
</ul>
<sys:message content="${message}"/>
<form id="listForm" method="post">
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>名称</th>
			<th width="100">服务类别</th>
			<th width="100">首页显示</th>
			<th width="100">pc端显示</th>
			<th width="100">移动端显示</th>
			<th width="120">移动端首页显示</th>
			<th width="100">大屏显示</th>
			<th width="100">云平台显示</th>
			<shiro:hasPermission name="sys:serverSourceManage:edit">
				<th width="250">操作</th>
			</shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="server">
			<tr id="${server.id}" pId="${server.pid ne ''?server.pid:'0'}">
				<td nowrap style="text-align:left;"><a href="${ctx}/sys/serverSourceManage/form?id=${server.id}">${server.name}</a></td>
				<td>${fns:getDictLabel(server.serverType, 'sys_server_type', '')}</td>
				<td>${fns:getDictLabel(server.homeShow, 'show_hide', '')}</td>
				<td>${fns:getDictLabel(server.pcShow, 'show_hide', '')}</td>
				<td>${fns:getDictLabel(server.mobileShow, 'show_hide', '')}</td>
				<td>${fns:getDictLabel(server.mobileHomeShow, 'show_hide', '')}</td>
				<td>${fns:getDictLabel(server.bigdataShow, 'show_hide', '')}</td>
				<td>${fns:getDictLabel(server.cloudShow, 'show_hide', '')}</td>
				<shiro:hasPermission name="sys:serverSourceManage:edit"><td nowrap>
					<a href="${ctx}/sys/serverSourceManage/form?id=${server.id}" title="修改">修改</a>
					<a href="${ctx}/sys/serverSourceManage/delete?id=${server.id}" onclick="return confirmx('要删除该服务及所有子服务项吗？', this.href)" title="删除">删除</a>
					<a href="${ctx}/sys/serverSourceManage/form?pid=${server.id}" style="width:80px;" title="添加下级菜单">添加下级菜单</a> 
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>应用管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/appmange/appMenu/">应用列表</a></li>
    <shiro:hasPermission name="appmange:appMenu:edit">
        <li><a href="${ctx}/appmange/appMenu/form">应用添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="appMenu" action="${ctx}/appmange/appMenu/" method="post" class="form-inline">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="form-group">
        <label>应用类型：</label>
        <form:select path="appType" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('app_menu_type')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>名称：</label>
        <form:input path="title" htmlEscape="false" maxlength="225" class="form-control w200"/>
    </div>
    <div class="form-group">
        <label>应用的请求链接：</label>
        <form:input path="href" htmlEscape="false" maxlength="225" class="form-control w200"/>
    </div>
    <%--<div class="form-group">--%>
    <%--<label>删除：</label>--%>
    <%--<form:radiobuttons path="delFlag" items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value"--%>
    <%--htmlEscape="false"/>--%>
    <%--</div>--%>

    <div class="form-group">
       <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
    </div>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>应用类型</th>
        <th>名称</th>
        <th>应用的请求链接</th>
        <th>显示</th>
        <shiro:hasPermission name="appmange:appMenu:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="appMenu">
        <tr>
            <td><a href="${ctx}/appmange/appMenu/form?id=${appMenu.id}">
                    ${fns:getDictLabel(appMenu.appType, 'app_menu_type', '')}
            </a></td>
            <td>
                    ${appMenu.title}
            </td>
            <td>
                    ${appMenu.href}
            </td>
            <td>
                    ${fns:getDictLabel(appMenu.isShow, 'show_hide', '')}
            </td>
            <shiro:hasPermission name="appmange:appMenu:edit">
                <td>
                    <a href="${ctx}/appmange/appMenu/form?id=${appMenu.id}">修改</a>
                    <a href="${ctx}/appmange/appMenu/delete?id=${appMenu.id}"
                       onclick="return confirmx('确认要删除该应用吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
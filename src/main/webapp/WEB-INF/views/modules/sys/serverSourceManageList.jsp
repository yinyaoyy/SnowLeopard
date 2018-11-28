<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>服务管理管理</title>
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
    <li class="active"><a href="${ctx}/sys/serverSourceManage/">服务管理列表</a></li>
    <shiro:hasPermission name="sys:serverSourceManage:edit">
        <li><a href="${ctx}/sys/serverSourceManage/form">服务管理添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="serverSourceManage" action="${ctx}/sys/serverSourceManage/" method="post"
           class="form-inline">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="form-group">
        <label>名称：</label>
        <form:input path="name" htmlEscape="false" maxlength="225" class="form-control"/>
    </div>
    <div class="form-group">
        <label>服务类别：</label>
        <form:select path="serverType" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('sys_server_type')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>法网首页显示：</label>
        <form:select path="homeShow" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>法网端显示：</label>
        <form:select path="pcShow" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>移动端显示：</label>
        <form:select path="mobileShow" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
        <div class="form-group">
        <label>移动端首页显示：</label>
        <form:select path="mobileHomeShow" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    
    <div class="form-group">
        <label>大屏显示：</label>
        <form:select path="bigdataShow" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>云平台显示：</label>
        <form:select path="cloudShow" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>层级：</label>
        <form:input path="leve" htmlEscape="false" maxlength="4" class="form-control"/>
    </div>
    <div class="form-group">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
    </div>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>名称</th>
        <th>服务类别</th>
        <th>首页显示</th>
        <th>pc端显示</th>
        <th>移动端显示</th>
        <th>移动端首页显示</th>
        <th>大屏显示</th>
        <th>云平台显示</th>
        <shiro:hasPermission name="sys:serverSourceManage:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="serverSourceManage">
        <tr>
            <td><a href="${ctx}/sys/serverSourceManage/form?id=${serverSourceManage.id}">
                    ${serverSourceManage.name}
            </a></td>
            <td>
                    ${fns:getDictLabel(serverSourceManage.serverType, 'sys_server_type', '')}
            </td>
            <td>
                    ${fns:getDictLabel(serverSourceManage.homeShow, 'show_hide', '')}
            </td>
            <td>
                    ${fns:getDictLabel(serverSourceManage.pcShow, 'show_hide', '')}
            </td>
            <td>
                    ${fns:getDictLabel(serverSourceManage.mobileShow, 'show_hide', '')}
            </td>
            <td>
                    ${fns:getDictLabel(serverSourceManage.mobileHomeShow, 'show_hide', '')}
            </td>
            <td>
                    ${fns:getDictLabel(serverSourceManage.bigdataShow, 'show_hide', '')}
            </td>
             <td>
                    ${fns:getDictLabel(serverSourceManage.cloudShow, 'show_hide', '')}
            </td>
            
            <shiro:hasPermission name="sys:serverSourceManage:edit">
                <td>
                    <a href="${ctx}/sys/serverSourceManage/form?id=${serverSourceManage.id}">修改</a>
                    <a href="${ctx}/sys/serverSourceManage/delete?id=${serverSourceManage.id}"
                       onclick="return confirmx('确认要删除该服务管理吗？', this.href)">删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
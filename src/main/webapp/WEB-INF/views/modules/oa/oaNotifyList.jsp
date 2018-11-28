<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消息管理</title>
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
	<style>
	#status1,#status2{margin-top: -2px;}
	label[for="status1"]{margin-right:10px;}
	.bt{
		overflow: hidden;
	    white-space: nowrap;
	    text-overflow: ellipsis;
    }
    .table{
    	table-layout: fixed;
    }
    table td:last a{
    	margin-left:5px;
    }
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/oaNotify/${oaNotify.self?'self':''}">消息列表</a></li>
		<c:if test="${!oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><li><a href="${ctx}/oa/oaNotify/form">消息添加</a></li></shiro:hasPermission></c:if>
	</ul>
	<form:form id="searchForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/${oaNotify.self?'self':''}" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="control-label">标题：</label>
				<form:input path="title" htmlEscape="false" class="form-control w200"/>
			</div>
			<div class="form-group">
				<label class="control-label">类型：</label>
			<form:select path="type" cssClass="form-control form-control w200">
				<option value="">${fns:getLanguageContent('sys_select_all')}</option>
				<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			</div>
			<c:if test="${!requestScope.oaNotify.self}">
			<div class="form-group">
				<label class="control-label">状态：</label>
			<form:select path="status" cssClass="form-control form-control w200">
				<option value="">${fns:getLanguageContent('sys_select_all')}</option>
				<form:options items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			</div>
			</c:if>
			<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="30%">标题</th>
				<th>类型</th>
				<th>状态</th>
				<th>查阅状态</th>
				<th width="13%">更新时间</th>
				<c:if test="${!oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><th width="15%">操作</th></shiro:hasPermission></c:if>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaNotify">
			<tr>
				<td class="bt" title="${oaNotify.title}"><a href="${ctx}/oa/oaNotify/${requestScope.oaNotify.self?'view':'form'}?id=${oaNotify.id}">${oaNotify.title}</a></td>
				<td>
					${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
				</td>
				<td>
					${fns:getDictLabel(oaNotify.status, 'oa_notify_status', '')}
				</td>
				<td>
					<c:if test="${requestScope.oaNotify.self}">
						${fns:getDictLabel(oaNotify.readFlag, 'oa_notify_read', '')}
					</c:if>
					<c:if test="${!requestScope.oaNotify.self}">
						${oaNotify.readNum} / ${oaNotify.readNum + oaNotify.unReadNum}
					</c:if>
				</td>
				<td>
					<fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<c:if test="${!requestScope.oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit">
				<td>
    				<c:choose>
    					<c:when test="${oaNotify.status==0}">
    						<a href="${ctx}/oa/oaNotify/form?id=${oaNotify.id}" title="修改">修改</a>
    					</c:when>
    					<c:otherwise>
    						<a title="修改">修改</a>
    					</c:otherwise>
    				</c:choose>
					<a href="${ctx}/oa/oaNotify/delete?id=${oaNotify.id}" title="删除" onclick="return confirmx('确认要删除该通知吗？', this.href)">删除</a>
				</td></shiro:hasPermission></c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
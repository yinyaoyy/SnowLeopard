<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>意见投诉管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function view(href){
			top.$.jBox.open('iframe:'+href,'查看文档',$(top.document).width()-220,$(top.document).height()-180,{
				buttons:{"关闭":true},
				loaded:function(h){
					//$(".jbox-content", top.document).css("overflow-y","hidden");
					//$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
				}
			});
			return false;
		}
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
		<li class="active"><a href="${ctx}/cms/comment/">意见投诉列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="comment" action="${ctx}/cms/comment/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id=contentId name="contentId" type="hidden" value="${contentId}"/>
	
		<div class="form-group">
				<label class="control-label">文档标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="50" class="form-control w200"/>
			</div>
		<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
			</div>
		<div class="form-group">
				<label class="control-label">状态：</label>
					<c:forEach items="${fns:getDictList('cms_del_flag')}" var="role">
								<label class="checkbox-inline">
						    <input type="radio" name="delFlag" value="${role.value }" 
						            <c:if test="${ comment.delFlag==role.value}"> 
						               checked="checked"
						            </c:if> onclick="$('#searchForm').submit();"
						    />${role.label}
						    </label>
							</c:forEach>
				<%-- <form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" /> --%>
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-bordered table-condensed">
		<thead><tr><th>评论内容</th><th>文档标题</th><th>评论人</th><th>评论IP</th><th>评论时间</th><th nowrap="nowrap">操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="comment">
			<tr>
				<td><a href="javascript:" onclick="$('#c_${comment.id}').toggle()">${fns:abbr(fns:replaceHtml(comment.content),40)}</a></td>
				<td><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${comment.category.id}-${comment.contentId}${fns:getUrlSuffix()}" title="${comment.title}" onclick="return view(this.href);">${fns:abbr(comment.title,40)}</a></td>
				<td>${comment.name}</td>
				<td>${comment.ip}</td>
				<td><fmt:formatDate value="${comment.createDate}" type="both"/></td>
				<td><shiro:hasPermission name="cms:comment:edit">
					<c:if test="${comment.delFlag ne '2'}"><a href="${ctx}/cms/comment/delete?id=${comment.id}${comment.delFlag ne 0?'&isRe=true':''}" 
						onclick="return confirmx('确认要${comment.delFlag ne 0?'恢复审核':'删除'}该审核吗？', this.href)" title="${comment.delFlag ne 0?'恢复审核':'删除'}">${comment.delFlag ne 0?'<i class="fa fa-fw fa-hand-o-left"></i>':'<i class="fa fa-fw fa-trash"></i>'}</a></c:if>
					<c:if test="${comment.delFlag eq '2'}"><a href="${ctx}/cms/comment/save?id=${comment.id}&contentId=${comment.contentId}" title="审核通过"><i class="fa fa-fw fa-check"></i></a></c:if></shiro:hasPermission>
				</td>
			</tr>
			<tr id="c_${comment.id}" style="background:#fdfdfd;display:none;"><td colspan="6">${fns:replaceHtml(comment.content)}</td></tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

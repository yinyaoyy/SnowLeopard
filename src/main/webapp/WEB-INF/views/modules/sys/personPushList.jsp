<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<script	type="text/javascript" src="${ctxStatic}/snowLeopard/js/common.js"></script>
<meta name="decorator" content="default" />
<script type="text/javascript">
    
	$(document).ready(function() {
		platForm.bindInsideAddTab("#chakan");
		 $(".task").bind("click",function(){
			 var ss = $(".task").attr("value");
			 $.ajax({
				    url:"${ctx}/sys/sysUserPush/changeStatus",    //请求的url地址
				    type:"GET",
				    data:{"id":ss},
				    success:function(data){
				    	location.reload();
				    },
				    error:function(){
				    	location.reload();
				    }
			 });
		}); 
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
	<form:form id="searchForm" modelAttribute="sysUserPush"
		action="${ctx}/sys/sysUserPush/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label>通知标题：</label>
			<form:input path="pushMessage.title" htmlEscape="false"
				maxlength="100" class="form-control w200" />
		</div>
		<div class="form-group">
			<label>通知内容：</label>
			<form:input path="pushMessage.msgContent" htmlEscape="false"
				maxlength="300" class="form-control w200" />
		</div>
		<%-- <div class="form-group">
			    <label >消息状态：</label>
			    <form:select path="isRead" htmlEscape="false" class="form-control w200">
				      <form:option value="全部"></form:option>
				      <form:option value="已读"/>
				      <form:option value="未读"/>
			    </form:select>
		</div> --%>
		<div class="form-group">
				<label class="control-label">类型：</label>
			<form:select path="isRead" cssClass="form-control form-control w200">
				<form:options items="${fns:getDictList('read_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<%-- <div class="form-group">
			    <label>业务类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="100" class="input-medium"/>
			</div> --%>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>通知标题</th>
				<th>通知内容</th>
				<th>消息状态</th>
				<th >发送时间</th>
				<th width="150">更新时间</th>
				<th width="170">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="sysUserPush">
				<tr>
					<td><a href="${ctx}/sys/sysUserPush/form?id=${sysUserPush.id}">
							${sysUserPush.pushMessage.title} </a></td>
					<td>${sysUserPush.pushMessage.msgContent}</td>
					<td>${sysUserPush.isRead=='0'?"未读":"已读"}</td>
					<td><fmt:formatDate value="${sysUserPush.sendTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${sysUserPush.updateDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>
						<%-- 	<a id = "chakan" href="${ctx}/sysUserPush/form?id=${sysUserPush.id}">查看</a> --%>
							<a class = "task"  id = "chakan" value="${sysUserPush.id}"   data-href="${ctx}${sysUserPush.url}" data-title="我的待办"><span class="title">查看</span><i class="fa-link-ext"></i></a>
							<a href="${ctx}/sys/sysUserPush/delete?id=${sysUserPush.id}"
							onclick="return confirmx('确认要删除该消息推送吗？', this.href)">删除</a>
						</td>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
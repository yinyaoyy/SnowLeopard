<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	table{table-layout: fixed;} .title{overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
	</style>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<form:form id="searchForm" action="${ctx}/sys/log/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		    <div class="form-group">
				<label class="control-label">操作：</label>
				<input id="title" name="title" type="text" maxlength="50" class="form-control" value="${log.title}"/>
			</div>
			<div class="form-group">
				<label class="control-label">用户ID：</label>
				<input id="createBy.id" name="createBy.id" type="text" maxlength="50" class="form-control" value="${log.createBy.id}"/>
			</div>
			<div class="form-group">
				<label class="control-label">日期范围：&nbsp;</label>
				<input type="text"  readonly="readonly" maxlength="20" class="form-control" name="beginDate" id="beginDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${log.beginDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;"> --
				<input type="text"  readonly="readonly" maxlength="20" class="form-control" name="endDate" id="endDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${log.endDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;">
			</div>
			<div class="form-group">
				<label class="control-label">URI：</label>
				<input id="requestUri" name="requestUri" type="text" maxlength="50" class="form-control" value="${log.requestUri}"/>
			</div>
			<div class="form-group">
				<label class="checkbox-inline"><input id="exception" style="margin-top:2px;" name="exception" type="checkbox"${log.exception eq '1'?' checked':''} value="1"/>只查询异常信息</label>
			</div>
			<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed ">
		<thead>
			<tr>
				<th width="120">操作</th>
				<th width="90">操作用户</th>
				<th width="125">业务所属</th>
				<th>URI</th>
				<th width="195">请求参数</th>
				<th width="110">操作者IP</th>
				<th width="150">操作时间</th>
				<th width="100">操作</th>
		</thead>
		<tbody>
			<%
				request.setAttribute("strEnter", "\n");
				request.setAttribute("strTab", "\t");
			%>
			<c:forEach items="${page.list}" var="log">
				<tr>
					<td class="title" title="${log.title}">${log.title}</td>
					<td>${log.createBy.name}</td>
					<td class="title" title="${log.createBy.office.name}">${log.createBy.office.name}</td>
					<td class="title" title="${log.requestUri}">
						<!-- <strong> -->${log.requestUri}<!-- </strong> -->
					</td>
					<td class="title" title="${log.params}">${log.params}</td>
					<td>${log.remoteAddr}</td>
					<td><fmt:formatDate value="${log.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><a href="${ctx}/sys/log/info?id=${log.id}" title="查看">查看</a></td>
				</tr>
				<c:if test="${not empty log.exception}">
					<tr>
						<td colspan="8"
							style="word-wrap: break-word; word-break: break-all;">异常信息:
							<br /> ${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>'), strTab, '&nbsp; &nbsp; ')}
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
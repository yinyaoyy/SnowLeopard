<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	  <ul class="nav nav-tabs">
	    <c:choose>
	      <c:when test="${zt==1 }">
	        <li><a href="${ctx}/act/task/all">全部任务</a></li>
			<li class="active"><a href="javascript:;}">查看请假单</a></li>
	      </c:when>
	      <c:when test="${zt==2 }">
	        <li><a href="${ctx}/act/task/allCreater">我的申请</a></li>
			<li class="active"><a href="javascript:;}">查看请假单</a></li>
	      </c:when>
	      <c:otherwise>
			<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
			<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
			<li><a href="${ctx}/act/task/process/">新建任务</a></li>
			<li class="active"><a href="javascript:;">查看请假单</a></li>
	      </c:otherwise>
	    </c:choose>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaLeave" action="${ctx}/oa/oaLeave/toDo" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey" class="taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">科室：</label>
			<form:input path="oaLeave.createBy.office.name" htmlEscape="false" maxlength="10" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职位：</label>
			<form:input path="oaLeave.createBy.userType" htmlEscape="false" maxlength="10" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
			<form:input path="oaLeave.createBy.name" htmlEscape="false" maxlength="10" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">开始时间：</label>
				<input name="startTime" id="startTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLeave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
			    <fmt:formatDate value="${oaLeave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">结束时间：</label>
				<input name="endTime" id="endTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLeave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
				<fmt:formatDate value="${ oaLeave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请假总时间：</label>
			        <form:input path="allTime" htmlEscape="false" readonly="true" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			       <label class="control-label">请假类型：</label>
			       <form:select path="leaveType" class="form-control valid" disabled="true">
						<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
						<form:options items="${fns:getDictList('oa_leave_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请假理由：</label>
			       <form:textarea path="reason" readonly="true" htmlEscape="false" rows="8" maxlength="255" class="form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">附件：</label>
					<form:hidden id="file" path="file" htmlEscape="false"
						maxlength="255" class="input-xlarge" />
							<sys:ckfinder input="file" type="files" uploadPath="/oa/leave"
								selectMultiple="true" readonly="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-actions">
					<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</div>
		</div>
			<act:histoicFlow procInsId="${oaLeave.act.procInsId}" />
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	function toVaild(){
		if($('#flag').val()=="no"&&$("#comment").val()==""){
			 toVail("审批意见不能为空","warning");
			 return false;
		}
	}
 </script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a href="${ctx}/oa/oaLeave/form?id=${oaLeave.id}"><shiro:hasPermission name="oa:oaLeave:edit">${not empty oaLeave.id?'办理':'申请'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaLeave:edit">查看</shiro:lacksPermission>请假单</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaLeave" action="${ctx}/oa/oaLeave/toDo" method="post" class="form-horizontal" onsubmit="return toVaild()">
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
				${oaLeave.createBy.office.name}
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职位：</label>
			     ${oaLeave.createBy.userType}
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
				${oaLeave.createBy.name}
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">开始时间：</label>
				<input name="startTime" id="startTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLeave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">结束时间：</label>
				<input name="endTime" id="endTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLeave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
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
				<div class="form-group">
			<label class="control-label">审批意见：</label>
				<form:input path="act.comment" id="comment" htmlEscape="false" maxlength="20" class="form-control "/>
				</div>
			</div>
		</div>		
		<div class="row">
		    <div class="col-xs-3">
			<shiro:hasPermission name="oa:oaLeave:edit">
			    <c:choose> 
			       <c:when test="${oaLeave.act.taskDefKey eq 'leave2' }">
			           <input id="btnSubmit" class="btn btn-primary" type="submit" value="同意" onclick="$('#flag').val('yes')"/>&nbsp;
					   <input id="btnSubmit2" class="btn btn-primary" type="submit" value="驳回" onclick="$('#flag').val('no')"/>&nbsp;
			       </c:when>
			       <c:when test="${oaLeave.act.taskDefKey eq 'leave4' }">
			       <input id="btnSubmit" class="btn btn-primary" type="submit" value="确定" onclick="$('#flag').val('yes')"/>&nbsp;
			       </c:when>
			       <c:when test="${oaLeave.act.taskDefKey eq 'leave5' }">
			       <input id="btnSubmit" class="btn btn-primary" type="submit" value="确定" onclick="$('#flag').val('yes')"/>&nbsp;
			       </c:when>
			    </c:choose>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		    </div>
		</div>
		<act:histoicFlow procInsId="${oaLeave.act.procInsId}" />
	</form:form>
</body>
</html>
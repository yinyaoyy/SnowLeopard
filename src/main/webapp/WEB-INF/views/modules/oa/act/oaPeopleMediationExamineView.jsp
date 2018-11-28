<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人民调解管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a href="javascript:;">查看人民调解调查记录</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaPeopleMediationExamine" action="javascript:;" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey" class="taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">调查时间</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<input name="examineDate"  id="examineDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaPeopleMediationExamine.examineDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					 />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">调查地点</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="examinePlace" disabled="true" htmlEscape="false" rows="8" class="form-control valid required"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">调查人</label>
				</div>
			</div>
			
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="inquirer" disabled="true"  htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">被调查人</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="respondent"  disabled="true" htmlEscape="false" rows="8" class="form-control valid required"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">参加人</label>
				</div>
			</div>
			
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="participants" disabled="true"  htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">记录人</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="recorder" htmlEscape="false" disabled="true"  rows="8" class="form-control valid required"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件标题</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.caseTitle"  disabled="true" htmlEscape="false" maxlength="600" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">记录内容</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:textarea path="recordContent"  disabled="true" htmlEscape="false" rows="8" class="form-control valid required"/>
				</div>
			</div>
		</div>
        <div class="row">
        <div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件相关文件</label>
				</div>
			</div>
				<div class="col-xs-11">
					<div class="control-group">
						<div class="controls">
							<form:hidden id="files" path="caseFile" htmlEscape="false"
								maxlength="255" class="input-xlarge" value="${oaPeopleMediationExamine.caseFile}"/>
							<sys:ckfinder input="files" type="files" uploadPath="/oa/act"
								selectMultiple="true" readonly="true" />
						</div>
					</div>
				</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">案件承办人</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">人民调解委员会</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:hidden path="oaPeopleMediationApply.peopleMediationCommittee.id"/>
				   <form:input path="oaPeopleMediationApply.peopleMediationCommittee.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">人民调解员</label>
				</div>
			</div>
			<div class="col-xs-5">
			      <div class="form-group">
				   <form:hidden path="oaPeopleMediationApply.mediator.id"/>
				   <form:input path="oaPeopleMediationApply.mediator.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	<act:histoicFlow procInsId="${oaPeopleMediationExamine.act.procInsId}" />
	</form:form>
</body>
</html>
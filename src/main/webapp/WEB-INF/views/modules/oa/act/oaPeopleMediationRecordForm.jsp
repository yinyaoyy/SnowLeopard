<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人民调解管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						//$("#name").focus();
						$("#inputForm").validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
						$("#btnSubmit")
								.on(
										"click",
										function() {
											$('#inputForm')
													.attr('action',
															'${ctx}/oa/act/oaPeopleMediationRecord/submit');
											$('#inputForm').submit();
										});
						$("#btnSave")
								.on(
										"click",
										function() {
											$('#inputForm')
													.attr('action',
															'${ctx}/oa/act/oaPeopleMediationRecord/save');
											$('#inputForm').submit();
										});
					});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a
			href="${ctx}/oa/act/oaPeopleMediationRecord/form?id=${oaPeopleMediationRecord.id}">人民调解记录表</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaPeopleMediationRecord"
		action="${ctx}/oa/act/oaPeopleMediationRecord/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" class="taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">调查时间</label>
				</div>
			</div>

			<div class="col-xs-5">
				<div class="form-group">
					<input name="mediateDate" id="mediateDate" type="text"
						readonly="readonly" maxlength="20" class="form-control "
						value="<fmt:formatDate value="${oaPeopleMediationRecord.mediateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">调查地点</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="mediatePlace" htmlEscape="false" rows="8"
						class="form-control valid required" />
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
					<form:input path="oaPeopleMediationExamine.inquirer"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">被调查人</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="oaPeopleMediationExamine.respondent"
						htmlEscape="false" rows="8" class="form-control valid required" />
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
					<form:input path="oaPeopleMediationExamine.participants"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">记录人</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="oaPeopleMediationExamine.recorder"
						htmlEscape="false" rows="8" class="form-control valid required" />
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
					<form:input path="oaPeopleMediationApply.caseTitle"
						htmlEscape="false" disabled="true" maxlength="600"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">调解记录</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="mediateRecord" htmlEscape="false" rows="8"
						class="form-control valid required" />
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
							maxlength="255" class="input-xlarge"
							value="${oaPeopleMediationRecord.caseFile}" />
						<sys:ckfinder input="files" type="files" uploadPath="/oa/act"
							selectMultiple="true" readonly="false" />
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
					<form:input
						path="oaPeopleMediationApply.peopleMediationCommittee.name"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">人民调解员</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.mediator.name"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:act:oaPeopleMediationRecord:edit">
				<input id="btnSave" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="提 交" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
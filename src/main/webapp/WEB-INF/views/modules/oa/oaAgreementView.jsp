<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>三定方案管理</title>
<meta name="decorator" content="default" />
</head>
<body>
	<ul class="nav nav-tabs">
		<c:choose>
			<c:when test="${zt!=3 }">
				<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
				<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
				<li><a href="${ctx}/act/task/all">全部任务</a></li>
				<li><a href="${ctx}/act/task/process/">新建任务</a></li>
				<li class="active"><a href="javascript:;">查看三定方案</a></li>
			</c:when>
			<c:otherwise>
				<li class="active"><a href="javascript:;">查看三定方案</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaAgreement"
		action="${ctx}/oa/oaAgreement/save" method="post"
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
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">类型：</label>
					<form:select path="type" class="input-xlarge form-control "
						disabled="true">
						<form:option value="" label="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_agreement_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">标题：</label>
					<form:input path="title" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">所属机构：</label>
					<form:input id="office" path="office.id"
						value="${oaAgreement.office.name}"
						labelValue="${oaAgreement.office.name}" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">所属地区：</label>
					<form:input id="area" path="area.id" value="${oaAgreement.area.id}"
						labelValue="${oaAgreement.area.name}" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-3">
				<div class="control-group">
					<label class="control-label">附件</label>
					<div class="controls">
						<form:hidden id="files" path="files" htmlEscape="false"
							maxlength="255" class="form-control" />
						<sys:ckfinder input="files" type="files" uploadPath="/oa/notify"
							selectMultiple="true" readonly="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
					<label class="control-label">内容：</label>
					<form:textarea path="content" htmlEscape="false" rows="8"
						maxlength="2000" class="form-control "
						disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
					<label class="control-label">备注：</label>
					<form:textarea path="remarks" htmlEscape="false" rows="8"
						maxlength="255" class=" form-control "
						disabled="true" />
				</div>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
		<act:histoicFlow procInsId="${oaAgreement.act.procInsId}" />
	</form:form>
</body>
</html>
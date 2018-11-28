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
															'${ctx}/oa/act/oaPeopleMediationAgreement/submit');
											$('#inputForm').submit();
										});
						$("#btnSave")
								.on(
										"click",
										function() {
											$('#inputForm')
													.attr('action',
															'${ctx}/oa/act/oaPeopleMediationAgreement/save');
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
			href="${ctx}/oa/act/oaPeopleMediationAgreement/form?id=${oaPeopleMediationAgreement.id}">填写人民调解协议书</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaPeopleMediationAgreement"
		action="${ctx}/oa/act/oaPeopleMediationAgreement/save" method="post"
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
		
			<div class="col-xs-2">
				<div class="form-group">
					<label class="control-label">协议书编号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="agreementCode" htmlEscape="false" maxlength="255"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">申请人基本情况</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserName"
						disabled="true" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control required" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="oaPeopleMediationApply.accuserSex"
						disabled="true" class="input-xlarge form-control required">
						<form:options items="${fns:getDictList('sex')}" itemLabel="label"
							itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">出生日期</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<input name="oaPeopleMediationApply.accuserBirthday" type="text"
						readonly="readonly" maxlength="20" class="form-control required"
						value="<fmt:formatDate value="${oaPeopleMediationAgreement.oaPeopleMediationApply.accuserBirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在旗县</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="oaPeopleMediationApply.accuserCounty"
						disabled="true" name="accuserCounty.id"
						value="${oaPeopleMediationAgreement.oaPeopleMediationApply.accuserCounty.id}"
						labelName="accuserCounty.name"
						labelValue="${oaPeopleMediationAgreement.oaPeopleMediationApply.accuserCounty.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " allowClear="false"
						notAllowSelectParent="false" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="oaPeopleMediationApply.accuserEthnic"
						disabled="true" class="input-xlarge form-control required">
						<form:options items="${fns:getDictList('ethnic')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">职业</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserOccupation"
						disabled="true" htmlEscape="false" maxlength="64"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在乡镇</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="oaPeopleMediationApply.accuserTown"
						disabled="true" name="accuserTown.id"
						value="${oaPeopleMediationAgreement.oaPeopleMediationApply.accuserTown.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationAgreement.oaPeopleMediationApply.accuserTown.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " allowClear="false"
						notAllowSelectParent="false" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserPostCode"
						disabled="true" htmlEscape="false" maxlength="10"
						class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserPhone"
						disabled="true" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserIdcard"
						disabled="true" htmlEscape="false" maxlength="64"
						class="input-xlarge form-control required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">户籍所在地</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserDomicile"
						disabled="true" htmlEscape="false" maxlength="64"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">住所地</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.accuserAddress"
						disabled="true" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">被申请人基本情况</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantName"
						disabled="true" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="oaPeopleMediationApply.defendantSex"
						disabled="true" class="input-xlarge form-control required">
						<form:options items="${fns:getDictList('sex')}" itemLabel="label"
							itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">出生日期</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<input name="oaPeopleMediationApply.defendantBirthday"
						disabled="true" type="text" readonly="readonly" maxlength="20"
						class="form-control "
						value="<fmt:formatDate value="${oaPeopleMediationAgreement.oaPeopleMediationApply.defendantBirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在旗县</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="oaPeopleMediationApply.defendantCounty"
						disabled="true" name="area.id"
						value="${oaPeopleMediationAgreement.oaPeopleMediationApply.defendantCounty.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationAgreement.oaPeopleMediationApply.defendantCounty.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " allowClear="false"
						notAllowSelectParent="false" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="oaPeopleMediationApply.defendantEthnic"
						disabled="true" class="input-xlarge form-control required">
						<form:options items="${fns:getDictList('ethnic')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">职业</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantOccupation"
						disabled="true" htmlEscape="false" maxlength="64"
						class="input-xlarge form-control " />
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在乡镇</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="oaPeopleMediationApply.defendantTown"
						disabled="true" name="area.id"
						value="${oaPeopleMediationAgreement.oaPeopleMediationApply.defendantTown.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationAgreement.oaPeopleMediationApply.defendantTown.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " allowClear="false"
						notAllowSelectParent="false" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantPostCode"
						disabled="true" htmlEscape="false" maxlength="10"
						class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantPhone"
						disabled="true" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantIdcard"
						disabled="true" htmlEscape="false" maxlength="64"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">户籍所在地</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantDomicile"
						disabled="true" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">住所地</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.defendantAddress"
						disabled="true" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">案件基本情况</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件标题</label>
				</div>
			</div>

			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="oaPeopleMediationApply.caseTitle" disabled="true"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案情及申请理由概述</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="oaPeopleMediationApply.caseSituation"
						disabled="true" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">纠纷事实</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="disputeFact" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">争议事项</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="disputeMatter" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">达成协议内容</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="agreementContent" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">履行方式</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="performMode" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">时限</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<input name="timeLimit" id="timeLimit" type="text"
						readonly="readonly" maxlength="20" class="form-control "
						value="<fmt:formatDate value="${oaPeopleMediationAgreement.timeLimit}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
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
							value="${oaPeopleMediationAgreement.caseFile}" />
						<sys:ckfinder input="files" type="files" uploadPath="/oa/act"
							selectMultiple="true" readonly="false" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">人民调解员</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:hidden path="oaPeopleMediationApply.mediator.id" />
					<form:input path="oaPeopleMediationApply.mediator.name"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">记录人</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<form:input path="recorder" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control "
						value="${oaPeopleMediationAgreement.oaPeopleMediationApply.mediator.name }" />
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:act:oaPeopleMediationAgreement:edit">
				<input id="btnSave" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="提 交" />&nbsp;
				</shiro:hasPermission>
			<input id="btnCancel" class="btn  btn-primary" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
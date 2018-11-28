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
															'${ctx}/oa/act/oaPeopleMediationAcceptRegister/submit');
											$('#inputForm').submit();
										});
						$("#btnSave")
								.on(
										"click",
										function() {
											$('#inputForm')
													.attr('action',
															'${ctx}/oa/act/oaPeopleMediationAcceptRegister/save');
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
			href="${ctx}/oa/act/oaPeopleMediationAcceptRegister/form?id=${oaPeopleMediationAcceptRegister.id}">填写人民调解受理登记表</a></li>
	</ul>
	<br />
	<form:form id="inputForm"
		modelAttribute="oaPeopleMediationAcceptRegister"
		action="${ctx}/oa/act/oaPeopleMediationAcceptRegister/save"
		method="post" class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" class="taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />

		<sys:message content="${message}" />
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
					<%-- <form:input path="oaPeopleMediationApply.defendantName" disabled="true" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/> --%>
					<form:input path="oaPeopleMediationApply.accuserName"
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
						value="<fmt:formatDate value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.accuserBirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
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
						value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.accuserCounty.id}"
						labelName="accuserCounty.name"
						labelValue="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.accuserCounty.name}"
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
						value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.accuserTown.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.accuserTown.name}"
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
						value="<fmt:formatDate value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.defendantBirthday}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
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
						value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.defendantCounty.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.defendantCounty.name}"
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
						value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.defendantTown.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.defendantTown.name}"
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
			<div class="col-xs-12 bg-info bg-info-title">案件性质</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件来源</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="caseSource"
						class="input-xlarge form-control required">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('mediate_case_Source')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及蒙语</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="hasMongol"
						class="input-xlarge form-control required">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('yes_no')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件严重等级</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="caseRank"
						class="input-xlarge form-control required">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('case_rank')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生时间</label>
				</div>
			</div>

			<div class="col-xs-3">
				<div class="form-group">
					<input name="caseTime" id="caseTime" type="text"
						readonly="readonly" maxlength="20" class="form-control "
						value="<fmt:formatDate value="${oaPeopleMediationAcceptRegister.caseTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及人员数量</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="caseInvolveCount" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">纠纷类型</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="oaPeopleMediationApply.caseType"
						class="input-xlarge form-control required" disabled="true">
						<form:options items="${fns:getDictList('oa_case_classify')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生旗县</label>
				</div>
			</div>

			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="oaPeopleMediationApply.caseCounty"
						name="caseCounty.id"
						value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.caseCounty.id}"
						labelName="caseCounty.name"
						labelValue="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.caseCounty.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " disabled="true"
						allowClear="false" notAllowSelectParent="false" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生乡镇</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="oaPeopleMediationApply.caseTown"
						name="caseTown.id"
						value="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.caseTown.id}"
						labelName="caseTown.name"
						labelValue="${oaPeopleMediationAcceptRegister.oaPeopleMediationApply.caseTown.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " disabled="true"
						allowClear="false" notAllowSelectParent="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生详细地址</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="caseArea" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " />
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
						htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">纠纷简要情况</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="disputeSituation" htmlEscape="false" rows="8"
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
							value="${oaPeopleMediationAcceptRegister.caseFile}" />
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
					<form:hidden
						path="oaPeopleMediationApply.peopleMediationCommittee.id" />
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
					<form:hidden path="oaPeopleMediationApply.mediator.id" />
					<form:input path="oaPeopleMediationApply.mediator.name"
						htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission
				name="oa:act:oaPeopleMediationAcceptRegister:edit">
				<input id="btnSave" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="提 交" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
		<act:histoicFlow
			procInsId="${oaPeopleMediationAcceptRegister.act.procInsId}" />
	</form:form>
</body>
</html>
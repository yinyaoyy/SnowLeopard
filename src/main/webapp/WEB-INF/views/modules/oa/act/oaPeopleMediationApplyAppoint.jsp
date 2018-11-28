<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人民调解管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm")
						.validate(
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
				$("#btnSubmit1,#btnSubmit2,#btnSubmit3")
				.on(
						"click",
						function() {
							$('#inputForm')
									.attr('action',
											'${ctx}/oa/act/oaPeopleMediationApply/toDo');
							$('#flag').val('yes');
							$('#inputForm').submit();
						});
		$("#btnSave1,#btnSave2,#btnSave3")
				.on(
						"click",
						function() {
							$('#inputForm')
									.attr('action',
											'${ctx}/oa/act/oaPeopleMediationApply/save');
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
		<li class="active"><a href="javascript:;">查看人民调解申请表</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaPeopleMediationApply"
		action="${ctx}/oa/act/oaPeopleMediationApply/submit" method="post"
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
					<form:input path="accuserName" disabled="true" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control required" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="accuserSex" disabled="true"
						class="input-xlarge form-control required">
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
					<input name="accuserBirthday" type="text" readonly="readonly"
						maxlength="20" class="form-control required"
						value="<fmt:formatDate value="${oaPeopleMediationApply.accuserBirthday}" pattern="yyyy-MM-dd"/>" />
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
					<form:input path="accuserCounty.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaPeopleMediationApply.accuserCounty.name}" disabled="true"/>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="accuserEthnic" disabled="true"
						class="input-xlarge form-control required">
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
					<form:input path="accuserOccupation" disabled="true"
						htmlEscape="false" maxlength="64"
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
					<form:input path="accuserTown.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaPeopleMediationApply.accuserTown.name}" disabled="true"/>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="accuserPostCode" disabled="true"
						htmlEscape="false" maxlength="10"
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
					<form:input path="accuserPhone" disabled="true" htmlEscape="false"
						maxlength="20" class="input-xlarge form-control " />
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
					<form:input path="accuserIdcard" disabled="true" htmlEscape="false"
						maxlength="64" class="input-xlarge form-control required" />
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
					<form:input path="accuserDomicile" disabled="true"
						htmlEscape="false" maxlength="64"
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
					<form:input path="accuserAddress" disabled="true"
						htmlEscape="false" maxlength="600"
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
					<form:input path="defendantName" disabled="true" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="defendantSex" disabled="true"
						class="input-xlarge form-control required">
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
					<input name="defendantBirthday" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${oaPeopleMediationApply.defendantBirthday}" pattern="yyyy-MM-dd"/>"
						disabled="true" />
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
					<form:input path="defendantCounty.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaPeopleMediationApply.defendantCounty.name}" disabled="true"/>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="defendantEthnic" disabled="true"
						class="input-xlarge form-control required">
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
					<form:input path="defendantOccupation" disabled="true"
						htmlEscape="false" maxlength="64"
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
					<form:input path="defendantTown.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaPeopleMediationApply.defendantTown.name}" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="defendantPostCode" disabled="true"
						htmlEscape="false" maxlength="10"
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
					<form:input path="defendantPhone" disabled="true"
						htmlEscape="false" maxlength="20"
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
					<form:input path="defendantIdcard" disabled="true"
						htmlEscape="false" maxlength="64"
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
					<form:input path="defendantDomicile" disabled="true"
						htmlEscape="false" maxlength="600"
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
					<form:input path="defendantAddress" disabled="true"
						htmlEscape="false" maxlength="600"
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
					<label class="control-label">纠纷类型</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="caseType"
						class="input-xlarge form-control required" disabled="true">
						<option value="" htmlEscape="false">请选择</option>
						<form:options
							items="${fns:getChildrenDictList('oa_case_classify','people_mediation')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生旗县</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div style="display:none">
					<sys:treeselect id="caseCounty" name="caseCounty.id" value="${oaPeopleMediationApply.caseCounty.id}" 
					labelName="caseCounty.name" labelValue="${oaPeopleMediationApply.caseCounty.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" parentId="caseCounty" />
				</div>
				<form:input path="caseCounty.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaPeopleMediationApply.caseCounty.name}" disabled="true"/>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生乡镇</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div style="display:none">
					<sys:treeselect id="caseTown" name="caseTown.id" value="${oaPeopleMediationApply.caseTown.id}" 
					labelName="caseTown.name" labelValue="${oaPeopleMediationApply.caseTown.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" parentId="caseCounty" disabled="true"/>
				</div>
				<form:input path="caseTown.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaPeopleMediationApply.caseTown.name}" disabled="true"/>
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
					<form:input path="caseTitle" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control " />
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
					<form:textarea path="caseSituation" disabled="true"
						htmlEscape="false" rows="8" class="form-control valid required" />
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
							value="${oaPeopleMediationApply.caseFile}" />
						<sys:ckfinder input="files" type="files" uploadPath="/oa/act"
							selectMultiple="true" readonly="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<!-- <div class="col-xs-12">
			<label class="control-label">案件承办人</label>
			</div> -->
			<h3>案件承办人</h3>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">人民调解委员会</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<sys:treeselectUser id="peopleMediationCommittee"
						name="peopleMediationCommittee.id"
						value="${oaPeopleMediationApply.peopleMediationCommittee.id}"
						labelName="peopleMediationCommittee.name"
						labelValue="${oaPeopleMediationApply.peopleMediationCommittee.name}"
						title="人民调解委员会"
						url="/oa/act/oaPeopleMediationApply/comTreeData?ceshi=1"
						cssClass="form-control valid" notAllowSelectParent="true"
						checked="false" allowInput="false" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">人民调解员</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
					<sys:treeselect id="mediator" name="mediator.id"
						value="${oaPeopleMediationApply.mediator.id}"
						labelName="mediator.name"
						labelValue="${oaPeopleMediationApply.mediator.name}" title="区域"
						url="/oa/act/oaPeopleMediationApply/manTreeData?ceshi=1"
						cssClass=" form-control  valid " allowClear="false"
						notAllowSelectParent="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-11">
				<shiro:hasPermission name="oa:act:oaPeopleMediationApply:edit">
					<c:choose>
						<c:when
							test="${oaPeopleMediationApply.act.taskDefKey eq 'mediation_zhiding'}">
							<input id="btnSave1" class="btn btn-primary" type="submit"
								value="保存" />&nbsp;
						<input id="btnSubmit1" class="btn btn-primary" type="submit"
								value="指定" />&nbsp;
					</c:when>
						<c:when
							test="${oaPeopleMediationApply.act.taskDefKey eq 'mediation_shenhe'}">
							<input id="btnSave2" class="btn btn-primary" type="submit"
								value="保存" />&nbsp;
						<input id="btnSubmit2" class="btn btn-primary" type="button"
								value="受理" />&nbsp;
						 <input class="btn btn-primary" type="button" value="驳回"
								onclick="back('0')" />&nbsp;
					</c:when>
						<c:when
							test="${oaPeopleMediationApply.act.taskDefKey eq 'mediation_xiugai'}">
							<input id="btnSave3" class="btn btn-primary" type="submit"
								value="保存" />&nbsp;
						<input id="btnSubmit3" class="btn btn-primary" type="button"
								value="提交" />&nbsp;
					</c:when>
					</c:choose>
				</shiro:hasPermission>
				<input id="btnCancel" class="btn btn-primary" type="button"
					value="返 回" onclick="history.go(-1)" />
			</div>
		</div>
		<act:histoicFlow procInsId="${oaPeopleMediationApply.act.procInsId}" />
	</form:form>
</body>
</html>
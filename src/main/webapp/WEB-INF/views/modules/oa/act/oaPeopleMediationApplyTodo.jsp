<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人民调解管理</title>
<meta name="decorator" content="default" />
<style type="text/css">
.reClosely {
	position: relative;
	width: 60%;
	margin-top: 5px;
	margin-left: 9px;
	height: 150px;
	display: none;
	margin-bottom: 20px;
}

.reClosely .closelyTextarea {
	width: 60%;
	height: 100%;
	box-sizing: border-box;
	resize: none;
	padding: 20px 100px 5px 5px;
}

.reClosely .closelyTextareaBtn {
	position: absolute;
	background: black;
	color: white;
	margin-right: 15px;
	text-align: center;
	width: 55px;
	height: 30px;
	line-height: 20px;
	cursor: pointer;
	top: 90px;
	left: 50%;
}

.reClosely .closelyTextareaBan {
	position: absolute;
	background: black;
	color: white;
	margin-right: 15px;
	text-align: center;
	width: 55px;
	height: 30px;
	line-height: 20px;
	cursor: pointer;
	top: 130px;
	left: 50%;
}
</style>
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						/* $("#inputForm").validate({
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
						}); */
						var taskDefKey = "${oaPeopleMediationApply.act.taskDefKey}";
						$("#accuserName").focus();
						jQuery.validator.addMethod('checkFullName', function(
								value, element) {
							return this.optional(element)
									|| /[^\u0000-\u00FF]/.test(value);
						}, "姓名必须是中文");
						//验证手机
						jQuery.validator.addMethod('checkPhoneNum', function(
								value, element) {
							return this.optional(element)
									|| /^1[34578]\d{9}$/.test(value);
						}, "请填写正确的手机号码");
						jQuery.validator
								.addMethod(
										"checkPaperNum",
										function(value, element) {
											return this.optional(element)
													|| /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
															.test(value);
										}, "请输入正确的身份证号");

						$("#inputForm")
								.validate(
										{
											rules : {
												accuserName : {
													required : true,
													checkFullName : true,
													rangelength : [ 2, 20 ]
												},
												accuserPhone : {
													required : true,
													checkPhoneNum : true
												},

												accuserIdcard : {
													required : true,
													checkPaperNum : true
												},
												accuserBirthday : {
													required : true
												},
												accuserCounty : {
													required : true
												},

												//被申请人的
												defendantName : {
													required : function() {
														if ("mediation_zhiding" == taskDefKey) {
															return true
														}
														return false
													},
													checkFullName : true,
													rangelength : [ 2, 20 ]
												},
												defendantPhone : {
													required : function() {
														if ("mediation_zhiding" == taskDefKey) {
															return true
														}
														return false
													},
													checkPhoneNum : true
												},

												defendantIdcard : {
													checkPaperNum : true,
													required : function() {
														if ("mediation_zhiding" == taskDefKey) {
															return true
														}
														return false
													}
												}
											},
											messages : {
												accuserName : {
													required : '姓名不能为空',
													checkUserName : '姓名必须是中文',
													rangelength : '姓名必须是2-20字之间'
												},
												accuserPhone : {
													required : '手机号码不能为空',
													checkPhoneNum : '请填写正确的手机号码格式'
												},
												accuserIdcard : {
													required : '身份证号不能为空'
												},
												accuserBirthday : {
													required : "出生日期不能为空"
												},
												accuserCounty : {
													required : "地区不能为空"
												},
												defendantName : {
													required : '姓名不能为空',
													checkUserName : '姓名必须是中文',
													rangelength : '姓名必须是2-20字之间'
												},
												defendantPhone : {
													required : '手机号码不能为空',
													checkPhoneNum : '请填写正确的手机号码格式'
												},
												defendantIdcard : {
													required : '身份证号不能为空',
													checkPaperNum : '请输入正确的身份证号'
												}
											},
											submitHandler : function(form) {
												loading('正在提交，请稍等...');
												form.submit();
											},
											errorContainer : "#messageBox",
											errorPlacement : function(error,
													element) {
												$("#messageBox").text(
														"输入有误，请先更正。");
												if (element.is(":checkbox")
														|| element.is(":radio")
														|| element
																.parent()
																.is(
																		".input-append")) {
													error.appendTo(element
															.parent().parent());
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
						$("#btnReturn")
								.on(
										"click",
										function() {
											if ($('#flag').val() == "no"
													&& $("#comment").val() == "") {
												toVail("审核意见不能为空", "warning");
											} else {
												$('#inputForm')
														.attr('action',
																'${ctx}/oa/act/oaPeopleMediationApply/toDo');
												$('#flag').val('no');
												$('#inputForm').submit();
											}
										});
					});
	function back(type) {
		if (type == 0) {
			$('#yj').show();
		} else {
			$('#yj').hide();
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
		<li class="active"><a
			href="${ctx}/oa/act/oaPeopleMediationApply/form?id=${oaPeopleMediationApply.id}">人民调申请表审核</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaPeopleMediationApply"
		action="" method="post" class="form-horizontal">
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
				<div class="form-group transverse">
					<form:input path="accuserName" htmlEscape="false" maxlength="200"
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
					<form:select path="accuserSex" class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
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
						maxlength="20" class="form-control "
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
					<sys:treeselect id="accuserCounty" name="accuserCounty.id"
						value="${oaPeopleMediationApply.accuserCounty.id}"
						labelName="accuserCounty.name"
						labelValue="${oaPeopleMediationApply.accuserCounty.name}"
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
					<form:select path="accuserEthnic"
						class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
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
					<form:input path="accuserOccupation" htmlEscape="false"
						maxlength="64" class="input-xlarge form-control " />
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
					<sys:treeselect id="accuserTown" name="accuserTown.id"
						value="${oaPeopleMediationApply.accuserTown.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationApply.accuserTown.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
						parentId="accuserCounty" allowClear="false"
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
					<form:input path="accuserPostCode" htmlEscape="false"
						maxlength="10" class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="accuserPhone" htmlEscape="false" maxlength="20"
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
					<form:input path="accuserIdcard" htmlEscape="false" maxlength="64"
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
					<form:input path="accuserDomicile" htmlEscape="false"
						maxlength="64" class="input-xlarge form-control " />
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
					<form:input path="accuserAddress" htmlEscape="false"
						maxlength="600" class="input-xlarge form-control " />
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
					<form:input path="defendantName" htmlEscape="false" maxlength="200"
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
					<form:select path="defendantSex" class="input-xlarge form-control ">
						<form:option value=""> 请选择</form:option>
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
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
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
					<sys:treeselect id="defendantCounty" name="defendantCounty.id"
						value="${oaPeopleMediationApply.defendantCounty.id}"
						labelName="defendantCounty.name"
						labelValue="${oaPeopleMediationApply.defendantCounty.name}"
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
					<form:select path="defendantEthnic"
						class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
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
					<form:input path="defendantOccupation" htmlEscape="false"
						maxlength="64" class="input-xlarge form-control " />
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
					<sys:treeselect id="defendantTown" name="defendantTown.id"
						value="${oaPeopleMediationApply.defendantTown.id}"
						labelName="defendantTown.name"
						labelValue="${oaPeopleMediationApply.defendantTown.name}"
						title="区域" url="/sys/area/treeData"
						cssClass=" form-control  valid " parentId="defendantCounty"
						allowClear="false" notAllowSelectParent="false" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="defendantPostCode" htmlEscape="false"
						maxlength="10" class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="defendantPhone" htmlEscape="false" maxlength="20"
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
					<form:input path="defendantIdcard" htmlEscape="false"
						maxlength="64" class="input-xlarge form-control " />
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
					<form:input path="defendantDomicile" htmlEscape="false"
						maxlength="600" class="input-xlarge form-control " />
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
					<form:input path="defendantAddress" htmlEscape="false"
						maxlength="600" class="input-xlarge form-control " />
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
						class="input-xlarge form-control required">
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
				<div class="form-group">
					<sys:treeselect id="caseCounty" name="caseCounty.id"
						value="${oaPeopleMediationApply.caseCounty.id}"
						labelName="caseCounty.name"
						labelValue="${oaPeopleMediationApply.caseCounty.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
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
					<sys:treeselect id="caseTown" name="caseTown.id"
						value="${oaPeopleMediationApply.caseTown.id}"
						labelName="caseTown.name"
						labelValue="${oaPeopleMediationApply.caseTown.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
						parentId="caseCounty" allowClear="false"
						notAllowSelectParent="false" />
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
					<form:input path="caseTitle" htmlEscape="false" maxlength="200"
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
					<form:textarea path="caseSituation" htmlEscape="false" rows="8"
						class="form-control valid " />
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
					<%-- <form:hidden path="peopleMediationCommittee.id"/>
				   <form:input path="peopleMediationCommittee.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " /> --%>
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
					<%--  <form:hidden path="mediator.id"/>
				   <form:input path="mediator.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " /> --%>
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
		<%-- <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">审核意见</label>
				<form:input path="act.comment" id="comment" htmlEscape="false" maxlength="20" class="form-control "/>
				</div>
			</div>
		</div> --%>
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
		<div id="yj" class="reClosely">
			<label class="control-label" style="text-align: left;">审核意见</label>
			<br />
			<form:textarea path="act.comment" id="comment"
				class="closelyTextarea" rows="10" maxlength="257" />
			<input id="btnReturn" class="closelyTextareaBtn" type="submit"
				value="确定" onclick="returnBack()" /> <input
				class="closelyTextareaBan" type="button" value="取消"
				onclick="back('1')" />
		</div>
		<act:histoicFlow procInsId="${oaPeopleMediationApply.act.procInsId}" />
	</form:form>
</body>
</html>
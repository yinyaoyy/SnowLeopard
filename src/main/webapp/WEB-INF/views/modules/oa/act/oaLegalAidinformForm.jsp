<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>填写通知辩护管理</title>
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
						//$("#name").focus();
						jQuery.validator
								.addMethod(
										"checkIdCard",
										function(value, element) {
											var idCard = element.value;
											var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1,
													6, 3, 7, 9, 10, 5, 8, 4, 2,
													1 ];// 加权因子  
											var ValideCode = [ 1, 0, 10, 9, 8,
													7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X 
											var isTrueValidateCodeBy18IdCard = function(
													idCard) {
												var sum = 0; // 声明加权求和变量  
												var a_idCard = idCard.split("");
												if (a_idCard[17].toLowerCase() == 'x') {
													a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作
												}
												for (var i = 0; i < 17; i++) {
													sum += Wi[i] * a_idCard[i];// 加权求和
												}
												if (a_idCard[17] == ValideCode[sum % 11]) {
													return true;
												} else {
													return false;
												}
											};
											var isValidityBrithBy18IdCard = function(
													idCard18) {
												var year = idCard18.substring(
														6, 10);
												var month = idCard18.substring(
														10, 12);
												var day = idCard18.substring(
														12, 14);
												temp_date = new Date(year,
														parseFloat(month) - 1,
														parseFloat(day));
												// 这里用getFullYear()获取年份，避免千年虫问题
												if (temp_date.getFullYear() != parseFloat(year)
														|| temp_date.getMonth() != parseFloat(month) - 1
														|| temp_date.getDate() != parseFloat(day)) {
													return false;
												} else {
													temp_date = year + "-"
															+ month + "-" + day;
													return true;
												}
											};
											var isValidityBrithBy15IdCard = function(
													idCard15) {
												var year = idCard15.substring(
														6, 8);
												var month = idCard15.substring(
														8, 10);
												var day = idCard15.substring(
														10, 12);
												var temp_date = new Date(year,
														parseFloat(month) - 1,
														parseFloat(day));
												// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法
												if (temp_date.getYear() != parseFloat(year)
														|| temp_date.getMonth() != parseFloat(month) - 1
														|| temp_date.getDate() != parseFloat(day)) {
													return false;
												} else {
													return true;
												}
											};
											if (idCard) {
												if (/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/
														.test(idCard)) {
													if (idCard.length == 15
															&& isValidityBrithBy15IdCard(idCard)) {
														return true;
													} else if (idCard.length == 18
															&& isValidityBrithBy18IdCard(idCard)
															&& isTrueValidateCodeBy18IdCard(idCard)) {
														return true;
													} else {
														return false;
													}
												} else {
													return false;
												}
											}
										}, "请输入正确的身份证号");
						$("#inputForm").validate(
								{
									rules : {
										idCard : {
											required : true,
											checkIdCard : true
										},
										name : {
											required : true
										}
									},
									messages : {
										idCard : {
											required : '身份证号不能为空'
										},
										name : {
											required : '姓名不能为空'
										}
									},
									submitHandler : function(form) {
										if (checkSubmit()) {
											loading('正在提交，请稍等...');
											form.submit();
										}
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
					});
	function checkSubmit() {
		return true;
	}
</script>
<script type="text/javascript">
	function back(type) {
		if (type == 0) {
			$('#yj').show();
		} else {
			$('#yj').hide();
		}
	};

	function returnBack() {
		//驳回操作
		if ($.trim($('#act\\.comment').val()) == '') {
			toVail("请填写销毁信息", "warning");
			return false;
		}
		//设置驳回
		$('#flag').val('no');
		$('#inputForm').submit();
	}
</script>
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/bootstrap-select/bootstrap-select.css" />
<link rel="stylesheet" type="text/css"
	href="${ctxStatic}/bootstrap-select/bootstrap-select.min.css" />
<script type="text/javascript"
	src="${ctxStatic}/bootstrap-select/bootstrap-select.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/bootstrap-select/bootstrap-select.min.js"></script>
<script type="text/javascript">
$(function(){
	 $('#CaseGuilt').selectpicker({
		 'selectedText': 'cat',
			'noneSelectedText': '请选择',
         'style': 'btn-white'//设置按钮样式
	            });
})
	  
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a
			href="${ctx}/oa/act/oaLegalAidInform/form?id=${oaLegalAidInform.id}">填写通知辩护-<shiro:hasPermission
					name="oa:act:oaLegalAidInform:edit">${not empty oaLegalAidInform.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="oa:act:oaLegalAidInform:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaLegalAidInform"
		action="${ctx}/oa/act/oaLegalAidInform/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="legalAidType" />
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" class="taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">援助人员基本情况</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="name" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control required"
						disabled="${oaLegalAidInform.act.taskDefKey eq 'aid_apply_zhiding' }" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="sex" class="input-xlarge form-control required"
						disabled="${oaLegalAidInform.act.taskDefKey eq 'aid_apply_zhiding'}">
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
				<div class="form-group transverse">
					<input name="birthday" type="text" readonly="readonly"
						maxlength="20" class="form-control required"
						value="<fmt:formatDate value="${oaLegalAidInform.birthday}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
						${(oaLegalAidInform.act.taskDefKey eq 'aid_apply_zhiding')?'disabled="disabled"':''} />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">户籍地</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<sys:treeselect id="dom" name="dom.id"
						value="${oaLegalAidInform.dom.id}" labelName="dom.name"
						labelValue="${oaLegalAidInform.dom.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
						disabled="${(oaLegalAidInform.act.taskDefKey eq 'aid_apply_zhiding')?'disabled':''}"
						allowClear="false" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="ethnic"
						class="input-xlarge form-control required">
						<form:option value=""> 请选择</form:option>
						<form:options items="${fns:getDictList('ethnic')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">文化程度</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="education"
						class="input-xlarge form-control required">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_education')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">国籍</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="internation"
						class="input-xlarge form-control required">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_internation')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系人</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="proxyName" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control required" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="phone" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受援人员类别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="renyuanType" class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_personnel_category')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">同案人数</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="casesum" htmlEscape="false"
						class="input-xlarge form-control required" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="idCard" htmlEscape="false" maxlength="18"
						class="input-xlarge form-control required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">户籍地详细</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
					<form:input path="domicile" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control required"
						disabled="${oaLegalAidInform.act.taskDefKey eq 'aid_apply_zhiding'}" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">羁押地</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
					<form:input path="address" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control required" />
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">申请事项信息</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<label class="control-label">受理日期</label><br />
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="sldate" type="text" readonly="readonly" maxlength="15"
						class="form-control required"
						value="<fmt:formatDate value="${oaLegalAidInform.sldate}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件名称</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="caseTitle" htmlEscape="false" maxlength="15"
						class="input-xlarge form-control required"
						disabled="${oaLegalAidInform.act.taskDefKey eq 'aid_apply_zhiding'}" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">通知机关类型</label>
				</div>
			</div>

			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="officeType" htmlEscape="false" maxlength="15"
						class="input-xlarge form-control required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">通知机关名称</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="officeNamee" htmlEscape="false" maxlength="15"
						class="input-xlarge form-control required" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">机关联系人</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="jgPerson" htmlEscape="false" maxlength="15"
						class="input-xlarge form-control required" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">机关联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="jgphone" htmlEscape="false" maxlength="15"
						class="input-xlarge form-control required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">通知函号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="caseInform" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件年号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="year" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件期号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="yearNo" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">通知原因</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="informReson" class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_informReson')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">诉讼阶段</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="casesStage" class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_litigation_phase')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件涉及</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="caseTelevancy"
						class="input-xlarge form-control ">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_case_involving')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
						<label class="control-label">涉及罪名</label>
					</div>
			</div>
			<div class="col-xs-11">
				<select id=CaseGuilt name="CaseGuilt" multiple
					data-live-search="true">
					<c:forEach items="${fns:getDictList('oa_caseGuilt')}" var="workers">
						<option value="${workers.value}">${workers.label}</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">附带民诉原告人</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
					<form:input path="cumName" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " />
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">备注</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
					<form:textarea path="caseReason" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件相关文件</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<input type="hidden" id="caseFile" name="caseFile"
						value="${oaLegalAidInform.caseFile}" />
					<sys:ckfinder input="caseFile" type="files"
						uploadPath="/oa/oaLegalAidInform" selectMultiple="true" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${oaLegalAidInform.act.taskDefKey eq ''?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">法援中心</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<sys:treeselectOfficeUser id="legalOffice" name="legalOffice.id"
						value="${oaLegalAidInform.legalOffice.id}"
						labelName="legalOffice.name"
						labelValue="${oaLegalAidInform.legalOffice.name}" title="法援机构"
						url="/sys/office/getOfficeUser?type=5"
						cssClass="form-control valid" notAllowSelectParent="false"
						checked="false" allowInput="false" isUser="0" />
				</div>
			</div>
		</div>
		<div
			style="display:${( oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren')?'block':'none' }">
			<div class="row">
				<div class="col-xs-1">
					<div class="form-group">
						<label class="control-label">律师事务所</label>
					</div>
				</div>
				<div class="col-xs-9">
					<div class="form-group">
						<sys:treeselectUser id="lawOffice" name="lawOffice.id"
							value="${oaLegalAidInform.lawOffice.id}"
							labelName="lawOffice.name"
							labelValue="${oaLegalAidInform.lawOffice.name}" title="律师事务所"
							url="/sys/office/treeUser?type=2" cssClass="form-control valid"
							setRootId="${officeLawyerOffice}" notAllowSelectRoot="true"
							checked="false" allowInput="false" />
					</div>
				</div>
			</div>
		</div>
		<c:choose>
			<c:when
				test="${oaLegalAidInform.act.taskDefKey eq 'defense_update' }">
				<div id="yj" class="reClosely">
					<label class="control-label" style="text-align: left;">填写销毁信息：</label>
					<br />
					<form:textarea path="act.comment" class="closelyTextarea" rows="10"
						maxlength="257" />
					<input class="closelyTextareaBtn" type="button" value="确定"
						onclick="returnBack()" /> <input class="closelyTextareaBan"
						type="button" value="取消" onclick="back('1')" />
				</div>
			</c:when>
		</c:choose>
		<shiro:hasPermission name="oa:act:oaLegalAidInform:edit">
			<c:choose>
				<c:when test="${oaLegalAidInform.act.taskDefKey eq ''}">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="提交申请" />&nbsp;
					</c:when>
				<c:when
					test="${oaLegalAidInform.act.taskDefKey eq 'defense_update' }">
					<input id="btnSubmit2" class="btn btn-primary" type="submit"
						value="重新申请" onclick="$('#flag').val('yes')" />&nbsp;
						<input id="btn3" class="btn btn-primary" type="button" value="销毁"
						onclick="back(0)" />
				</c:when>
			</c:choose>
		</shiro:hasPermission>
		<input id="btnCancel" class="btn btn-primary" type="button"
			value="返 回" onclick="history.go(-1)" />
	</form:form>
</body>
</html>
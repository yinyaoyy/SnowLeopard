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
											checkFullName : true,
											rangelength : [ 2, 20 ]
										},
										defendantPhone : {
											checkPhoneNum : true
										},

										defendantIdcard : {
											checkPaperNum : true
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
											checkUserName : '姓名必须是中文',
											rangelength : '姓名必须是2-20字之间'
										},
										defendantPhone : {
											checkPhoneNum : '请填写正确的手机号码格式'
										},
										defendantIdcard : {
											checkPaperNum : '请输入正确的身份证号'
										}
									},
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
											$('#flag').val('yes')
											$('#inputForm')
													.attr('action',
															'${ctx}/oa/act/oaPeopleMediationApply/submit');
											$('#inputForm').submit();
										});
						$("#btnSubmit2")
								.on(
										"click",
										function() {
											$('#flag').val('no')
											$('#inputForm')
													.attr('action',
															'${ctx}/oa/act/oaPeopleMediationApply/submit');
											$('#inputForm').submit();
										});

						$("#btnSave")
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
		<li class="active"><a
			href="${ctx}/oa/act/oaPeopleMediationApply/form?id=${oaPeopleMediationApply.id}">人民调解<shiro:hasPermission
					name="oa:act:oaPeopleMediationApply:edit">${not empty oaPeopleMediationApply.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="oa:act:oaPeopleMediationApply:edit">查看</shiro:lacksPermission></a></li>
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
				<div class="form-group transverse">
					<form:select path="accuserSex" class="input-xlarge form-control ">
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
					<input name="accuserBirthday" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${oaPeopleMediationApply.accuserBirthday}" pattern="yyyy-MM-dd"/>"
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
					<form:select path="accuserEthnic"
						class="input-xlarge form-control ">
						<form:option value=""> 请选择</form:option>
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
					<sys:treeselect id="accuserTown" name="accuserTown.id"
						value="${oaPeopleMediationApply.accuserTown.id}"
						labelName="area.name"
						labelValue="${oaPeopleMediationApply.accuserTown.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
						allowClear="false" notAllowSelectParent="false" />
				</div>
			</div>

			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
					<form:select path="defendantEthnic"
						class="input-xlarge form-control ">
						<form:option value=""> 请选择</form:option>
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
					<sys:treeselect id="defendantTown" name="defendantTown.id"
						value="${oaPeopleMediationApply.defendantTown.id}"
						labelName="defendantTown.name"
						labelValue="${oaPeopleMediationApply.defendantTown.name}"
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
						allowClear="false" notAllowSelectParent="false" />
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
				<div class="form-group transverse">
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
		<div class="form-actions">
			<shiro:hasPermission name="oa:act:oaPeopleMediationApply:edit">
				<input id="btnSave" class="btn btn-primary" type="submit" value="保存" />&nbsp;
			       <input id="btnSubmit" class="btn btn-primary" type="submit"
					value="提交" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
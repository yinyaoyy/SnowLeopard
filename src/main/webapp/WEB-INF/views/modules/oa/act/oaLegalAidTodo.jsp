<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>法律援助管理</title>
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
	$(document).ready(function() {
		//$("#name").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				if(checkSubmit()){
					if(caseType()){
						loading('正在提交，请稍等...');
						form.submit();
					}
				}
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
		});
	});
	function checkSubmit(){
		if(${oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding' } && $("#lawOfficeId").val()=="" && $("#legalOfficeId").val()=="" && $("#lawAssistanceOfficeId").val()==""){
			toVail("请指定律师事务所或基层法律服务所","warning");
			return false;
		}
		if(${oaLegalAid.act.taskDefKey eq 'aid_zhuren'} && $("#lawOfficeId").val()!="" && $("#lawyerId").val()==""){
			toVail("请指定律师","warning");
			return false;
		}
		if(${oaLegalAid.act.taskDefKey eq 'aid_zhuren'} && $("#legalOfficeId").val()!="" && $("#legalPersonId").val()==""){
			toVail("请指定基层法律服务工作者","warning");
			return false;
		}
		if(${oaLegalAid.act.taskDefKey eq 'aid_pingjia'}){
			if(!(/^\d+$/.test($("#thirdPartyScore").val()))){
				toVail("请填写第三方评分","warning");
				return false;
			}
			if($("#thirdPartyEvaluation").val()==""){
				toVail("请填写第三方评价","warning");
				return false;
			}
		}
		if(${oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie'} && $.trim($("#receiveSubsidy").val())==""){
			toVail("请填写承办案件领取补贴信息","warning");
			return false;
		}
		return true;
	}
	function returnBack(){
		//驳回操作
		if($.trim($('#act\\.comment').val())==''){
			toVail("请填写驳回信息","warning");
			return false;
		}
		//设置驳回
		$('#flag').val('no');
		$('#inputForm').submit();
	}
	function caseType(){
		if(${oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding' } && $("#lawOfficeId").val()!="" && $("#lawOfficeId").val()!= "" && $("#legalOfficeId").val()!="" && $("#legalOfficeId").val()!=""){
			toVail("不能同时选择律师事务所和基层法律服务所","warning");
			return false;
		}
		if(${oaLegalAid.act.taskDefKey eq 'aid_zhuren'} && $("#lawyerId").val()!="" && $("#lawyerId").val()!="" && $("#legalPersonId").val()!="" && $("#legalPersonId").val()!=""){
			toVail("不能同时选择律师和基层法律服务工作者","warning");
			return false;
		}
		return true;
	}
	function back(type){
		if(type == 0){
			$('#yj').show();
		}else{
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
		<li class="active"><a href="javascript:;">办理法律援助</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaLegalAid"
		action="${ctx}/oa/act/oaLegalAid/toDo" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" class="taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />
		<!-- 选择人员时需根据此值限制机构范围 -->
		<input type="hidden" id="companyId" />
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
					<form:input path="name" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control required" disabled="true" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="sex" class="input-xlarge form-control "
						disabled="true">
						<form:options items="${fns:getDictList('sex')}" itemLabel="label"
							itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="ethnic" class="input-xlarge form-control "
						disabled="true">
						<form:options items="${fns:getDictList('ethnic')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">出生日期</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<input name="birthday" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${oaLegalAid.birthday}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在区域</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<sys:treeselect id="area" name="area.id"
						value="${oaLegalAid.area.id}" labelName="area.name"
						labelValue="${oaLegalAid.area.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
						disabled="disabled" allowClear="false"
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
					<form:input path="postCode" htmlEscape="false" maxlength="6"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="idCard" htmlEscape="false" maxlength="18"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="phone" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所属类型</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="aidCategory" class="input-xlarge form-control "
						disabled="true">
						<form:options items="${fns:getDictList('legal_aid_category')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">工作单位</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="employer" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">住所地（经常居住地）</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="address" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " disabled="true" />
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
					<form:input path="domicile" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">代理人基本信息</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:input path="proxyName" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<p id="showRole">
						<c:forEach items="${fns:getDictList('legal_aid_proxy_type')}"
							var="pt">
							<label class="checkbox-inline"> <input type="radio"
								name="proxyType" value="${pt.value }"
								${(oaLegalAid.proxyType eq pt.value)?'checked="checked"':''}
								disabled="disabled" />${pt.label}
							</label>
						</c:forEach>
					</p>
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
					<form:input path="proxyIdCard" htmlEscape="false" maxlength="20"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">代理人基本情况</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案情标题</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="caseTitle" htmlEscape="false" maxlength="200"
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案情类型</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="caseType" class="input-xlarge form-control "
						disabled="true">
						<form:options
							items="${fns:getChildrenDictList('oa_case_classify','legal_aid')}"
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
					<form:select path="hasMongol" class="input-xlarge form-control "
						disabled="true">
						<form:options items="${fns:getDictList('yes_no')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案情性质</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="caseNature" class="input-xlarge form-control "
						disabled="true">
						<form:options items="${fns:getDictList('case_nature')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
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
					<form:textarea path="caseReason" htmlEscape="false" rows="8"
						class="form-control valid" disabled="true" />
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
						value="${oaLegalAid.caseFile}" />
					<sys:ckfinder input="caseFile" type="files"
						uploadPath="/oa/legalAid" selectMultiple="true" readonly="true" />
				</div>
			</div>
		</div>
		<!-- 仅在承办人办理、第三方评价、承办人申领补贴环节显示 -->
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'aid_chengbanren_banli' or oaLegalAid.act.taskDefKey eq 'aid_pingjia' or oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie')?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件办理过程</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="caseHandleProcess" htmlEscape="false" rows="8"
						class="form-control valid"
						readonly="${oaLegalAid.act.taskDefKey ne 'aid_chengbanren_banli'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'aid_chengbanren_banli' or oaLegalAid.act.taskDefKey eq 'aid_pingjia' or oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie')?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件相关文件(承办人上传)</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<input type="hidden" id="caseFile2" name="caseFile2"
						value="${oaLegalAid.caseFile2}" />
					<sys:ckfinder input="caseFile2" type="files"
						uploadPath="/oa/legalAid" selectMultiple="true"
						readonly="${oaLegalAid.act.taskDefKey ne 'aid_chengbanren_banli'}" />
				</div>
			</div>
		</div>
		<!-- 显示条件:
			1.科员审核的时候不显示
			2.科员指定的时候需要显示
			3.其他时候需要判断是否指定了律所处理，是律所处理的才显示
		 -->
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'adi_approve')?'none':((oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'block':((not empty (oaLegalAid.lawOffice.id))?'block':'none'))}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">律师事务所</label>
				</div>
			</div>
			<div
				class="col-xs-${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'11':'5'}">
				<div class="form-group">
					<sys:treeselectOfficeUser id="lawOffice" name="lawOffice.id"
						value="${oaLegalAid.lawOffice.id}" labelName="lawOffice.name"
						labelValue="${oaLegalAid.lawOffice.name}" title="律师事务所"
						url="/sys/office/getOfficeUser?type=2"
						cssClass="form-control valid" setRootId="${officeLawyerOffice}"
						disabled="${(oaLegalAid.act.taskDefKey eq 'aid_zhuren')?'disabled':''}"
						checked="false" allowInput="false" allowClear="true"
						areaId="${oaLegalAid.area.id }" isUser="0" />
				</div>
			</div>
			<div class="col-xs-1"
				style="display:${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'none':'block'}">
				<div class="form-group">
					<label class="control-label">律师库</label>
				</div>
			</div>
			<div class="col-xs-5"
				style="display:${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'none':'block'}">
				<div class="form-group">
					<%-- <sys:treeselectOfficeUser id="lawOffice" name="lawOffice.id" value="${oaLegalAid.lawOffice.id}"
					labelName="lawOffice.name" labelValue="${oaLegalAid.lawOffice.name}" title="律师事务所"
					url="/sys/office/getOfficeUser?type=2" cssClass="form-control valid" 
					setRootId="${officeLawyerOffice}"
					disabled="${(oaLegalAid.act.taskDefKey eq 'aid_zhuren')?'disabled':''}"
					checked="false" allowInput="false" allowClear="true" areaId="${oaLegalAid.area.id }" isUser="0"/> --%>

					<sys:treeselectOfficeUser id="lawyer" name="lawyer.id"
						value="${oaLegalAid.lawyer.id}" labelName="lawyer.name"
						labelValue="${oaLegalAid.lawyer.name}" title="律师"
						url="/sys/office/getOfficeUser?type=2"
						cssClass="form-control valid"
						setRootId="${oaLegalAid.lawOffice.id}"
						disabled="${(oaLegalAid.act.taskDefKey eq 'aid_chengbanren_shouli')?'disabled':''}"
						officeId="${oaLegalAid.lawOffice.id}" notAllowSelectParent="true"
						checked="false" allowClear="true" allowInput="false" isUser="1" />
				</div>
			</div>
		</div>
		<!-- 显示条件:
			1.科员审核或者指定申请时不显示
			2.科员指定的时候需要显示，并且不能是刑事、刑事附带民事案件时才能显示
			3.其他时候需要判断是否指定了法律服务所处理，是法律服务所处理的才显示
		 -->
		<div class="row"
			style="display:${((oaLegalAid.act.taskDefKey ne 'adi_approve') and oaLegalAid.legalAidType=='1')?((oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding' and oaLegalAid.caseType ne '1' and oaLegalAid.caseType ne '4')?'block':((not empty (oaLegalAid.legalOffice.id))?'block':'none')):'none'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">基层法律服务所</label>
				</div>
			</div>
			<div
				class="col-xs-${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'11':'5'}">
				<div class="form-group">
					<sys:treeselectOfficeUser id="legalOffice" name="legalOffice.id"
						value="${oaLegalAid.legalOffice.id}" labelName="legalOffice.name"
						labelValue="${oaLegalAid.legalOffice.name}" title="基层法律服务所"
						url="/sys/office/getOfficeUser?type=8"
						cssClass="form-control valid" setRootId="${officeLegalService}"
						disabled="${(oaLegalAid.act.taskDefKey eq 'aid_zhuren')?'disabled':''}"
						checked="false" allowInput="false" allowClear="true"
						areaId="${oaLegalAid.area.id }" isUser="0" />
				</div>
			</div>
			<div class="col-xs-1"
				style="display:${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'none':'block'}">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">基层法律服务工作者</label>
				</div>
			</div>
			<div class="col-xs-3"
				style="display:${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'none':'block'}">
				<div class="form-group">
					<sys:treeselectOfficeUser id="legalPerson" name="legalPerson.id"
						value="${oaLegalAid.legalPerson.id}" labelName="legalPerson.name"
						labelValue="${oaLegalAid.legalPerson.name}" title="基层法律服务工作者"
						url="/sys/office/getOfficeUser?type=8"
						cssClass="form-control valid"
						setRootId="${oaLegalAid.legalOffice.id}"
						disabled="${(oaLegalAid.act.taskDefKey eq 'aid_chengbanren_shouli')?'disabled':''}"
						officeId="${oaLegalAid.legalOffice.id}"
						notAllowSelectParent="true" checked="false" allowInput="false"
						allowClear="true" isUser="1" />
				</div>
			</div>
		</div>
		
		<!-- 显示条件:
			1.科员审核或者指定申请时不显示
			2.科员指定的时候需要显示，并且不能是刑事、刑事附带民事案件时才能显示
			3.其他时候需要判断是否指定了法律服务所处理，是法律服务所处理的才显示
		 -->
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'adi_approve')?'none':((oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'block':((not empty (oaLegalAid.lawAssistanceOffice.id))?'block':'none'))}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">法律援助中心</label>
				</div>
			</div>
			<div
				class="col-xs-${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'11':'5'}">
				<div class="form-group">
					<sys:treeselectOfficeUser id="lawAssistanceOffice" name="lawAssistanceOffice.id"
						value="${oaLegalAid.lawAssistanceOffice.id}" labelName="lawAssistanceOffice.name"
						labelValue="${oaLegalAid.lawAssistanceOffice.name}" title="法律援助中心"
						url="/sys/office/getOfficeUser?type=5"
						cssClass="form-control valid" setRootId="${officeLegalService}"
						disabled="${(oaLegalAid.act.taskDefKey eq 'aid_zhuren')?'disabled':''}"
						checked="false" allowInput="false" allowClear="true"
						areaId="${oaLegalAid.area.id }" isUser="0" />
				</div>
			</div>
			<div class="col-xs-1"
				style="display:${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'none':'block'}">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">法援律师</label>
				</div>
			</div>
			<div class="col-xs-3"
				style="display:${(oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding')?'none':'block'}">
				<div class="form-group">
					<sys:treeselectOfficeUser id="lawAssistUser" name="lawAssistUser.id"
						value="${oaLegalAid.lawAssistUser.id}" labelName="lawAssistUser.name"
						labelValue="${oaLegalAid.lawAssistUser.name}" title="法援律师"
						url="/sys/office/getOfficeUser?type=5&&flag=1"
						cssClass="form-control valid"
						setRootId="${oaLegalAid.lawAssistanceOffice.id}"
						disabled="${(oaLegalAid.act.taskDefKey eq 'aid_chengbanren_shouli')?'disabled':''}"
						officeId="${oaLegalAid.lawAssistanceOffice.id}"
						notAllowSelectParent="true" checked="false" allowInput="false"
						allowClear="true" isUser="1" />
				</div>
			</div>
		</div>
		
		
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'aid_pingjia' or oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie')?'block':'none' }">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">第三方评估人员评分</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="thirdPartyScore" htmlEscape="false"
						maxlength="600" class="form-control "
						disabled="${oaLegalAid.act.taskDefKey ne 'aid_pingjia'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'aid_pingjia' or oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie')?'block':'none' }">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">第三方评估人员评价</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="thirdPartyEvaluation" htmlEscape="false"
						rows="8" maxlength="2000" class="form-control valid"
						disabled="${oaLegalAid.act.taskDefKey ne 'aid_pingjia'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie')?'block':'none' }">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">承办案件领取补贴信息</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="receiveSubsidy" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<!-- 填写驳回意见 -->
		<%-- <div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">填写驳回意见：</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<form:textarea path="act.comment" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div> --%>
		<div class="row">
			<div class="col-xs-9">
				<shiro:hasPermission name="oa:act:oaLegalAid:edit">
					<c:choose>
						<c:when
							test="${oaLegalAid.act.taskDefKey eq 'adi_approve'
							or oaLegalAid.act.taskDefKey eq 'aid_chengbanren_shouli' }">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="同意" onclick="$('#flag').val('yes')" />&nbsp;
						<input id="btn2" class="btn btn-primary" type="button" value="驳回"
								onclick="back('0')" />&nbsp;
					</c:when>
						<c:when test="${oaLegalAid.act.taskDefKey eq 'aid_zhuren' }">
							<input id="btnSubmit3" class="btn btn-primary" type="submit"
								value="提交" onclick="$('#flag').val('yes')" />&nbsp;
						<input id="btn4" class="btn btn-primary" type="button" value="驳回"
								onclick="back('0')" />&nbsp;
					</c:when>
						<c:when
							test="${oaLegalAid.act.taskDefKey eq 'aid_ky_zhiding'
			       			or oaLegalAid.act.taskDefKey eq 'aid_pingjia'
			       			or oaLegalAid.act.taskDefKey eq 'aid_chengbanren_butie' }">
							<input id="btnSubmit5" class="btn btn-primary" type="submit"
								value="提交" onclick="$('#flag').val('yes')" />&nbsp;
					</c:when>
						<c:when
							test="${oaLegalAid.act.taskDefKey eq 'aid_chengbanren_banli' }">
							<input id="btnSubmit6" class="btn btn-primary" type="submit"
								value="保存" onclick="$('#flag').val('no')" />&nbsp;
						<input id="btnSubmit7" class="btn btn-primary" type="submit"
								value="办结"
								onclick="if(confirm('确定此业务已经办结吗?')){$('#flag').val('yes');}" />&nbsp;
					</c:when>
					</c:choose>
				</shiro:hasPermission>
				<input id="btnCancel" class="btn btn-primary" type="button"
					value="返 回" onclick="history.go(-1)" />
			</div>
		</div>
		<div id="yj" class="reClosely">
			<label class="control-label" style="text-align: left;">填写退回意见：</label>
			<br />
			<form:textarea path="act.comment" class="closelyTextarea" rows="10"
				maxlength="257" />
			<input class="closelyTextareaBtn" type="button" value="确定"
				onclick="returnBack()" /> <input class="closelyTextareaBan"
				type="button" value="取消" onclick="back('1')" />
		</div>
		<act:histoicFlow procInsId="${oaLegalAid.act.procInsId}" />
	</form:form>
</body>
</html>
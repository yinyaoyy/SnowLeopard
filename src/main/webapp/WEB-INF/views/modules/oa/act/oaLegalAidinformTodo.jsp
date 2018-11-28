<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>法援通知辩护管理</title>
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
					loading('正在提交，请稍等...');
					form.submit();
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
		if(${oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren' } && $("#lawOffice").val()=="" ){
			toVail("请指定律师事务所","warning");
			return false;
		}
		if(${oaLegalAidInform.act.taskDefKey eq ''} && $("#lawOfficeId").val()!="" && $("#lawyerId").val()==""){
			toVail("请指定律师","warning");
			return false;
		}
		if(${oaLegalAidInform.act.taskDefKey eq 'defense_pingjia'}){
			if(!(/^\d+$/.test($("#thirdPartyScore").val()))){
				toVail("请填写第三方评分","warning");
				return false;
			}
			if($("#thirdPartyEvaluation").val()==""){
				toVail("请填写第三方评价","warning");
				return false;
			}
		}
		if(${oaLegalAidInform.act.taskDefKey eq 'aid_chengbanren_butie'} && $.trim($("#receiveSubsidy").val())==""){
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
	function back(type){
		if(type == 0){
			$('#yj').show();
		}else{
			$('#yj').hide();
		}
	}
	
	
	function back1(type){
		$('#remarks').val('end')
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
		<li class="active"><a
			href="${ctx}/oa/oaLegalAidInform/form?id=${oaLegalAidInformInform.id}">办理法援通知辩护</a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="oaLegalAidInform"
		action="${ctx}/oa/act/oaLegalAidInform/toDo" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="act.taskId" />
		<form:hidden path="act.taskName" />
		<form:hidden path="act.taskDefKey" class="taskDefKey" />
		<form:hidden path="act.procInsId" />
		<form:hidden path="act.procDefId" />
		<form:hidden id="flag" path="act.flag" />
		<form:hidden id="remarks" path="remarks" />
		<!-- 选择人员时需根据此值限制机构范围 -->
		<input type="hidden" id="companyId" />
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
					<label class="control-label">出生日期</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="birthday" type="text" maxlength="20"
						class="form-control required"
						value="<fmt:formatDate value="${oaLegalAidInform.birthday}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
						readonly="readonly" 
						disabled="disabled"/>
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
				<div class="form-group">
					<sys:treeselect id="dom" name="dom.id"
						value="${oaLegalAidInform.dom.id}" labelName="dom.name"
						labelValue="${oaLegalAidInform.dom.name}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid "
						disabled="disabled" allowClear="false"
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
					<form:select path="ethnic" class="input-xlarge form-control "
						disabled="true">
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
						class="input-xlarge form-control required" disabled="true">
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
					<form:input path="internation" htmlEscape="false" maxlength="6"
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control required" disabled="true" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受援人员类别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="renyuanType" class="input-xlarge form-control"
						disabled="true">
						<form:option value="请选择"></form:option>
						<form:options items="${fns:getDictList('oa_personnel_category')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
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
					<form:input path="idCard" htmlEscape="false" maxlength="18"
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
				<div class="form-group transverse">
					<form:input path="domicile" htmlEscape="false" maxlength="600"
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control required" disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">申请事项信息</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<label class="control-label">受理日期</label>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="sldate" type="text" maxlength="15"
						class="form-control required"
						value="<fmt:formatDate value="${oaLegalAidInform.sldate}" pattern="yyyy-MM-dd"/>"
						readonly="readonly" />
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
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control required" disabled="true" />
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
						class="input-xlarge form-control " disabled="true" />
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
						class="input-xlarge form-control " disabled="true" />
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
						class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
			<div class="row"></div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">通知原因</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="informReson" class="input-xlarge form-control "
						disabled="true">
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
					<form:select path="casesStage" class="input-xlarge form-control "
						disabled="true">
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
						class="input-xlarge form-control " disabled="true">
						<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('oa_case_involving')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row" id="shji">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及罪名</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
					<form:textarea path="caseGuiltdesc" htmlEscape="false" rows="8"
						class="form-control valid required" disabled="true" />
				</div>
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
						class="input-xlarge form-control " readonly="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">诉讼阶段</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:select path="casesStage" class="input-xlarge form-control "
						disabled="true">
						<form:option value="请选择"></form:option>
						<form:options items="${fns:getDictList('oa_litigation_phase')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
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
						class="form-control valid required" disabled="true" />
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
						uploadPath="/oa/oaLegalAidInform" selectMultiple="true" readonly="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">法援中心</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<sys:treeselectUser id="legalOffice" name="legalOffice.id"
						value="${oaLegalAidInform.legalOffice.id}"
						labelName="legalOffice.name"
						labelValue="${oaLegalAidInform.legalOffice.name}" title="法援机构"
						url="/sys/office/treefyData?type=2" cssClass="form-control valid"
						notAllowSelectParent="false" checked="false" allowInput="false"
						disabled="disabled" />
				</div>
			</div>
		</div>

		<div class="row"
			style="display:${oaLegalAidInform.act.taskDefKey eq ''?'none':'block'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">援助方式</label>
				</div>
			</div>

			<div class="col-xs-11">
				<div class="form-group">
					<form:select path="modality" class="input-xlarge form-control "
						disabled="${oaLegalAidInform.act.taskDefKey eq 'defense_approve'?'false':'true'}">
						<form:option value="请选择"></form:option>
						<form:options items="${fns:getDictList('oa_modalities')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>


		<div class="row"
				style="display:${oaLegalAidInform.act.taskDefKey eq ''?'none':'block'}">
			<div class="col-xs-1">
				<label class="control-label">审查日期</label><br />
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="scdate" type="text" readonly="readonly" maxlength="15"
						class="form-control required"
						value="<fmt:formatDate value="${oaLegalAidInform.scdate}" pattern="yyyy-MM-dd"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
						${(oaLegalAidInform.act.taskDefKey eq 'defense_approve')?'':'disabled="disabled"'}/>
				</div>
			</div>
		</div>
		<!-- 仅在承办人办理、第三方评价、承办人申领补贴环节显示 -->
		<div class="row"
			style="display:${oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_banli'?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件办理过程</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="caseHandleProcess" htmlEscape="false" rows="8"
						class="form-control valid"
						readonly="${oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_banli'?'false':'true'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_banli' or oaLegalAidInform.act.taskDefKey eq 'defense_pingjia')?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件相关文件(承办人上传)</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<input type="hidden" id="caseFile2" name="caseFile2"
						value="${oaLegalAidInform.caseFile2}" />
					<sys:ckfinder input="caseFile2" type="files"
						uploadPath="/oa/oaLegalAidInform" selectMultiple="true"
						readonly="${oaLegalAidInform.act.taskDefKey ne 'defense_chengbanren_banli'?'true':'false'}" />
				</div>
			</div>
		</div>
		<!-- 显示条件:
			1.填写人申请时需要指定一个律师事务所
			
			3.其他时候需要判断是否指定了律所处理，是律所处理的才显示
		 -->
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_approve')?'none':(oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren')?'block':((oaLegalAidInform.act.taskDefKey eq 'aid_ky_zhiding')?'block':((not empty (oaLegalAidInform.lawOffice.id))?'block':'none'))}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">律师事务所</label>
				</div>
			</div>
			<div
				class="col-xs-${(oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren')?'11':'5'}">
				<div class="form-group">
					<sys:treeselectUser id="lawOffice" name="lawOffice.id"
						value="${oaLegalAidInform.lawOffice.id}"
						labelName="lawOffice.name"
						labelValue="${oaLegalAidInform.lawOffice.name}" title="律师事务所"
						url="/sys/office/treeUser?type=2" cssClass="form-control valid"
						setRootId="${officeLawyerOffice}"
						disabled="${(oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren')?'':'disabled'}"
						notAllowSelectParent="true" checked="false" allowInput="false" />
				</div>
			</div>
			<div style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_approve' or oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren')?'none':'block'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">律师</label>
				</div>
			</div>
			<div class="col-xs-5"
				>
				<div class="form-group">
					<sys:treeselectOfficeUser id="lawyer" name="lawyer.id"
						value="${oaLegalAidInform.lawyer.id}" labelName="lawyer.name"
						labelValue="${oaLegalAidInform.lawyer.name}" title="律师"
						url="/sys/office/getOfficeUser?type=2"
						cssClass="form-control valid"
						setRootId="${oaLegalAidInform.lawOffice.id}"
						disabled="${(oaLegalAidInform.act.taskDefKey eq 'aid_chengbanren_shouli')?'disabled':''}"
						officeId="${oaLegalAidInform.lawOffice.id}"
						notAllowSelectParent="true" checked="false" allowClear="true"
						allowInput="false" isUser="1" />
				</div>
			</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_pingjia' or oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_butie')?'block':'none' }">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">第三方评估人员评分</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="thirdPartyScore" htmlEscape="false"
						maxlength="600" class="form-control "
						disabled="${oaLegalAidInform.act.taskDefKey ne 'defense_pingjia'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_pingjia' or oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_butie')?'block':'none' }">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">第三方评估人员评价</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="thirdPartyEvaluation" htmlEscape="false"
						rows="8" maxlength="2000" class="form-control valid"
						disabled="${oaLegalAidInform.act.taskDefKey ne 'defense_pingjia'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_butie')?'block':'none' }">
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
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_approve' or oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren' )?'block':'none' }">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">填写意见：</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="act.comment" htmlEscape="false" rows="8"
						maxlength="255" class="form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<shiro:hasPermission name="oa:act:oaLegalAidInform:edit">
					<c:choose>
						<c:when
							test="${oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren' or oaLegalAidInform.act.taskDefKey eq 'defense_approve'  }">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="提交" onclick="$('#flag').val('yes')" />
							<input id="btn2" class="btn btn-primary" type="submit" value="驳回"
								onclick="	$('#flag').val('no')" />
							<input id="btnSubmit2" class="btn btn-primary" type="submit"
								value="结束任务" onclick="$('#remarks').val('end')"
								style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren' or oaLegalAidInform.act.taskDefKey eq 'defense_approve' or oaLegalAidInform.act.taskDefKey eq 'defense_confirm')?'inline-block':'none'}" /><br>
						</c:when>
						<c:when
							test="${oaLegalAidInform.act.taskDefKey eq 'defense_confirm' or oaLegalAidInform.act.taskDefKey eq 'defense_lszhuren' or oaLegalAidInform.act.taskDefKey eq 'defense_pingjia' or oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_butie'}">
							<input id="btnSubmit2" class="btn btn-primary" type="submit"
								value="结束任务" onclick="$('#remarks').val('end')"
								style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_fyzhuren' or oaLegalAidInform.act.taskDefKey eq 'defense_approve' or oaLegalAidInform.act.taskDefKey eq 'defense_confirm')?'inline-block':'none'}" /><br>
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="提交" onclick="$('#flag').val('yes')" /><br>
						</c:when>
						<c:when
							test="${oaLegalAidInform.act.taskDefKey eq 'defense_chengbanren_banli' }">
							<input id="btnSubmit6" class="btn btn-primary" type="submit"
								value="保存" onclick="$('#isSubmit').val('0')" />&nbsp;
						<input id="btnSubmit7" class="btn btn-primary" type="submit"
								value="办结"
								onclick="if(confirm('确定此业务已经办结吗?')){$('#flag').val('yes');}" />&nbsp;
					</c:when>

			    </c:choose>
			</shiro:hasPermission>
			<br><input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)" />
			</div>
			</div>
		  <div id="yj" class="reClosely">
			<label class="control-label" style="text-align: left;">填写意见：</label>
			<br />
			<form:textarea path="act.comment" class="closelyTextarea" rows="10"
				maxlength="257" />
			<shiro:hasPermission name="oa:act:oaLegalAidInform:edit">
				<input class="closelyTextareaBtn" type="button" value="确定"
					onclick="returnBack()" />
				<input class="closelyTextareaBan" type="button" value="取消"
					onclick="back('1')" />
			</shiro:hasPermission>
		</div>
		<act:histoicFlow procInsId="${oaLegalAidInform.act.procInsId}" />
	</form:form>
</body>
</html>
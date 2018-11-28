<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>法援通知辩护管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		//打印之前设置IE打印时不打印页眉 、页脚

		$("#btnPrint").click(function() {
			bdhtml = window.document.body.innerHTML;
			sprnstr = "<!--startprint-->"; //开始打印标识字符串有17个字符
			eprnstr = "<!--endprint-->"; //结束打印标识字符串
			prnhtml = bdhtml.substr(bdhtml.indexOf(sprnstr) + 17); //从开始打印标识之后的内容
			prnhtml = prnhtml.substring(0, prnhtml.indexOf(eprnstr)); //截取开始标识和结束标识之间的内容
			window.document.body.innerHTML = prnhtml; //把需要打印的指定内容赋给body.innerHTML
			/* if (!!window.ActiveXObject || "ActiveXObject" in window) {
			       remove_ie_header_and_footer();
			   } */
			window.print(); //调用浏览器的打印功能打印指定区域
			window.location.reload();
			//window.document.body.innerHTML=bdhtml;//重新给页面内容赋值； 
		});
		/* function remove_ie_header_and_footer() {
		   	var hkey_root, hkey_path, hkey_key;
		    hkey_path = "HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
		    try {
		       	var RegWsh = new ActiveXObject("WScript.Shell");
		        RegWsh.RegWrite(hkey_path + "header", "");          //设置页眉为空
		        RegWsh.RegWrite(hkey_path + "footer", "");	//设置页脚为空
		    } catch (e) {}
		} */
	})
</script>
<script type="text/javascript">
	function returnBack() {
		//驳回操作
		if ($.trim($('#inputForm1').find("[name='remarks']").val()) == '') {
			toVail("请填写结束任务意见", "warning");
			return false;
		}
		//设置驳回
		$('#flag').val('no');
		$('#inputForm1').submit();
	}
	function back(type) {
		if (type == 0) {
			$('#yj').show();
		} else {
			$('#yj').hide();
		}
	}
</script>
<!-- 打印的样式-->
<style media="print">
@page {
	size: auto;
	margin: 0mm;
}
</style>
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
	top: 141px;
	left:  43%;
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
	top: 141px;
	left: 50%;
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<c:choose>
			<c:when test="${zt==1 }">
				<li><a href="${ctx}/act/task/all">全部任务</a></li>
			</c:when>
			<c:when test="${zt==2 }">
				<li><a href="${ctx}/act/task/allCreater">我的申请</a></li>
			</c:when>
			<c:when test="${zt!=3 }">
				<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
				<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
				<li><a href="${ctx}/act/task/process/">新建任务</a></li>
				<li class="active"><a href="javascript:;">查看法援通知辩护</a></li>
			</c:when>
			<c:otherwise>
				<li class="active"><a href="javascript:;">查看法援通知辩护</a></li>
			</c:otherwise>
		</c:choose>
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
		<sys:message content="${message}" />
		<!--startprint-->
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
				<div class="form-group transverse">
					<form:select path="sex" class="input-xlarge form-control required"
						disabled="true">
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
						disabled="disabled" />
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
						disabled="disabled" allowClear="false" />
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
						class="input-xlarge form-control required" disabled="true">
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
					<form:select path="internation"
						class="input-xlarge form-control required" disabled="true">
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
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受援人员类别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="renyuanType" class="input-xlarge form-control "
						disabled="true">
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
				<div class="form-group">
					<form:input path="casesum" htmlEscape="false"
						class="input-xlarge form-control required" disabled="true" />
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
					<form:input path="idCard" htmlEscape="false" maxlength="18"
						class="input-xlarge form-control required" disabled="true" />
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
				<label class="control-label">受理日期</label><br />
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="sldate" type="text" disabled="disabled" maxlength="15"
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
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">通知原因</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<form:select path="informReson" class="input-xlarge form-control "
						disabled="true">
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
						uploadPath="/oa/oaLegalAidInform" selectMultiple="true"
						readonly="true" />
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
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">援助方式</label>
				</div>
			</div>

			<div class="col-xs-11">
				<div class="form-group">
					<form:select path="modality" class="input-xlarge form-control "
						disabled="true" >
						<form:option value="请选择"></form:option>
						<form:options items="${fns:getDictList('oa_modalities')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>


		<div class="row">
			<div class="col-xs-1">
				<label class="control-label">审查日期</label>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
					<input name="scdate" type="text" maxlength="15"
						class="form-control required"
						disabled="disabled"
						value="<fmt:formatDate value="${oaLegalAidInform.scdate}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件办理过程</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="caseHandleProcess" htmlEscape="false" rows="8"
						class="form-control valid" disabled="true" />
				</div>
			</div>
		</div>
		<!-- 仅在承办人办理、第三方评价、承办人申领补贴环节显示 -->
		<div class="row">
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
						readonly="${oaLegalAidInform.act.taskDefKey ne 'aid_chengbanren_banli'}" />
				</div>
			</div>
		</div>
		<div class="row"
			style="display:${(oaLegalAidInform.act.taskDefKey eq 'defense_approve') or (oaLegalAidInform.act.taskDefKey eq 'defense_update')?'none':'block'}">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">律师事务所</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<sys:treeselectUser id="lawOffice" name="lawyer.id"
						value="${oaLegalAidInform.lawOffice.id}"
						labelName="lawOffice.name"
						labelValue="${oaLegalAidInform.lawOffice.name}" title="律师"
						url="/sys/office/treeUser?type=3" cssClass="form-control valid"
						setRootId="${officeLawyerOffice}" disabled="disabled"
						hideBtn="false" checked="false" allowInput="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">律师</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<sys:treeselectUser id="lawyer" name="lawyer.id"
						value="${oaLegalAidInform.lawyer.id}" labelName="lawyer.name"
						labelValue="${oaLegalAidInform.lawyer.name}" title="律师"
						url="/sys/office/treeUser?type=3" cssClass="form-control valid"
						setRootId="${officeLawyerOffice}" disabled="disabled"
						notAllowSelectParent="true" checked="false" allowInput="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">第三方评估人员评分</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="thirdPartyScore" htmlEscape="false"
						maxlength="600" class="input-xlarge form-control "
						disabled="${oaLegalAidInform.act.taskDefKey ne 'aid_pingjia'}" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">第三方评估人员评价</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="thirdPartyEvaluation" htmlEscape="false"
						rows="8" maxlength="2000" class="form-control valid"
						disabled="${oaLegalAidInform.act.taskDefKey ne 'aid_pingjia'}" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">承办案件领取补贴信息</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:input path="receiveSubsidy" htmlEscape="false"
						maxlength="200" class="input-xlarge form-control " disabled="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label" style="text-align: left;">案件详情</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
					<form:textarea path="remarks" htmlEscape="false" rows="8"
						class="form-control valid required" />
				</div>
			</div>
		</div>
		<!--endprint-->

		<div class="row">
			<div class="col-xs-12">
				<div class="form-actions">
					<input id="btnSubmit2" class="btn btn-primary" type="button"
						value="结束任务" onclick="back('0')"
						style="display: ${oaLegalAidInform.act.taskDefKey eq 'defense_start'?'inline-block':'none'}" />
					<input id="btnCancel" class="btn btn-primary" type="button"
						value="返 回" onclick="history.go(-1)" />
					<input id="btnPrint" class="btn btn-primary" type="button" value="打印" />
				</div>
			</div>
		</div>
		<act:histoicFlow procInsId="${oaLegalAidInform.act.procInsId}" />
	</form:form>
	<form:form id="inputForm1" modelAttribute="oaLegalAidInform"
		action="${ctx}/oa/act/oaLegalAidInform/endProcess" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="procInsId" />
		<div style="position: relative;">
			<div id="yj" class="reClosely"
				style="display: none; position: absolute; top: -503px; right: 262px;">
				<label class="control-label" style="text-align: left;">结束意见：</label>
				<br />
				<form:textarea path="remarks" class="closelyTextarea" rows="10"
					maxlength="257" placeholder="在这里写入你结束任务意见"/>
				<input class="closelyTextareaBtn" type="button" value="确定"
					onclick="returnBack()" /> <input class="closelyTextareaBan"
					type="button" value="取消" onclick="back('1')" />
			
        </div>
		</div>
	</form:form>
</body>
</html>
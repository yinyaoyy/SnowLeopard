<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法律援助管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	</script>
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
			<li class="active"><a href="javascript:;">查看法律援助申请</a></li>
	      </c:when>
	      <c:otherwise>
	      	<li class="active"><a href="javascript:;">查看法律援助申请</a></li>
	      </c:otherwise>
	    </c:choose>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaLegalAid" action="${ctx}/oa/act/oaLegalAid/toDo" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey" class="taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>	
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
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control required" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<form:select path="sex" class="input-xlarge form-control " disabled="true">
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<form:select path="ethnic" class="input-xlarge form-control " disabled="true">
					<form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLegalAid.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">所在区域</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<sys:treeselect id="area" name="area.id" value="${oaLegalAid.area.id}" 
					labelName="area.name" labelValue="${oaLegalAid.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
				 	disabled="disabled"
					allowClear="false" notAllowSelectParent="false"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<form:input path="postCode" htmlEscape="false" maxlength="6" class="input-xlarge form-control " disabled="true"/>
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
				<form:input path="idCard" htmlEscape="false" maxlength="18" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">所属类型</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<form:select path="aidCategory" class="input-xlarge form-control " disabled="true">
					<form:options items="${fns:getDictList('legal_aid_category')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<form:input path="employer" htmlEscape="false" maxlength="600" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">住所地（经常居住地）</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:input path="address" htmlEscape="false" maxlength="600" class="input-xlarge form-control " disabled="true"/>
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
				<form:input path="domicile" htmlEscape="false" maxlength="600" class="input-xlarge form-control " disabled="true"/>
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
				<form:input path="proxyName" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-4">
				<div class="form-group">
					<p id="showRole">
							<c:forEach items="${fns:getDictList('legal_aid_proxy_type')}" var="pt">
								<label class="checkbox-inline">
						    <input type="radio" name="proxyType" value="${pt.value }" 
				 ${(oaLegalAid.proxyType eq pt.value)?'checked="checked"':''} disabled="disabled"/>${pt.label}
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
				<form:input path="proxyIdCard" htmlEscape="false" maxlength="20" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">案情标题</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:input path="caseTitle" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
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
				<form:select path="caseType" class="input-xlarge form-control " disabled="true">
					<form:options items="${fns:getChildrenDictList('oa_case_classify','legal_aid')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<form:select path="hasMongol" class="input-xlarge form-control " disabled="true">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<form:select path="caseNature" class="input-xlarge form-control " disabled="true">
					<form:options items="${fns:getDictList('case_nature')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">案情及申请理由概述</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:textarea path="caseReason" htmlEscape="false" rows="8" class="form-control valid" disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">案件相关文件</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<input type="hidden" id="caseFile" name="caseFile" value="${oaLegalAid.caseFile}" />
				<sys:ckfinder input="caseFile" type="files" uploadPath="/oa/legalAid" selectMultiple="true"
					readonly="true" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">案件办理过程</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:textarea path="caseHandleProcess" htmlEscape="false" rows="8" class="form-control valid" 
					readonly="${oaLegalAid.act.taskDefKey ne 'aid_chengbanren_banli'}"
				/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">案件相关文件(承办人上传)</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<input type="hidden" id="caseFile2" name="caseFile2" value="${oaLegalAid.caseFile2}" />
				<sys:ckfinder input="caseFile2" type="files" uploadPath="/oa/legalAid" selectMultiple="true"
					readonly="${oaLegalAid.act.taskDefKey ne 'aid_chengbanren_banli'}" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">法援援助中心</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="lawAssistanceOffice.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaLegalAid.lawAssistanceOffice.name}" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">法援律师</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="lawAssistUser.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaLegalAid.lawAssistUser.name}" disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">律师事务所</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="lawOffice.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaLegalAid.lawOffice.name}" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">律师</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="lawyer.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaLegalAid.lawyer.name}" disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row" style="display:${oaLegalAid.legalAidType=='1'?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">基层法律服务所</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="legalOffice.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${legalOffice.lawOffice.name}" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">基层法律服务工作人员</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group">
				<form:input path="legalPerson.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaLegalAid.legalPerson.name}" disabled="true"/>
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">第三方评估人员评分</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:input path="thirdPartyScore" htmlEscape="false" maxlength="600" class="input-xlarge form-control " disabled="${oaLegalAid.act.taskDefKey ne 'aid_pingjia'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">第三方评估人员评价</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:textarea path="thirdPartyEvaluation" htmlEscape="false" rows="8" maxlength="2000" class="form-control valid" disabled="${oaLegalAid.act.taskDefKey ne 'aid_pingjia'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">承办案件领取补贴信息</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<form:input path="receiveSubsidy" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>	
		<div class="row">
			<div class="col-xs-9">
				<div class="form-actions">
					<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
			</div>
		</div>
		<act:histoicFlow procInsId="${oaLegalAid.act.procInsId}" />
	</form:form>
</body>
</html>
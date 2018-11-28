<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>基层法律服务工作者管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {
		                required: true
					},
					idCard: {
		                required: true,
		                rangelength: [1, 18]
					},
					phone: {
		                required: true,
		                rangelength: [1, 11]
					},
					licenseNumber: {
		                required: true
					}
				},
				messages: {
					name: {
		                required: '姓名不能为空'
					},
					idCard: {
		                required: '身份证不能为空且身份证长度为18位'
					},
					phone: {
		                required: '手机号码不能为空且手机号码为11位'
					},
					licenseNumber: {
						required: '执业证号不能为空'
					}
				},
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
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
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/info/legalServicePerson/">基层法律服务工作者列表</a></li>
		<li class="active"><a href="${ctx}/info/legalServicePerson/form?id=${legalServicePerson.id}">基层法律服务工作者<shiro:hasPermission name="info:legalServicePerson:edit">${not empty legalServicePerson.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:legalServicePerson:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="legalServicePerson" action="${ctx}/info/legalServicePerson/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="300" class="input-xlarge form-control required"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">身份证号：</label>
				<form:input path="idCard" onblur="autoBirthdaySexByIdCard(this,'birthday','sex');" htmlEscape="false" maxlength="18" class="input-xlarge form-control required"/>
				</div>
				<span class="help-inline">生日、性别根据身份证号自动补充</span>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">出生年月：</label>
				<input id="birthday" name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${legalServicePerson.birthday}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>			
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">性别：</label>
			<form:select path="sex" disabled="true" class="input-xlarge form-control ">
				<form:option value="" label="">请选择</form:option>
				<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">手机号码：</label>
				<form:input path="phone" htmlEscape="false" maxlength="11" class="input-xlarge form-control required"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属基层法律服务所：</label>
			<sys:treeselectOfficeUser id="legalOffice" name="legalOffice.id" value="${legalServicePerson.legalOffice.id}"
					labelName="legalOffice.name" labelValue="${legalServicePerson.legalOffice.name}" title="基层法律服务所"
					url="/sys/office/getOfficeUser?type=8" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业证号：</label>
				<form:input path="licenseNumber" htmlEscape="false" maxlength="50" class="input-xlarge form-control required"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">资格证号：</label>
				<form:input path="qualificationNo" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">领证时间：</label>
				<input name="certificateDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${legalServicePerson.certificateDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		</div>
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">专职/兼职：</label>
			<form:select path="licenseType" class="input-xlarge form-control ">
				<form:option value="" label="">请选择</form:option>
				<form:options items="${fns:getDictList('legal_service_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">微信号：</label>
				<form:input path="weixinNo" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">QQ号：</label>
				<form:input path="qqNo" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业时间：</label>
				<input name="practisingDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${legalServicePerson.practisingDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主管机关：</label>
				<sys:treeselect id="office" name="office.id" value="${legalServicePerson.office.id}" labelName="office.name" labelValue="${legalServicePerson.office.name}"
					title="科室" url="/sys/office/treeData?type=2" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业行政区划：</label>
				<form:input path="administrativeDivision" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">民族：</label>
			    <form:select path="ethnic" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">学历：</label>
				<form:input path="education" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">政治面貌：</label>
				<form:input path="politicalFace" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">是否精通蒙汉双语：</label>
			    <form:select path="isMongolian" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职务：</label>
			<form:select path="role" class="input-xlarge form-control ">
			    <option value="">请选择</option>
				<form:options items="${fns:getDictList('info_role_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">照片：</label>
			<input type="hidden" id="imageUrl" name="imageUrl" value="${legalServicePerson.imageUrl}" />
			<sys:ckfinder input="imageUrl" type="thumb" uploadPath="/info/legalServicePerson" selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">行业奖励：</label>
				<form:textarea path="industryRewards" htmlEscape="false" rows="8" maxlength="2000" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:textarea path="businessExpertise" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">个人简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注信息：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:legalServicePerson:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
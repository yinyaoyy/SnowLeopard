<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>鉴定人员信息管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    //验证手机
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码");                
		    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /^[1-9]([0-9]{13,13}|[0-9]{16,16})([0-9]|X)$/.test(value);       
		    }, "请输入正确的身份证号");
			$("#inputForm").validate({
				rules:{
					name: {
		                required: true
					},
					expertise:{
		                required: true
					},
					licenseNumber:{
						required: true
					},
					idCard: {
		                checkPaperNum:true
		            }
				},
				messages: {
					name: {
		                required: '姓名不能为空'
					},
					expertise:{
		                required: '业务专长不能为空'
					},
					licenseNumber:{
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
		<li><a href="${ctx}/info/infoForensicPersonnel/">司法鉴定人员管理列表</a></li>
		<li class="active"><a href="${ctx}/info/infoForensicPersonnel/form?id=${infoForensicPersonnel.id}">司法鉴定人员管理<shiro:hasPermission name="info:infoForensicPersonnel:edit">${not empty infoForensicPersonnel.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:infoForensicPersonnel:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="infoForensicPersonnel" action="${ctx}/info/infoForensicPersonnel/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="225" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
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
			<label class="control-label">出生日期：</label>
				<input id="birthday" name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoForensicPersonnel.birthday}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">性别：</label>
				<form:select path="sex" disabled="true" class="input-xlarge form-control required">
					<form:option value=""> 请选择</form:option>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
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
			<label class="control-label">政治面貌：</label>
				<form:input path="politicalFace" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">学历：</label>
				<form:input path="education" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">入行年月，从业时间：</label>
				<input name="practisingTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoForensicPersonnel.practisingTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">司法鉴定所：</label>
			 <sys:treeselectOfficeUser id="judicialAuthentication" name="judicialAuthentication.id" value="${infoForensicPersonnel.judicialAuthentication.id}"
					labelName="judicialAuthentication.name" labelValue="${infoForensicPersonnel.judicialAuthentication.name}" title="鉴定所"
					url="/sys/office/getOfficeUser?type=4" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect id="area" name="area.id" value="${infoForensicPersonnel.area.id}" labelName="area.name" labelValue="${infoForensicPersonnel.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control valid required" allowClear="true" notAllowSelectParent="true"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">证书过期时间：</label>
				<input name="licenseExpiryTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoForensicPersonnel.licenseExpiryTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业证号：</label>
				<form:input path="licenseNumber" htmlEscape="false" maxlength="225" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
	   <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			   <label class="control-label">手机号：</label>
					<form:input path="phone" htmlEscape="false" maxlength="225" class="input-xlarge form-control required"/>
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
				<form:hidden id="imageUrl" path="imageUrl" htmlEscape="false" maxlength="225" class="input-xlarge"/>
				<sys:ckfinder input="imageUrl" type="images" uploadPath="/info/infoForensicPersonnel" selectMultiple="false"/> 
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:textarea path="expertise" htmlEscape="false" rows="8" maxlength="1024" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">说明：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:infoForensicPersonnel:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
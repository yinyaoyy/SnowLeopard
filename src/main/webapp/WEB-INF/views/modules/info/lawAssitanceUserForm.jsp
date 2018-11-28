<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法援中心工作人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//初始化的验证
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    //验证手机
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码");
		    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test(value);       
		    }, "请输入正确的身份证号"); 
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
					  loading('正在提交，请稍等...');
					  form.submit();
				},
				rules: {
					name: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 30]
					},
					
					phone:{
						required:true,
						checkPhoneNum: true
					},
					idCard:{
						required:true,
						checkPaperNum: true
					}
					/* office:{
						required:true
					} */
				},
				messages: {
					name: {
						required: '法援中心名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-30字之间'
					},
					
					phone:{
						required:'手机号码不能为空',
						checkPhoneNum: '请填写正确的手机号码格式'
					},
					idCard:{
						required:'身份证号不能为空',
						checkPaperNum:'请输入正确的身份证号'
					}
					/* office:{
						required:'归属法援中心不能为空'
					} */
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
		<li><a href="${ctx}/info/lawAssitanceUser/">法援中心工作人员列表</a></li>
		<li class="active"><a href="${ctx}/info/lawAssitanceUser/form?id=${lawAssitanceUser.id}">法援中心工作人员<shiro:hasPermission name="info:lawAssitanceUser:edit">${not empty lawAssitanceUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:lawAssitanceUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="lawAssitanceUser" action="${ctx}/info/lawAssitanceUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
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
			<label class="control-label">生日：</label>
				<input id="birthday" name="birthday" type="text" readonly="readonly" maxlength="20" class="required form-control "
					value="<fmt:formatDate value="${lawAssitanceUser.birthday}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">性别：</label>
				<form:select path="sex" disabled="true" class="input-xlarge form-control ">
				   <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="11" class="input-xlarge form-control required"/>
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
			<label class="control-label">学历：</label>
				<form:input path="education" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否蒙语：</label>
				<form:select path="isMongolian" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
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
			<label class="control-label">联系地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			        <label class="control-label">民族：</label>
				<form:select path="nation" class="input-xlarge form-control ">
				   <option value="" selected="selected">请选择</option>
				   <form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			   <label class="control-label">归属法援中心：</label>
			<sys:treeselectOfficeUser id="office" name="office.id" value="${lawAssitanceUser.office.id}"
					labelName="office.name" labelValue="${lawAssitanceUser.office.name}" title="法援中心"
					url="/sys/office/getOfficeUser?type=5" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect id="area" name="area.id" value="${lawAssitanceUser.area.id}" labelName="area.name" labelValue="${lawAssitanceUser.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业证号：</label>
				<form:input path="licenseNumber" htmlEscape="false" minlength="1" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否兼职法援律师：</label>
				<form:select path="isAidLawyer" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">照片：</label>
						<input type="hidden" id="image" name="image" value="${lawAssitanceUser.image}" />
						<sys:ckfinder input="image" type="thumb" uploadPath="/info/lawAssitanceUser" selectMultiple="false" />
					</div>
				</div>
		</div>	
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">个人简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" cols="2"  maxlength="500" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
	<div class="row">
		<div class="form-actions">
			<shiro:hasPermission name="info:lawAssitanceUser:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
	</form:form>
</body>
</html>
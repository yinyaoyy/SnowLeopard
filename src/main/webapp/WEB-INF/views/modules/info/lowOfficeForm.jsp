<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>律师事务所管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//初始化的验证
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "该字段必须是中文");
			jQuery.validator.addMethod('checkEmail', function (value, element) {
		        return this.optional(element) || /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(value);
		    }, "请填写正确的电子邮箱"); 
		    //验证手机
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/.test(value);
		    }, "请填写正确的电话号码");
		    jQuery.validator.addMethod('checkZipCode', function (value, element) {
		        return this.optional(element) || /^[0-9][0-9]{5}$/.test(value);
		    }, "请填写正确的邮政编码");
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
		                checkFullName:true,
		                rangelength: [1, 200]
					},
					phone: {
		                required: true,
		                checkPhoneNum:true
					},
					manager: {
		                required: true,
		                checkFullName:true,
		                rangelength: [1, 200]
					},
					zipCode:{
		            	checkZipCode:true
		            },
		            email:{
		            	checkEmail:true
		            }
				},
				messages: {
					name: {
		                required: '该字段不能为空',
		                rangelength:"长度过长"
					},
					phone: {
		                required: '律所联系电话不能为空',
		                checkPhoneNum:"请填写正确的电话号码"
					},
					manager: {
		                required: '该字段不能为空',
		                rangelength:"长度过长"
					},
		            zipCode:{
		            	checkZipCode:'请填写正确的邮政编码'
		            },
		            email:{
		            	checkEmail:"邮箱格式不正确"
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/info/lowOffice/">律师事务所列表</a></li>
		<li class="active"><a href="${ctx}/info/lowOffice/form?id=${lowOffice.id}">律师事务所<shiro:hasPermission name="info:lowOffice:edit">${not empty lowOffice.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:lowOffice:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="lowOffice" action="${ctx}/info/lowOffice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">律所名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">律所地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="500" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			   <label class="control-label">负责人：</label>
				<form:input path="manager" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="12" class="input-xlarge form-control "/>
				</div>
			</div>
		
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业状态：</label>
				<form:select path="licenseStatus" class="required input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('lawyer_license_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业证号：</label>
				<form:input path="licenseNumber" htmlEscape="false" maxlength="30" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">组织机构代码证：</label>
				<form:input path="organizationCode" htmlEscape="false" maxlength="30" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">批准日期：</label>
				<input name="approvalDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${lowOffice.approvalDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect id="area" name="area.id" value="${lowOffice.area.id}" labelName="area.name" labelValue="${lowOffice.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">批准文号：</label>
				<form:input path="approvalNumber" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主营机关：</label>
				<sys:treeselect id="office" name="office.id" value="${lowOffice.office.id}" labelName="office.name" labelValue="${lowOffice.office.name}"
					title="科室" url="/sys/office/treeData?type=2" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">坐标(经纬度)：</label>
				<sys:bmap id="coordinate" name="coordinate" value="${lowOffice.coordinate}" title="坐标" cssClass=" form-control  valid " />
				</div>
			</div>
		</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">传真号码：</label>
				<form:input path="faxNumber" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">成立时间：</label>
				<input name="establishTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${lowOffice.establishTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">律所网址：</label>
				<form:input path="website" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业时间：</label>
				<input name="practisingTime" type="text" readonly="readonly" maxlength="20" class="required form-control "
					value="<fmt:formatDate value="${lowOffice.practisingTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:input path="expertise" htmlEscape="false" maxlength="600" class="input-xlarge form-control "/>
				</div>
			</div>
		<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">组织形式：</label>
				<form:input path="organizationForm" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">邮政编码：</label>
				<form:input path="zipCode" htmlEscape="false" maxlength="6" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">电子邮箱：</label>
				<form:input path="email" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			</div>
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">图片地址：</label>						
			<input type="hidden" id="imageUrl" name="imageUrl" value="${lowOffice.imageUrl}" />
			<sys:ckfinder input="imageUrl" type="thumb" uploadPath="/info/lowOffice" selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8"  maxlength="500" class="input-xxlarge form-control "/>
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
		<div class="form-actions">
			<shiro:hasPermission name="info:lowOffice:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" style="margin-left: 10px" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button"  style="margin-left: 11px" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
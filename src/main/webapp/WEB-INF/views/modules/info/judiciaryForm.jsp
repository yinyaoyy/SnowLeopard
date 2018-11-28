<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>司法所管理</title>
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
			//$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 20]
		            },
		            
		            
		            phone: {
		                required: true,
		                checkPhoneNum: true
		            }
		            
				},
				messages: {
				  name:{
					    required: '姓名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-20字之间'
					  
				  },
					
				  phone: {
		                required: '手机号码不能为空',
		                checkPhoneNum: '请填写正确的手机号码格式'
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
		<li><a href="${ctx}/info/judiciary/">司法所列表</a></li>
		<li class="active"><a href="${ctx}/info/judiciary/form?id=${judiciary.id}">司法所<shiro:hasPermission name="info:judiciary:edit">${not empty judiciary.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:judiciary:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="judiciary" action="${ctx}/info/judiciary/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">司法所名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">司法所所长：</label>
				<form:input path="chargeUser" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属旗县：</label>
				<sys:treeselect id="area" name="area.id" value="${judiciary.area.id}" labelName="area.name" labelValue="${judiciary.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属乡镇：</label>
				 <sys:treeselect id="town" name="town.id" value="${judiciary.town.id}" labelName="town.name" labelValue="${judiciary.town.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">坐标(经纬度)：</label>
			    <sys:bmap id="coordinate" name="coordinate" value="${judiciary.coordinate}" title="坐标" cssClass=" form-control  valid " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">工作人员数：</label>
				<form:input path="teamSize" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">司法所简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" maxlength="500" class="input-xxlarge form-control "/>
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
			<shiro:hasPermission name="info:judiciary:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
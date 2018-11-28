<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>社区矫正人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
					  loading('正在提交，请稍等...');
					  form.submit();
				},
				rules: {
					name: {
		                required: true
					}
				},
				messages: {
					name: {
		                required: '姓名不能为空'
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
		<li><a href="${ctx}/info/correctUser/">社区矫正人员列表</a></li>
		<li class="active"><a href="${ctx}/info/correctUser/form?id=${correctUser.id}">社区矫正人员<shiro:hasPermission name="info:correctUser:edit">${not empty correctUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:correctUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="correctUser" action="${ctx}/info/correctUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">身份证号：</label>
				<form:input path="idCard" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在司法所：</label>
				<sys:treeselect id="office" name="office.id" value="${correctUser.office.id}" labelName="office.name" labelValue="${correctUser.office.name}"
					title="科室" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			
		</div>
		<div class="row">
		    <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">罪名：</label>
				<form:input path="accusation" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">主刑：</label>
			    <form:select path="mainPenalty" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('info_correct_user_penalty')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">责任人：</label>
				<form:input path="responsibilityName" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
		    <div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">矫正类别：</label>
			    <form:select path="correctType" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('info_correct_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">矫正状态：</label>
			    <form:select path="correctStatus" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('info_correct_user_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">矫正等级：</label>
			    <form:select path="correctLevel" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('info_correct_user_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions"  style="margin-left:10px;">
			<shiro:hasPermission name="info:correctUser:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
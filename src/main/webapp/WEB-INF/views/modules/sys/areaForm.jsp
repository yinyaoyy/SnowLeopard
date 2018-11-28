·<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules : {
					name : {
						required : true,
						rangelength: [0, 20]
					},
					code : {
						required : true,
					},
					type : {
						required : true,
					},
				},
				messages : {
					name: {
		                required: '区域名称不能为空',
		                rangelength:'区域名称必须是0-20字之间',
					},
					code: {
		                required: '区域编码不能为空',
					},
					type: {
						required: '请选择区域类型',
					},
				},
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
					  form.submit(); 
					  loading('正在提交，请稍等...'); 
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
		<li><a href="${ctx}/sys/area/">区域列表</a></li>
		<li class="active"><a href="form?id=${area.id}&parent.id=${area.parent.id}">区域<shiro:hasPermission name="sys:area:edit">${not empty area.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:area:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">上级区域</label>
				<sys:treeselect id="area" name="parent.id" value="${area.parent.id}" labelName="parent.name" labelValue="${area.parent.name}"
					title="区域" url="/sys/area/treeData" extId="${area.id}" cssClass="form-control valid" allowClear="true"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">区域名称</label>
				<form:input path="name" htmlEscape="false" maxlength="20" class="form-control valid"/>
				<span class="help-inline"></span>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">区域编码</label>
				<form:input path="code" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">区域类型</label>
			       <form:select path="type" class="form-control">
			           <form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
					   <form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				   </form:select>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">备注</label>
			<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="200" class="form-control"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<shiro:hasPermission name="sys:area:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</div></div>
		</div>
	</form:form>
</body>
</html>
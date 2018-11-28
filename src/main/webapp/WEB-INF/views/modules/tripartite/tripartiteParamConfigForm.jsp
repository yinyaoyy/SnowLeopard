<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>与第三方系统对接请求头、参数配置表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
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
		<li><a href="${ctx}/tripartite/tripartiteParamConfig/">与第三方系统对接请求头、参数配置表列表</a></li>
		<li class="active"><a href="${ctx}/tripartite/tripartiteParamConfig/form?id=${tripartiteParamConfig.id}">与第三方系统对接请求头、参数配置表<shiro:hasPermission name="tripartite:tripartiteParamConfig:edit">${not empty tripartiteParamConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="tripartite:tripartiteParamConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tripartiteParamConfig" action="${ctx}/tripartite/tripartiteParamConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">类型，字典为tripartite_config_type：</label>
				<form:select path="type" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('tripartite_config_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">上级id(系统或接口id)：</label>
				<form:input path="parent.id" htmlEscape="false" maxlength="32" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请求头或参数名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="300" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">中文说明：</label>
				<form:input path="description" htmlEscape="false" maxlength="300" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">解析默认值的方式tripartite_config_value_type：</label>
				<form:select path="valueType" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('tripartite_config_value_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">参数默认值：</label>
				<form:textarea path="defaultValue" htmlEscape="false" rows="8" maxlength="1000" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注信息：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="1000" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="tripartite:tripartiteParamConfig:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
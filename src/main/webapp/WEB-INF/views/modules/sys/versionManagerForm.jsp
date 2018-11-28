<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统版本管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				rules:{
					version:{
						required: true
					},
					represent:{
						required: true
					},
					 androidUrl:{
						required: true
					} 
					
				},
			  messages:{
				  version:{
					  required:"版本号不能为空"
				  },
				  represent:{
					  required:"描述不能为空"
				  },
				  androidUrl:{
					  required:"安卓页面不能为空"
				  }
				  
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
		<li><a href="${ctx}/sys/versionManager/">系统版本管理列表</a></li>
		<li class="active"><a href="${ctx}/sys/versionManager/form?id=${versionManager.id}">系统版本管理<shiro:hasPermission name="sys:versionManager:edit">${not empty versionManager.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:versionManager:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="versionManager" action="${ctx}/sys/versionManager/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">版本：</label>
				<form:input path="version" htmlEscape="false" maxlength="64" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">描述：</label>
				<form:textarea path="represent" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<%-- <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">ios页面：</label>
				<form:input path="iosUrl" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
		</div> --%>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			   <label class="control-label">ios页面：</label>
			  <input type="hidden" id="iosUrl" name="iosUrl" value="${versionManager.iosUrl}" />
			<sys:ckfinder input="iosUrl" type="thumb" uploadPath="/sys/versionManager" selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			   <label class="control-label">安卓页面：</label>
			  <input type="hidden" id="androidUrl" name="androidUrl" value="${versionManager.androidUrl}" />
			<sys:ckfinder input="androidUrl" type="files" uploadPath="/sys/versionManager" selectMultiple="false" />
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
			<shiro:hasPermission name="sys:versionManager:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
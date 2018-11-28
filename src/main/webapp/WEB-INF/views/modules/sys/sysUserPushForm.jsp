<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>消息推送管理</title>
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
		<li><a href="${ctx}/sys/sysUserPush/">消息推送列表</a></li>
		<li class="active"><a href="${ctx}/sys/sysUserPush/form?id=${sysUserPush.id}">消息推送<shiro:hasPermission name="sys:sysUserPush:edit">${not empty sysUserPush.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysUserPush:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sysUserPush" action="${ctx}/sys/sysUserPush/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
			<label class="control-label">接受人id：</label>
				<form:input path="receiveUserId" htmlEscape="false" maxlength="50" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-6">
				<div class="form-group">
			<label class="control-label">发送人id：</label>
				<form:input path="sendUserId" htmlEscape="false" maxlength="50" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
			<label class="control-label">通知标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
			<label class="control-label">通知内容：</label>
				<form:textarea path="content" htmlEscape="false" rows="8" maxlength="300" class="form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">发送时间：</label>
				<input name="sendTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${sysUserPush.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">发送状态0失败1成功：</label>
				<form:input path="status" htmlEscape="false" maxlength="2" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">area：</label>
				<form:input path="area" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务类型：</label>
				<form:input path="type" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:sysUserPush:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
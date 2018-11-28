<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			  jQuery.validator.addMethod("newPasswordto", function(value,element) {
				  var hh=$("#oldPassword").val();
				 if(hh==value){
					 return false
				 }else{
					 return true
				 }
			 },"新旧密码不能一样") 
			$("#inputForm").validate({
				rules:{
					oldPassword: {
		                required: true,
		                rangelength: [3,30]
		            },
		            newPassword: {
		                required: true,
		                rangelength: [3,30],
		                newPasswordto:true
		            },
		            confirmNewPassword: {
		                required: true,
		                rangelength: [3,30],
		                equalTo: "#newPassword"
		            }
				},
				messages: {
					oldPassword: {
		                required: '旧密码不能为空！',
		                rangelength:'密码长度必须在3—30之间'
		            },
		            newPassword: {
		                required: '新密码不能为空！',
		                rangelength:'密码长度必须在3—30之间'
		            },
		            confirmNewPassword: {
		                required:'确认新密码不能为空!',
		                rangelength:'确认新密码长度必须在3—30之间'
		            },
					confirmNewPassword: {equalTo: '输入与上面相同的密码'}
				},
				 submitHandler: function(form){
					 $(form).find("#btnSubmit").attr("disabled", true);
							var oldPassword = $("#oldPassword").val();
							var newPassword = $("#newPassword").val();
							$.ajax({
								type:"post",
								url:"${ctx}/sys/user/changePwd",
								data:{"oldPassword":oldPassword,"newPassword":newPassword},
								success:function(data){
									var url = data.server_url;
									if(data.status == '1'){
									/* 	toVail(data.message,"success"); */
										alert("密码修改成功，请重新登录！");
										top.window.location.href=url+'/index.html';
									}else{
										toVail(data.message,"error");
										window.location.reload();
									}
									
									
								}
							})
							
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误,请先更正。");
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
		<li><a href="${ctx}/sys/user/info">个人信息</a></li>
		<li class="active"><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
		<li><a href="${ctx}/sys/user/upload">修改头像</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<label class="control-label">旧密码</label>
					<input id="oldPassword" name="oldPassword" type="password" value=""class="form-control" />
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<label class="control-label">新密码</label>
					<input id="newPassword" name="newPassword" type="password" value="" class="form-control" />
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<label class="control-label">确认新密码</label>
					<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" class="form-control" />
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
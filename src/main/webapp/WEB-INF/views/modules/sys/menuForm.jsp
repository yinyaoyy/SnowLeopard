<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {
		                required: true,
		                rangelength: [1, 20]
					}
				},
				messages: {
					name: {
		                required: '名称不能为空',
		                rangelength:'名称必须是1-20字之间'
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
		<li><a href="${ctx}/sys/menu/">菜单列表</a></li>
		<li class="active"><a href="${ctx}/sys/menu/form?id=${menu.id}&parent.id=${menu.parent.id}">菜单<shiro:hasPermission name="sys:menu:edit">${not empty menu.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">上级菜单</label>
				 <sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}"
					title="菜单" url="/sys/menu/treeData" extId="${menu.id}" cssClass="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">名称</label>
				 <form:input path="name" htmlEscape="false" class="form-control valid"/>
				<span class="help-inline"> </span>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">链接</label>
				 <form:input path="href" htmlEscape="false" maxlength="2000" class="form-control valid"/>
				<p class="help-inline addpage-prompt note">点击菜单跳转的页面</p>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">目标</label>
				 <form:input path="target" htmlEscape="false" maxlength="10" class="form-control valid"/>
				<p class="help-inline addpage-prompt note">链接地址打开的目标窗口，默认：pageContent</p>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">图标</label>
					<sys:iconselect id="icon" name="icon" value="${menu.icon}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">排序</label>
				 <form:input path="sort" htmlEscape="false" maxlength="50" class="form-control valid"/>
				<p class="help-inline addpage-prompt note">排列顺序，升序。</p>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">可见</label>
				<p id="showRole">
							<c:forEach items="${fns:getDictList('show_hide')}" var="role">
								<label class="checkbox-inline">
						    <input type="radio" name="isShow" value="${role.value }" 
						            <c:if test="${ menu.isShow==role.value}"> 
						               checked="checked"
						            </c:if>
						    />${role.label}
						    </label>
							</c:forEach>
						</p>
						<p class="help-inline addpage-prompt note" style="margin-top:15px;">该菜单或操作是否显示到系统菜单中</p>
				 <%-- <form:radiobuttons path="isShow" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<p class="help-inline addpage-prompt note">该菜单或操作是否显示到系统菜单中</p> --%>
			</div></div>
		</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">权限标识</label>
				<form:textarea path="permission" htmlEscape="false" rows="8" class="form-control valid"/>
				<img src="${ctxStatic}/images/note.png" style="margin-top:10px;">
				<p class="help-inline addpage-prompt" style="margin-top:-20px;">控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</p>
			</div></div>
		</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">备注</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="200" class="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">语言key</label>
				<form:input path="languageKey" htmlEscape="false" maxlength="100" class="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</div></div>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>应用管理</title>
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
		<li><a href="${ctx}/appmange/appMenu/">应用列表</a></li>
		<li class="active"><a href="${ctx}/appmange/appMenu/form?id=${appMenu.id}">应用<shiro:hasPermission name="appmange:appMenu:edit">${not empty appMenu.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="appmange:appMenu:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="appMenu" action="${ctx}/appmange/appMenu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">应用类型：</label>
				<form:select path="appType" class="input-xlarge form-control required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('app_menu_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">名称：</label>
				<form:input path="title" htmlEscape="false" maxlength="225" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<%--<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">图标链接：</label>
				<form:input path="icon" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>--%>
        <div class="row">
            <div class="col-xs-3">
                <div class="form-group">
                    <label class="control-label">应用图标</label>
                    <form:input path="icon" type="hidden" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
                    <%--<input type="hidden" id="image" name="image" value="${article.imageSrc}" />--%>
                    <sys:ckfinder input="icon" type="thumb" uploadPath="/cms/article" selectMultiple="false" />
                </div>
            </div>
        </div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="11" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">应用的请求链接：</label>
				<form:input path="href" htmlEscape="false" maxlength="225" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">目标(mainFrame、_blank、_self、_parent、_top):</label>
				<form:input path="target" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">显示：</label>
				<form:select path="isShow" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
					<label class="control-label">归属角色</label>
					<p id="showRole">
						<c:forEach items="${allRoles}" var="role">
							<label class="checkbox-inline">
								<input type="checkbox" name="roleIdList" value="${role.id }"
										<c:forEach items="${allHaveRoles}" var="roleMenu" >
											<c:if test="${ roleMenu.roleId == role.id}">
												checked="checked"
											</c:if>
										</c:forEach>
								/>${role.name}
							</label>
						</c:forEach>
						<span class="help-inline"></span>
						<label class="roleList checkbox-inline" style="color: red;float: right;"></label>
					</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">说明：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="appmange:appMenu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
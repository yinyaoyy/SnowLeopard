<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>专题管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									rules : {
										name : {
											required : true,
										//rangelength: [1, 100]
										},
										title : {
											required : true,
										// rangelength: [1,100]
										},
										roleIdList : {
											required : true,
										// rangelength: [1,100]
										}

									},
									messages : {
										name : {
											required : '站点名称不能为空',
											rangelength : '站点名称必须是1-100字之间'
										},
										title : {
											required : '站点标题不能为空',
											rangelength : '站点标题必须是1-100字之间'
										},
										roleIdList : {
											required : '请选择站点归属角色',
										}
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
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
		<li><a href="${ctx}/cms/site/">专题列表</a></li>
		<li class="active"><a href="${ctx}/cms/site/form?id=${site.id}">站点<shiro:hasPermission
					name="cms:site:edit">${not empty site.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="cms:site:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="site"
		action="${ctx}/cms/site/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<label class="control-label">专题名称</label>
					<form:input path="name" htmlEscape="false" maxlength="100"
						class="form-control valid" />
				</div>
			</div>
			<div class="col-xs-6">
				<div class="form-group">
					<label class="control-label">专题标题</label>
					<form:input path="title" htmlEscape="false" maxlength="100"
						class="form-control valid" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">专题Logo</label>
					<form:hidden path="logo" htmlEscape="false" maxlength="255"
						class="input-xlarge" />
					<sys:ckfinder input="logo" type="images" uploadPath="/cms/site" />
					<p class="help-inline addpage-prompt note">建议Logo大小：1000 × 145（像素）</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
					<label class="control-label">描述</label>
					<form:textarea path="description" htmlEscape="false" rows="8"
						maxlength="200" class="form-control valid" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
					<label class="control-label">关键字</label>
					<form:input path="keywords" htmlEscape="false" maxlength="200"
						class="form-control valid" />
					<p class="help-inline addpage-prompt note">填写描述及关键字，有助于搜索引擎优化</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">默认主题</label>
					<form:select path="theme" class="form-control valid">
						<form:options items="${fns:getDictList('cms_theme')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12" >
				<div class="form-group">
					<label class="control-label">归属角色</label>
					<p id="showRole">
						<c:forEach items="${allRoles}" var="role">
							<label class="checkbox-inline" style="width:200px;margin-left:0;"> <input type="checkbox"
								name="roleIdList" value="${role.id }"
								<c:forEach items="${site.roleIdList}" var="roleId" > 
						            <c:if test="${ roleId==role.id}"> 
						               checked="checked"
						            </c:if>
						        </c:forEach> />${role.name}
							</label>
						</c:forEach>
						<span class="help-inline"></span> <label
							class="roleList checkbox-inline"
							style="color: red; float: right;"></label>
					</p>
				</div>
			</div>
		</div>

		<%--  <div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">归属角色</label>
						 <select class="form-control valid" aria-invalid="false" id="role.id" name="role.id">
                           <option value="">请选择角色</option>
                           <c:forEach items="${roleList}" var="role">
                             <option value="${role.id }" <c:if test="${ site.role.id==role.id}">selected</c:if>>${role.name }</option>
                           </c:forEach>
                       </select>
					</div>
				</div>
			</div>
					 --%>
		<%-- <div class="row">
			<div class="col-xs-12"
				style="background: none; color: #2c2e2f; margin-top: 175px;">
				<div class="form-group">
					<label class="control-label">版权信息</label>
					<form:textarea id="copyright" htmlEscape="true" path="copyright"
						rows="4" maxlength="200" class="form-control valid" />
					<sys:ckeditor replace="copyright" uploadPath="/cms/site" />
				</div>
			</div>
		</div> --%>
		<%-- <div class="row">
			<div class="col-xs-3" style="margin-top: 525px;">
				<div class="form-group">
					<label class="control-label">自定义首页视图</label>
					<form:input path="customIndexView" htmlEscape="false"
						maxlength="200" class="form-control valid" />
				</div>
			</div>
		</div> --%>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<shiro:hasPermission name="cms:site:edit">
						<input id="btnSubmit" class="btn btn-primary" type="submit"
							value="保 存" />&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn btn-primary" type="button"
						value="返 回" onclick="history.go(-1)" />
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
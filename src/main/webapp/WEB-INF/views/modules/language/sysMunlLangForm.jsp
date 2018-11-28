<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>语言管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#inputForm")
						.validate(
								{
									rules : {
										langKey : {
											required : true
										},
										langContext : {
											required : true
										},
										operationType : {
											required : true
										},
										attributeType : {
											required : true
										},
										languageAscription : {
											required : true
										},
										langCode : {
											required : true
										},

									},
									messages : {
										langKey : {
											required: '语言key不能为空'
										},
										langContext : {
											required : '内容不能为空'
										},
										operationType : {
											required : '请选择语言归属'
										},
										attributeType : {
											required : '请选择属性分类'
										},
										languageAscription : {
											required : '归属页面不能为空'
										},
										langCode : {
											required : '请选择语言'
										},
									},

									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
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
				$("#btnSubmit").click(
						function() {
							if (checkApk() == true) {
								$("#inputForm").attr("action",
										"${ctx}/language/sysMunlLang/save");
								$("#inputForm").submit();
							} else {
								toVail("语言key值已存在","error");
							}
						});
				function checkApk() {
					var flag = true;
					//语言key
					var key = $(".key").val();
					//语言
					var yy = $("#yy").val();
					var id = $("#id").val();
					if ((id == null || id == "")&&key!=""&&yy!="") {
						$.ajax({//默认加载内容
							type : "post",
							url : jsCtx + "/language/sysMunlLang/isExist",
							async : false,
							data : {
								"key" : key,
								"yy" : yy,
							},
							success : function(data) {
								if (data == 0) {
									flag = false
								}
							}
						});
					}
					return flag;
				}
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/language/sysMunlLang/">语言列表</a></li>
		<li class="active"><a
			href="${ctx}/language/sysMunlLang/form?id=${sysMunlLang.id}">语言<shiro:hasPermission
					name="language:sysMunlLang:edit">${not empty sysMunlLang.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="language:sysMunlLang:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="sysMunlLang" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">语言key</label>
					<c:choose>
						<c:when test="${flag}">
							<form:input path="langKey" htmlEscape="false" maxlength="255"
								class="form-control valid key" disabled="true"/>
						</c:when>
						<c:otherwise>
							<form:input path="langKey" htmlEscape="false" maxlength="255"
								class="form-control valid key"/>
						</c:otherwise>
					</c:choose>
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">内容</label>
					<form:input path="langContext" htmlEscape="false" maxlength="255"
						class="form-control valid nr" />
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<c:choose>
			<c:when test="${flag}">
				<div class="row">
					<div class="col-xs-3">
							<div class="form-group">
								<label class="control-label">语言归属</label>
								<form:select path="operationType" class="form-control valid yy"
									id="operationType" disabled="true">
									<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
									<form:options
										items="${fns:getDictList('language_operation_type')}"
										itemLabel="label" itemValue="value" htmlEscape="false" />
								</form:select>
							</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">属性分类</label>
							<form:select path="attributeType" class="form-control valid yy"
								id="attributeType" disabled="true">
								<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
								<form:options
									items="${fns:getDictList('language_attribute_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">归属页面</label>
							<form:input path="languageAscription" htmlEscape="false"
								maxlength="255" class="form-control valid key "
								list="languageAscriptionList" disabled="true"/>
							<datalist id="languageAscriptionList">
								<c:forEach items="${sysOnlylist}" var="index">
									<option>${index.languageAscription}</option>
								</c:forEach>
							</datalist>
							
							<p class="help-inline addpage-prompt">提示：语言归属建议填写为所属页面的访问地址，如/sys/user</p>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">语言归属</label>
							<form:select path="operationType" class="form-control valid yy"
								id="operationType">
								<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
								<form:options
									items="${fns:getDictList('language_operation_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">属性分类</label>
							<form:select path="attributeType" class="form-control valid yy"
								id="attributeType">
								<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
								<form:options
									items="${fns:getDictList('language_attribute_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">归属页面</label>
							<form:input path="languageAscription" htmlEscape="false"
								maxlength="255" class="form-control valid key "
								list="languageAscriptionList" />
							<datalist id="languageAscriptionList">
								<c:forEach items="${sysOnlylist}" var="index">
									<option>${index.languageAscription}</option>
								</c:forEach>
							</datalist>
							<p class="help-inline addpage-prompt">提示：语言归属建议填写为所属页面的访问地址，如/sys/user</p>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">语言</label>
					<form:select path="langCode" class="form-control valid yy" id="yy">
						<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
						<form:options items="${fns:getDictListByLanguage('act_langtype','CN')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">描述</label>
					<form:textarea path="description" htmlEscape="false" rows="8"
						maxlength="200" class="form-control valid" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<shiro:hasPermission name="language:sysMunlLang:edit">
						<input id="btnSubmit" class="btn btn-primary" type="button"
							value="保 存" />&nbsp;</shiro:hasPermission>
					<input id="btnCancel" class="btn btn-primary" type="button" value="返 回"
						onclick="history.go(-1)" />
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
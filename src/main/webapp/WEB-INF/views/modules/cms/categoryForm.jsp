<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>栏目管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#name").focus();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										$(form).find("#btnSubmit").attr(
												"disabled", true);
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
			});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/category/">栏目列表</a></li>
		<li class="active"><a
			href="${ctx}/cms/category/form?id=${category.id}&parent.id=${category.parent.id}">栏目<shiro:hasPermission
					name="cms:category:edit">${not empty category.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="cms:category:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="category"
		action="${ctx}/cms/category/save" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">归属机构</label>
					<sys:treeselect id="office" name="office.id"
						value="${category.office.id}" labelName="office.name"
						labelValue="${category.office.name}" title="机构"
						url="/sys/office/treeData" cssClass="required form-control valid" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">上级栏目</label>
					<sys:treeselect id="category" name="parent.id"
						value="${category.parent.id}" labelName="parent.name"
						labelValue="${category.parent.name}" title="栏目"
						url="/cms/category/treeData" extId="${category.id}"
						cssClass=" form-control valid" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">栏目模型</label>
					<form:select path="module" class="form-control valid">
						<form:option value=""
							label="${fns:getLanguageContent('sys_select_common_model')}" />
						<form:options items="${fns:getDictList('cms_module')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">栏目名称</label>
					<form:input path="name" htmlEscape="false" maxlength="50"
						class="required form-control valid" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">缩略图</label>
					<form:hidden path="image" htmlEscape="false" maxlength="255"
						class="input-xlarge form-control valid" />
					<sys:ckfinder input="image" type="thumb" uploadPath="/cms/category" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">链接</label>
					<form:input path="href" htmlEscape="false" maxlength="200"
						class="form-control valid" />
					<P class="help-inline addpage-prompt note">栏目超链接地址，优先级“高”</P>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">目标</label>
					<form:input path="target" htmlEscape="false" maxlength="200"
						class="form-control valid" />
					<P class="help-inline addpage-prompt note">栏目超链接打开的目标窗口，新窗口打开，请填写：“_blank”</P>
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
					<P class="help-inline addpage-prompt note">填写描述及关键字，有助于搜索引擎优化</P>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">排序</label>
					<form:input path="sort" htmlEscape="false" maxlength="11"
						class="required digits form-control valid" />
					<P class="help-inline addpage-prompt note">栏目的排列次序</P>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">在导航中显示</label>
					<p id="showRole">
						<c:forEach items="${fns:getDictList('show_hide')}" var="role">
							<label class="checkbox-inline"> <input type="radio"
								name="inMenu" value="${role.value }"
								<c:if test="${ category.inMenu==role.value}"> 
						               checked="checked"
						            </c:if> />${role.label}
							</label>
						</c:forEach>
					</p>
					<P class="help-inline addpage-prompt note"
						style="margin-top: 15px;">是否在导航中显示该栏目</P>

				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">在分类页中显示列表</label>
					<p id="showRole">
						<c:forEach items="${fns:getDictList('show_hide')}" var="role">
							<label class="checkbox-inline"> <input type="radio"
								name="inList" value="${role.value }"
								<c:if test="${ category.inList==role.value}"> 
						               checked="checked"
						            </c:if> />${role.label}
							</label>
						</c:forEach>
					</p>
					<P class="help-inline addpage-prompt note"
						style="margin-top: 15px;">是否在分类页中显示该栏目的文章列表</P>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<label class="control-label" title="默认展现方式：有子栏目显示栏目列表，无子栏目显示内容列表。">展现方式</label>
					<p id="showRole" style="width: 150%">
						<c:forEach items="${fns:getDictList('cms_show_modes')}" var="role">
							<label class="checkbox-inline"> <input type="radio"
								name="showModes" value="${role.value }"
								<c:if test="${ category.showModes==role.value}"> 
						               checked="checked"
						            </c:if> />${role.label}
							</label>
						</c:forEach>
					</p>
				</div>
			</div>
			<div class="col-xs-3" >
				<div class="form-group">
					<label class="control-label">是否允许评论</label>
					<p id="showRole">
						<c:forEach items="${fns:getDictList('yes_no')}" var="role">
							<label class="checkbox-inline"> <input type="radio"
								name="allowComment" value="${role.value }"
								<c:if test="${ category.allowComment==role.value}"> 
						               checked="checked"
						            </c:if> />${role.label}
							</label>
						</c:forEach>
					</p>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">是否需要审核</label>
					<p id="showRole">
						<c:forEach items="${fns:getDictList('yes_no')}" var="role">
							<label class="checkbox-inline"> <input type="radio"
								name="isAudit" value="${role.value }"
								<c:if test="${ category.isAudit==role.value}"> 
						               checked="checked"
						            </c:if> />${role.label}
							</label>
						</c:forEach>
					</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">自定义列表视图</label>
					<form:select path="customListView" class="form-control valid">
						<form:option value=""
							label="${fns:getLanguageContent('sys_select_default')}" />
						<form:options items="${listViewList}" htmlEscape="false" />
					</form:select>
					<P class="help-inline addpage-prompt note">自定义列表视图名称必须以"${category_DEFAULT_TEMPLATE}"开始</P>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">自定义内容视图</label>
					<form:select path="customContentView" class="form-control valid">
						<form:option value=""
							label="${fns:getLanguageContent('sys_select_default')}" />
						<form:options items="${contentViewList}" htmlEscape="false" />
					</form:select>
					<P class="help-inline addpage-prompt note">自定义内容视图名称必须以"${article_DEFAULT_TEMPLATE}"开始</P>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">自定义视图参数</label>
					<form:input path="viewConfig" htmlEscape="true"
						class="form-control valid" />
					<P class="help-inline addpage-prompt note">视图参数例如: {count:2,
						title_show:"yes"}</P>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<shiro:hasPermission name="cms:category:edit">
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
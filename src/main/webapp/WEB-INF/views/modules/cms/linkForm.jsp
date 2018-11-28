<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

	<head>
		<title>链接管理</title>
		<meta name="decorator" content="default" />
		<script type="text/javascript">
			$(document).ready(function() {
				$("#name").focus();
				$("#inputForm").validate({
					submitHandler: function(form) {
						if($("#categoryId").val() == "") {
							$("#categoryName").focus();
							top.$.jBox.tip('请选择归属栏目', 'warning');
						} else {
							loading('正在提交，请稍等...');
							form.submit();
						}
					},
					errorContainer: "#messageBox",
					errorPlacement: function(error, element) {
						$("#messageBox").text("输入有误，请先更正。");
						if(element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
			<li>
				<a href="${ctx}/cms/link/?category.id=${link.category.id}">链接列表</a>
			</li>
			<li class="active">
				<a href="<c:url value='${fns:getAdminPath()}/cms/link/form?id=${link.id}&category.id=${link.category.id}'><c:param name='category.name' value='${link.category.name}'/></c:url>">链接
					<shiro:hasPermission name="cms:link:edit">${not empty link.id?'修改':'添加'}</shiro:hasPermission>
					<shiro:lacksPermission name="cms:link:edit">查看</shiro:lacksPermission>
				</a>
			</li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="link" action="${ctx}/cms/link/save" method="post" class="form-horizontal">
			<form:hidden path="id" />
			<sys:message content="${message}" />
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">归属栏目</label>
						<sys:treeselect id="category" name="category.id" value="${link.category.id}" labelName="category.name" labelValue="${link.category.name}" title="栏目" url="/cms/category/treeData" module="link" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="required form-control valid" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">名称</label>
						<form:input path="title" htmlEscape="false" maxlength="200" class="input-xxlarge required measure-input form-control valid" />
					</div>
				</div>
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">颜色</label>
						<form:select path="color" class="input-mini form-control valid">
							<form:option value="" label="${fns:getLanguageContent('sys_select_default')}" />
							<form:options items="${fns:getDictList('color')}" itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">链接图片</label>
						<form:hidden path="image" htmlEscape="false" maxlength="255" class="input-xlarge form-control valid" />
						<sys:ckfinder input="image" type="images" uploadPath="/cms/link" selectMultiple="false" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">链接地址</label>
						<form:input path="href" htmlEscape="false" maxlength="255" class="input-xxlarge form-control valid" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">权重</label>
						<span style="float:right;">
					 		<p id="showRole" style="margin:0;"><label class="checkbox-inline"><input id="weightTop" type="checkbox" onclick="$('#weight').val(this.checked?'999':'0')"/>置顶</label></p>
						</span>
						<form:input path="weight" htmlEscape="false" maxlength="200" class="input-mini required digits form-control valid" />
					</div>
				</div>
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">过期时间</label>
						<input type="text" class="form-control" name="weightDate" id="weightDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${link.weightDate}' pattern=" yyyy-MM-dd "/>" style="background-color:white;cursor: auto;">
						<p class="help-inline addpage-prompt">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</p>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-4">
					<div class="form-group">
						<label class="control-label">备注</label>
						<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="200" class="form-control valid" />
					</div>
				</div>
			</div>
			<shiro:hasPermission name="cms:article:audit">
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">发布状态</label>
							<p id="showRole">
								<c:forEach items="${fns:getDictList('cms_del_flag')}" var="role">
									<label class="checkbox-inline">
						    <input type="radio" name="delFlag" value="${role.value }" 
						            <c:if test="${ link.delFlag==role.value}"> 
						               checked="checked"
						            </c:if>
						    />${role.label}
						    </label>
								</c:forEach>
							</p>
						</div>
					</div>
				</div>
			</shiro:hasPermission>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<shiro:hasPermission name="cms:link:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
						<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)" />
					</div>
				</div>
			</div>
		</form:form>
	</body>

</html>
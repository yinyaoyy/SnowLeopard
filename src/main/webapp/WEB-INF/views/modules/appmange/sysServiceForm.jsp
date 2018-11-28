<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>web服务管理</title>
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
				rules: {
					name: {
		                required: true,
		                rangelength: [1, 200]
					}
				},
				messages: {
					name: {
		                required: '服务名不能为空',
					}
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
		<li><a href="${ctx}/appmange/sysService/">web服务列表</a></li>
		<li class="active"><a href="${ctx}/appmange/sysService/form?id=${sysService.id}">web服务<shiro:hasPermission name="appmange:sysService:edit">${not empty sysService.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="appmange:sysService:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sysService" action="${ctx}/appmange/sysService/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">服务名：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
			       <label class="control-label">站点：</label>
				    <p id="showRole">
					<c:forEach items="${allSite}" var="site">
						<label class="checkbox-inline">
				      <input type="checkbox" name="siteId" value="${site.id }" 
				        <c:forEach items="${sysService.siteIdList}" var="siteIds" > 
				            <c:if test="${ siteIds==site.id}"> 
				               checked="checked"
				            </c:if>
				        </c:forEach> 
				    />${site.name}
				    </label>
					</c:forEach>
					<span class="help-inline"></span>
					<label class="siteIdList checkbox-inline" style="color: red;float: right;"></label>
				</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
			       <label class="control-label">人员机构：</label>
				    <p id="showRole">
					<c:forEach items="${fns:getDictList('sys_office_category')}" var="site">
						<label class="checkbox-inline">
				      <input type="checkbox" name="officeId" value="${site.value }" 
				        <c:forEach items="${sysService.officeIdList}" var="officeId" > 
				            <c:if test="${ officeId==site.value}"> 
				               checked="checked"
				            </c:if>
				        </c:forEach> 
				    />${site.label}
				    </label>
					</c:forEach>
					<span class="help-inline"></span>
					<label class="officeIdList checkbox-inline" style="color: red;float: right;"></label>
				</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-group">
			       <label class="control-label">oa办公：</label>
				    <p id="showRole">
					<c:forEach items="${fns:getDictList('sys_oa_type')}" var="site">
						<label class="checkbox-inline">
				      <input type="checkbox" name="oaId" value="${site.value }" 
				        <c:forEach items="${sysService.oaIdList}" var="oaId" > 
				            <c:if test="${ oaId==site.value}"> 
				               checked="checked"
				            </c:if>
				        </c:forEach> 
				    />${site.label}
				    </label>
					</c:forEach>
					<span class="help-inline"></span>
					<label class="oaIdList checkbox-inline" style="color: red;float: right;"></label>
				</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="appmange:sysService:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
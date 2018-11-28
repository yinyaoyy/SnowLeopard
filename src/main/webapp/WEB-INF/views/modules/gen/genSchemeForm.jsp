<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>生成方案管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
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
		<li><a href="${ctx}/gen/genScheme/">生成方案列表</a></li>
		<li class="active"><a href="${ctx}/gen/genScheme/form?id=${genScheme.id}">生成方案<shiro:hasPermission name="gen:genScheme:edit">${not empty genScheme.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="gen:genScheme:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="genScheme" action="${ctx}/gen/genScheme/save" method="post" class="form-horizontal">
		<form:hidden path="id"/><form:hidden path="flag"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">方案名称</label>
					<form:input path="name" htmlEscape="false" maxlength="200" class="required form-control valid"/>
				<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">模板分类</label>
					<form:select path="category" class="required input-xlarge form-control valid">
					<form:options items="${config.categoryList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<p class="help-inline addpage-prompt note">
					生成结构：{包名}/{模块名}/{分层(dao,entity,service,web)}/{子模块名}/{java类}
				</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">生成包路径</label>
					<form:input path="packageName" htmlEscape="false" maxlength="500" class="required input-xlarge form-control valid"/>
				<p class="help-inline addpage-prompt note">建议模块包：com.thinkgem.jeesite.modules</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" style="margin-top:2px;">生成模块名</label>
					<form:input style="margin-top:5px;" path="moduleName" htmlEscape="false" maxlength="500" class="required input-xlarge form-control valid"/>
				<span class="help-inline note">可理解为子系统名，例如 sys</span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">生成子模块名</label>
					<form:input path="subModuleName" htmlEscape="false" maxlength="500" class="input-xlarge form-control valid"/>
				<p class="help-inline addpage-prompt note">可选，分层下的文件夹，例如 </p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">生成功能描述</label>
					<form:input path="functionName" htmlEscape="false" maxlength="500" class="required input-xlarge  form-control valid"/>
				<p class="help-inline addpage-prompt note">将设置到类描述</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">生成功能名</label>
					<form:input path="functionNameSimple" htmlEscape="false" maxlength="500" class="required input-xlarge form-control valid"/>
				<p class="help-inline addpage-prompt note">用作功能提示，如：保存“某某”成功</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">生成功能作者</label>
					<form:input path="functionAuthor" htmlEscape="false" maxlength="500" class="required input-xlarge form-control valid"/>
				<p class="help-inline addpage-prompt note">功能开发者</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">业务表名</label>
					<form:select path="genTable.id" class="required input-xlarge form-control valid">
					<form:options items="${tableList}" itemLabel="nameAndComments" itemValue="id" htmlEscape="false"/>
				</form:select>
				<p class="help-inline addpage-prompt note">生成的数据表，一对多情况下请选择主表。</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<label class="control-label">备注</label>
					<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="200" class="form-control valid"/>
				<img src="${ctxStatic}/images/note.png" style="margin-top:-3px;">
				<p class="help-inline addpage-prompt" style="display:inline-block;margin-left:5px;">生成的数据表，一对多情况下请选择主表。</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">生成选项</label>
					 <p id="showRole">
								<label class="checkbox-inline">
						    <input style="margin-top:5px;" type="checkbox" name="replaceFile" value="true"  />是否替换现有文件
						    </label>
						</p> 
				</div>
			</div>
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="gen:genScheme:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存方案" onclick="$('#flag').val('0');"/>&nbsp;
				<input id="btnSubmit" class="btn btn-primary" style="width:125px;" type="submit" value="保存并生成代码" onclick="$('#flag').val('1');"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

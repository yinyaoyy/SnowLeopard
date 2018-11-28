<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate({
				rules: {
					value: {
		                rangelength: [1, 50],
		                required: true
					},
					label: {
		                rangelength: [1, 50],
		                required: true
		            },
		            type: {
		                rangelength: [1, 50],
		                required: true
		            },
		            languageType: {
		                required: true
		            }
				},
				messages: {
					value: {
		                required: '键值不能为空',
		                rangelength:'键值必须是1-50字之间'
					},
					label: {
		                required: '标签不能为空',
		                rangelength:'标签必须是1-50字之间'
					},
					type: {
		                required: '类型不能为空',
		                rangelength:'类型必须是1-50字之间'
					},
					languageType: {
		                required: '请选择语言',
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
		<li><a href="${ctx}/sys/dict/">字典列表</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${dict.id}">字典<shiro:hasPermission name="sys:dict:edit">${not empty dict.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">键值</label>
				<form:input path="value" htmlEscape="false" maxlength="50" class="form-control valid" />
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">标签</label>
				<form:input path="label" htmlEscape="false" class="form-control valid" />
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">类型</label>
				 <c:choose> 
				   <c:when test="${dict.delFlag }"> 
				      <form:input path="type" htmlEscape="false" class="form-control valid"  readonly="true" />
				   </c:when>
				   <c:otherwise> 
				      <form:input path="type" htmlEscape="false" class="form-control valid"  />
				   </c:otherwise>
				 </c:choose>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">描述</label>
				<form:input path="description" htmlEscape="false" class="form-control valid"/>
			</div></div>
		</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">排序</label>
				<form:input path="sort" htmlEscape="false" maxlength="11" class="form-control valid"/>
			</div></div>
		</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
                    <label class="control-label">上级</label>
				<form:select path="parentId" class="form-control valid" >
			       <form:option value="0" label="${fns:getLanguageContent('sys_select_select')}" />
				   <form:options items="${fns:getDictListByLanguage(dict.type,'CN')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>			
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
				<label class="control-label">语言</label>
				<form:select path="languageType" class="form-control valid" >
			     <form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
				<form:options items="${fns:getDictListByLanguage('act_langtype','CN')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<shiro:hasPermission name="sys:dict:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</div></div>
		</div>
	</form:form>
</body>
</html>
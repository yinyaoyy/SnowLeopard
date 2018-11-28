<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				rules : {
					name : {
						required : true
					},
					type : {
						required : true
					},
					grade : {
						required : true
					},

				},
				messages : {
					name : {
						required: '机构名称不能为空'
					},
					type : {
						required : '请选择机构类型'
					},
					grade : {
						required : '请选择机构级别'
					},
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
	<style>
		.form-horizontal .control-label{
			text-align:center;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/office/list?id=${office.parent.id}&parentIds=${office.parentIds}">机构列表</a></li>
		<li class="active"><a href="${ctx}/sys/office/form?id=${office.id}&parent.id=${office.parent.id}">机构<shiro:hasPermission name="sys:office:edit">${not empty office.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:office:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/sys/office/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">上级机构</label>
				<sys:treeselect id="office" name="parent.id" value="${office.parent.id}" labelName="parent.name" labelValue="${office.parent.name}"
					title="机构" url="/sys/office/treeData" extId="${office.id}" cssClass="form-control valid" allowClear="${office.currentUser.admin}"/>
				</div>
			</div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">归属区域</label>
				 <sys:treeselect id="area" name="area.id" value="${office.area.id}" labelName="area.name" labelValue="${office.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">机构名称</label>
				 <form:input path="name" htmlEscape="false" maxlength="50" class="form-control valid"/>
				<span class="help-inline"></span>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">机构编码</label>
				 <form:input path="code" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">机构类型</label>
				 <form:select path="type" id="type" class="form-control valid">
				    <form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
					<form:options items="${fns:getDictList('sys_office_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">机构级别</label>
				 <form:select path="grade" id="grade" class="form-control valid">
				    <form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
					<form:options items="${fns:getDictList('sys_office_grade')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">是否可用</label>
				 <form:select path="useable" class="form-control">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">主负责人</label>
				 <sys:treeselect id="primaryPerson" name="primaryPerson.id" value="${office.primaryPerson.id}" labelName="office.primaryPerson.name" labelValue="${office.primaryPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true"  cssClass="form-control valid"/>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">副负责人</label>
				 <sys:treeselect id="deputyPerson" name="deputyPerson.id" value="${office.deputyPerson.id}" labelName="office.deputyPerson.name" labelValue="${office.deputyPerson.name}"
					title="用户" url="/sys/office/treeData?type=3" allowClear="true" notAllowSelectParent="true" cssClass="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">联系地址</label>
				<form:input path="address" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">邮政编码</label>
				<form:input path="zipCode" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">负责人</label>
				<form:input path="master" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">电话</label>
				<form:input path="phone" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">传真</label>
				<form:input path="fax" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		<!-- </div>
		<div class="row"> -->
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">邮箱</label>
				<form:input path="email" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">备注</label>
				<form:input path="remarks" htmlEscape="false" maxlength="50" class="form-control valid"/>
			</div></div>
		</div>
		<c:if test="${empty office.id}">
		<div class="row">
			<div class="col-xs-7">
				<div class="form-group">
				<label class="control-label">快速添加下级科室</label>
				<p id="showRole">
				<c:forEach items="${fns:getDictList('sys_office_common')}" var="dict">
				  <label class="checkbox-inline">
				  <input style="margin-top:5px;" type="checkbox" name="roleIdList" value="${dict.value }" />
				  ${dict.label }
				  </label>
				</c:forEach>
			</div></div>
		</div>
		</c:if>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<shiro:hasPermission name="sys:office:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</div></div>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>在监服刑人员管理</title>
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
		                required: true
					},
					idCard: {
		                required: true,
		                rangelength: [1, 18]
					},
				},
				messages: {
					name: {
		                required: '姓名不能为空'
					},
					idCard: {
		                required: '身份证不能为空且身份证长度为18位'
					},
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
		<li><a href="${ctx}/info/prisonUser/">在监服刑人员列表</a></li>
		<li class="active"><a href="${ctx}/info/prisonUser/form?id=${prisonUser.id}">在监服刑人员<shiro:hasPermission name="info:prisonUser:edit">${not empty prisonUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:prisonUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="prisonUser" action="${ctx}/info/prisonUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">曾用名：</label>
				<form:input path="beforeName" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">性别：</label>
			<form:select path="sex" class="input-xlarge form-control ">
				<form:option value="" label="">请选择</form:option>
				<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">身份证号：</label>
				<form:input path="idCard" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">出生年月：</label>
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${prisonUser.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">民族：</label>
			    <form:select path="ethnic" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">学历：</label>
				<form:input path="education" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">归属地区：</label>
				<sys:treeselect id="area" name="area.id" value="${prisonUser.area.id}" labelName="area.name" labelValue="${prisonUser.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">核查时间：</label>
				<input name="checkDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${prisonUser.checkDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">是否农村籍：</label>
			    <form:select path="ruralCadastre" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">核查状态：</label>
			    <form:select path="checkState" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('check_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">是否有回执：</label>
			    <form:select path="receipt" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('cms_guestbook_type_inquiries')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在监所：</label>
				<form:input path="prisonName" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">是否有照片：</label>
			    <form:select path="image" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('cms_guestbook_type_inquiries')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">录入时间：</label>
				<input name="enteringDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${prisonUser.enteringDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">户籍地址：</label>
				<form:textarea path="address" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
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
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:prisonUser:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
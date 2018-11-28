<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公证机构管理</title>
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
					manager: {
		                required: true
					},
					licenseNumber: {
		                required: true
					},
				},
				messages: {
					name: {
		                required: '公证处名称不能为空'
					},
					manager: {
		                required: '负责人不能为空'
					},
					licenseNumber: {
						required: '执业证号不能为空'
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
		<li><a href="${ctx}/info/notaryAgency/">公证机构列表</a></li>
		<li class="active"><a href="${ctx}/info/notaryAgency/form?id=${notaryAgency.id}">公证机构<shiro:hasPermission name="info:notaryAgency:edit">${not empty notaryAgency.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:notaryAgency:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="notaryAgency" action="${ctx}/info/notaryAgency/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">公证处名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业证号：</label>
				<form:input path="licenseNumber" htmlEscape="false" maxlength="30" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业时间：</label>
				<input name="practisingTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${notaryAgency.practisingTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">负责人：</label>
				<form:input path="manager" htmlEscape="false" maxlength="200" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">公证处地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="500" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="telephone" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业状态：</label>
				<form:select path="licenseStatus" class="input-xlarge form-control ">
					<form:option value="" label="">请选择</form:option>
					<form:options items="${fns:getDictList('lawyer_license_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">服务时间：</label>
				<form:input path="serviceTime" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">组织机构代码证：</label>
				<form:input path="organizationCode" htmlEscape="false" maxlength="30" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>	
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">批准文号：</label>
				<form:input path="approvalNumber" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">批准日期：</label>
				<%-- <input name="approvalDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${notaryAgency.approvalDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> --%>
				<input id="approvalDate" name="approvalDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${notaryAgency.approvalDate}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主营机关：</label>
				<sys:treeselect id="office" name="office.id" value="${notaryAgency.office.id}" labelName="office.name" labelValue="${notaryAgency.office.name}"
					title="科室" url="/sys/office/treeData?type=2" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect id="area" name="area.id" value="${notaryAgency.area.id}" labelName="area.name" labelValue="${notaryAgency.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">坐标(经纬度)：</label>
				<sys:bmap id="coordinate" name="coordinate" value="${notaryAgency.coordinate}" title="坐标" cssClass=" form-control  valid " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">传真号码：</label>
				<form:input path="faxNumber" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">官网地址：</label>
				<form:input path="website" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">图片地址：</label>						
			<input type="hidden" id="imageUrl" name="imageUrl" value="${notaryAgency.imageUrl}" />
			<sys:ckfinder input="imageUrl" type="thumb" uploadPath="/info/lowOffice" selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:textarea path="expertise" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注信息：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:notaryAgency:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
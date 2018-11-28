<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>基层法律服务所管理管理</title>
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
					personInCharge: {
		                required: true
					},
					practiceLicenseNum: {
		                required: true
					},
					licenseNumber: {
		                required: true
					},
					no: {
		                required: true
					},
					administrativeDivision: {
						required: true,
						rangelength: [0, 6]
					}
				},
				messages: {
					name: {
		                required: '机构名称不能为空'
					},
					personInCharge: {
		                required: '负责人不能为空'
					},
					practiceLicenseNum: {
		                required: '执业许可证号不能为空'
					},
					licenseNumber: {
						required: '组织机构代码证编号不能为空'
					},
					no: {
						required: '机构证号不能为空'
					},
					administrativeDivision: {
						required: '行政区划长度不得超过6位'
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
		<li><a href="${ctx}/info/infoLegalServiceOffice/">基层法律服务所列表</a></li>
		<li class="active"><a href="${ctx}/info/infoLegalServiceOffice/form?id=${infoLegalServiceOffice.id}">基层法律服务所<shiro:hasPermission name="info:infoLegalServiceOffice:edit">${not empty infoLegalServiceOffice.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:infoLegalServiceOffice:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="infoLegalServiceOffice" action="${ctx}/info/infoLegalServiceOffice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="225" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">负责人：</label>
				<form:input path="personInCharge" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">法人：</label>
				<form:input path="legalPerson" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业许可证号：</label>
				<form:input path="practiceLicenseNum" htmlEscape="false" maxlength="225" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业许可证过期时间：</label>
				<input name="practiceLicenseExpiryTime" type="text" readonly="readonly" maxlength="20" class="form-control"
					value="<fmt:formatDate value="${infoLegalServiceOffice.practiceLicenseExpiryTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业状态：</label>
				<form:select path="occupationalState" class="input-xlarge form-control ">
					<form:option value="" label="">请选择</form:option>
					<form:options items="${fns:getDictList('occupational_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">组织机构代码证编号：</label>
				<form:input path="licenseNumber" htmlEscape="false" maxlength="100" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构证号：</label>
				<form:input path="no" htmlEscape="false" maxlength="50" class="input-xlarge form-control"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">组织形式：</label>
				<form:input path="licenseForm" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="11" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">传真：</label>
				<form:input path="fax" htmlEscape="false" maxlength="22" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">行政区划：</label>
				<form:input path="administrativeDivision" htmlEscape="false" maxlength="6" class="input-xlarge form-control"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect id="area" name="area.id" value="${infoLegalServiceOffice.area.id}" labelName="area.name" labelValue="${infoLegalServiceOffice.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid required" allowClear="true" notAllowSelectParent="true"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">乡镇：</label>
				<sys:treeselect id="town" name="town.id" value="${infoLegalServiceOffice.town.id}" labelName="town.name" labelValue="${infoLegalServiceOffice.town.name}"
					title="乡镇" url="/sys/area/treeData" cssClass=" form-control  valid required" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">经纬度(经度在前)：</label>
			<sys:bmap id="coordinate" name="coordinate" value="${infoLegalServiceOffice.coordinate}" title="坐标" cssClass=" form-control  valid " />
				</div>
			</div>
        </div>
        <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">批准文号：</label>
				<form:input path="approvalNumber" htmlEscape="false" maxlength="100" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">批准日期：</label>
				<input name="approvalDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoLegalServiceOffice.approvalDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">显示：</label>
				<form:select path="isShow" class="input-xlarge form-control ">
					<form:option value="" label="">请选择</form:option>
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">工作时间：</label>
				<input name="worktime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoLegalServiceOffice.worktime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">颁证时间：</label>
				<input name="certificationTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoLegalServiceOffice.certificationTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">邮政编码：</label>
				<form:input path="zipCode" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">官网地址：</label>
				<form:input path="officialWebAddress" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">负责人照片：</label>
					<form:hidden path="personInChargeHerf" htmlEscape="false" maxlength="225" class="input-xlarge"/>
                    <sys:ckfinder input="personInChargeHerf" type="thumb" uploadPath="/cms/article" selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
            <div class="col-xs-3">
                <div class="form-group">
                    <label class="control-label">logo：</label>
                    <form:hidden path="logo" htmlEscape="false" maxlength="225" class="input-xlarge"/>
                    <sys:ckfinder input="logo" type="thumb" uploadPath="/cms/article" selectMultiple="false" />
                </div>
            </div>
        </div>
<%--		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">负责人照片：</label>
				<form:input path="personInChargeHerf" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>--%>
		<%--<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业许可证：</label>
				<form:input path="practiceLicense" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>--%>
		<%--<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">组织机构代码证：</label>
				<form:input path="licenseHerf" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>--%>
		<%--<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主管机关id：</label>
				<form:input path="competentAuthoritiesId" htmlEscape="false" maxlength="64" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主管机关名称：</label>
				<form:input path="competentAuthoritiesName" htmlEscape="false" maxlength="64" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>--%>
		
		<%--<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="11" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>--%>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">地址：</label>
				<form:textarea path="address" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" maxlength="2250" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<%--<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属站点id：</label>
				<form:select path="siteId" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属pc端服务id：</label>
				<form:select path="sysServiceId" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属移动端服务id：</label>
				<form:select path="appMenuId" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>--%>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:textarea path="businessExpertise" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">说明：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:infoLegalServiceOffice:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
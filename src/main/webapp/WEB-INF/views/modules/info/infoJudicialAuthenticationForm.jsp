<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>司法鉴定所管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {
		                required: true
					},
					legalPersonMobile:{
						 required: true,
			                rangelength: [1, 11]
					},
					chargePersonMobile:{
						 required: true,
			                rangelength: [1, 11]
					},
					phone: {
		                required: true,
		                rangelength: [1, 11]
					},
					practiceLicenseNum: {
		                required: true
					},
					registeredAuthority:{
						required: true
					},
					address:{
						required: true
					},
					businessExpertise:{
						required: true
					}
					
				},
				messages: {
					name: {
		                required: '名称不能为空'
					},
					legalPersonMobile:{
						required:'法人手机号码不能为空且手机号码为11位'
					},
					phone: {
		                required: '联系电话不能为空且手机号码为11位'
					},
					chargePersonMobile:{
						required: '负责人手机号码不能为空且手机号码为11位'
					},
					practiceLicenseNum: {
						required: '执业证号不能为空'
					},
					registeredAuthority:{
						required: '登记管理部门名称不能为空'
					},
					address:{
						required: '地址不能为空'
					},
					businessExpertise:{
						required: '业务专长不能为空'
					}
					
				},
				
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
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
		<li><a href="${ctx}/info/infoJudicialAuthentication/">司法鉴定所管理列表</a></li>
		<li class="active"><a href="${ctx}/info/infoJudicialAuthentication/form?id=${infoJudicialAuthentication.id}">司法鉴定所管理<shiro:hasPermission name="info:infoJudicialAuthentication:edit">${not empty infoJudicialAuthentication.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:infoJudicialAuthentication:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="infoJudicialAuthentication" action="${ctx}/info/infoJudicialAuthentication/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="225" class="required input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="22" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">工作时间：</label>
				<form:input path="worktime" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">颁证日期：</label>
				<input name="certificateDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoJudicialAuthentication.certificateDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">委托管理部门：</label>
				<form:input path="entrustManagement" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">登记管理部门名称：</label>
				<form:input path="registeredAuthority" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构性质：</label>
				<form:input path="institutionalNature" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
				<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">法人性别：</label>
				<form:select path="legalPersonSex" class="input-xlarge form-control ">
					<form:option value="" label="">请选择</form:option>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">法人手机号码：</label>
				<form:input path="legalPersonMobile" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">统一社会信用代码：</label>
				<form:input path="unifiedCode" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">传真：</label>
				<form:input path="fax" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">邮编：</label>
				<form:input path="zipCode" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		
	<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
					<sys:treeselect id="area" name="area.id" value="${infoJudicialAuthentication.area.id}" labelName="area.name" labelValue="${infoJudicialAuthentication.area.name}"
									title="区域" url="/sys/area/treeData" cssClass="required form-control" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">经纬度(经度在前)：</label>
			<sys:bmap id="coordinate" name="coordinate" value="${infoJudicialAuthentication.coordinate}" title="坐标" cssClass=" form-control  valid " />
				</div>
            </div>
            <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职业状态：</label>
				<form:select path="occupationalState" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('occupational_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">团队规模：</label>
				<form:input path="teamSize" htmlEscape="false" maxlength="11" class="required input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业许可证号：</label>
				<form:input path="practiceLicenseNum" htmlEscape="false" maxlength="225" class="required input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">成立时间：</label>
				<input name="foundingTime" type="text" readonly="readonly" maxlength="20" class="form-control required"
					value="<fmt:formatDate value="${infoJudicialAuthentication.foundingTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构有效开始日期：</label>
				<input name="effectiveDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoJudicialAuthentication.effectiveDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构有限截止日期：</label>
				<input name="deadline" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoJudicialAuthentication.deadline}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">首次获准登记日期：</label>
				<input name="firstRegistration" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${infoJudicialAuthentication.firstRegistration}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		</div>

		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">颁证机关：</label>
				<form:input path="certificateAuthority" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构负责人姓名：</label>
				<form:input path="chargePerson" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构负责人性别：</label>
				<form:select path="chargePersonSex" class="input-xlarge form-control ">
					<form:option value="" label="">请选择</form:option>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			
		</div>
		<div class="row">
		    <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">负责人手机号码：</label>
				<form:input path="chargePersonMobile" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		    <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">官网地址：</label>
				<form:input path="officialWebAddress" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
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
        </div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">地址：</label>
					<form:textarea path="address" htmlEscape="false" rows="8" maxlength="225" class="required input-xxlarge form-control "/>
				</div>
			</div>
        </div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:textarea path="businessExpertise" htmlEscape="false" rows="8" maxlength="512" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		
		<div class="row">
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务范围：</label>
				<form:textarea path="scopeOfBusiness" htmlEscape="false" rows="8" maxlength="1024" class="input-xxlarge form-control "/>
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
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">说明：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		 	<div class="row">
		  <div class="col-xs-3">
                <div class="form-group">
                    <label class="control-label">logo：</label>
                    <form:input type = "hidden" path="log" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
                    <sys:ckfinder input="log" type="thumb" uploadPath="/cms/article" selectMultiple="false" />
                </div>
            </div>	
			</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:infoJudicialAuthentication:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>律师信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//初始化的验证
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test(value);       
		    }, "请输入正确的身份证号");  
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
		                checkFullName:true,
		                rangelength: [1, 200]
					},
					idCard: {
		                required: true,
		                checkPaperNum:true
		                
		            }
				},
				messages: {
					name: {
		                required: '律师名称名不能为空'
		                
					},
					idCard: {
		                required: '身份证号码不能为空'
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
		<li><a href="${ctx}/info/lawyer/">律师信息列表</a></li>
		<li class="active"><a href="${ctx}/info/lawyer/form?id=${lawyer.id}">律师信息<shiro:hasPermission name="info:lawyer:edit">${not empty lawyer.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:lawyer:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="lawyer" action="${ctx}/info/lawyer/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">律师名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">身份证号：</label>
				<form:input path="idCard" onblur="autoBirthdaySexByIdCard(this,'birthday','sex');" htmlEscape="false" maxlength="18" class="input-xlarge form-control required"/>
				</div>
				<span class="help-inline">生日、性别根据身份证号自动补充</span>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">生日：</label>
				<input id="birthday" name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${lawyer.birthday}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">性别：</label>
			       <form:select path="sex" disabled="true" class="input-xlarge form-control ">
			       <option value="" selected="selected">请选择</option>
				      <form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			       </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">民族：</label>
				<form:select path="ethnic" class="input-xlarge form-control required">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">政治面貌：</label>
				<form:input path="politicalFace" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
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
			<label class="control-label">电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="15" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">业务专长：</label>
				<form:input path="expertise" htmlEscape="false" maxlength="600" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业机构：</label>
			<sys:treeselectOfficeUser id="lawOffice" name="lawOffice.id" value="${lawyer.lawOffice.id}"
					labelName="lawOffice.name" labelValue="${lawyer.lawOffice.name}" title="律师事务所"
					url="/sys/office/getOfficeUser?type=2" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业证号：</label>
				<form:input path="licenseNumber" htmlEscape="false" maxlength="30" class="input-xlarge form-control required"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		<%-- <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业类别：</label>
			<form:select path="licenseType" class="input-xlarge form-control ">
			   <option value="" selected="selected">请选择</option>
				<form:options items="${fns:getDictList('lawyer_license_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
		</div> --%>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect id="area" name="area.id" value="${lawyer.area.id}" labelName="area.name" labelValue="${lawyer.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业状态：</label>
			<form:select path="licenseStatus" class="input-xlarge form-control ">
			    <option value="">请选择</option>
				<form:options items="${fns:getDictList('lawyer_license_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主营机关：</label>
				<sys:treeselect id="office" name="office.id" value="${lawyer.office.id}" labelName="office.name" labelValue="${lawyer.office.name}"
					title="科室" url="/sys/office/treeData?type=2" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">执业时间：</label>
				<input name="practisingTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${lawyer.practisingTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职务：</label>
			<form:select path="role" class="input-xlarge form-control ">
			    <option value="">请选择</option>
				<form:options items="${fns:getDictList('info_role_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			   <label class="control-label">图片地址：</label>
			  <input type="hidden" id="imageUrl" name="imageUrl" value="${lawyer.imageUrl}" />
			<sys:ckfinder input="imageUrl" type="thumb" uploadPath="/info/lawyer" selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注信息：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" cols="2" maxlength="255" class="input-xxlarge form-control "/>

				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="info:lawyer:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
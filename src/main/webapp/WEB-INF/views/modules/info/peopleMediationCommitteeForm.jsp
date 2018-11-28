<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人民调解委员会管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码");
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "机构名称必须是中文");
			jQuery.validator.addMethod('checkName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "机构名称必须是中文");
			jQuery.validator.addMethod('checkZipCode', function (value, element) {
		        return this.optional(element) || /^[0-9][0-9]{5}$/.test(value);
		    }, "请填写正确的邮政编码");
			$("#inputForm").validate({
				rules: {
					name: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 20]
		            },
		            office:{
		            	required: true
		            },
		            phone: {
		                required: true,
		                checkPhoneNum: true
		            },
		            chargeUser: {
		                required: true,
		                checkName: true,
		                rangelength: [2, 20]
		            },
		            establishTime:{
		            	required: true
		            },
		            zipCode:{
		            	required: true,
		            	checkZipCode:true
		            }
				},
				messages: {
					name: {	
		                required: '机构名不能为空',
		                checkFullName: '机构名称必须是中文',
		                rangelength: '机构名称必须是2-20字之间'
		            },
		            office:{
		            	required: "主营机关不能为空"
		            },
		            phone: {
		                required: '手机号码不能为空',
		                checkPhoneNum: '请填写正确的手机号码格式'
		            },
		            chargeUser: {
		                required: '负责人不能为空',
		                checkName: '负责人名字必须是中文',
		                rangelength: [2, 20]
		            },
		            establishTime:{
		            	required: "成立时间不能为空"
		            },
		            zipCode:{
		            	required: '邮政编码不能为空',
		            	checkZipCode:'请填写正确的邮政编码'
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
		<li><a href="${ctx}/info/peopleMediationCommittee/">人民调解委员会列表</a></li>
		<li class="active"><a href="${ctx}/info/peopleMediationCommittee/form?id=${peopleMediationCommittee.id}">人民调解委员会<shiro:hasPermission name="info:peopleMediationCommittee:edit">${not empty peopleMediationCommittee.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:peopleMediationCommittee:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="peopleMediationCommittee" action="${ctx}/info/peopleMediationCommittee/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">管理体制：</label>
				<form:input path="organizationForm" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">负责人：</label>
				<form:input path="chargeUser" htmlEscape="false" maxlength="64" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">简称：</label>
				<form:input path="abbreviation" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="500" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属乡镇：</label>
				<%-- <form:input path="belongtowns" htmlEscape="false" maxlength="500" class="input-xlarge form-control "/> --%>
				<sys:treeselect id="belongtowns" name="belongtowns.id" value="${peopleMediationCommittee.belongtowns.id}" labelName="belongtowns.name" labelValue="${peopleMediationCommittee.belongtowns.name}"
					title="所属乡镇" url="/sys/area/treeData" cssClass="required form-control" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		
		
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">主营机关：</label>
				<sys:treeselect id="office" name="office.id" value="${peopleMediationCommittee.office.id}" labelName="office.name" labelValue="${peopleMediationCommittee.office.name}"
					title="科室" url="/sys/office/treeData?type=2" cssClass="required form-control" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">邮编：</label>
				<form:input path="zipCode" htmlEscape="false" maxlength="6" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		
		
		
		
		<div class="row">
			
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">传真号码：</label>
				<form:input path="faxNumber" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">设立地点：</label>
		
				<%-- <form:input path="establishArea" htmlEscape="false" maxlength="64" class="input-xlarge form-control required"/> --%>
			    <sys:treeselect id="establishArea" name="establishArea" value="${peopleMediationCommittee.establishArea}" labelName="establishArea" labelValue="${peopleMediationCommittee.establishArea}"
					title="所属乡镇" url="/sys/area/treeData" cssClass="required form-control" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">成立时间：</label>
				<input name="establishTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${peopleMediationCommittee.establishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
		</div>
		
		
		
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属地区：</label>
				<sys:treeselect id="area" name="area.id" value="${peopleMediationCommittee.area.id}" labelName="area.name" labelValue="${peopleMediationCommittee.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">坐标(经纬度)：</label>
			  <sys:bmap id="coordinate" name="coordinate" value="${peopleMediationCommittee.coordinate}" title="坐标" cssClass="required form-control  valid " />
				<%-- <form:input path="coordinate" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/> --%>
				</div>
			</div>
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">行政区划：</label>
				<form:input path="administrativeDivision" htmlEscape="false" maxlength="6" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		</div>
		
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">图片地址：</label>
				<input type="hidden" id="imageUrl" name="imageUrl" value="${peopleMediationCommittee.imageUrl}" />
			    <sys:ckfinder input="imageUrl" type="thumb" uploadPath="/info/peopleMediationCommittee" selectMultiple="false" />
				</div>
			</div>
			</div>
			
			
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				<%-- <form:input path="introduction" htmlEscape="false" class="input-xlarge form-control "/> --%>
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
			<shiro:hasPermission name="info:peopleMediationCommittee:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
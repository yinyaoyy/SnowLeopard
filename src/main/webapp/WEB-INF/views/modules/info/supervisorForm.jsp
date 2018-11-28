<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人民监督员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    //验证手机
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码");                
		    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test(value);       
		    }, "请输入正确的身份证号"); 
			$("#inputForm").validate({
				rules:{
					xrName: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 20]
		            },
		            phone: {
		                required: true,
		                checkPhoneNum: true
		            },
		            idno: {
		                required: true,
		                checkPaperNum: true
		            },
		            mailbox:{
		            	email:true
		            }
				},
			    messages:{
			    	xrName:{
			    		required: '姓名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-20字之间'
			    	},
			    	phone: {
		                required: '手机号码不能为空',
		                checkPhoneNum: '请填写正确的手机号码格式'
		            },
		            idno:{
		            	required: '身份证号不能为空',
		                checkPaperNum: '请输入正确的身份证号'
		            },
		            mailbox:{
		            	email:'请输入正确的邮箱格式'
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
		<li><a href="${ctx}/info/supervisor/">人民监督员列表</a></li>
		<li class="active"><a href="${ctx}/info/supervisor/form?id=${supervisor.id}">人民监督员<shiro:hasPermission name="info:supervisor:edit">${not empty supervisor.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:supervisor:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="supervisor" action="${ctx}/info/supervisor/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">选任单位：</label>
				<sys:treeselect id="office" name="office.id" value="${supervisor.name.id}" labelName="office.name" labelValue="${supervisor.name.name}"
					title="选任单位" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">人民监督员姓名：</label>
				<form:input path="xrName" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">性别：</label>
			<form:select path="sex" class="input-xlarge form-control ">
			<form:option value=""> 请选择</form:option>
				<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">报名方式：</label>
				<form:input path="appllication" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
				<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">监督员生日：</label>
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${supervisor.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
		 <div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">民族：</label>
			    <form:input path="nation" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
			   <%--  <form:select path="nation" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select> --%>
				</div>
			</div> 
		</div>
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">政治面貌：</label>
				<form:input path="politicsStatus" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">监督员学历：</label>
				<form:input path="educationBackground" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">毕业院校：</label>
				<form:input path="graduateInstitutions" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">学历专业：</label>
				<form:input path="major" htmlEscape="false" maxlength="32" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">监督员身份证号：</label>
				<form:input path="idno" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
				<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">手机号：</label>
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">qq号：</label>
				<form:input path="qqNo" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">监督员座机：</label>
				<form:input path="telephone" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">地址邮编：</label>
				<form:input path="zipcode" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">邮箱：</label>
				<form:input path="mailbox" htmlEscape="false" maxlength="16" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">籍贯省：</label>
			<sys:treeselect id="nativeProvince" name="nativeProvince.id" value="${supervisor.nativeProvince.id}" labelName="nativeProvince.name" labelValue="${supervisor.nativeProvince.name}"
					title="省" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">籍贯市：</label>
				<sys:treeselect id="nativeCity" name="nativeCity.id" value="${supervisor.nativeCity.id}" labelName="nativeCity.name" labelValue="${supervisor.nativeCity.name}"
					title="市" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<%-- <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">籍贯县：</label>
				<form:input path="nativeCounty" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div> --%>
				<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">籍贯县：</label>
				<sys:treeselect id="nativeCounty" name="nativeCounty.id" value="${supervisor.nativeCounty.id}" labelName="nativeCounty.name" labelValue="${supervisor.nativeCounty.name}"
					title="县" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">籍贯详细：</label>
				<form:input path="nativeTowns" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">经常居住省：</label>
			
			<sys:treeselect id="oftenProvince" name="oftenProvince.id" value="${supervisor.oftenProvince.id}" labelName="oftenProvince.name" labelValue="${supervisor.oftenProvince.name}"
					title="省" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<%-- <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">照片地址：</label>
				<form:input path="photograph" htmlEscape="false" maxlength="64" class="input-xlarge form-control "/>
				</div>
			</div> --%>
		</div>
		<div class="row">
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">经常居住市：</label>
				<sys:treeselect id="oftenCity" name="oftenCity.id" value="${supervisor.oftenCity.id}" labelName="oftenCity.name" labelValue="${supervisor.oftenCity.name}"
					title="市" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">经常居住县区：</label>
				<sys:treeselect id="oftenCounty" name="oftenCounty.id" value="${supervisor.oftenCounty.id}" labelName="oftenCounty.name" labelValue="${supervisor.oftenCounty.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">经常居住详细：</label>
				<form:input path="oftenTowns" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">工作证号：</label>
				<form:input path="jobNo" htmlEscape="false" maxlength="16" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">工作地点：</label>
				<form:input path="company" htmlEscape="false" maxlength="32" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">工作单位性质：</label>
				<form:input path="companyNature" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
			
		</div>
		<div class="row">
		    <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职业类别：</label>
				<form:input path="job" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职位：</label>
				<form:input path="position" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职务：</label>
				<form:input path="duty" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职称：</label>
				<form:input path="technicalPost" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职称级别：</label>
				<form:input path="postTech" htmlEscape="false" maxlength="6" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否是人大代表：</label>
				<form:input path="representative" htmlEscape="false" maxlength="1" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否是政协委员：</label>
				<form:input path="cppcc" htmlEscape="false" maxlength="1" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否是公职：</label>
				<form:input path="civilServant" htmlEscape="false" maxlength="1" class="input-xlarge form-control "/>
				</div>
			</div>
			
		</div>
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			      <label class="control-label">头像路径：</label>
					<input type="hidden" id="photograph" name="photograph" value="${supervisor.photograph}" />
			         <sys:ckfinder input="photograph" type="thumb" uploadPath="/cms/supervisor" selectMultiple="false" />
				</div>
			</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">个人简介：</label>
			<form:textarea path="resume" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">个人荣誉：</label>
						<form:textarea path="honor" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">家庭成员：</label>
			<form:textarea path="member" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">成员简介：</label>
						<form:textarea path="memberJj" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">成员级别：</label>
          <form:textarea path="memberPost" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			
		</div>
		<div class="form-actions" style="margin-left:10px;">
			<shiro:hasPermission name="info:supervisor:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
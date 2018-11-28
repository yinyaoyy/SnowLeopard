<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var jianzhiList=[
	              <c:forEach items="${user.partTimeList}" var="role">
	              {
	               roleId:"${role.roleId}",
	               roleName:"${role.roleName}",
	               officeId:"${role.officeId}",
	               officeName:"${role.officeName}",
	               isMain:"${role.isMain}"},
	            </c:forEach>];
	var roleList=[
		              <c:forEach items="${user.roleList}" var="role">
		              {
		               roleId:"${role.id}",
		               roleName:"${role.name}",
		               isMain:"${role.isMain}"},
		            </c:forEach>];
	
	console.log(roleList);
		$(document).ready(function() {
			
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    //验证手机
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码");
		    jQuery.validator.addMethod("checkQQ", function(value, element) {       
		         return this.optional(element) || /^[1-9][0-9]{4,9}$/.test(value);       
		    }, "请添加正确的QQ号");
		    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test(value);       
		    }, "请输入正确的身份证号");  
			$("#inputForm").validate({
				rules: {
					name: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 20]
		            },
		            birthday: {
		                required: true,
		                date: true
		            },
		            papernum: {
		                required: true,
		                checkPaperNum: true
		            },
		            mobile: {
		                required: true,
		                checkPhoneNum: true
		            },
		            'userExpand.otherLinks': {
		                required: true,
		                checkPhoneNum: true
		            },
		            address: {
		                required: true,
		                rangelength: [3, 50]
		            },
		            email: {
		                required: true,
		                email: true
		            },
		            'userExpand.qq': {
		                checkQQ: true
		            }
				},
				messages: {
					name: {	
		                required: '姓名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-20字之间'
		            },
		            birthday: {
		                required: '出生日期不能为空',
		                rangelength: '请选择正确的时间格式'
		            },
		            papernum: {
		                required: '身份证号不能为空',
		                checkPaperNum: '请输入正确的身份证号'
		            },
		            mobile: {
		                required: '手机号码不能为空',
		                checkPhoneNum: '请填写正确的手机号码格式'
		            },
		            'userExpand.otherLinks': {
		                required: '其他联系方式不能为空',
		                checkPhoneNum: '请填写正确的联系翻方式格式'
		            },
		            address: {
		                required: '地址不能为空',
		                rangelength: '长度在3-50字之间'
		            },
		            email: {
		                required: '邮箱地址不能为空',
		                email: '请填写正确的邮箱格式'
		            },
		            'userExpand.qq': {
		                required: 'QQ不能为空',
		                checkQQ:'请填写正确的QQ号'
		            }
				},
				submitHandler: function(form){
					/* if($("#areaId").val()==""){
						toVail("所在区域不能为空","warning");
						return;
					}
					else{
						loading('正在提交，请稍等...');
						form.submit();
					} */
					 $(form).find("#modifyUserInfo").attr("disabled", true);
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
				},
				/* focusInvalid: true, */
				/* success:function(e)
				{
		    		$('#modifyUserInfo').attr('disabled','false');
				} */

			});
			/* $('#modifyUserInfo').bind('click',function(){
				$('#inputForm').submit();
				$(this).attr('disabled','disabled');
			}) */
			
		});
		
	</script>
	<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/userImg/css/cropper.min.css" />
	<link href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css" rel='stylesheet' type="text/css"/>
	<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css" rel='stylesheet' type="text/css"/>
	<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-forms.css" rel='stylesheet' type="text/css"/>
	<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css" rel='stylesheet' type="text/css"/>
	<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-skins.css" rel='stylesheet' type="text/css"/>
	<link href="${ctxStatic}/flavr/css/flavr.css" type="text/css" rel="stylesheet"/>
	<style>
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/user/info">个人信息</a></li>
		<li><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
		<li><a href="${ctx}/sys/user/upload">修改头像</a></li>
	</ul><br/>
	<div class="tab-content">
		<form:form id="inputForm" modelAttribute="user"  method="post" class="form-horizontal">
		<sys:message content="${message}"/>
			<div class="row">
				<div class="col-xs-12 bg-info bg-info-title">基本信息</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="full_name">姓名</label>
						<form:input path="name" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
			<label class="control-label">生日：</label>
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="papernum">身份证号</label>
						<input name="papernum" id="papernum" value="${user.papernum}"  onblur="autoBirthdaySexByIdCard(this,'birthday','sex');" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div>
				<div class="col-md-3">
				<div class="form-group">
			       <label class="control-label">民族：</label>
				    <form:select path="userExpand.ethnic" class="input-xlarge form-control ">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			</div>
			<div class="row">
			   <div class="col-md-3">
				<div class="form-group">
			       <label class="control-label">性别：</label>
				    <form:select path="userExpand.sex" id="sex" class="input-xlarge form-control ">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			</div>
			<div class="row">
				<div class="col-xs-12 bg-info bg-info-title">联系信息</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="mobile">手机号码</label>
						<form:input path="mobile" htmlEscape="false" maxlength="50" class="form-control valid sj" aria-required="true" aria-invalid="false"/>
						<label id="name-errore" class="error2" style="color: red;"></label>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="otherLinks">其他联系方式</label>
						<form:input path="userExpand.otherLinks" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
				</div>
				<%-- <div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="email">邮箱</label>
						<form:input path="email" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="qq">QQ</label>
						<form:input path="userExpand.qq" htmlEscape="false" maxlength="50" class="form-control qq"/>
						<label id="name-erroree" class="error2" style="color: red;"></label></div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="weChat">微信</label>
						<form:input path="userExpand.weChat" htmlEscape="false" maxlength="50" class="form-control valid" aria-invalid="false"/>
					</div>
				</div>
			--%>
			</div> 
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label" for="address">地址</label>
						<form:input path="address" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="userExpand.nativePlace">籍贯</label>
						<form:input path="userExpand.nativePlace" htmlEscape="false" maxlength="50" class="form-control"/>
						</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 bg-info bg-info-title">机构信息</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="department">归属机构</label>
						<input class="form-control" name="department" id="department" value="${user.company.name}" disabled="disabled">
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="department">归属科室</label>
						<input class="form-control" name="department" id="department" value="${user.office.name}" disabled="disabled">
					</div>
				</div>
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">所在区域</label>
				<sys:treeselect id="area" name="area.id" value="${user.area.id}" labelName="area.name" labelValue="${user.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control" allowClear="false" notAllowSelectParent="false"/>
					</div>
				</div>
				<%-- <div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="job">用户角色</label>
						<input class="form-control" name="job" id="job" value="${user.roleNames}" disabled="disabled">
					</div>
				</div> --%>
			</div>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="department">职位</label>
						<form:input path="userType" htmlEscape="false" maxlength="50" class="form-control"/>
					</div>
				</div>
				 
			</div>
			<div class="row">
				<div class="col-xs-12 bg-info bg-info-title">教育信息</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="schoolInfo">学校信息</label>
						<form:input path="userExpand.schoolInfo" htmlEscape="false" maxlength="50" class="form-control"/>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="major">专业</label>
						<form:input path="userExpand.major" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div>
				<div class="col-md-3">
					<div class="form-group">
						<label class="control-label" for="education">学历</label>
						<form:input path="userExpand.education" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12 bg-info bg-info-title">权限信息</div>
			</div>
			<div class="row">
				<div class="col-md-4">
					<div class="form-group">
						<label class="control-label" for="job">主职</label>
						<input class="form-control" name="job" id="job"  disabled="disabled" />
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label" for="jz">其他职位</label>
						<input class="form-control" name="jz" id="jz"  disabled="disabled">
					</div>
				</div> 
			</div>
			<div class="row">
			
				<div class="col-xs-3">
				<div class="form-group">
			        <label class="control-label">是否显示电话号码：</label>
				<form:select path="isShow" class="input-xlarge form-control ">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			</div>
			<div class="col-xs-6" id="partTimeHtml" style="padding-bottom:20px"></div>
			<div class="row" style="padding-top: 20px;">
				<div class="col-md-6">
					<div class="form-group">
						<button class="btn btn-primary" id="modifyUserInfo" type="submit">&nbsp;&nbsp;&nbsp;&nbsp;提&nbsp;&nbsp;交&nbsp;&nbsp;&nbsp;&nbsp;</button>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<script type="text/javascript">
var a = "";
$.each(jianzhiList,function(i,val){
	a+=val.officeName+":"+val.roleName+","
});
var b;
if(a!=null&&a!=""&&a.length>0){
	var b = a.substring(0,a.length-1);
}else{
	b=a;
}
$("#jz").val(b);
var c = "";
$.each(roleList,function(i,val){
	if(val.isMain=='1'){
		return true;
	}else{
		c+=val.roleName+","
	}
});
var d;
if(c!=null&&c!=""&&c.length>0){
	var d = c.substring(0,c.length-1);
}else{
	d=c;
}
$("#job").val(d);
console.log(d);
</script>
</body>

</html>
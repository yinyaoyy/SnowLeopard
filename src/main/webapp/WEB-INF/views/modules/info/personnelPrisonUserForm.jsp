<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>安置帮教人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			//初始化的验证
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
			jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test(value);       
		    }, "请输入正确的身份证号"); 
			$("#inputForm").validate({
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
					  loading('正在提交，请稍等...');
					  form.submit();
				},
				rules: {
					name: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 30]
					},
					sex:{
						required:true
					},
					
					idCard:{
						required:true,
						checkPaperNum: true
					},
					nation:{
						required:true
					},
					residenceAddress:{
						required:true
					}
					
					
				},
				messages: {
					name: {
						required: '安置帮教人员名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-30字之间'
					},
					sex:{
						required: '性别不能为空'
					},
					
					idCard:{
						required:'身份证号不能为空',
						checkPaperNum:'请输入正确的身份证号'
					},
					nation:{
						required:'民族不能为空'
					},
					residenceAddress:{
						required:'户籍地址不能为空'
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
		<li><a href="${ctx}/info/personnelPrisonUser/">在册安置帮教人员列表</a></li>
		<li class="active"><a href="${ctx}/info/personnelPrisonUser/form?id=${personnelPrisonUser.id}">在册安置帮教人员<shiro:hasPermission name="info:personnelPrisonUser:edit">${not empty personnelPrisonUser.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:personnelPrisonUser:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="personnelPrisonUser" action="${ctx}/info/personnelPrisonUser/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">安置人员姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">曾用名：</label>
				<form:input path="usedName" htmlEscape="false" maxlength="50" class="input-xlarge form-control "/>
				</div>
			</div>
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">性别：</label>
				<form:select path="sex" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">身份证号：</label>
				<form:input path="idCard" htmlEscape="false" maxlength="18" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">出生日期：</label>
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${personnelPrisonUser.birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">民族：</label>
				<form:select path="nation" class="input-xlarge form-control ">
					<form:option value="" label=""/>
					<option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">文化水平：</label>
				<form:input path="education" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否农村：</label>
				<form:select path="isCountry" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		
		
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">释放时间：</label>
				<input name="releaseTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${personnelPrisonUser.releaseTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
		</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否累犯：</label>
				<form:select path="isRecidivism" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">吸毒史：</label>
				<form:select path="drugUse" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('cms_guestbook_type_inquiries')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			
			<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否参加职业技能培训：</label>
				<form:select path="istrain" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			</div>
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否获得职业技能证书：</label>
				<form:select path="isSkill" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			
			
			
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否心理健康：</label>
				<form:select path="isMentalHealth" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">家庭联系情况：</label>
				<form:input path="familyTies" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否三无人员：</label>
				<form:select path="isThreePerson" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			
			
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否衔接：</label>
				<form:select path="isConnect" class="input-xlarge form-control ">
				    <option value="" selected="selected">请选择</option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">危险性评估：</label>
				<form:input path="riskAssessment" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">改造表现：</label>
				<form:input path="reconstruction" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">认罪态度：</label>
				<form:input path="attitude" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属旗县：</label>
				<sys:treeselect id="area" name="area.id" value="${personnelPrisonUser.area.id}" labelName="area.name" labelValue="${personnelPrisonUser.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		
			
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">录入时间：</label>
				<input name="entryTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${personnelPrisonUser.entryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				</div>
			</div>
		 
		<div class="row" >
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">户籍地址：</label>
				<form:input path="residenceAddress" htmlEscape="false"  maxlength="255" style="width:540px"  class="input-xlarge form-control "/>
				</div>
			</div>
			
		</div> 
			
		
		
		
		<%-- <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">户籍地址：</label>
				<form:textarea path="residenceAddress" htmlEscape="false" rows="8"  maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div> --%>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">服刑期间特殊情况备注及帮教建议：</label>
				<form:textarea path="circumstances" htmlEscape="false" rows="8"  maxlength="255" class="input-xxlarge form-control "/>
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
			<shiro:hasPermission name="info:personnelPrisonUser:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
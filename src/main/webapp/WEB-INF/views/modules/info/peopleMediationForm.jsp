<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人民调解员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    //验证手机
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码");
		    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
		         return this.optional(element) || /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(value);       
		    }, "请输入正确的身份证号"); 

			$("#inputForm").validate({
				rules: {
					name: {
		                required: true,
		                checkFullName: true,
		                rangelength: [2, 20]
		            },
		            phone: {
		                required: true,
		                checkPhoneNum: true
		            },
		            
		            idCard: {
		                required: true,
		                checkPaperNum: true
		            },
		            startTime:{
		            	required: true
		            },
		           /*   office:{
		            	required: true
		            }, */
		            area:{
		            	required: true
		            }, 
		            birthday:{
		            	required: true
		            }
				},
				messages: {
					name: {	
		                required: '姓名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-20字之间'
		            },
		            phone: {
		                required: '手机号码不能为空',
		                checkPhoneNum: '请填写正确的手机号码格式'
		            },
		            idCard: {
		                required: '身份证号不能为空'
		            },
		            startTime:{
		            	required:"执业时间不能为空"
		            },
		           /*  office:{
		            	required:"所属调委会不能为空"
		            }, */
		           area:{
		            	required:"地区不能为空"
		            }, 
		            birthday:{
		            	required:"生日不能为空"
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
		<li><a href="${ctx}/info/peopleMediation/">人民调解员列表</a></li>
		<li class="active"><a href="${ctx}/info/peopleMediation/form?id=${peopleMediation.id}">人民调解员<shiro:hasPermission name="info:peopleMediation:edit">${not empty peopleMediation.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:peopleMediation:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="peopleMediation" action="${ctx}/info/peopleMediation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">调解员名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="20" class="input-xlarge form-control"/>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">身份证号：</label>
				<form:input path="idCard" onblur="autoBirthdaySexByIdCard(this,'birthday','sex');" htmlEscape="false" maxlength="18" class="input-xlarge form-control "/>
				</div>
				<span class="help-inline">生日、性别根据身份证号自动补充</span>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">生日：</label>
				<input id="birthday" name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${peopleMediation.birthday}" pattern="yyyy-MM-dd"/>" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">性别：</label>
			    <form:select path="sex" disabled="true" class="input-xlarge form-control ">
				      <form:option value="请选择"></form:option>
				      <form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">民族：</label>
				<form:select path="ethnic" class="input-xlarge form-control ">
				      <form:option value="请选择"></form:option>
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
			<label class="control-label">调解员类型：</label>
				<form:select path="mediatorType" class="input-xlarge form-control ">
				      <form:option value="请选择"></form:option>
				      <form:options items="${fns:getDictList('mediator_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职务：</label>
				<form:input path="roleId" htmlEscape="false" maxlength="64" class="input-xlarge form-control "/>
				</div>
			</div>
		
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">开始执业时间：</label>
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${peopleMediation.startTime}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否兼职：</label>
				 <form:select path="isPluralistic" class="input-xlarge form-control " htmlEscape="false" maxlength="4">
				      <form:option value="请选择"></form:option>
				      <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属调委会：</label>
			<sys:treeselectOfficeUser id="office" name="office.id" value="${peopleMediation.office.id}"
					labelName="office.name" labelValue="${peopleMediation.office.name}" title="调委会"
					url="/sys/office/getOfficeUser?type=10" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在机构编号：</label>
				<form:input path="agencyNo" htmlEscape="false" maxlength="40" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">政治面貌：</label>
				<form:input path="politicalFace" htmlEscape="false" maxlength="30" class="input-xlarge form-control "/>
				</div>
			</div>
		
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">地区：</label>
				<sys:treeselect id="area" name="area.id" value="${peopleMediation.area.id}" labelName="area.name" labelValue="${peopleMediation.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control" allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		
		<div class="row">
			
			
			
			<%-- <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">图片地址：</label>
				<form:input path="imageUrl" htmlEscape="false" class="input-xlarge form-control "/>
				</div>
			</div>
			 --%>
			
	
		    <div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">是否精通蒙汉双语：</label>
			    <form:select path="isMongolian" class="input-xlarge form-control ">
				      <form:option value="请选择"></form:option>
				      <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
		</div>
		<div class="row">
		<div class="col-xs-3">
				<div class="form-group">
			      <label class="control-label">头像路径：</label>
					<input type="hidden" id="imageUrl" name="imageUrl" value="${peopleMediation.imageUrl}" />
			         <sys:ckfinder input="imageUrl" type="thumb" uploadPath="/info/peopleMediation" selectMultiple="false" />
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">个人简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" maxlength="255" class="input-xlarge form-control "/>
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
			<shiro:hasPermission name="info:peopleMediation:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
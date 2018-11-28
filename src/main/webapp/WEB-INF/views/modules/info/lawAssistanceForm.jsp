<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法援中心管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			jQuery.validator.addMethod('checkFullName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");
		    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
		        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
		    }, "请填写正确的手机号码"); 
		    jQuery.validator.addMethod('checkZipCodeNum', function (value, element) {
		        return this.optional(element) || /^[0-9]\d{5}(?!\d)$/.test(value);
		    }, "请填写正确的邮政编码");
		    jQuery.validator.addMethod('checkName', function (value, element) {
		        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
		    }, "姓名必须是中文");

			$("#inputForm").validate({
				rules: {
					name: {
		                required: true,
		                checkFullName:true,
		                rangelength: [2, 20]
					},
					phone: {
		                required: true, 
		                //checkPhoneNum: true
		            },
		            zipcode: {
		                required: true,
		                checkZipCodeNum:true,
		            },
		            manager:{
		            	required: true,
		            	checkName:true,
		            	rangelength: [2, 10]
		            }
				},
				messages: {
					name: {
						required: '法援中心名不能为空',
		                checkUserName: '姓名必须是中文',
		                rangelength: '姓名必须是2-20字之间'
					},
					phone: {
		                required: '联系电话不能为空',/*, 未考虑座机格式，暂不验证*/
		              //  checkPhoneNum: '请填写正确的手机号码格式'
		             },
		            zipcode: {
		                required:'邮政编码不能为空',
		                checkZipCodeNum:'请填写正确的邮政编码'
		            },
		            manager:{
		            	required:'负责人不能为空',
		            	checkName:'姓名必须是中文',
		            	rangelength: [2, 10]
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
		<li><a href="${ctx}/info/lawAssistance/">法援中心列表</a></li>
		<li class="active"><a href="${ctx}/info/lawAssistance/form?id=${lawAssistance.id}">法援中心<shiro:hasPermission name="info:lawAssistance:edit">${not empty lawAssistance.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="info:lawAssistance:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="lawAssistance" action="${ctx}/info/lawAssistance/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control "/>
				</div>
			</div>
            <div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">电话：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>	
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">办公时间：</label>
			      <form:input path="worktime" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>		
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">邮政编码：</label>
				<form:input path="zipcode" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系电话：</label>
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">联系地址：</label>
				<form:input path="address" htmlEscape="false" maxlength="100" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">负责人：</label>
				<form:input path="manager" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">人员数量：</label>
				<form:input path="teamSize" htmlEscape="false" maxlength="20" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所在地区：</label>
				<sys:treeselect  id="area" name="area.id" value="${lawAssistance.area.id}" labelName="area.name" labelValue="${lawAssistance.area.name}"
					title="区域" url="/sys/area/treeData" cssClass="required form-control  valid " allowClear="true" notAllowSelectParent="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">坐标(经纬度)：</label>
				<sys:bmap id="coordinate" name="coordinate" value="${lawAssistance.coordinate}" title="坐标" cssClass="required form-control  valid " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">机构简介：</label>
				<form:textarea path="introduction" htmlEscape="false" rows="8" maxlength="500" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">图标</label>
						<input type="hidden" id="image" name="img" value="${lawAssistance.img}" />
						<sys:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false" />
					</div>
				</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="info:lawAssistance:edit"><input id="btnSubmit" class="btn btn-primary"   type="submit"  style="margin-left:11px;width:80px" value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" style="margin-left:12px;width:80px;background-color: #ddd" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
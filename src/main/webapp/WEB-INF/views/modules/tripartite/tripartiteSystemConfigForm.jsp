<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>第三方系统对接配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if($('#type').val()!=1 && $('#parent\\.id').val()==''){
						toVail('请选择上级', 'warning');
						return false;
					}
					loading('正在提交，请稍等...');
					form.submit();
				},
				rules: {
					value: {
		                required: true
					}
				},
				messages: {
					value: {
		                required: '请填写内容'
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
			//根据系统选择上级
			$('#systemId').change(function(){
				$('#parent\\.id').html('');
				if($(this).val()==''){
					$('#parent\\.id').html('<option value="">无</option>');
				}
				else{//补充上级信息
					$.post("${ctx}/tripartite/tripartiteSystemConfig/getApiNameBySystem",
							{"systemId":$(this).val()},
							function(data){//(data至少包含此系统，所以不会为空)
								var options = '';
								for(var i=0;i<data.length;i++){
									options += '<option value="'+data[i].id+'">'+data[i].description+'</option>';
								}
								//重置上级信息
								$('#parent\\.id').html(options);
								//重置前置任务
								options = '<option value="">无</option>';
								for(var i=1;i<data.length;i++){
									options += '<option value="'+data[i].id+'">'+data[i].description+'</option>';
								}
								$('#beforeTask\\.id').html(options);
							});
					
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/tripartite/tripartiteSystemConfig/">第三方系统对接配置列表</a></li>
		<li class="active"><a href="${ctx}/tripartite/tripartiteSystemConfig/form?id=${tripartiteSystemConfig.id}">第三方系统对接配置<shiro:hasPermission name="tripartite:tripartiteSystemConfig:edit">${not empty tripartiteSystemConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="tripartite:tripartiteSystemConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="tripartiteSystemConfig" action="${ctx}/tripartite/tripartiteSystemConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">系统：</label>
				<form:select path="systemId" class="input-xlarge form-control required">
					<form:option value="" label="无"/>
					<form:options items="${systemList }" itemLabel="description" itemValue="id" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
			<label class="control-label">上级：</label>
				<form:select path="parent.id" class="input-xlarge form-control required">
					<form:option value="" label="无"/>
					<c:if test="${taskList != null }">
					<form:options items="${taskList }" itemLabel="description" itemValue="id"/>
					</c:if>
				</form:select>
				</div>
		</div>	
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">类型：</label>
				<form:select path="type" class="input-xlarge form-control required">
					<form:options items="${fns:getDictList('tripartite_config_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">是否暂停执行：</label>
				<form:select path="isPause" class="input-xlarge form-control required">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label"
					 selected="selected" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">系统/接口地址：</label>
				<form:input path="value" htmlEscape="false" maxlength="300" class="input-xlarge form-control required"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">中文说明：</label>
				<form:input path="description" htmlEscape="false" maxlength="300" class="input-xlarge form-control required"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请求方式：</label>
				<form:input path="method" htmlEscape="false" maxlength="300" class="input-xlarge form-control required"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">处理类名：</label>
				<form:input path="serviceName" htmlEscape="false" maxlength="300" class="input-xlarge form-control required"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请求频率(默认1天)：</label>
				<form:input path="requestRate" htmlEscape="false" maxlength="3" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">上次请求时间：</label>
				<input name="lastRequestDate" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${tripartiteSystemConfig.lastRequestDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:true});"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">前置任务：</label>
				<form:select path="beforeTask.id" class="input-xlarge form-control ">
					<form:option value="" label="无"/>
					<form:options items="${beforeList }" itemLabel="description" itemValue="id"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注信息：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="1000" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="tripartite:tripartiteSystemConfig:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
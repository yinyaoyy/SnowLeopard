<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>三定方案管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.reClosely {
		position: relative;
		width: 60%;
		margin-top: 5px;
		margin-left: 9px;
		height: 150px;
		display: none;
		margin-bottom: 20px;
	}
	
	.reClosely .closelyTextarea {
		width: 60%;
		height: 100%;
		box-sizing: border-box;
		resize: none;
		padding: 20px 100px 5px 5px;
	}
	
	.reClosely .closelyTextareaBtn {
		position: absolute;
		background: black;
		color: white;
		margin-right: 15px;
		text-align: center;
		width: 55px;
		height: 30px;
		line-height: 20px;
		cursor: pointer;
		top: 90px;
		left: 50%;
	}
	
	.reClosely .closelyTextareaBan {
		position: absolute;
		background: black;
		color: white;
		margin-right: 15px;
		text-align: center;
		width: 55px;
		height: 30px;
		line-height: 20px;
		cursor: pointer;
		top: 130px;
		left: 50%;
	}
	
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		//$("#name").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				$(form).find("#btnSubmit").attr("disabled", true);
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
			}
		});
	});
	function returnBack(){
		//驳回操作
		if($.trim($('#act\\.comment').val())==''){
			toVail("请填写退回意见","warning");
			return false;
		}  
		//设置驳回
		$('#flag').val('no');
		$('#inputForm').submit();
	}
	function back(type){
		if(type == 0){
			$('#yj').show();
		}else{
			$('#yj').hide();
		}
	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a href="${ctx}/oa/oaAgreement/form?id=${oaAgreement.id}">办理三定方案</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaAgreement" action="${ctx}/oa/oaAgreement/toDo" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey" class="taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			    <label class="control-label">类型：</label>
			    <form:select path="type" class="input-xlarge form-control ">
			    	  <form:option value="" label="">请选择</form:option>
				      <form:options items="${fns:getDictList('oa_agreement_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			    </form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属机构：</label>
				<form:input id="office" path="office.id" value="${oaAgreement.office.name}" labelValue="${oaAgreement.office.name}" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">所属地区：</label>
				<form:input id="area" path="area.id" value="${oaAgreement.area.name}" labelValue="${oaAgreement.area.name}" htmlEscape="false" maxlength="200" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="control-group">
					<label class="control-label">附件</label>
					<div class="controls">
						<form:hidden id="files" path="files" htmlEscape="false"
							maxlength="255" class="input-xlarge" />
						<sys:ckfinder input="files" type="files" uploadPath="/oa/notify"
							selectMultiple="true" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">内容：</label>
				<form:textarea path="content" htmlEscape="false" rows="8" maxlength="2000" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">备注：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<%-- <div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">退回意见：</label>
				<form:textarea path="act.comment" name="com" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div> --%>
		<div class="row">
		    <div class="col-xs-9">
			<shiro:hasPermission name="oa:oaAgreement:edit">
				<c:choose>
					<c:when test="${oaAgreement.act.taskDefKey eq 'agreement_approve'}">
						<input id="btnSubmit" class="btn btn-primary" type="submit" value="通过" onclick="$('#flag').val('yes')"/>&nbsp;
						<input id="btn2" class="btn btn-primary" type="button" value="退回" onclick="back('0')"/>&nbsp;
					</c:when>
			    </c:choose>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		    </div>
		</div>
		<div id="yj" class="reClosely">
			<label class="control-label" style="text-align:left;">填写退回意见：</label>
			<br/>
			<form:textarea path="act.comment" class="closelyTextarea" rows="10" maxlength="257"/>
			<input class="closelyTextareaBtn" type="button" value="确定" onclick="returnBack()"/>
			<input class="closelyTextareaBan" type="button" value="取消" onclick="back('1')"/>
		</div>
		<act:histoicFlow procInsId="${oaAgreement.act.procInsId}" />
	</form:form>
</body>
</html>
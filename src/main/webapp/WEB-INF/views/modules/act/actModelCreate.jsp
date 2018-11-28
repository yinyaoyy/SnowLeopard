<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>新建模型 - 模型管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			$("#inputForm").validate({
				rules : {
					name : {
						required : true
					},
					key : {
						required : true
					},

				},
				messages : {
					name : {
						required: '模型名称不能为空'
					},
					key : {
						required: '模型标识不能为空'
					},
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
					setTimeout(function(){location='${ctx}/act/model/'}, 1000);
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
		function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/model/">模型管理</a></li>
		<li class="active"><a href="${ctx}/act/model/create">新建模型</a></li>
	</ul><br/>
	<sys:message content="${message}"/>
	<form id="inputForm" action="${ctx}/act/model/create" target="_blank" method="post" class="form-horizontal">
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">流程分类</label>
				<select id="category" name="category" class="form-control valid">
					<c:forEach items="${fns:getDictList('act_category')}" var="dict">
						<option value="${dict.value}">${dict.label}</option>
					</c:forEach>
				</select>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">模型名称</label>
				<input id="name" name="name" type="text" class="form-control valid name" />
				<span class="help-inline"></span>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">模型标识</label>
				<input id="key" name="key" type="text" class="form-control valid key" />
				<span class="help-inline"></span>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<label class="control-label">模型描述</label>
				<textarea id="description" name="description" rows="8" class="form-control valid"></textarea>
			</div></div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="提 交"/>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
			</div></div>
		</div>
	</form>
</body>
</html>

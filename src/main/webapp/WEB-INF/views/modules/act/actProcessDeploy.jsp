<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>部署流程 - 流程管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
								});
			});
</script>
<style type="text/css">
.file {
    position: relative;
    display: inline-block;
    background:#2c2e2f;
    border: 1px solid #f5f5f5;
    border-radius: 4px;
    padding: 7px 7px;
    overflow: hidden;
    color: #f5f5f5;
    text-decoration: none;
    text-indent: 0;
    line-height: 20px;
    /* margin-left: 193px; */
}
.file input {
    position: absolute;
    font-size: 100px;
    right: 0;
    top: 0;
    opacity: 0;
}
.file:hover {
    background: #2c2e2f;
    border-color: #78C3F3;
    color: #f5f5f5;
    text-decoration: none;
}
.importFile{
    width: 79%;
   
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/process/">流程管理</a></li>
		<li class="active"><a href="${ctx}/act/process/deploy/">部署流程</a></li>
		<li><a href="${ctx}/act/process/running/">运行中的流程</a></li>
	</ul>
	<br />
	<sys:message content="${message}" />
	<form id="inputForm" action="${ctx}/act/process/deploy" method="post"
		enctype="multipart/form-data" class="form-horizontal">
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">流程分类</label> 
					<select id="category" name="category" class="form-control">
						<c:forEach items="${fns:getDictList('act_category')}" var="dict">
							<option value="${dict.value}">${dict.label}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group" style="margin-top:20px;">
					<label class="control-label">流程文件</label>
					    <!-- <p ><a href="javascript:;" class="file" style="width:75px;border-left:1px solid #ccc;">选择文件
						    <input type="file" id="file" name="file" class="form-control a-upload" onchange="changeFileName()" >
						</a><span id="fileName" style="position: relative;top: -11px;"></span></p>
						 -->
						<sys:importFile inputCssClass="importFile" ></sys:importFile>
						<%-- <img src="${ctxStatic}/images/note.png" style="margin-top:10px;"> --%>
						<p class="help-inline addpage-prompt note" style="margin-top:-18px;">支持文件格式：zip、bar、bpmn、bpmn20.xml</p>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<input id="btnSubmit" class="btn btn-primary" type="submit"
						value="提 交" /> <input id="btnCancel" class="btn btn-primary" type="button"
						value="返 回" onclick="history.go(-1)" />
				</div>
			</div>
		</div>
	</form>
</body>
</html>

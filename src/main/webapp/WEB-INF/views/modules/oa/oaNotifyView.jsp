<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(
			function() {
				//$("#name").focus();
				$("#inputForm").validate({
					rules: {
						type: {
			                required: true
			            },
			            title: {
			                required: true,
			                rangelength: [1,50]
			            },
			            content: {
			                required: true,
			                rangelength: [1,2000]
			            },
			            oaNotifyRecord: {
			                required: true
			            }
					},
					messages: {
						type: {
			                required: '请选择类型'
						},
						title: {
			                required: '标题不能为空',
			                rangelength:'标题必须是1-50字之间'
			            },
			            content: {
			                required: '内容不能为空',
			                rangelength:'内容必须是1-2000字之间'
			            },
			            oaNotifyRecord: {
			                required: '接收人不能为空'
			            }
					},
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
<style>
#status1,#status2 {
	margin-top: -2px;
}
label[for="status1"] {
	margin-right: 10px;
}
</style>

</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/oaNotify/self">消息列表</a></li>
		<li class="active"><a
			href="${ctx}/oa/oaNotify/form?id=${oaNotify.id}">消息<shiro:hasPermission
					name="oa:oaNotify:edit">${oaNotify.status eq '1' ? '查看' : not empty oaNotify.id ? '修改' : '添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="oa:oaNotify:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="oaNotify"
		action="${ctx}/oa/oaNotify/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<c:choose>
			<c:when test="${oaNotify.status eq '1'}">
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">类型</label>
						${oaNotify.status}
							<form:select path="type" cssClass="form-control" disabled="true">
								<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
								<form:options items="${fns:getDictList('oa_notify_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">标题</label>
							<form:input path="title" htmlEscape="false"
								class="form-control" disabled="true"/>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label">内容</label>
							<form:textarea path="content" htmlEscape="false" rows="8"
								 class="form-control required" disabled="true"/>
						</div>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">类型</label>
							<form:select path="type" cssClass="form-control">
								<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
								<form:options items="${fns:getDictList('oa_notify_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">标题</label>
							<form:input path="title" htmlEscape="false"
								class="form-control" />
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-4">
						<div class="form-group">
							<label class="control-label">内容</label>
							<form:textarea path="content" htmlEscape="false" rows="8"
								 class="form-control required" />
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<c:if test="${oaNotify.status ne '1'}">
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
					<div class="control-group">
						<label class="control-label">状态</label>
						<div class="controls">
							<form:radiobuttons path="status"
								items="${fns:getDictList('oa_notify_status')}" itemLabel="label"
								itemValue="value" htmlEscape="false" class="required" />
							<span class="help-inline">
								发布后不能进行操作。</span>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">接收人</label>
						<sys:treeselect id="oaNotifyRecord" name="oaNotifyRecordIds"
							value="${oaNotify.oaNotifyRecordIds}"
							labelName="oaNotifyRecordNames"
							labelValue="${oaNotify.oaNotifyRecordNames}" title="用户"
							url="/sys/office/treeDataAll?type=3"
							cssClass="form-control required valid" notAllowSelectParent="true"
							checked="true" />
						<span class="help-inline"></span>
					</div>
				</div>
			</div>
			
		</c:if>
		<c:if test="${oaNotify.status eq '1'}">
		<div class="row">
			<div class="col-xs-3">
			
			<div class="control-group">
				<label class="control-label">附件</label>
				<div class="controls">
					<form:hidden id="files" path="files" htmlEscape="false"
						maxlength="255" class="input-xlarge" />
					<sys:ckfinder input="files" type="files" uploadPath="/oa/notify"
						selectMultiple="true" readonly="true" />
				</div>
			</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
			<div class="control-group">
				<label class="control-label">接收人</label>
				<div class="controls">
					<table id="contentTable"
						class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th>接收人</th>
								<th>接受科室</th>
								<th>阅读状态</th>
								<th>阅读时间</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${oaNotify.oaNotifyRecordList}"
								var="oaNotifyRecord">
								<tr>
									<td>${oaNotifyRecord.user.name}</td>
									<td>${oaNotifyRecord.user.office.name}</td>
									<td>${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
									</td>
									<td><fmt:formatDate value="${oaNotifyRecord.readDate}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					已查阅：${oaNotify.readNum} &nbsp; 未查阅：${oaNotify.unReadNum} &nbsp;
					总共：${oaNotify.readNum + oaNotify.unReadNum}
				</div>
			</div>
			</div>
		</div>
		</c:if>
		<div class="row">
			<div class="col-xs-12">
				<div class="form-actions">
					<c:if test="${oaNotify.status ne '1'}">
						<shiro:hasPermission name="oa:oaNotify:edit">
							<input id="btnSubmit" class="btn btn-primary" type="submit"
								value="保 存" />&nbsp;</shiro:hasPermission>
					</c:if>
					<input id="btnCancel" class="btn btn-primary" type="button"
						value="返 回" onclick="history.go(-1)" />
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
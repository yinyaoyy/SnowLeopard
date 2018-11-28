<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>综合查询</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
	    	location = '${ctx}/act/task/comprehensiveQuery?pageNo='+n+'&pageSize='+s;
	    }
		/* 案件类型切换 */
		function typeChange()
		{		
			  var html = "<option value=''>请选择</option>";
			  var caseClassify = $('#type').val();
			  $.ajax({
	                 url:"${ctx}/oa/act/oaFastLegal/getCaseType",
	                 data: ({'type':'oa_handle_channels','parentId':caseClassify}),
	                 type:'post',
	                 success:function(data){
	                	if(data.result="success"){
	                		var list = data.list;
	                		for(var i = 0 ; i < list.length; i ++){
	                			html=html+"<option value="+list[i].value+">"+list[i].label+"</option>";
	                		}
	                		$('#caseType').empty();
	                		$('#caseType').append(html);
	                	}
	                 }
	          });
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/task/comprehensiveQuery">综合查询</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="processStateVo" action="${ctx}/act/task/comprehensiveQuery/" method="get" class="form-inline">
		<div class="form-group">
			<label class="control-label">办理渠道：</label>
			<form:select path="type" cssClass="form-control form-control w200" onchange="typeChange()">
				<form:option value="" label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getOneDictList('oa_handle_channels')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label">流程名称：</label>
			<form:select path="caseType" cssClass="form-control form-control w200" onchang="">
				<form:option value="" label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getChildrenDictList('oa_handle_channels',processStateVo.type)}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
						<label class="control-label" for="birthday">创建时间：</label>
						<input type="text"  readonly="readonly" maxlength="20" class="form-control  w200" name="applyBeginDate" id="applyBeginDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${processStateVo.applyBeginDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;"> --
						<input type="text"  readonly="readonly" maxlength="20" class="form-control  w200" name="applyEndDate" id="applyEndDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${processStateVo.applyEndDate}' pattern="yyyy-MM-dd"/>" style="background-color:white;cursor: auto;">
		</div>
		<div class="form-group">
			<label class="control-label">流程进度：</label>
			<form:select path="state" cssClass="form-control form-control w200">
				<form:option value="" label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${fns:getDictList('oa_act_progress')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label">案件标题：</label>
			<form:input path="title"  htmlEscape="false" maxlength="64" class="form-control form-control w200"/>
		</div>
		<div class="form-group">
			<label class="control-label" style="margin-left:10px;">承办人：</label>
			<form:input path="contractor"  htmlEscape="false" maxlength="64" class="form-control form-control w200"/>
		</div>
		<div class="form-group">
		&nbsp;	<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form><table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="500">标题</th>
				<th>申请人</th>
				<th>办理渠道</th>
				<th>案件类型</th>
				<th>当前环节</th>
				<th width="150">创建时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="processStateVo">
				<c:set var="title" value="${processStateVo.title}" />
				<c:set var="state" value="${processStateVo.stateDesc}" />
				<c:set var="beginDate" value="${processStateVo.applyDate}" />
				<c:set var="procInsId" value="${processStateVo.procInsId}" />
				<c:set var="id" value="${processStateVo.id}" />
				<c:set var="applyUser" value="${processStateVo.applyUser}" />
				<c:set var="caseType" value="${processStateVo.caseTypeDesc}" />
				<c:set var="type" value="${processStateVo.typeDesc}" />
				<tr>
					<td title="${title}">${title}</td>
					<td>${applyUser}</td>
					<td>${type}</td>
					<td>${caseType}</td>
					<td>${state}</td>
					<td><fmt:formatDate value="${beginDate}" type="both"/></td>
					<td>
						<c:choose> 
							<c:when test="${ (processStateVo.type eq '1' ) && (processStateVo.caseType eq 'legal_aid') }">
								<a href="${ctx}/oa/act/oaLegalAid/form?procInsId=${procInsId}&status=view&id=${id}&zt=3" title="详情" >详情</a>
							</c:when>
							<c:when test="${ (processStateVo.type eq '1' ) && processStateVo.caseType eq 'people_mediation' }">
								<a href="${ctx}/oa/act/oaPeopleMediationAcceptRegister/form?id=${id}&status=view&zt=3" title="详情" >详情</a>
							</c:when>
							<c:when test="${ (processStateVo.type eq '2' ) }">
								<a href="${ctx}/oa/act/oaFastLegal/form?id=${id}&status=view&zt=3" title="详情" >详情</a>
							</c:when>
				   		 </c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

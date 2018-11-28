<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发起任务</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			function showView(href){
				top.$.jBox.open('iframe:'+href,'查看当前环节',$(top.document).width()-440,$(top.document).height()-240,{
					buttons:{"关闭":true},
					loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
						$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
						$("body", h.find("iframe").contents()).css("margin","10px");
					}
				});
			}
			$('.showView').click(function(){
				var path = $(this).data('viewhref');
				showView(path)
			})
		});
		function page(n,s){
        	location = '${ctx}/act/task/process/?pageNo='+n+'&pageSize='+s;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li class="active"><a href="${ctx}/act/task/process/">新建任务</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/act/task/process/" method="post" class="form-inline">
		<div class="form-group">
				<label class="control-label">全部分类：</label>
				<select id="category" name="category" class="form-control form-control w200">
					<option value="">${fns:getLanguageContent('sys_select_categories')}</option>
					<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
					</c:forEach>
				</select>
		</div>
		<div class="form-group">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form>
	<sys:message content="${message}"/>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			   <th>流程名称</th>
				<th width="150">流程分类</th>
				<th>流程标识</th>
				<th>流程图</th>
				<th width="150">更新时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="object">
				<c:set var="process" value="${object[0]}" />
				<c:set var="deployment" value="${object[1]}" />
				<tr>
				    <td title="${process.name}">${process.name}</td>
					<td>${fns:getDictLabel(process.category,'act_category','无分类')}</td>
					<td>${process.key}</td>
					<td><a class="showView" data-viewHref="${pageContext.request.contextPath}/act/process/resource/read?procDefId=${process.id}&resType=image">${process.diagramResourceName}</a>
					<%-- <td><b title='流程版本号'>V: ${process.version}</b></td> --%>
					<td><fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<a href="${ctx}/act/task/form?procDefId=${process.id}" title="启动流程">启动流程</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

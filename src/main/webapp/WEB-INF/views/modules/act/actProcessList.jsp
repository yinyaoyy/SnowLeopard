<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			jihuo();
		});
		function page(n,s){
        	location = '${ctx}/act/process/?pageNo='+n+'&pageSize='+s;
        }
		function jihuo(){
			var  arr=$(".jihuo");
			var val="";
			for(var i=0;i<arr.length;i++){
				val=arr.eq(i).attr('href');
				var aa=val.split('&');
				arr.eq(i).attr('href',aa[0]+'&'+aa[1]+'&'+encodeURI(encodeURI(aa[2])));
			}
		}
		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", ifButtons:false,submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/process/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;margin-top: 15px;" class="form-search" onsubmit="loading('正在设置，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="procDefId" value="" />
			<select id="categoryBoxCategory" name="category" class="form-control" style="width: 160px;float:left;margin-left: 50px;">
				<option value="">无分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<input id="categorySubmit" class="btn btn-primary" style="float: left;margin-left: 10px;" type="submit" value="   保    存   "/>　　
           <br/><br/><br/><br/>		
        </form>
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/process/">流程管理</a></li>
		<li><a href="${ctx}/act/process/deploy/">部署流程</a></li>
		<li><a href="${ctx}/act/process/running/">运行中的流程</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/act/process/" method="post" class="form-inline">
	<div class="form-group">
				<select id="category" name="category" class="form-control w200">
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
	<table class="table table-striped table-bordered table-condensed table-nowrap">
		<thead>
			<tr>
			    <th>流程名称</th>
				<th width="80">流程版本</th>
				<th>流程分类</th>
				<th>流程ID</th>
				<th>流程标识</th>
				<th width="150">部署时间</th>
				<th>流程XML</th>
				<th>流程图片</th>
				<th width="240">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="object">
				<c:set var="process" value="${object[0]}" />
				<c:set var="deployment" value="${object[1]}" />
				<tr>
					<td title="${process.name}">${process.name}</td>
					<td><b title='流程版本号'>V:${process.version}</b></td>
					<td><a href="javascript:updateCategory('${process.id}', '${process.category}')" title="设置分类">${fns:getDictLabel(process.category,'act_category','无分类')}</a></td>
					<td title="${process.id}">${process.id}</td>
					<td title="${process.key}">${process.key}</td>
					<td><fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td title="${process.resourceName}"><a target="_blank" href="${ctx}/act/process/resource/read?procDefId=${process.id}&resType=xml">${process.resourceName}</a></td>
					<td title="${process.diagramResourceName}"><a target="_blank" href="${ctx}/act/process/resource/read?procDefId=${process.id}&resType=image">${process.diagramResourceName}</a></td>
					<td>
						<c:if test="${process.suspended}">
							<a class="jihuo" href="${ctx}/act/process/update/active?procDefId=${process.id}&key=${process.key}&name=${process.name}" onclick="return confirmx('确认要激活吗？', this.href)" title="激活">激活</a>
						</c:if>
						<c:if test="${!process.suspended}">
							<a href="${ctx}/act/process/update/suspend?procDefId=${process.id}&key=${process.key}" onclick="return confirmx('确认挂起除吗？', this.href)" title="挂起">挂起</a>
						</c:if>
						<a href='${ctx}/act/process/delete?deploymentId=${process.deploymentId}&key=${process.key}&version=${process.version}' onclick="return confirmx('确认要删除该流程吗？', this.href)" title="删除">删除</a>
                        <a href='${ctx}/act/process/convert/toModel?procDefId=${process.id}' onclick="return confirmx('确认要转换为模型吗？', this.href)" title="转换为模型"><i class="fa fa-fw fa-sitemap"></i></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

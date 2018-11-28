<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>模型管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
			bushu();
		});
		function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }
		function bushu(){
			var  arr=$(".bushu");
			var val="";
			for(var i=0;i<arr.length;i++){
				val=arr.eq(i).attr('href');
				var aa=val.split('&');
				arr.eq(i).attr('href',aa[0]+'&'+aa[1]+'&'+encodeURI(encodeURI(aa[2])));
			}
		}

		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", ifButtons:false,buttons:{"关闭":true}, submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/model/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;margin-top: 15px;" class="form-search" onsubmit="loading('正在分类，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="id" value="" />
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
		<li class="active"><a href="${ctx}/act/model/">模型管理</a></li>
		<li><a href="${ctx}/act/model/create">新建模型</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/act/model/" method="post" class="form-inline">
	<div class="form-group">
			<label  class="control-label">全部分类：</label> 
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
			   <th>模型名称</th>
				<th width="80">版本号</th>
				<th>流程分类</th>
				<th>模型ID</th>
				<th>模型标识</th>
				<th width="150">创建时间</th>
				<th width="150">最后更新时间</th>
				<th width="270">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="model">
				<tr>
					<td title="${model.name}">${model.name}</td>
					<td><b title='流程版本号'>V:${model.version}</b></td>
					<td><a href="javascript:updateCategory('${model.id}', '${model.category}')" title="设置分类">${fns:getDictLabel(model.category,'act_category','无分类')}</a></td>
					<td>${model.id}</td>
					<td>${model.key}</td>
					<td><fmt:formatDate value="${model.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${model.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<a href="${pageContext.request.contextPath}/act/process-editor/modeler.jsp?modelId=${model.id}" target="_blank" title="编辑">编辑</a>
						<a class="bushu" href="${ctx}/act/model/deploy?id=${model.id}&key=${model.key}&name=${model.name}" onclick="return confirmx('确认要部署该模型吗？', this.href)" title="部署">部署</a>
						<a href="${ctx}/act/model/export?id=${model.id}" target="_blank" title="导出">导出</a>
	                    <a href="${ctx}/act/model/delete?id=${model.id}" onclick="return confirmx('确认要删除该模型吗？', this.href)" title="删除">删除</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

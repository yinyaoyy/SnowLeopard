<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>文章管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function () {
		$("#createArticle").click(function (){
			var url=$(this).attr('url');
			var param=$(this).attr('data-id');
			 $.ajax({
				    type: "post",
				    url: jsCtx + "/cms/article/ifChildrenCategory",
				    async: false,
				    data: {
				      "category.id": param
				    },
				    success: function (data) {
				    	if(data){
				    		toVail("请选择二级栏目","warning");
				    	}else{
				    		 window.location.href =url;
				    	}
				    	 
				    }
			  });
			
		});
	});	
	function viewComment(href){
			top.$.jBox.open('iframe:'+href,'查看评论',{
				buttons:{"关闭":true},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
					$("body", h.find("iframe").contents()).css("margin","10px");
				}
			});
			return false;
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a></li>
		<shiro:hasPermission name="cms:article:edit">
			<li><a id="createArticle" href="javascript:void(0);"
				data-id="<c:url value=' ${article.category.id}'></c:url>"
				url="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'></c:url>">文章添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="article"
		action="${ctx}/cms/article/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label class="control-label" style="width: 75px;">栏目：</label>
			<sys:treeselect id="category" name="category.id"
				value="${article.category.id}" labelName="category.name"
				labelValue="${article.category.name}" title="栏目"
				url="/cms/category/treeData" module="article"
				cssClass="form-control required  input-xxlarge"
				notAllowSelectRoot="false" />
		</div>
		<div class="form-group">
			<label class="control-label" style="width: 100px;">标题：</label>
			<form:input path="title" htmlEscape="false" maxlength="50"
				class="form-control w200" />
		</div>
		<div class="form-group">
			<label class="control-label" style="width: 93px;">旗县：</label>
			<sys:treeselect id="area" name="area.id" value="${article.area.id}"
				labelName="area.name" labelValue="${article.area.name}" title="区域"
				url="/sys/area/treeData" cssClass=" form-control  valid "
				allowClear="false" notAllowSelectParent="false" />
		</div>
		<div class="form-group">
			<label class="control-label" style="width: 75px;">发文单位：</label>
			<sys:treeselectUser id="office" name="office.id"
				cssClass="form-control" value="${article.office.id}"
				labelName="office.name" labelValue="${article.office.name}"
				title="科室" url="/sys/office/treeDataAll?type=2"
				notAllowSelectParent="false" />
		</div>
		<div class="form-group">
			<label class="control-label" style="width: 100px;">发布人：</label>
			<sys:treeselectUser id="createBy" name="createBy.id"
				cssClass="form-control" value="${article.createBy.id}"
				labelName="createBy.name" labelValue="${article.createBy.name}"
				title="科员" url="/sys/office/treeDataAll?type=3"
				notAllowSelectParent="false" />
		</div>
		<div class="form-group">
			<label class="control-label" style="width: 100px;">发文时间：</label> <input
				id="beginDate" name="beginDate" type="text" readonly="readonly"
				maxlength="10" class="form-control required"
				value="<fmt:formatDate value='${article.beginDate}'/>"
				style="width: 100px"
				onclick="WdatePicker({onpicked:function(){endDate.click();},dateFmt:'yyyy-MM-dd',isShowClear:true,maxDate:'#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'})" />
			<label class="input-xlarge control-label" style="width: 4px;">-</label>
			<input id="endDate" name="endDate" type="text" readonly="readonly"
				maxlength="10" class="form-control required"
				value="<fmt:formatDate value='${article.endDate}'/>"
				style="width: 100px"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true,minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'%y-%M-%d'})" />
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />&nbsp;&nbsp;
		</div>
		<div class="form-group">
			<label class="control-label">状态：</label>
			<c:forEach items="${fns:getDictList('cms_del_flag')}" var="role">
				<label class="checkbox-inline"> <input type="radio"
					name="delFlag" value="${role.value }"
					<c:if test="${ article.delFlag==role.value}"> 
						               checked="checked"
						            </c:if>
					onclick="$('#searchForm').submit();" />${role.label}
				</label>
			</c:forEach>
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>栏目</th>
				<th>标题</th>
				<th width="50">权重</th>
				<th width="70">点击数</th>
				<th>发布者</th>
				<th width="150">更新时间</th>
				<th width="170">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="article">
				<tr>
					<td><a href="javascript:"
						onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;">${article.category.name}</a></td>
					<td><a href="${ctx}/cms/article/form?id=${article.id}"
						title="${article.title}">${fns:abbr(article.title,40)}</a></td>
					<td>${article.weight}</td>
					<td>${article.hits}</td>
					<td>${article.createBy.name}</td>
					<td><fmt:formatDate value="${article.updateDate}" type="both" /></td>
					<td>
						<%-- <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/view-${article.category.id}-${article.id}${fns:getUrlSuffix()}" target="_blank" title="访问"><i class="fa fa-fw fa-eye"></i></a> --%>
						<shiro:hasPermission name="cms:article:edit">
							<c:if test="${article.category.allowComment eq '1'}">
								<shiro:hasPermission name="cms:comment:view">
									<a
										href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2"
										title="评论" onclick="return viewComment(this.href);">评论</a>
								</shiro:hasPermission>
							</c:if>
							<a href="${ctx}/cms/article/form?id=${article.id}" title="修改">修改</a>
							<shiro:hasPermission name="cms:article:audit">
								<a
									href="${ctx}/cms/article/delete?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${article.category.id}"
									onclick="return confirmx('确认要${article.delFlag ne 0?'发布':'删除'}该文章吗？', this.href)"
									title="${article.delFlag ne 0?'发布':'删除'}">${article.delFlag ne 0?'发布':'删除'}</a>
							</shiro:hasPermission>
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
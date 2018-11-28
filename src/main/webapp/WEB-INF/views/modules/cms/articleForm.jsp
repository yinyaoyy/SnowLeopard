<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

	<head>
		<title>文章管理</title>
		<meta name="decorator" content="default" />
		<script type="text/javascript">
			$(document).ready(function() {
				if($("#link").val()) {
					$('#linkBody').show();
					$('#url').attr("checked", true);
				}
				/* $("#title").focus(); */
				$("#inputForm").validate({
					rules:{
						description: {
		                required: true
					},
					title:{
						 required: true
					}
				},
				messages: {
					description: {
		                required: '摘要不能为空'
					},
					title:{
						 required: '标题不能为空'
					}
				},
					
				submitHandler: function(form) {
						if($("#categoryId").val() == "") {
							$("#categoryName").focus();
							top.$.jBox.tip('请选择归属栏目', 'warning');
						} else if(CKEDITOR.instances.content.getData() == "" && $("#link").val().trim() == "") {
							top.$.jBox.tip('请填写正文', 'warning');
						} else {
							$(form).find("#btnSubmit").attr("disabled", true);
							  loading('正在提交，请稍等...');
							  form.submit();
						}
					},
					errorContainer: "#messageBox",
					errorPlacement: function(error, element) {
						$("#messageBox").text("输入有误，请先更正。");
						if(element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
							error.appendTo(element.parent().parent());
						} else {
							error.insertAfter(element);
						}
					}
				});
			});
		</script>
	</head>

	<body>
		<ul class="nav nav-tabs">
			<li>
				<a href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a>
			</li>
			<li class="active">
				<a href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">
				<shiro:hasPermission name="cms:article:edit">${not empty article.id?'文章修改':'文章添加'}</shiro:hasPermission>
                 <shiro:lacksPermission name="cms:article:edit">文章查看</shiro:lacksPermission>
				</a>
			</li>
		</ul><br/>
		<form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/article/save" method="post" class="form-horizontal">
			<form:hidden path="id" />
			<sys:message content="${message}" />
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">归属栏目</label>
						<span style="float:right">
                    <!-- <p id="showRole" style="margin:0;"><label class="checkbox-inline"><input id="url" type="checkbox" onclick="if(this.checked){$('#linkBody').show()}else{$('#linkBody').hide()}$('#link').val()">外部链接</label></p> -->
                </span>
						<sys:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}" title="栏目" url="/cms/category/treeData" module="article" cssClass="form-control required  input-xxlarge" notAllowSelectRoot="false" />
					</div>
				</div>
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label" for="name">标题</label>
						<form:input path="title" htmlEscape="false" maxlength="50" class="form-control valid" />
					</div>
				</div>
                <div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">关键字</label>
						<form:input path="keywords" htmlEscape="false" maxlength="50" class="form-control valid " />
						<p class="help-inline addpage-prompt note">多个关键字，用空格分隔。</p>
					</div>
				</div>	
               <div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">过期时间</label>
						<input type="text" class="form-control" name="weightDate" id="weightDate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${article.weightDate}' pattern=" yyyy-MM-dd "/>" style="background-color:white;cursor: auto;">
						<p class="help-inline addpage-prompt note">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</p>
					</div>
				</div>							
			</div>
			<!-- <div class="row" id="linkBody" style="display:none"> -->
				
			<!-- </div> -->
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">权重</label>
						<span style="float:right;">
					 <p id="showRole" style="margin:0;"><label class="checkbox-inline"><input id="weightTop" type="checkbox" style="margin-top:4px;" onclick="$('#weight').val(this.checked?'999':'0')">置顶</label></p>
				</span>
						<form:input path="weight" htmlEscape="false" maxlength="50" class="form-control valid " />
					</div>
				</div>
				
				
			  <div class="col-xs-3">
					<div class="form-group ">
						<label class="control-label">外部链接</label>
						<form:input path="link" htmlEscape="false" maxlength="50" class="form-control valid " />
						<p class="help-inline addpage-prompt note">绝对或相对地址。</p>
					</div>
				</div>	
              <div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">来源</label>
						<form:input path="articleData.copyfrom" htmlEscape="false" maxlength="200" class="form-control valid " />
					</div>
				</div>	
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">业务类型</label>
							<form:select path="type" cssClass="form-control">
								<form:option value=""
									label="${fns:getLanguageContent('sys_select_select')}" />
								<form:options items="${fns:getDictList('cms_article_type')}"
									itemLabel="label" itemValue="value" htmlEscape="false" />
							</form:select>
<%-- 						<input type="text" class="form-control" name="text" id="text" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" value="<fmt:formatDate value='${article.createDate}' pattern=" yyyy-MM-dd "/>" style="background-color:white;cursor: auto;">
 --%>					</div>
				</div>			
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="form-group">
						<label class="control-label">摘要</label>
						<form:textarea path="description" htmlEscape="false" rows="8" maxlength="200" class="form-control" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">缩略图</label>
						<input type="hidden" id="image" name="image" value="${article.imageSrc}" />
						<sys:ckfinder input="image" type="thumb" uploadPath="/cms/article" selectMultiple="false" />
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
							<sys:ckfinder input="files" type="files" uploadPath="/cms/article/files"
								selectMultiple="true" readonly="false" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12" style="background:none;color:#2c2e2f;">
					 <div class="form-group" style="background:none;">
						<label class="control-label">正文</label>
						<%-- <form:textarea id="content" htmlEscape="true" path="articleData.content" rows="4" maxlength="200" class="input-xxlarge" name="content" type="text/plain"/> --%>
						<form:textarea id="content" htmlEscape="true" path="articleData.content" rows="8" style="width:100%" class="input-xxlarge" name="content" type="text/plain" />
						<%-- <sys:ckeditor replace="content" uploadPath="/cms/article" /> --%>
					</div>
					
				</div>
			</div>
			<div class="row" >
				<div class="col-xs-7">
					<div class="form-group">
						<label class="control-label">推荐位</label>
						<p id="showRole">
							<c:forEach items="${fns:getDictList('cms_posid')}" var="role">
								<label class="checkbox-inline" style="margin-left:1px;">
						    <input style="margin-top:5px;" type="checkbox" name="posidList" value="${role.value }" 
						           <c:forEach items="${article.posidList}" var="cmsPosid">
						            <c:if test="${ cmsPosid==role.value}"> 
						               checked="checked"
						            </c:if>
						           </c:forEach>
						    />${role.label}
						    </label>
							</c:forEach>
						</p>
						<img src="${ctxStatic}/images/note.png">
						<span class="help-inline" style="margin-left:0;">每个站点最多发布三个首页推荐，超过将自动替换掉最旧的一篇</span>
					</div>
				</div>
			    <shiro:hasPermission name="cms:article:audit">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">发布状态</label>
							<p id="showRole">
								<c:forEach items="${fns:getDictList('cms_del_flag')}" var="role">
									<label class="checkbox-inline">
						    <input type="radio" name="delFlag" value="${role.value }" 
						            <c:if test="${ article.delFlag==role.value}"> 
						               checked="checked"
						            </c:if>
						    />${role.label}
						    </label>
								</c:forEach>
							</p>
						</div>
					</div>
			   </shiro:hasPermission>				
			</div>

			<%-- <shiro:hasPermission name="cms:article:audit">
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">自定义内容视图</label>
							<form:select path="customContentView" class="form-control">
								<form:option value="" label="${fns:getLanguageContent('sys_select_default')}" />
								<form:options items="${contentViewList}" htmlEscape="false" />
								<span class="help-inline">自定义内容视图名称必须以"${article_DEFAULT_TEMPLATE}"开始</span>
							</form:select>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label">自定义视图参数</label>
							<form:input path="viewConfig" htmlEscape="true" class="form-control" />
							<span class="help-inline">视图参数例如: {count:2, title_show:"yes"}</span>
						</div>
					</div>
				</div>
			</shiro:hasPermission> --%>
			<c:if test="${not empty article.id}">
			<div class="row">
				<div class="col-xs-12">
				<div class="control-group">
					<label class="control-label">查看评论</label>
					<div class="controls">
						<input id="btnComment" class="btn btn-primary" type="button" value="查看评论" onclick="viewComment('${ctx}/cms/comment/?module=comment&contentId=${article.id}&status=0')" />
						<script type="text/javascript">
							function viewComment(href) {
								top.$.jBox.open('iframe:' + href, '查看评论', $(top.document).width() - 220, $(top.document).height() - 180, {
									buttons: {
										"关闭": true
									},
									loaded: function(h) {
										$(".jbox-content", top.document).css("overflow-y", "hidden");
										$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
										$("body", h.find("iframe").contents()).css("margin", "10px");
									}
								});
								return false;
							}
						</script>
					</div>
				</div>
				</div>
				</div>
			</c:if>
			<div class="row">
				<div class="col-xs-12" style="background:none;color:#2c2e2f;">
					<div class="form-actions">
						<shiro:hasPermission name="cms:article:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
						<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)" />
					</div>
				</div>
			</div>
		</form:form>
		<!-- 配置文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
        var ue = UE.getEditor('content');
    </script>
	</body>

</html>
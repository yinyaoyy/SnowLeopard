<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>语言管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出语言数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/language/sysMunlLang/export");
						$("#searchForm").submit();
						$("#searchForm").attr("action","${ctx}/language/sysMunlLang/list");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnInput").click(function(){
				 var  op="";
				 $.ajax({
					    type: "post",
					    url: jsCtx + "/sys/dict/listDataByLanguage",
					    async: false,
					    data: {
					      "type": "act_langtype",
					      "languageType": "CN"
					    },
					    success: function (data) {
					      for(var i=0;i<data.length;i++){
					    	  op+='<option value='+data[i].value+'>'+data[i].label+'</option>';
					      }
					    }
				  });
				$.jBox('<div id="importBox">'+
						'<form id="importForm" action="${ctx}/language/sysMunlLang/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()" '+
							'class="form-search" style="padding:0px 10px;text-align:center;" onsubmit="loading('+"正在导入，请稍等..."+');">'+
							'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>'+
							'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>'+
							'<select style="width: 160px;float:left;margin-left: 50px;" class="form-control w200" id="languageType" name="languageType"><option value="">请选择语言</option>'+op+'</select>'+
							'<input style="float: left;margin-left: 10px;" id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>'+
						    '<a href="${ctx}/language/sysMunlLang/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>'+
						'</form>'+
						'<p id="fileName" style="padding-top: 50px;text-align: center;" class="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>'+
					'</div>', {
					title:"导入数据", 
					buttons:{},
					bottomText:"导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
					});
			});
			$("#btnDelete").click(function(){
				if($(".oneCheck[type='checkbox']:checked").length==0){
					toVail("请选择要删除的语言","warning");
				}else{
					 var ids="";
					 var index=0;
					  $(".oneCheck").each(function () {  
					        if($(this).attr('checked')){
					        	if(index>0){
					        		ids+=','+$(this).val();	
					        	}else{
					        		ids+=$(this).val();
					        	}
					        	index++;
					        }
					    });
					  var statu = confirmx('确认要删除该语言吗？',jsCtx + "/language/sysMunlLang/batchDelete?batchid="+ids+"");
			      }
			   });
			$(".allCheck").click(function(){
				  if($(".oneCheck[type='checkbox']:checked").length>0){
					  $(".allCheck").prop("checked", false);
					  $(".oneCheck").each(function () {  
					        $(this).prop("checked", false);  
					    });
				  }else{
					  $(".allCheck").prop("checked", true);
					  $(".oneCheck").each(function () {  
					        $(this).prop("checked", true);  
					    });
				  }
			});
			$(".oneCheck").click(function(){
					 if($(".oneCheck[type='checkbox']:checked").length>0){
						 $(".allCheck").prop("checked", true);
					 }else {
						 $(".allCheck").prop("checked", false);
					 }
					 
			});
		});
		function changeFileName(){
			var file = document.getElementById("uploadFile").files[0];
			document.getElementById("fileName").innerHTML=file.name;
		}
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/language/sysMunlLang/list"); 
			$("#searchForm").submit();
        	return false;
        }
		function  checkLanguage(){
			var files=document.getElementById("uploadFile").files[0];
			if(files==null||files==undefined||files==""){
				toVail("请上传文件","warning");
				return false;
			}else if($("#languageType").val()==""){
				toVail("请选择语言后再导入","warning");
				return false;
			}else{
				return true;
			}
		}
	</script>

	<style>

	.bt{
		overflow: hidden;
	    white-space: nowrap;
	    text-overflow: ellipsis;
    }
     .table{
    	table-layout: fixed;
    }
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/language/sysMunlLang/">语言列表</a></li>
		<shiro:hasPermission name="language:sysMunlLang:edit"><li><a href="${ctx}/language/sysMunlLang/form">语言添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysMunlLang" action="${ctx}/language/sysMunlLang/list" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<div class="form-group">
				<label class="control-label">语言Key：</label>
					<input id="langKey" name="langKey" class="form-control w200" type="text" value="${ sysMunlLang.langKey}" maxlength="50">
				</div>
			
			<div class="form-group">
			<label class="control-label">语言归属：</label>
			<select id="operationType" name="operationType" class="form-control w200">
				<option value="">${fns:getLanguageContent('sys_select_categories')}</option>
				<c:forEach items="${fns:getDictList('language_operation_type')}" var="dict">
					<option value="${ dict.value}" ${dict.value==sysMunlLang.operationType?'selected':''}>${dict.label}</option>
				</c:forEach>
			</select>
			</div>
			
			<div class="form-group">
			<label class="control-label">属性分类：</label>
			<select id="attributeType" name="attributeType" class="form-control w200">
				<option value="">${fns:getLanguageContent('sys_select_categories')}</option>
				<c:forEach items="${fns:getDictList('language_attribute_type')}" var="dict">
					<option value="${ dict.value}" ${dict.value==sysMunlLang.attributeType?'selected':''}>${dict.label}</option>
				</c:forEach>
			</select>
			</div>
			
			<div class="form-group">
			<label class="control-label">归属页面：</label>
			<input id="languageAscription" name="languageAscription" class="form-control w200" type="text" value="${ sysMunlLang.languageAscription}" maxlength="50">
			</div>
			
			<div class="form-group">
				<label class="control-label" style="margin-left:25px;">内容：</label>
				<input id="langContext" name="langContext" class="form-control w200" type="text" value="${ sysMunlLang.langContext}" maxlength="50">
			</div>
			
			<div class="form-group">
			    <label class="control-label" style="margin-left:27px;">语言：</label>
			    <select id="langCode" name="langCode" class="form-control w200">
				<option value="">${fns:getLanguageContent('sys_select_all')}</option>
				   <c:forEach items="${fns:getDictListByLanguage('act_langtype','CN')}" var="dicts">
					<option value="${ dicts.value}" ${dicts.value==sysMunlLang.langCode?'selected':''}>${dicts.label}</option>
				    </c:forEach>
			    </select>
			</div> 
		<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<input id="btnInput" class="btn btn-primary" type="button" value="导入"/>
				<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
				<input id="btnDelete" class="btn btn-primary" type="button" value="删除"/>
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			   <th style="width:25px"><input type="checkbox" class="allCheck"/></th> 
			    <th style="width:100px">语言Key</th>
			    <th style="width:100px">语言归属</th>
			    <th style="width:100px">属性分类</th>
			    <th style="width:100px">归属页面</th>
				<th style="width:80px">内容</th>
				<th style="width:60px">语言</th>
				<th style="width:75px">语言描述</th>
				<th style="width:100px">更新日期</th>
				<shiro:hasPermission name="language:sysMunlLang:edit"><th style="width:235px">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysMunlLang">
			<tr>
			<td><input type="checkbox" class="oneCheck" value="${sysMunlLang.id }"/></td> 
			<td class="bt" title="${sysMunlLang.langKey}">${sysMunlLang.langKey}</td>
			<td>${fns:getDictLabel(sysMunlLang.operationType,'language_operation_type','无分类')}</td>
			<td>${fns:getDictLabel(sysMunlLang.attributeType,'language_attribute_type','无分类')}</td>
			<td>${sysMunlLang.languageAscription}</td>
			<td class="bt" title="${sysMunlLang.langContext}">${sysMunlLang.langContext}</td>
			<td>
			${fns:getDictLabels("CN",'act_langtype',sysMunlLang.langCode,'无语言')}
			<td>${sysMunlLang.description}</td>
				<td><a href="${ctx}/language/sysMunlLang/form?id=${sysMunlLang.id}">
					<fmt:formatDate value="${sysMunlLang.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<shiro:hasPermission name="language:sysMunlLang:edit"><td>
    				<a href="${ctx}/language/sysMunlLang/form?id=${sysMunlLang.id}" title="修改">修改</a>
					<a href="${ctx}/language/sysMunlLang/delete?id=${sysMunlLang.id}" onclick="return confirmx('确认要删除该语言吗？', this.href)" title="删除">删除</a>
					<a href="<c:url value='${fns:getAdminPath()}/language/sysMunlLang/addLanguage?id=${sysMunlLang.id}'></c:url>" title="添加语言">添加语言</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
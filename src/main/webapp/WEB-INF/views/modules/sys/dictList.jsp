<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnOutput").click(function(){
			top.$.jBox.confirm("确认要导出字典数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx}/sys/dict/export");
					$("#searchForm").submit();
					$("#searchForm").attr("action","${ctx}/sys/dict");
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
					'<form id="importForm" action="${ctx}/sys/dict/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"'+
						'class="form-search" style="text-align:center;" onsubmit="loading('+"正在导入，请稍等..."+');">'+
						'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>'+
						'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/><select style="width: 160px;float:left;margin-left: 50px;" class="form-control w200" id="languageTypes" name="languageTypes"><option value="">请选择语言</option>'+op+'</select>'+
						'<input id="btnImportSubmit" style="float: left;margin-left: 10px;" class="btn btn-primary" type="submit" value="   导    入   "/>'+
					   ' <a href="${ctx}/sys/dict/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>'+
					'</form>'+
					'<p id="fileName" style="padding-top: 50px;text-align: center;" class="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>'+
				'</div>', {title:"导入数据", buttons:{}, 
				bottomText:"导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"});
		});
		$("#btnDelete").click(function(){
			if($(".oneCheck[type='checkbox']:checked").length==0){
				toVail("请选择要删除的内容","warning");
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
				  var statu = confirmx('确认要删除该字典吗？',jsCtx + "/sys/dict/batchDelete?batchid="+ids+"");
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
	};
	function  checkLanguage(){
		var files=document.getElementById("uploadFile").files[0];
		if(files==null||files==undefined||files==""){
			toVail("请上传文件","warning");
			return false;
		}else if($("#languageTypes").val()==""){
			toVail("请选择语言后再导入","warning");
			return false;
		}else{
			return true;
		}
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
		<li class="active"><a href="${ctx}/sys/dict/">字典列表</a></li>
		<shiro:hasPermission name="sys:dict:edit">
			<li><a href="${ctx}/sys/dict/form?sort=10">字典添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="dict"
		action="${ctx}/sys/dict/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="form-group">
			<label class="control-label">类型：</label>
			<form:select id="type" path="type" class="form-control w200">
				<form:option value=""
					label="${fns:getLanguageContent('sys_select_all')}" />
				<form:options items="${typeList}" htmlEscape="false" />
			</form:select>
		</div>
		<div class="form-group">
			<label class="control-label">描述：</label>
			<form:input path="description" htmlEscape="false" maxlength="50"
				class="form-control w200" />
		</div>
		<div class="form-group">
			<label class="control-label">语言：</label> <select id="languageType"
				name="languageType" class="form-control w200">
				<option value="">${fns:getLanguageContent('sys_select_all')}</option>
				<c:forEach items="${fns:getDictListByLanguage('act_langtype','CN')}"
					var="dicts">
					<option value="${ dicts.value}"
						${dicts.value==dict.languageType?'selected':''}>${dicts.label}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form-group">
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" /> &nbsp;<input id="btnInput" class="btn btn-primary"
				type="button" value="导入" /> &nbsp;<input id="btnOutput"
				class="btn btn-primary" type="button" value="导出" /> &nbsp;<input
				id="btnDelete" class="btn btn-primary" type="button" value="删除" />
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" class="allCheck" />
				<th>键值</th>
				<th>标签</th>
				<th>类型</th>
				<th>语言</th>
				<th>描述</th>
				<th>排序</th>
				<shiro:hasPermission name="sys:dict:edit">
					<th width="210">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="dict">
				<tr>
					<td><input type="checkbox" class="oneCheck"
						value="${dict.id }" /></td>
					<td>${dict.value}</td>
					<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
					<td><a href="javascript:"
						onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
					<td>
						${fns:getDictLabels("CN",'act_langtype',dict.languageType,'无语言')}
					</td>
					<td>${dict.description}</td>
					<td>${dict.sort}</td>
					<shiro:hasPermission name="sys:dict:edit">
						<td><a href="${ctx}/sys/dict/form?id=${dict.id}" title="修改">修改</a>
							<a href="${ctx}/sys/dict/delete?id=${dict.id}&type=${dict.type}"
							onclick="return confirmx('确认要删除该字典吗？', this.href)" title="删除">删除</a>
							<a
							href="<c:url value='${fns:getAdminPath()}/sys/dict/addKey?id=${dict.id}'></c:url>"
							title="添加键值">添加键值</a></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
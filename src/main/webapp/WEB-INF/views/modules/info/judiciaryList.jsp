<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>司法所管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		
		$("#btnOutput").click(function(){
			top.$.jBox.confirm("确认要导出司法所数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx}/info/judiciary/export");
					$("#searchForm").submit();
					//loading('正在提交，请稍等...');
					$("#searchForm").attr("action","${ctx}/info/judiciary");
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
		$("#btnImport").click(function() {
			$.jBox('<div id="importBox">' +
				'<form id="importForm" action="${ctx}/info/judiciary/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
				'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
				'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
				'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
				'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
				'<a href="${ctx}/info/judiciary/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
				'</form>' +
				'<p style="text-align: center;" class="fileName" id="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>' +
				'</div>', {
					title: "导入数据",
					buttons: {},
					bottomText: "导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
				});
			$('#btnImportSubmit').bind('click',function(){
				$('#importForm').submit();
				var files = document.getElementById("uploadFile").files[0];
				if(files == null || files == undefined || files == "") {
					toVail("请上传文件","error");
					this.attr('disabled',false)
				}else{
					loading('正在提交，请稍等...');
					$(this).attr('disabled','disabled')
				}
			})
		});
		$("#btnDelete").click(function() {
			if($(".oneCheck[type='checkbox']:checked").length == 0) {
				toVail("请选择要删除的司法所");
			} else {
				var ids = "";
				var index = 0;
				$(".oneCheck").each(function() {
					if($(this).attr('checked')) {
						if(index > 0) {
							ids += ',' + $(this).val();
						} else {
							ids += $(this).val();
						}
						index++;
					}
				});
				var statu = confirmx('确认要删除选中的司法所吗？', jsCtx + "/info/judiciary/batchDelete?batchid=" + ids + "");
			}
		});
		$(".allCheck").click(allCheck);
		$(".oneCheck").click(oneCheck);
	});
	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
    	return false;
    }
	function changeFileName() {
		var file = document.getElementById("uploadFile").files[0];
		document.getElementById("fileName").innerHTML = file.name;
	}

	function checkLanguage() {
		var files = document.getElementById("uploadFile").files[0];
		if(files == null || files == undefined || files == "") {
			return false;
		} else {
			return true;
		}
	}		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/info/judiciary/">司法所列表</a></li>
		<shiro:hasPermission name="info:judiciary:edit"><li><a href="${ctx}/info/judiciary/form">司法所添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="judiciary" action="${ctx}/info/judiciary/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	 <div class="form-group">
			<label class="control-label">名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="200" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label">归属旗县：</label>
			<sys:treeselect id="area" name="area.id" value="${judiciary.area.id}" labelName="area.name" labelValue="${judiciary.area.name}"
				title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group" style="margin-left:5px;">
			<label class="control-label">归属乡镇：</label>
			<sys:treeselect id="area" name="town.id" value="${judiciary.town.id}" labelName="town.name" labelValue="${judiciary.town.name}"
				title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" style="margin-left:10px;" type="submit" value="查询"/>
			&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
			&nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
			<!-- &nbsp;<input id="btnDelete" class="btn btn-primary" type="button" value="删除" /> -->
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>司法所名称</th>
				<th>所属旗县</th>
				<th>所属乡镇</th>
				<th>电话</th>
				<shiro:hasPermission name="info:judiciary:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="judiciary">
			<tr>
				<td><a href="${ctx}/info/judiciary/form?id=${judiciary.id}">
					${judiciary.name}
				</a></td>
				<td>
					${judiciary.area.name}
				</td>
				<td>
					${judiciary.town.name}
				</td>
				<td>
					${judiciary.phone}
				</td>
				<shiro:hasPermission name="info:judiciary:edit"><td>
    				<a href="${ctx}/info/judiciary/form?id=${judiciary.id}" >修改</a>
					<a href="${ctx}/info/judiciary/delete?id=${judiciary.id}" onclick="return confirmx('确认要删除该司法所吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
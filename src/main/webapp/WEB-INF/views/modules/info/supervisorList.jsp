<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人民监督科管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导人民监督员数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/supervisor/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/supervisor");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/supervisor/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/supervisor/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
					toVail("请选择要删除的监督员");
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
					var statu = confirmx('确认要删除选中的监督员吗？', jsCtx + "/info/supervisor/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/supervisor/">人民监督员列表</a></li>
		<shiro:hasPermission name="info:supervisor:edit"><li><a href="${ctx}/info/supervisor/form">人民监督员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="supervisor" action="${ctx}/info/supervisor/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	     <div class="form-group">
			<label class="control-label">姓名：</label>
			<form:input path="xrName" htmlEscape="false"   class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label" >选任单位：</label>
				<sys:treeselect id="name" name="name.id" value="${supervisor.name.id}" labelName="name.name" labelValue="${supervisor.name.name}"
					title="选任单位" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		 
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" style="margin-left:10px;" type="submit" value="查询"/>
			&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
			&nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
			&nbsp;<input id="btnDelete" class="btn btn-primary" type="button" value="删除" />
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>选任单位</th>
				<th>姓名</th>
				<th>身份证号</th>
				<th>手机号</th>
				<th>民族</th>
				<th>政治面貌</th>
				<th>文化程度</th>
				<shiro:hasPermission name="info:supervisor:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supervisor">
			<tr>
				<td><a href="${ctx}/info/supervisor/form?id=${supervisor.id}">
					${supervisor.name.name}
				</a></td>
				<td>${supervisor.xrName}</td>
				<td>${supervisor.idno}</td>
				<td>${supervisor.phone}</td>
				<td>${supervisor.nation}</td>
				<td>${supervisor.politicsStatus}</td>
				<td>${supervisor.educationBackground}</td>
				<shiro:hasPermission name="info:supervisor:edit"><td>
    				<a href="${ctx}/info/supervisor/form?id=${supervisor.id}" >修改</a>
					<a href="${ctx}/info/supervisor/delete?id=${supervisor.id}" onclick="return confirmx('确认要删除该人民监督科吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
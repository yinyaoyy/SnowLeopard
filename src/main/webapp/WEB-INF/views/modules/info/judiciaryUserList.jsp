<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>司法所工作人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function() {
				top.$.jBox.confirm("确认要导出司法所工作人员数据吗？", "系统提示", function(v, h, f) {
					if(v == "ok") {
						$("#searchForm").attr("action", "${ctx}/info/judiciaryUser/export");
						//$("#searchForm").submit();
						loading('正在提交，请稍等...');
					}
				}, {
					buttonsFocus: 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/judiciaryUser/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload"  style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/judiciaryUser/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
		});
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
		<li class="active"><a href="${ctx}/info/judiciaryUser/">司法所工作人员列表</a></li>
		<shiro:hasPermission name="info:judiciaryUser:edit"><li><a href="${ctx}/info/judiciaryUser/form">司法所工作人员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="judiciaryUser" action="${ctx}/info/judiciaryUser/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			 <div class="form-group">
			<label class="control-label">名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="200" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label" style='width:auto'>归属司法所：</label>
			<sys:treeselectOfficeUser id="office" name="office.id" value="${judiciaryUser.office.id}"
					labelName="office.name" labelValue="${judiciaryUser.office.name}" title="司法所"
					url="/sys/office/getOfficeUser?type=12" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
		</div>
		
		<div class="form-group" style="margin-left:10px;">
			<label class="control-label">归属旗县：</label>
			<sys:treeselect id="area" name="area.id" value="${judiciaryUser.area.id}" labelName="area.name" labelValue="${judiciaryUser.area.name}"
				title="旗县" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group" style="margin-left:10px;">
			<label class="control-label">归属乡镇：</label>
			<sys:treeselect id="town" name="town.id" value="${judiciaryUser.town.id}" labelName="town.name" labelValue="${judiciaryUser.town.name}"
				title="乡镇" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
			&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
			<!-- &nbsp;<input id="btnDelete" class="btn btn-primary" type="button" value="删除" /> -->
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>姓名</th>
				<th>电话</th>
				<th>民族</th>
				<th>职务</th>
				<th>兼职人民调解员</th>
				<th>归属司法所</th>
				<th>归属乡镇</th>
				<th>归属乡镇</th>
				<shiro:hasPermission name="info:judiciaryUser:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="judiciaryUser">
			<tr>
				<td><a href="${ctx}/info/judiciaryUser/form?id=${judiciaryUser.id}">
					${judiciaryUser.name}
				</a></td>
				<td>
					${judiciaryUser.phone}
				</td>
				<td>
				   ${fns:getDictLabel(judiciaryUser.ethnic, 'ethnic', '无')}
				</td>
				<td>
					 ${fns:getDictLabel(judiciaryUser.roleId, 'info_role_type', '无')}
				</td>
				<td>
				  ${fns:getDictLabel(judiciaryUser.isPeopleMediation, 'yes_no', '无')}
				</td>
				<td>
					${judiciaryUser.office.name}
				</td>
				<td>
					${judiciaryUser.area.name}
				</td>
				<td>
					${judiciaryUser.town.name}
				</td>
				<shiro:hasPermission name="info:judiciaryUser:edit"><td>
    				<a href="${ctx}/info/judiciaryUser/form?id=${judiciaryUser.id}" >修改</a>
					<a href="${ctx}/info/judiciaryUser/delete?id=${judiciaryUser.id}" onclick="return confirmx('确认要删除该司法所工作人员吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
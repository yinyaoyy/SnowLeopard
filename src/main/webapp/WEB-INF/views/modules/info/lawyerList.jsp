<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>律师信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出律师数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/lawyer/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/lawyer");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/lawyer/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/lawyer/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
					toVail("请选择要删除的律师");
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
					var statu = confirmx('确认要删除选中的律师吗？', jsCtx + "/info/lawyer/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/lawyer/">律师信息列表</a></li>
		<shiro:hasPermission name="info:lawyer:edit"><li><a href="${ctx}/info/lawyer/form">律师信息添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="lawyer" action="${ctx}/info/lawyer/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="control-label">律师名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="200" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label">执业机构：</label>
			<sys:treeselectOfficeUser id="lawOffice" name="lawOffice.id" value="${lawyer.lawOffice.id}"
					labelName="lawOffice.name" labelValue="${lawyer.lawOffice.name}" title="律师事务所"
					url="/sys/office/getOfficeUser?type=2" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
		</div>
		<div class="form-group">
			<label class="control-label">所在地区：</label>
			<sys:treeselect id="area" name="area.id" value="${lawyer.area.id}" labelName="area.name" labelValue="${lawyer.area.name}"
				title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			&nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
			&nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
			&nbsp;<input id="btnDelete" class="btn btn-primary" type="button" value="删除" />
		</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="50"><input type="checkbox" class="allCheck" /></th>
				<th >律师名称</th>
				<th >民族</th>
				<th >职务</th>
				<th >政治面貌</th>
				<th>执业年限</th>
				<th>执业机构</th>
				<th>执业证号</th>
				<th>所在地区</th>
				<th>执业状态</th>
				<!-- <th>备注信息</th> -->
				<shiro:hasPermission name="info:lawyer:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="lawyer">
			<tr>
				<td><input type="checkbox" class="oneCheck" value="${lawyer.id}" /></td>
				<td style="width:50px;"><a href="${ctx}/info/lawyer/form?id=${lawyer.id}">
					${lawyer.name}
				</a></td>
				<td>
				${fns:getDictLabel(lawyer.ethnic, 'ethnic', '无')} 
				</td>
				<td>
				${fns:getDictLabel(lawyer.role, 'info_role_type', '无')} 
				</td>
				<td>
				   ${lawyer.politicalFace==""|| empty lawyer.politicalFace ? '无' : lawyer.politicalFace}
				</td>
				<td style="width:80px;">
				   ${lawyer.practisingYear==""|| empty lawyer.practisingYear ? '无' : lawyer.practisingYear}
				</td>
				<td >
					${lawyer.lawOffice.name}
				</td>
				<td>
					${lawyer.licenseNumber}
				</td>
				<td>
				${lawyer.area.name==""|| empty lawyer.area.name ? '无' : lawyer.area.name}
					<%-- ${fns:getDictLabel(lawyer.licenseType, 'lawyer_license_type', '无')} --%>
				</td>
				<td>
					${fns:getDictLabel(lawyer.licenseStatus, 'lawyer_license_status', '无')}
				</td>
				<!-- <td>
					${lawyer.remarks}
				</td> -->
				<shiro:hasPermission name="info:lawyer:edit"><td>
    				<a href="${ctx}/info/lawyer/form?id=${lawyer.id}" >修改</a>
					<a href="${ctx}/info/lawyer/delete?id=${lawyer.id}" onclick="return confirmx('确认要删除该律师信息吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
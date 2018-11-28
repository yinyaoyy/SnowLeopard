<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>基层法律服务工作者管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出基层法律服务工作者数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/legalServicePerson/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/legalServicePerson");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/legalServicePerson/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/legalServicePerson/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
					toVail("请选择要删除的基层法律服务工作者");
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
					var statu = confirmx('确认要删除选中的基层法律服务工作者吗？', jsCtx + "/info/legalServicePerson/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/legalServicePerson/">基层法律服务工作者列表</a></li>
		<shiro:hasPermission name="info:legalServicePerson:edit"><li><a href="${ctx}/info/legalServicePerson/form">基层法律服务工作者添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="legalServicePerson" action="${ctx}/info/legalServicePerson/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="control-label">名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="200" class="input-medium"/>
		</div>
		<div class="form-group">
			<label class="control-label" style="width:150px">归属基层法律服务所：</label>
			<sys:treeselectOfficeUser id="legalOffice" name="legalOffice.id" value="${legalServicePerson.legalOffice.id}"
					labelName="legalOffice.name" labelValue="${legalServicePerson.legalOffice.name}" title="基层法律服务所"
					url="/sys/office/getOfficeUser?type=8" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
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
				<th width="50"><input type="checkbox" class="allCheck" /></th>
				<th>姓名</th>
				<th>职务</th>
				<th>所属基层法律服务所</th>
				<th width="50">性别</th>
				<th>执业证号</th>
				<th>资格证号</th>
				<th width="100">专职/兼职</th>
				<shiro:hasPermission name="info:legalServicePerson:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="legalServicePerson">
			<tr>
				<td><input type="checkbox" class="oneCheck" value="${legalServicePerson.id}" /></td>
				<td><a href="${ctx}/info/legalServicePerson/form?id=${legalServicePerson.id}">
					${legalServicePerson.name}
				</a></td>
				<td>
               ${fns:getDictLabel(legalServicePerson.role, 'info_role_type', '无')} 
            </td>
				<td title="${legalServicePerson.legalOffice.name}">
					${legalServicePerson.legalOffice.name}
				</td>
				<td>
					${fns:getDictLabel(legalServicePerson.sex, 'sex', '')==""? '无' : fns:getDictLabel(legalServicePerson.sex, 'sex', '')}
				</td>
				<td>
					${legalServicePerson.licenseNumber==""|| empty legalServicePerson.licenseNumber ? '无' : legalServicePerson.licenseNumber}
				</td>
				<td>
					${legalServicePerson.qualificationNo==""|| empty legalServicePerson.qualificationNo ? '无' : legalServicePerson.qualificationNo}
				</td>
				<td>
					${fns:getDictLabel(legalServicePerson.licenseType, 'legal_service_type', '')==""? '无' : ''}
				</td>
				<!-- 
				<td>
					<fmt:formatDate value="${legalServicePerson.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${legalServicePerson.remarks}
				</td>
				 -->
				<shiro:hasPermission name="info:legalServicePerson:edit"><td>
    				<a href="${ctx}/info/legalServicePerson/form?id=${legalServicePerson.id}" >修改</a>
					<a href="${ctx}/info/legalServicePerson/delete?id=${legalServicePerson.id}" onclick="return confirmx('确认要删除该基层法律服务工作者吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
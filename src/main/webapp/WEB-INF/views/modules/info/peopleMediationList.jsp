<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>人民调解员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出调解员数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/peopleMediation/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/peopleMediation");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/peopleMediation/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/peopleMediation/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
					toVail("请选择要删除的调解员");
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
					var statu = confirmx('确认要删除选中的调解员吗？', jsCtx + "/info/peopleMediation/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/peopleMediation/">人民调解员列表</a></li>
		<shiro:hasPermission name="info:peopleMediation:edit"><li><a href="${ctx}/info/peopleMediation/form">人民调解员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="peopleMediation" action="${ctx}/info/peopleMediation/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="control-label" style="word-break:keep-all;">调解员名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="200" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label" style="word-break:keep-all;">归属调委会：</label>
			<sys:treeselectOfficeUser id="office" name="office.id" value="${peopleMediation.office.id}"
					labelName="office.name" labelValue="${peopleMediation.office.name}" title="调委会"
					url="/sys/office/getOfficeUser?type=10" cssClass="form-control valid" 
					setRootId="" disabled="" 
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
		</div>
		
		<div class="form-group" style="margin-left:5px;">
			<label class="control-label">归属旗县：</label>
			<sys:treeselect id="area" name="area.id" value="${peopleMediation.area.id}" labelName="area.name" labelValue="${peopleMediation.area.name}"
				title="旗县" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group" style="margin-left:5px;">
			<label class="control-label">归属乡镇：</label>
			<sys:treeselect id="town" name="town.id" value="${peopleMediation.town.id}" labelName="town.name" labelValue="${peopleMediation.town.name}"
				title="乡镇" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group">
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
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
				<th>调解员名称</th>
				<th>性别</th>
				<th>电话</th>
				<th>民族</th>
				<th>学历</th>
				<th>职务</th>
				<th>所属调委会</th>
				<th>地区</th>
				<shiro:hasPermission name="info:peopleMediation:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="peopleMediation">
			<tr>
			   <td><input type="checkbox" class="oneCheck" value="${peopleMediation.id}" /></td>
				<td><a href="${ctx}/info/peopleMediation/form?id=${peopleMediation.id}">
					${peopleMediation.name}
				</a></td>
				<td>
				    ${empty fns:getDictLabel(peopleMediation.sex, 'sex', '')||fns:getDictLabel(peopleMediation.sex, 'sex', '')==''?'无':fns:getDictLabel(peopleMediation.sex, 'sex', '')}
				</td>
				<td>
					${empty peopleMediation.phone||peopleMediation.phone==''?'无':peopleMediation.phone}
				</td>
				<td>
				    ${empty fns:getDictLabel(peopleMediation.ethnic, 'ethnic', '')||fns:getDictLabel(peopleMediation.ethnic, 'ethnic', '')==''?'无':fns:getDictLabel(peopleMediation.ethnic, 'ethnic', '')}
				</td>
				<td>
					${empty peopleMediation.education||peopleMediation.education==''?'无':peopleMediation.education}
				</td>
				<td>
					${empty peopleMediation.roleId||emptypeopleMediation.roleId==''?'无':peopleMediation.roleId}
				</td>
				<td title="${empty peopleMediation.office.name||peopleMediation.office.name==''?'无':peopleMediation.office.name}">
					${empty peopleMediation.office.name||peopleMediation.office.name==''?'无':peopleMediation.office.name}
				</td>
				<td>
					${empty peopleMediation.area.name||peopleMediation.area.name==''?'无':peopleMediation.area.name}
				</td>
				<shiro:hasPermission name="info:peopleMediation:edit"><td>
    				<a href="${ctx}/info/peopleMediation/form?id=${peopleMediation.id}" >修改</a>
					<a href="${ctx}/info/peopleMediation/delete?id=${peopleMediation.id}" onclick="return confirmx('确认要删除该人民调解员吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>在监服刑人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出在监服刑人员数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/prisonUser/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/prisonUser");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/prisonUser/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
					'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
					'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
					'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
					'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
					'<a href="${ctx}/info/notaryMember/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
					toVail("请选择要删除的在监服刑人员");
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
					var statu = confirmx('确认要删除选中的在监服刑人员吗？', jsCtx + "/info/prisonUser/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/prisonUser/">在监服刑人员列表</a></li>
		<shiro:hasPermission name="info:prisonUser:edit"><li><a href="${ctx}/info/prisonUser/form">在监服刑人员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="prisonUser" action="${ctx}/info/prisonUser/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label>姓名：</label>
			<form:input path="name" htmlEscape="false" maxlength="20" class="form-control"/>
		</div>
		<div class="form-group">
			<label>所在地区：</label>
			<sys:treeselect id="area" name="area.id" value="${prisonUser.area.id}" labelName="area.name" labelValue="${prisonUser.area.name}"
				title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
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
				<th width="50">性别</th>
				<th width="50">年龄</th>
				<th>民族</th>
				<th>学历</th>
				<th>地址</th>
				<th>核查状态</th>
				<th>所在监所</th>
				<shiro:hasPermission name="info:prisonUser:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="prisonUser">
			<tr>
				<td><input type="checkbox" class="oneCheck" value="${prisonUser.id}" /></td>
				<td><a href="${ctx}/info/prisonUser/form?id=${prisonUser.id}">
					${prisonUser.name}
				</a></td>
				<td>
					${fns:getDictLabel(prisonUser.sex, 'sex', '')==""? '无' : fns:getDictLabel(prisonUser.sex, 'sex', '')}
				</td>
				<td>
					${prisonUser.age==""|| empty prisonUser.age ? '无' : prisonUser.age}
				</td>
				<td>
					${fns:getDictLabel(prisonUser.ethnic, 'ethnic', '')==""? '无' : fns:getDictLabel(prisonUser.ethnic, 'ethnic', '')}
				</td>
				<td>
					${prisonUser.education==""|| empty prisonUser.education ? '无' : prisonUser.education}
				</td>
				<td title="${prisonUser.address==''|| empty prisonUser.address ? '无' : prisonUser.address}">
				     ${prisonUser.address==""|| empty prisonUser.address ? '无' : prisonUser.address}
				</td>
				<td>
					${fns:getDictLabel(prisonUser.checkState, 'check_state', '')==""? '无' : fns:getDictLabel(prisonUser.checkState, 'check_state', '')}
				</td>
				<td>
					${prisonUser.prisonName==""|| empty prisonUser.prisonName ? '无' : prisonUser.prisonName}
				</td>
				<shiro:hasPermission name="info:prisonUser:edit"><td>
    				<a href="${ctx}/info/prisonUser/form?id=${prisonUser.id}" >修改</a>
					<a href="${ctx}/info/prisonUser/delete?id=${prisonUser.id}" onclick="return confirmx('确认要删除该在监服刑人员吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
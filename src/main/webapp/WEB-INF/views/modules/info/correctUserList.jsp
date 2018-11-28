<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>社区矫正人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出在监服刑人员数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/correctUser/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/correctUser");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/correctUser/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
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
					var statu = confirmx('确认要删除选中的在监服刑人员吗？', jsCtx + "/info/correctUser/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/correctUser/">社区矫正人员列表</a></li>
		<shiro:hasPermission name="info:correctUser:edit"><li><a href="${ctx}/info/correctUser/form">社区矫正人员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="correctUser" action="${ctx}/info/correctUser/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label>姓名：</label>
			<form:input path="name" htmlEscape="false" maxlength="20" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label" style="word-break:keep-all;">所在司法所：</label>
				<sys:treeselect id="office" name="office.id" value="${correctUser.office.id}" labelName="office.name" labelValue="${correctUser.office.name}"
					title="司法所" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		</div>
		<div class="form-group">
			<input id="btnSubmit" class="btn btn-primary"  style="margin-left:10px;" type="submit" value="查询"/>
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
				<th>所在司法所</th>
				<th>罪名</th>
				<th width="75">主刑</th>
				<th>矫正类别</th>
				<th>责任人</th>
				<th>矫正状态</th>
				<th>矫正等级</th>
				<shiro:hasPermission name="info:correctUser:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="correctUser">
			<tr>
				<td><input type="checkbox" class="oneCheck" value="${correctUser.id}" /></td>
				<td><a href="${ctx}/info/correctUser/form?id=${correctUser.id}">
					${correctUser.name}
				</a></td>
				<td>
					${correctUser.office.name==""|| empty correctUser.office.name ? '无' : correctUser.office.name}
				</td>
				<td title="${correctUser.accusation==''|| empty correctUser.accusation ? '无' : correctUser.accusation}">
					${correctUser.accusation==""|| empty correctUser.accusation ? '无' : correctUser.accusation}
				</td>
				<td>
					${fns:getDictLabel(correctUser.mainPenalty, 'info_correct_user_penalty', '')==""? '无' : fns:getDictLabel(correctUser.mainPenalty, 'info_correct_user_penalty', '')}
				</td>
				<td>
					${fns:getDictLabel(correctUser.correctType, 'info_correct_user_type', '')==""? '无' : fns:getDictLabel(correctUser.correctType, 'info_correct_user_type', '')}
				</td>
				<td>
					${correctUser.responsibilityName==""|| empty correctUser.responsibilityName ? '无' : correctUser.responsibilityName}
				</td>
				<td>
					${fns:getDictLabel(correctUser.correctStatus, 'info_correct_user_state', '')==""? '无' : fns:getDictLabel(correctUser.correctStatus, 'info_correct_user_state', '')}
				</td>
				<td>
					${fns:getDictLabel(correctUser.correctLevel, 'info_correct_user_level', '')==""? '无' : fns:getDictLabel(correctUser.correctLevel, 'info_correct_user_level', '')}
				</td>
				<shiro:hasPermission name="info:correctUser:edit"><td>
    				<a href="${ctx}/info/correctUser/form?id=${correctUser.id}" >修改</a>
					<a href="${ctx}/info/correctUser/delete?id=${correctUser.id}" onclick="return confirmx('确认要删除该社区矫正人员吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>

	<head>
		<title>用户管理</title>
		<meta name="decorator" content="default" />
		<script type="text/javascript">
			$(document).ready(function() {
				$("#btnExport").click(function() {
					top.$.jBox.confirm("确认要导出用户数据吗？", "系统提示", function(v, h, f) {
						if(v == "ok") {
							$("#searchForm").attr("action", "${ctx}/sys/user/export");
							$("#searchForm").submit();
						}
					}, {
						buttonsFocus: 1
					});
					top.$('.jbox-body .jbox-icon').css('top', '55px');
				});
				/* $("#btnImport").click(function() {
					$.jBox($("#importBox").html(), {
						title: "导入数据",
						buttons: {},
						bottomText: "导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
					});
				}); */
				$("#btnImport").click(function() {
					$.jBox('<div id="importBox">' +
						'<form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
						'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
						'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
						'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
						'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
						'<a href="${ctx}/sys/user/import/template" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
						toVail("请选择要删除的用户");
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
						var statu = confirmx('确认要删除该用户吗？', jsCtx + "/sys/user/batchDelete?batchid=" + ids + "");
					}
				});
				$(".allCheck").click(function() {
					if($(".oneCheck[type='checkbox']:checked").length > 0) {
						$(".allCheck").prop("checked", false);
						$(".oneCheck").each(function() {
							$(this).prop("checked", false);
						});
					} else {
						$(".allCheck").prop("checked", true);
						$(".oneCheck").each(function() {
							$(this).prop("checked", true);
						});
					}
				});
				$(".oneCheck").click(function() {
					if($(".oneCheck[type='checkbox']:checked").length > 0) {
						$(".allCheck").prop("checked", true);
					} else {
						$(".allCheck").prop("checked", false);
					}

				});
			});

			function page(n, s) {
				if(n) $("#pageNo").val(n);
				if(s) $("#pageSize").val(s);
				$("#searchForm").attr("action", "${ctx}/sys/user/list");
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
			<li class="active">
				<a href="${ctx}/sys/user/list">用户列表</a>
			</li>
			<shiro:hasPermission name="sys:user:edit">
				<li>
					<a href="${ctx}/sys/user/form">用户添加</a>
				</li>
			</shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/list" method="post" class="form-inline">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
			<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
			<div class="form-group">
				<label class="control-label">归属机构：</label>
				<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}" title="机构" url="/sys/office/treeData?type=1" cssClass="form-control" allowClear="true" />
			</div>
			<div class="form-group" style="margin-left:10px;">
				<label class="control-label">登录名：</label>
				<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control" />
			</div>
			<div class="form-group">
				<label class="control-label">归属科室：</label>
				<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" title="科室" url="/sys/office/treeData?type=2" cssClass="form-control" allowClear="true" notAllowSelectParent="true" />
			</div>
			<div class="form-group" style="margin-left:17px;">
				<label class="control-label">姓&nbsp;&nbsp;&nbsp;名：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" />
			</div>
			<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
				<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
				<input id="btnDelete" class="btn btn-primary" type="button" value="删除" />
			</div>

		</form:form>
		<sys:message content="${message}" />
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th width="50"><input type="checkbox" class="allCheck" /></th>
					<th>归属机构</th>
					<th>归属科室</th>
					<th class="sort-column login_name">登录名</th>
					<th class="sort-column name">姓名</th>
					<!-- <th class="sort-column no">工号</th> -->
					<th>手机</th>
					<th>是否允许登录</th>
					<shiro:hasPermission name="sys:user:edit">
						<th width="170">操作</th>
					</shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="user">
					<tr>
						<td><input type="checkbox" class="oneCheck" value="${user.id}" /></td>
						<td>${user.company.name}</td>
						<td>${user.office.name}</td>
						<td>
							<a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a>
						</td>
						<td>${user.name}</td>
						<%-- <td>${user.no}</td> --%>
						<td>${user.isShow=='0'?'暂无':user.mobile}</td>
						<td>${fns:getDictLabel(user.loginFlag, 'yes_no', '')}</td>
						<shiro:hasPermission name="sys:user:edit">
							<td>
								<%-- <a href="${ctx}/sys/user/form?id=${user.id}" title="修改">修改</a>
								<a href="${ctx}/sys/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)" title="删除">删除</a> --%>
								<a href="${ctx}/sys/user/form?id=${user.id}" title="修改">修改</a>
								<a href="${ctx}/sys/user/delete?id=${user.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)" title="删除">删除</a>
							</td>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="pagination">${page}</div>
	</body>
</html>
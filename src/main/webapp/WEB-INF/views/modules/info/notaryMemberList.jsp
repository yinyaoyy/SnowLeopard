<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公证员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出公证员数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/info/notaryMember/export");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/info/notaryMember");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function() {
				$.jBox('<div id="importBox">' +
					'<form id="importForm" action="${ctx}/info/notaryMember/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
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
					toVail("请选择要删除的公证员");
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
					var statu = confirmx('确认要删除选中的公证员吗？', jsCtx + "/info/notaryMember/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/notaryMember/">公证员列表</a></li>
		<shiro:hasPermission name="info:notaryMember:edit"><li><a href="${ctx}/info/notaryMember/form">公证员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="notaryMember" action="${ctx}/info/notaryMember/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label>公证员名称：</label>
			<form:input path="name" htmlEscape="false" maxlength="200" class="form-control"/>
		</div>
		<div class="form-group">
			<label class="control-label">执业机构：</label>
			<sys:treeselectOfficeUser id="notaryAgency" name="notaryAgency.id" value="${notaryMember.notaryAgency.id}"
					labelName="notaryAgency.name" labelValue="${notaryMember.notaryAgency.name}" title="公证处"
					url="/sys/office/getOfficeUser?type=3" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
		</div>
		<%-- <div class="form-group">
			<label>归属公证处：</label>
			<form:select path="notaryAgency.id" class="form-control w200">
				<option value="">全部</option>
				<form:options items="${notaryAgencyList }" itemValue="id" itemLabel="name"/>
			</form:select>
		</div>
 --%>		<div class="form-group">
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
				<th>公证员名称</th>
				<th>性别</th>
				<th>年龄</th>
				<th>民族</th>
				<th>政治面貌</th>
				<th>学历</th>
				<th>职务</th>
				<th>执业机构</th>
				<th>主管机关</th>
				<shiro:hasPermission name="info:notaryMember:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="notaryMember">
			<tr>
				<td><input type="checkbox" class="oneCheck" value="${lawyer.id}" /></td>
				<td><a href="${ctx}/info/notaryMember/form?id=${notaryMember.id}">
					${notaryMember.name}
				</a></td>
				<td>
					${fns:getDictLabel(notaryMember.sex, 'sex', '无')} 
				</td>
				<td>
					${notaryMember.age==""||empty notaryMember.age ? '无' : notaryMember.age}
				</td>
				<td>
				    ${fns:getDictLabel(notaryMember.ethnic, 'ethnic', '无')} 
				</td>
				<td>
					${notaryMember.politicalFace==""||empty notaryMember.politicalFace ? '无' : notaryMember.politicalFace}
				</td>
				<td>
					${notaryMember.education==""||empty notaryMember.education ? '无' : notaryMember.education}
				</td>
				<td>
					${fns:getDictLabel(notaryMember.role, 'info_role_type', '无')} 
				</td>
				<td title="${notaryMember.notaryAgency.name==''||empty notaryMember.notaryAgency.name ? '无' : notaryMember.notaryAgency.name}">
					${notaryMember.notaryAgency.name==""||empty notaryMember.notaryAgency.name ? '无' : notaryMember.notaryAgency.name}
				</td>
				<td title="${notaryMember.office.name==''||empty notaryMember.office.name ? '无' : notaryMember.office.name}">
					${notaryMember.office.name==""||empty notaryMember.office.name ? '无' : notaryMember.office.name}
				</td>
				<shiro:hasPermission name="info:notaryMember:edit"><td>
    				<a href="${ctx}/info/notaryMember/form?id=${notaryMember.id}" >修改</a>
					<a href="${ctx}/info/notaryMember/delete?id=${notaryMember.id}" onclick="return confirmx('确认要删除该公证员吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
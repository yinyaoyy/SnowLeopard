<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法援中心工作人员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function(){
			top.$.jBox.confirm("确认要导出法援中心工作人员数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx}/info/lawAssitanceUser/export");
					$("#searchForm").submit();
					//loading('正在提交，请稍等...');
					$("#searchForm").attr("action","${ctx}/info/lawAssitanceUser");
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
		$("#btnImport").click(function() {
			$.jBox('<div id="importBox">' +
				'<form id="importForm" action="${ctx}/info/lawAssitanceUser/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
				'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
				'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
				'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
				'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
				'<a href="${ctx}/info/lawAssitanceUser/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
				toVail("请选择要删除的法援中心工作人员");
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
				var statu = confirmx('确认要删除选中的法援中心工作人员吗？', jsCtx + "/info/lawAssitanceUser/batchDelete?batchid=" + ids + "");
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
		<li class="active"><a href="${ctx}/info/lawAssitanceUser/">法援中心工作人员列表</a></li>
		<shiro:hasPermission name="info:lawAssitanceUser:edit"><li><a href="${ctx}/info/lawAssitanceUser/form">法援中心工作人员添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="lawAssitanceUser" action="${ctx}/info/lawAssitanceUser/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="control-label">名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="form-control" />
			</div>
			<div class="form-group">
			<label class="control-label" style="width: 100px">所在地区：</label>
			<sys:treeselect id="area" name="area.id" value="${lawAssitanceUser.area.id}" labelName="area.name" labelValue="${lawAssitanceUser.area.name}"
				title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true" notAllowSelectParent="true"/>
		    </div>
		<div class="form-group">
			<label class="control-label" style="width: 100px">法援中心：</label>
			<sys:treeselectOfficeUser id="office" name="office.id" value="${lawAssitanceUser.office.id}"
					labelName="office.name" labelValue="${lawAssitanceUser.office.name}" title="法援中心"
					url="/sys/office/getOfficeUser?type=5" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
		</div>
			<div class="form-group">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
				<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
				<input id="btnExport" class="btn btn-primary" type="button" value="导出" />
				<input id="btnDelete" class="btn btn-primary" type="button" value="删除" />
			</div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th width="50"><input type="checkbox" class="allCheck" /></th>
				<th>名称</th>
				<th width="70">年龄</th>
				<th width="70">性别</th>
				<th>民族</th>
				<th>归属法援中心</th>
				<th>执业证号</th>
                <th>职务</th>
				<!-- <th>更新时间</th>
				<th>备注信息</th> -->
				<shiro:hasPermission name="info:lawAssitanceUser:edit">
				<th width="170">操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="lawAssitanceUser">
			<tr>
			    <td><input type="checkbox" class="oneCheck" value="${lawAssitanceUser.id}" /></td>
				<td><a href="${ctx}/info/lawAssitanceUser/form?id=${lawAssitanceUser.id}">
					${lawAssitanceUser.name}
				</a></td>
				<td>
					${ empty lawAssitanceUser.age || lawAssitanceUser.age=='null'? '无' : lawAssitanceUser.age}
					
				</td>
				<td>
					${fns:getDictLabel(lawAssitanceUser.sex, 'sex', '')}
					
				</td>
				<td>
					${fns:getDictLabel(lawAssitanceUser.nation, 'ethnic', '')}
				</td>
				<td>
					${lawAssitanceUser.office.name}
				</td>
				<td>
				   ${ empty lawAssitanceUser.licenseNumber || lawAssitanceUser.licenseNumber==null? '暂无' : lawAssitanceUser.licenseNumber}
				</td>
				<td>
					 ${fns:getDictLabel(lawAssitanceUser.role, 'info_role_type', '无')}
				</td>
				<%-- 2018/06/12
				<td>
					 <fmt:formatDate value="${lawAssitanceUser.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/> 
				</td>
				<td>
					 ${lawAssitanceUser.remarks} 
				</td> --%>
				<shiro:hasPermission name="info:lawAssitanceUser:edit"><td>
    				<a href="${ctx}/info/lawAssitanceUser/form?id=${lawAssitanceUser.id}" >修改</a>
					<a href="${ctx}/info/lawAssitanceUser/delete?id=${lawAssitanceUser.id}" onclick="return confirmx('确认要删除该法援中心工作人员吗？', this.href)" >删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
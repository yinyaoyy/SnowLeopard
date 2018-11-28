<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>基层法律服务所管理管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
    $(document).ready(function() {
		$("#btnOutput").click(function(){
			top.$.jBox.confirm("确认要导出基层法律服务所数据吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#searchForm").attr("action","${ctx}/info/infoLegalServiceOffice/export");
					$("#searchForm").submit();
				//loading('正在提交，请稍等...');
					$("#searchForm").attr("action","${ctx}/info/infoLegalServiceOffice");
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
		$("#btnImport").click(function() {
			$.jBox('<div id="importBox">' +
				'<form id="importForm" action="${ctx}/info/infoLegalServiceOffice/import" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
				'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
				'<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
				'<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
				'<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
				'<a href="${ctx}/info/infoLegalServiceOffice/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
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
				toVail("请选择要删除的基层法律服务所");
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
				var statu = confirmx('确认要删除选中的基层法律服务所吗？', jsCtx + "/info/infoLegalServiceOffice/batchDelete?batchid=" + ids + "");
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
    <li class="active"><a href="${ctx}/info/infoLegalServiceOffice/">基层法律服务所列表</a></li>
    <shiro:hasPermission name="info:infoLegalServiceOffice:edit">
        <li><a href="${ctx}/info/infoLegalServiceOffice/form">基层法律服务所添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="infoLegalServiceOffice" action="${ctx}/info/infoLegalServiceOffice/"
           method="post" class="form-inline">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="form-group">
        <label>名称：</label>
        <form:input path="name" htmlEscape="false" maxlength="225" class="form-control"/>
    </div>
    <div class="form-group">
        <label>执业许可证号：</label>
        <form:input path="practiceLicenseNum" htmlEscape="false" maxlength="225" class="form-control"/>
    </div>
    <div class="form-group">
        <label>执业状态：</label>
        <form:select path="occupationalState" class="form-control w200">
            <form:option value="" label=""/>
            <form:options items="${fns:getDictList('occupational_state')}" itemLabel="label" itemValue="value"
                          htmlEscape="false"/>
        </form:select>
    </div>
    <div class="form-group">
        <label>所在地区：</label>
        <sys:treeselect id="area" name="area.id" value="${infoLegalServiceOffice.area.id}" labelName="area.name"
                        labelValue="${infoLegalServiceOffice.area.name}"
                        title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true"
                        notAllowSelectParent="true"/>
    </div>
    <div class="form-group">
        <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
        &nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
        &nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
    </div>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th width="300">名称</th>
        <th>负责人</th>
        <th>执业许可证号</th>
        <th>执业状态</th>
        <th>组织机构代码证编号</th>
        <th width="70">批准文号</th>
        <shiro:hasPermission name="info:infoLegalServiceOffice:edit">
            <th width="170">操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="infoLegalServiceOffice">
        <tr>
            <td title=" ${infoLegalServiceOffice.name}"><a href="${ctx}/info/infoLegalServiceOffice/form?id=${infoLegalServiceOffice.id}">
                    ${infoLegalServiceOffice.name}
            </a></td>
            <td>
                    ${infoLegalServiceOffice.personInCharge}
            </td>
            <td>
                    ${infoLegalServiceOffice.practiceLicenseNum==""||empty infoLegalServiceOffice.practiceLicenseNum ? '无' : infoLegalServiceOffice.practiceLicenseNum}
            </td>
            <td>
                    ${fns:getDictLabel(infoLegalServiceOffice.occupationalState, 'occupational_state', '')==""? '无' : ''}
            </td>
            <td>
                    ${infoLegalServiceOffice.licenseNumber==""||empty infoLegalServiceOffice.licenseNumber ? '无' : infoLegalServiceOffice.licenseNumber}
            </td>
            <td>
                    ${infoLegalServiceOffice.approvalNumber==""||empty infoLegalServiceOffice.approvalNumber ? '无' : infoLegalServiceOffice.approvalNumber}
            </td>
            <shiro:hasPermission name="info:infoLegalServiceOffice:edit">
                <td>
                    <a href="${ctx}/info/infoLegalServiceOffice/form?id=${infoLegalServiceOffice.id}" >修改</a>
                    <a href="${ctx}/info/infoLegalServiceOffice/delete?id=${infoLegalServiceOffice.id}"
                       onclick="return confirmx('确认要删除该基层法律服务所管理吗？', this.href)" >删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div><script type="text/javascript">
   /*  $("#btnOutput").click(function(){
        top.$.jBox.confirm("确认要导出司法鉴定所数据吗？","系统提示",function(v,h,f){
            if(v=="ok"){
                $("#searchForm").attr("action","${ctx}/info/infoLegalServiceOffice/export");
                $("#searchForm").submit();
                $("#searchForm").attr("action","${ctx}/info/infoLegalServiceOffice/list");
            }
        },{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });
    $("#btnImport").click(function() {
        $.jBox('<div id="importBox">' +
            '<form id="importForm" action="${ctx}/info/infoLegalServiceOffice/inport" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
            'class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
            '<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload"></i></label>' +
            '<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
            '<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
            '<a href="${ctx}/info/infoLegalServiceOffice/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
            '</form>' +
            '<p style="text-align: center;" class="fileName" id="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>' +
            '</div>', {
            title: "导入数据",
            buttons: {},
            bottomText: "导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
        });
    }); */
</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>司法鉴定人员管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {

        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/info/infoForensicPersonnel/">司法鉴定人员管理列表</a></li>
    <shiro:hasPermission name="info:infoForensicPersonnel:edit">
        <li><a href="${ctx}/info/infoForensicPersonnel/form">司法鉴定人员管理添加</a></li>
    </shiro:hasPermission>
</ul>
<form:form id="searchForm" modelAttribute="infoForensicPersonnel" action="${ctx}/info/infoForensicPersonnel/"
           method="post" class="form-inline">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <div class="form-group">
        <label>姓名：</label>
        <form:input path="name" htmlEscape="false" maxlength="225" class="form-control"/>
    </div>
    <div class="form-group">
        <label>所在地区：</label>
        <sys:treeselect id="area" name="area.id" value="${infoForensicPersonnel.area.id}" labelName="area.name"
                        labelValue="${infoForensicPersonnel.area.name}"
                        title="区域" url="/sys/area/treeData" cssClass="form-control" allowClear="true"
                        notAllowSelectParent="true"/>
    </div>
   <div class="form-group" style="margin-left:10px;">
			<label>司法鉴定所：</label>
			 <sys:treeselectOfficeUser id="judicialAuthentication" name="judicialAuthentication.id" value="${infoForensicPersonnel.judicialAuthentication.id}"
					labelName="judicialAuthentication.name" labelValue="${infoForensicPersonnel.judicialAuthentication.name}" title="公证处"
					url="/sys/office/getOfficeUser?type=4" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId="" isUser="0"/>
				</div>
    
    <div class="form-group">
        <input id="btnSubmit" class="btn btn-primary" style="margin-left:10px;" type="submit" value="查询"/>
        &nbsp;<input id="btnImport" class="btn btn-primary" type="button" value="导入" />
        &nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
    </div>
</form:form>
<sys:message content="${message}"/>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>姓名</th>
        <th>性别</th>
        <th>民族</th>
        <th>职务</th>
        <th>学历</th>
        <th>执业证号</th>
        <th>所在地区</th>
        <shiro:hasPermission name="info:infoForensicPersonnel:edit">
            <th width="170">操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.list}" var="infoForensicPersonnel">
        <tr>
            <td><a href="${ctx}/info/infoForensicPersonnel/form?id=${infoForensicPersonnel.id}">
                    ${infoForensicPersonnel.name}
            </a></td>
            <td>
                ${fns:getDictLabel(infoForensicPersonnel.sex, 'sex', '无')} 
            </td>
             <td>
               ${fns:getDictLabel(infoForensicPersonnel.ethnic, 'ethnic', '无')} 
            </td>
             <td>
               ${fns:getDictLabel(infoForensicPersonnel.role, 'info_role_type', '无')} 
            </td>
            <td>
                    ${infoForensicPersonnel.education}
            </td>
            <td>
                    ${infoForensicPersonnel.licenseNumber}
            </td>
            <td>
                    ${infoForensicPersonnel.area.name}
            </td>

            <shiro:hasPermission name="info:infoForensicPersonnel:edit">
                <td>
                    <a href="${ctx}/info/infoForensicPersonnel/form?id=${infoForensicPersonnel.id}" >修改</a>
                    <a href="${ctx}/info/infoForensicPersonnel/delete?id=${infoForensicPersonnel.id}"
                       onclick="return confirmx('确认要删除该司法鉴定人员管理吗？', this.href)" >删除</a>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
<script type="text/javascript">
    $("#btnOutput").click(function(){
        top.$.jBox.confirm("确认要导出司法鉴定人员数据吗？","系统提示",function(v,h,f){
            if(v=="ok"){
                $("#searchForm").attr("action","${ctx}/info/infoForensicPersonnel/export");
                $("#searchForm").submit();
               // loading('正在提交，请稍等...');
                $("#searchForm").attr("action","${ctx}/info/infoForensicPersonnel/list");
            }
        },{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });
    $("#btnImport").click(function() {
        $.jBox('<div id="importBox">' +
            '<form id="importForm" action="${ctx}/info/infoForensicPersonnel/inport" method="post" enctype="multipart/form-data" onsubmit="return checkLanguage()"' +
            'class="form-search" style="text-align:center;" onsubmit="loading(' + "正在导入，请稍等..." + ');">' +
            '<label for="uploadFile" style="display:block;font-size:60px;margin:20px 0;"><i title="上传文件" id="uploadStatus" class="fa fa-fw fa-upload" style="font-size:60px;cursor:pointer;"></i></label>' +
            '<input id="uploadFile" name="file" type="file" style="width:330px;display:none;" onchange="changeFileName()"/>' +
            '<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>' +
            '<a href="${ctx}/info/infoForensicPersonnel/export?downType=1" title="下载模板" style="position: absolute;right: 20px;top: 60px;font-size: 20px;"><i class="fa fa-fw fa-file"></i></a>' +
            '</form>' +
            '<p style="text-align: center;" class="fileName" id="fileName">导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！</p>' +
            '</div>', {
            title: "导入数据",
            buttons: {},
            bottomText: "导入文件不能超过10M，仅允许导入“xls”或“xlsx”格式文件！"
        });
    });
</script>
</body>
</html>
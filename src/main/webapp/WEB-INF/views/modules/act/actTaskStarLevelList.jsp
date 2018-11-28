<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>满意度查询</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnOutput").click(function(){
				top.$.jBox.confirm("确认要导出满意度查询数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/act/task/starLevelExport");
						$("#searchForm").submit();
						//loading('正在提交，请稍等...');
						$("#searchForm").attr("action","${ctx}/act/task");
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
		}
		
		function checkType(){
			 var sel=document.getElementById("type"); 
	         var index = sel.selectedIndex; // 选中索引
	         var albumid= sel.options[index].value;
	         if(albumid == '1'){
	       		 $("#atype").val("2");
	       	 }else if(albumid == '6'){
	       		$("#atype").val("3");
	       	 }else if(albumid == '9'){
	       		$("#atype").val("8");
	       	 }else if(albumid == '11'){
	       		$("#atype").val("12");
	       	 }else if(albumid == '13'){
	       		$("#atype").val("5");
	       	 }else if(albumid == '14'){
	       		$("#atype").val("10");
	       	 }
		}
		
 		function dtype(){
			var sel=document.getElementById("type"); 
	        var index = sel.selectedIndex; // 选中索引
	        var albumid= sel.options[index].value;
	        if(albumid == ""){
	        	alert("查询类型为空，默认显示律师事务所！");
	        	return false;
	        }else{
	        	return true;
	        }
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/task/starLevel">满意度查询</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="starLevel" action="${ctx}/act/task/starLevel/" method="post" class="form-inline">
	<input id="atype" type="hidden" value="">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="form-group">
			<label class="control-label">分组类型：</label>
			<form:select id="groupType" path="groupType" class="form-control form-control w200" onchange="agroup()">
				<form:option value="0" label="">请选择</form:option>
				<form:option value="1" label="">地区</form:option>
				<form:option value="2" label="">机构</form:option>
				<form:option value="3" label="">列表</form:option>
			</form:select>
		</div>
		&nbsp;
		<div class="form-group">
			<label class="control-label">查询类型：</label>
			<form:select id="type" path="type" cssClass="form-control form-control w200" onchange="checkType()">
				<form:option value="" label="">请选择</form:option>
				<form:option value="1" label="">律师</form:option>
				<form:option value="6" label="">公证员</form:option>
				<form:option value="9" label="">基层法律服务工作者</form:option>
				<form:option value="11" label="">司法所工作人员</form:option>
				<form:option value="13" label="">法援中心工作人员</form:option>
				<form:option value="14" label="">人民调解员</form:option>
			</form:select>
		</div>
		&nbsp;
		<div class="form-group">
			<label class="control-label">地区：</label>
			<sys:treeselect id="area" name="areaIds"
						value="${starLevel.areaIds}" labelName="areaName"
						labelValue="${starLevel.areaName}" title="区域"
						url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true" checked="true"/>
		</div>
		&nbsp;
		<div class="form-group" id="div1" onclick="dtype()">
			<label class="control-label">机构：</label>
			<sys:treeselectOfficeUser id="office" name="officeIds" value="${starLevel.officeIds}"
					labelName="officeName" labelValue="${starLevel.officeName}" title="机构"
					url="/sys/office/getOfficeUser?type=2" cssClass="form-control valid" setRootId="" checked="false" notAllowSelectParent="false" allowInput="false" allowClear="true" areaId="" isUser="0" />
		</div>
		<%-- &nbsp;
		<div class="form-group" id="div1" style="display:none">
			<label class="control-label">机构：</label>
			<sys:treeselectOfficeUser id="office" name="officeId" value="${starLevel.officeIds}"
					labelName="officeName" labelValue="${starLevel.officeName}" title="机构"
					url="/sys/office/getOfficeUser?type=2" cssClass="form-control valid" setRootId="" checked="false" notAllowSelectParent="false" allowInput="false" allowClear="true" areaId="" isUser="0" />
		</div>
		&nbsp;
		<div class="form-group" id="div2" style="display:none">
			<label class="control-label">全部机构</label>
					<sys:treeselectUser id="office" name="officeId" value="${starLevel.officeIds}"
						cssClass="form-control valid" labelName="officeName" labelValue="${starLevel.officeName}"
						title="机构" url="/sys/office/treeDataAll?type=2" allowClear="true"/>
		</div> --%>
		&nbsp;
		<div class="form-group">
			<label class="control-label">评价星级：</label>
			<form:select path="evaluation" class="form-control form-control w200">
				<form:option value="" label="">请选择</form:option>
				<form:option value="1" label="">1星</form:option>
				<form:option value="2" label="">2星</form:option>
				<form:option value="3" label="">3星</form:option>
				<form:option value="3" label="">4星</form:option>
				<form:option value="3" label="">5星</form:option>
			</form:select>
		</div>
		<div class="form-group">
		&nbsp;	<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();" />
		&nbsp;<input id="btnOutput" class="btn btn-primary" type="button" value="导出"/>
		</div>
	</form:form><table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>地区</th>
				<th>机构</th>
				<th>人员名称</th>
				<th>评价星级</th>
				<th>评价数量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="starLevel">
				<tr>
					<td>${starLevel.area.name}</td>
					<td>${starLevel.office.name}</td>
					<td>${starLevel.name}</td>
					<td>${starLevel.evaluation}</td>
					<td>${starLevel.evaluationNum}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>


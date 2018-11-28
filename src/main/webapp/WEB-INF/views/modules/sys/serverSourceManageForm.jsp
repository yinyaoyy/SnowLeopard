<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>服务管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
					  form.submit(); 
				},
				rules: {
		            roleIdList: {
		            	required: true,
		            }
				},
				messages: {
					roleIdList: {
						required: '请选择服务归属角色',
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		var a = 0;
		function check(){
			if(a == 0){
				a = 1;
				$("input[name='roleIdList']").attr("checked",true);
			}else{
				a = 0;
				$("input[name='roleIdList']").attr("checked",false);
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/serverSourceManage/">服务管理列表</a></li>
		<li class="active"><a href="${ctx}/sys/serverSourceManage/form?id=${serverSourceManage.id}">服务管理<shiro:hasPermission name="sys:serverSourceManage:edit">${not empty serverSourceManage.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:serverSourceManage:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="serverSourceManage" action="${ctx}/sys/serverSourceManage/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
        <div class="row">
            <div class="col-xs-3">
                <div class="form-group">
                    <label class="control-label">父级服务：</label>
                    <sys:treeselect id="pid" name="pid" value="${serverSourceManage.pid}" labelName="pname" labelValue="${serverSourceManage.pname}"
                                    title="父级服务" url="/sys/serverSourceManage/treeData" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true"/>
                </div>
            </div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">服务类别：</label>
				<form:select path="serverType" class="input-xlarge form-control required">
				    <option value="" htmlEscape="false">请选择</option>
					<form:options items="${fns:getDictList('sys_server_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">服务对应资源</label>
				<form:input  path="sourceId"  htmlEscape="false" maxlength="225" class="input-xlarge form-control"/>
				</div>
			</div>
		</div>
        <div class="row">
            <div class="col-xs-3">
                <div class="form-group">
                    <label class="control-label">服务名称：</label>
                    <form:input path="name" htmlEscape="false" maxlength="225" class="input-xlarge form-control required"/>
                </div>
            </div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">pc端服务外部链接：</label>
				 <form:input path="pcHerf" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">移动端服务外部链接：</label>
				<form:input path="mobileHerf" htmlEscape="false" maxlength="225" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">法网首页显示：</label>
				<form:select path="homeShow" class="input-xlarge form-control ">
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">法网端显示：</label>
				<form:select path="pcShow" class="input-xlarge form-control ">
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">移动端显示：</label>
				<form:select path="mobileShow" class="input-xlarge form-control ">
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">移动端首页显示：</label>
				<form:select path="mobileHomeShow" class="input-xlarge form-control ">
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">大屏显示：</label>
				<form:select path="bigdataShow" class="input-xlarge form-control ">
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">云平台显示：</label>
				<form:select path="cloudShow" class="input-xlarge form-control ">
					<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">序号(父级序号作为序号前缀)</label>
					<form:input path="sort" htmlEscape="false" maxlength="11" class="input-xlarge form-control  digits"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
					<div class="form-group">
				<label class="control-label">法网logo：</label>
					<form:hidden id="logo" path="logo" htmlEscape="false" maxlength="225" class="input-xlarge"/>
					<sys:ckfinder input="logo" type="files" uploadPath="/sys/serverSourceManage" selectMultiple="false"/>
					</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">移动端logo：</label>
				<form:hidden id="mobileLogo" path="mobileLogo" htmlEscape="false" maxlength="225" class="input-xlarge"/>
				<sys:ckfinder input="mobileLogo" type="files" uploadPath="/sys/serverSourceManage" selectMultiple="false"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">说明：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="8" maxlength="225" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
			<div class="row">
				<div class="col-xs-12" style="background:none;color:#2c2e2f;">
					<div class="form-group">
						<label class="control-label">归属角色</label>
						&nbsp;&nbsp;&nbsp;
						<label class="checkbox-inline" style="font-size:13px;padding-top:0;">
						<input type="checkbox" name="whole" onclick="check()"/>全选
						</label>
						<div class="form-group" style="font-size: 13px;">
					
					<p id="showRole">
					<c:forEach items="${allRoles}" var="role">
							<label class="checkbox-inline" style="width:200px;margin-left:0;"> <input type="checkbox"
								name="roleIdList" value="${role.id }"
								<c:forEach items="${serverSourceManage.roleIdList}" var="roleId" > 
						            <c:if test="${ roleId==role.id}"> 
						               checked="checked"
						            </c:if>
						        </c:forEach> />${role.name}
							</label>
						</c:forEach>
					</p>
				</div>
						<!-- <p id="showRole"> -->
							<%-- <table style="margin-top:-15px;">
								<c:forEach items="${allRoles}" var="role" varStatus="status">
									<c:choose>
										<c:when test="${status.index < 6}">
											<c:if test="${status.index == 0}">
												<tr>
											</c:if>
												<td width="170px" align="left">
													<label class="checkbox-inline">
											    	<input type="checkbox" name="roleIdList" value="${role.id }" 
											        <c:forEach items="${serverSourceManage.roleIdList}" var="roleId" > 
											            <c:if test="${ roleId==role.id}"> 
											               checked="checked"
											            </c:if>
											        </c:forEach> 
											    	/>${role.name}
											    	</label>
											    </td>
										    <c:if test="${status.index == 6}">
												</tr>
											</c:if>
										</c:when>
										<c:otherwise>
											<c:if test="${status.index%6 == 0}">
												<tr>
											</c:if>
											<c:if test="${status.index >= 6}">
												<td width="170px" align="left">
													<label class="checkbox-inline">
											    	<input type="checkbox" name="roleIdList" value="${role.id }" 
											        <c:forEach items="${serverSourceManage.roleIdList}" var="roleId" > 
											            <c:if test="${ roleId==role.id}"> 
											               checked="checked"
											            </c:if>
											        </c:forEach> 
											    	/>${role.name}
											    	</label>
											    </td>
										    </c:if>
										    <c:if test="${status.index%6 == 5}">
												</tr>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</table> --%>
							<!-- <span class="help-inline"></span>
							<label class="roleList checkbox-inline" style="color: red;float: right;"></label> -->
						<!--</p>-->
					</div>
				</div>
			</div>
		<div class="row form-actions">
			<shiro:hasPermission name="sys:serverSourceManage:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
<script>
   /*  function loadAllSite($e,sourceId){
        $.ajax({
                type: "GET",
                url: jsCtx+"/api/100/300/10",
                data: { timestamp:new Date().getTime(), tag:"100"},
                dataType: "json",
                success: function(data) {
                    if(data.status != 0){
                        return;
                    }
                    var body = data.body;
                    var site;
                    for(var i = 0;i < body.length;i++){
                        site = body[i];
                        if(sourceId==site.id){
                            $e.append('<option value="'
                                    + c
                                    +'" htmlEscape="false" selected="selected">'
                                    + site.title
                                    + '</option>');

                        }else{
                            $e.append('<option value="'
                                    + site.id
                                    +'" htmlEscape="false">'
                                    + site.title
                                    + '</option>');
                        }
                     }
                   // $("#name").val($("#sourceId").find("option:selected").text());
                }
        });
    } */

    /*
    加载所有的人员机构类别
    */
   /*  function loadAllOfficeCategory($e,sourceId) {
        $.ajax({
            type: "GET",
            url:jsCtx+ "/api/100/510/10",
            data: { timestamp:new Date().getTime(), tag:"100",query:'{"key":"sys_office_category"}'},
            dataType: "json",
            async: false,
            success: function(data) {
                if(data.status != 0){
                    return;
                }
                var body = data.body;
                var dict;
                for(var i = 0;i < body.length;i++){
                    dict = body[i];
                    if(dict.value==sourceId){
                        $e.append('<option value="'
                                + dict.value
                                +'" htmlEscape="false" selected="selected">'
                                + dict.label
                                + '</option>');
                    }else{
                        $e.append('<option value="'
                                + dict.value
                                +'" htmlEscape="false" >'
                                + dict.label
                                + '</option>');
                    }
                }
               // $("#name").val($("#sourceId").find("option:selected").text());
            }
        });
    } */
    /*
    根据服务类型添加资源列表
    */
  /*   function loadByServerType(e, sourceId){
        var $source = $("#sourceId");
        $source.empty();
        switch (e){
            case "2":
              	$("#sourceIdHide").hide();
            	$("#sourceId").show();
                 loadAllOfficeCategory($source, sourceId);
                return;
            default:
               	$("#sourceId").hide();
        	    $("#sourceIdHide").show();
                return;
         }
    } */
    /*
    select change事件
     */
   /*  $("#serverType").change(function () {
    	    var $source = $("#sourceId");
    	    $source.empty();
            var typeValue = $("#serverType").val();
            loadByServerType(typeValue);
        }
    ) */
   /*  $(function(){
	    var typeValue = $("#serverType").val();
    	loadByServerType(typeValue, "${serverSourceManage.sourceId }");
    });
 */
    /*
    资源名自动赋值给服务名
    */
    $("#sourceId").change(function () {
         // $("#name").val($("#sourceId").find("option:selected").text());
        }
    )
</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法律服务直通车管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
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
			 
			
			$("#btnSubmit").on("click",function(){
				$('#flag').val('yes')
				$('#inputForm').attr('action','${ctx}/oa/act/oaFastLegal/toDo');
				$('#inputForm').submit();
			});
			
		});
		
		
		/* 案件类型切换 */
		function caseClassChange()
		{		
			  var html = "<option value=''>请选择</option>";
			  var caseClassify = $('#caseClassify').val();
			  $.ajax({
	                 url:"${ctx}/oa/act/oaFastLegal/getCaseType",
	                 data: ({'type':'oa_case_classify','parentId':caseClassify}),
	                 type:'post',
	                 success:function(data){
	                	if(data.result="success"){
	                		var list = data.list;
	                		for(var i = 0 ; i < list.length; i ++){
	                			html=html+"<option value="+list[i].value+">"+list[i].label+"</option>";
	                		}
	                		$('#caseType').empty();
	                		$('#caseType').append(html);
	                	}
	                 }
	          });
		}
		function idCardChange(){
			var idCard = $('#accuserIdCard').val();
			if('18'==idCard.length){
				var string = idCard.substring(16,17);
				if(string%2 ==1){
					$('#accuserSex').val('1');					
				}else{
					$('#accuserSex').val('2');					
				}
				var date = idCard.substring(6,10)+"-"+idCard.substring(10,12)+"-"+idCard.substring(12,14);
				$("input[name='accuserBirthday']").val(date);
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li><a href="${ctx}/act/task/all">全部任务</a></li>
		<li><a href="${ctx}/act/task/process/">新建任务</a></li>
		<li class="active"><a href="${ctx}/oa/act/oaFastLegal/form?id=${oaFastLegal.id}">案件办理直通车<shiro:hasPermission name="oa:act:oaFastLegal:edit">${not empty oaFastLegal.id?'办理':'添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:act:oaFastLegal:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaFastLegal" action="" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey" class="taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>		
		<div class="row">
				<h3>受理信息</h3>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受理人：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="acceptMan.name" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受理人工号：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="acceptManCode" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件受理编号：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="caseAcceptCode" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
				<h3>申请人基本情况</h3>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">姓名：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="accuserName" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">身份证号：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="accuserIdCard" htmlEscape="false" maxlength="18" class="input-xlarge form-control" onchange="idCardChange()" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="accuserEthnic" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
					<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">性别：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="accuserSex" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
					<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">出生日期：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<input name="accuserBirthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaFastLegal.accuserBirthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">手机号：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="accuserPhone" htmlEscape="false" maxlength="64" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在地区：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="accuserCounty.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaFastLegal.accuserCounty.name}" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在乡镇：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="accuserTown.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaFastLegal.accuserTown.name}" disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
				<h3>案件基本情况</h3>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件类别：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="caseClassify" class="input-xlarge form-control" onchange="caseClassChange()" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
					<form:option value="">请选择</form:option>
						<form:options items="${fns:getOneDictList('oa_case_classify')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件类型：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="caseType" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
					<form:option value="">请选择</form:option>
						<form:options items="${fns:getChildrenDictList('oa_case_classify',oaFastLegal.caseClassify)}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案发地区：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<div style="display:none">
					<sys:treeselect id="caseCounty" name="caseTown.id" value="${oaFastLegal.caseCounty.id}" 
					labelName="caseCounty.name" labelValue="${oaFastLegal.caseCounty.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" parentId="caseCounty" /> </div>
					<form:input path="caseCounty.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaFastLegal.caseCounty.name}" disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案发乡镇：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<div style="display:none">
					<sys:treeselect id="caseTown" name="caseTown.id" value="${oaFastLegal.caseTown.id}" 
					labelName="caseTown.name" labelValue="${oaFastLegal.caseTown.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" parentId="caseCounty" /> </div>
					<form:input path="caseTown.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaFastLegal.caseTown.name}" disabled="true"/>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件标题：</label>
				</div>
			</div>
			<div class="col-xs-8">
				<div class="form-group transverse">
					<form:input path="caseTitle" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件内容：</label>
				</div>
			</div>
			<div class="col-xs-8">
				<div class="form-group transverse">
					<form:textarea path="caseReason" htmlEscape="false" rows="8" class="form-control valid " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
				<h3>案件性质</h3>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生时间：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<input name="caseTime" type="text" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaFastLegal.caseTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及人数：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:input path="caseInvolvecount" htmlEscape="false" maxlength="8" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及金额：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="caseInvolveMoney" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('oa_case_money')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受理方式：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="caseSource" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('fast_case_source')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件严重等级：</label>
				</div>
			</div>
			<div class="col-xs-2">
				<div class="form-group transverse">
					<form:select path="caseRank" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('case_rank')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
				<h3>选择案件承办方</h3>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">机构：</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<div style="display:none">
					<sys:treeselect id="office" name="office.id" value="${oaFastLegal.office.id}" labelName="office.name" labelValue="${oaFastLegal.office.name}"
					title="科室" url="/oa/act/oaFastLegal/getOfficeTree?isUser=0&&" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true" procDefKey="fast_legal"
					/></div>
					<form:input path="office.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaFastLegal.office.name}" disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
				<h3>选择案件承办人</h3>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">承办人：</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<div style="display:${(oaFastLegal.act.taskDefKey ne 'fast_banli')?'block':'none' }">
						<sys:treeselect id="transactor" name="transactor.id" value="${oaFastLegal.transactor.id}" labelName="transactor.name" labelValue="${oaFastLegal.transactor.name}"
						title="科室" url="/oa/act/oaFastLegal/getOfficeTree?isUser=1&&" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true" procDefKey="fast_legal"
						 />
					</div>
					<div style="display:${(oaFastLegal.act.taskDefKey eq 'fast_banli')?'block':'none' }">
						<form:input path="transactor.name" htmlEscape="false" maxlength="200" class="input-xlarge form-control " value="${oaFastLegal.transactor.name}" disabled="true"/>
					</div>
					<%-- <sys:treeselectOfficeUser id="transactor" name="transactor.id" value="${oaFastLegal.transactor.id}"
					labelName="transactor.name" labelValue="${oaFastLegal.transactor.name}" title=""
					url="/sys/office/getOfficeUser?" cssClass="form-control valid" 
					setRootId="" disabled=""
					checked="false" allowInput="false" allowClear="true" areaId='${oaFastLegal.caseCounty.id}'  isUser="0" type='${oaFastLegal.caseClassify}' 
					townId = '${oaFastLegal.caseTown.id}' --%>
				</div>
			</div>
		</div>
		<div class="row">
				<h3>承办人填写</h3>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">处理结果：</label>
				</div>
			</div>
			<div class="col-xs-8">
				<div class="form-group transverse">
					<form:textarea path="handleResult" htmlEscape="false" rows="8" class="form-control valid " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_banli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
				<div class="col-xs-3">
					<div class="control-group">
						<label class="control-label">案件相关文件：</label>
						<div class="controls">
							<form:hidden id="files" path="caseFile" htmlEscape="false"
								maxlength="255" class="input-xlarge" value="${oaFastLegal.caseFile}"/>
							<sys:ckfinder input="files" type="files" uploadPath="/oa/act"
								selectMultiple="true" readonly="${oaFastLegal.act.taskDefKey ne 'fast_banli'}" />
						</div>
					</div>
				</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:act:oaFastLegal:edit">
			       <input id="btnSubmit" class="btn btn-primary" type="submit" value="提交" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
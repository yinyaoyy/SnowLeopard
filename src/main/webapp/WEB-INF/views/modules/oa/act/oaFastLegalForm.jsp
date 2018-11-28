<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法律服务直通车管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			jQuery.validator.addMethod('checkFullName', function(
								value, element) {
							return this.optional(element)
									|| /[^\u0000-\u00FF]/.test(value);
						}, "姓名必须是中文");
						//验证手机
						jQuery.validator.addMethod('checkPhoneNum', function(
								value, element) {
							return this.optional(element)
									|| /^1[34578]\d{9}$/.test(value);
						}, "请填写正确的手机号码");
						jQuery.validator
								.addMethod(
										"checkPaperNum",
										function(value, element) {
											return this.optional(element)
													|| /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
															.test(value);
										}, "请输入正确的身份证号");

						$("#inputForm")
								.validate(
										{
											rules : {
												accuserName : {
													required : true,
													checkFullName : true,
													rangelength : [ 2, 20 ]
												},
												accuserPhone : {
													required : true,
													checkPhoneNum : true
												},

												accuserIdCard : {
													required : true,
													checkPaperNum : true
												},
												accuserBirthday : {
													required : true
												},
												accuserCounty : {
													required : true
												},
												accuserEthnic : {
													required : true
												},
												caseClassify : {
													required : true
												},
												caseType : {
													required : true
												},
												caseCounty : {
													required : true
												},
												caseTown : {
													required : true
												},
												caseTitle : {
													required : true
												},
												caseReason: {
													required : true
												},
												office: {
													required : true
												}

											},
											messages : {
												accuserName : {
													required : '姓名不能为空',
													checkUserName : '姓名必须是中文',
													rangelength : '姓名必须是2-20字之间'
												},
												accuserPhone : {
													required : '手机号码不能为空',
													checkPhoneNum : '请填写正确的手机号码格式'
												},
												accuserIdCard : {
													required : '身份证号不能为空'
												},
												accuserBirthday : {
													required : "出生日期不能为空"
												},
												accuserCounty : {
													required : "地区不能为空"
												},
												accuserEthnic : {
													required : "民族不能为空"
												},
												caseClassify : {
													required : "案件类别不能为空"
												},
												caseType : {
													required : "案件类型不能为空"
												},
												caseCounty : {
													required : "案发地区不能为空"
												},
												caseTown : {
													required : "案发乡镇不能为空"
												},
												caseTitle : {
													required : "案件标题不能为空"
												},
												caseReason: {
													required : "案件内容不能为空"
												},
												office: {
													required : "机构不能为空"
												}
												
											},
											submitHandler : function(form) {
												loading('正在提交，请稍等...');
												form.submit();
											},
											errorContainer : "#messageBox",
											errorPlacement : function(error,
													element) {
												$("#messageBox").text(
														"输入有误，请先更正。");
												if (element.is(":checkbox")
														|| element.is(":radio")
														|| element
																.parent()
																.is(
																		".input-append")) {
													error.appendTo(element
															.parent().parent());
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
			$("#btnSave").on("click",function(){
				$('#inputForm').attr('action','${ctx}/oa/act/oaFastLegal/save');
				$('#inputForm').submit();
			});
			$("#btnReturn").on("click",function(){
				$('#flag').val('no')
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
		<li class="active"><a href="${ctx}/oa/act/oaFastLegal/form?id=${oaFastLegal.id}">案件办理直通车<shiro:hasPermission name="oa:act:oaFastLegal:edit">${not empty oaFastLegal.id?'受理':'添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:act:oaFastLegal:edit">查看</shiro:lacksPermission></a></li>
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
			<div class="col-xs-12 bg-info bg-info-title">受理信息</div>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受理人</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="acceptMan.name" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">受理人工号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="acceptManCode" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件受理编号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="caseAcceptCode" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">申请人基本情况</div>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="accuserName" htmlEscape="false" maxlength="64" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="accuserIdCard" htmlEscape="false" maxlength="18" class="input-xlarge form-control" onchange="idCardChange()" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
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
					<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="accuserSex" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
					<form:option value="">请选择</form:option>
						<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">出生日期</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="accuserBirthday" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaFastLegal.accuserBirthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">手机号</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="accuserPhone" htmlEscape="false" maxlength="64" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在地区</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<sys:treeselect id="accuserCounty" name="accuserCounty.id" value="${oaFastLegal.accuserCounty.id}" 
					labelName="accuserCounty.name" labelValue="${oaFastLegal.accuserCounty.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}" parentId="xmArea"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">所在乡镇</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<sys:treeselect id="accuserTown" name="accuserTown.id" value="${oaFastLegal.accuserTown.id}" 
					labelName="accuserTown.name" labelValue="${oaFastLegal.accuserTown.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" parentId="accuserCounty" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">案件基本情况</div>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件类别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="caseClassify" class="input-xlarge form-control" onchange="caseClassChange()" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
					<form:option value="">请选择</form:option>
						<form:options items="${fns:getOneDictList('oa_case_classify')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件类型</label>
				</div>
			</div>
			<div class="col-xs-3">
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
					<label class="control-label">案发地区</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<div style="display:none">
						<input id='xmAreaId' value='5'></input>
					</div>
					<sys:treeselect id="caseCounty" name="caseCounty.id" value="${oaFastLegal.caseCounty.id}" 
					labelName="caseCounty.name" labelValue="${oaFastLegal.caseCounty.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false"  disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"   parentId="xmArea"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案发乡镇</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<sys:treeselect id="caseTown" name="caseTown.id" value="${oaFastLegal.caseTown.id}" 
					labelName="caseTown.name" labelValue="${oaFastLegal.caseTown.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
					allowClear="false" notAllowSelectParent="false" parentId="caseCounty" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件标题：</label>
				</div>
			</div>
			<div class="col-xs-11">
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
			<div class="col-xs-11">
				<div class="form-group transverse">
					<form:textarea path="caseReason" htmlEscape="false" rows="8" class="form-control valid " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">案件性质</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件发生时间</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<input name="caseTime" type="text" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaFastLegal.caseTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及人数</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:input path="caseInvolvecount" htmlEscape="false" maxlength="8" class="input-xlarge form-control " disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">涉及金额</label>
				</div>
			</div>
			<div class="col-xs-3">
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
					<label class="control-label">受理方式</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="caseSource" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('fast_case_source')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">案件严重等级</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<form:select path="caseRank" class="input-xlarge form-control required" disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}">
						<option value="" htmlEscape="false">请选择</option>
						<form:options items="${fns:getDictList('case_rank')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">选择案件承办方</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
					<label class="control-label">机构</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
					<sys:treeselect id="office" name="office.id" value="${oaFastLegal.office.id}" labelName="office.name" labelValue="${oaFastLegal.office.name}"
					title="科室" url="/oa/act/oaFastLegal/getOfficeTree?isUser=0&&" cssClass=" form-control  valid " allowClear="true" notAllowSelectParent="true" procDefKey="fast_legal"
					disabled="${(oaFastLegal.act.taskDefKey ne 'fast_shouli')?'true':'false'}"/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:act:oaFastLegal:edit">
				   <input id="btnSave" class="btn btn-primary" type="submit" value="保存"/>&nbsp;
			       <input id="btnSubmit" class="btn btn-primary" type="submit" value="受理" />&nbsp;
			       <input id="btnReturn" class="btn btn-primary" type="submit" value="退回" />&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
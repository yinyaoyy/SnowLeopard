<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				rules : {
					startTime : {
						required : true
					},
					endTime : {
						required : true
					},
					allTime : {
						required : true
					},
					leaveType : {
						required : true
					},
					reason : {
						required : true
					},
					oaLeaveRecordNames: {
		                required: true
		            }

				},
				messages : {
					startTime : {
						required: '开始时间不能为空'
					},
					endTime : {
						required : '结束时间不能为空'
					},
					allTime : {
						required : '请假总时间不能为空'
					},
					leaveType : {
						required : '请选择请假类型'
					},
					reason : {
						required : '请假理由不能为空'
					},
					oaLeaveRecordNames: {
		                required: '审批人不能为空'
		            }

				},
				submitHandler: function(form){
					$(form).find("#btnSubmit").attr("disabled", true);
					  form.submit(); 
					  loading('正在提交，请稍等...');
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
		function timeChange(obj){
			var startTime=$("#startTime").val();
			var endTime=$("#endTime").val();
			if(endTime!=null&&endTime!=""&&startTime!=null&&startTime!=""){
				var time=DateDiff(endTime,startTime);
				if(time<=0){
					toVail("开始时间不能大于结束时间","warning");
					$("#allTime").val("");
				}else{
					$("#allTime").val(time+"天");
				}
			}
		}
		function DateDiff(d1,d2){
			  var day = 24 * 60 * 60 *1000;
			try{  
			    var dateArr = d1.split("-");
			  var checkDate = new Date(d1);
			    //checkDate.setFullYear(dateArr[0], dateArr[1]-1, dateArr[2]);
			  var checkTime = checkDate.getTime();
			  
			  var dateArr2 = d2.split("-");
			  var checkDate2 = new Date(d2);
			    //checkDate2.setFullYear(dateArr2[0], dateArr2[1]-1, dateArr2[2]);
			  var checkTime2 = checkDate2.getTime();
			   
			  var cha = (checkTime - checkTime2)/day; 
			    return cha.toFixed(1);
			  }catch(e){
			  return false;
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
		<li class="active"><a href="${ctx}/oa/oaLeave/form?id=${oaLeave.id}"><shiro:hasPermission name="oa:oaLeave:edit">${not empty oaLeave.id?'办理':'申请'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaLeave:edit">查看</shiro:lacksPermission>请假单</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaLeave" action="${ctx}/oa/oaLeave/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">科室：</label>
				${fns:getUser().office.name}
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职位：</label>
				${fns:getUser().userType}
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
				${fns:getUser().name}
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">开始时间：</label>
				<input name="startTime" id="startTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLeave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" onchange="timeChange(this)"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">结束时间：</label>
				<input name="endTime" id="endTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaLeave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" onchange="timeChange(this)"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请假总时间：</label>
				<form:input path="allTime" htmlEscape="false" maxlength="10" class="input-xlarge form-control " readonly="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			       <label class="control-label">请假类型：</label>
					<form:select path="leaveType" class="form-control valid yy" id="yy">
						<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
						<form:options items="${fns:getDictList('oa_leave_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">审批人</label>
					<sys:treeselect id="oaLeaveRecordIds" name="oaLeaveRecordIds"
						value="${oaLeave.oaLeaveRecordIds}"
						labelName="oaLeaveRecordNames"
						labelValue="${oaLeave.oaLeaveRecordNames}" title="用户"
						url="/sys/office/treeDataAll?type=3"
						cssClass="form-control required valid" notAllowSelectParent="true"
						checked="true" />
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">请假理由：</label>
			     <form:textarea path="reason" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">附件：</label>
					<form:hidden id="file" path="file" htmlEscape="false"
						maxlength="255" class="input-xlarge" />
							<sys:ckfinder input="file" type="files" uploadPath="/oa/leave"
								selectMultiple="true" />
				</div>
			</div>
		</div>
		<hr>
		<div class="row">
		   <div class="col-xs-3">
			<shiro:hasPermission name="oa:oaLeave:edit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			&nbsp;<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		   </div>
		</div>
	</form:form>
</body>
</html>
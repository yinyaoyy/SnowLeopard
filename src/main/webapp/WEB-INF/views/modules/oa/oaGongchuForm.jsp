<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>公出单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			jQuery.validator.addMethod('ifAgree', function (value, element) {
		       var idsArr=$("#oaGongchuRecordIdsId").val().split(',').length;
				if(idsArr!=4){
					return false;
				}else{
					return true;
				}
		    }, "审批人必须为4个");
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
					gongchuType : {
						required : true
					},
					reason : {
						required : true
					},
					oaGongchuRecordNames: {
						ifAgree: true
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
						required : '公出总时间不能为空'
					},
					gongchuType : {
						required : '请选择公出类型'
					},
					reason : {
						required : '公出理由不能为空'
					},
					oaGongchuRecordNames: {
						ifAgree: '审批人必须为4个'
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
			  var checkTime = checkDate.getTime();
			  var dateArr2 = d2.split("-");
			  var checkDate2 = new Date(d2);
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
		<li class="active"><a href="${ctx}/oa/oaGongchu/form?id=${oaGongchu.id}">申请公出单</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaGongchu" action="${ctx}/oa/oaGongchu/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">科室：</label>
			<form:input path="fns:getUser().office.name" htmlEscape="false" maxlength="10" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">职位：</label>
			<form:input path="fns:getUser().userType" htmlEscape="false" maxlength="10" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">姓名：</label>
			<form:input path="fns:getUser().name" htmlEscape="false" maxlength="10" class="input-xlarge form-control " disabled="true"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">开始时间：</label>
				<input name="startTime"  id="startTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaGongchu.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" onchange="timeChange(this)"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">结束时间：</label>
				<input name="endTime"  id="endTime" type="text" readonly="readonly" maxlength="20" class="form-control "
					value="<fmt:formatDate value="${oaGongchu.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" onchange="timeChange(this)"/>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">公出总时间：</label>
				<form:input path="allTime" htmlEscape="false" maxlength="10" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			       <label class="control-label">公出类型：</label>
					<form:select path="gongchuType" class="form-control valid yy" id="yy">
						<form:option value="" label="${fns:getLanguageContent('sys_select_select')}" />
						<form:options items="${fns:getDictList('oa_gongchu_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">审批人</label>
					<sys:treeselect id="oaGongchuRecordIds" name="oaGongchuRecordIds"
						value="${oaGongchu.oaGongchuRecordIds}"
						labelName="oaGongchuRecordNames"
						labelValue="${oaGongchu.oaGongchuRecordNames}" title="用户"
						url="/sys/office/treeDataAll?type=3"
						cssClass="form-control valid" notAllowSelectParent="true"
						checked="true" />
					<span class="help-inline"></span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			<label class="control-label">公出理由：</label>
				<form:textarea path="reason" htmlEscape="false" rows="8" maxlength="255" class="input-xlarge form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
			       <label class="control-label">附件：</label>
				   <form:hidden id="file" path="file" htmlEscape="false" maxlength="255" class="input-xlarge" />
				   <sys:ckfinder input="file" type="files" uploadPath="/oa/leave" selectMultiple="true" />
				</div>
			</div>
		</div>
		<hr>
		<div class="row">
		   <div class="col-xs-3">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			&nbsp;<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		   </div>
		</div>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>法援申请管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.reClosely {
		position: relative;
		width: 60%;
		margin-top: 5px;
		margin-left: 9px;
		height: 150px;
		display: none;
		margin-bottom: 20px;
	}
	
	.reClosely .closelyTextarea {
		width: 60%;
		height: 100%;
		box-sizing: border-box;
		resize: none;
		padding: 20px 100px 5px 5px;
	}
	
	.reClosely .closelyTextareaBtn {
		position: absolute;
		background: black;
		color: white;
		margin-right: 15px;
		text-align: center;
		width: 55px;
		height: 30px;
		line-height: 20px;
		cursor: pointer;
		top: 90px;
		left: 50%;
	}
	
	.reClosely .closelyTextareaBan {
		position: absolute;
		background: black;
		color: white;
		margin-right: 15px;
		text-align: center;
		width: 55px;
		height: 30px;
		line-height: 20px;
		cursor: pointer;
		top: 130px;
		left: 50%;
	}
	
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
		    jQuery.validator.addMethod("checkIdCard", function(value,element) {
		    	  var idCard = element.value;
		    	  var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子  
		    	  var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值.10代表X 
		    	  var isTrueValidateCodeBy18IdCard = function(idCard) {
		    	    var sum = 0; // 声明加权求和变量  
		    	    var a_idCard=idCard.split("");
		    	    if (a_idCard[17].toLowerCase() == 'x') {
		    	      a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作
		    	    }
		    	    for ( var i = 0; i < 17; i++) {
		    	      sum += Wi[i] * a_idCard[i];// 加权求和
		    	    }
		    	    if (a_idCard[17] == ValideCode[sum % 11]) {
		    	      return true;
		    	    } else {
		    	      return false;
		    	    }
		    	  };
		    	  var isValidityBrithBy18IdCard = function(idCard18){
		    	    var year = idCard18.substring(6,10);
		    	    var month = idCard18.substring(10,12);
		    	    var day = idCard18.substring(12,14);
		    	    temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));
		    	    // 这里用getFullYear()获取年份，避免千年虫问题
		    	    if(temp_date.getFullYear()!=parseFloat(year)
		    	          ||temp_date.getMonth()!=parseFloat(month)-1
		    	          ||temp_date.getDate()!=parseFloat(day)){
		    	            return false;
		    	    }else{
		    	      temp_date = year+"-"+month+"-"+day;
		    	        return true;
		    	    }
		    	  };
		    	  var isValidityBrithBy15IdCard = function(idCard15){
		    	    var year = idCard15.substring(6,8);
		    	    var month = idCard15.substring(8,10);
		    	    var day = idCard15.substring(10,12);
		    	    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));
		    	    // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法
		    	    if(temp_date.getYear()!=parseFloat(year)
		    	            ||temp_date.getMonth()!=parseFloat(month)-1
		    	            ||temp_date.getDate()!=parseFloat(day)){
		    	              return false;
		    	    }else{
		    	      return true;
		    	    }
		    	  };
		    	  if(idCard){
		    	    if(/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test(idCard)){
		    	      if(idCard.length == 15 && isValidityBrithBy15IdCard(idCard)){
		    	        return true;
		    	      }else if(idCard.length == 18 && isValidityBrithBy18IdCard(idCard) && isTrueValidateCodeBy18IdCard(idCard)){
		    	        return true;
		    	      }else{
		    	        return false;
		    	      }
		    	    }else{
		    	      return false;
		    	    }
		    	  }
		    	}, "请输入正确的身份证号");  
			$("#inputForm").validate({
				rules: {
					idCard: {
		                required: true,
		                checkIdCard: true
		            },
		            name:{
		            	required: true
		            }
				},
				messages: {
					idCard: {
		                required: '身份证号不能为空'
		            },
		            name:{
		            	required: '姓名不能为空'
		            }
				},
				submitHandler: function(form){
					if(checkSubmit()){
						if(caseType()){
							loading('正在提交，请稍等...');
							form.submit();
						}
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
		function checkSubmit(){
			if(${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding' } && $("#lawyerId").val()=="" && $("#legalPersonId").val()==""){
				$("#flag").val("no");//申请人没有指定律师或者法律服务工作者
				return true;
			}
			if(${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding' } && ($("#lawyerId").val()!="" || $("#legalPersonId").val()!="")){
				$("#flag").val("yes");//申请人指定了律师或者是法律服务工作者
				if($("#lawyerId").val()!="" && $("#legalPersonId").val()!=""){
					//当申请人同时指定了两者时，给与一个小提示
					return confirmx("您选择律师和法律服务工作者，将优先安排律师为您办理",function(){return true;},function(){return false;});
				}
				return true;
			}
			if($("#areaId").val()==""){
				toVail("所在区域不能为空","warning");
				return false;
			}
			if($("#proxyName").val()!="" || $("#proxyIdCard").val()!=""){
				if($("#proxyName").val()==""){
					toVail("代理人姓名不能为空","warning");
					return false;
				}
				if(!(/^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test($("#proxyIdCard").val()))){
					toVail("代理人身份证不正确","warning");
					return false;
				}
				if($("input[name='proxyType']:checked").length==0){
					toVail("请选择代理人类型","warning");
					return false;
				}
			}
			return true;
		}
		function returnBack(){
			//驳回操作
			if($.trim($('#act\\.comment').val())==''){
				toVail("请填写销毁信息","warning");
				return false;
			}
			//设置驳回
			$('#flag').val('no');
			$('#inputForm').submit();
		}
		function caseType(){
			if(${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'} && $("#lawyerId").val()!="" && $("#legalPersonId").val()!=""){
				toVail("不能同时选择律师和基层法律服务工作者","warning");
				return false;
			}
			return true;
		}
		function back(type){
			if(type == 0){
				$('#yj').show();
			}else{
				$('#yj').hide();
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
		<li class="active"><a href="${ctx}/oa/act/oaLegalAid/form?id=${oaLegalAid.id}">法援申请<shiro:hasPermission name="oa:act:oaLegalAid:edit">${not empty oaLegalAid.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:act:oaLegalAid:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaLegalAid" action="${ctx}/oa/act/oaLegalAid/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="legalAidType"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey" class="taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">申请人基本情况</div>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group" >
				<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding' }"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">性别</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
				<form:select path="sex" class="input-xlarge form-control required"  disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}">
			 <form:option value=""> 请选择</form:option>
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
				<input name="birthday" type="text" readonly="readonly" maxlength="20" class="form-control required"
					value="<fmt:formatDate value="${oaLegalAid.birthday}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"
				 ${(oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding')?'disabled="disabled"':''}/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">所在区域</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
				<sys:treeselect id="area" name="area.id" value="${oaLegalAid.area.id}" 
					labelName="area.name" labelValue="${oaLegalAid.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " 
				 disabled="${(oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding')?'disabled':''}"
					allowClear="false" notAllowSelectParent="false"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">民族</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
				<form:select path="ethnic" class="input-xlarge form-control required"
			  	disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"  >
				 	<form:option value=""> 请选择</form:option>
					<form:options items="${fns:getDictList('ethnic')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">涉及蒙语</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
				<form:select path="hasMongol" class="input-xlarge form-control required"
				  disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}">>
				 <form:option value=""> 请选择</form:option>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
				<form:input path="idCard" htmlEscape="false" maxlength="18" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">户籍所在地</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
				<form:input path="domicile" htmlEscape="false" maxlength="600" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">住所地（经常居住地）</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
				<form:input path="address" htmlEscape="false" maxlength="600" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">邮政编码</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group transverse">
				<form:input path="postCode" htmlEscape="false" maxlength="6" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">联系电话</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group transverse">
				<form:input path="phone" htmlEscape="false" maxlength="20" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">工作单位</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
				<form:input path="employer" htmlEscape="false" maxlength="600" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>	
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">代理人基本信息</div>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">姓名</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group transverse">
				<form:input path="proxyName" htmlEscape="false" maxlength="200" class="input-xlarge form-control "
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
			<div class="col-xs-8">
				<div class="form-group">
					<p id="showRole">
							<c:forEach items="${fns:getDictList('legal_aid_proxy_type')}" var="pt">
								<label class="checkbox-inline">
						    <input type="radio" name="proxyType" value="${pt.value }" 
				 ${(oaLegalAid.proxyType eq pt.value)?'checked="checked"':''}
				 ${(oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding')?'disabled="disabled"':''}
						    />${pt.label}
						    </label>
							</c:forEach>
						</p>
				<!-- <form:radiobuttons path="proxyType" items="${fns:getDictList('legal_aid_proxy_type')}" itemLabel="label" itemValue="value" htmlEscape="false" class=" form-control "/> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">身份证号</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
				<form:input path="proxyIdCard" htmlEscape="false" maxlength="20" class="input-xlarge form-control "
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">案件基本情况</div>
		</div>	
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">案情标题</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group transverse">
				<form:input path="caseTitle" htmlEscape="false" maxlength="200" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">案情类型</label>
				</div>
			</div>
			<div class="col-xs-5">
				<div class="form-group transverse">
				<form:select path="caseType" class="input-xlarge form-control required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}">
				 <form:option value=""> 请选择</form:option>
					<form:options items="${fns:getChildrenDictList('oa_case_classify','legal_aid')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">案情及申请理由概述</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group transverse">
				<form:textarea path="caseReason" htmlEscape="false" rows="8" class="form-control valid required"
				 disabled="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">案件相关文件</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
				<input type="hidden" id="caseFile" name="caseFile" value="${oaLegalAid.caseFile}" />
				<sys:ckfinder input="caseFile" type="files" uploadPath="/oa/legalAid" selectMultiple="true" 
				 readonly="${oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding'}"/>
				</div>
			</div>
		</div>
		<!-- 只有当申请的审核通过后，申请人才可以选择律师或者是法律服务工作者 -->
		<div style="display:${(oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding')?'block':'none' }">
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label">律师库</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
			<sys:treeselectOfficeUser id="lawyer" name="lawyer.id" value="${oaLegalAid.lawyer.id}"
					labelName="lawyer.name" labelValue="${oaLegalAid.lawyer.name}" title="律师"
					url="/sys/office/getOfficeUser?type=2" cssClass="form-control valid"
					setRootId="${officeLawyerOffice}"
					notAllowSelectParent="true" checked="false" allowInput="false" allowClear="true" areaId="${oaLegalAid.area.id }" isUser="1"/>
				</div>
			</div>
		</div>
		<div class="row" style="display:${oaLegalAid.legalAidType=='1'?'block':'none'}">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">基层法律服务工作者</label>
				</div>
			</div>
			<div class="col-xs-11">
				<div class="form-group">
			<sys:treeselectOfficeUser id="legalPerson" name="legalPerson.id" value="${oaLegalAid.legalPerson.id}"
				labelName="legalPerson.name" labelValue="${oaLegalAid.legalPerson.name}" title="基层法律服务工作者"
				url="/sys/office/getOfficeUser?type=8" cssClass="form-control valid"
				setRootId="${officeLegalService}"
				notAllowSelectParent="true" checked="false" allowInput="false" allowClear="true" areaId="${oaLegalAid.area.id }" isUser="1"/>
				</div>
			</div>
		</div>
		</div>
		<!-- 填写驳回意见 -->
		<%-- <c:choose> 
			<c:when test="${oaLegalAid.act.taskDefKey eq 'aid_update' }">
		<div class="row">
			<div class="col-xs-1">
				<div class="form-group">
				<label class="control-label" style="text-align:left;">填写驳回意见：</label>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
				<form:textarea path="act.comment" htmlEscape="false" rows="8" maxlength="255" class="input-xxlarge form-control "/>
				</div>
			</div>
		</div>
		</c:when>
		</c:choose> --%>
		<div id="yj" class="reClosely">
			<label class="control-label" style="text-align:left;">填写退回意见：</label>
			<br/>
			<form:textarea path="act.comment" class="closelyTextarea" rows="10" maxlength="257"/>
			<input class="closelyTextareaBtn" type="button" value="确定" onclick="returnBack()"/>
			<input class="closelyTextareaBan" type="button" value="取消" onclick="back('1')"/>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:act:oaLegalAid:edit">
			    <c:choose> 
					<c:when test="${oaLegalAid.act.taskDefKey eq '' or oaLegalAid.act.taskDefKey eq 'aid_apply_zhiding' }">
						<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存" />&nbsp;
					</c:when>
					<c:when test="${oaLegalAid.act.taskDefKey eq 'aid_update' }">
						<input id="btnSubmit2" class="btn btn-primary" type="submit" value="重新申请" onclick="$('#flag').val('yes')"/>&nbsp;
						<input id="btn3" class="btn btn-primary" type="button" value="销毁" onclick="back('0')"/>&nbsp;
					</c:when>
			    </c:choose>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
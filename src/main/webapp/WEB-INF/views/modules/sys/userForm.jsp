<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
var roleList=[
    <c:forEach items="${user.partTimeList}" var="role">
    {
     roleId:"${role.roleId}",
     roleName:"${role.roleName}",
     officeId:"${role.officeId}",
     officeName:"${role.officeName}",
     isMain:"${role.isMain}",},
  </c:forEach>];
  
$(document).ready(function() {
		$("#no").focus();
		showSelectRole();
		//初始化的验证
		jQuery.validator.addMethod('checkFullName', function (value, element) {
	        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
	    }, "姓名必须是中文");
	    //验证手机
	   /*  jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
	        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
	    }, "请填写正确的手机号码"); */
	    jQuery.validator.addMethod("checkQQ", function(value, element) {       
	         return this.optional(element) || /^[1-9][0-9]{4,9}$/.test(value);       
	    }, "请添加正确的QQ号");   
	    jQuery.validator.addMethod("checkPaperNum", function(value, element) {       
	         return this.optional(element) || /^[1-9]([0-9]{13}|[0-9]{16})([0-9]|X)$/.test(value);       
	    }, "请输入正确的身份证号");  
		$("#inputForm").validate({
			rules: {
				 loginName: {
	                required: true,
					remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')
				}, 
				newPassword: {
	                rangelength: [3, 20]
	            },
				confirmNewPassword: {
	                rangelength: [3, 20],
					equalTo: "#newPassword"
	            },
				name: {
	                required: true,
	                checkFullName: true,
	                rangelength: [2, 20]
	            },
	            papernum: {
	                required: true,
	                checkPaperNum: true
	            },
	            office: {
	                required: true
	            }
			},
			messages: {
				 loginName: {
	                required: '用户登录名不能为空',
					remote: "用户登录名已存在"
				}, 
				newPassword: {
	                required: '密码不能为空',
	                rangelength:'密码必须是3-20字之间'
				},
				confirmNewPassword: {
	                required: '密码不能为空',
	                rangelength:'密码必须是3-20字之间',
	                equalTo:"两次密码不一致"
				},
				name: {	
	                required: '姓名不能为空',
	                checkUserName: '姓名必须是中文',
	                rangelength: '姓名必须是2-20字之间'
	            },
			/* 	no: {
					required:'工号不能为空',
	                rangelength: '工号必须是1-100字之间'
				}, */
	          /*   birthday: {
	                required: '出生日期不能为空',
	                rangelength: '请选择正确的时间格式'
	            }, */
	         /*    'userExpand.qq': {
	                required: 'QQ不能为空',
	                checkQQ:'请填写正确的QQ号'
	            },
	            address: {
	                required: '地址不能为空',
	                rangelength: '长度在3-50字之间'
	            }, */
	          /*   mobile: {
	                required: '手机号码不能为空',
	                checkPhoneNum: '请填写正确的手机号码格式'
	            }, */
	          /*   email: {
	                required: '邮箱地址不能为空',
	                email: '请填写正确的邮箱格式'
	            }, */
	            papernum: {
	                required: '身份证号不能为空',
	                checkPaperNum:'请输入正确的身份证号'
	            },
	            office: {
	                required: '科室不能为空'
	            }
			},
			submitHandler: function(form){
				var imgUrl = "";
				if($("#photo").val().indexOf("userProfile_default.png")!=-1||$("#photo").val()==""){
					imgUrl = top.$.createUserNameImage($("#name").val()).src;
				}else{
					imgUrl = $("#photo").val();
				}
				$("#photo").val(imgUrl);
				if($("input[name='roleIdList']:checked").length<=0){
					$(".roleList").text("角色不能为空");
				}else if($("#companyName").val()==""){
					$("#companyName").attr("placeholder","机构不能为空");
				}else if($("#officeName").val()==""){
					$("#officeName").attr("placeholder","科室不能为空");
				}/* else if($("#areaId").val()==""){
					toVail("所在区域不能为空","warning");
				} */else{
					$(form).find("#modifyUserInfo").attr("disabled", true);
					  form.submit(); 
					loading('正在提交，请稍等...');
				}
			}
		});
		$("#modifyUserInfo").click(function(){  
			if($("input[name='roleIdList']:checked").length<=0){
				$(".roleList").text("角色不能为空");
			}
		});
		$("input[name='roleIdList']").click(function(){
			if($("input[name='roleIdList']:checked").length<=0){
				$(".roleList").text("角色不能为空");
			}else{
				$(".roleList").text("");
			}
		});
		$("#assignButton").click(function(){
			top.$.jBox.open("iframe:${ctx}/sys/user/userToPartTime?id=${user.id}", "分配兼职",810,$(top.document).height()-240,{
				buttons:{"确定分配":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择科室，然后为列出的机构分配角色。",submit:function(v, h, f){
					var roleLists = h.find("iframe")[0].contentWindow.roleList;
					//var ids = h.find("iframe")[0].contentWindow.ids;
					//nodes = selectedTree.getSelectedNodes();
					if (v=="ok"){
						roleList=roleLists;
						showSelectRole();
				    	return true;
					} else if (v=="clear"){
						//roleList=new Array();
						//showSelectRole();
						h.find("iframe")[0].contentWindow.clearAssign();
						return false;
	                }
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
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
	};
	
	function  showSelectRole(){//回显兼职信息
		console.log(roleList)
		var htmlArray=new Array();
	    var roleOfficeIds='[';
	    var j=0;
		for(var i=0;i<roleList.length;i++){
			if(roleList[i].isMain==1){
				if(j==0){
					roleOfficeIds+='{"roleId":"'+roleList[i].roleId+'","officeId":"'+roleList[i].officeId+'","isMain":"1"}';
				}else{
					roleOfficeIds+=',{"roleId":"'+roleList[i].roleId+'","officeId":"'+roleList[i].officeId+'","isMain":"1"}';
				}
				j++;
				var flag=false;
				for(var k=0;k<htmlArray.length;k++){
	               if(roleList[i].officeId==htmlArray[k].officeId){
	            	   var aa=htmlArray[k];
	            	   htmlArray[k].roleList.push({"roleId":roleList[i].roleId,"roleName":roleList[i].roleName});
	            	   flag=true;
	            	   break;
	               }				
				}
				if(!flag){
					htmlArray.push({"officeId":roleList[i].officeId,"officeName":roleList[i].officeName,"roleList":[{"roleId":roleList[i].roleId,"roleName":roleList[i].roleName}]});	
				}
			}
		}
		roleOfficeIds+=']';
		var roleHtml='';
		for(var i=0;i<htmlArray.length;i++){
			roleHtml+=htmlArray[i].officeName+"---";
			var list=htmlArray[i].roleList;
			for(var k=0;k<list.length;k++){
				if(k==0){
					roleHtml+=list[k].roleName;	
				}else{
					roleHtml+=","+list[k].roleName;	
				}
			}
			roleHtml+='<br/>';
		}
		$("#partTimeHtml").html(roleHtml);
		$("#roleOfficeIdList").val(roleOfficeIds);
	};
</script>
<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css">
<link href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css" rel='stylesheet' type="text/css" />
<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css" rel='stylesheet' type="text/css" />
<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-forms.css" rel='stylesheet' type="text/css" />
<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css" rel='stylesheet' type="text/css" />
<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-skins.css" rel='stylesheet' type="text/css" />
<link href="${ctxStatic}/flavr/css/flavr.css" type="text/css" rel="stylesheet" />
<style>
.oyh {
	overflow-y: hidden;
}
</style>
</head>
<body class="oyh">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}">用户<shiro:hasPermission
					name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="user"
		action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<form:hidden path="roleOfficeIdList" />
		<input type="hidden" name="photo" id="photo" value="${user.photo}">
		<sys:message content="${message}" />
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">基本信息</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="name">姓名aa</label>
					<form:input path="name" htmlEscape="false" maxlength="50"
						class="form-control valid" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="name">登录名</label>
					<form:input path="loginName" htmlEscape="false" maxlength="50"
						class="form-control valid" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">性别：</label>
					<form:select path="userExpand.sex" id="sex" 
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('sex')}" itemLabel="label"
							itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<%-- <div class="col-xs-3">
					<div class="form-group">
					<label class="control-label" for="loginName">登录名</label>
						<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
						<form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div> --%>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">曾用名：</label>
					<form:input path="userExpand.usedName" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="newPassword">密码</label> <input
						id="newPassword" name="newPassword" type="password" value=""
						class="${empty user.id?'required':''} form-control valid"
						aria-required="true" aria-invalid="false" autocomplete="off" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="confirmNewPassword">确认密码</label>
					<input id="confirmNewPassword" name="confirmNewPassword"
						type="password" value=""
						class="${empty user.id?'required':''} form-control valid"
						aria-required="true" aria-invalid="false" autocomplete="off" />
				</div>
			</div>
			<div class="col-xs-3">
					<div class="form-group">
						<label class="control-label" for="no">工号</label>
						<form:input path="no" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div> 
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="papernum">身份证号</label>
					<form:input path="papernum" id="papernum" htmlEscape="false" onblur="autoBirthdaySexByIdCard(this,'birthday','sex');"
						maxlength="18" class="form-control valid" />
				</div>
			</div>
			
		</div>
		<div class="row">
		   <div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="birthday">出生日期</label> <input
						type="text" class="form-control" name="birthday" id="birthday"
						readonly="readonly"
						value="<fmt:formatDate value='${user.birthday}' pattern="yyyy-MM-dd"/>"
						style="background-color: white; cursor: auto;">
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">民族：</label>
					<form:select path="userExpand.ethnic"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('ethnic')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>

			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">政治面貌：</label>
					<form:input path="userExpand.politicalFace" htmlEscape="false"
						maxlength="30" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="userExpand.joinTime">入党（团）时间：</label>
					<input name="userExpand.joinTime" type="text" readonly="readonly"
						class="form-control "
						value="<fmt:formatDate value="${user.userExpand.joinTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
		</div>


		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">联系信息</div>
		</div>
		<div class="row">
			<c:choose>
				<c:when test="${empty user.mobile||user.isShow =='1'}">
					<div class="col-xs-3">
						<div class="form-group">
							<label class="control-label" for="mobile">手机号码</label>
							<form:input id="mobile" path="mobile" htmlEscape="false"
								maxlength="50" class="form-control valid" aria-required="true"
								aria-invalid="false" />
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-xs-3" style="display: none;">
						<div class="form-group">
							<label class="control-label" for="mobile">手机号码</label>
							<form:input id="mobile" path="mobile" htmlEscape="false"
								maxlength="50" class="form-control valid" aria-required="true"
								aria-invalid="false" />
						</div>
					</div>
				</c:otherwise>
			</c:choose>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">籍贯：</label>
					<form:input path="userExpand.nativePlace" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">出生地：</label>
					<form:input path="userExpand.birthPlace" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">教育信息</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="userExpand.schoolInfo">学校信息</label>
					<form:input path="userExpand.schoolInfo" htmlEscape="false"
						maxlength="50" class="form-control" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="userExpand.major">专业</label>
					<form:input path="userExpand.major" htmlEscape="false"
						maxlength="50" class="form-control valid" aria-required="true"
						aria-invalid="false" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label" for="userExpand.education">学历</label>
					<form:input path="userExpand.education" htmlEscape="false"
						maxlength="50" class="form-control valid" aria-required="true"
						aria-invalid="false" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">学位：</label>
					<form:input path="userExpand.degree" htmlEscape="false"
						maxlength="30" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">专业类别：</label>
					<form:input path="userExpand.professionCategory" htmlEscape="false"
						maxlength="50" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">毕业时间：</label> <input
						name="userExpand.graduationTime" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${user.userExpand.graduationTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">学习培训情况：</label>
					<form:input path="userExpand.learnTraing" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">机构信息</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">归属机构</label>
					<sys:treeselectUser id="company" name="company.id"
						cssClass="form-control" value="${user.company.id}"
						labelName="company.name" labelValue="${user.company.name}"
						title="机构" url="/sys/office/treeDataAll?type=2" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">归属科室</label>
					<sys:treeselectUser id="office" name="office.id"
						cssClass="form-control" value="${user.office.id}"
						labelName="office.name" labelValue="${user.office.name}"
						title="科室" url="/sys/office/treeDataAll?type=2"
						notAllowSelectParent="false" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">人员状态：</label>
					<form:select path="userExpand.personStatus"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('person_status')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<%-- <div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">所在区域</label>
				<sys:treeselect id="area" name="area.id" value="${user.area.id}" labelName="area.name" labelValue="${user.area.name}"
					title="区域" url="/sys/area/treeData" cssClass=" form-control  valid " allowClear="false" notAllowSelectParent="false"/>
					</div>
				</div> --%>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">参加工作时间：</label> <input
						name="userExpand.startworkTime" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${user.userExpand.startworkTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>

		</div>
		<div class="row">
			<%-- <div class="col-xs-3">
					<div class="form-group">
						<label class="control-label">职位</label>
						<form:input path="userType" htmlEscape="false" maxlength="50" class="form-control valid" aria-required="true" aria-invalid="false"/>
					</div>
				</div> --%>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">职位：</label>
					<form:select path="userType" class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('user_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<%-- <div class="col-xs-3">
					<div class="form-group">
						<label class="control-label" for="department">其他职位</label>
						<div class="form-control" style="background-color: #eeeeee">
						<c:forEach items="${user.careerList }" var="cl">
							<c:if test="${cl.isMain==0 }">
							${cl.remark }${cl.isMainDesc }
							</c:if>
						</c:forEach>
						</div>
					</div>
				</div> --%>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">身份类别：</label>
					<form:select path="userExpand.identityType"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('identity_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">在编情况：</label>
					<form:input path="userExpand.isSeries" htmlEscape="false"
						maxlength="2" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">编制类型：</label>
					<form:select path="userExpand.prepareType"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('prepare_type')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">

			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">公职人员登记时间：</label> <input
						name="userExpand.publicOfficialsRegistrationTime" type="text"
						readonly="readonly" maxlength="20" class="form-control "
						value="<fmt:formatDate value="${user.userExpand.publicOfficialsRegistrationTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">是否高配：</label>
					<form:select path="userExpand.isHigh"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('yes_no')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">记功情况：</label>
					<form:input path="userExpand.contribution" htmlEscape="false"
						maxlength="300" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">职务类别：</label>
					<form:select path="userExpand.jobCategory"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('job_category')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">职务级别：</label>
					<form:select path="userExpand.jobLevel"
						class="input-xlarge form-control ">
						<form:option value="" label="请选择" />
						<form:options items="${fns:getDictList('job_level')}"
							itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">现任职时间：</label> <input
						name="userExpand.nowWorktime" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${user.userExpand.nowWorktime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">现任职文号：</label>
					<form:input path="userExpand.nowDocNumber" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">任同职级时间：</label>
					<form:input path="userExpand.serveSamejobTime" htmlEscape="false"
						maxlength="10" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">任同职级文号：</label>
					<form:input path="userExpand.serveSamedocNumberTime"
						htmlEscape="false" maxlength="10"
						class="input-xlarge form-control " />
				</div>
			</div>

			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">奖惩情况：</label>
					<form:input path="userExpand.rewardPunishment" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">工资来源：</label>
					<form:input path="userExpand.salarySource" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">列编</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">列编标志：</label>
					<form:input path="userExpand.columnMarking" htmlEscape="false"
						maxlength="50" class="input-xlarge form-control" />
					<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">列编日期：</label> <input
						name="userExpand.columnDate" type="text" readonly="readonly"
						maxlength="20" class="form-control"
						value="<fmt:formatDate value="${user.userExpand.columnDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
					<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">列编文号：</label>
					<form:input path="userExpand.columnDocNumber" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control" />
					<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">列编渠道：</label>
					<form:input path="userExpand.columnChannel" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control" />
					<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">列编来源：</label>
					<form:input path="userExpand.columnSource" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control" />
					<!-- 	<span class="help-inline"><font color="red">*</font> </span> -->
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">减员信息</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">减员标志：</label>
					<form:input path="userExpand.layoffsSign" htmlEscape="false"
						maxlength="50" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">减员日期：</label> <input
						name="userExpand.layoffsTime" type="text" readonly="readonly"
						maxlength="20" class="form-control "
						value="<fmt:formatDate value="${user.userExpand.layoffsTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
				</div>
			</div>
			<div class="col-xs-3">

				<div class="form-group">
					<label class="control-label">减员渠道：</label>
					<form:input path="userExpand.layoffsChannel" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">减员文号：</label>
					<form:input path="userExpand.layoffsDocNumber" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">减员去向：</label>
					<form:input path="userExpand.layoffsGo" htmlEscape="false"
						maxlength="100" class="input-xlarge form-control " />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">个人简介</div>
		</div>
		<div class="row" style="height: 170px;">
			<div class="col-xs-12" style="background: none;">
				<div class="form-group">
					<label class="control-label"></label>
					<form:textarea path="userExpand.introduction" htmlEscape="false"
						rows="8" maxlength="255" class="form-control "/>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="form-group">
					<label class="control-label">用户照片：</label> <input type="hidden"
						id="userImg" name="userExpand.userImg"
						value="${user.userExpand.userImg}" />
					<sys:ckfinder input="userImg" type="thumb" uploadPath="/info/user"
						selectMultiple="false" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">权限管理</div>
		</div>
		<div class="row">
			<div class="col-xs-3" style="margin-top: -15px;">
				<div class="form-group">
					<label class="control-label">是否允许登录</label>
					<div class="input-group">
						<form:select path="loginFlag" cssClass="form-control">
							<form:options items="${fns:getDictList('yes_no')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select>
						<img src="${ctxStatic}/images/note.png" style="margin-top: 12px;">
						<p class="help-inline" style="margin-top: -18px;">“是”代表此账号允许登录，“否”则表示此账号不允许登录</p>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 bg-info bg-info-title">用户角色
					<label class="checkbox-inline" style="padding-top: 0;margin-left: 20px;"> 
					<input type="checkbox"  name="whole" onclick="check()" />全选
					</label></div>
		</div>
		<div class="row">
			<div class="col-xs-12" style="background: none; color: #2c2e2f;">
				<div class="form-group" style="font-size: 13px;">
					
					<p id="showRole">
					<c:forEach items="${allRoles}" var="role">
							<label class="checkbox-inline" style="width:200px;margin-left:0;"> <input type="checkbox"
								name="roleIdList" value="${role.id }"
								<c:forEach items="${user.roleIdList}" var="roleId" > 
						            <c:if test="${ roleId==role.id}"> 
						               checked="checked"
						            </c:if>
						        </c:forEach> />${role.name}
							</label>
						</c:forEach>
					<!-- <span class="help-inline"></span> <label
						class="roleList checkbox-inline" style="color: red; float: right;"></label> -->
					</p>
				</div>
			</div>
		</div>
		<div class="row">
		   <div class="col-xs-12 bg-info bg-info-title">兼职信息</div>
		</div>
		<div class="row">
		<div class="col-xs-2">
				<input id="assignButton" class="btn btn-primary" type="button"
					value="分配兼职" />
			</div>
			<div class="col-xs-6" id="partTimeHtml" style="padding-bottom:20px"></div>
		</div>
		<hr/>
		<div class="row">
			<div class="col-xs-6">
				<div class="form-group">
					<shiro:hasPermission name="sys:user:edit">
						<button class="btn btn-primary" id="modifyUserInfo" type="submit">保
							存</button>
					</shiro:hasPermission>
					<button class="btn btn-primary" id="modifyUserInfo" type="button"
						onclick="history.go(-1)">返 回</button>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
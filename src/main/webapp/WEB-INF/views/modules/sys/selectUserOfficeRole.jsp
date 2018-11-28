<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>分配角色</title>
<meta name="decorator" content="blank" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<script type="text/javascript">
	 // var allRole=new Array();
	  var allRole=[
          <c:forEach items="${allRoles}" var="role">
          {id:"${role.id}",
           name:"${role.name}"},
          </c:forEach>];//所有角色
       var roleList=[
              <c:forEach items="${roleList}" var="role">
              {
               userId:"${role.user.id}",
               userName:"${role.user.name}",
               roleId:"${role.role.id}",
               roleName:"${role.role.name}",
               officeId:"${role.office.id}",
               officeName:"${role.office.name}",
               isMain:"${role.isMain}",},
            </c:forEach>];
		var officeTree;
		var selectedTree;//zTree已选择对象
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			showSelectRole();
		});

		var setting = {view: {selectedMulti:false,nameIsHTML:true,showTitle:false,dblClickExpand:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var pre_selectedNodes =[
   		        <c:forEach items="${userList}" var="user">
   		        {id:"${user.id}",
   		         pId:"0",
   		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
   		        </c:forEach>];
		
		var selectedNodes =[
		        <c:forEach items="${userList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"<font color='red' style='font-weight:bold;'>${user.name}</font>"},
		        </c:forEach>];
		
		var pre_ids = "${selectIds}".split(",");
		var ids = "${selectIds}".split(",");
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
		    var officeId=treeNode.id;
			if(isSelectMain(officeId)){
		    	alert("该成员已经在该机构做主职，不能再选择兼职");
		    }else{
		    	var htmls="";
				for(var i=0;i<allRole.length;i++){
					var role=allRole[i];
					htmls+='<label style="width:200px"><input type="checkbox" name="roleIdList" '+isroleId(officeId,role.id)+' value='+role.id+' data-name="'+role.name+'"/>'+role.name+'</label>';
				}
				$("#userTree").html(htmls);
				$("#userTree").find('input[name="roleIdList"]').bind('click',function(e){
					e.stopPropagation();
					if($(this).is(':checked')){
						addRole(officeId,treeNode.name,$(this).val(),$(this).data("name"));
					}else{
						deleteRole(officeId,$(this).val());
					}
					showSelectRole();
				});
		    }
		};
		function addRole(officeId,officeName,roleId,roleName){//添加兼职
			roleList.push({
				   "roleId":roleId,
	               "roleName":roleName,
	               "officeId":officeId,
	               "officeName":officeName,
	               "isMain":"1"}
			); 
		};
		function deleteRole(officeId,roleId){//取消兼职
			for(var k=0;k<roleList.length;k++){
				var role=roleList[k];
				if(officeId==role.officeId&&roleId==role.roleId){
					roleList.splice(k,1);
				}
			}
			console.log(roleList);
		};

		function isroleId(officeId,roleId){//某机构下的某个角色是否被选中
			var flag='';
			for(var k=0;k<roleList.length;k++){
				var role=roleList[k];
				if(officeId==role.officeId&&roleId==role.roleId){
					flag='checked';
					break;
				}
			}
			return flag;
		};
		function isSelectMain(officeId){//主职是否选择了改机构
			var flag=false;
			for(var i=0;i<roleList.length;i++){
				if(officeId==roleList[i].officeId&&roleList[i].isMain==0){
					flag=true;
					break;
				}
			}
			return flag;
		};
		function  showSelectRole(){//回显选择的所有机构以及下面的角色
			var htmlArray=new Array();
			for(var i=0;i<roleList.length;i++){
				if(roleList[i].isMain==1){
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
			$("#selectedTree").html(roleHtml);
		}
		function  clearRole(){//清空兼职
			var newRoleList=new Array();
			for(var k=0;k<roleList.length;k++){
				if(roleList[k].isMain==0){
					newRoleList.push(roleList[k]);
				}
			}
			roleList=newRoleList;
		}
		 function clearAssign(){
			var submit = function (v, h, f) {
			    if (v == 'ok'){
					var tips="";
					tips = "已选兼职清除成功！";
					clearRole();
					showSelectRole();
					//ids=pre_ids.slice(0);
					//selectedNodes=pre_selectedNodes;
					//$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
			    	top.$.jBox.tip(tips, 'info');
			    } else if (v == 'cancel'){
			    	// 取消
			    	top.$.jBox.tip("取消清除操作！", 'info');
			    }
			    return true;
			};
			tips="确定清除已选兼职？";
			top.$.jBox.confirm(tips, "清除确认", submit);
		}; 
	</script>
</head>
<body>
	<div class="row" id="assignRole" style="min-height:300px">
		<div class="col-xs-4">
			<div style="border-right: 1px solid #A8A8A8;">
				<p>所在科室：</p>
				<div id="officeTree" class="ztree"></div>
			</div>
		</div>
		<div class="col-xs-8">
			<div id="userTree" class="ztree"></div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12">
			<div class="span3"
				style="border-top:1px solid #ccc;">
				<p>已选兼职：</p>
				<div id="selectedTree" class="ztree"></div>
			</div>
		</div>
	</div>
</body>
</html>

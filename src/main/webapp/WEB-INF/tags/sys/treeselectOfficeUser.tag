<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="isUser" type="java.lang.String" required="false" description="是否展示用户数据0不展示1展示"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<%@ attribute name="setRootId" type="java.lang.String" required="false" description="指定根节点，从指定节点开始展开属性菜单数据"%>
<%@ attribute name="showRole" type="java.lang.String" required="false" description="是否显示角色，仅用于用户信息编辑"%>
<%@ attribute name="treeHints" type="java.lang.String" required="false" description="自定义树结构不能选择提示语"%>
<%@ attribute name="areaId" type="java.lang.String" required="false" description="机构所属地区"%>
<%@ attribute name="townId" type="java.lang.String" required="false" description="机构所属乡镇"%>
<%@ attribute name="officeId" type="java.lang.String" required="false" description="机构所属地区"%>
<%@ attribute name="type" type="java.lang.String" required="false" description="所属业务类型"%>
<div class="input-group">
	<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}"/>
	<input id="${id}Name" name="${labelName}" ${allowInput?'':'readonly="readonly"'} type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}" class="${cssClass}" style="${cssStyle}"/>
	<a id="${id}Button" href="javascript:" class="input-group-addon ${disabled} ${hideBtn ? 'hide' : ''}" style="${smallBtn?'padding:4px 2px;':''}"><i class="fa fa-fw fa-search"></i></a>
</div>
<script type="text/javascript">
	$("#${id}Button, #${id}Name").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		var companyId="";
		if("${id}"=="office"){
			companyId=$("#companyId").val();
		}
		if(typeof("${setRootId}")!="undefined"&&"${setRootId}"!=""){
			companyId = "${setRootId}";
		}
		var atype = "";
		if(atype == ""){
			atype = $("#atype").val();
		}
		// 正常打开	
		top.$.jBox.open("iframe:${ctx}/tag/treeselectt?url="+encodeURIComponent("${url}"+"&atype="+atype+"&module=${module}&checked=${checked}&extId=${extId}&isUser=${isUser}"+"&areaId=${areaId}&officeId=${officeId}&type=${type}&townId=${townId}"), "选择${title}", 300, 420, {
			ajaxData:{selectIds: $("#${id}Id").val()},buttons:{"确定":"ok", ${allowClear?"\"清除\":\"clear\", ":""}"关闭":true}, submit:function(v, h, f){
				if (v=="ok"){
					var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
					var ids = [], names = [], nodes = [];
					if ("${checked}" == "true"){
						nodes = tree.getCheckedNodes(true);
					}else{
						nodes = tree.getSelectedNodes();
					}
					var treeHints = "${treeHints}";
					for(var i=0; i<nodes.length; i++) {//<c:if test="${checked && notAllowSelectParent}">
						if (nodes[i].isParent){
							continue; // 如果为复选框选择，则过滤掉父节点
						}//</c:if><c:if test="${notAllowSelectRoot}">
						if (nodes[i].level == 0){
							if(treeHints.length > 0){
								top.$.jBox.tip(treeHints);
							}else{
								top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
							}
							return false;
						}//</c:if><c:if test="${notAllowSelectParent}">
						if (nodes[i].isParent && ${notAllowSelectParent}){
							if(treeHints.length > 0){
								top.$.jBox.tip(treeHints);
							}else{
								top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
							}
							return false;
						} //</c:if><c:if test="${not empty module && selectScopeModule}">
						if (nodes[i].module == ""){
							if(treeHints.length > 0){
								top.$.jBox.tip(treeHints);
							}else{
								top.$.jBox.tip("不能选择公共模型（"+nodes[i].name+"）请重新选择。");
							}
							return false;
						}else if (nodes[i].module != "${module}"){
							if(treeHints.length > 0){
								top.$.jBox.tip(treeHints);
							}else{
								top.$.jBox.tip("不能选择当前栏目以外的栏目模型，请重新选择。");
							}
							return false;
						}//</c:if>
						ids.push(nodes[i].id);
						names.push(nodes[i].name);//<c:if test="${!checked}">
						break; // 如果为非复选框选择，则返回第一个选择  </c:if>
					}
					$("#${id}Id").val(ids.join(",").replace(/u_/ig,""));
					$("#${id}Name").val(names.join(","));
					if("${id}"=="company"){
						var companyId = ids.join(",").replace(/u_/ig,"");
						$.ajax({
							url : ctx + "/sys/office/getOffice",
							type : "post",
							dataType : "json",
						    data: {
					        	"companyId":companyId
			          		},
							success : function(data) {
								if(data.length>0){
									$("#officeId").val(data[0].id);
									$("#officeName").val(data[0].name);
								}else{
									$("#officeId").val("");
									$("#officeName").val("");
								}
							}
						});
						showRole(companyId);
					}
					if("${id}"=="office"){
						var officeId = ids.join(",").replace(/u_/ig,"");
						showRole(officeId);
					}
				}
				else if (v=="clear"){
					$("#${id}Id").val("");
					$("#${id}Name").val("");
                }
				if(typeof ${id}TreeselectCallBack == 'function'){
					${id}TreeselectCallBack(v, h, f);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
	function showRole(officeId){
		if(${!showRole}){
			return;
		}
		$.ajax({
			url : ctx + "/sys/role/findRoleByRoleId",
			type : "post",
			dataType : "json",
		    data: {
	        	"roleId":officeId
      		},
			success : function(data) {
			  var content="";
			  if(data.length==0){
				  content+="暂无角色"; 
			  }else{
				  for(var i=0;i<data.length;i++){
					  content+='<label class="checkbox-inline"><input type="checkbox" name="roleIdList" value="'+data[i].id+'">'+data[i].name+'</label>';
				  }
			  }
			  content+='<span class="help-inline"></span><label class="roleList checkbox-inline" style="color: red;float: right;"></label>';
			  $("#showRole").html(content);
			}
		});
	}
</script>
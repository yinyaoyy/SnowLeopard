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
<%@ attribute name="isAll" type="java.lang.Boolean" required="false" description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="divClass" type="java.lang.String" required="false" description="外层div样式"%>
<%@ attribute name="divStyle" type="java.lang.String" required="false" description="外层div样式"%>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="addonText" type="java.lang.String" required="false" description="addon显示文字"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<%@ attribute name="treeHints" type="java.lang.String" required="false" description="自定义树结构不能选择提示语"%>
<%@ attribute name="parentId" type="java.lang.String" required="false" description="级联时上级控件的id"%>
<%@ attribute name="procDefKey" type="java.lang.String" required="false" description="流程类型"%>
<div class="input-group ${divClass}" style="${divStyle}">
	<input id="${id}Id" name="${name}" class="${cssClass}" type="hidden" value="${value}"  onpropertychange="function(){${changeFuc}}"/>
	<input id="${id}Name" name="${labelName}" ${allowInput?'':'readonly="readonly"'} type="text" value="${labelValue}" data-msg-required="${dataMsgRequired}" class="${cssClass} ng-scope" style="${cssStyle}"  onpropertychange="function(){${changeFuc}}"/>
	<a id="${id}Button" href="javascript:" class="input-group-addon ${disabled} ${hideBtn ? 'hide' : ''}" style="${smallBtn?'padding:4px 2px;':''}">${addonText==''||addonText==null||addonText==undefined?'<i class="fa fa-fw fa-search"></i>':addonText}</a>
</div>
<script type="text/javascript">
$(function(){
	$("#${id}Button, #${id}Name").bind('click',function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		var parentId = "";
		if(typeof($("#${parentId}Id").val()=="undefined" && $.trim($("#${parentId}Id").val()) != "")){
			//设置了上级id，过滤掉其他内容
			parentId = $.trim($("#${parentId}Id").val());
		}
		var peopleMediationCommittee = $('#peopleMediationCommitteeId').val();
		
		if("${procDefKey}"=='fast_legal'){
			var fastCasetype=$('#caseClassify').val();
			var fastAreaId=$('#caseCountyId').val();
			var fastTownId=$('#caseTownId').val();
			var fastOfficeId=$('#officeId').val();
		}
		
		// 正常打开	
		top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("${url}"+"?parentId="+parentId+"&modules=${module}&checked=${checked}&extId=${extId}&isAll=${isAll}"+"&peopleMediationCommittee="+peopleMediationCommittee+"&fastCasetype="+fastCasetype+"&fastAreaId="+fastAreaId+"&fastTownId="+fastTownId+"&fastOfficeId="+fastOfficeId), "选择${title}", 300, 420, {
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
						/* if (nodes[i].isParent){
							top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
							return false;
						} *///</c:if><c:if test="${not empty module && selectScopeModule}">
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
					//getUserList($("#${id}Id").val());
				}//<c:if test="${allowClear}">
				else if (v=="clear"){
					$("#${id}Id").val("");
					$("#${id}Name").val("");
                }//</c:if>
				if(typeof ${id}TreeselectCallBack == 'function'){
					${id}TreeselectCallBack(v, h, f);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
})
</script>
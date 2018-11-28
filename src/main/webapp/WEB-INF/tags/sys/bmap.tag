<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="addonText" type="java.lang.String" required="false" description="addon显示文字"%>
<div class="input-group">
	<input id="${id}Id" name="${name}" ${allowInput?'':'readonly="readonly"'} type="text" value="${value}" data-msg-required="${dataMsgRequired}" class="${cssClass} ng-scope" style="${cssStyle}"/>
	<a id="${id}Button" href="javascript:" class="input-group-addon ${disabled} ${hideBtn ? 'hide' : ''}" style="${smallBtn?'padding:4px 2px;':''}">${addonText==''||addonText==null||addonText==undefined?'<i class="fa fa-fw fa-search"></i>':addonText}</a>
</div>
<script type="text/javascript">
$(function(){
	$("#${id}Button, #${id}Name").bind('click',function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		// 正常打开	
		top.$.jBox.open("iframe:${ctx}/map/bmap?coordinate=${value}", "选择${title}", 1000, 600,{
			buttons:{"确定":"ok"}, submit:function(v, h, f){
				if (v=="ok"){
					var coordinate = h.find("iframe").contents().find("#coordinate").val();//h.find("iframe").contents();
					$("#${id}Id").val(coordinate);
					$("#${id}Name").val(coordinate);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
})
</script>
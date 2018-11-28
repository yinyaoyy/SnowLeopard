<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<%-- <i id="${id}Icon" class="fa-${not empty value?value:' hide'}"></i>&nbsp;
<label id="${id}IconLabel">${not empty value?value:'无'}</label>&nbsp;
 --%>
<%-- <input id="${id}" name="${name}" type="hidden" value="${value}"/>
<a id="${id}Button" href="javascript:;" class="btn btn-primary">选择</a>&nbsp;&nbsp;
 --%>
<div class="input-group">
	<i id="${id}Icon" class="fa-${not empty value?value:' hide'}" style="position:absolute;top:9px;left:15px;z-index:20160804;"></i>
	<input id="${id}" name="${name}" type="hidden" value="${value}"/>
	<input id="${id}Name" name="" style="padding-left:35px;" class="form-control" readonly="readonly" type="text" value="${not empty value?value:'无'}"/>
	<a id="${id}Button" href="javascript:;" class="input-group-addon"><i class="fa fa-search"></i></a>
</div>

<script type="text/javascript">
	$("#${id}Button").click(function(){
		top.$.jBox.open("iframe:${ctx}/tag/iconselect?value="+$("#${id}").val(), "选择图标", 700, $(top.document).height()-180, {
            buttons:{"确定":"ok", "清除":"clear", "关闭":true}, submit:function(v, h, f){
                if (v=="ok"){
                	var icon = h.find("iframe")[0].contentWindow.$("#icon").val();
                	$("#${id}Icon").attr("class", "fa-"+icon);
	                $("#${id}Name").val(icon);
	                $("#${id}").val(icon);
                }else if (v=="clear"){
	                $("#${id}Icon").attr("class", "fa- hide");
	                $("#${id}Name").val("无");
	                $("#${id}").val("");
                }
            }, loaded:function(h){
                $(".jbox-content", top.document).css("overflow-y","hidden");
            }
        });
	});
</script>

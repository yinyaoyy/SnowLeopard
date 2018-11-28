<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="inputCssClass" type="java.lang.String" required="false" description="input css样式"%>
<%@ attribute name="aCssClass" type="java.lang.String" required="false" description="a  css样式"%>
<div class="input-group">
	<input type="file" id="file" name="file" class="form-control a-upload" onchange="changeFileName()"  readonly="readonly" style="display:none;">
	<input id="fileName" type="text" value="${value}" readonly="readonly" class="form-control ${inputCssClass}"/>	
	<a id="${id}Button" href="javascript:;" class="input-group-addon " style="width: 62px;margin-bottom:0;border-left:1px solid #ccc; padding: 6px 1px;">选择文件</a>
</div>

<script type="text/javascript">
	$("#${id}Button").click(function(){
		$('#file').click();
	});
	function changeFileName(){
		var file = document.getElementById("file").files[0];
		document.getElementById("fileName").value=file.name;
	}
</script>

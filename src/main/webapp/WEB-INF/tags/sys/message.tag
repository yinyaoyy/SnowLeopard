<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<script type="text/javascript">top.$.jBox.closeTip();</script>
<c:if test="${not empty content}">
	<c:if test="${not empty type}"><c:set var="ctype" value="${type}"/></c:if>
	<c:if test="${empty type}"><c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'error'}"/></c:if>
	<c:if test="${fn:indexOf(content,'在 语言归属为')>0}"><c:set var="ctype" value="warning"/></c:if>
	<div id="messageBox" class="alert alert-${ctype}"><button data-dismiss="alert" class="close">×</button>${content}</div> 
	<!-- <script type="text/javascript">if(!top.$.jBox.tip.mess){$("#messageBox").show();}</script> -->
</c:if>
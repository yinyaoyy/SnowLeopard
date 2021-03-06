<%
response.setStatus(500);

// 获取异常类
Throwable ex = Exceptions.getThrowable(request);
if (ex != null){
	LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
}

// 编译错误信息
StringBuilder sb = new StringBuilder("错误信息：\n");
if (ex != null) {
	sb.append(Exceptions.getStackTraceAsString(ex));
} else {
	sb.append("未知错误.\n\n");
}

// 如果是异步请求或是手机端，则直接返回信息
if (Servlets.isAjaxRequest(request)) {
	out.print(sb);
}

// 输出异常信息页面
else {
%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@page import="com.thinkgem.jeesite.common.web.Servlets"%>
<%@page import="com.thinkgem.jeesite.common.utils.Exceptions"%>
<%@page import="com.thinkgem.jeesite.common.utils.StringUtils"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>500 - 系统内部错误</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
	<div align="center"><img src="${ctxStatic}/images/platform/500_error.png"></div>
	<!-- <div class="container-fluid">
		<div class="page-header"><h1>系统内部错误.</h1></div>
		<div class="errorMessage">
			错误信息：<%//=ex==null?"未知错误.":""%> <br/> <br/>
			将错误信息发送给系统管理员，谢谢！<br/> <br/>
			<a href="javascript:" onclick="if(typeof(history.previous) != 'undefined'){history.go(-1);};" class="btn">返回上一页</a> &nbsp;
		</div>
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	</div> -->
</body>
</html>
<%
} out = pageContext.pushBody();
%>
<%
//自定义错误码，为主动抛出的异常
response.setStatus(999);

// 获取异常类
Throwable ex = Exceptions.getThrowable(request);
if (ex != null){
	LoggerFactory.getLogger("999.jsp").error(ex.getMessage(), ex);
}

// 编译错误信息
StringBuilder sb = new StringBuilder("提示信息：\n");
if (ex != null) {
	sb.append(Exceptions.getStackTraceAsString(ex));
} else {
	sb.append("\n\n");
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
	<title>999 - 提交信息提示</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1>提示信息.</h1></div>
		<div class="errorMessage">
			提示信息：<%=ex==null?"":StringUtils.toHtml(ex.getLocalizedMessage())%> <br/> <br/>
			<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
		</div>
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	</div>
</body>
</html>
<%
} out = pageContext.pushBody();
%>
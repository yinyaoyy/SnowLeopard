<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <%@ include file="/WEB-INF/views/include/taglib.jsp" %>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta charset="UTF-8">
    <title>重置密码</title>
    <style type="text/css">
        .Absolute-Center {
            width: 300px;
            height: 150px;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            margin: auto;
        }

    </style>
</head>
<body>
<div class="Absolute-Center">
    <form action="${pageContext.request.contextPath}/changepwd_forget" method="post">
        <input type="hidden" name="forgettoken" value="${forgettoken}"/>
        <p>输入密码：<input type="text" type="password" name="pwd" id="pwd" required /></p>
        <p>再次输入密码：<input type="text" type="password" id="recheckpwd" /></p>
        <p ><input type="submit" value="重置密码" id="submit"/></p>
    </form>
</div>
<script>
    $("#submit").click(function () {
        var pwd = $("#pwd").val();
        var recheckpwd = $("#recheckpwd").val();
        if (!recheckpwd || recheckpwd != pwd) {
            return false;
        }
        return true;
    });
</script>
</body>
</html>
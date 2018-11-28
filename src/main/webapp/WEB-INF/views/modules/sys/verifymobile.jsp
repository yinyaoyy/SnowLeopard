<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <%@ include file="/WEB-INF/views/include/taglib.jsp" %>
    <%@include file="/WEB-INF/views/include/head.jsp" %>
    <meta charset="UTF-8">
    <title>验证手机号</title>
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
    <form method="post" action="${pageContext.request.contextPath}/checkmobilecode_forget">
        <p>手机号：<input type="text" name="mobile" id="mobile" required /></p>
        <p>
            <button type="button" id="checkcodelabel">点击获取验证码</button>
            <input type="text" name="checkmobilecode" id="checkmobilecode" required/>
        </p>
        <p><input type="submit" value="提交验证码"/></p>
        <p><span>${msg}</span></p>
    </form>
</div>
<script>
    //点击获取短信验证码
    $("#checkcodelabel").click(function () {
        var mobile = $("#mobile").val();
        if (!mobile) {
            return;
        }
        $("#checkcodelabel").attr("disabled", "disabled");
        var countdown = 90;
        var interval = setInterval(function () {
            $("#checkcodelabel").text(countdown + 's后再次获取验证码');
            if (countdown < 0) {
                clearInterval(interval);
                $("#checkcodelabel").removeAttr("disabled");
            }
            countdown--;
        }, 1000);
        $.get("${pageContext.request.contextPath}/mobilecheckcode?mobile=" + mobile);
        // return false;
    });
</script>
</body>
</html>
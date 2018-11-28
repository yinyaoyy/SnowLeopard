<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<html style="overflow-x: hidden; overflow-y: hidden;">
   <head>
   	 <%@ include file="/WEB-INF/views/include/taglib.jsp"%>
     <%@include file="/WEB-INF/views/include/head.jsp"%>
     <title>${fns:getConfig('productName')}登录</title>
     <style type="text/css">
            html,
            body,
            table {
              background-color: #f5f5f5;
              width: 100%;
            }

            html,
            body {
              height: 100%;
              overflow: hidden;
              padding: 0;
              margin: 0
            }

            .form-signin-heading {
              position: relative;
              color: #fff;
              font-family: "Helvetica Neue",Helvetica,Arial,"Microsoft Yahei","Hiragino Sans GB","Heiti SC","WenQuanYi Micro Hei",sans-serif;
              font-size: 36px;
              margin-bottom: 20px;
            }

            .form-signin {
              position: relative;
              text-align: left;
              width: 300px;
              padding: 25px 29px 40px;
              margin: 0 auto 20px;
              background-color: rgba(255, 255, 255, .2);
              border: none;
              -webkit-border-radius: 5px;
              -moz-border-radius: 5px;
              border-radius: 5px;
              -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
              -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
              box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            }

            .form-signin .checkbox {
              margin-bottom: 10px;
              color: #0663a2;
            }

            .form-signin .input-label {
              vertical-align: -webkit-baseline-middle;
              font-size: 16px;
              line-height: 23px;
              margin-bottom: 10px;
              color: #999;
              color: #fff;
            }

            .form-signin .input-block-level {
              background: transparent;
              color: #fff;
              font-size: 16px;
              height: auto;
              margin-bottom: 15px;
              padding: 7px;
              *width: 283px;
              *padding-bottom: 0;
              _padding: 7px 7px 9px 7px;
            }

            .form-signin .btn.btn-large {
              font-size: 16px;
            }

            .form-signin #themeSwitch {
              position: absolute;
              right: 30px;
              bottom: 10px;
              color: #fff;
            }

            #loginForm div.validateCode {
              display:none;
            }

            .mid {
              color: #fff;
              vertical-align:top;
            }

            .header {
              height: 70px;
              padding-top: 20px;
              margin-bottom: 20px;
            }

            .alert {
              position: relative;
              width: 300px;
              margin: 0 auto;
              *padding-bottom: 0px;
              border-radius: 5px;
            }

            label.error {
              background: none;
              font-weight: normal;
              color: inherit;
              margin: 0;
              cursor: default;
            }

            .alert .close {
              position: absolute;
              top: 5px;
              right: 10px;
            }

            #backgroundcanvas {
              position: fixed;
              background-color: #164979;
              width: 100%;
              height: 100%;
              margin: 0;
              padding: 0;
              left: 0;
              top: 0;
            }

            .txt {
              width: 70px;
              height: auto;
            }
            .select2-chosen{
              color: #000;
            }
            
            #validateCodes-error{
    			right: 29%;
            }
          </style>
     <link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css" />
     <script src="${ctxStatic}/snowLeopard/js/md5.js" type="text/javascript"></script>
     <script type="text/javascript" src="${ctxStatic}/snowLeopard/js/common.js"></script>
     <script type="text/javascript" src="${ctxStatic}/backgroundCanvas/backgroundcanvas.js"></script>
     <script type="text/javascript">
     top.location.href= "${fns:getConfig('server_url')}";
     </script>
   </head>
   <body style="overflow: hidden;background-size:100% 100%;" class="page-body">
  
     <input type="hidden" id="backgroundType" value="${fns:getConfig('backgroundType')}"/>
     <canvas id="backgroundcanvas"></canvas>
          <div class="login-page login-light">
            <div class="login-container">
              <div class="row">
                <div class="col-sm-5 login-form fade-in-effect">
                  <div class="login-header">
                    <a href="#" class="logo"> <img src="${fns:getConfig('server_url')}${fns:getConfig('logo_url')}?${random}" alt="" />
						</a>
                    <p class="copyName"></p>
                  </div>
                  <div class="flippedBox">
                    <div class="front">
                      <form id="loginForm">
                        <div class="form-group">
                          <label class="control-label" for="username" id="usernamelabel"></label> <input type="text" id="username" name="username"
                            autocomplete="off" class="form-control " value="${username}">
                        </div>
                        <div class="form-group">
                          <label class="control-label" for="password" id="passwordlabel"></label> <input type="password" id="password" name="password"
                            autocomplete="off" class="form-control ">
                        </div>
                        <div class="form-group validateCode">
                            <label class="control-label" for="validateCodes" id="validateCodelabel" style="color:#fff;font-weight:normal;width:69%;">验证码</label>
                            <sys:validateCode name="validateCodes" inputCssStyle="width:69%" />
                        </div>
                        <div class="form-group">
                          <button class="btn btn-primary-login btn-block text-center" id="loginBtn">
								<i class="fa fa-fw fa-lock"></i> 登录
							</button>
                        </div>
                      </form>
                      <%--   <div class="form-group">
                            <a href="${pageContext.request.contextPath}/forgetpwd">忘记密码</a>
                        </div> --%>
                    </div>
                    <div class="back">
							<form id="register">
								<div class="form-group">
									<label for="realname" class="control-label" id="realnamelabel"></label> <input
										type="text" data-validate="realname" class="form-control" placeholder="请输入真实姓名"
										name="realname" id="realname">
								</div>
								<div class="form-group">
									<label for="papernum" class="control-label" id="papernumlabel"></label> <input
										type="text" data-validate="papernum" class="form-control" placeholder="请输入身份证号"
										name="papernum" id="papernum" required>
								</div>
								<div class="form-group">
									<label for="newUserName" class="control-label" id="newUserNamelabel"></label> <input
										type="text" data-validate="newUserName" class="form-control" placeholder="请输入手机号"
										name="newUserName" id="newUserName" required>
								</div>
								<div class="form-group">
									<label for="newPasswd" class="control-label" id="newPasswdlabel"></label> <input
										type="password"class="form-control" name="newPasswd"
										id="newPasswd">
								</div>
								<div class="form-group">
									<label for="recheckPasswd" class="control-label" id="recheckPasswdlabel"></label> <input
										type="password" class="form-control" name="recheckPasswd"
										id="recheckPasswd">
								</div>
                                <div class="form-group">
                                    <button id="checkcodelabel" type="button">点击获取验证码</button> <input
                                        type="text"  name="checkcode" required
                                        id="checkcode">
                                </div>
                                <div class="form-group">
									<button class="btn btn-primary btn-block text-center" id="btnImportSubmit">
										<i class="fa fa-fw fa-lock"></i>
									</button> 
								</div>
							</form>
						</div>
					</div>
					<div class="login-footer">
						<c:if test="${fns:getConfig('is_register')}"> 
							<a class="changeLR" href="javascript:;">新用户注册</a>
						</c:if>
						<div style="float:right">
						<!-- 根据参数启用国际化语言 -->
						<c:if test="${fns:getConfig('enable_international_language')}">
							<label class="control-label" style="color:#fff;font-weight:normal;vertical-align:top;line-height: 1.4;" id="defaultLanguage">默认语言：</label>
							<span id="langCode"  name="langCode" style="display:inline-block;margin-left:5px;cursor:pointer;color:#fff;position:relative;">
								<span class="curLang" >中文</span>
								<input type="hidden" id="curLangValue" value="${fns:getConfig('langeageType')}">
								<ul id="langList" style="display:none;position: absolute;width: 60px;margin: 0;padding-left: 0;border: 1px solid #fff;border-radius: 2px;text-align: center;top: 23px;left: -17px;">
								    <c:forEach items="${fns:getDictListByLanguage('act_langtype','CN')}" var="index">
									<c:choose> 
							           <c:when test="${index.value==fns:getConfig('langeageType')}">
							              <li data-value="${index.value}" selected="selected"  style="list-style: none;line-height: 25px;">${index.label }</li>
							           </c:when>
							           <c:otherwise>
							               <li data-value="${index.value}"  style="list-style: none;line-height: 25px;">${index.label}</li>
							           </c:otherwise>
							         </c:choose>
									</c:forEach>
								</ul>
							</span>
						</c:if>
						</div> 
					</div>
                 </div>
              </div>
            </div>
         </div>
        <script src="${ctxStatic}/flash/zoom.min.js" type="text/javascript"></script>
        <script src="${ctxStatic}/login/login.js"></script>
   <script>
       //点击获取短信验证码
       $("#checkcodelabel").click(function () {
           var mobile = $("#newUserName").val();
           if(!mobile){
               return;
           }
           $("#checkcodelabel").attr("disabled","disabled");
           var countdown = 90;
           var interval = setInterval(function(){
               $("#checkcodelabel").text(countdown + 's后再次获取验证码');
               if(countdown < 0){
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">

	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>锁屏</title>
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css">
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css">
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css">
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-forms.css">
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css">
		<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script type="text/javascript">
			var ctx = '${ctx}',
				ctxStatic = '${ctxStatic}';
			var jsCtx = "${ctx}";
		</script>
		<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/common.js"></script>
		<script>
			//防止页面后退
			history.pushState(null, null, document.URL);
			window.addEventListener('popstate', function() {
				history.pushState(null, null, document.URL);
			});
			$(document).ready(function() {
				// var currentLanguageArr;
				//当前页的国际化数据	
				$.ajax({
					type: "post",
					url: jsCtx + "/language/sysMunlLang/getCurrentPageList",
					async: false,
					data: {
						"languageAscription": "/lockscreen"
					},
					success: function(data) {
						currentLanguageArr = data;
						setValue();
					}
				});

				function setValue() {
					$(".lockscreen_hello").text(currentLanguageArr.lockscreen_hello + $(".lockscreen_hello").text());
					$(".lockscreen_tip").text(currentLanguageArr.lockscreen_tip + "！");
					$(".lockscreen_unlock").text(currentLanguageArr.lockscreen_unlock);
					$("#passwd").attr('placeholder', currentLanguageArr.lockscreen_password);
				};
			});
		</script>
	</head>

	<body class="page-body">
		<canvas id="backgroundcanvas"></canvas>
		<div class="lockscreen-page">
			<form role="form" id="lockscreen" class="lockscreen-form fade-in-effect in" novalidate="novalidate">
				<div class="user-thumb">
					<a href="#">
						<img src="${fns:getConfig('server_url')}${fns:getUser().photo}" class="img-responsive img-circle">
					</a>
				</div>
				<div class="form-group">
					<h3 class="lockscreen_hello">${fns:getUser().name}！</h3>
					<p class="lockscreen_tip"></p>
					<div class="input-group unlockBtn">
						<input type="password" class="form-control input-dark" name="passwd" id="passwd" placeholder="">
						<span class="input-group-btn">
								<button class="btn lockscreen_unlock"></button>
							</span>
					</div>
				</div>
			</form>
			<!--</div>
			</div>-->
		</div>
		<!-- Bottom Scripts -->
		<script src="${ctxStatic}/snowLeopard/js/bootstrap.min.js"></script>
		<script src="${ctxStatic}/snowLeopard/js/TweenMax.min.js"></script>
		<script src="${ctxStatic}/snowLeopard/js/resizeable.js"></script>
		<script src="${ctxStatic}/snowLeopard/js/joinable.js"></script>
		<script type="text/javascript" src="${ctxStatic}/jquery-validate/jquery.validate.js"></script>
		<script src="${ctxStatic}/toastr/toastr.min.js"></script>
		<!-- JavaScripts initializations and stuff -->
		<script src="${ctxStatic}/snowLeopard/js/SnowLeopard-custom.js"></script>
		<script src="${ctxStatic}/backgroundCanvas/backgroundcanvas.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
			$(function() {
				//背景图
				var cookie = $.getCookie('backgroundType');
				if(cookie != null && cookie != undefined &&
					cookie != '') {
					var cookieList = cookie.split("|");
					$.loadBackground({
						iscavans: cookieList[0],
						israndom: cookieList[1],
						styleOrPath: cookieList[2],
						canvasText: decodeURI(cookieList[3])
					})
				} else {
					$.loadBackground({
						iscavans: 'canvas',
						israndom: true,
						styleOrPath: 'canvasStar'
					})
				}
				$(".lockscreen_unlock").on('click', function(e) {
					e.preventDefault();
					e.stopPropagation();
					var passwd = $("#passwd").val();
					$.ajax({ //默认加载内容
						type: "post",
						url: jsCtx + "/sys/user/unlock",
						data: {
							"password": passwd,
						},
						success: function(data) {
							var pa = JSON.parse(data);
							if(pa.code == "0") {
								window.location.href = jsCtx + "/newIndex";
							} else if(pa.code == "-1") {
								indow.location.href = jsCtx + "/login";
							} else {
								var btnW = $('.lockscreen_unlock').width();
								var errorR = btnW + 10;
								if($('#passwd-error').length > 0) {
									return
								} else {
									var errStr = '<label id="passwd-error" class="error" for="passwd" style="right:' + errorR + 'px">' + currentLanguageArr.lockscreen_passwordError + '</label>'
									$('.unlockBtn').append(errStr);
									$('#passwd').addClass('error').siblings('.input-group-btn').find('button').css({
										'border-color': 'rgba(204, 63, 68, 0.5)',
										'background-color': 'rgba(255,40,40,.3)',
										'color': '#fff'
									})
								}

							}
						}
					});
				});
			})
		</script>
	</body>

</html>
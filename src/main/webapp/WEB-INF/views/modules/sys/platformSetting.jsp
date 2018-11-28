<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="decorator" content="default" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>平台管理</title>
<link rel="stylesheet"
	href="${ctxStatic}/snowLeopard/Css/SnowLeopard-forms.css">
<link rel="stylesheet"
	href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css">
	<script type="text/javascript">
	</script>
</head>

<body>
	<input type="hidden" id="serverUrl"
		value="${fns:getConfig('server_url')}" />
	<div class="profile-env platformOption">
		<div class="panel" id="platform-settings">
			<div class="row form-group">
				<div class="col-sm-2 middle-align">
					<label class="control-label">选择平台LOGO</label>
				</div>
				<div class="col-sm-10">
					<div class="row">
						<div class="col-sm-12">
							<label for="userpic" style="cursor: pointer;" class="platlogo">
								<img
								src="${fns:getConfig('server_url')}${fns:getConfig('logo_url')}?${params}"
								alt="" width="160px" />
							</label> <input type="file" name="" id="userpic" value=""
								style="display: none;" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<p class="help-inline addpage-prompt note" >提示：请使用图片大小请小于10M，宽高比为16:9的图片，以达到更好的显示效果！</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-sm-2 middle-align">
					<label class="control-label">版权设置</label>
				</div>
				<div class="col-sm-10">
					<div class="row">
						<div class="col-sm-6  input-group">
							<input type="text" class="form-control no-right-border"
								id="copyright" value="${fns:getConfig('companyVersion')}">
							<span class="input-group-btn">
								<button id="copyrightBtn" class="btn btn-primary" type="button" style="height:32px;">更改</button>
							</span>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<p class="help-inline addpage-prompt note" >如：&copy; 2010-2017（起始终止年）版权所有</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-sm-2 middle-align">
					<label class="control-label">平台语言</label>
				</div>
				<div class="col-sm-1">
					<select class="form-control" id="language">
						<c:forEach items="${languageList}" var="dict">
							<c:choose>
								<c:when test="${dict.value==fns:getConfig('langeageType')}">
									<option value="${dict.value }" selected="selected">${dict.label }</option>
								</c:when>
								<c:otherwise>
									<option value="${dict.value }">${dict.label }</option>
								</c:otherwise>
							</c:choose>

						</c:forEach>
					</select>
				</div>
			</div>
			<div class="row form-group backgroundOpt">
				<div class="col-sm-2 middle-align">
					<label class="control-label">开放用户注册</label>
				</div>
				<div class="col-sm-10">
					<input type="checkbox" class="ylswitch"
						<c:if test="${fns:getConfig('is_register')}"> 
					       checked 
					       </c:if>
						id='ifregister' name="ifregister" /> <label for="ifregister"
						class="ylswitch-label"></label>
				</div>
			</div>
			<div class="row form-group backgroundOpt">
				<div class="col-sm-2 middle-align">
					<label class="control-label">启用国际化语言</label>
				</div>
				<div class="col-sm-10">
					<input type="checkbox" class="ylswitch"
						<c:if test="${fns:getConfig('enable_international_language')}"> 
					       checked 
					       </c:if>
						id='enableLanguage' name="enableLanguage" /> <label for="enableLanguage"
						class="ylswitch-label"></label>
				</div>
			</div>
			<div class="row form-group backgroundOpt">
				<div class="col-sm-2 middle-align">
					<label class="control-label">开启静态背景</label>
				</div>
				<div class="col-sm-10">
					<input type="checkbox" class="ylswitch" checked id='ifstatics'
						name="ifstatics" /> <label for="ifstatics" class="ylswitch-label"></label>
				</div>
			</div>

			<div class="row form-group backgroundOpt">
				<div class="col-sm-2 middle-align">
					<label class="control-label">随机动态背景</label>
				</div>
				<div class="col-sm-10">
					<input type="checkbox" class="ylswitch" checked id='ifrandom'
						name="ifrandom" /> <label for="ifrandom" class="ylswitch-label"></label>
				</div>
			</div>
			<div class="row form-group backgroundOpt">
				<div class="col-sm-2 middle-align">
					<div class="row">
						<label class="control-label">平台动态背景</label>
					</div>
				</div>
				<div class="col-sm-10 ">
					<div class="row canvasList">
						<div class="col-sm-4 canvasPic canvasStar"
							data-styleType="statics" data-styleOrPath="canvasStar">
							<div class="userCanvasControl">
								<div class="form-group">
									<i class="fa fa-fw fa-check green chooseCanvas"></i>
								</div>
							</div>
							<p class="canvasName">点点繁星</p>
						</div>
						<div class="col-sm-4 canvasPic canvasDotted"
							data-styleType="statics" data-styleOrPath="canvasDotted">
							<div class="userCanvasControl">
								<div class="form-group">
									<i class="fa fa-fw fa-check green chooseCanvas"></i>
								</div>
							</div>
							<p class="canvasName">互动连线</p>
						</div>
						<div class="col-sm-4 canvasPic canvasRedLine"
							data-styleType="statics" data-styleOrPath="canvasRedLine">
							<div class="userCanvasControl">
								<div class="form-group">
									<i class="fa fa-fw fa-check green chooseCanvas"></i>
								</div>
							</div>
							<p class="canvasName">绚丽弧线</p>
						</div>
					</div>
					<div class="row canvasList">
						<div class="canvasPic canvasSquare" data-styleType="statics"
							data-styleOrPath="canvasSquare">
							<div class="userCanvasControl">
								<div class="form-group">
									<i class="fa fa-fw fa-check green chooseCanvas"></i>
								</div>
							</div>
							<p class="canvasName">多彩方形</p>
						</div>
						<div class="canvasPic canvasHexagon" data-styleType="statics"
							data-styleOrPath="canvasHexagon">
							<div class="userCanvasControl">
								<div class="form-group">
									<i class="fa fa-fw fa-check green chooseCanvas"></i>
								</div>
							</div>
							<p class="canvasName">炫彩六边</p>
						</div>
						<div class="canvasPic canvasFont" data-styleType="statics"
							data-styleOrPath="canvasFont">
							<div class="userCanvasControl">
								<div class="form-group">
									<i class="fa fa-fw fa-pencil info editText"></i> 
									<label for="editarea"class="editlabel"> 
										<input type="text" id="editarea" />
										<i class="fa fa-fw fa-check" id="submitText"></i>
									</label>
								</div>
							</div>
							<p class="canvasName">自定义文字</p>
						</div>
					</div>

				</div>
			</div>
			<div class="row form-group backgroundOpt">
				<div class="col-sm-2">
					<label class="control-label">自定义背景</label>
				</div>
				<div class="col-sm-8 imageList">
					<c:forEach items="${imgList }" var="imgs">
						<div class="userBackImage" data-styleType="statics"
							data-styleOrPath="${fns:getConfig('server_url')}${imgs}">
							<img src="${fns:getConfig('server_url')}${imgs}" />
							<div class="userBackImageControl">
								<div class="form-group">
									<i class="fa fa-fw fa-trash red delImage"></i> 
									<i class="fa fa-fw fa-check green chooseImage"></i>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-sm-2 middle-align">
					<label class="control-label">群体性事件阈值</label>
				</div>
				<div class="col-sm-2">
					<div class="row">
						<div class="col-sm-6  input-group">
							<input type="text" class="form-control no-right-border"
								id="threshold" value="${fns:getConfig('threshold')}">
							<span class="input-group-btn">
								<button id="thresholdBtn" type="button" class="btn btn-primary" style="height:32px;">更改</button>
							</span>
						</div>
					</div>
				</div>
				<div class="col-sm-5 middle-center" >
					<p class="help-inline addpage-prompt note">大于或等于该数值时，为群体性事件</p>
				</div>
			</div>
			<div class="row form-group">
				<div class="col-sm-2 middle-align">
					<label class="control-label">敏感字库初始化</label>
				</div>
				<div class="col-sm-2">
					<div class="row">
						<div class="col-sm-6  input-group">
						<a href="${ctx}/platform/Initialization" class="btn btn-primary" style="width:95px;">初始化字库</a>
						</div>
					</div>
				</div>
				<div class="col-sm-5 middle-center">
					<p class="help-inline addpage-prompt note">初始化时机：当字库配置文件被修改时，点击初始化字库，页面刷新即初始化成功</p>
				</div>
			</div>
		</div>
	</div>
	<script src="${ctxStatic}/userImg/js/platformopts.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/backgroundCanvas/backgroundcanvas.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/snowLeopard/js/TweenMax.min.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/resizeable.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/joinable.js"></script>
	<script src="${ctxStatic}/userImg/js/xenon-toggles.js"></script>
	<script src="${ctxStatic}/toastr/toastr.min.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/SnowLeopard-custom.js"></script>
</body>
</html>
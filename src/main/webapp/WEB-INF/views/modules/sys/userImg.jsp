<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="decorator" content="default"/>
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>用户信息</title>
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="${ctxStatic}/userImg/css/cropper.min.css" />
		<link href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-forms.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-skins.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/flavr/css/flavr.css" type="text/css" rel="stylesheet"/>
		<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	</head>
	<body class="scrollauto" style="overflow-x:hidden;">
		<ul class="nav nav-tabs">
			<li><a href="${ctx}/sys/user/info">个人信息</a></li>
			<li><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
			<li class="active"><a href="${ctx}/sys/user/upload">修改头像</a></li>
		</ul><br/>
		<img alt="" src="${userImgHidden }" id="userImgHidden" style="display:none;" >
		<section class="profile-env">
			<div class="row">						
				<div class="col-sm-12">									
					<div class="tab-content">									
						<div class="panel tab-pane active" id="changeUserImg">
							<div class="row">
								<div class="col-sm-3">
									<div class="docs-preview">
										<div class="img-preview preview-lg">
										</div>
										<div class="img-preview preview-md">
										</div>
										<div class="img-preview preview-sm">
										</div>
									</div>
								</div>
								<div class="col-sm-9" id="image-cropper">
									<div class="btn-group">
										<button class="btn btn-info" data-method="zoom" data-option="0.1" type="button">
            								<span class="docs-tooltip" data-toggle="tooltip" title="放大" data-original-title="放大">
              									<span class="fa fa-fw fa-search-plus"></span>
            								</span>
          								</button>
										<button class="btn btn-info" data-method="zoom" data-option="-0.1" type="button">
            								<span class="docs-tooltip" data-toggle="tooltip" title="缩小" data-original-title="缩小">
              									<span class="fa fa-fw fa-search-minus"></span>
            								</span>
          								</button>
										<button class="btn btn-info" data-method="rotate" data-option="-45" type="button">
            								<span class="docs-tooltip" data-toggle="tooltip" title="向左旋转" data-original-title="向左旋转">
              									<span class="fa fa-fw fa-undo"></span>
            								</span>
          								</button>
										<button class="btn btn-info" data-method="rotate" data-option="45" type="button">
            								<span class="docs-tooltip" data-toggle="tooltip" title="向右旋转" data-original-title="向右旋转">
              									<span class="fa fa-fw fa-repeat"></span>
            								</span>
          								</button>
										<label class="btn btn-info" for="inputImage">
            								<input class="sr-only" id="inputImage" name="file" type="file" accept="image/jpg,image/jpeg,image/png">
            								<span class="docs-tooltip" data-toggle="tooltip" title="重新上传图像" data-original-title="重新上传图像">
             									<span class="fa fa-fw fa-upload"></span>
            								</span>
          								</label>
										<button class="btn btn-info" data-method="getCroppedCanvas" type="button">
           									<span class="docs-tooltip" data-toggle="tooltip" title="保存图片" data-original-title="保存图片">
             									<span class="fa fa-fw fa-floppy-o"></span>
            								</span>
          								</button>
									</div>
									<div id="cropper-content">
										<label class="btn btn-white" for="inputImage">
            								<input class="sr-only" id="inputImage" name="file" type="file" accept="image/jpg,image/jpeg,image/png">
            								<span class="docs-tooltip" data-toggle="tooltip" title="上传图片" data-original-title="上传图片">
            									<i class="fa fa-fw fa-cloud-upload"></i>
            								</span>
          								</label>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
		<!-- Bottom Scripts -->
	<script src="${ctxStatic}/userImg/js/cropper.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/snowLeopard/js/bootstrap.min.js"></script>						
	<script type="text/javascript" src="${ctxStatic}/jquery-validate/jquery.validate.js"></script>
	<script src="${ctxStatic}/flavr/js/flavr.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxStatic}/userImg/js/userInfo.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
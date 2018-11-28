<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" import="java.io.File,com.thinkgem.jeesite.common.config.Global,com.thinkgem.jeesite.modules.sys.utils.UserUtils" pageEncoding="UTF-8"%>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
	<title>${fns:getConfig('productName')}</title>
	<%@include file="/WEB-INF/views/include/head.jsp"%>
	<script type="text/javascript">
	 $(document).ready(function(){
		 window.setInterval(function () {
             $.get("${ctx}/sys/sysUserPush/no_read", 
                 function (data) {
            	 $("#notifyNum").html(data);
            	 $("#ss").html(data);
             });
         }, 10000);
		 //点击小铃铛事件
		 $("#tongzhi").bind("click",function(){
			 $(this).toggleClass('open')
			 $('#hhhhh').find('li').remove();
			 $.ajax({
				    url:"${ctx}/sys/sysUserPush/mesList",    //请求的url地址
				    data:{"pageSize":5,"no_read":"0"},    //参数值
				    dataType:"json",
				    type:"GET",
				    success:function(data){
				    	$("#as").show();
				    	var str ="";
				    	var list = data.list;
				    	if(list!=''&&list!=undefined&&list!=null){

					    	$.each(list,function(i,val){
					    		$li =
									$('<li class="notification-warning" id="notify-' + i +
										'"><a data-href="${ctx}' + list[i].url +
										'"title="我的任务" id="' + list[i].id +
										'"><i class="fa-envelope-o"></i><span  style="display:none" class="title">'+"我的任务"+'</span><p>'+list[i].pushMessage.msgContent+
										'</p></a><li>');
					    		$li.find('a').bind('click',function(){
					    			$.ajax({
									    url:"${ctx}/sys/sysUserPush/changeStatus",    //请求的url地址
									    type:"GET",
									    data:{"id":$(this).attr('id')},
									    success:function(data){
									    	if(data=="success"){
									    		 //$("#notifyNum").html("0");
									    		 $("#ss").html("0");
									    	}
									    }
								 	});
					    			$(this).parent().parent().parent().parent().parent().removeClass('open')
					    		})
					    		$('#hhhhh').append($li)
					    	})
				    	}else{
				    		$("#notifyNum").html('0');
				    	}
				    	platForm.bindMainMenuClick('#hhhhh a');
				    }
			 });
			
		 });
		
		 
		  $("#qbyd").bind("click",function(){
			 $.ajax({
				    url:"${ctx}/sys/sysUserPush/qbyd",    //请求的url地址
				    type:"GET",
				    success:function(data){
				    	if(data=="success"){
				    		 $("#notifyNum").html("0");
				    		 $("#ss").html("0");
				    	}
				    }
			 });
		 }); 
	 });
	 /* function change(se){
			alert(1);
			 $.ajax({
				    url:"${ctx}/sys/sysUserPush/changeStatus",    //请求的url地址
				    type:"GET",
				    data:{"id":id},
				    success:function(data){
				    	if(data=="success"){
				    		 $("#notifyNum").html("0");
				    		 $("#ss").html("0");
				    	}
				    }
			 });
		 }  */
	</script>
</head>
<body class="page-body skin-own-gray">
    <input value="${isPerfect}" id="isPerfect" type="hidden">
    <!-- 顶部弹出菜单 -->
		<div class="settings-pane">
			<!-- 关闭按钮 -->
			<a href="#" data-toggle="settings-pane" data-animate="true">&times;</a>
			<!-- 弹出菜单 -->
			<div class="settings-pane-inner">
				<div class="row">
					<div class="platRowTitle"></div>
				</div>
				<div class="row">
					<div class="col-md-4">
						<div class="user-info">
							<div class="user-image">
								<a href="extra-profile.html">
									<img src="${fns:getConfig('server_url')}${fns:getUser().photo}" class="img-responsive img-circle" id="popupsSculpture" />
								</a>
							</div>
							<div class="user-details">
								<h3>
                                <a href="extra-profile.html"><span id="popupsRealName">${fns:getUser().name}</span></a>
                                <!--可用状态:在线,空闲时,正忙着和离线is-online, is-idle, is-busy and is-offline-->
                                <span class="user-status is-online"></span>
                            </h3>
								<p class="user-title"><span id="popupsPostion"></span></p>
								<div class="user-links">
									<a href="extra-profile.html" class="btn btn-primary">按钮1</a>
									<a href="extra-profile.html" class="btn btn-success">按钮2</a>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-8 link-blocks-env">
						<div class="links-block left-sep">
							<h4><span>设置默认角色</span></h4>
							<ul id="radioRoleList" class="list-unstyled input-list">
								<li class="checkbox">
									<label for="sp-rdo1"><input type="checkbox" value="" title="" id="sp-rdo1" name="role" />角色1</label>
								</li>
							</ul>
						</div>
						<div class="links-block left-sep">
							<h4><span>通知</span></h4>
							<ul class="list-unstyled input-list">
								<li class="checkbox">
									<label for="sp-chk1"><input type="checkbox" checked="checked" id="sp-chk1"/>信息</label>
								</li>
								<li class="checkbox">
									<label for="sp-chk2"><input type="checkbox" checked="checked" id="sp-chk2"/>事件</label>
								</li>
								<li class="checkbox">
									<label for="sp-chk3"><input type="checkbox" checked="checked" id="sp-chk3"/>升级</label>
								</li>
								<li class="checkbox">
									<label for="sp-chk4"><input type="checkbox" checked="checked" id="sp-chk4"/>登陆时间</label>
								</li>
							</ul>
						</div>
						<div class="links-block left-sep">
							<h4><span>帮助</span></h4>
							<ul class="list-unstyled">
								<li>
									<a href="#">
										<i class="fa-angle-right"></i>支持中心
									</a>
								</li>
								<li>
									<a href="#">
										<i class="fa-angle-right"></i>提交意见
									</a>
								</li>
								<li>
									<a href="#">
										<i class="fa-angle-right"></i>协议
									</a>
								</li>
								<li>
									<a href="#">
										<i class="fa-angle-right"></i>服务条款
									</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--左侧菜单-->
		<div class="page-container">
			<!-- 添加sidebar-collapsed使侧边栏从默认变成折叠，chat-visible使聊天窗口显示 -->
			<!-- 添加fixed类使侧边栏固定显示在窗口 -->
			<!-- 添加toggle-others使在同一时间只有一个菜单项显示 -->
			<!-- 添加collapsed折叠侧边栏根元素只显示图标 -->
			<div class="sidebar-menu toggle-others fixed ">
				<div class="sidebar-menu-inner">
					<header class="logo-env">
						<!-- logo -->
						<div class="logo">
							<a href="javascript:;" data-href="platform-options.html" class="logo-expanded">
								<img src="${fns:getConfig('server_url')}${fns:getConfig('logo_url')}?${params}" alt="" id="tupianre"/>
							</a>
							<a href="javascript:;" data-href="platform-options.html" class="logo-collapsed">
								<img src="${fns:getConfig('server_url')}${fns:getConfig('logo_url')}?${params}" alt="" id="tupianreMin" />
							</a>
						</div>
						<!--用户信息-->
						<div class="copyName-details">
							<h3></h3>
						</div>

						<!-- 移动设备折叠菜单按钮 -->
						<div class="mobile-menu-toggle visible-xs">
							<a href="#" data-toggle="user-info-menu">
								<i class="fa-bell-o"></i>
								<span class="badge badge-success">7</span>
							</a>
							<a href="#" data-toggle="mobile-menu">
								<i class="fa-bars"></i>
							</a>
						</div>
					</header>
					<ul id="main-menu" class="main-menu">
					</ul>
				</div>
			</div>
			<!-- 主页面 -->
			<!--<div class="main-content">-->
			<!-- 用户信息, 通知，菜单 -->
			<nav class="navbar user-info-navbar fixed" role="navigation">
				<!--左侧菜单为用户接收信息提醒-->
				<ul class="user-info-menu left-links list-inline list-unstyled">
					<!-- 折叠菜单 -->
					<li class="hidden-sm hidden-xs">
						<a href="#" data-toggle="sidebar" title="${language.index_menuSwitch }">
							<i class="fa-bars"></i>
						</a>
					</li>

					<!-- 通知列表 -->
					<li class="dropdown hover-line" style="width:50px;">
						<%-- <a href="#" data-toggle="dropdown" title="${language.index_notify }"> --%>
						<a  title="${language.index_notify }" id="tongzhi" data-toggle="dropdown" class="dropdown-toggle">
							<i class="fa-bell-o"></i>
							<span class="badge badge-purple" id="notifyNum"></span>
						</a>
						<div id="message" style="height:200px;width:200px"></div>
						
						
						<ul class="dropdown-menu notifications" id="as">
							<li class="top" id="firstLi">
								<p class="small">
									<a id="qbyd" class="pull-right">全部标记已读</a> 你有 <strong id="ss"></strong> 条新通知.
								</p>
							</li>
							<li>
								<ul class="dropdown-menu-list list-unstyled ps-scrollbar" id="hhhhh">
									<!-- <li class="active notification-success">
										<a href="#">
											<i class="fa-user"></i>
											<span class="line">
                                            <strong>New user registered</strong>
                                        </span>
											<span class="line small time">30 seconds ago</span>
										</a>
									</li>
									<li class="active notification-secondary">
										<a href="#">
											<i class="fa-lock"></i>
											<span class="line"><strong>Privacy settings have been changed</strong></span>
											<span class="line small time">3 hours ago</span>
										</a>
									</li>
									<li class="notification-primary">
										<a href="#">
											<i class="fa-thumbs-up"></i>
											<span class="line"><strong>Someone special liked this</strong></span>
											<span class="line small time">2 minutes ago</span>
										</a>
									</li>
									<li class="notification-danger">
										<a href="#">
											<i class="fa-calendar"></i>
											<span class="line">John cancelled the event</span>
											<span class="line small time">9 hours ago</span>
										</a>
									</li>
									<li class="notification-info">
										<a href="#">
											<i class="fa-database"></i>
											<span class="line">The server is status is stable</span>
											<span class="line small time">yesterday at 10:30am</span>
										</a>
									</li>
									 -->
								</ul>
							</li>  
							<li class="external">
								<a  href="javascript:" data-href="${ctx}/sys/sysUserPush/mlist" id="allNotice" title="我的消息">
									<span class="title">查看所有消息</span>
									<i class="fa-link-ext"></i>
								</a>
							</li>
						</ul>
						
					</li>
				</ul>
				<!--右侧菜单：查询，用户登录信息，聊天-->
				<ul class="user-info-menu right-links list-inline list-unstyled">
					
					<!-- 用户登录信息 -->
					<li class="dropdown user-profile">
						<a href="#" data-toggle="dropdown" class="dropdown-toggle">
							<img src="<%
							  String file_path = Global.getUserfilesBaseDir()+UserUtils.getUser().getPhoto(); 
							  if(file_path.indexOf("?")!=-1){
								  file_path = file_path.substring(0,file_path.indexOf("?"));
							  }
							  File file = new File(file_path);
							  if(!"".equals(UserUtils.getUser().getPhoto())&&UserUtils.getUser().getPhoto()!=null){
							  if(file.exists()){%>
								  ${fns:getConfig('server_url')}${fns:getUser().photo}
							  <%}else{%>
							  	  ${fns:getConfig('server_url')}/userfiles/default/userProfile_default.png
							  <%}}else{%>${fns:getConfig('server_url')}/userfiles/default/userProfile_default.png<%}%>" alt="user-image" class="img-circle img-inline userpic-32" width="28" id="topSculpture" />
							<span id="menuRealName">${fns:getUser().name}</span>
						</a>
						<ul class="dropdown-menu user-profile-menu list-unstyled">
							
							<li>
								<a href="javascript:" data-href="${ctx}/sys/user/info" id="28" title="${language.index_userInfo }">
									<i class="fa fa-user fa-fw"></i>
									<span class="title">${language.index_userInfo }</span>
								</a>
							</li>
							<li>
								<a href="${ctx}/sys/user/lockscreen" title="${language.index_lockscreen }">
									<i class="fa fa-lock fa-fw"></i>
									<span class="title">${language.index_lockscreen }</span>
								</a>
							</li>
							<li>
								<a href="${ctx}/logout" title="${language.index_loginOut }">
									<i class="fa fa-fw fa-power-off"></i>
									<span class="title">${language.index_loginOut }</span>
								</a>
							</li>
						</ul>
					</li>
				<%-- 	<li>
						<a href="#" data-toggle="chat" title="${language.index_chat }">
							<i class="fa-comments-o"></i>
						</a>
					</li> --%>
				</ul>
			</nav>
			<div class="iframe-content row">
				<ul class="nav nav-tabs col-sm-12">
					<li class="active" data-tag="navTab">
						<a href="#tab_index" data-toggle="tab">
						 <c:forEach items="${fns:getDictList('index_yeqian')}" var="dict">
						 	<input class="selectContent  ${dict.value}" type="hidden" value="${dict.label}" name="${dict.value}"/>
							</c:forEach> 
							<i class="hidden-xs">${language.index_homePage }</i>
						</a>
					</li>
				</ul>
				<div class="tab-content col-sm-12">
					<div class="tab-pane active" id="tab_index">
						<iframe id='pageContent' name="pageContent" src="${ctx}/sys/user/toDrag" style="border: none;"></iframe>
					</div>
				</div>
			</div>
			<footer class="main-footer sticky footer-type-1 fixed">
				<div class="footer-inner">
					<!-- Add your copyright text here -->
					<div class="footer-text" id="footer-copyright">
					${fns:getConfig('companyVersion')}
					</div>
					<div class="go-up">
						<a href="#" rel="go-top" title="回到顶部">
							<i class="fa-angle-up"></i>
						</a>
					</div>
				</div>
			</footer>
		</div>
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/joinable.js"></script>
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/TweenMax.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/resizeable.js"></script>
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/SnowLeopard-custom.js"></script>
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/SnowLeopard-toggles.js"></script>
	<script	type="text/javascript" src="${ctxStatic}/snowLeopard/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/snowLeopard/js/SnowLeopard.initPlatform.js"></script>
	<script type="text/javascript">
	   jQuery(document).ready(function($) {
		   platForm.bindMainMenuClick('.user-profile-menu a');
		   platForm.bindMainMenuClick('.external a');
	  });
	 /*   $("#goHome").click(function (){
		   window.open($(this).data('href'));            
	   }); */
	</script>
</body>
</html>
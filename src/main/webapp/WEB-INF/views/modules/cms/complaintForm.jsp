<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="decorator" content="default" />
  <title>意见投诉审核</title>
  <link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/common/jeesite.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/flavr/css/flavr.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/toastr/toastr.min.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-skins.css"/>
<style type="text/css">
	.consultation-detail {
		width: 100%;
		overflow: hidden;
		margin-bottom: 20px;
		min-height: 660px;
	}
	
	.consultation-detail .detail-content {
		width: 100%;
		margin: 20px auto 0;
	}
	
	.consultation-detail .detail-content .detail-main {
		width: 100%;
		float: left;
	}
	
	.consultation-detail .detail-content .detail-main .bread {
		padding-left: 30px;
		position: relative;
		height: 30px;
		line-height: 20px;
		padding-bottom: 10px;
		border-bottom: 1px solid #ccc;
		width: 100%;
		margin: 20px auto;
		box-sizing: border-box;
	}
	
	.consultation-detail .detail-content .detail-main .bread:before {
		content: url(../../img/icon4.png);
		position: absolute;
		top: 1px;
		left: 10px;
	}
	
	.consultation-detail .detail-content .detail-main .bread ul {
		list-style: none;
	}
	
	.consultation-detail .detail-content .detail-main .bread ul li {
		font-size: 13px;
		height: 20px;
		line-height: 20px;
		float: left;
	}
	
	.consultation-detail .detail-content .detail-main .bread ul li:after {
		content: '>';
		height: 20px;
		line-height: 20px;
		padding: 0 3px;
	}
	
	.consultation-detail .detail-content .detail-main .bread ul li:last-child:after {
		content: '';
		display: none;
	}
	
	.consultation-detail .detail-content .detail-main .search-box {
		width: 400px;
		height: 32px;
		line-height: 32px;
		border: 2px solid #1296D6;
		margin-bottom: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .search-box input {
		width: 340px;
		height: 32px;
		line-height: 32px;
		padding-left: 10px;
		box-sizing: border-box;
		border: none;
		float: left;
	}
	
	.consultation-detail .detail-content .detail-main .search-box button {
		width: 60px;
		height: 32px;
		line-height: 32px;
		background: #1296D6;
		color: #FFFFFF;
		text-align: center;
		border: none;
		float: right;
		cursor: pointer;
	}
	
	.consultation-detail .detail-content .detail-main .detail-q {
		width: 100%;
		box-sizing: border-box;
		padding-left: 40px;
		padding-right: 20px;
		position: relative;
		border-bottom: 1px solid #ccc;
		padding-bottom: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-q:before {
		content: url(../../img/q.png);
		position: absolute;
		width: 23px;
		height: 23px;
		top: 5px;
		left: 8px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-q .q-title {
		font-size: 24px;
		font-weight: bold;
		line-height: 1.5;
	}
	
	.consultation-detail .detail-content .detail-main .detail-q .q-msg {
		font-size: 13px;
		margin-top: 15px;
		margin-bottom: 10px;
		line-height: 1.5;
	}
	
	.consultation-detail .detail-content .detail-main .detail-q .q-time {
		font-size: 13px;
		text-align: right;
		padding-right: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a {
		width: 100%;
		box-sizing: border-box;
		padding-right: 20px;
		position: relative;
		padding-bottom: 20px;
		margin-top: 10px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .a-title {
		width: 100%;
		height: 30px;
		line-height: 30px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .a-title p {
		position: relative;
		height: 30px;
		line-height: 30px;
		width: 120px;
		font-size: 16px;
		font-weight: bold;
		text-align: center;
		padding: 0 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .a-title p:before {
		content: url(../../img/icon.png);
		width: 3px;
		height: 30px;
		position: absolute;
		top: 2px;
		left: 12px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .a-title p:after {
		content: url(../../img/icon.png);
		width: 3px;
		height: 30px;
		position: absolute;
		top: 2px;
		right: 12px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .a-msg {
		width: 100%;
		box-sizing: border-box;
		padding-left: 40px;
		padding-right: 20px;
		position: relative;
		padding-bottom: 10px;
		margin-top: 20px;
		border-bottom: 1px solid #CCCCCC;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .a-msg:before {
		content: url(../../img/an.png);
		position: absolute;
		width: 23px;
		height: 23px;
		top: 5px;
		left: 8px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .a-msg .name {
		height: 55px;
		line-height: 40px;
		font-size: 13px;
		color: #1296D6;
		position: relative;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .a-msg .msg {
		font-size: 13px;
		line-height: 1.5;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .qa-closely {
		margin-top: 15px;
		width: 100%;
		box-sizing: border-box;
		border-bottom: 1px solid #CCCCCC;
		font-size: 13px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .qa-closely .closely-q {
		box-sizing: border-box;
		padding-left: 100px;
		margin-bottom: 20px;
		position: relative;
		line-height: 1.5;
		height: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .qa-closely .closely-q:before {
		content: '追问';
		width: 40px;
		text-align: center;
		display: block;
		margin: 0 auto;
		height: 20px;
		line-height: 20px;
		border: 1px solid #e1001a;
		border-radius: 3px;
		position: absolute;
		top: 0px;
		left: 50px;
		font-size: 13px;
		color: #E1001A;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .qa-closely .closely-a {
		box-sizing: border-box;
		padding-left: 100px;
		position: relative;
		line-height: 1.5;
		height: 20px;
		margin-bottom: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .qa-closely .closely-a:before {
		content: '追答';
		width: 40px;
		text-align: center;
		display: block;
		margin: 0 auto;
		height: 20px;
		line-height: 20px;
		border: 1px solid #29a0da;
		border-radius: 3px;
		position: absolute;
		top: 0px;
		left: 50px;
		font-size: 13px;
		color: #29a0da;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs {
		padding: 20px 0;
		border-bottom: 1px solid #CCCCCC;
		text-align: right;
		font-size: 13px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .a-msg .detail-thumbs {
		padding: 10px 0;
		border: 0px solid #CCCCCC;
		text-align: right;
		font-size: 10px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs .re-time {
		display: inline-block;
		margin-right: 15px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs #good {
		display: inline-block;
		margin-right: 15px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs #good img {
		width: 18px;
		height: 18px;
		vertical-align: middle;
		cursor: pointer;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs #diss {
		display: inline-block;
		margin-right: 15px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs #diss img {
		width: 18px;
		height: 18px;
		vertical-align: middle;
		cursor: pointer;
	}
	
	.consultation-detail .detail-content .detail-main .detail-a .qa-box .detail-thumbs .closely-btn {
		display: inline-block;
		background: transparent;
		margin-right: 15px;
		border: 1px solid #1296D6;
		color: #1296D6;
		border-radius: 4px;
		text-align: center;
		width: 50px;
		height: 22px;
		line-height: 20px;
		cursor: pointer;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list .title-text {
		width: 100%;
		height: 30px;
		line-height: 30px;
		margin-bottom: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list .title-text p {
		position: relative;
		height: 30px;
		line-height: 30px;
		width: 120px;
		font-size: 16px;
		font-weight: bold;
		padding: 0 20px;
		text-align: center;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list .title-text p:before {
		content: url(../../img/icon.png);
		width: 3px;
		height: 30px;
		position: absolute;
		top: 2px;
		left: 12px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list .title-text p:after {
		content: url(../../img/icon.png);
		width: 3px;
		height: 30px;
		position: absolute;
		top: 2px;
		right: 12px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list ul {
		width: 100%;
		list-style: none;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list ul li {
		padding: 15px 0 15px 40px;
		border-bottom: 1px solid #CCCCCC;
		width: 100%;
		box-sizing: border-box;
		position: relative;
		line-height: 1.4;
		cursor: pointer;
	}
	
	.consultation-detail .detail-content .detail-main .detail-list ul li:before {
		content: url(../../img/q.png);
		position: absolute;
		width: 23px;
		height: 23px;
		top: 15px;
		left: 5px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .title-text {
		width: 100%;
		height: 30px;
		line-height: 30px;
		margin-bottom: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .title-text p {
		position: relative;
		height: 30px;
		line-height: 30px;
		width: 120px;
		font-size: 16px;
		font-weight: bold;
		padding: 0 20px;
		text-align: center;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .title-text p:before {
		content: url(../../img/icon.png);
		width: 3px;
		height: 30px;
		position: absolute;
		top: 2px;
		left: 12px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .title-text p:after {
		content: url(../../img/icon.png);
		width: 3px;
		height: 30px;
		position: absolute;
		top: 2px;
		right: 12px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .knowledge-q {
		box-sizing: border-box;
		padding-left: 40px;
		padding-right: 20px;
		position: relative;
		border-bottom: 1px solid #ccc;
		padding-bottom: 20px;
		font-weight: bold;
		width: 100%;
		line-height: 30px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .knowledge-q:before {
		content: url(../../img/q.png);
		position: absolute;
		width: 23px;
		height: 23px;
		top: 5px;
		left: 8px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .knowledge-a {
		width: 100%;
		padding-top: 5px;
		box-sizing: border-box;
		padding-left: 40px;
		padding-right: 20px;
		position: relative;
		padding-bottom: 20px;
		margin-top: 20px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .knowledge-a:before {
		content: url(../../img/an.png);
		position: absolute;
		width: 23px;
		height: 23px;
		top: 5px;
		left: 8px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .knowledge-a .a-text {
		width: 100%;
		background: #f3fafd;
		font-size: 15px;
		line-height: 1.5;
		position: relative;
		padding: 10px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .knowledge-a .a-text:before {
		content: '';
		width: 10px;
		height: 10px;
		background-color: #F3FAFD;
		transform: rotate(45deg);
		position: absolute;
		top: 10px;
		left: -5px;
	}
	
	.consultation-detail .detail-content .detail-main .detail-knowledge .from {
		width: 100%;
		height: 30px;
		line-height: 30px;
		text-align: right;
		color: #1296D6;
		font-size: 13px;
		border-bottom: 1px solid #CCCCCC;
	}
	
	.qa-box .detail-thumbs .closely-btn {
		display: inline-block;
		background: transparent;
		margin-right: 15px;
		border: 1px solid #1296D6;
		color: #1296D6;
		border-radius: 4px;
		text-align: center;
		width: 50px;
		height: 22px;
		line-height: 20px;
		cursor: pointer;
	}
	
	.qa-box .detail-thumbs .reClosely {
		position: relative;
		width: 100%;
		margin-top: 20px;
		height: 80px;
		display: none;
	}
	
	.qa-box .detail-thumbs .reClosely .closelyTextarea {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		resize: none;
		padding: 5px 100px 5px 5px;
	}
	
	.qa-box .detail-thumbs .reClosely .closelyTextareaBtn {
		position: absolute;
		display: inline-block;
		background: transparent;
		margin-right: 15px;
		border: 1px solid #1296D6;
		color: #1296D6;
		border-radius: 4px;
		text-align: center;
		width: 50px;
		height: 22px;
		line-height: 20px;
		cursor: pointer;
		top: 30px;
		right: 0px;
	}
	
	.qa-box .a-msg .detail-thumbs .closely-ban {
		display: inline-block;
		background: transparent;
		margin-right: 15px;
		border: 1px solid #1296D6;
		color: #1296D6;
		border-radius: 4px;
		text-align: center;
		width: 50px;
		height: 22px;
		line-height: 0px;
		cursor: pointer;
	}
	
		.qa-box .a-msg .detail-thumbs .closely-bann {
		display: inline-block;
		background: transparent;
		margin-right: 15px;
		border: 1px solid #1296D6;
		color: #1296D6;
		border-radius: 4px;
		text-align: center;
		width: 50px;
		height: 22px;
		line-height: 0px;
		cursor: pointer;
	}
	
	.qa-box .a-msg .detail-thumbs .reCloselys {
		position: relative;
		width: 100%;
		margin-top: 20px;
		height: 80px;
		display: none;
	}
	
	.qa-box .a-msg .detail-thumbs .reCloselys .closelyTextareas {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		resize: none;
		padding: 5px 100px 5px 5px;
	}
	
	.qa-box .a-msg .detail-thumbs .reCloselys .closelyTextareaBan {
		position: absolute;
		display: inline-block;
		background: transparent;
		margin-right: 15px;
		border: 1px solid #1296D6;
		color: #1296D6;
		border-radius: 4px;
		text-align: center;
		width: 50px;
		height: 22px;
		line-height: 20px;
		cursor: pointer;
		top: 30px;
		right: 0px;
	}
	
	.closely-time {
		  position: absolute;
		  top: 0;
		  right: 0;
		  float: right;
		  font-size: 13px;
		  text-align: right;
		  line-height: 2;
    }
  	
  	.qa-box .a-msg .a-io {
  		position: relative;
		width: 100%;
		margin-top: 20px;
		height: 56%;
		display: none;
	}
	
	.qa-box .a-msg .a-io .io {
		width: 100%;
		height: 56%;
		box-sizing: border-box;
		resize: none;
		padding: 12px 111px 58px 41px;
	}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$("#reContent").focus();
		$("#inputForm").validate({
			submitHandler: function(form) {
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if(element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});	
		var a = 0;
		var b = 0;
		$(".closely-btn").click(function() {
			if(a == 0){
				$(".reClosely").show();
				a = 1;
			}else{
				$(".reClosely").hide();
				a = 0;
			}
		})
		$(".closely-ban").click(function() {
			if(b == 0){
				$(this).siblings(".reCloselys").show();
				b = 1;
			}else{
				$(this).siblings(".reCloselys").hide();
				b = 0;
			}
		})
		$(".closely-bann").click(function() {
		   if(a == 0){
			   $(this).parent().siblings(".a-io").show();
			   $(this).parent().siblings(".msg").hide();
				a = 1;
			}else{
			   $(this).parent().siblings(".a-io").hide();
			   $(this).parent().siblings(".msg").show();
			   a = 0;
			}
		})
		$(".closely-aa").click(function() {
			 var arrss=$(this).attr("data-name");
		          if(a == 0){
			   $("."+arrss).show();
			   $("#"+arrss).hide();
				a = 1;
			}else{
				 $("."+arrss).hide();
				  $("#"+arrss).show();
				a = 0;
			}
		})
			
	});
</script>
<script type="text/javascript">
	$(function(){
		$(".closelyTextareaBan").click(function(){//点击按钮提交
			var content = $(this).siblings(".closelyTextareas").val();
			var arrss=$(this).attr("data-name");
		  	if(content.length == 0){
			 	return alert("追答内容不能为空"); 
			}else{ 
	            $.ajax({
	                 url:"${ctx}/cms/guestbookcommentre/save",
	                 data: ({'id':arrss,"conttent":content,'type':"complaintForm"}),
	                 type:'post',
	                 success:function(data){
	                	if(data.data="success"){
	                		//alert("保存追答成功");
	                		$("#inputForm").attr("action","${ctx}/cms/complaint/form");
	                		$("#inputForm").submit();
	                		$("#inputForm").attr("action","${ctx}/cms/complaint");
	                	}else{
	                		//alert("存追答失败");
	                		$("#inputForm").attr("action","${ctx}/cms/complaint/form");
	                		$("#inputForm").submit();
	                		$("#inputForm").attr("action","${ctx}/cms/complaint");
	                	}
	              
	                 }
	            })   
		    }
		 })
		//修改回复内容
		 	$(".closely-bannn").click(function(){//点击按钮提交
		 	var content = $(this).siblings(".io").val();
			var arrss=$(this).attr("data-name");
		  	if(content.length == 0){
			 	return alert("修改内容不能为空"); 
			}else{ 
	            $.ajax({
	                 url:"${ctx}/cms/guestbookcommentre/updateContent",
	                 data: ({'id':arrss,"conttent":content,'type':"complaint"}),
	                 type:'post',
	                 success:function(data){
	                	if(data.data="success"){
	                		
	                		//alert("保存追答成功");
	                		window.location.href="${ctx}/cms/complaint/form?id="+data.id;
	                	}else{
	                		window.location.href="${ctx}/cms/complaint/form?id="+data.id;
	                	}
	              
	                 }
	            })   
		    }
		 })
		 //删除追问
		 $(".closely-bannnn").click(function(){
			 var arrss=$(this).attr("data-name");
			 if(window.confirm('要删除改记录吗')){
				 window.location.href="${ctx}/cms/guestbookcommentre/delete?id="+arrss+"&type="+"complaint"; 
			 }else{
				 $(".closely-bannnn").focus();
			 }
			 
		 })
		 //删除追答
		  $(".closely-aaaa").click(function(){
			 var arrss=$(this).attr("data-name");
			 if(window.confirm('要删除改记录吗')){
				 window.location.href="${ctx}/cms/guestbookcommentre/deleteguestbookcommentre?id="+arrss+"&type="+"complaint"; 
			 }else{
				 $(".closely-aaaa").focus();
			 }
			 
		 })
		 $("#closely-btn-delete").click(function() {
			    var statu = confirmx('确认要删除该意见投诉吗？', jsCtx + "/cms/complaint/delete?id=" + $("#complaintid").val()+ "");
		  });
		 //修改追答内容
		 $(".closely-aaa").click(function(){//点击按钮提交
			var content = $("#commentretext").val();
			var arrss=$(this).attr("data-name");
		  	if(content.length == 0){
			 	return alert("修改追答内容不能为空"); 
			}else{ 
	            $.ajax({
	                 url:"${ctx}/cms/guestbookcommentre/updateguestbookcommentre",
	                 data: ({'id':arrss,"conttent":content,'type':"complaint"}),
	                 type:'post',
	                 success:function(data){
	                	if(data.data="success"){
	                		
	                		//alert("保存追答成功");
	                		window.location.href="${ctx}/cms/complaint/form?id="+data.id;
	                	}else{
	                		window.location.href="${ctx}/cms/complaint/form?id="+data.id;
	                	}
	              
	                 }
	            })   
		    }
		 })
	})
</script>
</head>
<body>
<ul class="nav nav-tabs">
			<li>
				<a href="${ctx}/cms/complaint/">意见投诉列表</a>
			</li>
			<li class="active">
				<a href="${ctx}/cms/complaint/form?id=${complaint.id}">意见投诉<!--
					--><shiro:hasPermission name="cms:complaint:edit">${complaint.delFlag eq '2'?'审核':'查看'}</shiro:hasPermission><!--
					--><shiro:lacksPermission name="cms:complaint:edit">查看</shiro:lacksPermission>
				</a>
			</li>
		</ul><br/>
<div class="wrapper">
  <div class="blank" ></div>
  <form:form id="inputForm" modelAttribute="complaint" action="${ctx}/cms/complaint/save" method="post" class="form-horizontal">
   <form:hidden path="id" id="complaintid"/>
			<form:hidden path="delFlag" />
			<sys:message content="${message}" />
			<div class="consultation-detail">
			<div class="content-wrapper">
				<div class="detail-content">
					<div class="detail-main">
						<div class="detail-q">
							<div class="q-title">${complaint.title}</div>
							<div class="q-msg">${complaint.content}</div>
							<div class="qa-box">
								<div class="detail-thumbs">
									<div class="q-time">
										意见投诉人：<span id="q-time">${complaint.name}</span>&nbsp&nbsp&nbsp意见投诉时间：<span id="q-time"><fmt:formatDate value="${complaint.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
									</div>
									<br/>
									<div class="q-time">
										<input class="closely-btn" type="button" value="回复"/>
										<input class="closely-btn" id="closely-btn-delete"  type="button" value="删除"/>
									</div>
									<div class="reClosely">
										<textarea name="reContent" cols="2" class="closelyTextarea" ></textarea>
										<input class="closelyTextareaBtn" type="submit" value="提交"/>
									</div>
								</div>
							</div>
						</div>
						<div class="detail-a">
							<div class="a-title">
								<p>回复内容</p>
							</div>
							<c:forEach var="comment" items="${complaint.commentList }">
							<div class="qa-box">
								<div class="a-msg">
									<div class="name"><span class="userInfo">${comment.createUser.name }</span> <span class="userInfo">${comment.createUser.userType }</span></div>
									<div class="msg" id="${comment.id}">${comment.content}</div>
									<div class="a-io">
 									   <textarea class="io">${comment.content}</textarea>
                                    	<input class="closely-bannn" type="button" value="修改" data-name="${comment.id}" style="    position: absolute;display: inline-block;background: transparent;margin-right: -3px; border: 1px solid #1296D6;color: #1296D6; border-radius: 4px; text-align: center;width: 49px;height: 22px; line-height: 20px;cursor: pointer; top: 34px;right: 24px;"/>
									<%-- <input type="button"  class="closely-bannnn" value="删除" data-name="${comment.id}" style="position: absolute; display: inline-block; background: transparent;margin-right: -3px;border: 1px solid #1296D6; color: #1296D6; border-radius: 4px;text-align: center;width: 49px;height: 22px;line-height: 20px; cursor: pointer;top: 83px;right: 34px;"> --%>
									</div>
									<div class="detail-thumbs">
										<input class="closely-ban" type="button" value="追答" />
										<input class="closely-bann" type="button" value="编辑" />
										<div class="reCloselys">
											<textarea class="closelyTextareas"></textarea>
											<input class="closelyTextareaBan" type="button" value="提交 " data-name="${comment.id}"/>
										</div>
									</div>
								</div>
								<c:if test="${not empty comment.guestbookCommentReList}">
									<div class="qa-closely">
										<c:forEach var="commentre" items="${comment.guestbookCommentReList}">
											<c:choose> 
									           <c:when test="${commentre.commentType eq '0' }">
									             <div class="closely-q">${commentre.content}
									             	<div class="closely-time"><fmt:formatDate value="${commentre.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
									             </div>
									           </c:when>
									           <c:otherwise>
									         	 <div class="closely-a" id="${commentre.id}">${commentre.content}<div class="closely-time"><fmt:formatDate value="${commentre.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div></div>
									             <div class="${commentre.id}" style="display:none; position: relative;">
									             	<textarea id="commentretext" id="commentretext" name="text" style="width: 100%;height: 56%;box-sizing: border-box;resize: none;padding: 12px 111px 58px 41px;">${commentre.content}</textarea>
									                <input class="closely-aaa" type="button" value="修改" data-name="${commentre.id}" style="    position: absolute;display: inline-block;background: transparent; margin-right: -3px;border: 1px solid #1296D6;color: #1296D6;border-radius: 4px;text-align: center;width: 49px; height: 22px;line-height: 20px; cursor: pointer; top:10px; right: 34px;"/>
									                <%-- <input class="closely-aaaa" type="button" value="删除" data-name="${commentre.id}" style="    position: absolute;display: inline-block;background: transparent; margin-right: -3px;border: 1px solid #1296D6;color: #1296D6;border-radius: 4px;text-align: center;width: 49px; height: 22px;line-height: 20px; cursor: pointer;top: 43px; right: 34px;"/> --%>
									             </div>
<%-- 									         <input class="closely-aa" type="button" value="编辑" data-name="${commentre.id}" style="display: inline-block;background: transparent;margin-right: 15px;border: 1px solid #1296D6;color: #1296D6;border-radius: 4px;text-align: center;width: 50px;height: 22px;line-height: 0px;cursor: pointer;right: -93px; margin-left: 1358px;"/>
 --%>									       </c:otherwise> 
									        </c:choose>
								        </c:forEach>
							        </div>
							     </c:if>
							</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
     </form:form>
  </div>
</body>
</html>
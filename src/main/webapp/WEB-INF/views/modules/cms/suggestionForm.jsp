<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="decorator" content="default" />
  <title>意见审核</title>
  <link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/common/jeesite.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/flavr/css/flavr.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/toastr/toastr.min.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/SnowLeopard-skins.css"/>
  <style>
    .blank{
      height: 20px;
    }
    .footer {
      background-color: rgb(244,244,244);
    }
    .form-control{
      background-color:rgb(244,244,244) ;
    }
    input{
      background-color: rgb(244,244,244);
    }
    .radius-btn{
      background: rgb(18,150,215);
      border: none;
      color: #fff;
      height: 23px;
      line-height: 23px;
      width: 48px;
      border-radius: 15px;
      cursor: pointer;
    }
    .mt10{
      margin-top: 10px;
    }
    .userInfo{
      display: inline-block;
      margin-left: 10px;
      vertical-align: middle;
    }
    .icon {
      background-image: url(iconpp.png);
      background-repeat: no-repeat;
      border: none;
      color: #fff;
      height: 20px;
      line-height: 25px;
      width: 31px;
      border-radius: 5px;
      cursor: pointer;
    }
  </style>
  <style>
        .pink-box{
          background-color: rgb(255,246,247);
          width: 100%;
          position: relative;
          margin-top:20px;
        }
        .pink-box:before{
          content: '';
          width: 10px;
          height: 10px;
          background-color: rgb(255,246,247);
          -webkit-transform: rotate(90deg);
          -moz-transform: rotate(45deg);
          -ms-transform: rotate(45deg);
          -o-transform: rotate(45deg);
          transform: rotate(45deg);
          position: absolute;
          top: 10px;
          left: -5px;
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
				
				      
			});
		</script>
		<script type="text/javascript">
			$(function(){
				$(".radius-btn").click(function(){//点击按钮提交
					  var txtVal=$(this).parent().siblings('.col-sm-9').find('input').val()
				        var arrss=$(this).attr("data-name");
					  if (txtVal.length == 0)
				        {
						 return alert("追答内容不能为空") }
					
					  else{ 
						  $(this).parent().siblings('.col-sm-9').find('input')=="";

					            $.ajax({
					                 url:"${ctx}/cms/guestbookcommentre/save",
					                 data: ({'id':arrss,"conttent":txtVal,'type':"guestbookForm"}),
					                 type:'post',
					                 success:function(data){
					                	if(data.data="success"){
					                		 $("#"+arrss+"").find("div").append('<br><br><br><br><br><br><br><div class="row"><div class="col-sm-1" style="font-size: 16px;color: red;line-height:1;">追答</div>').append(txtVal).append('<div class="col-sm-9"> </div></div>') 
					                		 
					                		 alert("保存追答成功");
					                	}else{
					                		alert("报存追答失败");
					                	}
					              
					                 }
					            })   }
				 })
			})
		</script>
</head>
<body>
<ul class="nav nav-tabs">
			<li>
				<a href="${ctx}/cms/suggestion/">留言列表</a>
			</li>
			<li class="active">
				<a href="${ctx}/cms/suggestion/form?id=${guestbook.id}">留言<!--
					--><shiro:hasPermission name="cms:suggestion:edit">${guestbook.delFlag eq '2'?'审核':'查看'}</shiro:hasPermission><!--
					--><shiro:lacksPermission name="cms:suggestion:edit">查看</shiro:lacksPermission>
				</a>
			</li>
		</ul><br/>
<div class="wrapper">
  <div class="blank" ></div>
  <form:form id="inputForm" modelAttribute="suggestion" action="${ctx}/cms/suggestion/save" method="post" class="form-horizontal">
   <form:hidden path="id" id="complaintid"/>
			<form:hidden path="delFlag" />
			<sys:message content="${message}" />
    <div class="row">
      <div class="col-sm-3">
        <div class="form-group">
          <label for="title">标题:</label> 
       <form:input path="title" htmlEscape="false" readonly="true" maxlength="100" class="input-xlarge form-control "/>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label for="messageP">留言人:</label>
          <form:input path="name" htmlEscape="false" readonly="true" maxlength="100" class="input-xlarge form-control "/>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label for="businessType">业务类型:</label>
          <input type="input" class="form-control" id="businessType" placeholder="公证咨询">
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label for="guestionType">问题类型:</label>
          <input type="input" class="form-control" id="guestionType" placeholder="无分类">
        </div>
      </div>
    </div>
    <div class="blank"></div>
    <div class="row">
      <div class="col-sm-9">
        <div class="form-group">
          <label for="messageDetails">留言内容:</label>
           <form:input path="content" htmlEscape="false" readonly="true" maxlength="100" class="input-xlarge form-control "/>
        </div>
      </div>
      <div class="col-sm-3">
        <div class="form-group">
          <label for="messageTime">留言时间:</label>
           <form:input path="createDate"  htmlEscape="false" readonly="true" maxlength="100" class="input-xlarge form-control "/>
        </div>
      </div>
    </div>
    <div class="blank"></div>
    <div class="row">
      <div class="col-sm-9">
        <div class="form-group">
          <label for="reply">回复:</label>
          <form:textarea path="reContent" htmlEscape="false" rows="8" class="input-xlarge form-control" value=""/>
        </div>
      </div>
      <div class="col-sm-3"></div>
    </div>
    <div class="row">
      <div class="col-sm-1">
     <input class="btn btn-primary" type="submit" value="回复" />&nbsp;
      </div>
    </div>
  </div>
     <c:forEach var="comment" items="${suggestion.commentList }">
	     <div class="col-sm-9" style="margin-top:20px">
	     	<div class="footer col-sm-12">
		     	<div class="blank"></div>
		  		<div class="row">
		    		<div class="col-sm-2">回复内容:</div>
		    		<div class="blank"></div>
		    	</div>
		      	<div class="row">
		       		<div class="col-sm-12">
		       	 		<img src="${comment.createUser.photo}" height="20" width="20" style="border-radius: 50%;">
		       			<span class="userInfo">${comment.createUser.name }</span> 
		         		<span class="userInfo">法律服务人员</span>
		      		</div>
		      	</div>
		      	<div class="row" id="${comment.id}">
		        	<div class="col-sm-11">
		        		<textarea name="" id="" cols="15" rows="5" class="col-xs-9" style="margin-top: 20px;">${comment.content}</textarea>
		        	</div>
		      	</div>
		      	<c:forEach var="commentre" items="${comment.guestbookCommentReList}">
			       <div class="row">
			       	<div class="col-sm-12" style="padding: 10px;margin-top: 20px;">
					     <div class="blank"></div>
					     <div class="row" >
					       <c:choose> 
					         <c:when test="${commentre.commentType eq '0' }">
					           <div class="col-sm-1" style="font-size: 16px;color: red;line-height:1;">追问</div>
					         </c:when>
					          <c:otherwise>
					           <div class="col-sm-1" style="font-size: 16px;color: red;line-height:1;">追答</div>
					          </c:otherwise> 
					       </c:choose>
					       <div class="col-sm-9">${commentre.content}</div>
					     </div>
					 </div>
			       </div>
			     </c:forEach>
	      	</div>
	      </div>
      	<div class="col-sm-3">
      	<div class="pink-box">
        <div class="row">
          <div class="col-sm-4" style="padding: 10px;" >解答时效</div>
          <div class="col-sm-4" style="padding: 10px;">满意</div>
        </div>
        <div class="row">
          <div class="col-sm-4" style="padding: 10px;" >解答时效</div>
          <div class="col-sm-4" style="padding: 10px;">满意</div>
        </div>
        <div class="row">
          <div class="col-sm-4" style="padding: 10px;" >解答时效</div>
          <div class="col-sm-4" style="padding: 10px;">满意</div>
        </div>
      </div>
      	</div>
       <div class="blank"></div>
		     <div class="row">
          <div class="col-sm-9">
            <div class="form-group">
              <input type="input" class="form-control" >
            </div>
          </div>
          <div class="col-sm-1" style="padding: 12px;margin-top: -7px;">
            <input type="button" value="追答" class="radius-btn" data-name="${comment.id}">
          </div>
    </div>
     </c:forEach>
     </form:form>
  </div>
</div>
</body>
<script>
var $select = $(".select")
$select.hide()
  $(".btn").click(function (event) {
     if ($select.is(":hidden")){
       $select.show()
     }else {
       $select.hide()
     }

     event.preventDefault()
  })
</script>
</html>
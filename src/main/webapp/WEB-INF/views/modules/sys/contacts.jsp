<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<title>通讯录</title>
		<link href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-forms.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-components.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-skins.css" rel='stylesheet' type="text/css"/>
		<link href="${ctxStatic}/toastr/toastr.min.css" type="text/css" rel="stylesheet"/>
		<link href="${ctxStatic}/flavr/css/flavr.css" type="text/css" rel="stylesheet"/>
		<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	</head>
	<style>
		.touxiang:before{
			display: table;
		    line-height: 0;
		    content: "";
		}
		.touxiang:after{
			clear: both;
		}
		.touxiang{
		    margin-bottom: 8px;
		    border-bottom: 1px solid #ddd;
		    margin-left: 0;
   			list-style: none;
   			padding: 0;
   			height:38px;
		}
		.touxiang>li{
		    margin-bottom: -1px;
		    float: left;
	        line-height: 20px;
	        height:38px;
		}
		.touxiang>.active>a{
		    color: #555;
		    cursor: default;
		    background-color: #fff;
		    border: 1px solid #ddd;
		    border-bottom-color: transparent;
		}
		.touxiang>li>a{
		    padding-top: 8px;
		    padding-bottom: 8px;
		    line-height: 20px;
		    border-width: 1px;
		    border-style: solid;
		    border-color: transparent;
		    border-image: initial;
		    border-radius: 4px 4px 0px 0px;
	        padding-right: 12px;
		    padding-left: 12px;
		    margin-right: 2px;
		    outline: none !important;
		    color: #444444;
   			text-decoration: none !important;
	        display: block;
		    font-family: "Helvetica Neue",Helvetica,Arial,"Microsoft Yahei","Hiragino Sans GB","Heiti SC","WenQuanYi Micro Hei",sans-serif;
   			font-size: 13px; 
		}
		p{
			margin:0;
		}
		table{
			border-spacing:0;
		}
		td{
			border-left:0;																
			border-right:0;
		}
		.user-baseinfo{
			cursor: pointer;
		}
		.user-job {
		    margin-left: -1px;
		}
		.text-center{
		    font-size: 12px;
    		text-align: center;
		}
		.fliter-search{
		    display: inline-block;
		    width: 100%;
		    border: 1px solid #CDD7E5;
		    padding: 8px 6px 7px 15px;
		    outline: none;
		    height: 33px;
    	}
	</style>
	<script type="text/javascript">
		$(document).ready(function(){
			$.each($("#userList tr"), function(i){   
			   if(i>10){
				   $(this).hide();  
			   } 
			}); 
			var zResult=new Array();
			$.post("${ctx}/sys/user/addBooks",{firstName:$(this).text()},function(result){
				zResult	= result;
		  	});
			$(document).on("input","#sousuo",function(){
				var btm = $(this).val();
				var yhList = "";
				var result = zResult;
				for(var i=0;i<result.length;i++){
					if(result[i].name.indexOf(btm)!=-1){
						var roleName = "";
						if(result[i].roleList!=null){
							for(var j=0;j<result[i].roleList.length;j++){
								roleName+='<span class="user-job">'+
								/* ${fns:getDictLabels("","sys_user_type",result[i].roleList[j].userType,'未填写')} */
											 result[i].roleList[j].name+ 
										'</span>';
							} 
						}
						var department = "";
						if(result[i].office==null||result[i].office==''||result[i].office.name==null||result[i].office.name==''){
							department = "未填写";
						}else{
							department = result[i].office.name;
						}
						var qq = "";
						var weChat = "";
						if(result[i].userExpand!=null&&result[i].userExpand!=''){
							if(result[i].userExpand.qq==null||result[i].userExpand.qq==""){
								qq = "未填写";
							}else{
								qq = result[i].userExpand.qq;
							}
							if(result[i].userExpand.weChat==null||result[i].userExpand.weChat==""){
								weChat = "未填写";
							}else{
								weChat = result[i].userExpand.weChat;
							}
						}else{
							qq = "未填写";
							weChat = "未填写";
						}
						var photo = result[i].photo==null||result[i].photo==''?'未填写':result[i].photo;
						var name = result[i].name==null||result[i].name==''?'未填写':result[i].name;
						var mobile = result[i].mobile==null||result[i].mobile==''?'未填写':result[i].mobile;
						var addresss = result[i].address==null||result[i].address==''?'未填写':result[i].address;
						var address ="";
						if(addresss.length>10){
							address = addresss.substring(0,10)+"......"; 
						}else{
							address = addresss;
						}
						
						var email = result[i].email==null||result[i].email==''?'未填写':result[i].email;
						var bir = result[i].bir==null||result[i].bir==''?'未填写':result[i].bir.substring(0,10);
				    	yhList+='<tr>'+
							'<td class="user-baseinfo">'+
					    		'<input type="hidden" class="weixin" value="'+weChat+'">'+
					    		'<input type="hidden" class="birthday" value="'+bir+'">'+
								'<img src="${fns:getConfig('server_url')}'+photo+'" alt="" class="user-img"> '+
								'<div class="text-info">'+
									'<span class="user-name">'+name+'</span>'+
									'<p>'+
										'<span class="user-department">'+
											department+
										'</span>&nbsp;|&nbsp; '+
										'<span class="user-job">'+
											result[i].userType+
										'</span>'+
									'</p>'+
								'</div>'+
							'</td>'+
							'<td>'+
								'<span class="user-cellphone">'+
								mobile+
								'</span>'+
							'</td>'+
							'<td>'+
								'<span class="user-adr" title='+addresss+'>'+
									address+
								'</span>'+
							'</td>'+
							'<td>'+
								'<span class="user-QQ">'+
									qq+
								'</span>'+
							'</td>'+
							'<td>'+
								'<span class="user-email">'+
									email+
								'</span>'+
							'</td>'+
							'<td class="user-control text-center">'+
								'<span class="docs-tooltip"  title="" data-original-title="操作">'+
		         					'<i class="fa fa-fw fa-ellipsis-v"></i>'+
								'</span>'+
								/* '<ul class="control-list">'+
									'<li class="sendEmail">'+
										'<a href="javascript:;" id="userYangLu" data-href="mail-compose.html"><i class="fa fa-fw fa-pencil-square-o"></i>&nbsp;发送邮件</a>'+
									'</li>'+
								'</ul>'+ */
							'</td>'+
						'</tr>';
				    }
				}
				$("#userList").html(yhList);
				$.each($("#userList tr"), function(i){   
				   if(i>10){
					   $(this).hide();  
				   } 
				}); 
				jiazaiOver();
			});
			$(".surname-filter li").click(function(){
				$("#sousuo").val("");
				$(this).siblings().removeClass("active");
				$(this).addClass("active");				
				$.post("${ctx}/sys/user/selectAddBook",{firstName:$(this).text()},function(result){
					zResult = result;
					var yhList = "";
					for(var i=0;i<result.length;i++){
						var roleName = "";
						if(result[i].roleList!=null){
							for(var j=0;j<result[i].roleList.length;j++){
								roleName+='<span class="user-job">'+
											result[i].roleList[j].name+
										'</span>';
							} 
						}
						var department = "";
						if(result[i].office==null||result[i].office==''||result[i].office.name==null||result[i].office.name==''){
							department = "未填写";
						}else{
							department = result[i].office.name;
						}
						var qq = "";
						var weChat = "";
						if(result[i].userExpand!=null&&result[i].userExpand!=''){
							if(result[i].userExpand.qq==null||result[i].userExpand.qq==""){
								qq = "未填写";
							}else{
								qq = result[i].userExpand.qq;
							}
							if(result[i].userExpand.weChat==null||result[i].userExpand.weChat==""){
								weChat = "未填写";
							}else{
								weChat = result[i].userExpand.weChat;
							}
						}else{
							qq = "未填写";
							weChat = "未填写";
						}
						var photo = result[i].photo==null||result[i].photo==''?'未填写':result[i].photo;
						var name = result[i].name==null||result[i].name==''?'未填写':result[i].name;
						var mobile = result[i].mobile==null||result[i].mobile==''?'未填写':result[i].mobile;
						var addresss = result[i].address==null||result[i].address==''?'未填写':result[i].address;
						var address ="";
						if(addresss.length>10){
							address = addresss.substring(0,10)+"......"; 
						}else{
							address = addresss;
						}
						var email = result[i].email==null||result[i].email==''?'未填写':result[i].email;
						var bir = result[i].bir==null||result[i].bir==''?'未填写':result[i].bir.substring(0,10);
				    	yhList+='<tr>'+
							'<td class="user-baseinfo">'+
					    		'<input type="hidden" class="weixin" value="'+weChat+'">'+
					    		'<input type="hidden" class="birthday" value="'+bir+'">'+
								'<img src="'+$("#btm").val()+photo+'" alt="" class="user-img"> '+
								'<div class="text-info">'+
									'<span class="user-name">'+name+'</span>'+
									'<p>'+
										'<span class="user-department">'+
											department+
										'</span>&nbsp;|&nbsp; '+
										'<span class="user-job">'+
										result[i].userType+
										'</span>'+
										
									'</p>'+
								'</div>'+
							'</td>'+
							'<td>'+
								'<span class="user-cellphone"> '+mobile+'</span>'+
							'</td>'+
							'<td>'+
								'<span class="user-adr" title='+addresss+'>'+address+'</span>'+
							'</td>'+
							'<td>'+
								'<span class="user-QQ">'+
									qq+
								'</span>'+
							'</td>'+
							'<td>'+
								'<span class="user-email">'+
									email+
								'</span>'+
							'</td>'+
							'<td class="user-control text-center">'+
								'<span class="docs-tooltip"  title="" data-original-title="操作">'+
		         					'<i class="fa fa-fw fa-ellipsis-v"></i>'+
								'</span>'+
								'<ul class="control-list">'+
									'<li class="sendEmail">'+
										'<a href="javascript:;" id="userYangLu" data-href="mail-compose.html"><i class="fa fa-fw fa-pencil-square-o"></i>&nbsp;发送邮件</a>'+
									'</li>'+
								'</ul>'+
							'</td>'+
						'</tr>';
				    }
					$("#userList").html(yhList);
					$.each($("#userList tr"), function(i){   
					   if(i>10){
						   $(this).hide();  
					   } 
					}); 
					jiazaiOver();
			  	});
			});
			//操作按钮点击
			/* $(document).on("click",".user-control",function(){
				if($(this).find(".control-list").css("display")=="none"){
					$(this).find(".control-list").css("display","block");
				}else{
					$(this).find(".control-list").css("display","none");
				}
			}) */
		})
	</script>
	<body>
		<input type="hidden" id="btm" value="${fns:getConfig('server_url')}">
		<div class="contacts-env">
			<div class="user-filter row">
				<ul class="surname-filter">
					<li class="active">全部</li>
					<li>A</li>
					<li>B</li>
					<li>C</li>
					<li>D</li>
					<li>E</li>
					<li>F</li>
					<li>G</li>
					<li>H</li>
					<li>I</li>
					<li>J</li>
					<li>K</li>
					<li>L</li>
					<li>M</li>
					<li>N</li>
					<li>O</li>
					<li>P</li>
					<li>Q</li>
					<li>R</li>
					<li>S</li>
					<li>T</li>
					<li>U</li>
					<li>V</li>
					<li>W</li>
					<li>X</li>
					<li>Y</li>
					<li>Z</li>
				</ul>
				<div class="fliter-box">
					<input type="text" name="" id="sousuo" value="" class="form-control fliter-search" />
				</div>
			</div>
			<table class="contacts-list">
				<thead>
					<tr>
						<td style="width:410px;">
							<span class="user-baseinfo">姓名</span>
						</td>
						<td style="width:125px;">
							<span class="user-cellphone">手机</span>
						</td>
						<td>
							<span class="user-adr">地址</span>
						</td>
						<td>
							<span class="user-QQ">QQ</span>
						</td>
						<td>
							<span class="user-email">邮箱</span>
						</td>
						<td class="text-center">
							<span class="user-control">操作</span>
						</td>
					</tr>
				</thead>
				<tbody id="userList">
					<c:forEach items="${userList}" var="list">
					<tr>
						<td class="user-baseinfo">
						<input type="hidden" class="weixin" value="${list.userExpand!=null?list.userExpand.weChat:''}">
					    <input type="hidden" class="birthday" value="${list.bir==null?'':list.bir.substring(0,10)}">
							<img src="${fns:getConfig('server_url')}${list.photo}" alt="" class="user-img">
							<div class="text-info">
								<span class="user-name">${list.name}</span>
								<p>
									<span class="user-department"><c:choose><c:when test="${list.office!=null&&list.office.name!=null}">${list.office.name}</c:when><c:otherwise>未填写</c:otherwise></c:choose></span>&nbsp;|&nbsp;
									<span class="user-job"> 
										<c:choose>
										  <c:when test="${list.userType!=null&&list.userType!=''}">${list.userType}</c:when>
										  <c:otherwise>未填写</c:otherwise>
									    </c:choose>
									</span>
							</div>
						</td>
						<td>
							<span class="user-cellphone">
							<c:choose>
								<c:when test="${list.mobile!=null&&list.mobile!=''}">${list.mobile}</c:when>
								<c:otherwise>未填写</c:otherwise>
							</c:choose>
							</span>
						</td>
						<td>
							<%-- <span class="user-adrr" hidden="hidden">${list.address}</span> --%>
							<span class="user-adr" title="${list.address}">
							<%-- <c:set var="testStr" value="${list.address}" ></c:set> --%>
							<c:choose>
								<c:when test="${list.address!=null&&list.address!=''}">
									<c:choose>
										<c:when test="${fn:length(list.address) > 10}">
											${fn:substring(list.address, 0, 10)}......
											<%-- <c:out value="${fn:substring(testStr, 0, 10)}......"></c:out> --%>
										</c:when>
										<c:otherwise>${list.address}</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>未填写</c:otherwise>
							</c:choose>
							</span>
						</td>
						<td>
							<span class="user-QQ">
							<c:choose>
								<c:when test="${list.userExpand!=null&&list.userExpand.qq!=''}">${list.userExpand.qq}</c:when>
								<c:otherwise>未填写</c:otherwise>
							</c:choose>
							</span>
						</td>
						<td>
							<span class="user-email">
							<c:choose>
								<c:when test="${list.email!=null&&list.email!=''}">${list.email}</c:when>
								<c:otherwise>未填写</c:otherwise>
							</c:choose>
							</span>
						</td>
						<td class="user-control text-center">
							<span class="docs-tooltip"  title="" data-original-title="操作">
             					<i class="fa fa-fw fa-ellipsis-v"></i>
							</span>
							<ul class="control-list">
								<li class="sendEmail">
									<a href="javascript:;" id="userYangLu" data-href="mail-compose.html"><i class="fa fa-fw fa-pencil-square-o"></i>&nbsp;发送邮件</a>
								</li>
							</ul>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pagination">${page}</div>
		</div>
	</body>
	<script src="${ctxStatic}/snowLeopard/js/bootstrap.min.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/TweenMax.min.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/resizeable.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/joinable.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/SnowLeopard-toggles.js"></script>
	<script src="${ctxStatic}/toastr/toastr.min.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/SnowLeopard-custom.js"></script>
	<script src="${ctxStatic}/snowLeopard/js/contacts.js" type="text/javascript" charset="utf-8"></script>
</html>
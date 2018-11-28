$(function() {
	contactsController();
	scrollDownLoad();
})

function calcContactsTrH() {
	var PAGECOUNT = 10;
	var contactsEnvH = $('body').outerHeight();
	var userFilterH = $('.user-filter').outerHeight();
	var footPagiH = $('.foot-pagination').outerHeight();
	var theadH = $('.contacts-list').find('thead').outerHeight();
	var tbodyH = contactsEnvH - userFilterH - footPagiH - theadH;
	var trH = Math.floor(tbodyH / PAGECOUNT)
	$('.contacts-list tbody tr').css('height', trH)
}

function scrollDownLoad() {
	var range = 0; //距下边界长度/单位px  
	var elemt = 70; //插入元素高度/单位px  
	var startCount = 20; //初始化加载条数
	var num = 10; //每次加载个数
	var totalheight = 0;
	var main = $(".contacts-env"); //主体元素  
	var loadingbar = '<div class="row foot-pagination">' +
		'<p class="text-center"><i class="fa fa-spin fa-circle-o-notch fa-fw"></i> 正在加载中,请稍后...</p>' +
		'</div>';
	//
	//初始化加载AJAX
	//
	$(window).scroll(function() {
		var srollPos = $(window).scrollTop(); //滚动条距顶部距离(页面超出窗口的高度)  
		var contentH = $('.user-filter').height() + $('.contacts-list').height(); //页面的文档高度
		totalheight = parseFloat($(window).height()) + parseFloat(srollPos);
		if((contentH - range) <= totalheight) {
			if($('.foot-pagination').length!=0){
				return;
			}else{
				main.append(loadingbar);
				setTimeout("jiazai()",1000);
			}
			//
			//下滑加载AJAX
			//加载完成REMOVE LOADINGBAR
			//
		}
	});
}
function jiazai(){
	var main = $(".contacts-env"); //主体元素  
	var index = $("#userList tr").length-$("#userList tr:hidden").length;
	if(index!=$("#userList tr").length){
		for(var i=index;i<(index+10);i++){
			$("#userList tr").eq(i).show();
		}
		jiazaiOver();
	}else{
		jiazaiOver();
		var loadingbar = '<div class="row foot-pagination">' +
		'<p class="text-center">已到最后一条</p>' +
		'</div>';
		main.append(loadingbar);
	}
}
function jiazaiOver(){
	$(".foot-pagination").remove();
}
function contactsController() {
	$('body').on('click',function() {
		if($('.user-control').find('.control-list').show()) {
			$('.control-list').hide()
		}
	});
	/*$('.user-control').unbind('click').bind('click', function(e) {
		var emailAdr = $(this).parent().find('.user-email').html();
		e.stopPropagation()
		$(this).find('.control-list').show()
		window.parent.platForm.bindInsideAddTab('.sendEmail a',emailAdr)
		$(this).parent().siblings().find('.control-list').hide();
	});*/
	$(document).on("click",".contacts-list tbody .user-baseinfo",function(){
		var trContent = $(this).parent();
		var userImg = trContent.find('.user-img').attr("src");
		var userName = trContent.find('.user-name').html();
		var userJob = trContent.find('.user-job').html();
		var userEmail = trContent.find('.user-email').html();
		var userAdr = trContent.find('.user-adr').html();
		var phoneNum = trContent.find('.user-cellphone').html();
		var userDepartment = trContent.find('.user-department').html();
		var userQQ = trContent.find('.user-QQ').html();
		var weixin = trContent.find('.weixin').val();
		var birthday=trContent.find('.birthday').val();
		if(userQQ==null||userQQ==''){
			userQQ="未填写";
		}
		if(weixin==null||weixin==''){
			weixin="未填写";
		}
		if(birthday==null||birthday==''){
			birthday="未填写";
		}
		if(userAdr==null||userAdr==''){
			userAdr="未填写";
		}
		if(phoneNum==null||phoneNum==''){
			phoneNum="未填写";
		}
		if(userEmail==null||userEmail==''){
			userEmail="未填写";
		}
		var contactContainer = '<div class="userCard">' +
			'<div class="userinfo-left">' +
			'<div class="user-image">' +
			'<img src="'+userImg+'" alt="Member">' +
			'</div>' +
			'<p class="departmentJob">' + userDepartment + '<br/>' + userJob + '</p>' +
			'<p class="userName">' + userName + '</p>' +
			'</div>' +
			'<div class="userinfo-right">' +
			'<div class="user-info">' +
			'<p class="row"><span class="info-title col-sm-3">手机：</span><span class="col-sm-9">' + phoneNum + '</span></p>' +
			'<p class="row"><span class="info-title col-sm-3">邮箱：</span><span class="col-sm-9">' + userEmail + '</span></p>' +
			'<p class="row"><span class="info-title col-sm-3">QQ：</span><span class="col-sm-9">' + userQQ + '</span></p>' +
			'<p class="row"><span class="info-title col-sm-3">微信：</span><span class="col-sm-9">' + weixin + '</span></p>' +
			'<p class="row"><span class="info-title col-sm-3">生日：</span><span class="col-sm-9">' + birthday + '</span></p>' +
			'<p class="row"><span class="info-title col-sm-3">地址：</span><span class="col-sm-9">' + userAdr + '</span></p>' +
			'</div>' +
			'</div>' +
			'</div>';
		window.parent.$.flavr({
			content: contactContainer,
			windowSize:{
				width:550,
				height:350
			},
			closeOverlay: true,
			buttons: {}
		});
	})
}
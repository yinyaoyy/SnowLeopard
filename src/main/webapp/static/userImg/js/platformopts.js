$(function() {
	platOptController();
});
//总控制
function platOptController() {
	var canvasList = $('.canvasList').find('.canvasPic');
	var imageList = $('.imageList').find('.userBackImage');
	//平台LOGO控制器
	$('#userpic').change(function() {
		updatePlatLogo()
	});
	//读取COOKIE判断已有背景类型
	getBjCookie();
	//开放用户注册
	$('#ifregister').bind('click',function(){
		if($('#ifregister').prop('checked')) {
			//开启注册
			//alert('开启注册')
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"is_register",
			        	"value":true,
			        	"logMessage":"开启注册"
			          },
			         success: function (data) {
			        	 toVail("开启注册!","success");
			         }
			    });
		} else {
			//关闭注册
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"is_register",
			        	"value":false,
			        	"logMessage":"关闭注册"
			          },
			         success: function (data) {
			        	 toVail("关闭注册!","success");
			         }
			    });
			//alert('关闭注册')
		}
	});
	//启用国际化语言
	$('#enableLanguage').bind('click',function(){
		if($('#enableLanguage').prop('checked')) {
			//启用
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"enable_international_language",
			        	"value":true,
			        	"logMessage":"启用国际化语言"
			          },
			         success: function (data) {
			        	 toVail("已启用!","success");
			         }
			    });
		} else {
			//关闭注册
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"enable_international_language",
			        	"value":false,
			        	"logMessage":"停用国际化语言"
			          },
			         success: function (data) {
			        	 toVail("已停用!","success");
			         }
			    });
			//alert('关闭注册')
		}
	});
	//静态背景开关
	$('#ifstatics').bind('click', function() {
		if($('#ifstatics').prop('checked')) {
			//开启静态
			canvasList.addClass('disabled');
			imageList.removeClass('disabled');
			$('#ifrandom').prop('checked', false);
			$('#ifrandom').prop('disabled', true);
			$('.chooseImage').show();
			bindimgControl();
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"backgroundType",
			        	"value":"pic",
			        	"type":1,
			        	"logMessage":"开启静态"
			          },
			         success: function (data) {
			         }
			    });
		} else {
			//关闭静态
			$('.addBackImage').remove();
			canvasList.addClass('disabled');
			imageList.addClass('disabled');
			$('#ifrandom').prop('checked', true);
			$('#ifrandom').prop('disabled', false);
			bindcanvasControl('true');
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"backgroundType",
			        	"value":"canvas",
			        	"type":1,
			        	"logMessage":"关闭注册"
			          },
			         success: function (data) {
			         }
			    });
		}
	});
	//随机背景开关控制器
	$('#ifrandom').bind('click', function() {
		if($('#ifrandom').prop('checked')) {
			//开机随机
			canvasList.addClass('disabled');
			imageList.addClass('disabled');
			bindcanvasControl('true');
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"backgroundType",
			        	"value":"true",
			        	"type":2,
			        	"logMessage":"开启背景随机"
			          },
			         success: function (data) {
			         }
			    });
		} else {
			//关闭随机
			$('.addBackImage').remove();
			canvasList.removeClass('disabled');
			imageList.addClass('disabled');
			bindcanvasControl('false');
			 $.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"backgroundType",
			        	"value":"false",
			        	"type":2,
			        	"logMessage":"关闭背景随机"
			          },
			         success: function (data) {
			         }
			    });
		}
	});
	$('#copyrightBtn').bind('click', function() {
		var version=$("#copyright").val();
		 $.ajax({//默认加载内容
		        type: "post",
		        url: jsCtx+"/platform/version",
		        data: {
		        	"key":"companyVersion",
		        	"value":version,
		        	"logMessage":"修改版权"
		          },
		         success: function (data) {
		        	 toVail("版权修改成功!","success");
		        	 $("#footer-copyright", window.parent.document).html(version);
		         }
		    });
	});
	$('#language').bind('change', function() {
		var value=$("#language").val();
		 $.ajax({//默认加载内容
		        type: "post",
		        url: jsCtx+"/platform/version",
		        data: {
		        	"key":"langeageType",
		        	"value":value,
		        	"logMessage":"修改平台语言",
		        	"type":"4"
		          },
		         success: function (data) {	
		        	 if(data==-1){
		        		 toVail("该语言的平台数据还没有完善，请慎重选择!","warning"); 
		        	 }else{
		        		 toVail("平台语言修改成功!","success");
		        	 }
		         }
		    });
	});
	//更改案件涉及人数安全阈值
	$('#thresholdBtn').bind('click', function() {
		var threshold=$("#threshold").val();
		 $.ajax({//默认加载内容
		        type: "post",
		        url: jsCtx+"/platform/version",
		        data: {
		        	"key":"threshold",
		        	"value":threshold,
		        	"logMessage":"修改群体性事件阈值"
		          },
		         success: function (data) {
		        	 toVail("群体性事件阈值修改成功!","success");
		         }
		    });
	});
}
//上传平台Logo
function updatePlatLogo() {
	//检验是否为图像文件  
	var file = document.getElementById("userpic").files[0];
	if(file != '' && file != undefined && file != null) {
		if(!/image\/\w+/.test(file.type)) {
			//alert("看清楚，这个需要图片！");
			toVail("看清楚，这个需要图片！","error");
			return false;
		}
		if(file.size > 10240000) {
			toVail("图片必须小于10M","error");
			/*window.parent.$.flavr({
				content: '图片必须小于10M'
			})*/
			return false
		}
		var reader = new FileReader();
		//将文件以Data URL形式读入页面  
		reader.readAsDataURL(file);
		reader.onload = function(e) {
		var parent = $('.platlogo');
		parent.find('img').attr('src',this.result)
		$.ajax({//默认加载内容
		        type: "post",
		        url: jsCtx+"/platform/logoUplod",
		        data: {
		        	"param":this.result
		          },
		         success: function (data) {
		        	 var srce = window.top.$("#tupianre").attr("src")
		        	 if(srce.indexOf("?")!=-1){
		        		 srce=srce.substring(0,srce.indexOf("?"));
		        	 }
		        	 window.top.$("#tupianre").attr("src",srce+"?"+data);
		        	 window.top.$("#tupianreMin").attr("src",srce+"?"+data);
		        	 toVail("logo修改成功！","success");
		         }
		    });

		}
	}
};

//获取已定义cookie内容
function getBjCookie() {
	var canvasList = $('.canvasList').find('.canvasPic');
	var imageList = $('.imageList').find('.userBackImage');
	//获取已定义COOKIE
	var canvasCookie = top.$.getCookie('backgroundType').split('|');
	if(canvasCookie == '' || canvasCookie == undefined || canvasCookie == null) {
		throw Error('canvasCookie is empty!')
	}
	var backgroundType = canvasCookie[0]; // canvas pic
	var israndom = canvasCookie[1]; //true false
	var styleOrPath = canvasCookie[2]; //canvasID
	var fontText = canvasCookie[3]; //canvasFont text
	//判断cookie backgroundType类型
	if(backgroundType === 'canvas') {
		//判断是否为CANVAS
		$('#ifstatics').prop('checked', false)
		imageList.addClass('disabled');
		$('.canvasList').find('.' + styleOrPath).addClass('checked');
		$('.imageList').find('.checked').removeClass('checked');
		if(israndom === "true") {
			$('#ifrandom').prop('checked', true)
			canvasList.addClass('disabled');
			bindcanvasControl('true');
		} else if(israndom === "false") {
			$('#ifrandom').prop('checked', false)
			canvasList.removeClass('disabled');
			bindcanvasControl('false');
		}
	} else if(backgroundType === 'pic') {
		//判断是否为PIC
		$('#ifstatics').prop('checked', true)
		$('#ifrandom').attr('disabled', 'disabled')
		$('#ifrandom').prop('checked', false);
		canvasList.addClass('disabled');
		imageList.removeClass('disabled');
		$.each(imageList, function(index, val) {
			if($(val).data('styleorpath') === styleOrPath) {
				$(val).addClass('checked');
				$(val).find('.chooseImage').hide();
			} else {
				$(val).removeClass('checked');
			}
		});
		$('.canvasList').find('.checked').removeClass('checked');
		bindimgControl();
	}
};
//开启canvas操作
function bindcanvasControl(ifrandom) {
	//解绑IMAGE操作
	$('.userBackImage').unbind('mouseover')
	$('.userBackImage').unbind('mouseout')
	var canvasPicList = $(".canvasPic");
	if(ifrandom === "true") {
		canvasPicList.unbind('mouseover')
		canvasPicList.unbind('mouseout')
		//默认从canvasStar开始循环新样式
		window.parent.$.setBgCookie('backgroundType', 'canvas', true, 'canvasStar');
	} else if(ifrandom === "false") {
		canvasPicList.bind('mouseover', function() {
			$(this).find('.userCanvasControl').show().stop().animate({
				opacity: 1
			}, 100);
		});
		canvasPicList.bind('mouseout', function() {
			$(this).find('.userCanvasControl').stop().animate({
				opacity: 0
			}, 100).hide();
		});
		$('.chooseCanvas').unbind('click').bind('click', function(e) {
			e.stopPropagation();
			var controlBox = $(this).parent().parent().parent();
			var styleOrPath = controlBox.data('styleorpath');
			controlBox.addClass('checked').siblings().removeClass('checked');
			controlBox.parent().siblings().find('.checked').removeClass('checked');
			$('.imageList').find('.userBackImage').removeClass('checked');
			$.ajax({//默认加载内容
		        type: "post",
		        url: jsCtx+"/platform/version",
		        data: {
		        	"key":"backgroundType",
		        	"value":"canvas|false|"+styleOrPath,
		        	"logMessage":"设置动态背景"
		          },
		         success: function (data) {
		         }
		    });
			 toVail("设置动态背景成功","success");
		     window.parent.$.setBgCookie('backgroundType', 'canvas', false, styleOrPath);
		})
		$('.editText').unbind('click').bind('click', function(e) {
			e.stopPropagation();
			$(this).hide();
			$('.editlabel').show().animate({
				'width': '120px',
				'height': '20px'
			}, 200)
			$('#submitText').unbind('click').bind('click', function(e) {
				e.stopPropagation();
				var controlBox = $(this).parent().parent().parent();
				var parentBox = controlBox.parent();
				//转码中文以存储COOKIE
				var canvastext = encodeURI($('#editarea').val());
				$('.editlabel').hide();
				$('.editText').show();
				controlBox.hide().animate({
					'opacity': 0
				}, 100);
				parentBox.addClass('checked').siblings().removeClass('checked');
				parentBox.parent().siblings().find('.checked').removeClass('checked');
				$('.imageList').find('.userBackImage').removeClass('checked');
				$.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/platform/version",
			        data: {
			        	"key":"backgroundType",
			        	"value":"canvas|false|canvasFont|"+canvastext,
			        	"logMessage":"设置静态背景"
			          },
			         success: function (data) {
			         }
			    });
				toVail("设置动态背景成功","success");
				window.parent.$.setBgCookie('backgroundType', 'canvas', false, 'canvasFont', canvastext);
			})
		})
	}
};
//开启图片操作
function bindimgControl() {
	//解绑CANVAS操作
	$(".canvasPic").unbind('mouseover');
	$(".canvasPic").unbind('mouseout');
	//获取图片数量判断是否可以添加
	var imageCount = $('.imageList').find('.userBackImage').length;
	addImage($('.imageList'), 'nocontrol', imageCount);
	//绑定图片悬停操作
	$('.userBackImage').unbind('mouseover').bind('mouseover', function() {
		$(this).find('.userBackImageControl').show().stop().animate({
			opacity: 1
		}, 100);
	});
	$('.userBackImage').unbind('mouseout').bind('mouseout', function() {
		$(this).find('.userBackImageControl').stop().animate({
			opacity: 0
		}, 100).hide();
	});
	$('.chooseImage').unbind('mousedown').bind('mousedown', function(e) {
		e.stopPropagation();
		var controlBox = $(this).parent().parent().parent();
		var styleOrPath = controlBox.data('styleorpath');
		imageCount = $('.imageList').find('.userBackImage').length;
		$(this).hide()
		controlBox.addClass('checked').siblings().removeClass('checked').find('.chooseImage').show();
		$('.canvasList').find('.canvasPic').removeClass('checked');
		$(this).parent().parent().stop().animate({
			opacity: 0
		}, 100).hide();
		top.$.setBgCookie('backgroundType', 'pic', false, styleOrPath);
		$.ajax({//默认加载内容
		        type: "post",
		        url: jsCtx+"/platform/version",
		        data: {
		        	"key":"backgroundType",
		        	"value":"pic|false|"+styleOrPath,
		        	"logMessage":"设置静态背景"
		          },
		         success: function (data) {
		         }
		    });
		toVail("设置背景图片成功","success");
/*		window.parent.platForm.alertDialog({
			type: 'notice',
			state: 'success',
			text: '设置背景图片成功'
		});
*/	})
	$('.delImage').unbind('mousedown').bind('mousedown', function(e) {
		e.stopPropagation();
		var parentBox = $(this).parent().parent().parent();
		imageCount = $('.imageList').find('.userBackImage').length;
		
		var stylePath = parentBox.data('styleorpath');
		if(parentBox.hasClass('checked')) {
			parentBox.removeClass('checked').next('.userBackImage').addClass('checked');
			var styleOrPath = $('.imageList').find('.checked').data('styleorpath');
			window.parent.$.setBgCookie('backgroundType', 'pic', false, styleOrPath);
			parentBox.remove();
		} else {
			parentBox.remove();
		}
		//删除数据库图片在此添加
		$.ajax({//默认加载内容
	        type: "post",
	        url: jsCtx+"/platform/delImg",
	        data: {
	        	"param":stylePath,
	          },
	         success: function (data) {
	         }
	    });
		$(this).parent().parent().stop().animate({
			opacity: 0
		}, 100).hide();
		toVail("删除背景图片成功","success");
/*		window.parent.platForm.alertDialog({
			type: 'notice',
			state: 'error',
			text: '删除背景图片成功'
		});
*/		addImage($('.imageList'), 'del', imageCount)
		$('#addBackImage').unbind('change').bind('change', function(e) {
			readAsDataURL()
		})
	})
	$('#addBackImage').unbind('change').bind('change', function(e) {
		readAsDataURL()
	})
};
//上传图片
function readAsDataURL() {
	//检验是否为图像文件  
	var imageCount = $('.imageList').find('.userBackImage').length;
	var file = document.getElementById("addBackImage").files[0];
	if(file != '' && file != undefined && file != null) {
		if(!/image\/\w+/.test(file.type)) {
			toVail("看清楚，这个需要图片！","error");
			//alert("看清楚，这个需要图片！");
			return false;
		}
		if(file.size > 5120000) {
			window.parent.$.flavr({
				content: '图片必须小于5M'
			})
			return false
		}
		var reader = new FileReader();
		//将文件以Data URL形式读入页面  
		reader.readAsDataURL(file);
		reader.onload = function(e) {
			var parent = $('.imageList');
			if($('#ifstatics').prop('checked')) {
				var imageCount = $('.imageList').find('.userBackImage').length;
				 $.ajax({//默认加载内容
				        type: "post",
				        url: jsCtx+"/platform/upalodBackImg",
				        data: {
				        	"param":this.result,
					          },
				         success: function (data) {
								addImage(parent, 'add', imageCount,$("#serverUrl").val()+data)
								bindimgControl();
				         }
				    });
			} else {
				return
			}
		}
	}
};
//添加图片
function addImage(parentBox, ifadd, imageCount, targetImageSrc) {
	var imageUpdate = '<label class="addBackImage" for="addBackImage">' +
		'<input type="file" id="addBackImage" style="display:none"/>' +
		'</label>';
	var imgBox = '<div class="userBackImage" data-styleType="statics" data-styleOrPath="' + targetImageSrc + '">' +
		'<img src="' + targetImageSrc + '" />' +
		'<div class="userBackImageControl">' +
		'<div class="form-group">' +
		'<i class="fa-trash red delImage"></i>' +
		'<i class="fa-check green chooseImage"></i>' +
		'</div>' +
		'</div>' +
		'</div>';
	switch(ifadd) {
		case 'add':
			if(imageCount >= 3) {
				$('.addBackImage').remove()
			} else if(imageCount < 3 && imageCount >= 0) {
				if($('.addBackImage').length > 0) {
					$('.addBackImage').before(imgBox);
				} else {
					parentBox.find('.userBackImage').last().after(imageUpdate);
				}
			}
			break;
		case 'del':
			if(imageCount <= 3 && imageCount >= 0) {
				if($('.addBackImage').length > 0) {
					return false
				} else {
					parentBox.find('.userBackImage').last().after(imageUpdate);
				}
			} else {
				return false
			}
			break;
		case 'nocontrol':
			if(imageCount < 3 && imageCount >= 0) {
				if($('.addBackImage').length > 0) {
					console.log('nocontrolhasAdd')
					return false
				} else {
					console.log('nocontrolhasnotAdd')
					parentBox.find('.userBackImage').last().after(imageUpdate);
				}
			} else {
				if($('.addBackImage').length > 0) {
					console.log('nocontrolhasAdd')
					$('.addBackImage').remove()
				} else {
					console.log('nocontrolhasnotAdd')
					return false
				}
			}
			break;
	}
};
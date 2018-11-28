// 
//  common.js
//  <project>
//  
//  Created by LeiwsYang on 2016-12-14.
//  Copyright 2016 LeiwsYang. All rights reserved.
// 

$(function() {
	//Disable right click
	$.disableRightClick();
	//Init Companyinfo
	var info = {
//		imgUrl: ${fns:getConfig('server_url')}${fns:getConfig('logo_url')},
		//companyName: '凯英信业',
		//copyright: 'keytec'
	}
	$.bindCompanyInfo(info);
	$.windowResize();
})
$.extend({
	//获取高精度坐标
	getGeographCoord: function(adrId, type) {
		var userIpW = 'http://chaxun.1616.net/s.php?type=ip&output=json&callback=?&_=' + Math.random();
		var userIp;
		$.getJSON(userIpW, function(data) {
			userIp = data.Ip;
		});
		var IPurl = 'http://api.map.baidu.com/highacciploc/v1'; //baidu highacciploc
		$.ajax({
			type: "GET",
			url: IPurl,
			dataType: "jsonp",
			jsonp: "callback",
			data: {
				qcip: userIp,
				qterm: 'pc',
				ak: 'kbh5MyGSq0gcvPIGSTGG1V3GdHU3UNo2',
				coord: 'bd09ll',
				callback_type: 'jsonp',
				extensions: 3
			},
			async: true,
			success: function(data) {
				var coordInfo;
				if(data.result.error == 302) {
					coordInfo = {
						status: 0,
						errorTxt: "获取地址失败"
					};
				} else {
					var lat = data.content.location.lat;
					var lng = data.content.location.lng;
					coordInfo = {
						status: 1,
						po: {
							lat: lat,
							lng: lng
						},
						surround: data.content.pois
					}
				}
				$('#' + adrId).renderSurroundList(coordInfo, type)
			}
		})
	},
	//绑定部门信息
	bindCompanyInfo: function(companyInfo) {
		var defaults = {
//			imgUrl: 'images/keytec-logo.png',
			companyName: '',
			//copyright: '&copy; 2016 KEYTEC 版权所有 北京凯英信业科技股份有限部门'
		}
		var $mes = $.extend(defaults, companyInfo);
		var $mainInfo = $('.page-container');
		var $loginInfo = $('.login-container').find('.login-header');
		//mainPage
		$mainInfo.find('.logo img').attr('src', $mes.imgUrl);
		if($mes.companyName == "" || $mes.companyName == undefined || $mes.companyName == null) {
			$mainInfo.find('.copyName-details').remove()
		} else {
			$mainInfo.find('.copyName-details h3').html($mes.companyName);
		}
		$mainInfo.find('.footer-text').html($mes.copyright);
		//loginPage
		//$loginInfo.find('.logo img').attr('src', $mes.imgUrl);
		$loginInfo.find('.copyName').html($mes.companyName);
	},
	//禁用右键
	disableRightClick: function() {
		$(document).bind("contextmenu", function(e) {
			return false;
		});
	},
	//显示Loading
	show_loading_bar: function(options) {
		var defaults = {
			pct: 0,
			delay: 1.3,
			wait: 0,
			before: function() {},
			finish: function() {},
			resetOnEnd: true
		};

		if(typeof options == 'object')
			defaults = jQuery.extend(defaults, options);
		else
		if(typeof options == 'number')
			defaults.pct = options;

		if(defaults.pct > 100)
			defaults.pct = 100;
		else
		if(defaults.pct < 0)
			defaults.pct = 0;

		var $ = jQuery,
			$loading_bar = $(".xenon-loading-bar");

		if($loading_bar.length == 0) {
			$loading_bar = $('<div class="xenon-loading-bar progress-is-hidden"><span data-pct="0"></span></div>');
			public_vars.$body.append($loading_bar);
		}

		var $pct = $loading_bar.find('span'),
			current_pct = $pct.data('pct'),
			is_regress = current_pct > defaults.pct;

		defaults.before(current_pct);

		TweenMax.to($pct, defaults.delay, {
			css: {
				width: defaults.pct + '%'
			},
			delay: defaults.wait,
			ease: is_regress ? Expo.easeOut : Expo.easeIn,
			onStart: function() {
				$loading_bar.removeClass('progress-is-hidden');
			},
			onComplete: function() {
				var pct = $pct.data('pct');

				if(pct == 100 && defaults.resetOnEnd) {
					hide_loading_bar();
				}

				defaults.finish(pct);
			},
			onUpdate: function() {
				$pct.data('pct', parseInt($pct.get(0).style.width, 10));
			}
		});
	},
	//隐藏Loading
	hide_loading_bar: function() {
		var $ = jQuery,
			$loading_bar = $(".xenon-loading-bar"),
			$pct = $loading_bar.find('span');

		$loading_bar.addClass('progress-is-hidden');
		$pct.width(0).data('pct', 0);
	},
	//序列化参数
	serializeToObj: function(data) {
		var result = new Object();
		var strs = data.split('&');
		for(var i = 0; i < strs.length; i++) {
			result[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
		}
		return result;
	},
	//动态适应浏览器宽高
	windowResize: function(issub) {
		var NAVBARPADDING = 18;
		if(typeof(issub) == "undefined"){//如果没有就是父页面自适应
		$('.iframe-content .tab-content .tab-pane').css({
			height: window.document.body.offsetHeight - $('.navbar').outerHeight() - $('.main-footer').outerHeight() - $('.iframe-content .nav-tabs').outerHeight() - NAVBARPADDING + 'px'
		});
		}else{//如果有就是子页面调整父页面的自适应
			$('.iframe-content .tab-content .tab-pane', window.parent.document).css({
				height: window.parent.document.body.offsetHeight - $('.navbar', window.parent.document).outerHeight() - $('.main-footer', window.parent.document).outerHeight() - $('.iframe-content .nav-tabs', window.parent.document).outerHeight() - NAVBARPADDING + 'px'
			});
		}
		$(window).resize(function() {
			$('.iframe-content .tab-content .tab-pane').css({
				height: window.document.body.offsetHeight - $('.navbar').outerHeight() - $('.main-footer').outerHeight() - $('.iframe-content .nav-tabs').outerHeight() - NAVBARPADDING + 'px'
			});
		});
		platForm.calcRemainWidth();
	},
	//设置背景缓存
	setBgCookie: function(bgType, iscavans, israndom, style, fonttext) {
		var d = new Date();
		document.cookie = bgType + "=" + iscavans + "|" + israndom + "|" + style + "|" + fonttext+";path=/;";
	},
	//获取缓存
	getCookie: function(cname) {
		var name = cname + "=";
		var ca = document.cookie.split(';');
		for(var i = 0; i < ca.length; i++) {
			var c = ca[i].trim();
			if(c.indexOf(name) == 0) return c.substring(name.length, c.length);
		}
		return "";
	},
	//初始化生成用户名称头像
	createUserNameImage: function(userName) { //初始化生成用户名称头像
		//验证文字长度
		var textlen = 0;
		for(var i = 0; i < userName.length; i++) {
			if(userName.charCodeAt(i) > 127 || userName.charCodeAt(i) == 94) {
				textlen += 2;
			} else {
				textlen++;
			}
		}
		//生成CANVAS
		var canvas = '<canvas width="125px" height="125px" id="userNameImage" style="display: none"></canvas>'
		$('body').append(canvas);
		var cvs = document.getElementById('userNameImage');
		var ctx = cvs.getContext('2d');
		//绘制背景色
		var colorList = ['#F32951', '#FE6B18', '#FAC527', '#4CD28E', '#000000', '#2F90E8', '#9F1BD2']
		ctx.fillStyle = colorList[Math.floor(Math.random() * colorList.length)]
		ctx.fillRect(0, 0, 125, 125);
		//填充文字
		ctx.fillStyle = '#fff';
		if(textlen > 4) {
			if(userName.charCodeAt(0) > 127 || userName.charCodeAt(0) == 94) {
				userName = userName.substr(1, 2)
				ctx.font = '40px Microsoft Yahei'
				ctx.fillText(userName, 23, 78);
			} else {
				userName = userName.substr(0, 2)
				ctx.font = '50px Microsoft Yahei'
				ctx.fillText(userName, 33, 78);
			}
		} else if(textlen <= 4 && textlen > 2) {
			if(userName.charCodeAt(0) > 127 || userName.charCodeAt(0) == 94) {
				userName = userName
				ctx.font = '40px Microsoft Yahei'
				ctx.fillText(userName, 23, 78);
			} else {
				userName = userName.substr(0, 2)
				ctx.font = '50px Microsoft Yahei'
				ctx.fillText(userName, 33, 78);
			}
		} else if(textlen <= 2 && textlen > 1) {
			if(userName.charCodeAt(0) > 127 || userName.charCodeAt(0) == 94) {
				userName = userName
				ctx.font = '40px Microsoft Yahei'
				ctx.fillText(userName, 43, 78);
			} else {
				userName = userName.substr(0, 2)
				ctx.font = '50px Microsoft Yahei'
				ctx.fillText(userName, 33, 78);
			}
		} else if(textlen == 1) {
			userName = userName.substr(0, 2)
			ctx.font = '50px Microsoft Yahei'
			ctx.fillText(userName, 43, 78);
		}
		//返回图片文件
		var userNameImage = $('#userNameImage')[0];
		var image = new Image();
		image.src = userNameImage.toDataURL("image/png");
		return image
	}
});
$.fn.extend({
	//渲染周边建筑名称
	renderSurroundList: function(coordInfo, type) {
		$(this).children().remove();
		var outbox = $(this);
		if(coordInfo.status == 0) {
			outbox.attr('disabled', 'disabled').css('color', 'red').append('<option>' + coordInfo.errorTxt + '</option>')
		} else {
			$.each(coordInfo.surround, function(index, val) {
				if(type == "select") {
					var inner = '<option value="' + val.name + '">' + val.name + '</option>';
				} else if(type == 'li') {
					var inner = '<li value="' + val.name + '">' + val.name + '</li>';
				}
				outbox.append(inner);
			});
		}
	},
	//绑定Iframe关闭按钮
	bindIframeClose: function() {
		$(this).bind('click', function() {
			var $li = $(this).parent();
			var index = $li.index();
			var totalLiLength = $li.parent().find('li').length - 1;
			var iframePane = $li.parent().siblings('.tab-content').find('.tab-pane');
			if($li.hasClass('active')) {
				if(index == totalLiLength) {
					$li.prev().addClass('active');
					iframePane.eq(index - 1).addClass('active')
				}
				iframePane.eq(index - 1).addClass('active');
				iframePane.eq(index).remove();
				$li.parent().find('li').eq(index - 1).addClass('active');
				$li.remove();

			} else {
				iframePane.eq(index).remove()
				$li.remove();
			}
		})
	},
	//绑定邮件菜单按钮点击事件
	bindIframeRefreshClose: function(typeFuc) {
		console.log('bind')
		$(this).bind('click', function() {
			var type = typeFuc || $(this).data('button');
			var $li = $(this).parent().parent().parent();
			var index = $li.index();
			var totalLiLength = $li.parent().find('li').length - 1;
			var iframePane = $li.parent().siblings('.tab-content').find('.tab-pane');
			if(type == 'close') {
				if($li.hasClass('active')) {
					if(index == totalLiLength) {
						$li.prev().addClass('active');
						iframePane.eq(index - 1).addClass('active')
					}
					iframePane.eq(index - 1).addClass('active');
					iframePane.eq(index).remove();
					$li.parent().find('li').eq(index - 1).addClass('active');
					$li.remove();
				} else {
					if(index == 0) {
						return;
					}
					iframePane.eq(index).remove()
					$li.remove();
				}
			} else if(type == 'closeOther') {
				$li.addClass('active').siblings().not(':first-child').remove();
				iframePane.eq(index).addClass('active').siblings().not(':first-child').remove();
			} else if(type == 'closeLeft') {
				if($li.prev().index() == 0) {
					return
				}
				if($li.prev().hasClass('active')) {
					$li.addClass('active')
					iframePane.eq(index).addClass('active')
				}
				$li.prev().remove();
				iframePane.eq(index).prev().remove();
			} else if(type == 'closeRight') {
				if($li.next().hasClass('active')) {
					$li.addClass('active')
					iframePane.eq(index).addClass('active')
				}
				$li.next().remove();
				iframePane.eq(index).next().remove();
			} else if(type == 'refresh') {
				iframePane.eq(index).find('iframe')[0].contentWindow.location.reload(true)
			} else if(type == 'closeAll') {
				$li.parent().find('li').eq(0).addClass('active').siblings().remove()
				iframePane.eq(0).addClass('active').siblings().remove()
			}
			$(this).parent().parent('.rightClick').remove();
		})
	},
	//绑定邮件菜单
	bindRightClick: function(el) {
		$el = el || $(this);
		$el.bind('mousedown', function(e) {
			e.stopPropagation();
			if(e.button == 2) {
				$(this).find('.rightClick').remove();
				var  content="";
				var $rightClick =
					$('<div class="rightClick">' +
					'<ul class="buttonList">' +
					'<li data-button="refresh">刷新</li>'+
				 	'<li data-button="close">关闭当前标签</li>'+
				 	'<li data-button="closeOther">关闭其他标签</li>'+
				 	'<li data-button="closeLeft">关闭左侧标签</li>'+
				 	'<li data-button="closeRight">关闭右侧标签</li>'+
				 	'<li data-button="closeAll">关闭全部标签</li>'+
					'</ul>' +
					'</div>');
				$rightClick.find('li').bindIframeRefreshClose()
				$(this).append($rightClick)
				$(this).css('position', 'relative');
				$(this).find('.rightClick').show().css({
					top: e.offsetY,
					left: e.offsetX
				});
			} else {
				return false
			}
		})
		$(document).click(function() {
			$('.rightClick').remove();
		})
	}
})
var platForm = {
	//绑定平台主菜单点击事件
	bindMainMenuClick: function(el) {
		$(el).bind('click', function(e) {
			e.stopPropagation();
			var pageUrl = $(this).data('href'); //链接
			var titleHtml = $(this).find('.title').html(); //标题
			var aId = $(this).attr('id'); //id
			if(pageUrl == 'alertDialog') {
				//AJAX请求人员角色信息
				var roleOption = '<select class="form-control text-center">' +
					'<option>' + '系统管理员' + '</option>' +
					'<option>' + '普通员工' + '</option>' +
					'</select>';
				platForm.alertDialog({
					title: '切换身份',
					type: 'alert',
					state: 'comfirm',
					text: roleOption,
					comfirmCallback: function() {},
					closeCallback: function() {}
				})
			} else {
				if(aId != '' && aId != undefined && aId != null) {
					var contentList = $('.iframe-content .nav-tabs').find('li[data-tag="navTab"]');
					var remainWidth = platForm.calcRemainWidth();
					var nowWidth = 0;
					for(var i = 0; i < contentList.length; i++) {
						nowWidth += $(contentList[i]).width();
					}
					if(remainWidth <= 100) {
						platForm.alertDialog({
							type: 'notice',
							state: 'error',
							text: '开启标签已达上限，请关闭后，重新打开！'
						})
						return;
					}
					for(var i = 0; i < contentList.length; i++) {
						if("#tab_" + aId == $(contentList[i]).find('a').attr('href')) {
							$(contentList[i]).addClass('active').siblings().removeClass('active');
							$('.tab-content').find('.tab-pane').eq(i).find('iframe')[0].contentWindow.location.reload(true)
							$('.tab-content').find('.tab-pane').eq(i).addClass('active').siblings().removeClass('active')
							return false;
						}
					}
					var $li = $('<li data-tag="navTab">' +
						'<a href="#tab_' + aId + '" data-toggle="tab">' +
						'<i class="hidden-xs" title="' + titleHtml + '">' + titleHtml + '</i>' +
						'</a>' +
						'<i class="close fa fa-times fa-fw"></i>' +
						'</li>');
					var $iframeContent = $('<div class="tab-pane" id="tab_' + aId + '">' +
						'<iframe src="' + pageUrl + '" id="iframe_' + aId + '"name="iframe_'+aId+'" appid="' + aId + '" width="100%" height="100%" style="border: none;"></iframe>' +
						'</div>');
					var loadingOverlay = '<div class="page-loading-overlay"><div class="loader-2"></div></div>'
					if(pageUrl != '' && pageUrl != undefined && pageUrl != null) {
						$li.bindRightClick()
						$li.appendTo($('.iframe-content .nav-tabs')).addClass('active').siblings().removeClass('active');
						$iframeContent.appendTo($('.iframe-content .tab-content')).addClass('active').siblings().removeClass('active');
					}
					$li.find('.close').bindIframeClose();
					$.windowResize();
				}
			}
			if($('.rightClick').show()){
				$('.rightClick').hide()
			}
		});
	},
	//计算TabNav剩余宽度
	calcRemainWidth: function() {
		var BSPADDING = 30;
		var MINLIWIDTH = 120;
		var nowWidth = 0;
		var nowLi = $('.nav-tabs').find('li[data-tag="navTab"]')
		for(var i = 0; i < nowLi.length; i++) {
			var liWidth = $(nowLi[i]).width()
			nowWidth += liWidth
		}
		var totalWidth = $('.tab-content').width() - BSPADDING;
		var remainWidth = totalWidth - nowWidth - MINLIWIDTH;
		return remainWidth;
	},
	//弹出层及通知框体
	alertDialog: function(ownOpt) {
		var defaultOpt = {
			title: '', //title str
			type: 'notice', //notice alert
			state: 'success', //notice: success/warning/error  alert:alert/comfirm
			text: '成功', //提示信息
			comfirmBtn:'确定',
			closeBtn:'取消',
			comfirmCallback: function() {}, //alert:comfirm确定按钮操作
			closeCallback: function() {} //alert:alert/comfirm 关闭操作
		}
		var options = $.extend(defaultOpt, ownOpt);
		if(options.type === 'notice') {
			showNotice(options.state, options.text)
		} else if(options.type === 'alert') {
			if(options.state === 'alert') {
				new $.flavr({
					title: options.title,
					content: options.text,
					buttons: {
						onClose: {
							text: options.comfirmBtn,
							style: "default",
							action: function() {
								options.closeCallback()
							}
						}
					}
				})
			} else if(options.state === 'comfirm') {
				new $.flavr({
					title: options.title,
					content: options.text,
					dialog: 'confirm',
					buttons: {
						onConfirm: {
							text: options.comfirmBtn,
							style: 'default',
							action: function() {
								options.comfirmCallback()
							}
						},
						onCancel: {
							text: options.closeBtn,
							style: 'danger',
							action: function() {
								options.closeCallback()
							}
						}
					}
				});
			} else {
				throw new Error('This state "' + options.state + '" is not belong to alertDialog')
			}

		} else {
			throw new Error('alertDialog type is empty!')
		}

		function showNotice(state, text) {
			toastr.options = {
				"closeButton": true, //是否显示关闭按钮
				"debug": false, //是否使用debug模式
				"positionClass": "toast-top-right", //弹出窗的位置
				"showDuration": "300", //显示的动画时间
				"hideDuration": "1000", //消失的动画时间
				"timeOut": "5000", //展现时间
				"extendedTimeOut": "1000", //加长展示时间
				"showEasing": "swing", //显示时的动画缓冲方式
				"hideEasing": "linear", //消失时的动画缓冲方式
				"showMethod": "fadeIn", //显示时的动画方式
				"hideMethod": "fadeOut" //消失时的动画方式
			};
			toastr[state](text, '提示');
		}
	},
	//绑定Iframe页内按钮方法（暂时适用邮件跳转）
	bindInsideAddTab: function(el) {
		var iframeId = $('body .iframe-content .tab-content').find('.active').find('iframe').attr('id');
		$(el).unbind('click').bind('click', function(e) {
			e.stopPropagation();
			var pageUrl = $(this).data('href'); //链接
			var titleHtml = $(this).data('title'); //标题
			var aId = $(this).attr('id'); //id
			if(aId != '' && aId != undefined && aId != null) {
				var remainWidth = window.parent.platForm.calcRemainWidth();
				var contentList = $('.iframe-content .nav-tabs', window.parent.document).find('li');
				var nowWidth = 0;
				for(var i = 0; i < contentList.length; i++) {
					nowWidth += $(contentList[i]).width();
				}
				if(remainWidth <= 100) {
					platForm.alertDialog({
						type: 'notice',
						state: 'error',
						text: '开启标签已达上限，请关闭后，重新打开！'
					})
					return;
				}
				for(var i = 0; i < contentList.length; i++) {
					if("#tab_" + aId == $(contentList[i]).find('a').attr('href')) {
						$(contentList[i]).addClass('active').siblings().removeClass('active');
						$('.tab-content', window.parent.document).find('.tab-pane').eq(i).addClass('active').siblings().removeClass('active')
						return false;
					}
				}
				var $li = $('<li data-tag="navTab">' +
					'<a href="#tab_' + aId + '" data-toggle="tab">' +
					'<i class="hidden-xs" title="'+titleHtml+'">' + titleHtml + '</i>' +
					'</a>' +
					'<i class="close fa-close fa-times fa-fw"></i>' +
					'</li>');
				var $iframeContent = $('<div class="tab-pane" id="tab_' + aId + '">' +
					'<iframe src="' + pageUrl + '" id="iframe_' + aId + '"name="iframe_'+aId+'" width="100%" height="100%" style="border: none;"></iframe>' +
					'</div>');
				var loadingOverlay = '<div class="page-loading-overlay"><div class="loader-2"></div></div>'
				if(pageUrl != '' && pageUrl != undefined && pageUrl != null) {
					$li.appendTo($('.iframe-content .nav-tabs', window.parent.document)).addClass('active').siblings().removeClass('active');
					$iframeContent.appendTo($('.iframe-content .tab-content', window.parent.document)).addClass('active').siblings().removeClass('active');
					$li.bindRightClick();
				}
				$li.find('.close').bindIframeClose();
				$.windowResize('issub');
				$(this).parents('.control-list').hide()
			}
		})
	},
	//打开方式
    openDisplayFormat: function (url, model, title, id, width, height, openerid,isAppendParams) {
        if (!url || url.toString().length == 0) {
            return;
        }
        if (!id) {
            id = platForm.newid();
        }
        if (width == 0) width = undefined;
        if (height == 0) height = undefined;
        if (isAppendParams) {
            url += url.indexOf('?') >= 0 ? "&appid=" + id : "?appid=" + id;
        }
        url = url.substr(0, 1) == "/" ? url : rootdir + url;
        switch (parseInt(model)) {
            //tab
            case 0:
                platForm.addTabs({ id: "tab_" + id.replaceAll('-', ''), title: title, src: url });
                break;
            //弹出层可拖动
            case 1:
                platForm.openModel({
                    id: "window_" + id.replaceAll('-', ''),
                    title: title,
                    url: url,
                    width: width || 800,
                    height: height || 460,
                    resizewindow: true,
                    canMove: true,
                    openerid: openerid
                });
                break;
            //弹出层不可拖动
            case 2:
                platForm.openModel({
                    id: "window_" + id.replaceAll('-', ''),
                    title: title,
                    url: url,
                    width: width || 800,
                    height: height || 460,
                    resizewindow: false,
                    canMove: false,
                    openerid: openerid
                });
                break;
            case 3:
                window.open(url);
                break;
            case 4:
                platForm.openModel({
                    id: "window_" + id.replaceAll('-', ''),
                    title: title,
                    url: url,
                    width: width || 800,
                    height: height || 460,
                    autoclose:true,
                    resizewindow: false,
                    canMove: false,
                    openerid: openerid
                });
                break;
        }
    },
	//获取GUID
	newid: function(isMiddline) {
		var guid = "";
		isMiddline = isMiddline == undefined ? true : isMiddline;
		for(var i = 1; i <= 32; i++) {
			var n = Math.floor(Math.random() * 16.0).toString(16);
			guid += n;
			if(isMiddline && (i == 8 || i == 12 || i == 16 || i == 20)) {
				guid += "-";
			}
		}
		return guid;
	}
}
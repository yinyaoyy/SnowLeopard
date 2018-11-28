"use strict";
$(document).ready(function () {
  var validatorL;
  var validatorR;
  var currentLanguageArr;
  //当前页的国际化数据	
  $.ajax({
	    type: "post",
	    url: jsCtx + "/language/sysMunlLang/getCurrentPageList",
	    async: false,
	    data: {
	      "languageAscription": "/login"
	    },
	    success: function (data) {
	      currentLanguageArr = data;
	    }
  });
  pageValue();
  //获取默认语言
  var langList = $('#langList').find('li');
  for (let i = 0; i < langList.length; i++) {
    if ($(langList[i]).attr('selected') === "selected") {
      $('.curLang').val($(langList[i]).data('value')).html($(langList[i]).text())
    }
  }
  languageSelectChange($('.curLang').val());
  //增加验证用户名方法
  jQuery.validator.addMethod('checkUserName', function (value, element) {
    return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
  }, currentLanguageArr.login_username_prompt_1);
  //焦点状态
  $(".login-form .form-group:has(label)").each(function (i, el) {
    var $this = $(el),
      $label = $this.find('label'),
      $input = $this.find('.form-control');

    $input.on('focus', function () {
      $this.addClass('is-focused');
    });

    $input.on('keydown', function () {
      $this.addClass('is-focused');
    });

    $input.on('blur', function () {
      $this.removeClass('is-focused');

      if ($input.val().trim().length > 0) {
        $this.addClass('is-focused');
      }
    });

    $label.on('click', function () {
      $input.focus();
    });
    if ($input.val() != undefined && $input.val() != null && $input.val() != '') {
      if ($input.val().trim().length > 0) {
        $this.addClass('is-focused');
      }
    }
  });
  //翻转登录注册
  $(".changeLR").on('click', function () {
    $('.flippedBox').toggleClass('flipped');
    if ($(this).text() == currentLanguageArr.new_register_button) {
      $(this).text(currentLanguageArr.return_login)
    } else {
      $(this).text(currentLanguageArr.new_register_button)
    }
  });
  //显示登录框
  setTimeout(function () {
    $(".fade-in-effect").addClass('in');
  }, 1);
  //登陆时，用户名只要变化，重置登录按钮
  $("#uesername").keydown(function () {
    $('#loginBtn').attr('disabled', false).removeClass('btn-success').removeClass('btn-danger').html("<i class=\"fa fa-lock fa-fw\"></i>" + currentLanguageArr.login_button + "")
  });
  //登陆时，密码只要变化，重置登录按钮
  $("#password").keydown(function () {
    $('#loginBtn').attr('disabled', false).removeClass('btn-success').removeClass('btn-danger').html("<i class=\"fa fa-lock fa-fw\"></i>" + currentLanguageArr.login_button + "")
  });
  //登陆时，验证码只要变化，重置登录按钮
  $("#validateCode").keydown(function () {
    $('#loginBtn').attr('disabled', false).removeClass('btn-success').removeClass('btn-danger').html("<i class=\"fa fa-lock fa-fw\"></i>" + currentLanguageArr.login_button + "")
  });
  //注册时，用户名只要变化，重置注册按钮
  $("#newUserName").keydown(function () {
    $('#registerNew').attr('disabled', false).removeClass('btn-success').removeClass('btn-danger').html('<i class="icon-check fa-fw"></i>' + currentLanguageArr.register_button + '')
  });
  function isOtherUser() {
    var returnData = "";
    $.ajax({//默认加载内容
      type: "post",
      url: jsCtx + "/isOtherUser",
      async: false,
      data: {
      },
      success: function (data) {
        returnData = data;
      }
    });
    return returnData;
  }
  function clearnuser(type) {
    platForm.alertDialog({
      title: currentLanguageArr.login_relogin_prompt_title,
      type: 'alert',
      state: 'comfirm',
      text: currentLanguageArr.login_relogin_prompt_con,
      comfirmBtn:currentLanguageArr.login_force_login,
      closeBtn:currentLanguageArr.login_force_close,
      comfirmCallback: function (item) {
        $.ajax({//默认加载内容
          type: "post",
          url: jsCtx + "/clearnuser",
          async: false,
          data: {
            "type": 1//0 清除自己的session(不强制登录)  1 清除别人的session(强制登录) 
          },
          success: function () {
            window.location.href = jsCtx + "/newIndex";
          }
        });
      },
      closeCallback: function () {
    	  window.location.href = jsCtx + "/logout";
       /* $.ajax({//默认加载内容
          type: "post",
          url: jsCtx + "/clearnuser",
          async: false,
          data: {
            "type": 0//0 清除自己的session(不强制登录)  1 清除别人的session(强制登录) 
          },
          success: function () {
            window.location.href = jsCtx + "/login";
          }
        });*/
      }
    });

  }
  //初始化验证
  function initValidate() {
    validatorL = $("#loginForm").validate({
      rules: {
        username: {
          required: true,
          checkUserName: true,
          rangelength: [2, 20]
        },
        password: {
          required: true,
          rangelength: [3, 20]
        },
        validateCodes: {
          required: true
        }
      },
      messages: {
        username: {
          required: currentLanguageArr.login_username_prompt_2,
          checkUserName: currentLanguageArr.login_username_prompt_1,
          rangelength: currentLanguageArr.login_username_prompt_3
        },
        password: {
          required: currentLanguageArr.login_password_prompt_1,
          rangelength: currentLanguageArr.login_password_prompt_2
        },
        validateCodes: {
          required: currentLanguageArr.login_validateCode_prompt_1
        }
      },
      submitHandler: function (form) {
        $.ajax({//默认加载内容
          type: "post",
          url: jsCtx + "/login",
          data: {
            "username": $("#username").val(),
            "password": hex_md5($("#password").val()+$("#username").val()),
            "validateCode": $('#validateCodes').val(),
            "langCode": $('.curLang').val()
          },
          success: function (data) {
            try {
              var pa = JSON.parse(data);
              if (pa.code == "-1") {
                if (pa.validateCodeLogin) {
                  if (pa.massage == "-1") {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_username_prompt_4);
                  } else if (pa.massage == "-2") {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_username_prompt_5);
                  } else if (pa.massage == "-3") {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_username_prompt_6);
                  } else {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + pa.massage)
                  }
                  $('.validateCode').show();
                }
                else {
                  if (pa.massage == "-1") {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_username_prompt_4);
                  } else if (pa.massage == "-2") {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_username_prompt_5);
                  } else if (pa.massage == "-3") {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_username_prompt_6);
                  } else {
                    $('#loginBtn').attr('disabled', false).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-lock fa-fw"></i>' + pa.massage)
                  }
                }
              } else {
                if (isOtherUser()) {//已经有用户登录
                  clearnuser();
                } else {
                  window.location.href = jsCtx + "/newIndex";
                }
              }
            } catch (e) {
              if (isOtherUser()) {//已经有用户登录
                clearnuser();
              } else {
                window.location.href = jsCtx + "/newIndex";
              }
            }
          }
        });
      }
    });
    validatorR = $("#register").validate({
        rules: {
          newUserName: {
            required: true,
            checkUserName: true,
            rangelength: [2, 20]
          },
          newPasswd: {
            required: true,
            rangelength: [3, 20]
          },
          recheckPasswd: {
            required: true,
            rangelength: [3, 20],
            equalTo: "#newPasswd"
          }
        },
        messages: {
          newUserName: {
            required: currentLanguageArr.login_username_prompt_2,
            checkUserName: currentLanguageArr.login_username_prompt_1,
            rangelength: currentLanguageArr.login_username_prompt_3
          },
          newPasswd: {
            required: currentLanguageArr.login_password_prompt_1,
            rangelength: currentLanguageArr.login_password_prompt_2
          },
          recheckPasswd: {
            required: currentLanguageArr.login_repaet_password_prompt_1,
            rangelength: currentLanguageArr.login_password_prompt_2,
            equalTo: currentLanguageArr.login_repaet_password_prompt_2
          }
        },
        submitHandler: function (form) {
          var realname = $("#realname").val();
          var papernum = $("#papernum").val();
          var newUserName = $("#newUserName").val();
          var newPasswd = $("#newPasswd").val();
          var recheckPasswd = $("#recheckPasswd").val();
          var checkcode = $("#checkcode").val();
          if(!checkcode){
              $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + '短信验证码不得为空');
          }
          if(!(papernum.length == 15 || papernum.length == 18)){
              $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + '身份证号码不合法');
              return;
          }
          if(!newPasswd){
              $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + '密码不得为空');
              return;

          }
          if(newPasswd != recheckPasswd){
              $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + '两侧密码输入不一致');
              return;
          }

          $("#registerNew").html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_register_prompt_1 + '...').attr("disabled", "true");
          $.ajax({
            url: jsCtx + "/register",
            type: "POST",
            data: {
                "realname":realname,
                "papernum":papernum,
                "checkcode":checkcode,
              "newUserName": newUserName,
              "newPasswd": newPasswd,
            },
            success: function (data) {
              if (data == -2) {
                $('#btnImportSubmit').attr('disabled', true).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-close fa-fw"></i>' + currentLanguageArr.login_register_prompt_2 + '')
                $('#checkcode').on('focus', function () {
                  $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + currentLanguageArr.register_button + '')
                })
              }else if (data == -3) {
                $('#btnImportSubmit').attr('disabled', true).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-close fa-fw"></i>' + currentLanguageArr.login_register_prompt_2 + '')
                $('#papernum').on('focus', function () {
                  $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + currentLanguageArr.register_button + '')
                })
              }else if (data == -1) {
                $('#btnImportSubmit').attr('disabled', true).removeClass('btn-success').addClass('btn-danger').html('<i class="fa fa-close fa-fw"></i>' + currentLanguageArr.login_register_prompt_2 + '')
                $('#newUserName').on('focus', function () {
                  $('#btnImportSubmit').attr('disabled', false).removeClass('btn-danger').addClass('btn-primary').html('<i class="fa fa-close fa-fw"></i>' + currentLanguageArr.register_button + '')
                })
              } else {
                $(".changeLR").unbind("click");
                InterValObj = window.setInterval(SetRemainTime, 1000);
              }
            }
          });
        }
    });
  }
  
  //timer处理函数
  var InterValObj; //timer变量，控制时间
  var count = 3; //间隔函数，1秒执行
  var curCount;//当前剩余秒数
  function SetRemainTime() {
    if (count == 0) {
      window.clearInterval(InterValObj);//停止计时器   
      $(".changeLR").on('click', function () {
        $('.flippedBox').toggleClass('flipped');
        if ($(this).text() == currentLanguageArr.new_register_button) {
          $(this).text(currentLanguageArr.return_login)
        } else {
          $(this).text(currentLanguageArr.new_register_button)
        }
      });
      $('.changeLR').text(currentLanguageArr.new_register_button);
      $('.flippedBox').toggleClass('flipped');
      $("#result").html("");
      $("#newUserName").val("");
      $("#newPasswd").val("");
      $("#recheckPasswd").val("");
      $('#btnImportSubmit').attr('disabled', false).removeClass('btn-success').removeClass('btn-warning').addClass('btn-primary').html('<i class="fa-check"></i>' + currentLanguageArr.register_button + '')
      count = 3;
    }
    else {
      count--;
      $("#btnImportSubmit").attr({ 'disabled': true, 'style': 'opacity:1' }).removeClass('btn-primary').removeClass('btn-warning').addClass('btn-success').html('<i class="fa-check"></i>' + currentLanguageArr.login_register_prompt_3 + count + currentLanguageArr.login_register_prompt_4)
    }
  };
  //背景图
  var cookie = $.getCookie('backgroundType');
  if (cookie != null && cookie != undefined
    && cookie != '') {
  } else {
    cookie = $("#backgroundType").val();
  }
  var cookieList = cookie.split("|");
  //console.log(typeof (decodeURI(cookieList[3])))
  $.loadBackground({
    iscavans: cookieList[0],
    israndom: cookieList[1],
    styleOrPath: cookieList[2],
    canvasText: decodeURI(cookieList[3])
  })
  $.setBgCookie('backgroundType', cookieList[0], cookieList[1], cookieList[2], cookieList[3]);
  //语言切换
  $('#langCode').on('click', function (e) {
    e.stopPropagation();
    $('#langList').toggle();
  })
  bindLangControl();
  function bindLangControl(){
	  $('#langList').find('li').on('click', function (e) {
		    e.stopPropagation();
		    var val = $(this).data('value');
		    var txt = $(this).text();
		    $('.curLang').val(val).text(txt);
		    $.ajax({//当前页的国际化数据
		      type: "post",
		      url: jsCtx + "/language/sysMunlLang/getCurrentPageListByLan",
		      async: false,
		      data: {
		        "languageAscription": "/login",
		        "langCode": val
		      },
		      success: function (data) {
		        currentLanguageArr = data;
		        pageValue();
		        languageSelectChange(val);


		        validatorL.resetForm();
		        validatorR.resetForm();  
		      }
		    });

		    $('#langList').hide();
		  })
  }
  $(document).on('click', function (e) {
    e.stopPropagation();
    if ($('#langList').show()) {
      $('#langList').hide();
    } else {
      return;
    }
  })
  function languageSelectChange(language){
	    $.ajax({//当前页的国际化数据
	        type: "post",
	        url: jsCtx + "/sys/dict/listDataByLanguage",
	        async: false,
	        data: {
	          "type": "act_langtype",
	          "languageType": "CN"
	        },
	        success: function (data) {
	           var content="";
	           for (var i = 0; i < data.length; i++) {
	        	   if($('.curLang').val()==data[i].value){
		        	 $(".curLang").text(data[i].label);
	        	   }
	        	   content+='<li data-value="'+data[i].value+'" style="list-style: none;line-height: 25px;">'+data[i].label+'</li>';	 
			  }
	           $('#langList').html(content); 
	           //$('#langList').find('li').bind('click');
	           bindLangControl();
	         }
	      });
 
  }
  function pageValue() {
	$('label.error').remove();
    $("#usernamelabel").text(currentLanguageArr.login_name);
    $("#passwordlabel").text(currentLanguageArr.login_password);
    $("#validateCodelabel").text(currentLanguageArr.login_validateCode_input);
    $("#loginBtn").html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.login_button + '');
    $("#newUserNamelabel").text(currentLanguageArr.login_name);
    $("#newPasswdlabel").text(currentLanguageArr.login_password);
    $("#recheckPasswdlabel").text(currentLanguageArr.repeat_password);
    $("#defaultLanguage").text(currentLanguageArr.login_language_select);
    $("#btnImportSubmit").html('<i class="fa fa-lock fa-fw"></i>' + currentLanguageArr.register_button + '');
    if ($('.flippedBox').hasClass("flipped")) {//注册页面
      $(".changeLR").text(currentLanguageArr.return_login)
    } else {//登录页面
      $(".changeLR").text(currentLanguageArr.new_register_button)
    }
    initValidate();
  }
});
// 如果在框架或在对话框中，则弹出提示并跳转到首页
if (self.frameElement && self.frameElement.tagName == "IFRAME"
  || $('#left').length > 0 || $('.jbox').length > 0) {
  //alert('未登录或登录超时。请重新登录，谢谢！');
  top.location.href= "${ctx}/login";
}
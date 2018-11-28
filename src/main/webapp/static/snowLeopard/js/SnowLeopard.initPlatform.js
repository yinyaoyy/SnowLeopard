$(function () {
	//初始化的验证
	jQuery.validator.addMethod('checkFullName', function (value, element) {
        return this.optional(element) || /[^\u0000-\u00FF]/.test(value);
    }, "姓名必须是中文");
    //验证手机
    jQuery.validator.addMethod('checkPhoneNum', function (value, element) {
        return this.optional(element) || /^1[34578]\d{9}$/.test(value);
    }, "请填写正确的手机号码");
	//判断用户是否初次登录
	/* 云平台用户不存在完善信息的情况，注册用户的信息完善在法网上
	 * 2018-5-16 15:10:09
	 * if ($("#isPerfect").val() == "0") {
		firstLoginForm();
	};*/
	//加载菜单
	renderMenuList();
	//验证完善用户资料
	$("form#newUserInfo").validate({
        rules: {
            full_name: {
                required: true,
                checkFullName: true,
                rangelength: [2, 20]
            },
            birth: {
                required: true,
                date: true
            },
            adr: {
                required: true,
                rangelength: [3, 50]
            },
            contact: {
                required: true,
                checkPhoneNum: true
            },
            email: {
                required: true,
                email: true
            },
            department: {
                required: true
            },
            job: {
                required: true
            }
        },
        messages: {
            full_name: {
                required: '姓名不能为空',
                checkUserName: '姓名必须是中文',
                rangelength: '姓名必须是2-20字之间'
            },
            birth: {
                required: '出生日期不能为空',
                rangelength: '请选择正确的时间格式'
            },
            adr: {
                required: '地址不能为空',
                rangelength: '长度在3-50字之间'
            },
            contact: {
                required: '手机号码不能为空',
                checkPhoneNum: '请填写正确的手机号码格式'
            },
            email: {
                required: '邮箱地址不能为空',
                email: '请填写正确的邮箱格式，如abc@abc.com'
            },
            department: {
                required: '科室不能为空'
            },
            job: {
                required: '角色不能为空'
            }
        },
        submitHandler:function(form){
        	var birthday = $("#birth").val();
			var address = $("#adr").val();
			var phone = $("#contact").val();
			var email = $("#email").val();
			var job = $("#job option:selected").val();
			var jobname = $("#job option:selected").text();
			var jgId =$(".cascade").attr("bh");
			var name=$("#full_name").val();
			var userImg=$.createUserNameImage(name).src;
			$.ajax({
				url:jsCtx+"/sys/user/update",
				type:'POST',
				data:{
					name:name,
					birthday:birthday,
					address:address,
					mobile:phone,
					email:email,
					job:job,
					jobname:jobname,
					jgId:jgId,
					userImg:userImg
				},
				success:function(data){
					if(data>0){
						window.location.reload();
					}
				}
			})
        }
	});
});
//渲染菜单
function renderMenuList() {
  $.ajax({
    url: jsCtx + '/sys/menu/getUserMenu',
    type: 'get',
    success: function (data) {
      var firstClass = "fa fa-folder-o fa-fw";
      var _data = data;

      $.each(data, function (index, el) {
        //一级菜单
        if (el.parentId == '1' && el.isShow != "0") {
          var id = el.id;
          var secondList = renderSecondList(_data, id)
          if (el.icon != '' && el.icon != undefined && el.icon != null) {
            firstClass = 'fa fa-fw fa-' + el.icon;
          }
          var li = '<li> <a href="javascript:;" id="' + el.id + '" data-parentId="' + el.parentId + '"> <i class="' + firstClass + '"></i><i class="title">' + el.name + '</i></a>' + secondList + '</li>';
          $('#main-menu').append(li);
        }
      })

      setup_sidebar_menu()
      platForm.bindMainMenuClick('.main-menu a')
    }
  })
  function renderSecondList(allItems, parentId) {
    var _data = allItems;
    var ul = "<ul>";
    var secondClass = "fa fa-fw fa-folder-o";
    $.each(allItems, function (index, items) {
      if (items.parentId == parentId && items.isShow != "0") {
        var id = items.id;
        var thirdList = renderThirdList(_data, id);
        if (items.icon != '' && items.icon != undefined && items.icon != null) {
          secondClass = 'fa fa-fw fa-' + items.icon;
        }
        if (items.href != '' && items.href != undefined && items.href != null) {
          var li = '<li><a href="javascript:" id="' + items.id + '" data-parentId="' + items.parentId + '" data-href="' +jsCtx + items.href + '"><i class="' + secondClass + '"></i><i class="title">' + items.name + '</i></a>' + thirdList + '</li>';
        } else {
          var li = '<li><a href="javascript:" id="' + items.id + '" data-parentId="' + items.parentId + '"><i class="' + secondClass + '"></i><i class="title">' + items.name + '</i></a>' + thirdList + '</li>';
        }

        ul += li
      }
    })
    ul += "</ul>";
    return ul
  }
  function renderThirdList(allItems, parentId) {
    var ul = "<ul>";
    var thirdClass = "fa fa-fw fa-folder-o";
    $.each(allItems, function (index, items) {
      if (items.parentId == parentId && items.isShow != "0") {
        if (items.icon != '' && items.icon != undefined && items.icon != null) {
          thirdClass = 'fa fa-fw fa-' + items.icon;
        }
        if (items.href != '' && items.href != undefined && items.href != null) {
          var li = '<li><a href="javascript:" id="' + items.id + '" data-parentId="' + items.parentId + '" data-href=' +jsCtx+ items.href + '"><i class="' + thirdClass + '"></i><i class="title">' + items.name + '</i></a></li>'
        } else {
          var li = '<li><a href="javascript:" id="' + items.id + '" data-parentId="' + items.parentId + '"><i class="' + thirdClass + '"></i><i class="title">' + items.name + '</i></a></li>'
        }
        ul += li
      }
    })
    ul += "</ul>";
    return ul
  }
};
//完善个人信息
function firstLoginForm() {
	$.ajax({
		url:jsCtx+"/sys/user/getTree",
		data:{},
		async: false,
		success:function(results){
			result=results;
			var oneTab = "";
			for(var i = 0;i < result.length;i++){
				if(result[i].pId==0){
					oneTab+='<div class="casItems" bh="'+result[i].id+'">'+result[i].name+'</div>'
				}
			}
			var infoForm =
				'<form role="form">' +
				'<div class="row">' +
				'<div class="col-md-6">' +
				'<div class="form-group">' +
				'<input type="text" class="form-control" name="full_name" id="full_name" placeholder="姓名">' +
				'</div>' +
				'</div>' +
				'<div class="col-md-6">' +
				'<div class="form-group">' +
				'<input type="text" class="form-control" name="birth" id="birth" placeholder="出生日期" readonly="readonly" style="background-color:white;cursor: auto;" onclick=WdatePicker({dateFmt:"yyyy-MM-dd",isShowClear:false});>' +
				'</div>' +
				'</div>' +
				'</div>' +
				'<div class="row">' +
				'<div class="col-md-6">' +
				'<div class="form-group">' +
				'<input class="form-control" name="contact" id="contact" placeholder="手机">' +
				'</div>' +
				'</div>' +
				'<div class="col-md-6">' +
				'<div class="form-group">' +
				'<input class="form-control" name="email" id="email" placeholder="邮箱">' +
				'</div>' +
				'</div>' +
				'</div>' +
				'<div class="row">' +
				'<div class="col-md-12">' +
				'<div class="form-group">' +
				'<input type="text" class="form-control" name="adr" id="adr" placeholder="地址">' +
				'</div>' +
				'</div>' +
				'</div>' +
				'<div class="row">' +
				'<div class="col-md-12">' +
				'<div class="form-group">' +
				'<p style="padding: 10px 15px;color: #fff;background-color: #cc3f44;">友情提示：单位信息为新用户初次登录时填写，一经确认无法更改，请仔细选择！</p>' +
				'</div>' +
				'</div>' +
				'</div>' +
				'<div class="row">' +
				'<div class="col-md-12">' +
				'<div class="form-group">' +
				'<div name="department" id="department" class="form-control">' +
				'<div class="cascade">请选择-科室</div>'+
				'<div class="cascadeBox">'+
				'<div class="tabbable">' +
				'<ul class="nav nav-tabs">' +
				'<li class="oneTab active"><a href="#tab1" data-toggle="tab">请选择</a></li>' +
				'</ul>' +
				'<div class="tab-content">' +
				'<div class="tab-pane active" id="tab1">' +
				 oneTab +
				'</div>' +
				'</div>' +
				'</div>' +
				'</div>'+
				'</div>' +
				'</div>' +
				'</div>' +
				'</div>' +
				'<div class="row">' +
				'<div class="col-md-12">' +
				'<div class="form-group">' +
				'<select name="job" id="job" class="form-control">' +
				'<option value="">请选择-角色</option>' +
				'</select>' +
				'</div>' +
				'</div>' +
				'</div>' +
				'<div class="row">' +
				'<div class="col-md-12">' +
				'<div class="form-group"><button class="btn btn-default btn-block text-center" id="registerNew"><i class="fa-check"></i> 提交</button></div>' +	
				'</div>' +
				'</div>' +
				'</form>';
			new $.flavr({
				title: '初始化用户信息',
				dialog: 'form',
				form: {
					id: 'newUserInfo',
					content: infoForm,
					method: 'post'
				},
				windowSize:{
					width:600
				},
				buttons: {}
			})
			//时间插件
			/*$('#birth').datepicker().on('changeDate', function(ev) {
				$('#birth').datepicker('hide')
				$('#birth').blur()
			});*/
			//级联组织机构
			$('.cascade').bind('click',function(e){
				e.stopPropagation();
				$('.cascadeBox').toggle();
				if($('.cascadeBox').css('display')=='none'){
					var roleList="<option value=''>请选择-角色</option>";
					var bh=$('.cascade').attr("bh");
					$.ajax({//默认加载内容
					        type: "post",
					        url: jsCtx+"/sys/role/findRoleByRoleId",
					        async:false,
					        data: {
					        	"roleId":bh,
					          },
					         success: function (data) {
					        	 for(var i = 0;i < data.length;i++){
					        		 if(data[i].name!="系统管理员"){
					        			 roleList=roleList+'<option value="'+data[i].id+'">'+data[i].name+'</option>';
					        		 }
					        	 }
					         }
					    });
					$('#job').html('')
					$("#job").append(roleList);
				}
			})
			addCascadeAct();
		}
	})
};
//获取通知数目
function getNotifyNum(){
	$.get("${ctx}/oa/oaNotify/self/count?updateSession=0&t="+new Date().getTime(),function(data){
		var num = parseFloat(data);
		if (num > 0){
			$("#notifyNum").show().html("("+num+")");
		}else{
			$("#notifyNum").hide()
		}
	});
};//级联事件
//级联选项点击事件
function addCascadeAct(){
	var step=0;
	$(document).on("click",".casItems",function(e){
		e.preventDefault();
		e.stopPropagation();
		var bh=$(this).attr("bh");
		var oneTab = "";
		var aa = 0;
		step++;
		for(var i = 0;i < result.length;i++){
			if(bh==result[i].pId){
				oneTab+='<div class="casItems" bh="'+result[i].id+'">'+result[i].name+'</div>';
				aa++;
			}
		}
		$(".cascadeBox .oneTab").each(function(i,v){
			if(i>$(".oneTab.active").index()){
				$(this).remove();
			}
		})
		$(".cascadeBox .tab-pane").each(function(i,v){
			if(i>$(".tab-pane.active").index()){
				$(this).remove();
			}
		})
		$(".oneTab.active a").text($(this).text());
		$(".cascade").attr("bh",bh);
		if(aa>0){
				$(".cascadeBox .tab-pane").removeClass("active");
				$(".cascadeBox .oneTab").removeClass("active");
				var arr=$(".cascadeBox .oneTab");
				var con="";
				for (var i = 0; i < arr.length; i++) {
					if(i==0){
						con+=arr.eq(i).text();
					}else{
						con+=" / "+arr.eq(i).text();
					}
				}
				$(".cascade").text(con);
				$(".cascadeBox .nav-tabs").append('<li class="oneTab active"><a href="#tab_'+step+'" data-toggle="tab">请选择</a></li>');
				$(".cascadeBox .tab-content").append('<div class="tab-pane active" id="tab_'+step+'">' + oneTab +'</div>');
		}else{
			$(".cascadeBox").hide();
			var arr=$(".cascadeBox .oneTab");
			var con="";
			for (var i = 0; i < arr.length; i++) {
				if(i==0){
					con+=arr.eq(i).text();
				}else{
					con+=" / "+arr.eq(i).text();
				}
			}
			$(".cascade").text(con);
			var roleList="<option value=''>请选择-角色</option>";
			//var bh=$('.cascade').attr("bh");
			$.ajax({//默认加载内容
			        type: "post",
			        url: jsCtx+"/sys/role/findRoleByRoleId",
			        async:false,
			        data: {
			        	"roleId":bh,
			          },
			         success: function (data) {
			        	 for(var i = 0;i < data.length;i++){
			        		 roleList=roleList+'<option value="'+data[i].id+'">'+data[i].name+'</option>'; 
			        	 }
			         }
			    });
			$('#job').html('')
			$("#job").append(roleList);
		}
		//$(".cascade").text($(this).text());
	})
}

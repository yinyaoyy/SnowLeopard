jQuery(document).ready(function($) {	
	//头像修改
	//判断浏览器是否支持FileReader接口  
	if(typeof FileReader == 'undefined') {
		result.InnerHTML = "<p>你的浏览器不支持FileReader接口！</p>";
		//使选择控件不可操作  
		file.setAttribute("disabled", "disabled");
	}
	var userImgs=$("#userImgHidden").attr("src");
/*	 $.ajax({//默认加载内容
	        type: "post",
	        url: jsCtx+"/sys/user/infoData",
	        async:false,
	        data: {
	          },
	         success: function (data) {
	        	 userImgs=$("#userImgHidden").val()+data.photo;
	         }
	    });*/
	
	if(IsExist(userImgs)){
		//头像回显
		var result = document.getElementById("cropper-content");
		//显示文件  
		result.innerHTML = '<img src="' + userImgs + '" alt="" height="100%"/>';
		imageCropper();
	}
	function IsExist(pathImg)
{
		   var ImgObj=new Image();
		    ImgObj.src= pathImg;
		    //alert(ImgObj.complete);
		    if(ImgObj.fileSize > 0 || (ImgObj.width > 0 && ImgObj.height > 0))
		     {
		       return true;
		     } else {
		       return false;
		    }
} 
	$('#inputImage').change(function() {
		readAsDataURL()
	})	
	function readAsDataURL() {
		//检验是否为图像文件  
		var file = document.getElementById("inputImage").files[0];
		if(file != '' && file != undefined && file != null) {
			if(!/image\/\w+/.test(file.type)) {
				alert("看清楚，这个需要图片！");
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
				var result = document.getElementById("cropper-content");
				//显示文件  
				result.innerHTML = '<img src="' + this.result + '" alt="" height="100%"/>';
				imageCropper()

			}
		}
	}

	function imageCropper() {
		var options = {
			minContainerHeight: 500,
			aspectRatio: 1, //裁剪框比例 1：1  
			preview: '.img-preview',
			mouseWheelZoom: true, //通过鼠标滚轮来缩放图片
			viewMode: 1, //剪裁框移动范围 只能在图片内移动
			guides: true, //裁剪框虚线 默认true有  
			build: function(e) { //加载开始  
				$('#cropper-content').find('img').hide()
			},
			built: function(e) { //加载完成  
				$('#image-cropper .btn-group').show()
			},
			background: true, // 容器是否显示网格背景  
			movable: true, //是否能移动图片  
			cropBoxMovable: false, //是否允许拖动裁剪框  
			cropBoxResizable: false //是否允许拖动 改变裁剪框大小  
		};
		$('#cropper-content img').cropper(options)
	}
	$(document.body).on('click', '[data-method]', function(e) {
		$image = $('#cropper-content > img');
		var data = $(this).data();
		if(data.method) {
			data = $.extend({}, data); // Clone a new one
			var options = {
				lg: {
					'width': '125',
					'height': '125'
				}
				
			}
			var resultList = new Array();
			if(data.method === "getCroppedCanvas") {
				$.each(options, function(index, opt) {
					var result = $image.cropper(data.method, opt);
					var img = convertCanvasToImage(result);
					var imgsrc = img.src;
					var imgname = 'img' + img.width;
					var resultDb = {
						imageName: imgname,
						imgsrc: imgsrc
					}
					resultList.push(resultDb)
				})
				//上传文件到服务器
				 $.ajax({//默认加载内容
				        type: "post",
				        url: jsCtx+"/sys/user/imgUpload",
				        data: {
				        	"param":resultList[0].imgsrc,
				          },
				         success: function (data) {
				        	 toVail("修改头像成功！","success");
				        	 window.top.$("#topSculpture").attr("src",data);
				        	 window.top.$(".img-circle").attr("src",data);
				         }
				    });
			} else {
				$image.cropper(data.method, data.option);
			}

		}
	})

	function convertCanvasToImage(canvas) {
		var image = new Image();
		image.src = canvas.toDataURL("image/png");
		return image
	}
});
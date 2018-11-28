<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>图标选择</title>
		<meta name="decorator" content="blank" />
		<style style="text/css">
			.searchbox,
			.controlbox {
				padding: 5px 0;
			}
			
			.fa-hover {
				height: 50px;
				line-height: 1;
				font-size: 13px;
				cursor: pointer;
				padding-top: 10px;
				text-align: center;
				margin: 5px 0;
			}
			
			.fa-hover .fa {
				margin-right: 10px;
				margin-bottom: 5px;
			}
			
			.fa-hover:hover {
				background-color: green;
				color: white;
			}
			
			.fa-hover p {
				width: 80%;
				margin: 0 auto;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.active {
				background-color: green;
				color: white;
			}
			
			.fa-hover:hover .fa {
				font-size: 16px;
			}
			
			.form-control-feedback {
				right: 20px;
				top: 10px;
			}
			
			#selectList {
				overflow: auto;
				height: auto;
				text-align: center;
				padding-left: 15px;
				padding-right: 15px;
			}
		</style>
		<script type="text/javascript">
			var curSelectName = '';
			var curSelectPath = '';
			var iconname = '';
			var icoTree = null;
			$(function() {
				//getFiles(path);
				loadIcon();
				selIconList();
				$(document).on("click", ".lib", function() {
					$(this).siblings().removeClass("active");
					$(this).addClass("active");
					$("#icon").val($(this).find("p").text());
				});
			});

			function loadIcon() {
				$.ajax({
					type: "post",
					url: jsCtx + "/tag/faselect",
					dataType: "json",
					data: {
						'Path': 'IconFont.json'
					},
					success: function(data) {
						$.each(data, function(index, classList) {
							//加载select选项
							var iconId = 'icon_' + encodeURI(index).split('%').join('')
							var option = '<option value="' + iconId + '">' + index + '</option>'
							$('#selIconList').append(option)
							//加载主体图标
							var $section = $('<section id="' + iconId + '">');
							var h4 = '<div class="row"><div class="col-sm-12 text-center"><h3>' + index + '</h3></div></div>';
							$section.append(h4)
							var $iconList = $('<div class="row iconList"></div>')
							for(var i = 0; i < classList.length; i++) {
								var col = '<div class=\'fa-hover col-xs-2 lib\' ' +
									'curSelectPath=\'fa ' + classList[i] + '\'; title="' + classList[i].substring(3) + '">' +
									'<i class="fa ' + classList[i] + '"></i><p>' + classList[i].substring(3) + '</p>' +
									'</div>';
								$iconList.append(col)
							}
							$iconList.appendTo($section)
							$section.appendTo($('#selectList'))
						})
					}
				})
			}

			function selIconList() {
				$('#selIconList').bind('change', function() {
					var iconListId = $(this).val();
					if(iconListId === 'all') {
						$('section').show();
					} else {
						$('#' + iconListId).show().siblings('section').hide()
					}
				})
				$('#searchIcon').bind('input propertychange', function() {
					var inputTxt = $(this).val();
					if(inputTxt == '' || inputTxt == undefined || inputTxt == null) {
						$('#searchResult').remove();
						$('section').show()
					} else {
						$('section').hide();
						$('#searchResult').remove()
						//加载主体图标
						var faList = $('#selectList').find('.fa');
						var resultList = new Array();
						$.each(faList, function(index, el) {
							var iconClass = $(el).attr('class').substring(3)
							if(iconClass.indexOf(inputTxt) >= 0) {
								resultList.push(iconClass);
							}
						})
						var $section = $('<section id="searchResult">');
						var h4 = '<div class="row"><div class="col-sm-12 text-center"><h3>搜索结果</h3></div></div>';
						$section.append(h4)
						var $iconList = $('<div class="row iconList"></div>')
						for(var i = 0; i < resultList.length; i++) {
							var col = '<div class=\'fa-hover col-xs-2 lib\' onclick="$(\'.iconList\').children().removeClass(\'active\');$(this).addClass(\'active\');" ' +
								'curSelectPath=\'fa ' + resultList[i] + '\'; ondblclick="OK_Click();" title="' + resultList[i].substring(3) + '">' +
								'<i class="fa ' + resultList[i] + '"></i><p>' + resultList[i].substring(3) + '</p>' +
								'</div>';
							$iconList.append(col)
						}
						$iconList.appendTo($section)
						$section.appendTo($('#selectList'))
						resultList = []
					}
				})
			}
		</script>
	</head>
	<body>
		<input type="hidden" id="icon" value="${value}" />
		<div class="row searchbox">
			<div class="col-xs-6">
				<select class="form-control" id="selIconList">
				<option value="all">==全部==</option>
				</select>
			</div>
			<div class="col-xs-6">
				<input type="text" class="form-control" id="searchIcon" placeholder="图标名称" />
				<span class="fa fa-fw fa-biz-info-new form-control-feedback"></span>
			</div>
		</div>
		<div id="selectList" class="col-xs-12"></div>
	</body>
</html>
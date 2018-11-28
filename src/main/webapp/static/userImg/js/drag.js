$(function() {

	// 标题经过效果
	$(".sort-item h5").on("mouseover", ".btn-more", function() {
		$(this).find("img").attr("src", jsCtx+"/static/images/platform/more2.png")
		$(this).find(".box-show").stop().slideDown(10)
	})
	$(".sort-item h5").on("mouseleave", ".btn-more", function() {
		$(this).find("img").attr("src", jsCtx+"/static/images/platform/more1.png")
		$(this).find(".box-show").stop().slideUp(10)
	})
	// 开启拖拽
	var flag = false;
	$('.btn-menu').on('click', function() {
		$('.btn-reload').stop().fadeIn(100);
		$('.btn-drag').stop().fadeIn(200);
		$('.btn-add').stop().fadeIn(400);
		if(!flag) {
			$(this).addClass('btn-menu-click')
			$(this).html('<i class="fa fa-fw fa-times" style="width:20px;height:20px;position: absolute;left: 10px;top: 12px;"></i>');
			flag = true;
		} else {
			$(this).removeClass('btn-menu-click')
			$(this).html('<i class="fa fa-fw fa-bars" style="width:20px;height:20px;position: absolute;left: 10px;top: 12px;"></i>');
			$('.btn-reload').stop().fadeOut(400);
			$('.btn-drag').stop().fadeOut(200);
			$('.btn-add').stop().fadeOut(100);
			flag = false;
		}
	})
	$('.btn-drag').bind('click', function() {
		var drag = $(this).hasClass('isdrag');
		console.log(drag)
		if(drag) {
			$('.sort-item .sort-item-ul').css('cursor','move');
			$('.btn-drag').removeClass('isdrag')
			$(this).attr('title', '保存');
			$(this).html('<i class="fa fa-fw fa-floppy-o" style="width:20px;height:20px;position: absolute;left: 10px;top: 12px;"></i>');
			
			$(".sort-item").sortable({});
			$(".sort-item").sortable("enable");
		} else {
			$('.sort-item .sort-item-ul').css('cursor','default');
			$('.btn-drag').addClass('isdrag');
			$(this).attr('title', '拖放');
			$(this).html('<i class="fa fa-fw fa-arrows" style="width:20px;height:20px;position: absolute;left: 10px;top: 12px;"></i>');
			$(".sort-item").sortable("disable");
		}

	})

	$('.btn-add').bind('click', function() {
		var $box = $('<div class="col-md-3 sort-item-ul"><div class="sort-item-li"><h5>');
		var $btnMore = $('<div class="btn-more">');
		var $img = $('<img src="'+jsCtx+'/static/images/platform/more1.png">');
		var $boxShow = $('<div class="box-show">');
		var $btnRefresh = $('<div class="btn-refresh"><i class="fa fa-fw fa-refresh"></i><span style="margin-left:4px;display:inline-block;font-size:12px">刷新</span></div>');
		var $btnTrash = $('<div class="btn-trash"><i class="fa fa-fw fa-trash" style="font-size:15px;"></i><span style="margin-left:6px;display:inline-block;font-size:12px">删除</span></div>')

		$btnMore.bind('mouseover', function() {
			$(this).find("img").attr("src", jsCtx+"/static/images/platform/more2.png")
			$(this).find(".box-show").stop().slideDown(50)
		}).bind('mouseleave', function() {
			$(this).find("img").attr("src", jsCtx+"/static/images/platform/more1.png")
			$(this).find(".box-show").stop().slideUp(50)
		})
		$btnTrash.bind('click',function(){
			$(this).parent().parent().parent().parent().parent().remove();
		})
		$btnRefresh.bind('click',refreshModel)
		console.log(refreshModel)
		$boxShow.append($btnRefresh).append($btnTrash);
		$btnMore.append($img).append($boxShow);
		$btnMore.appendTo($box.find('h5'))
		$(".sort-item").append($box)
		var drag = $('.btn-drag').hasClass('isdrag');
		if(!drag){
			$('.sort-item .sort-item-ul').css('cursor','move');
		}else{
			$('.sort-item .sort-item-ul').css('cursor','default');
		}

	})
	// 删除事项模块
	$(".sort-wrapper .btn-trash").on("click", function() {
		$(this).parent().parent().parent().parent().parent().remove();
	})
	$('.sort-item-add').on("click", function() {
		alert("此处为弹框界面")
	});
	
	//刷新模块
	$('.btn-reload').on('click',function(){
//		renderChart();
		window.location.reload();
	})
	var refreshModel = function(modelId){
		$.ajax({
			data:modelId
		})
	}
})

//init chart
renderChart()

function renderChart() {
	//first
	function myChart1() {
		var myChart_progress = echarts.init(document.getElementById('myChart-progress'));
		option1 = {
			color: ['#019FE9'],
			tooltip: {
				trigger: 'axis'
			},
			calculable: true,
			xAxis: [{
				type: 'category',
				boundaryGap: false,
				data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
			}],
			yAxis: [{
				type: 'value'
			}],
			series: [{
					// name:'完成进度',
					type: 'line',
					smooth: true,
					itemStyle: {
						normal: {
							areaStyle: {
								type: 'default'
							}
						}
					},
					data: [100, 342, 221, 354, 260, 830, 710]
				},

			]
		};

		myChart_progress.setOption(option1);
	}
	myChart1()
	$("#project-progress-schedule .btn-refresh").on("click", function() {
		// var obj ={}
		// reloadPage(obj);
		myChart1();
	})

	function mychart2() {
		//second
		var myChart_qualify = echarts.init(document.getElementById('myChart-qualify'));
		option2 = {
			legend: {
				orient: 'horizontal',
				x: 'center',
				y: 'center',
			},
			title: {
				text: '100%',
				x: 'center',
				y: 'center',
				textStyle: {
					fontSize: '16',
					color: '#4C9FE8',
				}
			},
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b}: {c} ({d}%)"
			},
			series: [{
				name: '访问来源',
				type: 'pie',
				radius: ['34%', '56%'],
				center: ['50%', '50%'],
				avoidLabelOverlap: false,
				label: {
					normal: {
						show: false,
						position: 'center'
					},
				},
				color: ['#59BBFA', '#81C5F0', '#025DB0', '#007DE3', '#199AF5'],
				labelLine: {
					normal: {
						show: false
					}
				},
				data: [{
					value: 20,
					name: '1'
				}, {
					value: 10,
					name: '2'
				}, {
					value: 30,
					name: '3'
				}, {
					value: 15,
					name: '4'
				}, {
					value: 25,
					name: '5'
				}, ]
			}]
		};
		myChart_qualify.setOption(option2);

	}
	mychart2();

	$("#project-qualify .btn-refresh").on("click", function() {
		// var obj ={}
		// reloadPage(obj);
		mychart2();
	})
	//third
	function mychart3() {
		var myChart_finish = echarts.init(document.getElementById('myChart-finish'));
		option3 = {
			legend: {
				orient: 'horizontal',
				x: 'center',
				y: 'center',
			},
			series: [{
				name: '业务指标',
				type: 'gauge',
				radius: '80%',
				min: 0,
				max: 100,
				center: ['center', '50%'],
				splitNumber: 10, // 分割段数，默认为5
				axisLine: { // 坐标轴线
					lineStyle: { // 属性lineStyle控制线条样式
						color: [
							[0.2, '#ff4500'],
							[0.8, '#48b'],
							[1, '#228b22']
						],
						width: 8
					}
				},
				axisTick: { // 坐标轴小标记
					splitNumber: 5, // 每份split细分多少段
					length: 16, // 属性length控制线长
					lineStyle: { // 属性lineStyle控制线条样式
						color: 'auto'
					}
				},
				axisLabel: { // 坐标轴文本标签，详见axis.axisLabel
					textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						color: 'auto'
					}
				},
				splitLine: { // 分隔线
					show: true, // 默认显示，属性show控制显示与否
					length: 10, // 属性length控制线长
					lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
						color: 'auto'
					}
				},
				pointer: {
					width: 3
				},
				title: {
					textStyle: {
						// 其余属性默认使用全局文本样式，详见TEXTSTYLE  
						// fontWeight: 'bolder',  
						fontSize: 14,
						color: '#000'
					}
				},
				detail: {
					formatter: '{value}%',
					textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
						color: 'auto',
						fontWeight: 'bolder',
						fontSize: '14',
					}
				},
				data: [{
					value: 50,
					name: '完成率'
				}]
			}]
		};

		clearInterval(timeTicket);
		var timeTicket = setInterval(function() {
			option3.series[0].data[0].value = (Math.random() * 100).toFixed(2) - 0;
			myChart_finish.setOption(option3, true);
		}, 2000);

		// 使用刚指定的配置项和数据显示图表。
		myChart_finish.setOption(option3);
	}
	mychart3();
	$("#project-finish .btn-refresh").on("click", function() {
		// var obj ={}
		// reloadPage(obj);
		mychart3();
	})
};
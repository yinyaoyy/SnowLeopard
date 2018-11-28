<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>律师事务所管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="https://api.map.baidu.com/api?v=2.0&ak=UPdojkdmGagfoDMIeNzsWy3QGoinLBiW"></script>
</head>
<body>
<div class="row">
  <div class="col-xs-3">
    <div class="form-group">
      <label class="control-label">按关键字搜索：</label>
      <input type="text" class="input-xlarge form-control" placeholder="请输入关键字进行搜索" id="bmapInput">
      <div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
    </div>
    <div class="form-group">
      <label class="control-label">坐标(经纬度)：</label>
      <input type="text" id="coordinate" name="coordinate" readonly="readonly" maxlength="30" class="input-xlarge form-control"/>
    </div>
  </div>
  <div id="bmapDiv" class="col-xs-8" style="height:500px;"></div>
</div>
<!-- 百度地图处理函数 -->
<script type="text/javascript">
	// 百度地图API功能
	var bmap = new BMap.Map("bmapDiv",{enableMapClick:false});// 创建Map实例，构造底图时，关闭底图可点功能
	var p = "${coordinate }";
	if(p!=""){
		$("#coordinate").val(p);
		var bp = p.split(",");
		var point = new BMap.Point(bp[0], bp[1]);
		bmap.centerAndZoom(point, 13);
		var marker = new BMap.Marker(point);
		bmap.addOverlay(marker);//地图上添加点
		marker.enableDragging();//设置点可以拖动(disableDragging不可拖动)
		marker.addEventListener('dragend', function (e) {//拖动标注结束
			$("#coordinate").val(e.point.lng + "," + e.point.lat);
		});
	}
	else{
		bmap.centerAndZoom("锡林郭勒盟", 13);//初始化地图,根据城市设置中心点和地图级别
	}
	// 左上角，添加比例尺
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
	//左上角，添加默认缩放平移控件
	var top_left_navigation = new BMap.NavigationControl();  
	//添加地图类型控件
	bmap.addControl(top_left_control);
	bmap.addControl(top_left_navigation);
	bmap.enableScrollWheelZoom(true);//开启鼠标滚轮缩放
	//单击获取点击的经纬度
	bmap.addEventListener("click",function(e){
		bmap.clearOverlays();
		$("#coordinate").val(e.point.lng + "," + e.point.lat);
		var marker = new BMap.Marker(new BMap.Point(e.point.lng,e.point.lat));
		bmap.addOverlay(marker);//地图上添加点
		marker.enableDragging();//设置点可以拖动(disableDragging不可拖动)
		marker.addEventListener('dragend', function (e) {//拖动标注结束
			$("#coordinate").val(e.point.lng + "," + e.point.lat);
		});
	});
	var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
		{"input" : "bmapInput"
		,"location" : bmap
	});
	ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
	var str = "";
		var _value = e.fromitem.value;
		var value = "";
		if (e.fromitem.index > -1) {
			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		}    
		str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
		
		value = "";
		if (e.toitem.index > -1) {
			_value = e.toitem.value;
			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		}    
		str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		$("#searchResultPanel").html(str);
	});
	var myValue;
	ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
	var _value = e.item.value;
		myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		$("#searchResultPanel").html("onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue);
		setPlace();
	});
	function setPlace(){
		bmap.clearOverlays();    //清除地图上所有覆盖物
		function myFun(){
			var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
			bmap.centerAndZoom(pp, 18);
			bmap.addOverlay(new BMap.Marker(pp));    //添加标注
			$("#coordinate").val(pp.lng + "," + pp.lat);
		}
		var local = new BMap.LocalSearch(bmap, { //智能搜索
		  onSearchComplete: myFun
		});
		local.search(myValue);
	}
</script>
</body>
</html>
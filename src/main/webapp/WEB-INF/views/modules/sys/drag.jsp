<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<!DOCTYPE html>
<html style="overflow-x : hidden;">

	<head lang="en">
		<meta charset="UTF-8">
		<title></title>
		<link href="${ctxStatic}/snowLeopard/Css/fonts/fontawesome/css/font-awesome.css" rel='stylesheet' type="text/css" />
		<link rel="stylesheet" href="${ctxStatic}/snowLeopard/Css/bootstrap.css">
		<link href="${ctxStatic}/snowLeopard/Css/SnowLeopard-core.css" rel='stylesheet' type="text/css" />
		<link rel="stylesheet" href="${ctxStatic}/userImg/css/drag.css">
		<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script	type="text/javascript" src="${ctxStatic}/snowLeopard/js/common.js"></script>
		<script src="${ctxStatic}/snowLeopard/js/echarts.min.js"></script>
		<script type="text/javascript">
			var ctx = '${ctx}',
				ctxStatic = '${ctxStatic}';
			var jsCtx = "${ctx}";
		</script>
		<style type="text/css">
					/*时间*/
					.timeCenter {
						height: 85px;
						color: #FFFFFF;
						width: 100%;
						position: absolute;
						top: 10px;
						text-align: center;
					}
					
					.timeCenter .dateDay,
					.timeCenter .dateInfo,
					.timeCenter .dateTime {
						display: inline-block;
						color: #fff;
					}
					
					.timeCenter .dateInfoWeek,
					.timeCenter .dateInfoMY {
						color: #fff
					}
					
					.timeCenter .dateDay {
						font-size: 3em;
						line-height: 85px;
					}
					
					.timeCenter .dateInfo {
						margin-left: 10px;
						font-size: 1em;
					}
					
					.timeCenter .dateTime {
						font-size: 3em;
						line-height: 85px;
						margin-left: 5px;
					}
					@media screen and (min-width:1200px){
					    .timeCenter .dateDay {
							font-size: 4em;
						}
						
						.timeCenter .dateInfo {
							font-size: 1.5em;
						}
						
						.timeCenter .dateTime {
							font-size: 4em;
						}
					}
					@media only screen and (min-width: 480px) and (max-width: 767px){
						.timeCenter .dateDay {
							font-size: 2.3em;
						}
						
						.timeCenter .dateInfo {
							font-size: 0.8em;
						}
						
						.timeCenter .dateTime {
							font-size: 2.3em;
						}
					}
				</style>
	</head>

	<body>
		<!-- 可拖拽模块 -->
		<div class="drag-wrapper">
			<div class="row">
				<div class="drag-nav col-md-3">
					<div class="drag-nav act-task" style="background:url(${ctxStatic}/images/jx-1.png) no-repeat;background-size: 100% 100%;" id='wait' data-href='${ctx }/act/task/todo' data-title='待办事项'>
						<div class="dragmessagenumber"><i class="fa fa-biz-upcoming-1" style="font-size:62px;margin-left: -5px;float:left;padding-top: 10px; opacity:0.4"></i>
							<div class="r-count" style="margin: 12px 21px 0 0;"><span>${waitCount }</span><span style="font-size:16px;">待办事项</span></div>
						</div>
						<div class="dragmessagebtn">
							<input type="button" value="查看更多">
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="drag-nav act-task" style="background:url(${ctxStatic}/images/jx-2.png) no-repeat;background-size: 100% 100%;" id='already' data-href='${ctx }/act/task/historic' data-title='已办事项'>
						<div class="dragmessagenumber"><i class="fa fa-biz-has-been-done-1" style="font-size: 62px;margin-left:-5px;float:left;padding-top: 14px; opacity:0.4"></i>
							<div class="r-count" style="margin: 12px 21px 0 0;"><span>${alreadyCount }</span><span style="font-size:16px;">已办事项</span></div>
						</div>
						<div class="dragmessagebtn">
							<input type="button" value="查看更多">
						</div>
					</div>
				</div>
				<div class="drag-nav col-md-3">
					<div class="drag-nav act-task" style="background:url(${ctxStatic}/images/jx-3.png) no-repeat;background-size: 100% 100%;" id='all' data-href='${all }act/task/all' data-title='全部事项'>
						<div class="dragmessagenumber"><i class="fa fa-biz-upcoming-1" style="font-size:62px;margin-left: -5px;float:left;padding-top: 10px; opacity:0.4"></i>
							<div class="r-count" style="margin: 12px 21px 0 0;"><span>${allCount }</span><span style="font-size:16px;">全部事项</span></div>
						</div>
						<div class="dragmessagebtn">
							<input type="button" value="查看更多">
						</div>
					</div>
				</div>
			</div>
			<script>
				$(function() {
					platForm.bindInsideAddTab('.act-task');
					setInterval(function() {
						setHeaderTime()
					}, 1000)

					function setHeaderTime() {
						var week = new Array(7);
						week[0] = "星期日";
						week[1] = "星期一";
						week[2] = "星期二";
						week[3] = "星期三";
						week[4] = "星期四";
						week[5] = "星期五";
						week[6] = "星期六";
						var year = new Date().getFullYear();
						var month = new Date().getMonth() + 1;
						var day = new Date().getDate();
						var weekCode = new Date().getDay();
						var H = new Date().getHours();
						var M = new Date().getMinutes();
						var S = new Date().getSeconds();
						if(day < 10) {
							day = '0' + day;
						} else {
							day = day;
						}
						if(H < 10) {
							H = '0' + H;
						} else {
							H = H;
						}
						if(M < 10) {
							M = '0' + M;
						} else {
							M = M;
						}
						if(S < 10) {
							S = '0' + S;
						} else {
							S = S;
						}
						$('.timeCenter .dateDay').html(day);
						$('.timeCenter .dateInfoWeek').html(week[weekCode]);
						$('.timeCenter .dateInfoMY').html(month + '/' + year);
						$('.timeCenter .dateTime').html(H + ':' + M + ':' + S);
					}
				});
			</script>
		</div>
		<div class="sort-wrapper">
			<div class="sort-item row">
				<c:forEach items="${serverList }" var="sl" varStatus="status">
				<div class="col-md-${fn:split(sl.remarks,',')[0] } sort-item-ul">
					<iframe id="aiframe${status.index }" src="${ctx }${sl.pcHerf }" width="100%" height="${fn:split(sl.remarks,',')[1] }" style="border: none;"></iframe>
				</div>
				</c:forEach>
			</div>
		</div>
<script>
	document.body.onselectstart = document.body.ondrag = function() {
		return false;
	}
</script>
	</body>

</html>

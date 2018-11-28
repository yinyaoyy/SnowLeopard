<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="decorator" content="default"/>
		<title>日志查询</title>
		<style>
			* {
				margin: 0;
				padding: 0;
			}
			
			s,
			i,
			em {
				font-style: normal;
				text-decoration: none;
			}
			
			html,
			body {
				box-sizing: border-box;
				font-size: 13px;
				font-family: "Helvetica Neue",Helvetica,Arial,"Microsoft Yahei","Hiragino Sans GB","Heiti SC","WenQuanYi Micro Hei",sans-serif;
			}
			.logo{
				width: 100%;
			}
			.logo img{
				height: 30px;
			}
			table,
			tr,
			td {
				border-collapse: collapse;
				text-align: center;
			}
			table {
				width: 100%
			}
			
			.dateTitle {
				text-align: center;
				margin-top: 5px;
				margin-bottom: 5px;
			}
			
			td {
				padding: 5px 10px;
			}
			
			.dateTable{
				border-right: 1px solid #000;
				border-bottom: 1px solid #000;
			}
			.dateTable td{
				border-left: 1px solid #000;
				border-top: 1px solid #000;
			}
			.orderTable{
				border-left: 1px solid #000;
			}
			.orderTable td{
				border-right: 1px solid #000;
				border-bottom: 1px solid #000;
			}
		</style>
	</head>

	<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/sys/log">日志查询</a></li>
		<li class="active"><a href="">日志详情</a></li>
	</ul>
		<div class="dates">
			<div class="dateTitle">
				 <h2 style="font-size:20px;font-weight:normal;margin-bottom:30px;">日志详情</h2> 
			</div>
			<div class="dateMain">
				<table class="dateTable" id="" style="table-layout: fixed" >
					<thead>
						<tr>
							<td width="100px;">操作菜单</td>
							<td style="text-align: left !important;">${log.title}</td>
							<td width="100px">操作用户</td>
							<td style="text-align: left !important;">${log.createBy.name}</td>
						    <td width="100px">业务所属</td>
						    <td style="text-align: left !important;">${log.createBy.office.name}</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>URI</td>
							<td colspan="3" style="text-align: left !important;word-wrap: break-word;">${log.requestUri}</td>
							<td>提交方式</td>
							<td style="text-align: left !important;">${log.method}</td>
						</tr>
						<tr>
							<td>提交参数</td>
							<td colspan="5" style="height:200px;min-height:200px;text-align: left !important;vertical-align: top;word-wrap: break-word;">${log.params}</td> 
						</tr>
						<tr>
							<td>用户代理</td>
							<td colspan="5" style="text-align: left !important;vertical-align: top;overflow:auto; word-wrap: break-word;">${log.userAgent}</td>
						</tr>
						<tr>
							<td>操作者IP</td>
							<td colspan="3" style="text-align: left !important;">${log.remoteAddr}</td>
							<td>操作时间</td>
							<td style="text-align: left !important;"><fmt:formatDate value="${log.createDate}" type="both"/></td>
						</tr>
					</tbody>
				</table>
			
			</div>
		</div>
		<div id="printbtn" style=" width:100%;text-align: center; margin:10px auto;">  
  <input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
</div>
	</body>
</html>


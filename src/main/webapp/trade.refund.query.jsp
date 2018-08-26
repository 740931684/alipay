<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp"%>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page isELIgnored="false"%>
<base href="<%=basePath %>">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>退款查询</title>
</head>
<body>
<table style="margin: 100px auto;text-align: center;">
	<tr>
		<th>订单号</th>
		<th style="width: 400px">交易号</th>
		<th style="width:100px">付款金额</th>
		<th style="width:400px">商品描述</th>
		<th style="width:200px">交易状态</th>
	</tr>
	<tr style="height: 50px">
		<td class="outTradeNo">${goods.outTradeNo}</td>
		<td class="tradeNo" style="width:400px">${goods.tradeNo}</td>
		<td class="refundAmount" style="width:100px">${goods.totalAmount}</td>
		<td class="refundReason" style="width:400px">${goods.body}</td>
		<td class="outRequestNo" style="width:200px">${goods.status}</td>
	</tr>

</table>



</body>
</html>
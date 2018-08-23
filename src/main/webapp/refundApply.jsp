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
    <title>退款申请列表</title>
</head>
<body>

<c:forEach var="list"  items="lists">
    <p>订单号：${list.outTradeNo}</p>
    <p>交易号：${list.tradeNo}</p>
    <p>退款金额：${list.refundAmount}</p>
    <p>退款原因：${list.refundReason}</p>
    <p>退款号：${list.outRequestNo}</p>
</c:forEach>

</body>
</html>

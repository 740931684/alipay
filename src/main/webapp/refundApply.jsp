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

<table style="margin: 100px auto;text-align: center;">
    <tr>
        <th>订单号</th>
        <th style="width: 400px">交易号</th>
        <th style="width:100px">退款金额</th>
        <th style="width:400px">退款原因</th>
        <th style="width:200px">退款号</th>
        <th>操作</th>
    </tr>
    <c:forEach var="list"  items="${lists}" >
        <tr style="height: 50px">
            <td class="outTradeNo">${list.outTradeNo}</td>
            <td class="tradeNo" style="width:400px">${list.tradeNo}</td>
            <td class="refundAmount" style="width:100px">${list.refundAmount}</td>
            <td class="refundReason" style="width:400px">${list.refundReason}</td>
            <td class="outRequestNo" style="width:200px">${list.outRequestNo}</td>
            <td>
                <button class="confirm">确认</button>
                <button class="cancel">取消</button>
            </td>
        </tr>
    </c:forEach>

</table>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script>
    $(document).ready(function () {
        $('.confirm').click(function () {
            operator('.confirm',"refundConfirm");
        });

        $('.cancel').click(function () {
            operator('.cancel',"refundCancel");
        });

        function operator(var1,var2) {
            var params = {};
            params.outTradeNo = $(var1).parent().parent().find('.outTradeNo').text();
            params.tradeNo = $(var1).parent().parent().find('.tradeNo').text();
            params.refundAmount = $(var1).parent().parent().find('.refundAmount').text();
            params.refundReason = $(var1).parent().parent().find('.refundReason').text();
            params.outRequestNo = $(var1).parent().parent().find('.outRequestNo').text();
            $.ajax({
                async:true,
                url:var2,
                type:"post",
                data:params,
                dataType:"json",
                success:function (data) {
                    window.location.reload();
                },
                error:function (data) {
                    alert(data.responseText);
                    window.location.reload();
                }
            });
        }
    });
</script>

</body>
</html>

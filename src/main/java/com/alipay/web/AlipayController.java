package com.alipay.web;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.config.AlipayConfig;
import com.alipay.dao.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alipay.dao.TradeRefundDao;
import com.alipay.entity.Goods;
import com.alipay.entity.TradeRefund;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
public class AlipayController {
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    TradeRefundDao refundDao;


    @RequestMapping(value = {"/","index"})
    public String showIndex(){
        return "index";
    }


    @RequestMapping("trade_pay")
    public String sendToZFB(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //付款金额，必填
        String total_amount = new String(request.getParameter("WIDtotal_amount").getBytes("ISO-8859-1"),"UTF-8");
        //订单名称，必填
        String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
        //商品描述，可空
        String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();//调用sdk生成表单


        //输出，注意输出格式，错误容易验签失败
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        return null;
    }


    @RequestMapping("return_url")
    public String paySuccess(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
            //	System.out.println(valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            //回调参数里面没有Body这个
           // String body = new String(request.getParameter("body").getBytes("ISO-8859-1"),"utf-8");
            //将数据插入数据库
            goodsDao.insertIntoGoods(out_trade_no,total_amount,trade_no,"");

            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
        }else {
            response.getWriter().println("验签失败");
        }
        return "return_url";
    }


    @RequestMapping("trade_query")
    @ResponseBody
    public String tradeQuery(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        //AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTQout_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTQtrade_no").getBytes("ISO-8859-1"), "UTF-8");
        //请二选一设置

        Goods goods = goodsDao.selectInfo(out_trade_no);
        response.setContentType("text/html;charset=UTF-8");
        if (goods == null) {
            response.getWriter().write("没有该订单号,请确认输入的订单号是否正确");
        } else if (!goods.getTradeNo().equals(trade_no)) {
            response.getWriter().write("没有该交易号,请确认输入的交易号是否正确");
        }else if (goods.getStatus()==null||goods.getStatus().equals("交易成功")){
            if (goods.getStatus()==null)goodsDao.updateGoods(out_trade_no,"交易成功");
            response.getWriter().write("交易成功<br>" +
                    "订单号："+out_trade_no+"<br>"+
                    "交易号："+trade_no);
        }else if (goods.getStatus().equals("退款申请中")){
            response.getWriter().write("退款申请中<br>" +
                    "订单号："+out_trade_no+"<br>"+
                    "交易号："+trade_no);
        } else if (goods.getStatus().equals("已退款")){
            response.getWriter().write("已退款<br>" +
                    "订单号："+out_trade_no+"<br>"+
                    "交易号："+trade_no);
        }else if (goods.getStatus().equals("交易关闭")){
            response.getWriter().write("交易关闭<br>" +
                    "订单号："+out_trade_no+"<br>"+
                    "交易号："+trade_no);
        }else {
            response.getWriter().write("系统异常");
        }

        return null;
    }

    @RequestMapping("trade_refund")
    @ResponseBody
    public String tradeRefund(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //设置请求参数
       // AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTRout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTRtrade_no").getBytes("ISO-8859-1"),"UTF-8");
        //请二选一设置
        //需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = new String(request.getParameter("WIDTRrefund_amount").getBytes("ISO-8859-1"),"UTF-8");
        //退款的原因说明
        String refund_reason = new String(request.getParameter("WIDTRrefund_reason").getBytes("ISO-8859-1"),"UTF-8");
        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = new String(request.getParameter("WIDTRout_request_no").getBytes("ISO-8859-1"),"UTF-8");

        if (out_trade_no!=null || trade_no!=null) {
            refundDao.addRefund(out_trade_no, trade_no, refund_amount, refund_reason, out_request_no);
            goodsDao.updateGoods(out_trade_no,"退款申请中");
        }

        //输出
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("退款申请中，请等待。。。。。。");
        return null;
    }

    @RequestMapping("refundApply")
    public String showRefund(Model model){
        List<TradeRefund> lists = refundDao.getRefundInfo();
        model.addAttribute("lists",lists);
        return "refundApply";
    }

    @RequestMapping(value = {"refundConfirm"},
                    method = {RequestMethod.POST},
                    produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public void refundConfirm(TradeRefund tradeRefund,HttpServletResponse response) throws IOException, AlipayApiException {
        String outTradeNo = tradeRefund.getOutTradeNo();
        response.setContentType("text/html;charset=utf-8");
        if (outTradeNo.isEmpty()){
            response.getWriter().write("系统出现异常，未获取到订单号");
        }else if (refundDao.getRefundByOutTradeNo(outTradeNo)==null){
            response.getWriter().write("数据库不存在该号订单");
        }else {
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ tradeRefund.getOutTradeNo() +"\","
                    + "\"trade_no\":\""+ tradeRefund.getTradeNo() +"\","
                    + "\"refund_amount\":\""+ tradeRefund.getRefundAmount() +"\","
                    + "\"refund_reason\":\""+ tradeRefund.getRefundReason() +"\"}");

            //请求
            String result = alipayClient.execute(alipayRequest).getBody();
            response.getWriter().write(result);
            refundDao.deleteRefund(tradeRefund.getOutTradeNo());
            goodsDao.updateGoods(outTradeNo,"已退款");
        }
    }

    @RequestMapping(value = {"refundCancel"},
                    method = {RequestMethod.POST},
                    produces = {"application/json;charset=utf-8"})
    public void refundCancel(TradeRefund tradeRefund,HttpServletResponse response) throws IOException {
        refundDao.deleteRefund(tradeRefund.getOutTradeNo());
        goodsDao.updateGoods(tradeRefund.getOutTradeNo(),"交易成功");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("取消退款申请成功");
    }

}

package com.alipay.web;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.config.AlipayConfig;
import com.alipay.dao.GoodsDao;
import com.alipay.enums.AlipayStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.*;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = {"/","index"})
    public String showIndex(){
        return "index";
    }


    @RequestMapping(value = {"trade_pay"},
                    produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public void sendToZFB(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
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
        //String subject = "test";
        //商品描述，可空
        String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();//调用sdk生成表单


        //输出，注意输出格式，错误容易验签失败
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(result);
    }


    @RequestMapping("return_url")
    public String paySuccess(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        //获取请求的map集合
        Map<String,String> params = getRequest(request);

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            goodsDao.insertIntoGoods(out_trade_no,total_amount,trade_no,AlipayStateEnum.PaySuccess.getInfo());

            response.setContentType("text/html; charset=utf-8");
            response.getWriter().println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
        }else {
            response.getWriter().println("验签失败");
        }
        return "return_url";
    }

    @RequestMapping("notify_url")
    @ResponseBody
    public String notifyUrl(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {

        //获取请求的map集合
        Map<String,String> params = getRequest(request);


        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

	    //logger.info("验证结果："+signVerified);
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            /*if(trade_status.equals("TRADE_FINISHED")){
            }else if (trade_status.equals("TRADE_SUCCESS")){
            }*/

           // logger.info("success");
            return "success";

        }else {//验证失败
            /*logger.info("错误码Message:"+new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8"));
            logger.info("fail");*/
            return "fail";
        }
    }


    @RequestMapping("trade_query")
    @ResponseBody
    public void tradeQuery(HttpServletRequest request,HttpServletResponse response) throws IOException {

        //商户订单号，商户网站订单系统中唯一订单号
        String out_trade_no = new String(request.getParameter("WIDTQout_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDTQtrade_no").getBytes("ISO-8859-1"), "UTF-8");
        //请二选一设置

        Goods goods = goodsDao.selectInfo(out_trade_no,trade_no);

        response.setContentType("text/html;charset=UTF-8");
        if (goods == null) {
            response.getWriter().write("数据不存在，请检查订单号或者交易号");
        }else{
            response.getWriter().write(goods.getStatus()+"<br>"
                                        +"订单号："+goods.getOutTradeNo()+"<br>"
                                        +"交易号："+goods.getTradeNo());
        }
    }

    @RequestMapping("trade_refund")
    @ResponseBody
    public void tradeRefund(HttpServletRequest request,HttpServletResponse response) throws IOException{

        response.setContentType("text/html;charset=utf-8");

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


        Goods goods = goodsDao.selectInfo(out_trade_no,trade_no);
        if (goods != null){
            String dbRefundAmount = goods.getTotalAmount();
            if (refund_amount.isEmpty()){
                response.getWriter().println("退款金额不得为空");
            } else if (Double.parseDouble(dbRefundAmount)<Double.parseDouble(refund_amount)){
                response.getWriter().println("退款金额输入有误，请重新输入");
            }else if ((Double.parseDouble(dbRefundAmount)>Double.parseDouble(refund_amount)) && out_request_no.isEmpty()){
                response.getWriter().println("部分退款，未输入退款码");
            }else {
                refundDao.addRefund(out_trade_no, trade_no, refund_amount, refund_reason, out_request_no);
                goodsDao.updateGoods(out_trade_no,trade_no,AlipayStateEnum.RefundApply.getInfo());
                response.getWriter().println("退款申请中，请等待。。。。。。");
            }
        }else {
            response.getWriter().println("请检查订单号或者交易号是否有误");
        }

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

        response.setContentType("text/html;charset=utf-8");
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ tradeRefund.getOutTradeNo() +"\","
                    + "\"trade_no\":\""+ tradeRefund.getTradeNo() +"\","
                    + "\"refund_amount\":\""+ tradeRefund.getRefundAmount() +"\","
                    + "\"refund_reason\":\""+ tradeRefund.getRefundReason() +"\","
                    + "\"out_request_no\":\""+ tradeRefund.getOutRequestNo() + "\"}");

            //请求
            String result = alipayClient.execute(alipayRequest).getBody();
            response.getWriter().write(result);
            if (tradeRefund.getOutRequestNo().isEmpty()){
                refundDao.deleteRefund(tradeRefund.getOutTradeNo(),tradeRefund.getTradeNo());
                goodsDao.updateGoods(tradeRefund.getOutTradeNo(),tradeRefund.getTradeNo(),AlipayStateEnum.RefundSuccess.getInfo());

            }else {
                refundDao.deleteRefund(tradeRefund.getOutTradeNo(),tradeRefund.getTradeNo());
                goodsDao.updateGoods(tradeRefund.getOutTradeNo(),tradeRefund.getTradeNo(),AlipayStateEnum.RefundPartSuccess.getInfo());
            }
    }

    @RequestMapping(value = {"refundCancel"},
                    method = {RequestMethod.POST},
                    produces = {"application/json;charset=utf-8"})
    public void refundCancel(TradeRefund tradeRefund,HttpServletResponse response) throws IOException {
        refundDao.deleteRefund(tradeRefund.getOutTradeNo(),tradeRefund.getTradeNo());
        goodsDao.updateGoods(tradeRefund.getOutTradeNo(),tradeRefund.getTradeNo(),AlipayStateEnum.PaySuccess.getInfo());
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("取消退款申请成功");
    }


    @RequestMapping(value = {"trade.refund.query"})
    @ResponseBody
    public void tradeRefundQuery(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {

        AlipayClient alipayClient =  new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        AlipayTradeFastpayRefundQueryRequest refundQueryRequest = new AlipayTradeFastpayRefundQueryRequest();

        String out_trade_no = new String(request.getParameter("WIDRQout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号
        String trade_no = new String(request.getParameter("WIDRQtrade_no").getBytes("ISO-8859-1"),"UTF-8");

        //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
        String out_request_no = new String(request.getParameter("WIDRQout_request_no").getBytes("ISO-8859-1"),"UTF-8");

        refundQueryRequest.setBizContent("{\"out_trade_no\":\""+out_trade_no+"\","
                                        + "\"trade_no\":\""+trade_no+"\","
                                        + "\"out_request_no\":\""+out_request_no+"\"}");

        String result = alipayClient.execute(refundQueryRequest).getBody();
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(result);
    }


    @RequestMapping("alipay.trade.close")
    public String tradeClose(){
        return "alipay.trade.close";
    }


    public Map<String,String> getRequest(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        for (Iterator<String> iter = request.getParameterMap().keySet().iterator();iter.hasNext();){
            String name = (String) iter.next();
            String[] value = request.getParameterMap().get(name);
            String valStr = "";
            for (int i = 0;i<value.length;i++){
                valStr = (i == value.length - 1) ? valStr + value[i] : valStr + value[i] + ",";
            }
            map.put(name,valStr);
        }
        return map;
    }

}

package com.alipay.entity;

public class Goods {
    //订单号
    private String outTradeNo;
    //付款金额
    private String totalAmount;
    //交易号
    private String tradeNo;
    //订单名称
    private String subject;
    //商品描述
    private String body;

    @Override
    public String toString() {
        return "Goods{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

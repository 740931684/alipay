package com.alipay.enums;

import com.alipay.entity.Goods;

public enum AlipayStateEnum {
    PaySuccess("交易成功"),
    PayClossing("交易关闭"),
    RefundApply("退款申请中"),
    RefundSuccess("全额退款成功"),
    RefundPartSuccess("部分退款成功"),
    RefundFail("退款失败");


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private String info;


    AlipayStateEnum(String info){
        this.info = info;
    }

}

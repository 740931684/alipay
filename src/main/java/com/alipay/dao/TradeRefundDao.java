package com.alipay.dao;

import com.alipay.entity.TradeRefund;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TradeRefundDao {
    int addRefund(@Param("outTradeNo")String outTradeNo,
                  @Param("tradeNo")String tradeNo,
                  @Param("refundAmount")String refundAmount,
                  @Param("refundReason")String refundReason,
                  @Param("outRequestNo")String outRequestNo);

    int deleteRefund(@Param("outTradeNo")String outTradeNo);


    List<TradeRefund> getRefundInfo();
}

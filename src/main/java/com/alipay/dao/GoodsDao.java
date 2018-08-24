package com.alipay.dao;

import com.alipay.entity.Goods;
import org.apache.ibatis.annotations.Param;

public interface GoodsDao {
    int insertIntoGoods(@Param("outTradeNo") String outTradeNo,
                        @Param("totalAmount")String totalAmount,
                        @Param("tradeNo")String tradeNo,
                        @Param("body")String body);


    Goods selectInfo(@Param("outTradeNo") String outTradeNo);

    int updateGoods(@Param("outTradeNo")String outTradeNo,
                    @Param("status")String status);
}

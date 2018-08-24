package com.alipay.dao;

import org.apache.ibatis.annotations.Param;

public interface GoodsDao {
    int insertIntoGoods(@Param("out_trade_no") String out_trade_no,
                        @Param("total_amount") String total_amount,
                        @Param("subject") String subject,
                        @Param("body") String body);
    int updateGoods(@Param("out_trade_no") String out_trade_no,
                    @Param("trade_no") String trade_no);
}

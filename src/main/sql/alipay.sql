--创建数据库
create database alipaydb;

use alipaydb;


--创建商品表
create table goods(
out_trade_no varchar(30) comment '订单号',
total_amount varchar(30) comment '付款金额',
trade_no varchar(30) comment '交易号',
subject varchar(30) comment '订单名称',
private varchar(100) comment '商品描述'
);
package com.alipay.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring/spring-*.xml")
public class GoodsDaoTest {

   // @Autowired
    //GoodsDao goodsDao;


    @Test
    public void testGoods() {
        //  goodsDao.insertIntoGoods("1341654654","100","苹果","");
        char[] c = {'a','b','c','d','e'};
        System.out.println(String.valueOf(c,0,4));

    }
}
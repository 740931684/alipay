package com.alipay.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-*.xml")
public class GoodsDaoTest {

    @Autowired
    GoodsDao goodsDao;


    @Test
    public void testGoods(){
      //  goodsDao.insertIntoGoods("1341654654","100","苹果","");
<<<<<<< HEAD
       // goodsDao.updateGoods("1341654654","65486813");
=======
>>>>>>> de26ee6f7d3113b09e448f314378281e5611c83b
    }

}
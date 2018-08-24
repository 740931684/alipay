package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016091700535609";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEtbEXoK5y36ny2ca7ddj2GaDk/fn/tuV1amwUjbXfg0YM77WChkH3kgj4HxBLZRchCxlu+0w+HESLd8an7GOrm7WNTEqQ242PIMi2apVMgEhsZmcRddIxI7zPq2MIVXPvd/tP0YWLshrXGjGiTZ1dwdY1wvwHFSHe8SARoZHfGN/zMVkSWMgFMXMSLcRzGaCZ4aZ0OBVXG/Yu6etwUIQKPt37oYrF3cUj8eH7VGLJOr9UN0VMxdYAL89uBuFql84m8REjIK13Xzjq1zS3/hlsREXOdtSflhIvQAwWMKhRoh52fGqIt+bb4PFgk9eqqGnUc6GhGIw/RTVxVNrYFoCZAgMBAAECggEBAJGUPenUmwadDPOTxIY740PPF8ohRIHBY/QPvMlBT1aJmrA8P0LIBc4pH8YxhyelOwbTZhFCndxho0DsdfMU+XapmCVoTs8CDbPdeW23V6L0VaUT7Dd50QNZBYVTu8kRJOoXC8C4uXL3bvG+ivkpI8udhuDm2wqK0zRhk3A4+PH387AkY/GyxUsTolIckl2XXJhTO5ZvGvS40r5KXnSwndo9zotQhKriSRq1769pGMtMDhP3aJc9/Mmyr9zeUzln9h3m06rgIpGDHrq06fLUfreeWt8rB+NLFqwU+TOBnb2qCAU9b138x4FaqqIe+FY+4KS1GvwAtNp5HWeGgQNI3hkCgYEA8muP0oA70m8ySpevCIXlVEUX+TuRFOAHWlJZSf0vE4vN+oejJZJVXv1cg9yOdNV1L0Eq6i1r6vDc6vFysJR++MygV11plulIePb7fIoth3rQw+rble9W+Aq73LQ6fnZyT7eKGOJlvL+N73R02fgWj+XuzHivTuaQ3NQGkLryb2MCgYEAz7qeB/Vb/wC/USczj3Z1UuNnY0FbjIFn274CXtg7nbkVNAM54UWx8tQs/uqsgklruMIVnGC709EOHur8YXBUDnvpM+DTIOQ4FdFyXU0qHFiEVNvLmPl4XAdXOBwzksHyqtkOGY6Gv3h1W4/fN9ssW9F1dCvtjtL3oec7WUYUJtMCgYBlSCU1qFfWCpkzav45DGAM/jUO7yElXJgV13PworQpcW6kbkjuaJ38atRdTyfBjnFARfTdFA8TgIB8NFZbih8maa/P8HouD9c0mtPla4GsKdbRE+2cTfvC9BEbG72YmnoV7jPnQ6eH3Ww6kChF7Q3YHxPPH9j2mQQLDxaTH1gsywKBgE3Hp4J9WYv64KfnUMQH69g0c7LTdw7+KUA7PjH7Lx9YfNrz1klGSmINFigBAMTr7tzIN0VNZybhoTLsc8GcSKz/sosNRKCNM37f5L2QthLVBf+sKOjHiEyVJxBIZvsticp06XWiS0wIM5EsJr2IYGyk2iC8jf+reLNznGJJ/iOnAoGAbuT2RrlyZjj+LzLSSEjoGm5K70wbRNkiFlgl+rXSa+N9rBKAJawTj3Pp8ZWtKJZEzK0ETJhxGp80DjuPBMGW6cgBoCFhZnHWaTMt9YupRg5jWCU/NXr39ucTbpPYSKyUXznvSfwNaRC+MhEY9Jla6iYXwH/rrygiDjgOo5At5ZU=";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArDUp4KK4INQRKYd3HT4dFDO/s8qXmcCiWoiQvRNdqljfDIjNo3cAfn6xTg1hl7kC1VxV7YO9u4TR54R9sMrETgH5yhW8wnK764ATx7ojNhUzTuOzVSTB+jcBlOBCf++Q0TLyUUDKzQi71ASf39v9u/eoiTKePbw3AAAFLB2NI2rrjGjki3gjWoV8uftTpxQ1vynigfAxI32HfXHZAz+3NBzO1+Iz4+2Lgobm5WOGm6LU+7YU0Am9lI1eTP5NJTBKS756pho3G1zNngVpS00CufnbBMQpvxvSc3GxLAw0e0HsAxU4mz7GvMePojJnKHFjANoI2WKxUoywBwa4onoOIQIDAQAB";

	public static String notify_url = "http://193.112.64.192/alipay/notify_url.jsp";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://localhost:8080/return_url";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "D:\\logger";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


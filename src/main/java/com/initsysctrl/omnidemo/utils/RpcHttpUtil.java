package com.initsysctrl.omnidemo.utils;

import com.alibaba.fastjson.JSON;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.initsysctrl.omnidemo.dto.BaseRPCresponse;
import com.initsysctrl.omnidemo.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: yepeng
 * @create: 2018-09-13 12:16
 **/
@Slf4j
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class RpcHttpUtil {
    private static final String RPC_HOST = "http://127.0.0.1";
//    private static final String RPC_HOST = "http://47.52.103.44";
    private static final String RPC_PORT = "8332";
    private static final String URL = RPC_HOST + ":" + RPC_PORT;
    private JsonRpcHttpClient mClient;
    private RestTemplate mClient2;
    private String RPC_USER = "usdtuser";
    private String RPC_PASSWORD = "rpc!@12abc";

    public RpcHttpUtil() {

        String cred = Base64.encodeBase64String((RPC_USER + ":" + RPC_PASSWORD).getBytes());
        log.error(RPC_USER + ":" + RPC_PASSWORD);
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", "Basic " + cred);
        try {
            this.mClient = new JsonRpcHttpClient(new URL(URL), headers);
            log.error(URL);
            log.error(mClient.getHeaders().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    /***
     * 网络请求和数据处理
     * @param: [method, var] 方法&参数
     * @return: java.lang.String 结果
    //     **/
    public String engine(@NotNull String method, Object... var) {
        return engine(method, String.class, var);
    }

    public String engine22(@NotNull String method, Object... var) {
        return engine2(method, String.class, var);
    }

    public <T> T engine2(@NotNull String method, Class<T> clazz, Object... var) {
        try {
            return mClient.invoke(method, var, clazz);
        } catch (Throwable throwable) {

            String message = throwable.getMessage();
            Throwable cause = throwable.getCause();
         return null;
        }
    }

    public <T> T engine(@NotNull String method, Class<T> clazz, Object... var) {
        String temp = method;
        if(method.equals("omni_listblocktransactions2")){
            method = "omni_listblocktransactions";

        }
        try {
            return mClient.invoke(method, var, clazz);
        } catch (Throwable throwable) {

            String message = throwable.getMessage();
            Throwable cause = throwable.getCause();
            log.error(message == null ? "error message=null" : "error message111=" + message);
            log.error(cause == null ? "error cause=null" : "error cause=" + cause.getMessage());
            log.error(throwable == null ? "error message=null" : "error message=" + throwable.getMessage());

            try {
                BaseRPCresponse res = JSON.parseObject(message, BaseRPCresponse.class);
                BaseRPCresponse.ErrorBean error = res.getError();
               if(temp.equals("omni_listblocktransactions2")){

                   return null;
               }
                throw new BaseException(String.valueOf(error.getCode()), error.getMessage());

            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException("W000", e.getMessage());
            }
        }
    }

}

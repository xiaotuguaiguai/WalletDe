package com.initsysctrl.omnidemo.utils;

import com.alibaba.fastjson.JSON;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.initsysctrl.omnidemo.dto.BaseRPCresponse;
import com.initsysctrl.omnidemo.dto.BaseRpcReq;
import com.initsysctrl.omnidemo.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @package: com.leazxl.bs.uitls
 * @description:
 * @author: yepeng
 * @create: 2018-09-13 12:16
 **/
@Slf4j
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class RpcHttpUtil {
    private static final String RPC_HOST = "your wallet host,eg:199.9.9.9";
    private static final String RPC_PORT = "your wallet port,eg:9527";
    private static final String URL = RPC_HOST + ":" + RPC_PORT;
    private JsonRpcHttpClient mClient;
    private RestTemplate mClient2;
    private String RPC_USER = "your prc user";
    private String RPC_PASSWORD = "you are rpc password ";

    public RpcHttpUtil() {


        //方式1
        String cred = Base64.encodeBase64String((RPC_USER + ":" + RPC_PASSWORD).getBytes());
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", "Basic " + cred);
        try {
            this.mClient = new JsonRpcHttpClient(new URL(URL), headers);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//
//        //方式二
//        this.mClient2 = new RestTemplateBuilder()
//                .basicAuthorization(RPC_USER, RPC_PASSWORD)
//                .rootUri(URL)
//                .build();
    }


    /***
     * 网络请求和数据处理
     * @param: [method, var] 方法&参数
     * @return: java.lang.String 结果
    //     **/
    public String engine(@NotNull String method, Object... var) {
        return engine(method, String.class, var);
    }

    public <T> T engine(@NotNull String method, Class<T> clazz, Object... var) {
        try {

            return mClient.invoke(method, var, clazz);
        } catch (Throwable throwable) {
            String message = throwable.getMessage();
            Throwable cause = throwable.getCause();
            log.error("error cause=" + cause.getMessage());
            log.error("error message=" + throwable.getMessage());
            try {
                BaseRPCresponse res = JSON.parseObject(message, BaseRPCresponse.class);
                BaseRPCresponse.ErrorBean error = res.getError();
                throw new BaseException(String.valueOf(error.getCode()), error.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new BaseException("W000", e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> BaseRPCresponse<T> engine2(@NotNull String method) {
        try {
            BaseRpcReq baseRpcReq = BaseRpcReq.create(method);
            return mClient2.postForObject(URL, baseRpcReq, BaseRPCresponse.class);
        } catch (Throwable throwable) {
            throw new BaseException(throwable.getMessage());
        }
    }

//    @SuppressWarnings("unchecked")
//    public <T> BaseRPCresponse<T> engine2(@NotNull String method, Object... var) {
//        try {
//            BaseRpcReq baseRpcReq = BaseRpcReq.create(method, var);
//            return mClient2.postForObject(URL, baseRpcReq, BaseRPCresponse.class);
//        } catch (Throwable throwable) {
//            log.error("throwable message=" + throwable.getMessage());
//            log.error(throwable.getCause() == null ? "null" : throwable.getCause().getMessage());
//            throw new BaseException(throwable.getMessage());
//        }
//    }

}

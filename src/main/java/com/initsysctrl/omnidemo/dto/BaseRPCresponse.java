package com.initsysctrl.omnidemo.dto;

import lombok.Data;

@Data
public class BaseRPCresponse<T> {
    /**
     * result : null
     * error : {"code":-32601,"message":"Method not found"}
     * id : 623910162827836658
     */
    private T result;
    private ErrorBean error;
    private String id;
    @Data
    public static class ErrorBean {
        /**
         * code : -32601
         * message : Method not found
         */
        private int code;
        private String message;

    }
}

package com.initsysctrl.omnidemo.exception;


public enum E {
    UNKNOWN_ERROR("W-1", "未知异常"),


    ADDRESS_ERROR("W9001", "地址格式错误"),


    AMOUNT_INPUT_EEROR("W9017", "数值输入错误");


    private String code;
    private String message;


    E(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

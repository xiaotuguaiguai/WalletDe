package com.initsysctrl.omnidemo.exception;

/**
 * @package: com.leazxl.bs.enums
 * @description: 钱包 异常-类型列表
 * @author: yepeng
 * @create: 2018-08-06 14:27
 **/
public enum E {
    UNKNOWN_ERROR("W-1", "未知异常"),

    WEB3J_ERROR("W9000", "服务端区块同步异常"),

    ADDRESS_ERROR("W9001", "地址格式错误"),

    PASSWORD_ERROR("W9002", "密码错误"),

    BLANCE_ENOUTH_ERROR("W9003", "账户余额不足"),

    GAS_LIMITE("W9004", "gas燃料费不足"),

    ETH_WALLET_FILE_ERROR("W9005", "keystore文件错误:无法解析文件"),

    NONSUPPORT_COIN_ERROR("W9006", "暂不支持的币种"),

    INVALID_PASSWORD_PROVIDE("W9008", "keystore错误:密码错误"),

    UNABLE_TO_DESERIALIZE_PARAMS("W9009", "keystore错误:无法解析参数"),

    WALLET_VERSION_IS_NOT_SUPPORTED("W9010", "keystore错误:不支持的钱包版本"),

    WALLET_CIPHER_IS_NOT_SUPPORTED("W9011", "keystore错误:不支持的秘钥"),

    KDF_TYPE_IS_NOT_SUPPORTED("W9012", "keystore错误:不支持的加密方式,仅支持AES_128_CTR"),

    NON_DECIMAL_WEI_VALUE_PROVIDED("W9013", "参数错误：不支持的转账金额"),

    SEND_TRANSFER_ERROR("W9014", "区块网络错误：交易发送失败"),

    GAS_PRICE_GET_ERROR("W9015", "区块网络错误：获取gas price失败"),

    TOKEN_ID_ERROR("W9016", "令牌ID错误"),

    AMOUNT_INPUT_EEROR("W9017", "数值输入错误"),

    FEE_NOT_ENOUGH("W9018", "手续费不足");


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

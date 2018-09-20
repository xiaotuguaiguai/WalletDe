package com.initsysctrl.omnidemo.exception;

/**
 * @package: com.initsysctrl.omnidemo.exception
 * @description:
 * @author: yepeng
 * @create: 2018-09-11 16:28
 **/
public class BaseException extends RuntimeException {
    enum CoreRetCodeEnum {
        RET_CODE_000000("A001", "成功"),
        RET_CODE_999999("A999", "异常");

        private String retErrCode;
        private String retErrMessage;

        private CoreRetCodeEnum(String errCode, String errMessage) {
            this.retErrCode = errCode;
            this.retErrMessage = errMessage;
        }

        public String getRetErrCode() {
            return this.retErrCode;
        }

        public void setRetErrCode(String retErrCode) {
            this.retErrCode = retErrCode;
        }

        public String getRetErrMessage() {
            return this.retErrMessage;
        }

        public void setRetErrMessage(String retErrMessage) {
            this.retErrMessage = retErrMessage;
        }
    }

    private static final long serialVersionUID = 7833283455112352655L;
    private String errCode;

    public BaseException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getErrCode() {
        return this.errCode;
    }

    public static BaseException error(String errCode, String errMsg) {
        return new BaseException(errCode, errMsg);
    }

    public static BaseException error(String errMsg) {
        return new BaseException(CoreRetCodeEnum.RET_CODE_999999.getRetErrCode(), errMsg);
    }
}

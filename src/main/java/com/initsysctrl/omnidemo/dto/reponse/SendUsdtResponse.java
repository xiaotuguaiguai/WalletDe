package com.initsysctrl.omnidemo.dto.reponse;

/**
 * Created by quanlun on 2020/4/6.
 */
public class SendUsdtResponse {
    private int status;
    private String msg;

    private String hash;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

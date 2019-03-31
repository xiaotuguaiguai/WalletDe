package com.initsysctrl.omnidemo.dto;

import lombok.Data;

/**
 * @package: com.leazxl.bs.bean
 * @description:
 * @author: yepeng
 * @create: 2018-10-19 17:39
 **/
@Data
public class BaseUtxo {
    String txid;
    int vout;

    public BaseUtxo(String txid, int vout) {
        this.txid = txid;
        this.vout = vout;
    }

    @Override
    public String toString() {
        return "BaseUtxo{" +
                "txid='" + txid + '\n' +
                "vout=" + vout +
                '}' + '\n';
    }

    public BaseUtxo() {
    }
}

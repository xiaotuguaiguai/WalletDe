package com.initsysctrl.omnidemo.dto;

import lombok.Data;

/**
 * @package: com.leazxl.bs.bean
 * @description:
 * @author: yepeng
 * @create: 2018-10-22 10:44
 **/
@Data
public class SimpleUtxo extends BaseUtxo {
    String scriptPubKey;
    double value;

    public SimpleUtxo(String txid, int vout, String scriptPubKey, double value) {
        super(txid, vout);
        this.scriptPubKey = scriptPubKey;
        this.value = value;
    }
}

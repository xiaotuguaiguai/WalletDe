package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-09-17 17:44
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnspentRes {


    /**
     * txid : fe90733870abaf5cf6ca2a72171fa1e976470a3098c20d0d34892bad1a378a05
     * vout : 0
     * address : mpaumxor659PhoJhXp1VCVHVwbFCZSRmuf
     * account : yepeng
     * scriptPubKey : 76a9146378b17c611612a678ea0db33cb20a1941a5188588ac
     * amount : 0.55275278
     * confirmations : 825
     * spendable : true
     * solvable : true
     */

    private String txid;
    private int vout;
    private String address;
    private String account;
    private String scriptPubKey;
    private double amount;
    private int confirmations;
    private boolean spendable;
    private boolean solvable;

    @Override
    public String toString() {
        return "UnspentRes{" + '\n' +
                "txid='" + txid + '\n' +
                "vout=" + vout +
                "address='" + address + '\n' +
                "account='" + account + '\n' +
                "scriptPubKey='" + scriptPubKey + '\n' +
                "amount=" + amount + '\n' +
                "confirmations=" + confirmations + '\n' +
                "spendable=" + spendable + '\n' +
                "solvable=" + solvable + '\n' +
                '}' + '\n';
    }
}

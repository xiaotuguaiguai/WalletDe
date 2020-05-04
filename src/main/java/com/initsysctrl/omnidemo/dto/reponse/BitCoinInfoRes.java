package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @package: com.leazxl.bs.bean
 * @description:
 * @author: yepeng
 * @create: 2018-09-05 17:57
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitCoinInfoRes {

    /**
     * version : 130200
     * protocolversion : 70015
     * walletversion : 130000
     * balance : 0.71967596
     * blocks : 1412389
     * timeoffset : 0
     * connections : 8
     * proxy :
     * difficulty : 1
     * testnet : true
     * keypoololdest : 1535371434
     * keypoolsize : 100
     * paytxfee : 5.0E-5
     * relayfee : 5.0E-5
     * errors : Warning: unknown new rules activated (versionbit 28)
     */

    private int version;
    private int protocolversion;
    private int walletversion;
    private double balance;
    private int blocks;
    private int timeoffset;
    private int connections;
    private String proxy;
    private long difficulty;
    private boolean testnet;
    private long keypoololdest;
    private long keypoolsize;
    private double paytxfee;
    private double relayfee;
    private String errors;
}

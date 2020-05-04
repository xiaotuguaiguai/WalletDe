package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-09-04 18:45
 **/

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmniInfoRes {


    /**
     * version : 130200
     * protocolversion : 70015
     * walletversion : 130000
     * balance : 0.569647
     * blocks : 1412262
     * timeoffset : 0
     * connections : 8
     * proxy :
     * difficulty : 1
     * testnet : true
     * keypoololdest : 1535371433
     * keypoolsize : 100
     * paytxfee : 5.0E-5
     * relayfee : 5.0E-5
     * errors : Warning: unknown new rules activated (versionbit 28)
     */
//
    private int version;
    private int protocolversion;
    private int walletversion;
    private double balance;
    private int blocks;
    private int timeoffset;
    private int connections;
    private String proxy;
    private int difficulty;
    private boolean testnet;
    private int keypoololdest;
    private int keypoolsize;
    private double paytxfee;
    private double relayfee;
    private String errors;

    private String mastercoreversion;
    private long totaltransactions;
    private long blocktime;
    private long blocktransactions;
    private long totaltrades;
    private String omnicoreversion;
    private String bitcoincoreversion;
    private int omnicoreversion_int;
    private long block;
    private List<?> alerts;


}

package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:比特币地址信息
 * @author: yepeng
 * @create: 2018-09-19 21:54
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BtcAddressInfo {


    /**
     * address : mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2
     * account : account2
     * amount : 1.37062639
     * confirmations : 1016
     * label : account2
     * txids : ["d8c4935a32796de29aad522fcd4e460f796ecf520cd50a3a36c28ce3c2e95130",
     * "d5ef21cd7430d603774e063a70707aeb241e080e475733b3eed5f8ac0f388f9b"]
     */

    private String address;
    private String account;
    private double amount;
    private long confirmations;
    private String label;
    private List<String> txids;


}

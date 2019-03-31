package com.initsysctrl.omnidemo.dto.reponse;

import lombok.Data;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-09-05 18:00
 **/


@Data
public class OmniPropertyInfoRes {


    /**
     * propertyid : 1
     * name : Omni
     * category : N/A
     * subcategory : N/A
     * data : Omni serve as the binding between Bitcoin, smart properties and contracts created on the Omni Layer.
     * url : http://www.omnilayer.org
     * divisible : true
     * issuer : mpexoDuSkGGqvqrkrjiFng38QPkJQVFyqv
     * creationtxid : 0000000000000000000000000000000000000000000000000000000000000000
     * fixedissuance : false
     * managedissuance : false
     * totaltokens : 221707.23801701
     */

    private long propertyid;
    private String name;
    private String category;
    private String subcategory;
    private String data;
    private String url;
    private boolean divisible;
    private String issuer;
    private String creationtxid;
    private boolean fixedissuance;
    private boolean managedissuance;
    private String totaltokens;

}

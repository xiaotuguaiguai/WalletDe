package com.initsysctrl.omnidemo.dto.reponse;

import lombok.Data;

import java.util.List;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-09-05 18:20
 **/
@Data
public class OmniTokenBalWithAddressRes {


    /**
     * address : mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC
     * balances : [{"propertyid":1,"name":"Omni","balance":"10.00000000","reserved":"0.00000000","frozen":"0.00000000"},{"propertyid":2,"name":"Test Omni","balance":"10.00000000","reserved":"0.00000000","frozen":"0.00000000"}]
     */

    private String address;
    private List<BalancesBean> balances;

    @Data
    public static class BalancesBean {
        /**
         * propertyid : 1
         * name : Omni
         * balance : 10.00000000
         * reserved : 0.00000000
         * frozen : 0.00000000
         */

        private long propertyid;
        private String name;
        private String balance;
        private String reserved;
        private String frozen;

    }
}

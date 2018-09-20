package com.initsysctrl.omnidemo.dto.response;

import lombok.Data;

/**
 * @description:
 * @author: yepeng
 * @create: 2018-09-05 18:07
 **/
@Data
public class OmniTokenBalanceInfoRes {
    /**
     * 根据地址和id共同查询的结果
     */
//            "balance": "10.00000000",
//            "reserved": "0.00000000",
//            "frozen": "0.00000000"


    /**
     * 仅仅根据地址查询的结果 list
     */
//                "propertyid":2,
//                "name":"Test Omni",
//                "balance":"10.00000000",
//                "reserved":"0.00000000",
//                "frozen":"0.00000000"
    /**
     * 仅仅根据id查询的结果 list
     */
//            "address": "mrAVAPxdQEZxFkunh56skB6sgJa6vrfrpo",
//             "balance": "80000",
//             "reserved": "0",
//             "frozen": "0"


    private String address;
    private int propertyid;
    private String balance;
    private String reserved;
    private String frozen;


}

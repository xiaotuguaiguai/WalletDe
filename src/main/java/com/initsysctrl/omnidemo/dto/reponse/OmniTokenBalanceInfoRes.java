package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-09-05 18:07
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private long propertyid;
    private String balance;
    private String reserved;
    private String frozen;
    private String name;


    public double getValueDoub() {
        if (balance == null || balance.isEmpty()) return 0;
        return Double.valueOf(balance);
    }

    public BigDecimal getValueBigd() {
        if (balance == null || balance.isEmpty()) return new BigDecimal("0");
        return new BigDecimal(balance);
    }
}

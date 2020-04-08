package com.initsysctrl.omnidemo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalanceInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public class AssetController {
    @Autowired
    OmniCoreDao omniCoreDao;

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/getBalanceU")
    public String getBalanceUsdt(@RequestParam String address) {
        OmniTokenBalanceInfoRes omniTokenBalanceInfoRes = omniCoreDao.getBalanceByAddAndId(address, 1);
        String str = JSONObject.toJSONString(omniTokenBalanceInfoRes);//fastjson默认转换是不序列化null值对应的key的
        return str;
    }

    @RequestMapping("/getUTransantList")
    public String getUTransantList(@RequestParam String address) {
        if(StringUtils.isEmpty(address)){
            return "error";
        }
        return omniCoreDao.listOmniTransactions(address).toString();
    }

    @RequestMapping("/sendU")
    public String sendU(@RequestParam String toAddress,@RequestParam String num){
        return omniCoreDao.sendOmniToken(
                "mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G",//5.8,0.2
                toAddress,//0
                1,
                new BigDecimal(num),
                "mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G");
    }

}

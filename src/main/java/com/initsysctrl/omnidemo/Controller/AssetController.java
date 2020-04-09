package com.initsysctrl.omnidemo.Controller;

import com.alibaba.fastjson.JSONObject;
import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.ReceiveBean;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalanceInfoRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTransactionRes;
import com.initsysctrl.omnidemo.utils.Const;
import com.initsysctrl.omnidemo.utils.EhcacheUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class AssetController {
    @Autowired
    OmniCoreDao omniCoreDao;


    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/getInfo")
    public String getInfo() {
        String str = JSONObject.toJSONString(omniCoreDao.getOmniInfo().toString());//fastjson默认转换是不序列化null值对应的key的
        return str;
    }

    @RequestMapping("/getNewAddress")
    public String getNewAddress() {
        String str = omniCoreDao.getNewAddress(null);
        return JSONObject.toJSONString(str);
    }

    @RequestMapping("/getOmniTransaction")
    public String getOmniTransaction(@RequestParam String tx) {
        String str = JSONObject.toJSONString(omniCoreDao.getOmniTransaction(tx).toString());//fastjson默认转换是不序列化null值对应的key的
        return str;
    }

    @RequestMapping("/getBalanceByAddAndId")
    public String getBalanceByAddAndId(@RequestParam String address) {
        OmniTokenBalanceInfoRes omniTokenBalanceInfoRes = omniCoreDao.getBalanceByAddAndId(address, 31);
//        String str = JSONObject.toJSONString(omniTokenBalanceInfoRes);//fastjson默认转换是不序列化null值对应的key的
        return omniTokenBalanceInfoRes.toString();
    }

    @RequestMapping("/listOmniTransactions")
    public String listOmniTransactions(@RequestParam String address) {
        if (StringUtils.isEmpty(address)) {
            return "error";
        }
        return omniCoreDao.listOmniTransactions(address).toString();
    }

    @RequestMapping("/listAllMyTransactions")
    public String listAllMyTransactions(@RequestParam String height) {

        List<ReceiveBean> cacheBean = null;
        try {
            cacheBean = (List<ReceiveBean>) EhcacheUtil.getInstance().get("ehcacheGO").get(height).getObjectValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ReceiveBean> beanList = new ArrayList<>();

        if (cacheBean != null && cacheBean.size() != 0) {
            beanList.addAll(cacheBean);
        }
        List<String> mList = omniCoreDao.listBlockTransactions(Long.parseLong(height));
        for (int i = 0; i < mList.size(); i++) {
            ReceiveBean reveiveBean = new ReceiveBean();
            OmniTransactionRes omniTransactionRes = omniCoreDao.getOmniTransaction(mList.get(i));
            if (omniTransactionRes.isValid() && omniTransactionRes.getPropertyid() == 31) {
                if (omniTransactionRes.getReferenceaddress().equals(Const.MY_ADDRESS)) {
                    reveiveBean.setSendAddress(omniTransactionRes.getReferenceaddress());
                    reveiveBean.setSendAmount(omniTransactionRes.getAmount());
                    reveiveBean.setBlockHeight(omniTransactionRes.getBlock() + "");
                    reveiveBean.setTxHash(omniTransactionRes.getBlockhash());
                    beanList.add(reveiveBean);
                }
            }
            insertData(height, beanList);
            insertData(height);
        }
        return "true";
    }

    @RequestMapping("/selectData")
    public String selectData(@RequestParam("height") String height) {
        List<ReceiveBean> bean = (List<ReceiveBean>) EhcacheUtil.getInstance().get("ehcacheGO").get(height).getObjectValue();
        String str = JSONObject.toJSONString(bean);
        return str;
    }

    @RequestMapping("/clearData")
    public String clearData() {
        EhcacheUtil.getInstance().removeAll("ehcacheGO");
        return "true";
    }

    @RequestMapping("/saveData")
    public String saveData() {
        List<ReceiveBean> bean = new ArrayList<>();
        EhcacheUtil.getInstance().put("ehcacheGO", Const.BLOCK_HEIGHT, bean);
        return bean.toString();
    }

    @RequestMapping("/selectHeight")
    public String selectHeight() {
       String height= EhcacheUtil.getInstance().get("ehcacheHeight", "blockHeight")+"";
        return height+"";
    }

    public String insertData(String height, List<ReceiveBean> bean) {
        EhcacheUtil.getInstance().put("ehcacheGO", height + "", bean);
        return bean.toString();
    }

    public void insertData(String height) {
        EhcacheUtil.getInstance().put("ehcacheHeight","blockHeight",height);
    }

    @RequestMapping("/listBlockTransactions")
    public String listBlockTransactions(@RequestParam String height) {
        if (StringUtils.isEmpty(height)) {
            return "error";
        }
        return omniCoreDao.listBlockTransactions(Long.parseLong(height)).toString();
    }

    @RequestMapping("/sendU")
    public String sendU(@RequestParam String toAddress, @RequestParam String num) {
        return omniCoreDao.sendOmniToken(
                "mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G",//5.8,0.2
                toAddress,//0
                1,
                new BigDecimal(num),
                "mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G");
    }

}

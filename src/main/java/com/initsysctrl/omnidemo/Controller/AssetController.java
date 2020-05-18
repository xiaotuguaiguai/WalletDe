package com.initsysctrl.omnidemo.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.ReceiveBean;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalanceInfoRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTransactionRes;
import com.initsysctrl.omnidemo.dto.reponse.SendUsdtResponse;
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
import java.util.Random;

@RestController
@Slf4j
public class AssetController {
    @Autowired
    OmniCoreDao omniCoreDao;




    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/getBBalance")
    public String getBBalance(@RequestParam  String address) {
        return omniCoreDao.getBalance(address);
    }

    @RequestMapping("/getBInfo")
    public String getBInfo(@RequestParam  String address) {
        return omniCoreDao.getAccountByAddress(address);
    }
//1Aqf9HEzsE7FbsWGuW4ZxUYMFT1s5QL3ze
    @RequestMapping("/sendB")
    public String sendB(@RequestParam String fromAddress,@RequestParam String toAddress,@RequestParam  double num) {
        System.out.println("toAddress="+toAddress+" || num="+num);
       return omniCoreDao.sendBtc(fromAddress,toAddress,num);
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
        if(beanList!=null){

        }
        return "true";
    }

    @RequestMapping("/selectData")
    public String selectData() {
        List<ReceiveBean> bean = (List<ReceiveBean>) EhcacheUtil.getInstance().get("ehcacheGO").get("unDealList").getObjectValue();
        String str = JSONObject.toJSONString(bean);
        return str;
    }

//    @RequestMapping("/changeAddress")
//    public String changeAddress(@RequestParam String address) {
//        Const.MY_ADDRESS = address;
//        return Const.MY_ADDRESS ;
//    }

    @RequestMapping("/clearData")
    public String clearData() {
        EhcacheUtil.getInstance().remove("ehcacheGO","unDealList");
        EhcacheUtil.getInstance().remove("ehcacheHeight","blockHeight");
        return "true";
    }

    @RequestMapping("/saveData")
    public String saveData() {
        List<ReceiveBean> bean = new ArrayList<>();
        EhcacheUtil.getInstance().put("ehcacheGO", Const.BLOCK_HEIGHT, bean);
        return bean.toString();
    }

    @RequestMapping("/insertHeight")
    public String insertHeight(@RequestParam String height) {
        EhcacheUtil.getInstance().remove("ehcacheHeight","blockHeight");
        EhcacheUtil.getInstance().put("ehcacheHeight","blockHeight",height);
        Const.BLOCK_HEIGHT_NOW = height;
        return selectHeight();
    }

    @RequestMapping("/selectHeight")
    public String selectHeight() {
       String height= EhcacheUtil.getInstance().get("ehcacheHeight", "blockHeight")+"";
//       if(StringUtils.isEmpty(height)){
//           EhcacheUtil.getInstance().put("ehcacheHeight","blockHeight",Const.BLOCK_HEIGHT_NOW);
//       }
        return height+" || "+Const.BLOCK_HEIGHT_NOW;
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

    @RequestMapping("/validate")
    public String validate(@RequestParam String address){
        return omniCoreDao.validateAddress(address).toString();
    }

    @RequestMapping("/sendU")
    public String sendU(@RequestParam String toAddress, @RequestParam String num) {//1NLm54ri3jCWcndjkY5PVbmWio5ZtUMtsb
        SendUsdtResponse response = new SendUsdtResponse();
        if(!omniCoreDao.validateAddress(toAddress).isvalid){
            response.setMsg("非法地址");
            response.setHash("null");
            response.setStatus(1);
            return JSON.toJSONString(response);
        }

        String res= omniCoreDao.sendOmniToken(
                "1NLm54ri3jCWcndjkY5PVbmWio5ZtUMtsb",//5.8,0.2
                toAddress,//0
                31,
                new BigDecimal(num),
                "1NLm54ri3jCWcndjkY5PVbmWio5ZtUMtsb");

//        String res=getRandomString();
        if(res!=null ){
            response.setMsg("成功");
            response.setHash(res);
            response.setStatus(0);
        }else{
            response.setMsg("失败");
            response.setHash("null");
            response.setStatus(1);
        }
        return JSON.toJSONString(response);
    }


    public static String getRandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<32;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    @RequestMapping("/sendU3")
    public String sendU3(@RequestParam String fromAddress,@RequestParam String toAddress,@RequestParam String feedAddress,  @RequestParam String num) {
        SendUsdtResponse response = new SendUsdtResponse();

        String res= omniCoreDao.sendOmniToken2(
                fromAddress,//5.8,0.2
                toAddress,//0
                31,
                new BigDecimal(num),
                feedAddress);
        if(res!=null ){
            response.setMsg("成功");
            response.setHash(res);
            response.setStatus(0);
        }else{
            response.setMsg("失败");
            response.setHash("null");
            response.setStatus(1);
        }
        return JSON.toJSONString(response);
    }

    @RequestMapping("/listUnSpent")
    public String listUnSpent(@RequestParam String address){
        return omniCoreDao.listUnSpent(address).toString();
    }

    @RequestMapping("/sendU2")
    public String sendU2(@RequestParam String fromAddress, @RequestParam String toAddress, @RequestParam String num) {
        SendUsdtResponse response = new SendUsdtResponse();

        String res= omniCoreDao.sendOmniToken(
                fromAddress,//5.8,0.2
                toAddress,//0
                31,
                new BigDecimal(num),
                fromAddress);
        if(res!=null ){
            response.setMsg("成功");
            response.setHash(res);
            response.setStatus(0);
        }else{
            response.setMsg("失败");
            response.setHash("null");
            response.setStatus(1);
        }
        return JSON.toJSONString(response);
    }

}

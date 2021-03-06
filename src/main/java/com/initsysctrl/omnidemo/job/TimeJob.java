package com.initsysctrl.omnidemo.job;


import com.alibaba.fastjson.JSONObject;
import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.ReceiveBean;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalWithAddressRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalanceInfoRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTransactionRes;
import com.initsysctrl.omnidemo.utils.Const;
import com.initsysctrl.omnidemo.utils.EhcacheUtil;
import com.initsysctrl.omnidemo.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling//可以在启动类上注解也可以在当前文件
@Slf4j
public class TimeJob {

    @Autowired
    OmniCoreDao omniCoreDao;

    @Scheduled(fixedRate = 1000 * 30)
    public void runsecend() {
        String height = null;
        if (!StringUtils.isEmpty(Const.BLOCK_HEIGHT_NOW)) {
            height = Const.BLOCK_HEIGHT_NOW;
           String aa= EhcacheUtil.getInstance().get("ehcacheHeight", "blockHeight")+"" ;
           if(StringUtils.isEmpty(aa) || aa.equals("null")){
                insertData(height);
           }
        } else {
            try {
                height = EhcacheUtil.getInstance().get("ehcacheHeight", "blockHeight") + "";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (StringUtils.isEmpty(height) || height.equals("null")) {
            log.error("block height is null");
        } else {
            height = (Long.parseLong(height) + 1) + "";
            Const.BLOCK_HEIGHT_NOW = height;
            getData(height);
        }

    }

    private void getData(String height) {
        List<ReceiveBean> cacheBean = null;
        try {
            if(EhcacheUtil.getInstance().get("ehcacheGO")!=null &&  EhcacheUtil.getInstance().get("ehcacheGO").get("unDealList")!=null ){
                cacheBean = (List<ReceiveBean>) EhcacheUtil.getInstance().get("ehcacheGO").get("unDealList").getObjectValue();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        List<ReceiveBean> beanList = new ArrayList<>();

        if (cacheBean != null && cacheBean.size() != 0) {
            beanList.addAll(cacheBean);
        }
        System.out.println("height====="+height+"      ||real="+Long.parseLong(height));
        List<String> mList = omniCoreDao.listBlockTransactions2(Long.parseLong(height));

        if (mList != null ) {
            for (int i = 0; i < mList.size(); i++) {
                ReceiveBean reveiveBean = new ReceiveBean();
                OmniTransactionRes omniTransactionRes = omniCoreDao.getOmniTransaction(mList.get(i));
                if (omniTransactionRes.isValid() && omniTransactionRes.getPropertyid() == 31) {

                    if (omniTransactionRes.getReferenceaddress().equals(Const.MY_ADDRESS)) {
                        log.info("入金","address="+omniTransactionRes.getSendingaddress()
                                +" amount="+omniTransactionRes.getAmount()
                                +" height="+omniTransactionRes.getBlock()
                        +" txhash="+omniTransactionRes.getTxid());
                        reveiveBean.setSendAddress(omniTransactionRes.getSendingaddress());
                        reveiveBean.setSendAmount(omniTransactionRes.getAmount());
                        reveiveBean.setBlockHeight(omniTransactionRes.getBlock() + "");
                        reveiveBean.setTxHash(omniTransactionRes.getTxid());
                        beanList.add(reveiveBean);
                    }
                }
            }
            insertData(beanList);
            insertData(height);
            if (beanList != null && beanList.size() != 0) {
                sendInfo(0, beanList);
            }
        }else{
            Const.BLOCK_HEIGHT_NOW=  (Long.parseLong(Const.BLOCK_HEIGHT_NOW)-1)+"";
            System.out.println("======================="+Const.BLOCK_HEIGHT_NOW);
        }
    }

    private void sendInfo(int pos, List<ReceiveBean> mlist) {
        String url = Const.server_host + "restful/queuelog/into/" + mlist.get(pos).getSendAddress();
        RequestBody formBody = new MultipartBody.Builder()
                .addFormDataPart("intoAddress", mlist.get(pos).getSendAddress())
                .addFormDataPart("intoAmount", mlist.get(pos).getSendAmount())
                .addFormDataPart("intoTime", System.currentTimeMillis() / 1000 + "")
                .addFormDataPart("txId", mlist.get(pos).getTxHash())
                .build();

        final Request request = new Request.Builder().url(url).put(formBody).build();
        try (Response response = OkHttpUtils.getInstance().newCall(request).execute()) {
            String result = response.body().string();
            if (!StringUtils.isEmpty(result)) {
                System.out.println(result + "");
                JSONObject serverResBean = com.alibaba.fastjson.JSON.parseObject(result);
                if (serverResBean.getIntValue("code") == 200) {
                    System.out.println("code200");
                    pos = pos + 1;
                    if (pos < mlist.size()) {
                        sendInfo(pos, mlist);
                    } else {
                        setNewCache(mlist);
                    }
                } else {
                    System.out.println("code==" + serverResBean.getIntValue("code"));
                    mlist.get(pos).setFaile(true);
                    pos = pos + 1;
                    if (pos < mlist.size()) {
                        sendInfo(pos, mlist);
                    } else {
                        setNewCache(mlist);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("exception=" + e.getMessage());
            mlist.get(pos).setFaile(true);
            pos = pos + 1;
            if (pos < mlist.size()) {
                sendInfo(pos, mlist);
            } else {
                setNewCache(mlist);
            }
        }

    }

    private void setNewCache(List<ReceiveBean> mlist) {
        List<ReceiveBean> mm = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i).isFaile()) {
                mm.add(mlist.get(i));
            }
        }
        clearData();
        if (mm.size() != 0) {
            insertData(mm);
        }
    }

    private String insertData(List<ReceiveBean> bean) {
        clearData();
        EhcacheUtil.getInstance().put("ehcacheGO", "unDealList", bean);
        return bean.toString();
    }

    private void clearData() {
        EhcacheUtil.getInstance().remove("ehcacheGO", "unDealList");
    }

    private void insertData(String height) {
        log.warn(height);
        EhcacheUtil.getInstance().put("ehcacheHeight", "blockHeight", height);
        Const.BLOCK_HEIGHT_NOW = height;
    }
}

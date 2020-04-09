package com.initsysctrl.omnidemo.job;


import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.ReceiveBean;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalWithAddressRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalanceInfoRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTransactionRes;
import com.initsysctrl.omnidemo.utils.Const;
import com.initsysctrl.omnidemo.utils.EhcacheUtil;
import lombok.extern.slf4j.Slf4j;
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

    @Scheduled(fixedRate = 1000 * 10)
    public void runsecend() {
        String height = null;
        try {
            height = EhcacheUtil.getInstance().get("ehcacheHeight", "blockHeight") + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(height)) {
            height = 600000 + "";
        }
        getData(height);
    }

    private void getData(String height) {
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
        if (mList != null && mList.size() != 0) {
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
        }
    }

    private String insertData(String height, List<ReceiveBean> bean) {
        EhcacheUtil.getInstance().put("ehcacheGO", height + "", bean);
        return bean.toString();
    }

    private void insertData(String height) {
        EhcacheUtil.getInstance().put("ehcacheHeight", "blockHeight", height);
        log.info("height", height);
    }
}

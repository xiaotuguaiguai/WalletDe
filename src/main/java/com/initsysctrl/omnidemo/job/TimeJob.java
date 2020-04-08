package com.initsysctrl.omnidemo.job;


import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalWithAddressRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTokenBalanceInfoRes;
import com.initsysctrl.omnidemo.dto.reponse.OmniTransactionRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@EnableScheduling//可以在启动类上注解也可以在当前文件
@Slf4j
public class TimeJob {

    @Autowired
    OmniCoreDao omniCoreDao;

    @Scheduled(fixedRate = 1000 * 10)
    public void runsecend(){
//        getImportInfo();
    }
    public void getImportInfo() {
        double rechargeSum = 0.0;
        double withdrawSum = 0.0;
        int rechargeNum = 0;
        int withrawNum = 0;
        //1:查询出本钱包所有的交易记录
        List<OmniTransactionRes> allTransactions = omniCoreDao.listOmniTransactions(null);
        //2:查询所有的转入与转出记录

        for (OmniTransactionRes res : allTransactions) {
            if (!res.isValid()) continue;//合法交易
            if (res.getType_int() != 0) continue;//普通资产交易类型
            if (res.getPropertyid() != 31) continue;//限usdt
            String sendingaddress = res.getSendingaddress();
            String referenceaddress = res.getReferenceaddress();
            boolean sendIsMine = omniCoreDao.validateAddress(sendingaddress).ismine;
            boolean referenceIsMine = omniCoreDao.validateAddress(referenceaddress).ismine;

            if (!sendIsMine && referenceIsMine) {
                //充值记录
                String amount = res.getAmount();
                double v = new BigDecimal(amount).doubleValue();

                log.warn(String.format("充值：%s", amount));
            } else if (sendIsMine && !referenceIsMine) {
                //提现记录

//                String amount = res.getAmount();
//                double v = new BigDecimal(amount).doubleValue();
//                withdrawSum += v;
//                withrawNum++;
//                log.warn(String.format("提现：%s", amount));
//            } else {
//            }
            }

            double allUsdtBalance = 0;
            List<OmniTokenBalanceInfoRes> allBalances = omniCoreDao.getAllBalances();
            if (allBalances == null) return;
            for (OmniTokenBalanceInfoRes bean : allBalances) {
                if (bean.getPropertyid() == 31) {
                    allUsdtBalance = Double.valueOf(bean.getBalance());
                }
            }

            log.warn(String.format("累计全部交易记录：%s 条", rechargeNum + withrawNum));
            log.warn(String.format("累计充值记录 %s 条，充值总额 %s USDT ", rechargeNum, rechargeSum));
            log.warn(String.format("累计提现记录 %s 条，提现总额 %s USDT ", withrawNum, withdrawSum));
            log.warn(String.format("USDT总余额：%s", allUsdtBalance));

            int unselec = 0;
            List<OmniTokenBalWithAddressRes> balances = omniCoreDao.getAllBalancesWithAddress();
            for (OmniTokenBalWithAddressRes om : balances) {
                String address = om.getAddress();
                if ("1MvLGyyRtgUS9qTptxLemVapMqw6KmciRa".equals(address)) continue;
                List<OmniTokenBalWithAddressRes.BalancesBean> beans = om.getBalances();
                if (beans == null) continue;
                for (OmniTokenBalWithAddressRes.BalancesBean b : beans) {
                    if (b.getPropertyid() != 31) continue;
                    unselec++;
                    log.warn(String.format("地址 %s 有%s USDT 未进行归集", address, b.getBalance()));
                }
            }
            log.warn(String.format("未归集个数：%s", unselec));
        }

//    /**
//     * 批量转账
//     */
//    @Test
//    public void batchTrade() {
//        String speAddress = "1MvLGyyRtgUS9qTptxLemVapMqw6KmciRa";//归集地址
////        String speAddress = "1MvLGyyRtgUS9qTptxLemVapMqw6KmciRa";//归集地址
//        List<OmniTokenBalWithAddressRes> allBalancesWithAddress = omniCoreDao.getAllBalancesWithAddress();
//        if (allBalancesWithAddress == null) return;
//        ArrayList<String> needhandle = new ArrayList<>();//没有需要手动转账的
//        needhandle.clear();
//        log.warn(String.format("需要进行归集操作的地址个数：%s", allBalancesWithAddress.size()));
//        for (OmniTokenBalWithAddressRes bean : allBalancesWithAddress) {
//            String address = bean.getAddress();
//            if (speAddress.equals(address)) continue;
//            List<OmniTokenBalWithAddressRes.BalancesBean> balances = bean.getBalances();
//            if (address == null) continue;
//            OmniValAddRes omniValAddRes = omniCoreDao.validateAddress(address);
//            if (!omniValAddRes.isvalid) continue;//不合法地址
//            if (!omniValAddRes.ismine) continue;//非私钥地址
//            // get utxo
//            List<UnspentRes> unspentRes = omniCoreDao.listUnSpent(address);
//            if (unspentRes == null || unspentRes.isEmpty()) {
//                needhandle.add(address);
//            }
//            ;
//            //判断usdt资产信息
//            for (OmniTokenBalWithAddressRes.BalancesBean b : balances) {
//                if (b.getPropertyid() != 31) continue;
//                log.warn(String.format("存在用于utxo的待归集地址：%s 待转金额：%s", address, b.getBalance()));
//                String txid = omniCoreDao.sendOmniAll(address, speAddress);
//                log.warn(String.format("已经发送交易信息:" + txid));
//            }
//            break;
//        }
//        for (String a : needhandle) {
//            log.warn(String.format("需要手动归集的地址：%s", a));
//        }
    }
}

package com.initsysctrl.omnidemo;

import com.alibaba.fastjson.JSONObject;
import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import com.initsysctrl.omnidemo.dto.reponse.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WalletDeApplicationTests {

    @Autowired
    OmniCoreDao omniCoreDao;

    /***
     *
     * @param: []
     * @return: void
     **/
    @Test
    public void test0() {
        log.warn(omniCoreDao.getOmniInfo().toString());
        log.warn(omniCoreDao.getOmniInfo().getOmnicoreversion());
//        log.warn(omniCoreDao.getBitCoinInfo().toString());
    }

    //查询 比特余额
    @Test
    public void test1() {
        log.warn(omniCoreDao.getBalance("mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G"));
    }


    //查询比特地址账户
    @Test
    public void test3() {
        log.warn("============================================");
//        log.warn(omniCoreDao.getAccountByAddress("mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G"));
        log.warn(omniCoreDao.getReceivedByAddress("mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G"));

        log.warn("============================================");
    }

    //投硬币，连续抛255次
    @Test
    public void test4() {
        log.warn(omniCoreDao.getNewAddress(null));
    }

    //验证地址信息
    @Test
    public void test5() {
        log.warn(omniCoreDao.validateAddress("mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2").toString());

    }

    //查询令牌信息
    @Test
    public void test6() {
        log.warn(omniCoreDao.getOmniPropertyById(1).toString());
    }

    //查询所有地址上的令牌
    @Test
    public void test7() {
        log.warn(omniCoreDao.getAllBalancesWithAddress().toString());
    }

    //查询令牌总余额
    @Test
    public void test8() {
        log.warn(omniCoreDao.getAllBalances().toString());
    }

    //查询事务列表
    @Test
    public void test9() {
        //全部
        log.warn(omniCoreDao.listOmniTransactions(null).toString());
        log.warn(omniCoreDao.listOmniTransactions(null).get(0).toString());

        //过滤地址
        log.warn(omniCoreDao.listOmniTransactions("mpaumxor659PhoJhXp1VCVHVwbFCZSRmuf").toString());

    }

    //查询 缓冲区中的事务列表
    @Test
    public void test10() {
        log.warn(omniCoreDao.listOmniPendingTransactions().toString());
    }

    @Test
    public void test11a() {
       // log.warn(omniCoreDao.getOmniTransaction("d46f7b612c2cb8ad7bd684b7b2bf225ac40587caf77cfd85cb33a71ab5439438").toString());
//        log.warn(omniCoreDao.listBlockTransactions(600000L).toString());
        OmniTokenBalanceInfoRes omniTokenBalanceInfoRes = omniCoreDao.getBalanceByAddAndId("mhuQvhXABbyjpR26AhjH3y5LvFN1EWDiBj", 1);
        String str = JSONObject.toJSONString(omniTokenBalanceInfoRes);//fastjson默认转换是不序列化null值对应的key的
        log.warn(str);
    }

    //查询 utxo列表
    @Test
    public void test11() {
        //执行这个方法
        List<UnspentRes> res = omniCoreDao.listUnSpent(null);
        //打印
        log.warn(res.toString());
        //获取实体
        log.warn(res.get(0).toString());

    }

    //转账交易令牌，固定手续费地址
    @Test
    public void test12() {
        log.warn(omniCoreDao.sendOmniToken(
                "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2",//5.8,0.2
                "mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf",//0
                1,
                new BigDecimal("0.1"),
                "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2"));//0.94601041,
    }

    //发送地址上的所有令牌
    @Test
    public void test13() {
        log.warn(omniCoreDao.sendOmniTokenAll("from", "to", true, "fee"));
    }

    //备份钱包以 .dat的格式
    @Test
    public void test14() {
        log.warn(omniCoreDao.backupWallet());
    }

    //导出钱包以 .txt的格式
    @Test
    public void test15() {
        log.warn(omniCoreDao.dumpWallet());
    }

    //以原生交易方式发送omni代币
    @Test
    public void test16() {
        String fromaddress = "mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC";
        String toaddress = "muXyxGeeGC5fnbBrFwQJSo4ccibnt1FynJ";
        long pid = 2147484888L;
        BigDecimal amount = new BigDecimal("0.4");
        String feeaddress = "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2";//有btc 3.46631048的余额，没有任何令牌
        log.warn(omniCoreDao.sendOmniTokenRaw(fromaddress, toaddress, pid, amount, feeaddress));
    }

    //发送比特币，支持手续费
    @Test
    public void test17() {
        String fromaddress = "mgmNSxhyBifCfaaoKmvMMVGdpRSpBBA87G";
        String toAddress = "moneyqMan7uh8FqdCA2BV5yZ8qVrc9ikLP";
        Double amount = 0.1;
        log.warn(omniCoreDao.sendBtc(fromaddress, toAddress, amount));
    }

    //批量转btc
    @Test
    public void test18() {
        String fromaddress = "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2";
        HashMap<String, Double> map = new HashMap<>();
        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
//        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
//        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
//        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
//        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
//        map.put("moneyqMan7uh8FqdCA2BV5yZ8qVrc9ikLP", 0.0000055);
//        map.put("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf", 0.0000055);
//        map.put("mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC", 0.0000055);
//        map.put("mm3UmfEUwhHvFBJGycHT4BqPaEwrLRBsKx", 0.0000055);
//        map.put("mpaumxor659PhoJhXp1VCVHVwbFCZSRmuf", 0.0000055);


        String hash = omniCoreDao.sendBtcBatch(fromaddress, map);
        log.warn("比特币交易广播结束：" + hash);
    }

    @Test
    public void test19() {
/*        ArrayList<String> list = new ArrayList<>();
        list.add("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf");
        list.add("mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC");
        list.add("mm3UmfEUwhHvFBJGycHT4BqPaEwrLRBsKx");
        list.add("mpaumxor659PhoJhXp1VCVHVwbFCZSRmuf");
        for (String to : list
        ) {
            log.warn(omniCoreDao.sendOmniToken(
                    "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2",//5.8,0.2
                    to,//0
                    1,
                    20,
                    "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2"));

        }*/

    }

    @Test
    public void test21() {
        for (int x = 0; x < 10; x++) {
            String newAddress = omniCoreDao.getNewAddress(null);
            System.out.print(newAddress + "\n");
        }
    }

    @Test
    public void test22() {
        List<OmniTokenBalanceInfoRes> allBalances = omniCoreDao.getAllBalances();
        if (allBalances == null) return;
        for (OmniTokenBalanceInfoRes bean : allBalances) {
            if (bean.getPropertyid() == 31) {
                bean.getBalance();
            }
        }

    }

    @Test
    public void test23() {
        String fromaddress = "mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf";
        String toaddress = "muXyxGeeGC5fnbBrFwQJSo4ccibnt1FynJ";
        long pid = 1L;
        BigDecimal amount = new BigDecimal("0.5");
        String feeaddress = "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2";//有btc 3.46631048的余额，没有任何令牌

        String raw = omniCoreDao.sendOmniToken(fromaddress, toaddress, pid, amount, feeaddress);
        log.warn("raw txid=" + raw);
    }

//    @Test
//    public void test24() {
//        List<UsdtTransHistory> list = usdtTransRepository.findByCantCollect("mhbDvsb2bVncbQkMMrU1KnM4kMxNJL8xbf");
//        log.warn("list:::::\n" + list.size());
//        for (UsdtTransHistory u : list) {
//            log.warn("" + u.getSendAdd() + "【" + u.getStatus() + "】" + "\n");
//        }
//    }
//
//    @Test
//    public void test25() {
//        int n = 100;
//        String account = null;
//
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < n; i++) {
//            String address = omniCoreDao.getNewAddress(null);
//            String privkey = omniCoreDao.dumpPrivkey(address);
//            UsdtAddress usdtAddress = new UsdtAddress(address, account, privkey);
//            usdtAddressRepositry.save(usdtAddress);
//            System.out.print("new address=" + address + "\n");
//        }
//        long end = System.currentTimeMillis();
//        System.out.print("耗时：" + ((end - start) / 1000) + " s");
//
//    }

    /*
     *查询转入总和，转出总和，以及等待归集地址
     *预计运行10-20min
     *
     */
    @Test
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
                rechargeSum += v;
                rechargeNum++;
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

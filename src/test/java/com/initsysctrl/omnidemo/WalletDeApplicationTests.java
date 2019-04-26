package com.initsysctrl.omnidemo;

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
//        log.warn(omniCoreDao.getOmniInfo().toString());
//        log.warn(omniCoreDao.getOmniInfo().getOmnicoreversion());
        log.warn(omniCoreDao.getBitCoinInfo().toString());
    }

    //查询 比特余额
    @Test
    public void test1() {
        log.warn(omniCoreDao.getBalance(""));
    }


    //查询比特地址账户
    @Test
    public void test3() {
        log.warn(omniCoreDao.getAccountByAddress("mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC"));
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

    //查询 缓冲区中的事务列表，统计未确认交易的类型、id、金额和地址
    @Test
    public void test10() {
        String address = "";//不做地址过滤
        List<OmniTransactionRes> pendingTransactions = omniCoreDao.listOmniPendingTransactions(address);
        if (pendingTransactions == null) {
            log.warn("交易缓冲区为空");
        } else {
            int sum = 0;
            int recharge = 0;
            int withdrawal = 0;
            int internal = 0;
            for (OmniTransactionRes pend : pendingTransactions) {
                if (pend == null) continue;
                boolean ismine = pend.isIsmine();
                if (!ismine) continue;
                if (pend.getPropertyid() != 31) continue;
                if (pend.getType_int() != 0) continue;
                String txid = pend.getTxid();
                String sendingaddress = pend.getSendingaddress();
                String referenceaddress = pend.getReferenceaddress();
                String amount = pend.getAmount();
                boolean sendIsMine = omniCoreDao.validateAddress(sendingaddress).ismine;
                boolean refeIsMine = omniCoreDao.validateAddress(referenceaddress).ismine;
                String type;

                if (sendIsMine) {
                    if (refeIsMine) {
                        type = "内部归集";
                        internal++;
                    } else {
                        type = "提现类型";
                        withdrawal++;
                    }
                } else {
                    if (refeIsMine) {
                        type = "充值类型";
                        recharge++;
                    } else {
                        type = "异常类型";
                    }
                }
                String format = String.format("\n 交易哈希: %s \n 发送者：%s \n 接收者：%s \n 金额: %s \n 类型：%s \n --------- \n", txid, sendingaddress, referenceaddress, amount, type);
                sum++;
                log.warn(format);
            }
            log.warn(String.format("累计未确认交易%s条，其中充值 %s条未确认，提现%s条未确认，内部归集%s条未确认", sum, recharge, withdrawal, internal));
        }
    }

    @Test
    public void test11a() {
        log.warn(omniCoreDao.getOmniTransaction("10e055df8cc920432ff407453238b46f8b6e4198950a879c9bdb3fc870952d7b").toString());
    }

    //查询某个地址下的utxo总和，即btc余额
    @Test
    public void test11() {
        //执行这个方法
        List<UnspentRes> res = omniCoreDao.listUnSpent(null);
        if (res == null || res.isEmpty()) {
            log.warn("0");
        } else {
            double sum = 0.0;
            for (UnspentRes utxo : res) {
                double amount = utxo.getAmount();
                sum += amount;
            }
            log.warn(String.valueOf(sum));
        }
    }

    //转账交易令牌，固定手续费地址
    @Test
    public void test12() {
        log.warn(omniCoreDao.sendOmniToken(
                "发送地址",//5.8,0.2
                "接受地址",//0
                1,
                new BigDecimal("0.1"),
                "手续费地址"));//0.94601041,
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

    //以原生交易方式发送omni代币-废弃
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
        String fromaddress = "mh7tRuxwdJmeYzRai8vm377pi4K3SLTdM2";
        String toAddress = "moneyqMan7uh8FqdCA2BV5yZ8qVrc9ikLP";
        Double amount = 0.1;
        log.warn(omniCoreDao.sendBtc(fromaddress, toAddress, amount));
    }

    //批量转btc
    @Test
    public void test18() {
        String fromaddress = "send";
        HashMap<String, Double> map = new HashMap<>();
        map.put("t01", 0.0000055);
        map.put("to2", 0.0000055);
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

                String amount = res.getAmount();
                double v = new BigDecimal(amount).doubleValue();
                withdrawSum += v;
                withrawNum++;
                log.warn(String.format("提现：%s", amount));
            } else {
            }
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
//    }
// 查询未归集 且 缺少utxo的地址
public void getSpecialAddress() {
    ArrayList<String> specialAddress = new ArrayList<>();
    //遍历出所有的omni 代币地址余额列表
    List<OmniTokenBalWithAddressRes> balances = omniCoreDao.getAllBalancesWithAddress();
    for (OmniTokenBalWithAddressRes om : balances) {
        String address = om.getAddress();//代查询地址
        //查询改地址下是否含有usdt 的代币
        List<OmniTokenBalWithAddressRes.BalancesBean> omBalances = om.getBalances();
        if (omBalances == null || omBalances.isEmpty()) continue;
        for (OmniTokenBalWithAddressRes.BalancesBean ombean : omBalances) {
            if (ombean == null) continue;
            if (ombean.getPropertyid() != 31) continue;//如果代币不是usdt，不处理
            String balance = ombean.getBalance();
            if (Double.valueOf(balance) <= 0) continue;//如果地址下面的usdt小于某个值，就不处理
            //最后查询这个有usdt的未归集地址下面，是否有utxo
            List<UnspentRes> unspentRes = omniCoreDao.listUnSpent(address);
            if (unspentRes == null || unspentRes.isEmpty()) {
                specialAddress.add(address);//将这个缺少utxo的地址添加到集合中
            }
        }
    }
    // 打印或者存储，有USDT，但没有utxo的地址
    for (String speAddress : specialAddress) {
        log.warn(speAddress);
    }
}


}

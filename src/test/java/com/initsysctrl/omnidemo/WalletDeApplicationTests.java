package com.initsysctrl.omnidemo;

import com.initsysctrl.omnidemo.dao.OmniCoreDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WalletDeApplicationTests {
    @Autowired
    OmniCoreDao omniCoreDao;

    @Test
    public void test0() {
        log.warn(omniCoreDao.getOmniInfo().toString());
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
        log.warn(omniCoreDao.getnewaddress(null));
    }

    //验证地址信息
    @Test
    public void test5() {
        log.warn(omniCoreDao.validateAddress("mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC").toString());

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
        log.warn(omniCoreDao.getOmniTransaction("10e055df8cc920432ff407453238b46f8b6e4198950a879c9bdb3fc870952d7b").toString());
    }

    //查询 utxo列表
    @Test
    public void test11() {
        log.warn(omniCoreDao.listUnSpent("").toString());

    }

    //转账交易令牌，固定手续费地址
    @Test
    public void test12() {
        log.warn(omniCoreDao.sendOmniToken("your sender address",
                "your receiver address",
                31,
                0.5,
                "your fee address"));
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



}

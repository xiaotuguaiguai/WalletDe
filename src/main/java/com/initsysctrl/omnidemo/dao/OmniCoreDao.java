package com.initsysctrl.omnidemo.dao;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.initsysctrl.omnidemo.dto.response.*;
import com.initsysctrl.omnidemo.exception.E;
import com.initsysctrl.omnidemo.utils.AssertUp;
import com.initsysctrl.omnidemo.utils.RpcHttpUtil;
import com.sun.istack.internal.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @description: omnicore fork bitcoin
 * @author: yepeng
 * @create: 2018-08-24 11:00
 * @other: Congratulations to LGD for the 2018 dota2 international invitational runner-up ，
 * and hopes to win the championship in 2019
 **/

@Slf4j
@Repository
@SuppressWarnings(value = {"unused", "unchecked", "FieldCanBeLocal"})
public class OmniCoreDao {


    private RpcHttpUtil http;
    private JsonRpcHttpClient mClient;

    public OmniCoreDao() {
        http = new RpcHttpUtil();
    }


    /***
     * 获取比特币底层基本信息
     * @param: []
     * @return: java.lang.String
     **/
    public BitCoinInfoRes getBitCoinInfo() {
        return http.engine("getinfo", BitCoinInfoRes.class);
    }

    /***
     * 验证地址合法性
     * @param: [address]
     * @return: java.lang.String
     **/
    public OmniValAddRes validateAddress(String address) {
        return http.engine("validateaddress", OmniValAddRes.class, address);
    }


    /***
     * 生成新的地址,usdt与bit通用地址
     * @param: [account] 账户名，如果不设置默认为空
     * @return: java.lang.String 地址
     **/
    public String getnewaddress(@Nullable String account) {
        if (StringUtils.isEmpty(account)) {
            return http.engine("getnewaddress", String.class);
        } else {
            return http.engine("getnewaddress", String.class, account);
        }


    }


    /***
     * 获取账户名称下的地址列表
     * @param: [account]
     * @return: java.lang.String
     **/
    public String[] getAddressesByAccName(String accountName) {
        return http.engine("getaddressesbyaccount", String[].class, accountName);
    }


    /***
     * 获取地址对于的账户名称 by address
     * @param: [address]
     * @return: com.leazxl.bs.dto.BaseRPCresponse<java.lang.String>
     **/
    public String getAccountByAddress(String address) {
        AssertUp.isTrue(validateAddress(address).isvalid, E.ADDRESS_ERROR);
        return http.engine("getaccount", String.class, address);
    }


    /***
     * 获取所有账户下的比特币余额，或者指定账户下的余额
     * @param: [account]
     * @return: java.lang.String
     **/
    public String getBalance(@Nullable java.lang.String account) {
        if (StringUtils.isEmpty(account)) {
            return http.engine("getbalance", String.class);
        } else {
            return http.engine("getbalance", account);
        }
    }

    /***
     * 查询指定地址的收款总额（不是余额！不是余额！不是余额）
     * @param:地址
     * @return:收款总额
     **/
    public String getReceivedByAddress(String address) {
        return http.engine("getreceivedbyaddress", address);
    }


    /***
     * 获取UTXo
     * @param: [address]
     * @return: void
     **/
    public List<UnspentRes> listUnSpent(@Nullable java.lang.String address) {

        AssertUp.isTrue(address == null || validateAddress(address).isvalid, E.ADDRESS_ERROR);
        if (StringUtils.isEmpty(address)) {
            return http.engine("listunspent", List.class);
        } else {
            Object[] parms = {1, 999999, new java.lang.String[]{address}};//最小确认，最大确认
            return http.engine("listunspent", List.class, parms);
        }
    }


    /***
     * 获取钱包内比特币地址信息列表
     * @param: []
     * @return: void
     **/
    public List<BtcAddressInfo> listReceivedByAddress() {
        return http.engine("listreceivedbyaddress", List.class, 1, true);
    }


/**************************************omni corl层********************************/

    /***
     * 查询Omni状态信息。
     * @param: []
     * @return: java.lang.String
     **/
    public OmniInfoRes getOmniInfo() {
        return http.engine("omni_getinfo", OmniInfoRes.class);
    }


    /***
     * 查询令牌属性。
     * @param: [propertyid]
     * @return: java.lang.String
     **/
    public OmniPropertyInfoRes getOmniPropertyById(int propertyid) {
        return http.engine("omni_getproperty", OmniPropertyInfoRes.class, propertyid);

    }

    /***
     * 查询令牌余额信息 by 地址&id
     * @param: [address, propertyid]
     * @return: java.lang.String
     **/
    public List<OmniTokenBalanceInfoRes> getBalanceByAddAndId(String address, int propertyid) {

        AssertUp.isTrue(validateAddress(address).isvalid, E.ADDRESS_ERROR);
        return http.engine("omni_getbalance", List.class, address, propertyid);
    }

    /***
     *查询令牌余额信息 by id
     Returns a list of token balances for a given currency or property identifier.
     * @param: [token_id]
     * @return: void
     **/
    @Deprecated
    public List<OmniTokenBalanceInfoRes> getOmniBalanceById(int token_id) {
        return http.engine("omni_getallbalancesforid", List.class, token_id);
    }


    /***
     * 查询令牌余额信息 by 地址
     * @param: [address]
     * @return:
     */
    public List<OmniTokenBalanceInfoRes> getOmniBalanceByAddress(String address) {


        AssertUp.isTrue(validateAddress(address).isvalid, E.ADDRESS_ERROR);

        return http.engine("omni_getallbalancesforaddress", List.class, address);
    }


    /***
     * 查询钱包中所有地址的余额信息
     * @param: []
     * @return: java.lang.String
     **/
    public List<OmniTokenBalWithAddressRes> getAllBalancesWithAddress() {
        return http.engine("omni_getwalletaddressbalances", List.class);
    }

    /***
     * 查询所有令牌的余额总额
     * @param: []
     * @return: java.lang.String
     **/
    public List<OmniTokenBalanceInfoRes> getAllBalances() {
        return http.engine("omni_getwalletbalances", List.class);
    }


    /***
     * (废弃，。0.3开始采用新的api没这个作为备用)
     * 发送指定id的令牌到指定地址
     * @param: [fromaddress, toaddress,接受者地址propertyid,令牌i amount,金额redeemaddress,referenceamount]
     * @return: java.lang.String
     **/
    @Deprecated
    private String sendOmniToken(String fromaddress,
                                 String toaddress,
                                 int propertyid,
                                 String amount,
                                 String redeemaddress,
                                 String referenceamount) {
        return http.engine("omni_send",
                fromaddress,
                toaddress,
                propertyid,
                amount,
                redeemaddress,
                referenceamount);
    }

    /***
     * 优先使用此方法
     * 创建并发送资助的简单发送交易。
     * 来自发件人的所有比特币都被消费，如果缺少比特币，则从指定的费用来源获取。更改将发送到费用来源！
     * @param: [
     * fromaddress 发送地址,
     * toaddress 接收地址,
     * propertyid 令牌id,
     * amount =金额,
     * feeaddress 支付手续费的地址
     * ]
     * @return: java.lang.String
     * https://github.com/OmniLayer/omnicore/blob/master/src/omnicore/doc/rpc-api.md#omni_funded_send
     **/
    public String sendOmniToken(String fromaddress,
                                String toaddress,
                                int propertyid,
                                double amount,
                                String feeaddress) {
        AssertUp.isTrue(validateAddress(feeaddress).isvalid, E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(toaddress).isvalid, E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(fromaddress).isvalid, E.ADDRESS_ERROR);
        AssertUp.isTrue(amount > 0, E.AMOUNT_INPUT_EEROR);

        return http.engine("omni_funded_send", fromaddress, toaddress, propertyid, String.valueOf(amount), feeaddress);
    }


    /***
     * 创建并发送将给定生态系统中的所有可用令牌传输给收件人的事务，全部发送
     * @param:
     * [
     * fromaddress:令牌发送者地址
     * toaddress:令牌接受者地址
     * ecosystem:生态，1-正式生态，2-测试生态
     * feeaddressx:用于支付手续费的地址
     * ]
     * @return: void
     **/
    public String sendOmniTokenAll(String fromaddress, String toaddress, boolean is_main_ecosystem, String feeaddressx) {
        AssertUp.isTrue(validateAddress(feeaddressx).isvalid, E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(toaddress).isvalid, E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(fromaddress).isvalid, E.ADDRESS_ERROR);

        return http.engine("omni_funded_sendall", String.class, fromaddress, toaddress, is_main_ecosystem ? 1 : 2, feeaddressx);
    }


    /***
     * 获取有关Omni事务的详细信息。
     * 【全网】
     * @param: [txid]：需要查找事务的哈希值
     * @return: void
     **/
    public OmniTransactionRes getOmniTransaction(String txid) {
        return http.engine("omni_gettransaction", OmniTransactionRes.class, txid);
    }


    /**
     * 列出钱包事务，可选地按地址和块边界过滤。充值事件在这个方法的返回中
     * List wallet transactions, optionally filtered by an address and block boundaries.
     * 【本机】
     * 不建议直接使用这个方法
     *
     * @param: String txid      可选的	地址过滤（默认："*"）
     * int count	    可选的	显示最多n事务（默认：10）
     * skip             可选的	跳过第n个事务（默认：0）
     * int startblock	可选的	第一个块开始搜索（默认：0）
     * int endblock;    可选的 最后一个块中搜索包括（默认值：999999999）
     * @return: java.lang.String
     **/
    @Deprecated
    public List<OmniTransactionRes> listOmniTransactions(String tx, int count, int skip, int start, int end) {
        return http.engine("omni_listtransactions", List.class, tx, count, skip, start, end);
    }

    /***
     * 列出【本机】钱包事务，可选地按地址和块边界过滤。充值事件在这个方法的返回中
     * 默认实现
     * @param: [address：相关地址，如果为空则不过滤]
     * @return: java.lang.String，返回的结果中 ismine为true代表与本钱包中的地址有关系
     **/
    public List<OmniTransactionRes> listOmniTransactions(@Nullable String address) {

        AssertUp.isTrue(validateAddress(address).isvalid || address == null || address.equals("*"), E.ADDRESS_ERROR);

        String x = StringUtils.isEmpty(address) ? "*" : address;
        return http.engine("omni_listtransactions",
                List.class,
                x,
                100,
                0,
                0,
                999999999);
    }

    /***
     * 列出【本机】钱包中未验证的事务
     * @param: []
     * @return: void
     **/
    public List<OmniTransactionRes> listOmniPendingTransactions() {
        return http.engine("omni_listpendingtransactions", List.class);
    }

    /***
     * 获取【全网】下固定区块高度的事务列表
     * @param: [block_height：区块高度]
     * @return: 哈希列表
     **/
    public List<String> listBlockTransactions(long block_height) {
        return http.engine("omni_listblocktransactions", List.class, block_height);
    }

}


package com.initsysctrl.omnidemo.dao;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import com.initsysctrl.omnidemo.dto.BaseUtxo;
import com.initsysctrl.omnidemo.dto.SimpleUtxo;
import com.initsysctrl.omnidemo.dto.reponse.*;
import com.initsysctrl.omnidemo.exception.E;
import com.initsysctrl.omnidemo.utils.AssertUp;
import com.initsysctrl.omnidemo.utils.RpcHttpUtil;
import com.sun.istack.internal.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.initsysctrl.omnidemo.exception.E.BLANCE_ENOUTH_ERROR;


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
     * 发送比特币
     * @param: [fromAddress, toAddress, amount]
     * @return: java.lang.String
     **/
    @SuppressWarnings("UnnecessaryLocalVariable")
    public String sendBtc(String fromAddress, String toAddress, Double amount) {
        HashMap<String, Double> lixiaolai = new HashMap<>();
        lixiaolai.put(toAddress, amount);
        return sendBtcBatch(fromAddress, lixiaolai);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public String sendBtcBatch(String fromAddress, HashMap<String, Double> outs) {
        AssertUp.isTrue(validateAddress(fromAddress).isIsvalid(), E.ADDRESS_ERROR);
        Double sumAmount = 0.0;//转出的和
        for (Map.Entry<String, Double> entry : outs.entrySet()) {
            String address = entry.getKey();
            Double amount = entry.getValue();
            AssertUp.isTrue(validateAddress(address).isIsvalid(), E.ADDRESS_ERROR);
            AssertUp.isTrue(amount != null && amount > 0, E.AMOUNT_INPUT_EEROR);
            sumAmount += amount;
        }
        BigDecimal _amount = new BigDecimal(sumAmount.toString());
        BigDecimal _fee = new BigDecimal("0.0001");
        List<UnspentRes> unspentRes = listUnSpent(fromAddress);
        AssertUp.isTrue(!unspentRes.isEmpty(), BLANCE_ENOUTH_ERROR);
        log.warn(unspentRes.toString());
        Double sumUtxo = 0.0;
        ArrayList<BaseUtxo> input = new ArrayList<>();
        for (UnspentRes utxo : unspentRes) {
            if (sumUtxo < sumAmount) {
                sumUtxo = sumUtxo + utxo.getAmount();
                input.add(new BaseUtxo(utxo.getTxid(), utxo.getVout()));
            } else {
                break;
            }
        }
        BigDecimal _sum = new BigDecimal(sumUtxo.toString());
//       输入 >输出+手续费
//        输入=输出+手续+找零
        AssertUp.isTrue(sumUtxo >= (sumAmount + _fee.doubleValue()), BLANCE_ENOUTH_ERROR);
        log.warn(input.toString());
//        最终的输出output
        HashMap<String, Double> output = new HashMap<>(outs);
//        添加找零地址，即发送地址
        output.put(fromAddress, _sum.subtract(_amount).subtract(_fee).doubleValue());//找零地址地址接收
        log.warn(output.toString());
        //创建交易
        String createrawtransaction = http.engine("createrawtransaction", String.class, input, output);
        log.warn("createrawtransaction" + createrawtransaction);
        AssertUp.isTrue(createrawtransaction != null, "创建交易失败");
        //签名交易
        SignrawtransRes signrawtransaction = http.engine("signrawtransaction", SignrawtransRes.class, createrawtransaction);
        log.warn("signrawtransaction" + signrawtransaction);
        AssertUp.isTrue(signrawtransaction != null, "签名失败");
        //广播交易
        String sendrawtransaction = http.engine("sendrawtransaction", String.class, signrawtransaction.getHex());
        log.warn("sendrawtransaction" + sendrawtransaction);
        AssertUp.isTrue(sendrawtransaction != null, "广播交易失败");
        return sendrawtransaction;//返回事务哈希
    }

    /***
     * 生成新的地址,usdt与bit通用地址
     * @param: [account] 账户名，如果不设置默认为空
     * @return: java.lang.String 地址
     **/
    public String getNewAddress(@Nullable String account) {
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
     * 导出私钥
     * @param: [address]
     * @return: java.lang.String
     **/
    public String dumpPrivkey(@NotNull String address) {
        return http.engine("dumpprivkey", String.class, address);
    }


    /***
     * 获取地址对于的账户名称 by address
     * @param: [address]
     * @return: com.leazxl.bs.dto.BaseRPCresponse<java.lang.String>
     **/
    public String getAccountByAddress(String address) {
        AssertUp.isTrue(validateAddress(address).isIsvalid(), E.ADDRESS_ERROR);
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
     * 获取UTXO
     * @param: [address]
     * @return: void
     **/
    public List<UnspentRes> listUnSpent(@Nullable String address) {
        AssertUp.isTrue(address == null || validateAddress(address).isIsvalid(), E.ADDRESS_ERROR);
        Object result;
        if (StringUtils.isEmpty(address)) {
            result = http.engine("listunspent", Object.class);
        } else {
            Object[] parms = {1, 999999, new java.lang.String[]{address}};//最小确认，最大确认
            result = http.engine("listunspent", Object.class, parms);
        }
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<UnspentRes>> typeReference = new TypeReference<List<UnspentRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(result), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /***
     * 获取钱包内比特币地址信息列表
     * @param: []
     * @return: void
     **/
    public List<BtcAddressInfo> listReceivedByAddress() {
        Object result = http.engine("listreceivedbyaddress", Object.class, 1, true);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<BtcAddressInfo>> typeReference = new TypeReference<List<BtcAddressInfo>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(result), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
    public OmniTokenBalanceInfoRes getBalanceByAddAndId(String address, int propertyid) {

        AssertUp.isTrue(validateAddress(address).isIsvalid(), E.ADDRESS_ERROR);
        return http.engine("omni_getbalance", OmniTokenBalanceInfoRes.class, address, propertyid);

    }

    /***
     *查询令牌余额信息 by id
     Returns a list of token balances for a given currency or property identifier.
     * @param: [token_id]，USDT=31
     * @return: void
     **/
    @Deprecated
    public List<OmniTokenBalanceInfoRes> getOmniBalanceById(int token_id) {
        Object object = http.engine("omni_getallbalancesforid", Object.class, token_id);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTokenBalanceInfoRes>> typeReference = new TypeReference<List<OmniTokenBalanceInfoRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    /***
     * 查询令牌余额信息 by 地址
     * @param: [address]
     * @return:
     */
    public List<OmniTokenBalanceInfoRes> getOmniBalanceByAddress(String address) {


        AssertUp.isTrue(validateAddress(address).isIsvalid(), E.ADDRESS_ERROR);

        Object object = http.engine("omni_getallbalancesforaddress", Object.class, address);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTokenBalanceInfoRes>> typeReference = new TypeReference<List<OmniTokenBalanceInfoRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /***
     * 查询钱包中所有地址的余额信息
     * @param: []
     * @return: java.lang.String
     **/
    public List<OmniTokenBalWithAddressRes> getAllBalancesWithAddress() {
        Object object = http.engine("omni_getwalletaddressbalances", Object.class);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTokenBalWithAddressRes>> typeReference = new TypeReference<List<OmniTokenBalWithAddressRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 查询所有令牌的余额总额
     * @param: []
     * @return: java.lang.String
     **/
    public List<OmniTokenBalanceInfoRes> getAllBalances() {
        Object object = http.engine("omni_getwalletbalances", Object.class);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTokenBalanceInfoRes>> typeReference = new TypeReference<List<OmniTokenBalanceInfoRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /***
     *
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
                                long propertyid,
                                BigDecimal amount,
                                String feeaddress) {
        AssertUp.isTrue(validateAddress(feeaddress).isIsvalid(), E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(toaddress).isIsvalid(), E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(fromaddress).isIsvalid(), E.ADDRESS_ERROR);

        return http.engine("omni_funded_send", fromaddress, toaddress, propertyid, amount.toString(), feeaddress);
    }

    /***
     *  使用原生模式交易
     * @param:
     * [fromaddress 发送usdt地址,
     * toaddress 接受usdt地址,
     * propertyid 令牌id，正式环境USDt下固定为31,
     * amount 发送usdt金额,
     * feeaddress 手续费地址]
     * @return: void
     **/
    public String sendOmniTokenRaw(String fromaddress, String toaddress, long propertyid, BigDecimal amount, String feeaddress) {
        /**第一步，列出formeaddress,feeaddress上的utxo **/
        double fee = 0.00005;
        List<UnspentRes> unspentRes1 = listUnSpent(fromaddress);
        AssertUp.isTrue(unspentRes1 != null && !unspentRes1.isEmpty(), E.FEE_NOT_ENOUGH);
        log.warn(unspentRes1.toString());

        UnspentRes utxo1 = null;
        for (UnspentRes un : unspentRes1) {
            if (un.isSpendable() && un.getConfirmations() >= 1) {
                utxo1 = un;
                break;
            }
        }
        log.warn(utxo1 == null ? "null" : utxo1.toString());
        AssertUp.isTrue(utxo1 != null, E.FEE_NOT_ENOUGH);


        List<UnspentRes> unspentRes2 = listUnSpent(feeaddress);
        AssertUp.isTrue(unspentRes2 != null && !unspentRes2.isEmpty(), E.FEE_NOT_ENOUGH);
        log.warn(unspentRes2.toString());
        UnspentRes utxo2 = null;
        for (UnspentRes un : unspentRes2) {
            if (un.getAmount() >= fee && un.isSpendable() && un.getConfirmations() >= 2) {
                utxo2 = un;
                break;
            }
        }
        log.warn(utxo2 == null ? "null" : utxo2.toString());
        AssertUp.isTrue(utxo2 != null, E.FEE_NOT_ENOUGH);

        /**第二步：构造发送代币类型和代币数量数据**/
        String simplesend = http.engine("omni_createpayload_simplesend", String.class, propertyid, amount.toString());

        /**第三步：构造交易基本数据（transaction base） https://bitcoin.org/en/developer-reference#createmultisig**/
        BaseUtxo baseUtxo1 = new BaseUtxo(utxo1.getTxid(), utxo1.getVout());
        BaseUtxo baseUtxo2 = new BaseUtxo(utxo2.getTxid(), utxo2.getVout());
        ArrayList<BaseUtxo> inputs = new ArrayList<>();
        inputs.add(baseUtxo1);
        inputs.add(baseUtxo2);
        HashMap<String, Double> outputs = new HashMap<>();
        String createrawtransaction = http.engine("createrawtransaction", String.class, inputs, outputs);
        log.warn(createrawtransaction == null ? "createrawtransaction==null" : "createrawtransactio=" + createrawtransaction);
        AssertUp.isTrue(createrawtransaction != null, "createrawtransaction==null");

        /***第四步：.在交易数据中加上omni代币数据,从第2步获取有效负载，从第3步获取基本事务。**/
        String createrawtxOpreturn = http.engine("omni_createrawtx_opreturn", String.class, createrawtransaction, simplesend);
        log.warn(createrawtxOpreturn == null ? "createrawtxOpreturn==null" : "createrawtxOpreturn=" + createrawtxOpreturn);
        AssertUp.isTrue(createrawtxOpreturn != null, "createrawtxOpreturn==null");

        /**第五步：在交易数据上加上接收地址，从步骤4获取扩展事务，并将参考输出添加到"接受地址"，它将成为令牌的接收者。**/
        String createrawtxReference = http.engine("omni_createrawtx_reference", String.class, createrawtxOpreturn, toaddress);
        log.warn(createrawtxReference == null ? "createrawtxReference==null" : "createrawtxReference" + createrawtxReference);
        AssertUp.isTrue(createrawtxReference != null, " createrawtxReference ==null");

        /**第六步：指定矿工费并附加变更输出（根据需要）**/

        SimpleUtxo simpleUtxo1 = new SimpleUtxo(utxo1.getTxid(), utxo1.getVout(), utxo1.getScriptPubKey(), utxo1.getAmount());
        SimpleUtxo simpleUtxo2 = new SimpleUtxo(utxo2.getTxid(), utxo2.getVout(), utxo2.getScriptPubKey(), utxo2.getAmount());
        ArrayList<SimpleUtxo> list = new ArrayList<>();
        list.add(simpleUtxo1);
        list.add(simpleUtxo2);

        String createrawtxChange = http.engine("omni_createrawtx_change", String.class, createrawtxReference, list, feeaddress, fee);
        AssertUp.isTrue(createrawtxChange != null, " createrawtxChange ==null");

        /**第七步：签署交易**/
        SignrawtransRes signrawtransRes = http.engine("signrawtransaction", SignrawtransRes.class, createrawtxChange);
        log.warn(signrawtransRes == null ? "signrawtransRes==null" : "signrawtransRes=" + signrawtransRes);
        AssertUp.isTrue(signrawtransRes != null && signrawtransRes.getHex() != null, " signrawtransRes ==null");

        /**第八步：广播交易**/
        String sendrawtransaction = http.engine("sendrawtransaction", String.class, signrawtransRes.getHex());
        log.warn(sendrawtransaction == null ? "sendrawtransaction==null" : "sendrawtransaction=" + sendrawtransaction);
        AssertUp.isTrue(sendrawtransaction != null, " sendrawtransaction ==null");
        return sendrawtransaction;
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
        AssertUp.isTrue(validateAddress(feeaddressx).isIsvalid(), E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(toaddress).isIsvalid(), E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(fromaddress).isIsvalid(), E.ADDRESS_ERROR);

        return http.engine("omni_funded_sendall", String.class, fromaddress, toaddress, is_main_ecosystem ? 1 : 2, feeaddressx);
    }

    /**
     * 发生全部的token
     *
     * @param fromaddress
     * @param toaddress
     * @return
     */
    public String sendOmniAll(String fromaddress, String toaddress) {
        AssertUp.isTrue(validateAddress(toaddress).isIsvalid(), E.ADDRESS_ERROR);
        AssertUp.isTrue(validateAddress(fromaddress).isIsvalid(), E.ADDRESS_ERROR);

        return http.engine("omni_sendall", String.class, fromaddress, toaddress, 1);
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
        Object object = http.engine("omni_listtransactions", Object.class, tx, count, skip, start, end);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTransactionRes>> typeReference = new TypeReference<List<OmniTransactionRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 列出【本机】钱包事务，可选地按地址和块边界过滤。充值事件在这个方法的返回中
     * 默认实现
     * @param: [address：相关地址，如果为空则不过滤]
     * @return: java.lang.String，返回的结果中 ismine为true代表与本钱包中的地址有关系
     **/
    public List<OmniTransactionRes> listOmniTransactions(@Nullable String address) {

        AssertUp.isTrue(address == null || address.equals("*") || validateAddress(address).isIsvalid(), E.ADDRESS_ERROR);

        String x = StringUtils.isEmpty(address) ? "*" : address;
        Object object = http.engine("omni_listtransactions",
                Object.class,
                x,
                99999,
                0,
                0,
                999999999);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTransactionRes>> typeReference = new TypeReference<List<OmniTransactionRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 列出【本机】钱包中未验证的事务
     * @param: []
     * @return: void
     **/
    public List<OmniTransactionRes> listOmniPendingTransactions(@Nullable String address) {
        String add;
        if (address == null || address.equals("") || address.equals("*")) {
            add = "";
        } else {
            add = address;
        }
        Object object = http.engine("omni_listpendingtransactions", Object.class, add);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<OmniTransactionRes>> typeReference = new TypeReference<List<OmniTransactionRes>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 获取【全网】下固定区块高度的事务列表
     * @param: [block_height：区块高度]
     * @return: 哈希列表
     **/
    public List<String> listBlockTransactions(long block_height) {
        Object object = http.engine("omni_listblocktransactions", Object.class, block_height);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
        };
        try {
            return mapper.readValue(JSON.toJSONString(object), typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 钱包数据备份
     * @param: []
     * @return: void
     * https://bitcoin.org/en/developer-reference#backupwallet
     **/
    public String backupWallet() {
        System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String file = String.format("wallet-%s.dat", time);
        http.engine("backupwallet", file);
        return file;
    }

    /***
     * 钱包数据导入
     * @param: []
     * @return: void
     * https://bitcoin.org/en/developer-reference#importwallet
     *对于影响新添加的密钥的事务，调用可能需要重新扫描整个链，可能需要几分钟。
     **/
    public void importWallet(String fielName) {
        http.engine("importwallet", fielName);
    }

    /***
     * 导出钱包数据以人类可读的方式
     * @param: []
     * @return: java.lang.String
     **/
    public String dumpWallet() {
        System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String file = String.format("wallet-%s.txt", time);
        http.engine("dumpwallet", file);
        return file;
    }
}


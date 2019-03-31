package com.initsysctrl.omnidemo.dto.reponse;

import lombok.Data;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-09-06 15:28
 **/
@Data
public class OmniTransactionRes {

    //普通交易事务
    /**
     * txid : ef6e77063dff8988b82044286b3d3f022df5a14aae260179a95c2e80c0e47ec4
     * fee : 0.00055300
     * sendingaddress : mqrA5Ai8XdKe1ob1L2HwyYr3TXUf9nUeBf
     * referenceaddress : mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC
     * ismine : true
     * version : 0
     * type_int : 0
     * type : Simple Send
     * propertyid : 1
     * divisible : true
     * amount : 4.00000000
     * valid : true
     * blockhash : 00000000000000262588c21afbf9e1da151daf10b11215d501271163f26ea74a
     * blocktime : 1536589385
     * positioninblock : 544
     * block : 1412856
     * confirmations : 864
     */

    /**
     * txid : ac1be0d180efad9029876f547aeb3b2243dfacb73a59510b2dbab806544b4082
     * fee : 0.00003130
     * sendingaddress : mpaumxor659PhoJhXp1VCVHVwbFCZSRmuf
     * ismine : true
     * version : 0
     * type_int : 50
     * type : Create Property - Fixed
     * propertyid : 2147484888
     * divisible : true
     * ecosystem : test
     * propertytype : divisible
     * category : fuck your father
     * subcategory : fuck you mon
     * propertyname : FuckYou
     * data : what the fuck
     * url : https://www.fuckyouerveyday.com/
     * amount : 100000000.00000000
     * valid : true
     * blockhash : 000000000001f7578061c7185612d2b3a6f018c205d65bfba5adf9a7d01011e5
     * blocktime : 1538101607
     * positioninblock : 46
     * block : 1414520
     * confirmations : 4
     */


    private String txid;//事务哈希
    private String fee;//手续费
    private String sendingaddress;//发送者
    private String referenceaddress;//接受者
    private boolean ismine;//是否与本钱包相关
    private int version;
    private int type_int;//类型
    private String type;//类型
    private long propertyid;
    private boolean divisible;
    private String amount;//发送金额
    private boolean valid;//是否成功
    private String invalidreason;//如果交易确认失败了，那么此处会返回具体的原因。
    private String blockhash;
    private long blocktime;
    private long positioninblock;
    private long block;
    private long confirmations;//确认数


    //特殊事务可能会有的字段
    private String ecosystem;
    private String propertytype;
    private String category;
    private String subcategory;
    private String propertyname;
    private String data;
    private String url;


}

package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @package: com.leazxl.bs.bean
 * @description:
 * @author: yepeng
 * @create: 2018-09-04 10:38
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmniValAddRes {

    /**
     * isvalid : true
     * address : mjH1iB7wt5TC4f8qjvZqtmBXd1aCPSPinC
     * scriptPubKey : 76a914293d87f697ca96ffb00f049b60645e5c8979498488ac
     * ismine : true
     * iswatchonly : false
     * isscript : false
     * pubkey : 0302cb77549243dd028915c31a0480285483dbb997573955e8e5ae78353c14a8fe
     * iscompressed : true
     * account : account0
     * hdkeypath : m/0'/0'/14'
     * hdmasterkeyid : 996e890cd8c4d3d9c2702616b1727797cd46799c
     */

    public boolean isvalid;
    public String address;
    public String scriptPubKey;
    public boolean ismine;
    public boolean iswatchonly;
    public boolean isscript;
    public String pubkey;
    public boolean iscompressed;
    public String account;
    public String hdkeypath;
    public String hdmasterkeyid;


}

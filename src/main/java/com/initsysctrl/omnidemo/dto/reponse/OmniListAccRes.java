package com.initsysctrl.omnidemo.dto.reponse;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: OmniListAccRes
 * @description: 返回：账户及余额列表
 * @author: yepeng
 * @create: 2018/9/4 下午6:29
 **/
@Data
public class OmniListAccRes {

    public Map<String, BigDecimal> result;

}

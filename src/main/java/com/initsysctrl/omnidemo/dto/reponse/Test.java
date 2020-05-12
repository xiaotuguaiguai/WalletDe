package com.initsysctrl.omnidemo.dto.reponse;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by quanlun on 2020/5/4.
 */
public class Test {
    public static void main(String[] args) {
//        BigDecimal bigDecimal = new BigDecimal("0.0002");
//        System.out.println(bigDecimal.toString());
        String a="113";
        String b="30";
        String c="300";

        calculateUsdt(2,0,b,"0",a);
        calculateUsdt(2,1,c,"30",a);
    }
    /**
     *
     * @param queueLength 队列中需要出SIC的人的数量
     * @param position 位置索引
     * @param sicNum 用户持有的SIC数量
     * @param preSIcTotal 前面人持有的SIC数量
     * @param needShareUsdtNum 共享的USDT数量
     */
    public static String calculateUsdt(int queueLength,int position,String sicNum,String preSIcTotal,String needShareUsdtNum){

        BigDecimal holdSICDecimal = new BigDecimal(sicNum);
        BigDecimal shareUsdtDecimal = new BigDecimal(needShareUsdtNum);
        BigDecimal a=  shareUsdtDecimal.subtract(new BigDecimal("3"));
        BigDecimal onePointFive = new BigDecimal("0.5");
        if(position!=queueLength-1){
            return    (holdSICDecimal.multiply(onePointFive)
                    .multiply(a).divide(shareUsdtDecimal)).toString();

        }else{
            BigDecimal pretotal = new BigDecimal(preSIcTotal);
            return (shareUsdtDecimal.subtract(onePointFive.multiply(pretotal))).multiply(a).divide(shareUsdtDecimal).toString();
        }

    }


}

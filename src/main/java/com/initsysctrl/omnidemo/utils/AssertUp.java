package com.initsysctrl.omnidemo.utils;

import com.initsysctrl.omnidemo.exception.E;
import com.initsysctrl.omnidemo.exception.WalletException;

/**
 * @description: assert utils
 * @author: yepeng
 * @create: 2018-08-06 15:12
 **/
public class AssertUp {

    public static void isTrue(boolean what, E e) {
        if (!what) {
            throw new WalletException(e);
        }
    }

    public static void isFalse(boolean what, E e) {
        if (what) {
            throw new WalletException(e);
        }
    }

    public static void isFalse(boolean what, String e) {
        if (what) {
            throw new WalletException(e);
        }
    }
}

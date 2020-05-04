package com.initsysctrl.omnidemo.dto;

import java.io.Serializable;

public class ReceiveBean implements Serializable {
    private String sendAddress;
    private String sendAmount;
    private String txHash;
    private String blockHeight;
    private boolean isFaile;

    public boolean isFaile() {
        return isFaile;
    }

    public void setFaile(boolean faile) {
        isFaile = faile;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(String sendAmount) {
        this.sendAmount = sendAmount;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(String blockHeight) {
        this.blockHeight = blockHeight;
    }
}

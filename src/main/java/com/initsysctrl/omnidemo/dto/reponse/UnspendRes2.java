package com.initsysctrl.omnidemo.dto.reponse;

import java.util.List;

/**
 * Created by quanlun on 2020/5/5.
 */
public class UnspendRes2 {

    /**
     * notice :
     * unspent_outputs : [{"tx_hash":"3e95e0715b25da98e7e5662d146ed7d2857e196fd10f4578b8e08c463359155a","tx_hash_big_endian":"5a155933468ce0b878450fd16f197e85d2d76e142d66e5e798da255b71e0953e","tx_output_n":0,"script":"76a914adb820605ac92a88450b2c6a596d5eee1c7afb1a88ac","value":2000,"value_hex":"07d0","confirmations":163,"tx_index":0},{"tx_hash":"ce25ba5c7704ff53ba5072f479526d22b7bde53cebd47930888044ca875b64fc","tx_hash_big_endian":"fc645b87ca4480883079d4eb3ce5bdb7226d5279f47250ba53ff04775cba25ce","tx_output_n":2,"script":"76a914adb820605ac92a88450b2c6a596d5eee1c7afb1a88ac","value":546,"value_hex":"0222","confirmations":158,"tx_index":0},{"tx_hash":"f196165ecf95bdc4c9e5f9ec084f12a308eb5584f1c427129fc4c93f65e271a9","tx_hash_big_endian":"a971e2653fc9c49f1227c4f18455eb08a3124f08ecf9e5c9c4bd95cf5e1696f1","tx_output_n":2,"script":"76a914adb820605ac92a88450b2c6a596d5eee1c7afb1a88ac","value":546,"value_hex":"0222","confirmations":51,"tx_index":0},{"tx_hash":"c2768422d231c92f0d4f9c78bf5fc71c2c2573f145b026d54210c08450068942","tx_hash_big_endian":"4289065084c01042d526b045f173252c1cc75fbf789c4f0d2fc931d2228476c2","tx_output_n":2,"script":"76a914adb820605ac92a88450b2c6a596d5eee1c7afb1a88ac","value":546,"value_hex":"0222","confirmations":0,"tx_index":0}]
     */

    private String notice;
    private List<UnspentOutputsBean> unspent_outputs;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<UnspentOutputsBean> getUnspent_outputs() {
        return unspent_outputs;
    }

    public void setUnspent_outputs(List<UnspentOutputsBean> unspent_outputs) {
        this.unspent_outputs = unspent_outputs;
    }

    public static class UnspentOutputsBean {
        /**
         * tx_hash : 3e95e0715b25da98e7e5662d146ed7d2857e196fd10f4578b8e08c463359155a
         * tx_hash_big_endian : 5a155933468ce0b878450fd16f197e85d2d76e142d66e5e798da255b71e0953e
         * tx_output_n : 0
         * script : 76a914adb820605ac92a88450b2c6a596d5eee1c7afb1a88ac
         * value : 2000
         * value_hex : 07d0
         * confirmations : 163
         * tx_index : 0
         */

        private String tx_hash;
        private String tx_hash_big_endian;
        private int tx_output_n;
        private String script;
        private int value;
        private String value_hex;
        private int confirmations;
        private int tx_index;

        public String getTx_hash() {
            return tx_hash;
        }

        public void setTx_hash(String tx_hash) {
            this.tx_hash = tx_hash;
        }

        public String getTx_hash_big_endian() {
            return tx_hash_big_endian;
        }

        public void setTx_hash_big_endian(String tx_hash_big_endian) {
            this.tx_hash_big_endian = tx_hash_big_endian;
        }

        public int getTx_output_n() {
            return tx_output_n;
        }

        public void setTx_output_n(int tx_output_n) {
            this.tx_output_n = tx_output_n;
        }

        public String getScript() {
            return script;
        }

        public void setScript(String script) {
            this.script = script;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getValue_hex() {
            return value_hex;
        }

        public void setValue_hex(String value_hex) {
            this.value_hex = value_hex;
        }

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public int getTx_index() {
            return tx_index;
        }

        public void setTx_index(int tx_index) {
            this.tx_index = tx_index;
        }
    }
}

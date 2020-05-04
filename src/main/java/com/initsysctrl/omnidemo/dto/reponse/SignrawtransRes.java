package com.initsysctrl.omnidemo.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @package: com.leazxl.bs.dto.reponse
 * @description:
 * @author: yepeng
 * @create: 2018-10-22 10:58
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignrawtransRes {


    /**
     * hex : 0100000002de95b97cf4c67ec01485fd698ec154a325ff69dd3e58435d7024bae7f69534c2000000006a47304402200a6fef16882db2f3e07356b619121d74cf0bd42872cba57430e901b4252f7c8102202edcaa90b278d568faa55a6a9c523ff0cd09e0c916e75ea7baf4690d5747789c01210382df61bad93a1211ceac5c78fd273d65e405a7e148e068ced3e40bf87cf71721ffffffffb3b60aaa69b860c9bf31e742e3b37e75a2a553fd0bebf8aaf7da0e9bb07316ee020000006b483045022100ae11d3c92a501496381aa7eaf10ef458f4aabdd3075233ae70d9d32f6b83d812022053aa4171e3d2b58465dde42d07ba4e5f74948b2cdb01a67dfdac4e4eb24b684901210382df61bad93a1211ceac5c78fd273d65e405a7e148e068ced3e40bf87cf71721ffffffff036a5a0d00000000001976a914c6734676a08e3c6438bd95fa62c57939c988a17b88ac0000000000000000166a146f6d6e690000000000000002000000000098968022020000000000001976a914ee692ea81da1b12d3dd8f53fd504865c9d843f5288ac00000000
     * complete : true
     */

    String hex;
    boolean complete;


}

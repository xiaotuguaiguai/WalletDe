package com.initsysctrl.omnidemo.dto.response;

import lombok.Data;

import java.util.List;


@Data
public class OmniInfoRes {


    private String mastercoreversion;
    private int totaltransactions;
    private int blocktime;
    private int blocktransactions;
    private int totaltrades;
    private String omnicoreversion;
    private String bitcoincoreversion;
    private int omnicoreversion_int;
    private int block;
    private List<?> alerts;


}

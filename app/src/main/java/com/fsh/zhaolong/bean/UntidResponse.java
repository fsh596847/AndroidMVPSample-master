package com.fsh.zhaolong.bean;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/20.
 */
@Setter @Getter
public class UntidResponse extends BaseResponse<List<UntidResponse.DataBean>> {

  @Setter @Getter
  public static class DataBean implements Serializable{
    /**
     * unitcode : sz10101 unitid : f9a8fe65591a65b901591a6fb01e0006 unitname : 苏州鱼苗批发商
     */

    private String unitcode;
    private String unitid;
    private String unitname;
  }
}

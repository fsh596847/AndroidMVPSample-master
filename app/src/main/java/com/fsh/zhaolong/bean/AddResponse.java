package com.fsh.zhaolong.bean;

import java.util.LinkedList;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/21.
 */
@Setter @Getter
public class AddResponse extends BaseResponse<LinkedList<AddResponse.DataBean>> {


  @Setter @Getter
  public static class DataBean {
    /**
     * varietycode : 黄13 varietyname : 黄花鱼 varietyid : f9a8fe65591c307501591c378bbf000d
     */

    private String varietycode;
    private String varietyname;
    private String varietyid;
  }

}

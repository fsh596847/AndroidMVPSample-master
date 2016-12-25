package com.fsh.zhaolong.bean;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/25.
 */
@Setter @Getter
public class AddProjrctResponse extends BaseResponse<List<AddProjrctResponse.DataBean>> {



  @Setter @Getter
  public static class DataBean {
    /**
     * projectid : f9a8fe65591c307501591c45ad8a0013
     * projectname : 鱼虾项目
     * projectcode : 04
     */

    private String projectid;
    private String projectname;
    private String projectcode;
    private boolean isCheck;

  }
}

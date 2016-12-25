package com.fsh.zhaolong.bean;

/**
 * Created by HIPAA on 2016/12/19.  登录
 */

import lombok.Getter;
import lombok.Setter;

 @Setter@Getter
public class LonginResponse {

  /**
   * status : 1 data : {"id":"4028810c590aecde01590af14a330001","username":"test","mobile":null}
   */

  private int status;
  private DataBean data;
  @Setter@Getter
  public static class DataBean {
    /**
     * id : 4028810c590aecde01590af14a330001 username : test mobile : null
     */

    private String id;
    private String username;
    private String mobile;
  }
}

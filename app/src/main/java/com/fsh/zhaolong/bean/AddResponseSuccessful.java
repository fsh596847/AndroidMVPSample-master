package com.fsh.zhaolong.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/21.
 */
@Setter @Getter
public class AddResponseSuccessful {

  /**
   * status : 1
   * data : 添加成功
   */

  private int status;
  private String data;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}

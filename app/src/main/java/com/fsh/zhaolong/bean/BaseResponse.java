package com.fsh.zhaolong.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/19.
 */
@Setter @Getter
public class BaseResponse<T>{

  private int status;
  private T data;
  private String pagesize;
  private int totalRows;
  private int p;
  private int totalPage;
}

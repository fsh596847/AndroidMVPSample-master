package com.fsh.zhaolong.bean;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/23.
 */
@Setter @Getter
public class DetailResponse extends BaseResponse<List<DetailResponse.DataBean>> {




  @Setter @Getter
  public static class DataBean {
    /**
     * spec : 12
     * waste : null
     * mny : null
     * unitname : 广东省鱼苗批发总代理
     * hid : f9a8fe655929c79701592a7dbb300001
     * mea : 公斤
     * userid : 1
     * totalrealweight : -43
     * totalmny : null
     * createtime : 2016-12-23 15:03:20
     * username : admin
     * totaltareweight : 50
     * unitid : f9a8fe65591a65b901591a6f31e20005
     * totalwaste : 0
     * realweight : null
     * varietyname : 鲤鱼
     * bid : f9a8fe655929c79701592a7dbb300002
     * peel : 50
     * netweight : null
     * billcode : 201612230000
     * totalnetweight : -43
     * totalnum : null
     * varietyid : f9a8fe65591c307501591c322c560003
     * num : null
     * deliveryaddress : æ·æ¯å¤§å¦
     * grossweight : 7
     * tareweight : 0
     * totalgrossweight : 7
     */

    private double spec;
    private String waste;
    private String mny;
    private String unitname;
    private String hid;
    private String mea;
    private String userid;
    private double totalrealweight;
    private String totalmny;
    private String createtime;
    private String username;
    private double totaltareweight;
    private String unitid;
    private double totalwaste;
    private String realweight;
    private String varietyname;
    private String bid;
    private double peel;
    private String netweight;
    private String billcode;
    private double totalnetweight;
    private String totalnum;
    private String varietyid;
    private String num;
    private String deliveryaddress;
    private double grossweight;
    private double tareweight;
    private double totalgrossweight;
    private double price;

  }
}

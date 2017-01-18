package com.fsh.zhaolong.bean;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by HIPAA on 2016/12/21.
 */
@Setter @Getter
public class MainResponse extends BaseResponse<List<MainResponse.DataBean>>
    implements Serializable {


  @Setter @Getter
  public static class DataBean implements Serializable {
    /**
     * spec : 55
     * waste : 55
     * mny : 55
     * unitname : 苏州鱼苗批发商
     * hid : f9a8fe655921b5c2015921c289170019
     * mea : 公斤
     * userid : 1
     * totalrealweight : -24
     * totalmny : 55
     * createtime : 8ujj
     * username : admin
     * totaltareweight : 20
     * unitid : f9a8fe65591a65b901591a6fb01e0006
     * totalwaste : 10
     * realweight : 55
     * varietyname : 鲸鱼
     * bid : f9a8fe655921b5c2015921c28917001a
     * peel : 10
     * netweight : 55
     * billcode : 201612210008
     * totalnetweight : -14
     * totalnum : 55
     * varietyid : f9a8fe65591c307501591c38093d000f
     * num : 55
     * deliveryaddress : ?μ?èˉ???°???
     * grossweight : 3
     * tareweight : 0
     * totalgrossweight : 6
     */

    private String spec;
    private String waste;
    private String mny;
    private String unitname;
    private String hid;
    private String mea;
    private String userid;
    private String totalrealweight;
    private String totalmny;
    private String createtime;
    private String username;
    private String totaltareweight;
    private String unitid;
    private String totalwaste;
    private String realweight;
    private String varietyname;
    private String bid;
    private String peel;
    private String netweight;
    private String billcode;
    private String totalnetweight;
    private String totalnum;
    private String varietyid;
    private String num;
    private String deliveryaddress;
    private String grossweight;
    private String tareweight;
    private String totalgrossweight;
    private String projectname;
    private String projectid;
    private String remark;
  }
}

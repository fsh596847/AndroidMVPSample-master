package com.fsh.zhaolong.retrofit;

import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.AddResponseSuccessful;
import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.bean.UntidResponse;
import com.fsh.zhaolong.mvp.main.MainModel;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ApiStores {
    //baseUrl http://www.weather.com.cn/ http://121.40.126.229:8082/wht
    String API_SERVER_URL = "http://121.40.126.229:8888/yz/";

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Call<MainModel> loadDataByRetrofit(@Path("cityId") String cityId);

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<MainModel> loadDataByRetrofitRxjava(@Path("cityId") String cityId);

    //登录
    @FormUrlEncoded
    @POST("phone_loginUser.action")
    Observable<String> login(@Field("username") String username, @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("phone_loginUser.action")
    Observable<String> login(@FieldMap Map<String, String> map);

    //主页
    @FormUrlEncoded
    @POST("phone_queryMakCode.action")
    Observable<String> projectList(@FieldMap Map<String, String> map);

    //@Headers({"Content-type:application/json;charset=UTF-8"})
    //@Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    @POST("phone_queryMakCode.action")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<String> projectList(@Field("userid") String userid,
        @Field("unitid") String unitid, @Field("varietyid") String varietyid,
        @Field(value = "varietyname") String varietyname,
        @Field("p") String P, @Field("pagesize") String pagesize);


    //查询交货单位接口
    @FormUrlEncoded
    @POST("phone_queryDeliveryunit.action")
    Observable<UntidResponse> queryDeliveryunit(@Field("p") int p);

    //查询品种接口
    @FormUrlEncoded
    @POST("phone_queryVariety.action")
    Observable<AddResponse> queryVariety(@Field("p") int p,@Field("pagesize") int pagesize);

    //增加划码单接口
    @FormUrlEncoded
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("phone_addMakCode.action")
    Observable<AddResponseSuccessful> phoneAddMakCode(@Field("userid") String userid,
        @Field("unitname") String unitname,
        @Field("unitid") String unitid,
        @Field("totalwaste") double totalwaste,
        @Field("remark") String remark,
        @Field("mea") String mea,
        @Field("peel") String peel,
        @Field("deliveryaddress") String deliveryaddress,
        @Field("dataList") String dataList,
        @Field("projectid") String projectid

    );
    //增加划码单接口

    //@Multipart
    @FormUrlEncoded
    @POST("phone_addMakCode.action")
    Observable<AddResponseSuccessful> phoneAddMakCode(@FieldMap Map<String, String> map);

    //划码单详情

    @POST("phone_queryMakCodeDatail.action")
    Observable<DetailResponse> phoneueryMakCodeDatail(@QueryMap Map<String, String> map);

    //查询项目

    @POST("phone_queryProject.action")
    Observable<AddProjrctResponse> phoneQueryProject();
}

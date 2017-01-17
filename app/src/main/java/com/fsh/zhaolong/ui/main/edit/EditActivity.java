package com.fsh.zhaolong.ui.main.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddItemBean;
import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.AddResponseSuccessful;
import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.bean.MainResponse;
import com.fsh.zhaolong.bean.UntidResponse;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.ui.detail.DetailActivity;
import com.fsh.zhaolong.ui.main.MainActivity;
import com.fsh.zhaolong.ui.unitid.UnitidActity;
import com.fsh.zhaolong.ui.view.AlertDialog;
import com.fsh.zhaolong.ui.view.ProjectDialog;
import com.fsh.zhaolong.util.Const;
import com.fsh.zhaolong.util.DateUtil;
import com.fsh.zhaolong.util.FullyLinearLayoutManager;
import com.fsh.zhaolong.util.PreferenceUtils;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

/**
 * Created by HIPAA on 2016/12/19.
 */

public class EditActivity extends MvpActivity<EditPresenter>
    implements EditView, AlertDialog.CallPayType, EditAdapter.CallBack, EditAdapter.ZengHang,
    ProjectAdapter.Freshen, ProjectDialog.CallPayType {
  public static final String INTENT_KEY_UNITNAME = "Unitname";
  private final String JIN = "0";
  private final String GONG_JIN = "1";
  private final int REUEST_CODE = 1;
  DecimalFormat df = new DecimalFormat("######0.00");
  //交货单位
  @Bind(R.id.tv1) TextView tv1;
  //地址
  @Bind(R.id.tvaddress) EditText mTvAddress;
  //去皮
  @Bind(R.id.tv3) EditText tv3;
  //单位
  @Bind(R.id.tv2) TextView tv2;
  //净重
  @Bind(R.id.netWeight) TextView tvNetWeight;
  //重置
  @Bind(R.id.tvreset) TextView tvReset;
  //时间
  @Bind(R.id.tvDate) TextView tvDate;
  @Bind(R.id.tvproject) TextView tvproject;
  //备注
  @Bind(R.id.etRemarks) EditText etRemarks;
  @Bind(R.id.RecyclerView) RecyclerView recyclerView;
  //项目
  @Bind(R.id.recyclerView) RecyclerView recyclerProject;
  //件数
  @Bind(R.id.numPage) TextView mTvnumPage;
  //毛重
  @Bind(R.id.roughWeight) TextView mTvRoughWeight;
  //皮重
  @Bind(R.id.tareWeight) TextView mTvTareWeight;
  ////废品
  //@Bind(R.id.wasteProduct) TextView wasteProduct;
  //废品
  @Bind(R.id.wasteProductEt) EditText mTvWasteProductEt;
  //实重
  @Bind(R.id.trueWeight) TextView mTvTrueWeight;
  //净重
  double netWeight;
  //件数
  int size;
  //皮重
  double tareWeight;
  //毛重
  double rough = 0;
  //废品
  double wasteProduct = 0;
  //实际重量
  double trueWeight = 0;
  //默认公斤
  private String code = "1";
  private String middleCode = "1";
  //交货单位
  private UntidResponse.DataBean mUntid;
  //品种Item
  private List<AddItemBean> mDatas;
  private EditAdapter myadapter;
  //项目Id
  private String projectid = null;
  private String projectName = null;
  private String hid = null;
  //请选择品种
  private List<AddResponse.DataBean> mBreeds = new ArrayList<>();
  //项目
  private List<AddProjrctResponse.DataBean> mProjects = new ArrayList<>();
  private int mItem = 0;
  private ProjectAdapter projectAdapter;
  //交货单位
  private String Unitname;
  private String Unitid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);
  }

  @Override protected EditPresenter createPresenter() {
    return new EditPresenter(EditActivity.this);
  }

  public void backClick(View view) {
    finish();
  }

  @Override public void init() {
    Map<String, String> map = new HashMap<>();
    map.put("userid", PreferenceUtils.getPrefString(mActivity, "userid", null));
    map.put("hid", getIntent().getStringExtra(DetailActivity.INTEN_KEY_HID));
    mvpPresenter.phoneueryMakCodeDatail(map);

    MainResponse.DataBean dataBean =
        (MainResponse.DataBean) getIntent().getSerializableExtra(MainActivity.INTENT_KEY_EDIT);
    Unitname = dataBean.getUnitname();
    Unitid = dataBean.getUnitid();
    //单位.
    if (dataBean.getMea().equals(GONG_JIN)) {
      tv2.setText("公斤");
    } else {
      tv2.setText("斤");
    }
    hid = dataBean.getHid();
    projectid = dataBean.getProjectid();
    projectName = dataBean.getProjectname();
    tvproject.setText(dataBean.getProjectname());
    //交货单位mTvAddress.
    mTvAddress.setText(dataBean.getDeliveryaddress());
    //去皮.
    tv3.setText(dataBean.getPeel());
    //交货单位
    tv1.setText(dataBean.getUnitname());
    // 备注. etRemarks.setText(dataBean.getR);

    //皮重mTvTareWeight.setText(dataBean.getTareweight());
    //废品
    mTvWasteProductEt.setText(dataBean.getTotalwaste());
    //实重mTvTrueWeight

    //时间
    tvDate.setText(DateUtil.actualTime());
    tv3.addTextChangedListener(new MyTextWatch(tv3));
    mTvWasteProductEt.addTextChangedListener(new TvWasteProductWatch(mTvWasteProductEt));
  }

  private void initRecycle() {
    if (code.equals(GONG_JIN)) {
      myadapter = new EditAdapter(mActivity, mDatas, R.layout.adapter_add);
    } else if (code.equals(JIN)) {
      myadapter = new EditAdapter(mActivity, mDatas, R.layout.adapter_add_brother);
    }
    recyclerView.setAdapter(myadapter);
    myadapter.setCallBack(this);
    myadapter.setZengHang(this);
  }

  @OnClick(R.id.lyt_title3)
  public void unite() {
    AlertDialog alertDialog = new AlertDialog(EditActivity.this).builder();
    alertDialog.setTitle("单位");
    alertDialog.setCallback(this);
    alertDialog.setList(initJin());
    alertDialog.show();
  }

  /**
   * 项目
   */
  @OnClick(R.id.tvproject)
  public void project() {
    mvpPresenter.getProjectRecponse();
  }

  /**
   * 项目
   */
  @Override public void getProjectRecponse(AddProjrctResponse model) {
    mProjects = model.getData();
    //recyclerProject.setLayoutManager(
    //    new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL));
    //recyclerProject.setItemAnimator(new DefaultItemAnimator());
    //projectAdapter = new ProjectAdapter(mProjects, mActivity);
    //projectAdapter.setFreshen(this);
    //recyclerProject.setAdapter(projectAdapter);
    ProjectDialog alertDialog = new ProjectDialog(EditActivity.this).builder();
    alertDialog.setTitle("项目");
    alertDialog.setProjectCallback(this);
    alertDialog.setList(mProjects);
    alertDialog.show();
  }

  /**
   * 项目
   */
  @Override public void callback(AddProjrctResponse.DataBean dataBean, int i) {
    projectid = dataBean.getProjectid();
    projectName = dataBean.getProjectname();
    tvproject.setText(dataBean.getProjectname());
  }

  /**
   * 增加一条之前判断 之前加的数据是否补全
   */
  protected void initData(int pos) {
    if (mDatas.size() > 0) {
      for (int i = 0; i < mDatas.size(); i++) {
        if (TextUtils.isEmpty(mDatas.get(i).getBreed()) ||
            TextUtils.isEmpty(mDatas.get(i).getNorms()) || TextUtils.isEmpty(
            mDatas.get(i).getRough())) {
          Toast.makeText(mActivity, "请补全信息", Toast.LENGTH_SHORT).show();
          return;
        }
      }
    }
    AddItemBean addItemBean = new AddItemBean();
    mDatas.add(addItemBean);
  }

  protected List<AddResponse.DataBean> initJin() {
    List<AddResponse.DataBean> jins = new ArrayList<>();
    AddResponse.DataBean itemBean = new AddResponse.DataBean();
    itemBean.setVarietyname("斤");
    itemBean.setVarietycode("0");
    jins.add(itemBean);
    AddResponse.DataBean itemBean1 = new AddResponse.DataBean();
    itemBean1.setVarietyname("公斤");
    itemBean1.setVarietycode("1");
    jins.add(itemBean1);
    return jins;
  }

  @Override public void getDataSuccess(AddResponse model) {
    mBreeds = model.getData();
    AlertDialog alertDialog = new AlertDialog(EditActivity.this).builder();
    alertDialog.setTitle("品种");
    alertDialog.setCallback(this);
    alertDialog.setList(mBreeds);
    alertDialog.show();
  }

  @Override public void getRecponse(AddResponseSuccessful model) {
    if (model.getStatus() == Const.SUNNESS_STATUE) {
      Toast.makeText(mActivity, model.getData(), Toast.LENGTH_SHORT).show();
      finish();
    } else {
      Toast.makeText(mActivity, model.getData(), Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void getDataFail(String msg) {
    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
  }

  @Override public void getDataSuccess(DetailResponse model) {
    List<DetailResponse.DataBean> list = model.getData();
    mDatas = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      AddItemBean addItemBean = new AddItemBean();
      addItemBean.setBreed(list.get(i).getVarietyname());
      addItemBean.setBreedId(list.get(i).getVarietyid());
      addItemBean.setNorms(list.get(i).getSpec() + "");
      addItemBean.setRough(list.get(i).getGrossweight() + "");
      mDatas.add(addItemBean);
    }
    //initData(0);
    recyclerView.setLayoutManager(new FullyLinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    initRecycle();
    //计算
    tongJi();
  }

  public void unitidClick(View view) {
    Intent intent = new Intent(mActivity, UnitidActity.class);
    startActivityForResult(intent, REUEST_CODE);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data == null) {
      return;
    }
    mUntid =
        (UntidResponse.DataBean) data.getSerializableExtra(INTENT_KEY_UNITNAME);
    tv1.setText(mUntid.getUnitname());
  }

  @Override public void callback(AddResponse.DataBean dataBean, int i) {
    if (!TextUtils.isEmpty(dataBean.getVarietyid())) {
      mDatas.get(mItem).setBreed(dataBean.getVarietyname());
      mDatas.get(mItem).setBreedId(dataBean.getVarietyid());
      myadapter.notifyDataSetChanged();
    } else {
      tv2.setText(dataBean.getVarietyname());
      //code 不同 算法也不同
      code = dataBean.getVarietycode();
      if (!code.equals(middleCode)) {
        mDatas = analyzeCode();
        initRecycle();
        tongJi();
        countWasteProduct();
        middleCode = dataBean.getVarietycode();
      }
    }
  }

  /**
   * 根据 斤和公斤 初始化list
   */
  private List<AddItemBean> analyzeCode() {
    if (code.equals(JIN)) {//斤
      for (int i1 = 0; i1 < mDatas.size(); i1++) {
        AddItemBean addItem = mDatas.get(i1);
        if (!TextUtils.isEmpty(addItem.getNorms())
            && Double.parseDouble(addItem.getNorms()) > 0) {
          addItem.setNorms(String.valueOf(Double.parseDouble(addItem.getNorms()) * 2));
        }
        if (!TextUtils.isEmpty(addItem.getRough())
            && Double.parseDouble(addItem.getRough()) > 0) {
          addItem.setRough(String.valueOf(Double.parseDouble(addItem.getRough()) * 2));
        }
      }
    } else if (code.equals(GONG_JIN)) {//公斤
      for (int i1 = 0; i1 < mDatas.size(); i1++) {
        AddItemBean addItem = mDatas.get(i1);
        if (!TextUtils.isEmpty(addItem.getNorms())
            && Double.parseDouble(addItem.getNorms()) > 0) {
          addItem.setNorms(String.valueOf(Double.parseDouble(addItem.getNorms()) / 2));
        }
        if (!TextUtils.isEmpty(addItem.getRough())
            && Double.parseDouble(addItem.getRough()) > 0) {
          addItem.setRough(String.valueOf(Double.parseDouble(addItem.getRough()) / 2));
        }
      }
    }
    return mDatas;
  }

  public void saveClick(View view) {
    //String projectid = null;
    //for (int i = 0; i < mProjects.size(); i++) {
    //  if (mProjects.get(i).isCheck()) {
    //    projectid = mProjects.get(i).getProjectid();
    //    break;
    //  }
    //}
    if (TextUtils.isEmpty(tv1.getText().toString())) {
      Toast.makeText(mActivity, "请选择交货单位", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(mTvAddress.getText().toString())) {
      Toast.makeText(mActivity, "请选择交货地址", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(tv3.getText().toString())) {
      Toast.makeText(mActivity, "请填写去皮", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(projectid)) {
      Toast.makeText(mActivity, "请选择项目", Toast.LENGTH_SHORT).show();
      return;
    }
    String sWasteProduct = mTvWasteProductEt.getText().toString();
    if (sWasteProduct.equals("")) {
      Toast.makeText(mActivity, "请输入废品", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(sWasteProduct)
        && (Double.parseDouble(mTvWasteProductEt.getText().toString())) >= 0) {
      Toast.makeText(mActivity, "请输入废品", Toast.LENGTH_SHORT).show();
      return;
    }

    String json = formJson();
    String userid = PreferenceUtils.getPrefString(mActivity, "userid", null);


    Map<String, String> queryParam = new HashMap<>();
    queryParam.put("userid", userid);
    queryParam.put("unitname", Unitname);
    queryParam.put("unitid", Unitid);
    queryParam.put("totalwaste", mTvWasteProductEt.getText().toString());
    queryParam.put("remark", etRemarks.getText().toString());
    queryParam.put("mea", code);
    queryParam.put("peel", tv3.getText().toString());
    queryParam.put("deliveryaddress", mTvAddress.getText().toString().trim());
    queryParam.put("dataList", json);
    queryParam.put("projectid", projectid);
    queryParam.put("projectname", projectName);
    queryParam.put("hid", hid);
    mvpPresenter.phoneAddMakCode(queryParam);
    //mvpPresenter.phoneAddMakCode(userid,Unitname,Unitid,wasteProduct,
    //    etRemarks.getText().toString(),code, tv3.getText().toString(),tv.getText().toString()
    //,json,
    //    projectid);
  }

  public String formJson() {
    try {
      JSONArray json = new JSONArray();
      for (int i = 0; i < mDatas.size(); i++) {
        JSONObject body = new JSONObject();
        body.put("varietyid", mDatas.get(i).getBreedId());//品种主键
        body.put("varietyname", mDatas.get(i).getBreed());//品种名称
        body.put("spec", mDatas.get(i).getNorms());//规格
        body.put("grossweight", mDatas.get(i).getRough()); //毛重
        json.put(body);
      }
      return json.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 统计  用来计算实际重量
   */
  public void tongJiClick(View view) {
    countWasteProduct();
  }

  /**
   * 根据废品计算实际重量
   */
  private void countWasteProduct() {

    String sWasteProduct = mTvWasteProductEt.getText().toString();
    if (sWasteProduct.equals("")) {
      Toast.makeText(mActivity, "请输入废品", Toast.LENGTH_SHORT).show();
      return;
    }
    if (TextUtils.isEmpty(sWasteProduct)
        && (Double.parseDouble(mTvWasteProductEt.getText().toString())) >= 0) {
      Toast.makeText(mActivity, "请输入废品", Toast.LENGTH_SHORT).show();
      return;
    }
    //废品
    wasteProduct = Double.parseDouble(sWasteProduct);
    if (code.equals(middleCode)) {

    } else {
      if (code.equals(GONG_JIN)) {
        wasteProduct = wasteProduct / 2;
      } else {
        wasteProduct = wasteProduct * 2;
      }
    }
    mTvWasteProductEt.setText(formDecimalFormat(wasteProduct));
    //实重 =净重-废品
    if (netWeight > 0) {
      trueWeight = netWeight - wasteProduct;
      mTvTrueWeight.setText("实重:" + formDecimalFormat(trueWeight));
    } else {
      Toast.makeText(mActivity, "实重必须大于0", Toast.LENGTH_SHORT).show();
    }
  }

  private void tongJi() { //件数=行数
    rough = 0;
    size = 0;
    for (int i1 = 0; i1 < mDatas.size(); i1++) {
      if (!TextUtils.isEmpty(mDatas.get(i1).getRough())
          && parseDouble(mDatas.get(i1).getRough()) >= 0) {
        size++;
      }
    }

    if (size < 0) {
      Toast.makeText(mActivity, "请增加商品", Toast.LENGTH_SHORT).show();
      return;
    }

    if (TextUtils.isEmpty(tv3.getText().toString())) {
      Toast.makeText(mActivity, "请输入每条去皮", Toast.LENGTH_SHORT).show();
      return;
    }

    //毛重=每件的毛重累加
    for (int i1 = 0; i1 < mDatas.size(); i1++) {
      if (!TextUtils.isEmpty(mDatas.get(i1).getRough())
          && parseDouble(mDatas.get(i1).getRough()) >= 0) {
        rough += (parseDouble(mDatas.get(i1).getRough()));
      }
    }
    mTvnumPage.setText("件数:" + size);
    mTvRoughWeight.setText("毛重:" + formDecimalFormat(rough));
    //皮重 =件数*每条去皮
    tareWeight = 0;
    if (!TextUtils.isEmpty(tv3.getText().toString())) {
      tareWeight = parseDouble(tv3.getText().toString());
      tareWeight = tareWeight * mDatas.size();
      mTvTareWeight.setText("皮重:" + formDecimalFormat(tareWeight));
    }
    //净重=毛重-皮重

    netWeight = rough - tareWeight;
    tvNetWeight.setText("净重:" + formDecimalFormat(netWeight));
    countWasteProduct();
  }

  /**
   * 重置
   */
  public void renSetClick(View view) {
    mDatas.clear();
    initData(0);
    initRecycle();
    //交货单位
    tv1.setText(null);
    mUntid = null;
    //地址
    mTvAddress.setText(null);
    //单位
    code = "1";
    tv2.setText("公斤");
    //备注
    etRemarks.setText(null);
    //每条去皮
    tv3.setText(null);
    //件数
    size = 0;
    mTvnumPage.setText("件数:--");
    //毛重
    rough = 0;
    mTvRoughWeight.setText("毛重:--");
    //皮重
    tareWeight = 0;
    mTvTareWeight.setText("皮重:--");
    //净重
    netWeight = 0;
    tvNetWeight.setText("净重:--");
    //废品
    wasteProduct = 0;
    mTvWasteProductEt.setHint("--");
    //实重
    trueWeight = 0;
    mTvTrueWeight.setText("实重:--");
  }

  private String formDecimalFormat(double d) {

    return df.format(d);
  }

  //品种
  @Override public void BreedCallbck(int position) {
    mItem = position;
    mvpPresenter.queryVariety(1, 200);
  }

  //毛重
  @Override public void MaoZhong(int pos, String s) {
    if (TextUtils.isEmpty(tv3.getText().toString())) {
      Toast.makeText(mActivity, "请输入每条去皮", Toast.LENGTH_SHORT).show();
      mDatas.get(pos).setRough("");
      myadapter.notifyDataSetChanged();
      return;
    }
    mDatas.get(pos).setRough(s);
    tongJi();
  }

  /**
   * 增加行 1.增加行之前确认头部信息填写
   */
  @Override public void add(int pos) {

    if (TextUtils.isEmpty(tv3.getText().toString())) {
      Toast.makeText(mActivity, "请填写去皮", Toast.LENGTH_SHORT).show();
      return;
    }

    if (mDatas.size() > 0) {
      for (int i = 0; i < mDatas.size(); i++) {
        if (TextUtils.isEmpty(mDatas.get(i).getBreed()) ||
            TextUtils.isEmpty(mDatas.get(i).getNorms()) || TextUtils.isEmpty(
            mDatas.get(i).getRough())) {
          Toast.makeText(mActivity, "请补全信息", Toast.LENGTH_SHORT).show();
          return;
        }
      }
    }
    //if (pos < mDatas.size()) {
    AddItemBean addItemBean = new AddItemBean();
    //addItemBean.setBreed("");//品种
    //addItemBean.setBreedId("");
    //addItemBean.setNorms("0");//规格
    //addItemBean.setRough("0");//毛重
    mDatas.add(addItemBean);
    myadapter.notifyDataSetChanged();
    //}
  }

  @Override public void remove(int pos) {
    if (pos < mDatas.size()) {
      mDatas.remove(pos);
      myadapter.notifyDataSetChanged();
    }
  }

  @Override public void freshen(List<AddProjrctResponse.DataBean> list) {
    this.mProjects = list;
    projectAdapter.notifyDataSetChanged();
  }

  private class MyTextWatch implements TextWatcher {

    private EditText editText;

    public MyTextWatch(EditText editText) {
      this.editText = editText;
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {
      if (s.toString().contains(".")) {
        if (s.length() - 1 - s.toString().indexOf(".")
            > 2) {
          s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
          editText.setText(s);
          editText.setSelection(s.length());
        }
      }

      if (s.toString().trim().substring(0).equals(".")) {
        s = "0" + s;
        editText.setText(s);
        editText.setSelection(2);
      }
      if (s.toString().startsWith("0")
          && s.toString().trim().length() > 1) {
        if (!s.toString().substring(1, 2).equals(".")) {
          editText.setText(s.subSequence(0,
              1));
          editText.setSelection(1);
          return;
        }
      }
    }

    @Override public void afterTextChanged(Editable editable) {

    }
  }

  private class TvWasteProductWatch implements TextWatcher {

    private EditText editText;

    public TvWasteProductWatch(EditText editText) {
      this.editText = editText;
    }

    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {

      if (netWeight > 0) {
        if (s.toString().contains(".")) {
          if (s.length() - 1 - s.toString().indexOf(".")
              > 2) {
            s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
            editText.setText(s);
            editText.setSelection(s.length());
          }
        }
        if (s.toString().trim().substring(0).equals(".")) {
          s = "0" + s;
          editText.setText(s);
          editText.setSelection(2);
        }
        if (s.toString().startsWith("0")
            && s.toString().trim().length() > 1) {
          if (!s.toString().substring(1, 2).equals(".")) {
            editText.setText(s.subSequence(0,
                1));
            editText.setSelection(1);

            return;
          }
        }
        mTvTrueWeight.setText(
            "实重:" + formDecimalFormat(netWeight - Double.parseDouble(s.toString())));
      } else {
        //mTvWasteProductEt.setText(s.toString());
        Toast.makeText(mActivity, "请输入毛重,否则无法计算实际重量！", Toast.LENGTH_SHORT).show();
      }
    }

    @Override public void afterTextChanged(Editable editable) {

    }
  }
}

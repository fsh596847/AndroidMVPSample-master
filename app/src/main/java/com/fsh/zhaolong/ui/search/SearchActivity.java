package com.fsh.zhaolong.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.UntidResponse;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.ui.main.MainActivity;
import com.fsh.zhaolong.ui.unitid.UnitidActity;
import com.fsh.zhaolong.ui.view.AlertDialog;
import java.util.ArrayList;
import java.util.List;

import static com.fsh.zhaolong.ui.increse.AddActivity.INTENT_KEY_UNITNAME;

/**
 * Created by HIPAA on 2016/12/19.
 */

public class SearchActivity extends MvpActivity<SearchPresenter>
    implements SearchView, AlertDialog.CallPayType {

  @Bind(R.id.tvUnitid) TextView tvUnitid;
  @Bind(R.id.tvBreed) TextView tvBreed;
  @Bind(R.id.etSearch) EditText etSearch;
  //交货单位
  private UntidResponse.DataBean dataBean;
  private final int REUEST_CODE = 1;

  //请选择品种
  private List<AddResponse.DataBean> mBreeds = new ArrayList<>();
  private AddResponse.DataBean mBreedBean;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
  }

  @Override protected SearchPresenter createPresenter() {
    return new SearchPresenter(this);
  }

  public void backClick(View view) {
    finish();
  }

  @Override public void init() {

  }

  @Override public void getDataSuccess(AddResponse model) {
    mBreeds = model.getData();
    AlertDialog alertDialog = new AlertDialog(SearchActivity.this).builder();
    alertDialog.setTitle("品种");
    alertDialog.setCallback(this);
    alertDialog.setList(mBreeds);
    alertDialog.show();
  }

  @Override public void getSearchSuccess(AddResponse model) {

  }

  @Override public void getDataFail(String msg) {

  }

  public void unitidClick(View view) {
    Intent intent = new Intent(mActivity, UnitidActity.class);
    startActivityForResult(intent, REUEST_CODE);
  }

  public void breedClick(View view) {
    mvpPresenter.queryVariety(1);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data == null) {
      return;
    }
    dataBean =
        (UntidResponse.DataBean) data.getSerializableExtra(INTENT_KEY_UNITNAME);
    tvUnitid.setText(dataBean.getUnitname());
  }

  public void searchClick(View view) {


    if(!TextUtils.isEmpty(etSearch.getText().toString().toString())||mBreedBean!=null||dataBean!=null){
      if(dataBean!=null){
        MainActivity.CallIntent(mActivity,dataBean.getUnitid(),etSearch.getText().toString());
      }else if(mBreedBean!=null){
        MainActivity.CallIntent1(mActivity,mBreedBean.getVarietyid(),etSearch.getText().toString());
      }else if(dataBean!=null&&mBreedBean!=null){
        MainActivity.CallIntent(mActivity,dataBean.getUnitid(),mBreedBean.getVarietyid(),etSearch.getText().toString());
      }else if(!TextUtils.isEmpty(etSearch.getText().toString())){
        MainActivity.CallIntent2(mActivity,etSearch.getText().toString());
      }
      finish();
    }else {
      Toast.makeText(mActivity,"请输入品种关键字或者选择交货单位 和 品种",Toast.LENGTH_SHORT).show();
    }



    //
    //String userid = PreferenceUtils.getPrefString(mActivity, "userid", null);
    //String unitid = dataBean.getUnitid();
    //String varietyid = mBreedBean.getVarietyid();
    //String varietyName = mBreedBean.getVarietyname();
    //Map<String, String> map = new HashMap<>();
    //
    //map.put("userid", userid);
    //map.put("unitid", unitid);
    //map.put("varietyid", varietyid);
    //mvpPresenter.phoneueryMakCodeDatail(map);

    //if(!TextUtils.isEmpty(dataBean.getUnitid())){
    //  MainActivity.CallIntent(mActivity,dataBean.getUnitid(),etSearch.getText().toString());
    //}else if(!TextUtils.isEmpty(mBreedBean.getVarietyid())&&mBreedBean!=null){
    //  MainActivity.CallIntent1(mActivity,mBreedBean.getVarietyid(),etSearch.getText().toString());
    //}else if((!TextUtils.isEmpty(dataBean.getUnitid())&&dataBean!=null)&&(!TextUtils.isEmpty(mBreedBean.getVarietyid())&&mBreedBean!=null)){
    //  MainActivity.CallIntent(mActivity,dataBean.getUnitid(),mBreedBean.getVarietyid(),etSearch.getText().toString());
    //}

  }

  @Override public void callback(AddResponse.DataBean dataBean, int i) {
    if (!TextUtils.isEmpty(dataBean.getVarietyid())) {
      mBreedBean = dataBean;
      tvBreed.setText(mBreedBean.getVarietyname());
    }
  }
}

package com.fsh.zhaolong.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.FailSuccesssful;
import com.fsh.zhaolong.bean.LonginResponse;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.ui.main.MainActivity;
import com.fsh.zhaolong.util.Const;
import com.fsh.zhaolong.util.PreferenceUtils;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * Created by HIPAA on 2016/12/15.
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView {

  @Bind(R.id.input_name) EditText inputName;
  //@Bind(R.id.layout_name) TextInputLayout layoutName;
  @Bind(R.id.input_password) EditText inputPassword;
  //@Bind(R.id.layout_password) TextInputLayout layoutPassword;
  @Bind(R.id.login) Button login;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
  }

  @Override protected LoginPresenter createPresenter() {
    return new LoginPresenter(this);
  }

  @Override public void init() {
    inputName.addTextChangedListener(new MyTextWatcher(inputName));
    inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
  }

  @OnClick(R.id.login)
  public void login() {
    canLogin();
  }

  @Override public void getDataSuccess(String model) {
    Log.d(LoginActivity.class.getSimpleName(), model.toString());
    try {
      if (!TextUtils.isEmpty(model)) {
        JSONObject jsonObject = new JSONObject(model);
        int status = jsonObject.getInt("status");
        if (status==Const.SUNNESS_STATUE) {
          LonginResponse longinResponse = new Gson().fromJson(model, LonginResponse.class);
          PreferenceUtils.setPrefString(mActivity, "userid", longinResponse.getData().getId());
          PreferenceUtils.setPrefString(mActivity, "mobile",
              longinResponse.getData().getMobile());
          PreferenceUtils.setPrefString(mActivity, "username",
              longinResponse.getData().getUsername());
          Intent intent = new Intent(mActivity, MainActivity.class);
          startActivity(intent);
          finish();
          Toast.makeText(mActivity, "登录成功", Toast.LENGTH_SHORT).show();
        } else {
          FailSuccesssful longinResponse = new Gson().fromJson(model, FailSuccesssful.class);
          Toast.makeText(mActivity, longinResponse.getData(), Toast.LENGTH_SHORT).show();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void getDataFail(String msg) {
    Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
  }

  //动态监听输入过程
  private class MyTextWatcher implements TextWatcher {

    private View view;

    private MyTextWatcher(View view) {
      this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      switch (view.getId()) {
        case R.id.input_name:
          isNameValid();
          break;
        case R.id.input_password:
          isPasswordValid();
          break;
      }
    }
  }

  public boolean isNameValid() {

    if (inputName.getText().toString().trim().equals("") || inputName.getText()
        .toString()
        .trim()
        .isEmpty()) {
      //layoutName.setError(getString(R.string.error_name));
      inputName.requestFocus();
      return false;
    }
    //layoutName.setErrorEnabled(false);
    return true;
  }

  public boolean isPasswordValid() {
    if (inputPassword.getText().toString().trim().equals("") || inputPassword.getText()
        .toString()
        .trim()
        .isEmpty()) {
      //layoutPassword.setErrorEnabled(true);
      inputPassword.setError(getResources().getString(R.string.error_password));
      inputPassword.requestFocus();
      return false;
    }
    //layoutPassword.setErrorEnabled(false);
    return true;
  }

  /**
   * 判断是否可以登录的方法
   */
  private void canLogin() {
    if (!isNameValid()) {
      Toast.makeText(this, getString(R.string.check), Toast.LENGTH_SHORT).show();
      return;
    }
    if (!isPasswordValid()) {
      Toast.makeText(this, getString(R.string.check), Toast.LENGTH_SHORT).show();
      return;
    }
    Map<String, String> map = new HashMap<>();
    map.put("username", inputName.getText().toString());
    map.put("pwd", inputPassword.getText().toString());
    //mvpPresenter.login(inputName.getText().toString(), inputPassword.getText().toString())
    mvpPresenter.login(map);
  }
}

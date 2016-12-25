package com.fsh.zhaolong.ui.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.ui.increse.ProjectAdapter;
import java.util.List;

/**
 * Created by HIPAA on 2016/12/23.
 */

public class SubValuesRecycleAdapter extends BaseAdapter {

  private List<AddProjrctResponse.DataBean> mProjects;
  private Context context;

  public SubValuesRecycleAdapter(
      List<AddProjrctResponse.DataBean> mProjects, Context context) {
    this.mProjects = mProjects;
    this.context = context;
  }

  @Override public int getCount() {
    return mProjects.size();
  }

  @Override public Object getItem(int i) {
    return mProjects.get(i);
  }

  @Override public long getItemId(int i) {
    return i;
  }

  @Override public View getView(final int position, View view, ViewGroup viewGroup) {
    ViewHolder viewHolder = null;
    if (view == null) {

      view = LayoutInflater.from(context).inflate(R.layout.adaper_project, null);
      viewHolder = new ViewHolder(view);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }
    AddProjrctResponse.DataBean dataBean = mProjects.get(position);
    viewHolder.tvproject.setText(dataBean.getProjectname());
    viewHolder.checkbox.setChecked(dataBean.isCheck());
    viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        for (int i = 0; i < mProjects.size(); i++) {
          mProjects.get(position).setCheck(false);
        }

        mProjects.get(position).setCheck(!mProjects.get(position).isCheck());
        notifyDataSetChanged();
        //if (freshen != null) {
        //  freshen.freshen(mProjects);
        //}
      }
    });
    return view;
  }

  ProjectAdapter.Freshen freshen;

  public void setFreshen(ProjectAdapter.Freshen freshen) {
    this.freshen = freshen;
  }

  public interface Freshen {
    void freshen(List<AddProjrctResponse.DataBean> list);
  }

  static class ViewHolder {
    @Bind(R.id.tvproject) TextView tvproject;
    @Bind(R.id.checkbox) CheckBox checkbox;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}

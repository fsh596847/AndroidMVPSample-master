package com.fsh.zhaolong.ui.main.edit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddProjrctResponse;
import java.util.List;

/**
 * Created by HIPAA on 2016/12/25.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHold> {

  Freshen freshen;
  private List<AddProjrctResponse.DataBean> list;
  private Context context;

  public ProjectAdapter(List<AddProjrctResponse.DataBean> list, Context context) {
    this.list = list;
    this.context = context;
  }

  @Override
  public ProjectViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
    ProjectViewHold projectViewHold = new ProjectViewHold(LayoutInflater.from(
        context).inflate(R.layout.adaper_project, parent,
        false));
    return projectViewHold;
  }

  @Override public void onBindViewHolder(ProjectViewHold holder, final int position) {
    if (list.size() > 0) {
      AddProjrctResponse.DataBean dataBean = list.get(position);
      holder.tvProject.setText(dataBean.getProjectname());
      holder.checkBox.setChecked(dataBean.isCheck());
      holder.checkBox.setTag(position);
      holder.checkBox.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          for (int i = 0; i < list.size(); i++) {
            list.get(i).setCheck(false);
          }
          list.get(position).setCheck(!list.get(position).isCheck());
          if (freshen != null) {
            freshen.freshen(list);
          }
        }
      });
    }
  }

  public void setFreshen(Freshen freshen) {
    this.freshen = freshen;
  }

  @Override public int getItemCount() {
    return list.size();
  }

  public interface Freshen {
    void freshen(List<AddProjrctResponse.DataBean> list);
  }

  class ProjectViewHold extends RecyclerView.ViewHolder {

    TextView tvProject;
    CheckBox checkBox;

    public ProjectViewHold(View itemView) {
      super(itemView);
      tvProject = (TextView) itemView.findViewById(R.id.tvproject);
      checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
    }
  }
}

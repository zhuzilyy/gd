package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WeightCarActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    private ListDialog dialog;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_weight_car;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("重车碾压调查表");
        if (dialog == null) {
            dialog = new ListDialog(mContext);
        }
    }

    @OnClick({R.id.iv_back, R.id.ll_departmentName, R.id.ll_pipeName})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_departmentName:
                List<String> nameList = new ArrayList<>();
                nameList.add("涞源管道维护站");
                nameList.add("紫荆关管道维护站");
                nameList.add("琉璃河管道维护站");
                nameList.add("石景山管道维护站");
                nameList.add("固安管道维护站");
                nameList.add("通州管道维护站");
                nameList.add("采育管道维护站");
                nameList.add("门头沟管道维护站");
                nameList.add("怀柔管道维护站");
                nameList.add("密云管道维护站");

                dialog.setData(nameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvDepartmentName.setText(nameList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_pipeName:
                List<String> pipeNameList = new ArrayList<>();
                pipeNameList.add("陕京一线");
                pipeNameList.add("陕京二线");
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvDepartmentName.setText(pipeNameList.get(positionM));
                    dialog.dismiss();
                });
                break;
        }
    }
}

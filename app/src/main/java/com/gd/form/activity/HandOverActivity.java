package com.gd.form.activity;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class HandOverActivity extends BaseActivity {
    @BindView(R.id.tv_ll2_arrow)
    TextView tv_ll2_arrow;
    @BindView(R.id.tv_ll1_circular)
    TextView tv_ll1_circular;
    @BindView(R.id.tv_ll2_circular)
    TextView tv_ll2_circular;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext,R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_hand_over;
    }

    @OnClick({
            R.id.bt_ll1_ok,
    })
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_ll1_ok:
                tv_ll2_circular.setBackgroundResource(R.drawable.circular_red);
                tv_ll2_arrow.setTextColor(getResources().getColor(R.color.red));
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                break;
        }
    }


}

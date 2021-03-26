package com.gd.form.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>类描述：Fragment基类
 * <p>创建人：wh
 * <p>创建时间：2019/6/21
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder=ButterKnife.bind(this, view);
        initView(savedInstanceState);
    }

    protected abstract void initView(Bundle bundle);

    protected abstract int getLayoutId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
     * 以下为startActivity优化
     */
    /**
     * @param target 要跳转的Activity.class
     */
    protected void openActivity(Class<?> target) {
        openActivity(target, null);
    }


    protected void openActivity(Class<?> target, Bundle bundle) {
        try {
            Intent intent = new Intent(context, target);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

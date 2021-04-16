package com.gd.form.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.ViewSwitcher;

import com.gd.form.R;
import com.gd.form.adapter.TodoListAdapter;
import com.gd.form.base.BaseFragment;
import com.gd.form.constants.Constant;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TodoFragment extends BaseFragment {

    @BindView(R.id.todoListRecyclerView)
    RecyclerView todoListRecyclerView;
    @BindView(R.id.todoRefreshLayout)
    SmartRefreshLayout todoRefreshLayout;
    @BindView(R.id.todoListViewSwitcher)
    ViewSwitcher todoListViewSwitcher;

    private int page=1;
    private int listSize=0;
    private List<String> list;
    @Override
    protected void initView(Bundle bundle) {
        todoListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        todoRefreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.finishRefresh(2000);
        });
        todoRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            if(listSize< Constant.PAGE_SIZE){
                ToastUtil.show("没有更多数据了");
            }else{
                page++;

            }
            refreshLayout.finishLoadMore(2000);
        });


        list=new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");

       // list.clear();
        if(Util.isEmpty(list)){
            todoListViewSwitcher.setDisplayedChild(1);
        }else {
            TodoListAdapter adapter = new TodoListAdapter(context, list, R.layout.item_todo_list);
            todoListRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((v, position) -> {
                Log.i("-------","2222222");
                ToastUtil.show("item"+position);
            });

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_todo;
    }
}

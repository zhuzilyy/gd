package com.gd.form.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gd.form.R;


/**
 * 自定义 Indicator 的 ExpandableListView
 */
public class IndicatorExpandActivity extends AppCompatActivity {
    private static final String TAG = "IndicatorExpandActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        final ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandable_list);
        final IndicatorExpandableListAdapter adapter = new IndicatorExpandableListAdapter(Constant.BOOKS, Constant.FIGURES);
        listView.setAdapter(adapter);

        // 清除默认的 Indicator
        listView.setGroupIndicator(null);

        int groupCount = listView.getCount();
        for (int i=0; i<groupCount; i++)
        {
            listView.expandGroup(i);
        };
        //  设置分组项的点击监听事件
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);
                boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                adapter.setIndicatorState(groupPosition, groupExpanded);
                // 请务必返回 false，否则分组不会展开
                return false;
            }
        });

        //  设置子选项点击监听事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(IndicatorExpandActivity.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}

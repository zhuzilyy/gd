package com.gd.form.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.gd.form.R;
import com.gd.form.model.BottomDialogChildBean;

import java.util.List;


public     class BottomDialogChildAdapter extends BaseRecyclerViewAdapter<BottomDialogChildBean> {
    private int parentPosition;
    private boolean isSingleCheck;
    private int pos=-1;


    BottomDialogChildAdapter(Context context, List<BottomDialogChildBean> list, int layoutId, int parentPosition, boolean isSingleCheck){
        super(context, list, layoutId);
        this.parentPosition=parentPosition;
        this.isSingleCheck=isSingleCheck;
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, BottomDialogChildBean bottomDialogChildBean, int position) {
        CheckBox childCheckBox=viewHolder.getView(R.id.textChildCheckBox);
       // ImageView textChildImageView=viewHolder.getView(R.id.textChildImageView);
//        if(childCheckBox!=null) {
//            childCheckBox.setText(bottomDialogChildBean.getText());
//            childCheckBox.setChecked(bottomDialogChildBean.isCheck());
//            if (childCheckBox.isChecked()) {
//                pos=position;
//                textChildImageView.setVisibility(View.VISIBLE);
//            } else {
//                textChildImageView.setVisibility(View.GONE);
//            }
//            childCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
//                //如果不是手动点击则直接返回。没这段则重置状态会执行OnCheckedChangeListener 直至将状态都清空
//                if(!compoundButton.isPressed()){
//                    return;
//                }
//                if(isSingleCheck){
//                    if(pos!=-1){
//                        list.get(pos).setCheck(false);
//                    }
//                    pos=position;
//                    list.get(position).setCheck(b);
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            notifyDataSetChanged();
//                        }
//                    });
//                }
//               // EventBus.getDefault().postSticky(new EventMessage(parentPosition,position,b, Constant.BOTTOMDIALOG));
//                if (b) {
//                    textChildImageView.setVisibility(View.VISIBLE);
//                } else {
//                    textChildImageView.setVisibility(View.GONE);
//                }
//            });
      //  }
    }
}

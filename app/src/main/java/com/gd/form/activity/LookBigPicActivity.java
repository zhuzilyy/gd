package com.gd.form.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.gd.form.R;
import com.gd.form.adapter.HackyViewPager;
import com.gd.form.adapter.ImageScaleAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.EaluationPicBean;
import com.gd.form.utils.CommonUtils;
import com.gd.form.utils.EvaluateUtil;
import com.gd.form.view.PinchImageView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class LookBigPicActivity extends BaseActivity implements View.OnClickListener, ViewTreeObserver.OnPreDrawListener, HackyViewPager.HackyViewPagerDispatchListener{
    private List<EaluationPicBean> picDataList;
    private List<View> dotList = new ArrayList<>();
    public static String PICDATALIST = "PICDATALIST";
    public static String CURRENTITEM = "CURRENTITEM";
    private int currentItem;
    public int mPosition,photoSize;
    private ImageScaleAdapter imageScaleAdapter;
    private HackyViewPager viewPager;
    private LinearLayout ll_dots;
    private TextView tv_viewPage;
    private LinearLayout ll_root;
    private RelativeLayout ll_bottom_all;
    private int height;
    private int width;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_look_big_pic;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intiView();
        getData();
        setUpEvent();
        initDot(currentItem);
    }
    private void setUpEvent() {
        viewPager.setmHackyViewPagerDispatchListener(this);
        viewPager.setAdapter(imageScaleAdapter);
        viewPager.setCurrentItem(currentItem);
        setTitleNum(currentItem);
        setPagerChangeListener(viewPager);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        picDataList = (List<EaluationPicBean>) intent.getSerializableExtra("PICDATALIST");
        photoSize = picDataList.size();
        currentItem = intent.getIntExtra(CURRENTITEM, 0);
        mPosition = currentItem;
        tv_viewPage.setText((currentItem+1)+"/"+photoSize+"");
        imageScaleAdapter = new ImageScaleAdapter(this, picDataList);
    }
    private void intiView() {
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        ll_bottom_all = (RelativeLayout) findViewById(R.id.ll_bottom_all);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        tv_viewPage = (TextView) findViewById(R.id.tv_viewpager);
        viewPager = (HackyViewPager) findViewById(R.id.viewpager);
    }

    /**
     * 绘制前开始动画
     *
     * @return
     */
    @Override
    public boolean onPreDraw() {
        viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = imageScaleAdapter.getPrimaryItem();
        final PinchImageView imageView = (PinchImageView) ((ViewGroup) view).getChildAt(0);

        computeImageWidthAndHeight(imageView);

        final EaluationPicBean ealuationPicBean = picDataList.get(mPosition);
        final float vx = ealuationPicBean.width * 1.0f / width;
        final float vy = ealuationPicBean.height * 1.0f / height;

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedFraction = animation.getAnimatedFraction();

                view.setTranslationX(EvaluateUtil.evaluateInt(animatedFraction, ealuationPicBean.x + ealuationPicBean.width / 2 - imageView.getWidth() / 2, 0));
                view.setTranslationY(EvaluateUtil.evaluateInt(animatedFraction, ealuationPicBean.y + ealuationPicBean.height / 2 - imageView.getHeight() / 2, 0));
                view.setScaleX(EvaluateUtil.evaluateFloat(animatedFraction, vx, 1));
                view.setScaleY(EvaluateUtil.evaluateFloat(animatedFraction, vy, 1));

                ll_root.setBackgroundColor((int) EvaluateUtil.evaluateArgb(animatedFraction, 0x0, 0xff000000));
            }
        });

        addIntoListener(valueAnimator);
        valueAnimator.setDuration(200);
        valueAnimator.start();
        return true;
    }

    /**
     * 进场动画过程监听
     *
     * @param valueAnimator
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_root.setBackgroundColor(0x0);
                //rl_title.setVisibility(View.INVISIBLE);
                ll_bottom_all.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                // rl_title.setVisibility(View.VISIBLE);
                ll_bottom_all.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 页面改动监听
     *
     * @param viewPager
     */
    private void setPagerChangeListener(HackyViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                setTitleNum(position);
                initDot(position);
                tv_viewPage.setText((position+1)+"/"+photoSize+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTitleNum(int position) {
        //    tv_pager.setText((position + 1) + "/" + picDataList.size());
    }

    /**
     * 初始化轮播图点
     */
    private void initDot(int index) {
        // 清空点所在集合
        dotList.clear();
        ll_dots.removeAllViews();
        if (picDataList.size() < 2) {
            return;
        }
        for (int i = 0; i < picDataList.size(); i++) {
            ImageView view = new ImageView(this);
            if (i == index || picDataList.size() == 1) {
                // view.setBackgroundResource(R.mipmap.icon_dot_red);
            } else {
                //  view.setBackgroundResource(R.mipmap.icon_dot);
            }
            // 指定点的大小
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    CommonUtils.dip2px(this, 5), CommonUtils.dip2px(this, 5));
            // 指定点的间距
            layoutParams.setMargins(CommonUtils.dip2px(this, 2), 0, CommonUtils.dip2px(this, 2), 0);
            // 添加到线性布局中
            ll_dots.addView(view, layoutParams);
            // 添加到集合中去
            dotList.add(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.tv_back:
                startActivityAnim();
                break;*/
//            向左旋转
           /* case R.id.bt_left:
                rotation(90.0f);
                break;
//            向右旋转
            case R.id.bt_right:
                rotation(-90.0f);
                break;*/
        }
    }

    /**
     * 旋转
     *
     * @param v
     */
    private void rotation(float v) {
        View primaryView = imageScaleAdapter.getPrimaryItem();
        if (primaryView != null) {
            primaryView.getRotation();
            float rotation = primaryView.getRotation();
            primaryView.setRotation(rotation + v);
            primaryView.requestLayout();
        }
    }

    /**
     * 下面几个回调主要是优化体验的，模范的QQ空间的看图模式
     */
    @Override
    public void isDown() {
        //  ll_bottom.setVisibility(View.GONE);
    }

    @Override
    public void isUp() {
        //  ll_bottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void isCancel() {
        //   ll_bottom.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void onBackPressed() {
////        startActivityAnim();
//    }

    /**
     * 开始activity的动画
     */
    public void startActivityAnim() {
//        得到当前页的View
        final View view = imageScaleAdapter.getPrimaryItem();
        final PinchImageView imageView = (PinchImageView) ((ViewGroup) view).getChildAt(0);
//      当图片被放大时，需要把其缩放回原来大小再做动画
        computeImageWidthAndHeight(imageView);

        final EaluationPicBean ealuationPicBean = picDataList.get(mPosition);
        final float vx = ealuationPicBean.width * 1.0f / width;
        final float vy = ealuationPicBean.height * 1.0f / height;

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedFraction = animation.getAnimatedFraction();

                view.setTranslationX(EvaluateUtil.evaluateInt(animatedFraction, 0, ealuationPicBean.x + ealuationPicBean.width / 2 - imageView.getWidth() / 2));
                view.setTranslationY(EvaluateUtil.evaluateInt(animatedFraction, 0, ealuationPicBean.y + ealuationPicBean.height / 2 - imageView.getHeight() / 2));
                view.setScaleX(EvaluateUtil.evaluateFloat(animatedFraction, 1, vx));
                view.setScaleY(EvaluateUtil.evaluateFloat(animatedFraction, 1, vy));
                ll_root.setBackgroundColor((int) EvaluateUtil.evaluateArgb(animatedFraction, 0xff000000, 0x0));

                if (animatedFraction > 0.95) {
                    view.setAlpha(1 - animatedFraction);
                }
            }
        });
        addOutListener(valueAnimator);
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    /**
     * 计算图片的宽高
     *
     * @param imageView
     */
    private void computeImageWidthAndHeight(PinchImageView imageView) {

//      获取真实大小
        Drawable drawable = imageView.getDrawable();
        if (null != drawable) {
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int intrinsicWidth = drawable.getIntrinsicWidth();
//        计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
            float h = CommonUtils.getScreenSizeHeight(this) * 1.0f / intrinsicHeight;
            float w = CommonUtils.getScreenSizeWidth(this) * 1.0f / intrinsicWidth;
            if (h > w) {
                h = w;
            } else {
                w = h;
            }
//      得出当宽高至少有一个充满的时候图片对应的宽高
            height = (int) (intrinsicHeight * h);
            width = (int) (intrinsicWidth * w);
        }
    }

    /**
     * 退场动画过程监听
     *
     * @param valueAnimator
     */
    private void addOutListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_bottom_all.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

package com.gd.form.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.gd.form.utils.DensityUtil;

public class AppRadioButton extends AppCompatRadioButton {

    private Paint mPaint;
    private boolean isShowDot;
    private boolean isShowNumberDot;

    //小红点半径
    private final int circleDotRadius = DensityUtil.dip2px(4, getContext());
    //icon的尺寸，高 == 宽
    private final int drawableSize = DensityUtil.dip2px(22, getContext());
    //小红点距离icon的padding
    private final int drawablePadding = DensityUtil.dip2px(2,getContext());
    //矩形角标数字左右padding
    private final int rectFPaddingX = DensityUtil.dip2px(4,getContext());
    //矩形角标数字上下padding
    private final int rectFPaddingY = DensityUtil.dip2px(3,getContext());
    //角标矩形背景
    private int rectFRadius =  DensityUtil.dip2px(8,getContext());
    //圆点和角标颜色
    private final int PAINT_COLOR_DEFAULT = Color.parseColor("#FD481E");

    private String numberText;

    /**
     * 圆点和未读消息的坐标
     */
    private float pivotX;
    private float pivotY;

    public AppRadioButton(Context context) {
        super(context);
        init();
    }

    public AppRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(PAINT_COLOR_DEFAULT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getScrollX(), 0);
        pivotX = getWidth() / 2 + drawableSize / 2 + drawablePadding;
        pivotY = getHeight() / 2 - drawableSize / 2 - drawablePadding;
        if (isShowDot) {
            canvas.drawCircle(pivotX, pivotY, circleDotRadius, mPaint);
        } else if (isShowNumberDot && !TextUtils.isEmpty(numberText)) {
            if (Integer.parseInt(numberText) > 99) {
                numberText = "99+";
            }
            if (numberText.length() == 1) {
                rectFRadius =  DensityUtil.dip2px(6,getContext());
            }
            mPaint.setColor(PAINT_COLOR_DEFAULT);
            mPaint.setTextSize( DensityUtil.dip2px(12,getContext()));
            float textWidth = mPaint.measureText(numberText);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float textHeight = Math.abs((fontMetrics.top + fontMetrics.bottom));
            RectF rect = new RectF(pivotX -  DensityUtil.dip2px(4,getContext()), pivotY - textHeight / 2 - rectFPaddingY, pivotX + textWidth + rectFPaddingX * 2, pivotY + textHeight / 2 + rectFPaddingY);
            canvas.drawRoundRect(rect, rectFRadius, rectFRadius, mPaint);
            mPaint.setColor(Color.parseColor("#ffffff"));
            float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2 -  DensityUtil.dip2px(1,getContext());
            mPaint.setTextAlign(Paint.Align.CENTER);
            //绘制文本
            canvas.drawText(numberText, pivotX + textWidth / 2 + rectFPaddingX -  DensityUtil.dip2px(4,getContext()) / 2, baseline, mPaint);
        }
    }

    /**
     * `
     * 设置是否显示小圆点
     */
    public void setShowSmallDot(boolean isShowDot) {
        this.isShowDot = isShowDot;
        invalidate();
    }

    /**
     * 设置是否显示数字
     */
    public void setNumberDot(boolean isShowNumberDot, @NonNull String text) {
        this.isShowNumberDot = isShowNumberDot;
        this.numberText = text;
        if (isShowNumberDot) {
            isShowDot = false;
        }
        invalidate();
    }
}


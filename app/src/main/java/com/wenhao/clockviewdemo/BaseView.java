package com.wenhao.clockviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class BaseView extends View {
    protected final String TAG = getClass().getSimpleName();
    protected float mOriginHeightValue;
    protected float mOriginWidthValue;
    private int mOriginWidthMode;
    private int mOriginHeightMode;


    public BaseView(Context context) {
        super(context);
        init();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mOriginWidthValue = MeasureSpec.getSize(widthMeasureSpec);
        mOriginHeightValue = MeasureSpec.getSize(heightMeasureSpec);
        mOriginWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        mOriginHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.i(TAG, "onMeasure widthValue = " + mOriginWidthValue + " , widthMode = " + getMeasureMode(mOriginWidthMode) + ", heightValue = " + mOriginWidthValue + ", heightMode = " + getMeasureMode(mOriginHeightMode));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private String getMeasureMode(int mode) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
                return "AT_MOST";
            case MeasureSpec.EXACTLY:
                return "EXACTLY";
            case MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                return "UNKNOWN";
        }
    }

    protected void init(){

    }

    // 工具方法：dp转px（适配不同分辨率）
    protected float dp2px(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}

package com.wenhao.clockviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Choreographer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;

public class ClockView extends BaseView {
    private final int DRAW_HOUR_TYPE = 0;
    private final int DRAW_MINUTE_TYPE = 1;
    private final int DRAW_SECOND_TYPE = 2;
    private Paint mHourPointPaint;
    private Paint mMinutePointPaint;
    private Paint mSecondPointPaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mScalePaint;
    float mMainScaleLength;
    float mSubScaleLength;
    float mShaftSeatRadio;
    float mHourOneLength;
    float mHourTwoLineLength;
    float mMinuteOneLineLength;
    float mMinuteTwoLength;
    float mSecondOneLineLength;
    float mHourTextSize;
    float mSuperMainPaintStrokeWidth;
    float mMainScaleStrokeWidth;
    float mSubScaleStrokeWidth;
    float mHourPaintStrokeWidth;
    float mMinutePaintStrokeWidth;
    float mSecondPaintStrokeWidth;
    float mTextMarginScaleValue;
    float mSecondCircleRadius;
    float mSecondTwoLineLength;
    float mVerticalCenter;
    float mHorizontalCenter;
    Path mMinutePath;
    Path mHourPath;
    boolean isShowBorder;
    private float mRadios;
    private float mHourVertexAngel;
    private float mMinuteVertexAngle;
    private int mCircleColor;
    private int mScaleColor;
    private int mHourPointColor;
    private int mMinutePointColor;
    private int mSecondPointColor;
    private int mTextColor;
    private float mHourStrokeWidth;
    private float mMinuteStrokeWidth;
    private float mSecondStrokeWidth;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public class Pointer{
        private float length;
        private int color;
    }

    public class HourPointer extends Pointer{
        private float strokeWidth;
        private float vertexAngle;
    }

    public class MinutePointer extends Pointer{
        private float strokeWidth;
        private float vertexAngle;
    }

    @Override
    protected void init() {
        super.init();
        mMainScaleLength = dp2px(20);
        mSubScaleLength = dp2px(10);
        mShaftSeatRadio = dp2px(3);
        mMainScaleStrokeWidth = dp2px(4);
        mSubScaleStrokeWidth = dp2px(3);
        mTextMarginScaleValue = dp2px(5);
        mSecondCircleRadius = dp2px(5);
        mSecondTwoLineLength = dp2px(20);
        mHourTextSize = dp2px(24);
        mHourVertexAngel = 10;
        mMinuteVertexAngle = 5;
        mCircleColor = Color.GREEN;
        mScaleColor = Color.GREEN;
        mHourPointColor = Color.GREEN;
        mMinutePointColor = Color.GREEN;
        mSecondPointColor = Color.GREEN;
        mTextColor = Color.GREEN;
        mHourStrokeWidth = dp2px(2);
        mMinuteStrokeWidth = dp2px(2);
        mSecondStrokeWidth = dp2px(2);
        mHourTwoLineLength = dp2px(25);
        mMinuteTwoLength = dp2px(30);

        mMinutePath = new Path();
        mHourPath = new Path();
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setAntiAlias(true);
        mScalePaint = new Paint();
        mScalePaint.setColor(mScaleColor);
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mHourTextSize);
        mTextPaint.setAntiAlias(true);
        mHourPointPaint = new Paint();
        mHourPointPaint.setColor(mHourPointColor);
        mHourPointPaint.setAntiAlias(true);
        mHourPointPaint.setStrokeWidth(mHourStrokeWidth);
        mMinutePointPaint = new Paint();
        mMinutePointPaint.setColor(mMinutePointColor);
        mMinutePointPaint.setAntiAlias(true);
        mMinutePointPaint.setStrokeWidth(mMinuteStrokeWidth);
        mMinutePointPaint.setStyle(Paint.Style.STROKE);
        mSecondPointPaint = new Paint();
        mSecondPointPaint.setColor(mSecondPointColor);
        mSecondPointPaint.setAntiAlias(true);
        mSecondPointPaint.setStrokeWidth(mSecondStrokeWidth);
        mSecondPointPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mVerticalCenter = mOriginHeightValue / 2;
        mHorizontalCenter = mOriginWidthValue / 2;
        mRadios = Math.min(mHorizontalCenter, mVerticalCenter) - dp2px(10);
        mHourOneLength = mRadios / 10 * 4;
        mMinuteOneLineLength = mRadios / 10 * 6;
        mSecondOneLineLength = mRadios / 10 * 8;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mHorizontalCenter, mVerticalCenter, mShaftSeatRadio, mScalePaint);
        if (isShowBorder) {
            canvas.drawCircle(mHorizontalCenter, mVerticalCenter, mRadios, mCirclePaint);
        }

        drawText(canvas);

        // 每个刻度的角度间隔：360° / 60 = 6°
        float hour = 360f / 12;
        float minute = 360f / 60;

        for (int i = 0; i < 360; i++) {
            // 计算当前刻度的角度（0°为3点钟，减90°让0°对应12点钟）
            float angle = i - 90f;
            // 角度转弧度
            double rad = Math.toRadians(angle);

            // 判断是否为主刻度（每5个刻度为主要度，对应小时）
            boolean isMainScale = i % hour == 0;
            boolean isSubScale = i % minute == 0;
            if (isMainScale || isSubScale) {
                float scaleLength = isMainScale ? mMainScaleLength : mSubScaleLength;
                // 主刻度加粗

                // 刻度外端点坐标（半径=outerRadius）
                double x1 = mHorizontalCenter + mRadios * Math.cos(rad);
                double y1 = mVerticalCenter + mRadios * Math.sin(rad);

                // 刻度内端点坐标（半径=outerRadius - 刻度长度）
                double x2 = mHorizontalCenter + (mRadios - scaleLength) * Math.cos(rad);
                double y2 = mVerticalCenter + (mRadios - scaleLength) * Math.sin(rad);

                if (isMainScale) {
                    mScalePaint.setStrokeWidth(mMainScaleStrokeWidth);
                } else {
                    mScalePaint.setStrokeWidth(mSubScaleStrokeWidth);
                }

                // 绘制刻度线
                canvas.drawLine((float) x1, (float) y1, (float) x2, (float) y2, mScalePaint);

                if (isMainScale) {
                    canvas.save();
                    float textY = mVerticalCenter - mRadios + mMainScaleLength + mHourTextSize + mTextMarginScaleValue;
                    Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                    float fontHeight = fontMetrics.descent - fontMetrics.ascent;
                    float rotateTextY = textY - mHourTextSize / 2 + mTextMarginScaleValue;
                    canvas.rotate(angle + 90, mHorizontalCenter, mOriginHeightValue / 2);
                    canvas.rotate(-angle - 90, mHorizontalCenter, rotateTextY); // 反向旋转，中心是文字位置

                    String text = String.valueOf(((int) (i / hour)) == 0 ? 12 : ((int) (i / hour)));
                    float textWidth = mTextPaint.measureText(text);
                    float textX = mHorizontalCenter - textWidth / 2;
                    canvas.drawText(text, textX, textY + mTextMarginScaleValue, mTextPaint);
//                    canvas.drawRect(textX, textY - textSize, textX + textWidth, textY, mCirclePaint);
                    canvas.restore();
                }
            }
        }

        drawPoint(DRAW_HOUR_TYPE, canvas);
        drawPoint(DRAW_MINUTE_TYPE, canvas);
        drawPoint(DRAW_SECOND_TYPE, canvas);
    }

    private void drawText(Canvas canvas) {
//        canvas.drawRect();
    }


    private void drawPoint(int type, Canvas canvas) {
        int hourValue = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteValue = Calendar.getInstance().get(Calendar.MINUTE);
        int secondValue = Calendar.getInstance().get(Calendar.SECOND);
        int millisecondValue = Calendar.getInstance().get(Calendar.MILLISECOND);

        float angle = 0;
        float lengthValue = mHourOneLength;
        Paint paint = mHourPointPaint;
        switch (type) {
            case DRAW_HOUR_TYPE:
                lengthValue = mHourOneLength;
                angle = (float) (hourValue * 30 - 90 + minuteValue * 0.5 + secondValue * 0.008333333 + millisecondValue * 0.000008333);
                paint = mHourPointPaint;
                break;
            case DRAW_MINUTE_TYPE:
                lengthValue = mMinuteOneLineLength;
                angle = (float) (minuteValue * 6 - 90 + (secondValue * 0.1) + millisecondValue * 0.0001);
                paint = mMinutePointPaint;
                break;
            case DRAW_SECOND_TYPE:
                lengthValue = mSecondOneLineLength;
                angle = (float) ((secondValue * 6 - 90) + (millisecondValue * 0.006));
                paint = mSecondPointPaint;
                break;
        }
        // 角度转弧度
        double rad = Math.toRadians(angle);
        // 刻度外端点坐标（半径=outerRadius）
        double x = mHorizontalCenter + lengthValue * Math.cos(rad);
        double y = mVerticalCenter + lengthValue * Math.sin(rad);
        switch (type) {
            case DRAW_HOUR_TYPE:
                double leftHourVertexRadians = Math.toRadians(angle - mHourVertexAngel);
                float leftHourVertexX = (float) (mHorizontalCenter + (lengthValue - mHourTwoLineLength) * Math.cos(leftHourVertexRadians));
                float leftHourVertexY = (float) (mVerticalCenter + (lengthValue - mHourTwoLineLength) * Math.sin(leftHourVertexRadians));
                double rightHourVertexRadians = Math.toRadians(angle + mHourVertexAngel);
                float rightHourVertexX = (float) (mHorizontalCenter + (lengthValue - mHourTwoLineLength) * Math.cos(rightHourVertexRadians));
                float rightHourVertexY = (float) (mVerticalCenter + (lengthValue - mHourTwoLineLength) * Math.sin(rightHourVertexRadians));
                mHourPath.reset();
                mHourPath.moveTo(mHorizontalCenter, mVerticalCenter);
                mHourPath.lineTo(leftHourVertexX, leftHourVertexY);
                mHourPath.lineTo((float) x, (float) y);
                mHourPath.lineTo(rightHourVertexX, rightHourVertexY);
                mHourPath.close();
                canvas.drawPath(mHourPath, mMinutePointPaint);
                break;
            case DRAW_MINUTE_TYPE:
                double leftVertexRadians = Math.toRadians(angle - mMinuteVertexAngle);
                float leftVertexX = (float) (mHorizontalCenter + (lengthValue - mMinuteTwoLength) * Math.cos(leftVertexRadians));
                float leftVertexY = (float) (mVerticalCenter + (lengthValue - mMinuteTwoLength) * Math.sin(leftVertexRadians));
                double rightVertexRadians = Math.toRadians(angle + mMinuteVertexAngle);
                float rightVertexX = (float) (mHorizontalCenter + (lengthValue - mMinuteTwoLength) * Math.cos(rightVertexRadians));
                float rightVertexY = (float) (mVerticalCenter + (lengthValue - mMinuteTwoLength) * Math.sin(rightVertexRadians));
                mMinutePath.reset();
                mMinutePath.moveTo(mHorizontalCenter, mVerticalCenter);
                mMinutePath.lineTo(leftVertexX, leftVertexY);
                mMinutePath.lineTo((float) x, (float) y);
                mMinutePath.lineTo(rightVertexX, rightVertexY);
                mMinutePath.close();
                canvas.drawPath(mMinutePath, mMinutePointPaint);
                break;
            case DRAW_SECOND_TYPE:
                double secondCircleX = mHorizontalCenter + (lengthValue - mSecondTwoLineLength) * Math.cos(rad);
                double secondCircleY = mVerticalCenter + (lengthValue - mSecondTwoLineLength) * Math.sin(rad);
                double secondLine1X = mHorizontalCenter + (lengthValue - mSecondTwoLineLength - mSecondCircleRadius) * Math.cos(rad);
                double secondLine1Y = mVerticalCenter + (lengthValue - mSecondTwoLineLength - mSecondCircleRadius) * Math.sin(rad);
                double secondLine2X = mHorizontalCenter + (lengthValue - mSecondTwoLineLength + mSecondCircleRadius) * Math.cos(rad);
                double secondLine2Y = mVerticalCenter + (lengthValue - mSecondTwoLineLength + mSecondCircleRadius) * Math.sin(rad);
                canvas.drawCircle((float) secondCircleX, (float) secondCircleY, mSecondCircleRadius, mSecondPointPaint);
                canvas.drawLine(mHorizontalCenter, mOriginHeightValue / 2, (float) secondLine1X, (float) secondLine1Y, paint);
                canvas.drawLine((float) secondLine2X, (float) secondLine2Y, (float) x, (float) y, paint);
                break;
        }
    }

    // 优化版手动刷新示例（仅应急）
    private Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {

            // 精准刷新局部区域（避免全View重绘）
            invalidate();
            Choreographer.getInstance().postFrameCallback(this); // 继续下一帧
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startManualAnim();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopManualAnim();
    }

    // 启动动画
    public void startManualAnim() {
        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

    // 停止动画（必须手动调用，否则内存泄漏）
    public void stopManualAnim() {
        Choreographer.getInstance().removeFrameCallback(frameCallback);
    }

    public void setMainScaleLength(float mainScaleLength) {
        mMainScaleLength = mainScaleLength;
    }

    public void setSubScaleLength(float subScaleLength) {
        mSubScaleLength = subScaleLength;
    }

    public void setShaftSeatRadio(float shaftSeatRadio) {
        mShaftSeatRadio = shaftSeatRadio;
    }

    public void setHourOneLength(float hourOneLength) {
        mHourOneLength = hourOneLength;
    }

    public void setHourTwoLineLength(float hourTwoLineLength) {
        mHourTwoLineLength = hourTwoLineLength;
    }

    public void setMinuteOneLineLength(float minuteOneLineLength) {
        mMinuteOneLineLength = minuteOneLineLength;
    }

    public void setMinuteTwoLength(float minuteTwoLength) {
        mMinuteTwoLength = minuteTwoLength;
    }

    public void setSecondOneLineLength(float secondOneLineLength) {
        mSecondOneLineLength = secondOneLineLength;
    }

    public void setHourTextSize(float hourTextSize) {
        mHourTextSize = hourTextSize;
    }

    public void setSuperMainPaintStrokeWidth(float superMainPaintStrokeWidth) {
        mSuperMainPaintStrokeWidth = superMainPaintStrokeWidth;
    }

    public void setMainScaleStrokeWidth(float mainScaleStrokeWidth) {
        mMainScaleStrokeWidth = mainScaleStrokeWidth;
    }

    public void setSubScaleStrokeWidth(float subScaleStrokeWidth) {
        mSubScaleStrokeWidth = subScaleStrokeWidth;
    }

    public void setHourPaintStrokeWidth(float hourPaintStrokeWidth) {
        mHourPaintStrokeWidth = hourPaintStrokeWidth;
    }

    public void setMinutePaintStrokeWidth(float minutePaintStrokeWidth) {
        mMinutePaintStrokeWidth = minutePaintStrokeWidth;
    }

    public void setSecondPaintStrokeWidth(float secondPaintStrokeWidth) {
        mSecondPaintStrokeWidth = secondPaintStrokeWidth;
    }

    public void setTextMarginScaleValue(float textMarginScaleValue) {
        mTextMarginScaleValue = textMarginScaleValue;
    }

    public void setSecondCircleRadius(float secondCircleRadius) {
        mSecondCircleRadius = secondCircleRadius;
    }

    public void setSecondTwoLineLength(float secondTwoLineLength) {
        mSecondTwoLineLength = secondTwoLineLength;
    }

    public void setVerticalCenter(float verticalCenter) {
        mVerticalCenter = verticalCenter;
    }

    public void setHorizontalCenter(float horizontalCenter) {
        mHorizontalCenter = horizontalCenter;
    }

    public void setIsShowBorder(boolean showBorder) {
        isShowBorder = showBorder;
    }

    public void setRadios(float radios) {
        mRadios = radios;
    }

    public void setHourVertexAngel(float hourVertexAngel) {
        mHourVertexAngel = hourVertexAngel;
    }

    public void setMinuteVertexAngle(float minuteVertexAngle) {
        mMinuteVertexAngle = minuteVertexAngle;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
    }

    public void setScaleColor(int scaleColor) {
        mScaleColor = scaleColor;
    }

    public void setHourPointColor(int hourPointColor) {
        mHourPointColor = hourPointColor;
    }

    public void setMinutePointColor(int minutePointColor) {
        mMinutePointColor = minutePointColor;
    }

    public void setSecondPointColor(int secondPointColor) {
        mSecondPointColor = secondPointColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setHourStrokeWidth(float hourStrokeWidth) {
        mHourStrokeWidth = hourStrokeWidth;
    }

    public void setMinuteStrokeWidth(float minuteStrokeWidth) {
        mMinuteStrokeWidth = minuteStrokeWidth;
    }

    public void setSecondStrokeWidth(float secondStrokeWidth) {
        mSecondStrokeWidth = secondStrokeWidth;
    }
}

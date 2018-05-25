package com.lucenlee.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class LineView extends View {
    //    圆形虚线
    public static final int STYLE_CIRCLE_IMAGINARY = 0;
    //    矩形虚线
    public static final int STYLE_RECTANGULAR_IMAGINARY = 1;
    //    平行四边形
    public static final int STYLE_RHOMBOID_IMAGINARY = 2;
    //    线点交替
    public static final int STYLE_LINE_POINT_IMAGINARY = 4;
    //    线正方形交替
    public static final int STYLE_LINE_SQUARE_IMAGINARY = 5;
    //    线点点交替
    public static final int STYLE_LINE_DOUBLE_POINT_IMAGINARY = 6;
    //    单实线
    public static final int STYLE_SINGLE_FULL_LINE = 8;
    //    双实线
    public static final int STYLE_DOUBLE_FULL_LINE = 9;
    //    自定义
    public static final int STYLE_CUSTOM_LINE = 10;
    //垂直分割线
    public static final int VERTATION = 1;
    //水平分割线
    public static final int HORIZONTAL = 0;

    //自定义形状
    private Drawable mCustomDrawable;
    //线的类型
    private int mLineStyle = 0;
    //线的开始颜色
    private int mStartColor = Color.GRAY;
    //线的结束颜色
    private int mEndColor = Color.GRAY;
    //画笔
    private Paint mPaint;
    //方向
    private int mOrientation = 0;

    private int mDefaultWidth, mDefaultHeight;
    private int mContentWidth;
    private int mContentHeight;

    public LineView(Context context) {
        super(context);
        init(null, 0);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mDefaultWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics());
        mDefaultHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, getResources().getDisplayMetrics());

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LineView, defStyle, 0);


        mStartColor = a.getColor(
                R.styleable.LineView_lv_line_start_color,
                mStartColor);
        mEndColor = a.getColor(
                R.styleable.LineView_lv_line_end_color,
                mStartColor);

        if (a.hasValue(R.styleable.LineView_lv_custom_shape)) {
            mCustomDrawable = a.getDrawable(
                    R.styleable.LineView_lv_custom_shape);
            mCustomDrawable.setCallback(this);
        }
        mLineStyle = a.getInt(R.styleable.LineView_lv_line_style, mLineStyle);
        mOrientation = a.getInt(R.styleable.LineView_lv_orientation, mOrientation);
        a.recycle();
        // Set up a default TextPaint object
        mPaint = new TextPaint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        mContentWidth = getWidth() - paddingLeft - paddingRight;
        mContentHeight = getHeight() - paddingTop - paddingBottom;

//
//        if (mExampleDrawable != null) {
//            mExampleDrawable.setBounds(paddingLeft, paddingTop,
//                    paddingLeft + contentWidth, paddingTop + contentHeight);
//            mExampleDrawable.draw(canvas);
//        }

        switch (mLineStyle) {

            case STYLE_CIRCLE_IMAGINARY:
                drawCircleImaginary(canvas);
                break;
            case STYLE_RECTANGULAR_IMAGINARY:
                drawRectangularImaginary(canvas);
                break;
            case STYLE_RHOMBOID_IMAGINARY:
                drawRhomboidImaginary(canvas);
                break;
            case STYLE_LINE_POINT_IMAGINARY:
                drawLinePointImaginary(canvas);
                break;
            case STYLE_LINE_SQUARE_IMAGINARY:
                drawSquareImaginary(canvas);
                break;
            case STYLE_LINE_DOUBLE_POINT_IMAGINARY:
                drawLinePointPointImaginary(canvas);
                break;

            case STYLE_SINGLE_FULL_LINE:
                drawSingleFullLine(canvas);
                break;
            case STYLE_DOUBLE_FULL_LINE:
                drawDoubleFullLine(canvas);

                break;
            case STYLE_CUSTOM_LINE:
                drawCustomFullLine(canvas);
                break;

        }


    }

    private void drawCustomFullLine(Canvas canvas) {
        int width = mCustomDrawable.getIntrinsicWidth();
        int height = mCustomDrawable.getIntrinsicHeight();
        float radioWidth = mContentWidth / (width + 0.0f);
        float radioHeight = mContentHeight / (height + 0.0f);
        if (mOrientation == HORIZONTAL) {
            Drawable drawable = zoomDrawable(mCustomDrawable, (int) (width * radioHeight), (int) (height * radioHeight));
            int count = mContentWidth / drawable.getIntrinsicWidth();
            drawable.setCallback(this);
            float unnecessary = (mContentWidth - count * drawable.getIntrinsicWidth()) / 2;
            for (int i = 0; i < count; i++) {
                int left = (int) (unnecessary + getPaddingLeft() + drawable.getIntrinsicWidth() * i);
                int top = getPaddingTop();
                int right = left + drawable.getIntrinsicWidth();
                int bottom = top + drawable.getIntrinsicHeight();
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(canvas);
            }

        } else {
            Drawable drawable = zoomDrawable(mCustomDrawable, (int) (width * radioWidth), (int) (height * radioWidth));
            int count = mContentHeight / drawable.getIntrinsicHeight();
            drawable.setCallback(this);
            float unnecessary = (mContentHeight - count * drawable.getIntrinsicHeight()) / 2;
            for (int i = 0; i < count; i++) {
                int left = getPaddingLeft();
                int top = (int) (unnecessary + getPaddingTop() + drawable.getIntrinsicHeight() * i);
                int right = left + drawable.getIntrinsicWidth();
                int bottom = top + drawable.getIntrinsicHeight();
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(canvas);
            }
        }
    }

    /**
     * 绘制双实线
     *
     * @param canvas
     */
    private void drawDoubleFullLine(Canvas canvas) {
        LinearGradient shader = new LinearGradient(0, 0, mContentWidth, mContentHeight, new int[]{mStartColor, mEndColor}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        if (mOrientation == HORIZONTAL) {
            int lineWidth = mContentHeight / 3;
            canvas.drawRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + mContentWidth, getPaddingTop() + lineWidth, mPaint);
            canvas.drawRect(getPaddingLeft(), getPaddingTop() + lineWidth * 2, getPaddingLeft() + mContentWidth, getPaddingTop() + lineWidth * 3, mPaint);

        } else {
            int lineWidth = mContentWidth / 3;
            canvas.drawRect(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + lineWidth, getPaddingTop() + mContentHeight, mPaint);
            canvas.drawRect(getPaddingLeft() + lineWidth * 2, getPaddingTop(), getPaddingLeft() + lineWidth * 3, getPaddingTop() + mContentHeight, mPaint);

        }
    }

    /**
     * 绘制单实线
     *
     * @param canvas
     */
    private void drawSingleFullLine(Canvas canvas) {
        LinearGradient shader = new LinearGradient(0, 0, mContentWidth, mContentHeight, new int[]{mStartColor, mEndColor}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.drawRect(getPaddingLeft(), getPaddingTop(), mContentWidth - getPaddingLeft() - getPaddingRight(), mContentHeight - getPaddingTop() - getPaddingBottom(), mPaint);
    }

    /**
     * 绘制线点交替
     *
     * @param canvas
     */
    private void drawLinePointImaginary(Canvas canvas) {
        if (mOrientation == HORIZONTAL) {
            int rectHeight = mContentHeight;
            int rectWidth = rectHeight * 2;
            int width = mContentWidth;
            int count = width / rectWidth;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (width - count * rectWidth) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft() + unnecessary + rectWidth * i;
                float top = getPaddingTop();
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                if (i % 2 == 0) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                } else {
                    canvas.drawCircle((left + right) / 2, (top + bottom) / 2, rectHeight / 2, mPaint);
                }
            }
        } else {
            int rectWidth = mContentWidth;
            int rectHeight = rectWidth * 2;
            int height = mContentHeight;
            int count = height / rectHeight;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (height - count * rectHeight) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft();
                float top = getPaddingTop() + rectHeight * i + unnecessary;
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                if (i % 2 == 0) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                } else {
                    canvas.drawCircle((left + right) / 2, (top + bottom) / 2, rectWidth / 2, mPaint);
                }
            }
        }

    }

    /**
     * 绘制线点点交替
     *
     * @param canvas
     */
    private void drawLinePointPointImaginary(Canvas canvas) {
        if (mOrientation == HORIZONTAL) {
            int rectHeight = mContentHeight;
            int rectWidth = rectHeight * 2;
            int width = mContentWidth;
            int count = width / rectWidth;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (width - count * rectWidth) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft() + unnecessary + rectWidth * i;
                float top = getPaddingTop();
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                if (i % 3 == 0) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                } else {
                    canvas.drawCircle((left + right) / 2, (top + bottom) / 2, rectHeight / 2, mPaint);
                }
            }
        } else {
            int rectWidth = mContentWidth;
            int rectHeight = rectWidth * 2;
            int height = mContentHeight;
            int count = height / rectHeight;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (height - count * rectHeight) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft();
                float top = getPaddingTop() + rectHeight * i + unnecessary;
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                if (i % 3 == 0) {
                    canvas.drawRect(left, top, right, bottom, mPaint);
                } else {
                    canvas.drawCircle((left + right) / 2, (top + bottom) / 2, rectWidth / 2, mPaint);
                }
            }
        }

    }

    /**
     * 绘制平行四边形分割线
     *
     * @param canvas
     */
    private void drawRhomboidImaginary(Canvas canvas) {
        if (mOrientation == HORIZONTAL) {
            int rectHeight = mContentHeight;
            int rectWidth = rectHeight * 2;
            int width = mContentWidth;
            int count = width / rectWidth;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (width - count * rectWidth) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                if (i % 2 == 1) continue;
                float left = getPaddingLeft() + unnecessary + rectWidth * i;
                float top = getPaddingTop();
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                Path path = new Path();
                path.moveTo(left, top);
                path.lineTo(right, top);
                path.lineTo(right + rectWidth / 2f, bottom);
                path.lineTo(left + rectWidth / 2f, bottom);
                path.lineTo(left, top);
                canvas.drawPath(path, mPaint);
            }
        } else {
            int rectWidth = mContentWidth;
            int rectHeight = rectWidth * 2;
            int height = mContentHeight;
            int count = height / rectHeight;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (height - count * rectHeight) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                if (i % 2 == 1) continue;
                float left = getPaddingLeft();
                float top = getPaddingTop() + unnecessary + rectHeight * i;
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                Path path = new Path();
                path.moveTo(left, top);
                path.lineTo(right, top + rectHeight / 2);
                path.lineTo(right, bottom + rectHeight / 2);
                path.lineTo(left, bottom);
                path.lineTo(left, top);
                canvas.drawPath(path, mPaint);
            }
        }
    }

    /**
     * 绘制矩形分割线
     *
     * @param canvas
     */
    private void drawRectangularImaginary(Canvas canvas) {
        if (mOrientation == HORIZONTAL) {
            int rectHeight = mContentHeight;
            int rectWidth = rectHeight * 2;
            int width = mContentWidth;
            int count = width / rectWidth;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (width - count * rectWidth) / 2F;
            for (int i = 0; i < count; i++) {
                mPaint.setColor(colorList[i]);
                if (i % 2 == 1) continue;
                float left = getPaddingLeft() + unnecessary + rectWidth * i;
                float top = getPaddingTop();
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        } else {
            int rectWidth = mContentWidth;
            int rectHeight = rectWidth * 2;
            int height = mContentHeight;
            int count = height / rectHeight;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (height - count * rectHeight) / 2F;
            for (int i = 0; i < count; i++) {
                if (i % 2 == 1) continue;
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft();
                float top = getPaddingTop() + rectHeight * i + unnecessary;
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 绘制正方形分割线
     *
     * @param canvas
     */
    private void drawSquareImaginary(Canvas canvas) {
        if (mOrientation == HORIZONTAL) {
            int rectHeight = mContentHeight;
            int rectWidth = rectHeight;
            int width = mContentWidth;
            int count = width / rectWidth;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (width - count * rectWidth) / 2F;
            for (int i = 0; i < count; i++) {
                if (i % 2 == 1) continue;
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft() + unnecessary + rectWidth * i;
                float top = getPaddingTop();
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        } else {
            int rectWidth = mContentWidth;
            int rectHeight = rectWidth;
            int height = mContentHeight;
            int count = height / rectHeight;
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (height - count * rectHeight) / 2F;
            for (int i = 0; i < count; i++) {
                if (i % 2 == 1) continue;
                mPaint.setColor(colorList[i]);
                float left = getPaddingLeft();
                float top = getPaddingTop() + rectHeight * i + unnecessary;
                float right = left + rectWidth;
                float bottom = top + rectHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 绘制圆点虚线
     *
     * @param canvas
     */
    private void drawCircleImaginary(Canvas canvas) {

        if (mOrientation == HORIZONTAL) {
            int radius = mContentHeight / 2;
            int width = mContentWidth;
            int count = width / (radius * 2);
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (width - count * radius * 2) / 2F;
            for (int i = 0; i < count; i++) {
                if (i % 2 == 1) continue;
                mPaint.setColor(colorList[i]);
                float centerX = getPaddingLeft() + unnecessary + radius + radius * 2 * i;
                canvas.drawCircle(centerX, getPaddingTop() + radius, radius, mPaint);
            }
        } else {
            int radius = mContentWidth / 2;
            int height = mContentHeight;
            int count = height / (radius * 2);
            int[] colorList = getColorList(mStartColor, mEndColor, count);
            float unnecessary = (height - count * radius * 2) / 2F;
            for (int i = 0; i < count; i++) {
                if (i % 2 == 1) continue;
                mPaint.setColor(colorList[i]);
                float centerY = getPaddingTop() + unnecessary + radius + radius * 2 * i;
                canvas.drawCircle(getPaddingLeft() + radius, centerY, radius, mPaint);
            }
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.EXACTLY) {
            setMeasuredDimension(sizeWidth, sizeHeight);
        } else if (modeWidth == MeasureSpec.EXACTLY) {
            setMeasuredDimension(sizeWidth, mDefaultHeight);
        } else if (modeHeight == MeasureSpec.EXACTLY) {
            setMeasuredDimension(mDefaultWidth, sizeHeight);
        } else {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        }


    }

    /**
     * 生成渐变颜色
     *
     * @param startColor
     * @param endColor
     * @param count
     * @return
     */
    public int[] getColorList(int startColor, int endColor, int count) {
        int[] values = new int[count];
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        for (int i = 0; i < count; i++) {
            float radio = i / (count + 0.0f);
            int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
            int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
            int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
            int argb = Color.argb(255, red, greed, blue);
            values[i] = argb;
        }

        return values;
    }

    /**
     * 缩放drawable
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(getResources(), newbmp);
    }

    /**
     * 把drawable转化为bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

}

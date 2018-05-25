package com.lucenlee.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RectPasswordText extends TextView implements ActionMode.Callback {

    private Paint mFocusPaint;
    private Paint mNoFocusPaint;
    private Paint mTextPaint;
    //密码长度
    private int mLength = 6;
    //是否明文显示
    private boolean mProclaimedShow = false;
    //聚焦颜色
    private int mFocusColor = Color.BLACK, mNoFocusColor = Color.GRAY;
    //矩形之间的间隔
    private int mRectMargin = 0;
    //边框宽度
    private float mBorderWidth;
    //测量总高度
    private int mMeasuredHeight;
    //测量总宽度
    private int mMeasuredWidth;
    //绘制正方形的边长
    private int mSquareSlide;

    public RectPasswordText(Context context) {
        this(context, null);
    }

    public RectPasswordText(Context context,  AttributeSet attrs) {
        this(context, attrs, R.attr.rectanglePasswordTextStyle);
    }

    public RectPasswordText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RectPasswordText);
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1F, getResources().getDisplayMetrics());
        mSquareSlide = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24F, getResources().getDisplayMetrics());
        for (int i = 0; i < a.length(); i++) {
            int index = a.getIndex(i);
            if (index == R.styleable.RectPasswordText_prt_length) {
                mLength = a.getInteger(index, mLength);
            } else if (index == R.styleable.RectPasswordText_prt_margin) {
                mRectMargin = (int) a.getDimension(index, mRectMargin);

            } else if (index == R.styleable.RectPasswordText_prt_proclaimed_show) {
                mProclaimedShow = a.getBoolean(index, mProclaimedShow);

            } else if (index == R.styleable.RectPasswordText_prt_rectangle_color_focus) {
                mFocusColor = a.getColor(index, mFocusColor);

            } else if (index == R.styleable.RectPasswordText_prt_rectangle_color_no_focus) {
                mNoFocusColor = a.getColor(index, mNoFocusColor);

            } else if (index == R.styleable.RectPasswordText_prt_border_width) {
                mBorderWidth = a.getDimension(index, mBorderWidth);

            }


        }
        a.recycle();
        initFocusPaint();
        initNoFocusPaint();
        initTextPaint();
        initInputStyle();
    }

    private void initInputStyle() {
        setFocusableInTouchMode(true);
        setFocusable(true);
        setCursorVisible(false);
        setLongClickable(false);
        setTextIsSelectable(false);
        setSingleLine();
        setMaxLines(1);
        setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLength)});
        setCustomSelectionActionModeCallback(this);

    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mNoFocusColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initNoFocusPaint() {
        mNoFocusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNoFocusPaint.setStyle(Paint.Style.STROKE);
        mNoFocusPaint.setColor(mNoFocusColor);
        mNoFocusPaint.setStrokeWidth(mBorderWidth);
    }

    private void initFocusPaint() {
        mFocusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFocusPaint.setColor(mFocusColor);
        mFocusPaint.setStyle(Paint.Style.STROKE);
        mFocusPaint.setStrokeWidth(mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawRectText(canvas);

    }

    private void drawRectText(Canvas canvas) {
        List<Rect> rects = new ArrayList<>();
        int startTop = (mMeasuredHeight - mSquareSlide) / 2;
        int startLeft = getPaddingLeft() + mRectMargin;
        String text = getText().toString();
        for (int i = 0; i < mLength; i++) {
            Rect rect = new Rect(startLeft, startTop, startLeft + mSquareSlide, startTop + mSquareSlide);
            canvas.drawRect(rect, mNoFocusPaint);
            startLeft = startLeft + mSquareSlide + mRectMargin;
            rects.add(rect);
        }

        int length = text.length();
        if (isFocused() && length < mLength) {
            Rect mFocusRect = rects.get(length);
            canvas.drawRect(mFocusRect, mFocusPaint);
        }
        String s = text;
        for (int i = 0; i < s.length(); i++) {
            Rect rect = rects.get(i);

            if (!mProclaimedShow) {
                canvas.drawCircle(rect.centerX(), rect.centerY(), getTextSize() / 2, mTextPaint);
            } else {

                Paint.FontMetrics fm = mTextPaint.getFontMetrics();
                float bottom = fm.bottom;
                float top = fm.top;
                int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
                canvas.drawText(s.substring(i, i + 1), rect.centerX(), baseLineY, mTextPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (modeWidth == MeasureSpec.AT_MOST && modeHeight == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mSquareSlide * mLength + mRectMargin * (mLength + 1) + getPaddingRight() + getPaddingLeft(),
                    getPaddingTop() + getPaddingBottom() + mSquareSlide);

        } else if (modeWidth == MeasureSpec.AT_MOST) {
            setMeasuredDimension(heightSize * mLength + mRectMargin * (mLength + 1) + getPaddingRight() + getPaddingLeft(), heightSize);


        } else if (modeWidth == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, getPaddingTop() + getPaddingBottom() + widthSize / mLength);
        } else {
            setMeasuredDimension(widthSize, heightSize);
        }

        mMeasuredHeight = getMeasuredHeight();
        mMeasuredWidth = getMeasuredWidth();
        int height = mMeasuredHeight - getPaddingTop() - getPaddingBottom();
        int width = mMeasuredWidth - getPaddingLeft() - getPaddingRight() - (mLength + 1) * mRectMargin;
        int tmpWidth = width / mLength;
        mSquareSlide = Math.min(tmpWidth, height);

    }

    @Override
    public boolean getFreezesText() {
        return true;
    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

    @Override
    protected MovementMethod getDefaultMovementMethod() {
        return ArrowKeyMovementMethod.getInstance();
    }

    @Override
    public Editable getText() {
        return (Editable) super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, BufferType.EDITABLE);
    }


    @Override
    public void setEllipsize(TextUtils.TruncateAt ellipsis) {
        if (ellipsis == TextUtils.TruncateAt.MARQUEE) {
            throw new IllegalArgumentException("EditText cannot use the ellipsize mode "
                    + "TextUtils.TruncateAt.MARQUEE");
        }
        super.setEllipsize(ellipsis);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return EditText.class.getName();
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}

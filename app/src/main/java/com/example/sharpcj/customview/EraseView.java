package com.example.sharpcj.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sharpcj on 6/22/17.
 */

public class EraseView extends View {
    private int mCoverColor;
    private String mCoverText;
    private int mCoverTextColor;
    private Bitmap mBgBitmap;
    private Paint mPaint;
    private Bitmap mFgBitmap;
    private Canvas mCanvas;
    private Paint mTextPaint;
    private Paint mPathPaint;
    private Path mPath;


    public EraseView(Context context) {
        super(context);
    }

    public EraseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context,attrs);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs,R.styleable.EraseView);
        mCoverColor = typeArray.getInteger(R.styleable.EraseView_cover_color, Color.GRAY);
        mCoverText = typeArray.getString(R.styleable.EraseView_cover_text);
        mCoverTextColor = typeArray.getInteger(R.styleable.EraseView_cover_text_color, Color.WHITE);
        Drawable bgPicture = typeArray.getDrawable(R.styleable.EraseView_src);
        if(bgPicture instanceof BitmapDrawable){
            mBgBitmap = ((BitmapDrawable) bgPicture).getBitmap();
        }
        typeArray.recycle();
    }

    private void init(){
        mPaint =  new Paint();
        // 初始化覆盖层画布
        mFgBitmap = Bitmap.createBitmap(mBgBitmap.getWidth(),mBgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mFgBitmap);
        // 绘制覆盖层颜色
        mCanvas.drawColor(mCoverColor);
        // 绘制覆盖层文字
        mTextPaint = new Paint();
        mTextPaint.setColor(mCoverTextColor);
        mTextPaint.setTextSize(100);
        mTextPaint.setStrokeWidth(20);
        mCanvas.drawText(mCoverText,mCanvas.getWidth()/8,mCanvas.getHeight()/4,mTextPaint);
        // 初始化擦拭路径
        mPath = new Path();
        // 初始化擦拭画笔
        mPathPaint = new Paint();
        mPathPaint.setAlpha(0);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(50);
        mPathPaint.setXfermode(new PorterDuffXfermode((PorterDuff.Mode.DST_IN)));  //取两层绘制交集，显示下层
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(500, 500);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(500, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, 500);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBgBitmap,0,0,null);
        canvas.drawBitmap(mFgBitmap,0,0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPaint.reset();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                break;
        }
        mCanvas.drawPath(mPath,mPathPaint);
        invalidate();
        return true;
    }
}

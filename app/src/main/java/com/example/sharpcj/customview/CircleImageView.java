package com.example.sharpcj.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sharpcj on 6/22/17.
 */

public class CircleImageView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;
    private Paint mCirclePaint;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        Drawable drawable = typeArray.getDrawable(R.styleable.CircleImageView_img_src);
        if (drawable instanceof BitmapDrawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        typeArray.recycle();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStyle(Paint.Style.FILL);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = 150;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = 150;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int bWidth = mBitmap.getWidth();
        int bHeight = mBitmap.getHeight();
        int length = width <= height ? width : height;

        Bitmap currentBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(currentBitmap);
        mCanvas.drawCircle(width/2.0f,height/2.0f,length/2.0f,mCirclePaint);

        int saveIndex = canvas.saveLayer(0,0,width,height,null,Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mBitmap,new Rect(0,0,bWidth,bHeight),new Rect(0,0,width,height),mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(currentBitmap,0,0,mPaint);

        canvas.restoreToCount(saveIndex);
		currentBitmap.recycle();
    }
}

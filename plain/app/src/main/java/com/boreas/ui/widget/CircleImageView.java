package com.boreas.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

/**
 * 作者 boreas
 * 日期 18-3-13
 * 邮箱 13051089921@163.com
 * @author boreas
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = CircleImageView.class.getSimpleName();
    private Paint mPaint = null;
    private Drawable mDrawable = null;
    private Context context = null;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        this.mPaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = null;
        if(this.mDrawable == null){
            drawable = getDrawable();
        }else{
            drawable = this.mDrawable;
        }
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = getCircleBitmap(bitmap, 14);
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            mPaint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, mPaint);

        } else {
            super.onDraw(canvas);
        }

    }
    /**
     * 获取圆形图片方法
     * @param bitmap
     * @param pixels
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getCircleBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mPaint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        mPaint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawCircle(x / 2, x / 2, x / 2, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, mPaint);
        return output;


    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mDrawable = new BitmapDrawable(bitmap);
            invalidate();
            return;
        }
        throw new NullPointerException(TAG + "\t  setBitmap   bitmap is not null !!!");
    }

    public void setDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mDrawable = drawable;
            invalidate();
            return;
        }
        throw new NullPointerException(TAG + "\t setDrawable  bitmap is not null !!!");
    }

    public void setResources(int resourcesId) {
        Drawable drawable = this.context.getResources().getDrawable(resourcesId);
        if (drawable != null) {
            this.mDrawable = drawable;
            invalidate();
            return;
        }
        throw new NullPointerException(TAG + "\tsetResources  bitmap is not null !!!");
    }

}

package com.triplanner.triplanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {
    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        int min = Math.min(getWidth(), getHeight());
//        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, min, min, false);
//        Bitmap roundedBitmap = getRoundedBitmap(scaledBitmap);
//        canvas.drawBitmap(roundedBitmap, 0, 0, null);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        if (drawable == null) {
            return;
        }

        int min = Math.min(getWidth(), getHeight());
        if (min <= 0) {
            return;
        }

        Bitmap sourceBitmap = drawable.getBitmap();
        if (sourceBitmap == null) {
            return;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, min, min, false);
        Bitmap roundedBitmap = getRoundedBitmap(scaledBitmap);
        canvas.drawBitmap(roundedBitmap, 0, 0, null);
    }


    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, min, min);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}

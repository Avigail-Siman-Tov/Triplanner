package com.triplanner.triplanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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

        // Increase the thickness of the frame
        int frameThickness = (int) (8 * getResources().getDisplayMetrics().density); // Convert dp to pixels
        int frameRadius = min / 2;

        // Draw the circular frame directly on the canvas
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.parseColor("#29AEBF"));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(frameThickness);

        // Calculate the coordinates for drawing the frame
        int frameLeft = frameThickness / 2;
        int frameTop = frameThickness / 2;
        int frameRight = min - frameThickness / 2;
        int frameBottom = min - frameThickness / 2;

        // Draw the circular frame
        canvas.drawCircle(frameRadius, frameRadius, frameRadius - frameThickness / 2, borderPaint);

        // Draw the rounded image inside the circular frame
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(sourceBitmap, min - frameThickness, min - frameThickness, false);
        Bitmap roundedBitmap = getRoundedBitmap(scaledBitmap);

        // Draw the rounded image inside the circular frame
        canvas.drawBitmap(roundedBitmap, frameLeft, frameTop, null);
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

package com.playground.viewsplayground.custom;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;


public class PasswordEditText extends android.support.v7.widget.AppCompatEditText {

    public PasswordEditText(Context context) {
        super(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.save();


       int xPosition=getWidth()-(Math.round(getWidth()*0.3f));


        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        paint.setColor(Color.BLACK);


        paint.setTextSize(160);

        int yPosition=Math.round(getHeight()*0.9f);

        canvas.drawText("Some Text", xPosition, yPosition, paint);

        canvas.restore();



    }
}

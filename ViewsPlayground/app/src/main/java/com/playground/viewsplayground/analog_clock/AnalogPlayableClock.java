package com.playground.viewsplayground.analog_clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Talha Hasan Zia on 08-May-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class AnalogPlayableClock extends View {
    public AnalogPlayableClock(Context context) {
        super(context);
    }

    public AnalogPlayableClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnalogPlayableClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnalogPlayableClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init() {


    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(12);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, 300, paint);
        paint.setStrokeWidth(24);
        paint.setColor(Color.RED);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight() - (getHeight() / 1.55f), paint);

        paint.setStrokeWidth(20);
        paint.setColor(Color.BLUE);

        double length = 280;

        Calendar calendar=Calendar.getInstance();

        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 30);

        double angleFromTime=getAngleFromTime(calendar.getTimeInMillis());
        Log.d("ANACLOCK", "onDraw: "+angleFromTime);
        double endX =  (length * Math.cos(angleFromTime))+getWidth() / 2;
        double endY =  (length * Math.sin(angleFromTime))+getHeight() / 2;

        Log.d("ANACLOCK", "onDraw: "+ endX+","+endY);
        canvas.drawLine(getWidth() / 2, getHeight() / 2, (float)endX, (float) endY, paint);


    }

    private double getAngleFromTime(long timeInMilliseconds) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilliseconds);

        int hour=calendar.get(Calendar.HOUR);
        int minutes=calendar.get(Calendar.MINUTE);

        Log.d("ANACLOCK", "getAngleFromTime: hour: "+hour);
        Log.d("ANACLOCK", "getAngleFromTime: mins: "+minutes);

        if(hour>12)
            hour=hour-12;

        double totalInput=(hour*60)+minutes;
        

        double degreeInAngles=totalInput*6;

        Log.d("ANACLOCK", "getAngleFromTime: "+degreeInAngles);

        return  Math.toRadians(degreeInAngles-90);


    }
}

package com.playground.viewsplayground.analog_clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.playground.viewsplayground.R;

import java.util.Calendar;

/**
 * Created by Talha Hasan Zia on 08-May-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class AnalogPlayableClock extends View {

    private static final String TAG = "ANACLOCK";
    private int hour, minutes;
    private double smallHandStartX, smallHandEndX, bigHandStartX, bigHandEndX, smallHandStartY, smallHandEndY, bigHandStartY, bigHandEndY;
    private float smallHandLength, bigHandLength, dialRadius;
    private float smallHandWidth, bigHandWidth, dialWidth;
    private float validPadding;
    private double smallHandSlope, bigHandSlope;
    private int smallHandColor, bigHandColor;
    private boolean isDrawingAllowed = true;
    private boolean isActive, mAttached;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isActive)
                updateTime();
        }
    };
    private int dialColor;

    public AnalogPlayableClock(Context context) {
        super(context);
        init(context, null);
    }

    public AnalogPlayableClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnalogPlayableClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AnalogPlayableClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {

            TypedArray attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.AnalogPlayableClock);


            hour = attributeArray.getInteger(R.styleable.AnalogPlayableClock_start_hour, Calendar.getInstance().get(Calendar.HOUR));
            minutes = attributeArray.getInteger(R.styleable.AnalogPlayableClock_start_minutes, Calendar.getInstance().get(Calendar.MINUTE));
            dialColor = attributeArray.getColor(R.styleable.AnalogPlayableClock_dial_color, Color.BLUE);
            smallHandColor = attributeArray.getColor(R.styleable.AnalogPlayableClock_small_hand_color, Color.RED);
            bigHandColor = attributeArray.getColor(R.styleable.AnalogPlayableClock_big_hand_color, Color.BLUE);
            attributeArray.recycle();

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isDrawingAllowed = true;

        validPadding = getValidPadding();


        // ensure that width and height is same
        int measuredHeight = 0;
        int measuredWidth = 0;
        try {
            measuredWidth = measureWidth(widthMeasureSpec);
            measuredHeight = measureHeight(heightMeasureSpec);

            if (measuredHeight != measuredWidth) {
                isDrawingAllowed = false;
                throw new Exception(AnalogPlayableClock.class.getCanonicalName() + ": Height and Width of this view must be same.");
            }

            if (normalizeValue(validPadding) > measuredWidth / 2) {
                isDrawingAllowed = false;
                throw new Exception(AnalogPlayableClock.class.getCanonicalName() + ": Invalid padding values");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAttachedToWindow()) {

            if (!mAttached) {
                mAttached = true;
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_TIME_TICK);
                filter.addAction(Intent.ACTION_TIME_CHANGED);
                filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

                getContext().registerReceiver(mIntentReceiver, filter);
            }
        }
    }

    private int measureHeight(int measureSpec) throws Exception {
        // ensure that width and height is same
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;

        if (specMode == MeasureSpec.EXACTLY) { // only allowed fixed size or match parent
            result = specSize;
        } else {
            isDrawingAllowed = false;
            throw new Exception(AnalogPlayableClock.class.getCanonicalName() + ": Height of this view cannot be wrap_content");
        }

        return result;
    }

    private int measureWidth(int measureSpec) throws Exception {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;

        if (specMode == MeasureSpec.EXACTLY) { // only allowed fixed size or match parent
            result = specSize;
        } else {
            isDrawingAllowed = false;
            throw new Exception(AnalogPlayableClock.class.getCanonicalName() + ": Height of this view cannot be wrap_content");
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float viewMidX = getWidth() / 2;
        float viewMidY = getHeight() / 2;

        if (isDrawingAllowed) {

            dialRadius = getWidth() / 2.25f;
            smallHandStartX = viewMidX;
            smallHandStartY = viewMidY;

            bigHandStartX = smallHandStartX;
            bigHandStartY = smallHandStartY;


            bigHandLength = getHeight() - (getHeight() / 1.45f);
            smallHandLength = bigHandLength * 0.6f;


            smallHandWidth = getWidth() * 0.02f;
            bigHandWidth = getWidth() * 0.01f;
            dialWidth = smallHandWidth;

            drawDial(canvas, viewMidX, viewMidY);

            drawBigHand(canvas, viewMidX, viewMidY);

            drawSmallHand(canvas, viewMidX, viewMidY);


        } else {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            paint.setTextSize(getHeight() * 0.1f);
            canvas.drawText("Error, See logs.", 4, viewMidY, paint);
        }
    }

    private void drawBigHand(Canvas canvas, float viewMidX, float viewMidY) {
        Paint paint = new Paint();
        paint.setStrokeWidth(bigHandWidth);
        paint.setColor(bigHandColor);


        bigHandSlope = getMinutesAngleFromTime();
        bigHandEndX = (bigHandLength * Math.cos(bigHandSlope)) + getWidth() / 2;
        bigHandEndY = (bigHandLength * Math.sin(bigHandSlope)) + getHeight() / 2;

        canvas.drawLine(viewMidX, viewMidY, (float) bigHandEndX, (float) bigHandEndY, paint);
    }

    private void drawSmallHand(Canvas canvas, float viewMidX, float viewMidY) {
        Paint paint = new Paint();
        paint.setStrokeWidth(smallHandWidth);
        paint.setColor(smallHandColor);

        smallHandSlope = getHoursAngleFromTime();
        smallHandEndX = (smallHandLength * Math.cos(smallHandSlope)) + viewMidX;
        smallHandEndY = (smallHandLength * Math.sin(smallHandSlope)) + viewMidY;

        canvas.drawLine(viewMidX, viewMidY, (float) smallHandEndX, (float) smallHandEndY, paint);

    }

    private void drawDial(Canvas canvas, float viewMidX, float viewMidY) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(dialColor);
        paint.setStrokeWidth(dialWidth);
        canvas.drawCircle(viewMidX, viewMidY, dialRadius, paint);
    }

    private double getMinutesAngleFromTime() {

        Log.d(TAG, "getMinutesAngleFromTime: hour: " + hour);
        Log.d(TAG, "getMinutesAngleFromTime: mins: " + minutes);

        if (hour > 12)
            hour = hour - 12;

        double totalInput = (hour * 60) + minutes;


        double degreeInAngles = totalInput * 6;

        return Math.toRadians(degreeInAngles - 90);


    }

    private double getHoursAngleFromTime() {

        Log.d("ANACLOCK", "getMinutesAngleFromTime: hour: " + hour);

        if (hour > 12)
            hour = hour - 12;


        float hourDecimal = hour + (minutes / 60f);

        double degreeInAngles = hourDecimal * 30;


        return Math.toRadians(degreeInAngles - 90);


    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
        invalidate();
    }

    public void setHour(int hour) {
        this.hour = hour;
        invalidate();
    }

    private float getValidPadding() {
        float[] paddings = {getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom(), getPaddingStart(), getPaddingEnd()};

        float validPadding = paddings[0];

        for (int i = 1; i < paddings.length; i++) {
            if (validPadding < paddings[i])
                validPadding = paddings[i];
        }

        return validPadding;
    }

    private float normalizeValue(float val) {
        return val < 0 ? val * (-1) : val;
    }

    private void updateTime() {

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minutes = calendar.get(Calendar.MINUTE);

        invalidate();

    }
}

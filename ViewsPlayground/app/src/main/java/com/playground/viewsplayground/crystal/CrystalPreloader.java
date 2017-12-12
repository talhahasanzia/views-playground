package com.playground.viewsplayground.crystal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.playground.viewsplayground.R;

public class CrystalPreloader extends View {

    ////////////////////////////////////////
    // PUBLIC CLASS CONSTANTS
    ////////////////////////////////////////

    public static final class Size{
        public static final int VERY_SMALL  = 0;
        public static final int SMALL       = 1;
        public static final int MEDIUM      = 2;
        public static final int LARGE       = 3;
        public static final int EXTRA_LARGE = 4;
    }

    public static final class Style{
        public static final int SKYPE_BALLS = 0;
        public static final int HASHER = 1;
        public static final int ALTERNATIVE = 2;
        public static final int TRIPLEX = 3;
        public static final int TIME_MACHINE = 4;
        public static final int CHRONOS = 5;
        public static final int IN_CIRCLE = 6;
        public static final int VENTILATOR = 7;
        public static final int PULSE = 8;
        public static final int HALF_MOON = 9;
        public static final int BALL_PULSE = 10;
        public static final int BALL_SCALE = 11;
        public static final int BALL_SPIN_FADE = 12;
        public static final int BALL_PULSE_SYNC = 13;
        public static final int BALL_BEAT = 14;
        public static final int LINE_SCALE = 15;
        public static final int LINE_SCALE_PULSE_OUT = 16;
        public static final int LINE_SCALE_PULSE_OUT_RAPID = 17;
        public static final int EXPANDABLE_BALLS = 18;
        public static final int CIRCULAR = 19;
        public static final int BALL_RING = 20;
        public static final int TORNADO_CIRCLE_1 = 21;
        public static final int TORNADO_CIRCLE_2 = 22;
        public static final int TORNADO_CIRCLE_3 = 23;
    }

    ////////////////////////////////////////
    // PRIVATE VAR
    ////////////////////////////////////////

    private BasePreloader loader;
    private Paint fgPaint;
    private Paint bgPaint;
    private int fgColor;
    private int bgColor;
    private int size;
    private int style;

    ////////////////////////////////////////
    // CONSTRUCTOR
    ////////////////////////////////////////

    public CrystalPreloader(Context context) {
        this(context, null);
    }

    public CrystalPreloader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrystalPreloader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // prevent render is in edit mode
        //if(isInEditMode()) return;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CrystalPreloader);
        try{
            fgColor = getFgColor(array);
            bgColor = getBgColor(array);
            size    = getSize(array);
            style   = getStyle(array);
        }
        finally {
            array.recycle();
        }

        // initialize
        init();
    }

    ////////////////////////////////////////
    // PRIVATE METHODS
    ////////////////////////////////////////

    private void init(){
        fgPaint  = new Paint();
        fgPaint.setAntiAlias(true);
        fgPaint.setColor(getFgColor());
        fgPaint.setStyle(Paint.Style.FILL);
        fgPaint.setDither(true);

        bgPaint  = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(getBgColor());
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setDither(true);

        switch (getStyle()){
            case Style.SKYPE_BALLS: loader = new SkypeBalls(this, getSize()); break;

            default: loader = new SkypeBalls(this, getSize()); break;
        }

    }

    protected final void log(Object object){
        Log.d("CRS=>", String.valueOf(object));
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }

    ////////////////////////////////////////
    // PUBLIC METHODS
    ////////////////////////////////////////

    public final int getFgColor(){
        return this.fgColor;
    }

    public final int getBgColor(){
        return this.bgColor;
    }

    public final int getSize(){
        return this.size;
    }

    public final int getStyle(){
        return this.style;
    }

    ////////////////////////////////////////
    // PROTECTED METHODS
    ////////////////////////////////////////

    protected int getFgColor(final TypedArray typedArray){
        return typedArray.getColor(R.styleable.CrystalPreloader_crs_pl_fg_color, Color.BLACK);
    }

    protected int getBgColor(final TypedArray typedArray){
        return typedArray.getColor(R.styleable.CrystalPreloader_crs_pl_bg_color, Color.WHITE);
    }

    protected int getSize(final TypedArray typedArray){
        return typedArray.getInt(R.styleable.CrystalPreloader_crs_pl_size, Size.SMALL);
    }

    protected int getStyle(final TypedArray typedArray){
        return typedArray.getInt(R.styleable.CrystalPreloader_crs_pl_style, Style.SKYPE_BALLS);
    }

    ////////////////////////////////////////
    // OVERRIDE METHODS
    ////////////////////////////////////////


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(loader.getWidth(), loader.getHeight());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        loader.onDraw(canvas, fgPaint, bgPaint, loader.getWidth(), loader.getHeight(), loader.getWidth() / 2, loader.getHeight() / 2);
    }
}
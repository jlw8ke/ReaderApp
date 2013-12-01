package com.cauliflower.readerapp.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cauliflower.readerapp.R;

/**
 * Created by jlw8k_000 on 12/1/13.
 */
public class DrawingView extends View {

    public interface DrawingViewInterface {
        public void drawPath(Canvas canvas, Path path);
    }


    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;

    public int width;
    public int height;

    private Bitmap m_Bitmap;
    private Canvas m_Canvas;
    private Path m_Path;
    private Paint m_BitmapPaint;
    private DrawingViewInterface m_Interface;


    public DrawingView(Context context) {
        super(context);
        m_Path = new Path();
        m_BitmapPaint= new Paint(Paint.DITHER_FLAG);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_Path = new Path();
        m_BitmapPaint= new Paint(Paint.DITHER_FLAG);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        m_Path = new Path();
        m_BitmapPaint= new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            m_Interface = (DrawingViewInterface) ((Activity)getContext()).getFragmentManager().findFragmentById(R.id.container_main);
        } catch (ClassCastException e) {
            throw new ClassCastException(getContext().toString() + "must implement DrawingViewInterface");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        m_Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        m_Canvas = new Canvas(m_Bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xFFAAAAAA);
        canvas.drawBitmap(m_Bitmap, 0, 0, m_BitmapPaint);
        m_Interface.drawPath(canvas, m_Path);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        m_Path.reset();
        m_Path.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            m_Path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        m_Path.lineTo(mX, mY);
        // commit the path to our offscreen
        m_Interface.drawPath(m_Canvas, m_Path);
        // kill this so we don't double draw
        m_Path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}

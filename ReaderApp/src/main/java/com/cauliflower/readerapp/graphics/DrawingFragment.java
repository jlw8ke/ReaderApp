package com.cauliflower.readerapp.graphics;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cauliflower.readerapp.R;

/**
 * Created by jlw8k_000 on 10/28/13.
 */
public class DrawingFragment extends Fragment implements DrawingView.DrawingViewInterface{


    private FileFragmentInterface m_Interface;
    private Paint m_Paint;

    @Override
    public void drawPath(Canvas canvas, Path path) {
        canvas.drawPath(path, m_Paint);
    }

    public interface FileFragmentInterface{

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            m_Interface = (FileFragmentInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FileFragmentInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_Paint = new Paint();
        m_Paint.setAntiAlias(true);
        m_Paint.setDither(true);
        m_Paint.setColor(Color.BLUE);
        m_Paint.setStyle(Paint.Style.STROKE);
        m_Paint.setStrokeJoin(Paint.Join.ROUND);
        m_Paint.setStrokeCap(Paint.Cap.ROUND);
        m_Paint.setStrokeWidth(12);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file, container, false);

        return rootView;
    }

    public void colorChanged(int color) {
        m_Paint.setColor(color);
    }
}

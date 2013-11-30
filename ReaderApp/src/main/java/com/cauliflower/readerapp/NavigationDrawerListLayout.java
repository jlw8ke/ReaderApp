package com.cauliflower.readerapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

/**
 * Created by jlw8k_000 on 11/29/13.
 */
public class NavigationDrawerListLayout extends LinearLayout implements View.OnClickListener {

    private Adapter m_ListAdapter;
    private OnClickListener m_ClickListener;

    public NavigationDrawerListLayout(Context context) {
        super(context);
    }

    public NavigationDrawerListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationDrawerListLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onClick(View view) {
        if(m_ClickListener != null)
            m_ClickListener.onClick(view);
    }

    public void setList(Adapter list) {
        m_ListAdapter = list;

        if(m_ListAdapter != null) {
            for(int i = 0; i < m_ListAdapter.getCount(); i++) {
                View item = m_ListAdapter.getView(i, null, null);
                this.addView(item);
            }
        }
    }

    public void onClickListener (OnClickListener listener) {
        m_ClickListener = listener;
    }
}

package com.mariko.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by AStefaniuk on 07.08.2015.
 */
public class ScrollViewEx extends ScrollView {

    private Listener mListener;

    public ScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChanged(t);
        }
    }

    public void setListener(Listener mListener) {
        this.mListener = mListener;
    }

    public interface Listener {
        void onScrollChanged(int newScrollY);
    }
}

package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public class ViewAnimatedLayout extends RelativeLayout {

    private ViewAnimated mainView;
    private ViewAnimated detailsView;
    private int distance;

    public ViewAnimatedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void showInternal(ViewAnimated view, boolean show) {
        showInternal(view, show, true);
    }

    private void showInternal(ViewAnimated view, boolean show, boolean animated) {
        view.show(show ? 0 : -(view.getMeasuredWidth() - distance), animated);
    }

    public boolean isMainVisible() {
        return this.getVisibility() == View.VISIBLE && mainView.getVisibility() == View.VISIBLE && mainView.getTranslationX() == 0;
    }

    public boolean isDetailsVisible() {
        return this.getVisibility() == View.VISIBLE && detailsView.getVisibility() == View.VISIBLE && detailsView.getTranslationX() == 0;
    }

    public void init(int mainWidth, int detailsWidth, int distance) {
        this.distance = distance;

        mainView.getLayoutParams().width = mainWidth;
        detailsView.getLayoutParams().width = detailsWidth;
        detailsView.setPadding(mainWidth - mainView.getShadowWidth(), 0, 0, 0);

        requestLayout();
    }

    public void add(View main, View details) {

        mainView = new ViewAnimated(getContext(), null);
        detailsView = new ViewAnimated(getContext(), null);

        mainView.addMainView(main);
        detailsView.addMainView(details);

        addView(detailsView);
        addView(mainView);

        mainView.setListener(new ViewAnimated.Listener() {
            @Override
            public void show(boolean show) {

                ViewAnimatedLayout.this.showInternal(mainView, show);
                if (detailsView.isEnabled()) {
                    ViewAnimatedLayout.this.showInternal(detailsView, show);
                }
            }
        });

        detailsView.setListener(new ViewAnimated.Listener() {
            @Override
            public void show(boolean show) {

                ViewAnimatedLayout.this.showInternal(detailsView, show);

                if (!show) {
                    ViewAnimatedLayout.this.showInternal(mainView, false);
                }

            }
        });
    }

    public void show(boolean show, boolean details, boolean animated) {
        showInternal(details ? detailsView : mainView, show, animated);
    }

    public void show(boolean show) {
        showInternal(mainView, show);
        showInternal(detailsView, show);
    }

    public void setDetailsEnabled(boolean value) {
        detailsView.setEnabled(value);
    }
}

package com.mariko.lalamap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public class ViewAnimatedLayout extends RelativeLayout {

    private ViewAnimated mainView;
    private ViewAnimated detailsView;

    public ViewAnimatedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void showInternal(ViewAnimated view, boolean show) {
        view.show(show ? 0 : -(view.getMeasuredWidth() - 100));
    }

    public boolean isMainVisible() {
        return mainView.getTranslationX() == 0;
    }

    public boolean isDetailsVisible() {
        return detailsView.getTranslationX() == 0;
    }

    public void add(int width, View main, View details) {
        mainView = new ViewAnimated(getContext(), null);
        detailsView = new ViewAnimated(getContext(), null);

        mainView.addMainView(main);
        detailsView.addMainView(details);

        addView(detailsView);
        addView(mainView);

        mainView.getLayoutParams().width = width;
        detailsView.getLayoutParams().width = 3 * width;
        detailsView.setPadding(width - mainView.getShadowWidth(), 0, 0, 0);

        mainView.setListener(new ViewAnimated.Listener() {
            @Override
            public void show(boolean show) {

                ViewAnimatedLayout.this.showInternal(mainView, show);
                ViewAnimatedLayout.this.showInternal(detailsView, show);
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

    public void show(boolean show, boolean details) {
        showInternal(details ? detailsView : mainView, show);
    }

    public void show(boolean show) {
        showInternal(mainView, show);
        showInternal(detailsView, show);
    }
}

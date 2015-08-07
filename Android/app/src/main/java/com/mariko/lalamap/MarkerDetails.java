package com.mariko.lalamap;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.mariko.animation.ScrollViewEx;
import com.mariko.data.MapItem;
import com.mariko.data.Service;
import com.mariko.data.WikiData;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AStefaniuk on 27.04.2015.
 */
public class MarkerDetails extends RelativeLayout {

    private final TextView title;
    private final TextView body;
    private GridLayout list;

    public MarkerDetails(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.marker_details, this);

        ((ScrollViewEx) findViewById(R.id.scroll)).setListener(new ScrollViewEx.Listener() {
            @Override
            public void onScrollChanged(int newScrollY) {
                list.setTranslationY((int) (newScrollY * 0.7f));
            }
        });

        list = (GridLayout) findViewById(R.id.list);

        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);

        findViewById(R.id.youtube).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GApp.sInstance.getBus().post(new TextSpeaker.TextSpeakerEvent(body.getText().toString()));
                /*
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=" + URLEncoder.encode(item.key, "UTF-8")));
                    getContext().startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                */
            }
        });

        list.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if ((right - left) != (oldRight - oldLeft) || (top - bottom) != (oldTop - oldBottom)) {
                    loadImages();
                }
            }
        });
    }

    private MapItem item;

    private Rect getSpanItem(int[] items, int index) {

        Rect rect = null;

        for (int i = 0; i < items.length; i++) {

            if ((items[i] + "").contains(index + "")) {
                if (rect == null) {
                    rect = new Rect();
                    rect.left = ((items[i] + "").indexOf("" + index));
                    rect.top = (i);
                }

                rect.right = Math.max(rect.right, ((items[i] + "").lastIndexOf("" + index)));
                rect.bottom = Math.max(rect.bottom, (i));
            }

        }
        return rect;
    }

    public void load(MapItem item) {
        this.item = item;

        loadImages();

        title.setText("");
        body.setText("");

        new Service().getWiki(item.key).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WikiData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //TODO: showInternal error
            }

            @Override
            public void onNext(WikiData wikiData) {
                title.setText(wikiData.getTitle());
                SpannableStringBuilder ss = new SpannableStringBuilder(wikiData.getBody());

                int maxWidth = (int) (list.getMeasuredWidth() - GApp.sInstance.DPI * 100);

                String text = wikiData.getBody();

                TextPaint textPaint = body.getPaint();
                int index = 0;
                Rect rect = new Rect();
                int lines = 0;
                for (int i = 0; i < text.length() && lines < 3; i++) {
                    textPaint.getTextBounds(text, index, i, rect);
                    if (rect.width() >= maxWidth) {

                        for (int j = i; j >= 0; j--) {
                            if (!Character.isLetterOrDigit(text.charAt(j))) {
                                i = j;
                                break;
                            }
                        }

                        ss.insert(i + lines, "\n");
                        ++lines;
                        index = i;
                    }
                }

                body.setText(ss);
            }
        });

    }

    public void loadImages() {
        list.removeAllViews();

        if (this.item == null) {
            return;
        }

        int[] span = getSpanList(item.images.size());

        if (span.length > 0) {

            list.setColumnCount((span[0] + "").length());
            list.setRowCount(span.length);

            int cellHeight = (int) (list.getLayoutParams().height / span.length - GApp.sInstance.DPI * 0);
            int cellWidth = (int) (list.getMeasuredWidth() * 1.0f / ("" + span[0]).length() - GApp.sInstance.DPI * 0);

            int padding = (int) (GApp.sInstance.DPI * 2);

            for (int i = 0; i < item.images.size(); i++) {
                Rect rect = getSpanItem(span, i + 1);
                if (rect == null) {
                    continue;
                }

                View view = LayoutInflater.from(getContext()).inflate(R.layout.image_item, null);

                view.setPadding(padding, padding, padding, padding);

                final ImageView imageView = (ImageView) view.findViewById(R.id.image);

                list.addView(view);

                imageView.getLayoutParams().width = cellWidth * (rect.width() + 1) + 2 * rect.width() * padding;
                imageView.getLayoutParams().height = cellHeight * (rect.height() + 1) + 2 * rect.height() * padding;

                ((GridLayout.LayoutParams) view.getLayoutParams()).columnSpec = GridLayout.spec(rect.left, rect.width() + 1);
                ((GridLayout.LayoutParams) view.getLayoutParams()).rowSpec = GridLayout.spec(rect.top, rect.height() + 1);

                Glide.with(getContext())
                        .load(item.images.get(i).url)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {

                                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                                Matrix matrix = new Matrix();

                                int width = imageView.getLayoutParams().width;
                                int height = imageView.getLayoutParams().height;

                                int widthBitmap = drawable.getIntrinsicWidth();
                                int heightBitmap = drawable.getIntrinsicHeight();

                                while (widthBitmap < width || heightBitmap < height) {
                                    widthBitmap += 1;
                                    heightBitmap = (int) (drawable.getIntrinsicHeight() * widthBitmap * 1.0f / drawable.getIntrinsicWidth());
                                }

                                matrix.preScale(widthBitmap * 1.0f / drawable.getIntrinsicWidth(), heightBitmap * 1.0f / drawable.getIntrinsicHeight());

                                imageView.setImageMatrix(matrix);

                                super.onResourceReady(drawable, anim);
                            }
                        });
            }
        }
    }

    private int[] getSpanList(int count) {
        switch (count) {

            case 0:
                return new int[]{};

            case 1:
                return new int[]{1};

            case 2:
                return new int[]{1, 2};

            case 3:
                return new int[]{112, 113};

            case 4:
                return new int[]{1112, 1113, 1114};

            case 5:
                return new int[]{1124, 1135};

            case 6:
                return new int[]{234, 115, 116};

            case 7:
                return new int[]{11123, 11156, 11167};

            case 8:
                return new int[]{2345, 1116, 1117, 1118};

            case 9:
            default:
                return new int[]{2345, 6117, 8119};

        }

    }

    public void setListHeight(int height) {
        list.getLayoutParams().height = height;
        requestLayout();
    }

}

package com.mariko.lalamap;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    }

    private MapItem item;

    private Rect getSpanItem(int[] a, int index) {

        Rect rect = null;

        for (int i = 0; i < a.length; i++) {

            if ((a[i] + "").contains(index + "")) {
                if (rect == null) {
                    rect = new Rect();
                    rect.left = ((a[i] + "").indexOf("" + index));
                    rect.top = (i);
                }

                rect.right = Math.max(rect.right, ((a[i] + "").lastIndexOf("" + index)));
                rect.bottom = Math.max(rect.bottom, (i));
            }

        }
        return rect;
    }

    public void load(MapItem item) {
        this.item = item;

        list.removeAllViews();

        int[] span = getSpanList(item.images.size());

        list.setColumnCount((span[0] + "").length());
        list.setRowCount(span.length);

        for (int i = 0; i < item.images.size(); i++) {
            Rect rect = getSpanItem(span, i + 1);
            if (rect == null) {
                continue;
            }

            ImageView imageView = new ImageView(getContext(), null);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            list.addView(imageView);

            ((GridLayout.LayoutParams) imageView.getLayoutParams()).columnSpec = GridLayout.spec(rect.left, rect.width() + 1);
            ((GridLayout.LayoutParams) imageView.getLayoutParams()).rowSpec = GridLayout.spec(rect.top, rect.height() + 1);

            Glide.with(getContext()).load(item.images.get(i).url).override(100 * (rect.width() + 1), 100 * (rect.height() + 1)).into(imageView);
        }

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
                body.setText(wikiData.getBody());
            }
        });

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
}

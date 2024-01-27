package com.triplanner.triplanner;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DynamicHeightListView extends ListView {

    public DynamicHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ListAdapter adapter = getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            int itemCount = adapter.getCount();

            for (int i = 0; i < itemCount; i++) {
                View item = adapter.getView(i, null, this);
                item.measure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
                totalHeight += item.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = totalHeight + (getDividerHeight() * (itemCount - 1));
            setLayoutParams(params);
        }
    }
}

package com.zp.tech.deleted.messages.status.saver.utils;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {
    private AdapterDataObserver dataObserver;
    private View emptyView;

    public EmptyRecyclerView(Context context) {
        this(context, null);
        init();
    }

    public EmptyRecyclerView(Context context, AttributeSet attr) {
        this(context, attr, 0);
        init();
    }

    public EmptyRecyclerView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }

    private void init() {
        this.dataObserver = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                if (getAdapter() != null && emptyView != null) {
                    if (getAdapter().getItemCount() == 0) {
                        emptyView.setVisibility(VISIBLE);
                        setVisibility(GONE);
                    } else {
                        emptyView.setVisibility(GONE);
                        setVisibility(VISIBLE);
                    }
                }
            }
        };
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.dataObserver);
        }
    }


    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}

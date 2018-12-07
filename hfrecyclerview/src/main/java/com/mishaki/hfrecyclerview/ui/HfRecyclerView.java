package com.mishaki.hfrecyclerview.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HfRecyclerView extends RecyclerView {
    private final ArrayList<View> headerViewList = new ArrayList<>();
    private final ArrayList<View> footerVieList = new ArrayList<>();

    private HfAdapter mHfAdapter;

    public HfRecyclerView(Context context) {
        this(context, null, 0);
    }

    public HfRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HfRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(@NonNull final View headerView) {
        headerViewList.add(headerView);
        if (mHfAdapter != null) {
            mHfAdapter.addHeaderView(headerView);
        }
    }

    public void addFooterView(@NonNull final View footerView) {
        footerVieList.add(footerView);
        if (mHfAdapter != null) {
            mHfAdapter.addFooterView(footerView);
        }
    }

    public void removeHeaderView(@NonNull final View headerView) {
        if (headerViewList.contains(headerView)) {
            removeHeaderViewAsAdapter(headerView);
        }
    }

    public void removeHeaderView(final int position) {
        if (position < headerViewList.size()) {
            removeHeaderViewAsAdapter(headerViewList.get(position));
        }
    }

    private void removeHeaderViewAsAdapter(@NonNull final View headerView) {
        if (mHfAdapter != null) {
            mHfAdapter.removeHeaderView(headerView);
        }
    }

    public void removeFooterView(@NonNull final View footerView) {
        if (footerVieList.contains(footerView)) {
            removeFooterViewAsAdapter(footerView);
        }
    }

    public void removeFooterView(final int position) {
        if (position < footerVieList.size()) {
            removeFooterViewAsAdapter(footerVieList.get(position));
        }
    }

    private void removeFooterViewAsAdapter(@NonNull final View footerView) {
        if (mHfAdapter != null) {
            mHfAdapter.removeFooterView(footerView);
        }
    }

    public void removeAllHeaderView() {
        if (mHfAdapter != null) {
            mHfAdapter.removeAllHeaderView();
        }
    }

    public void removeAllFooterView() {
        if (mHfAdapter != null) {
            mHfAdapter.removeAllFooterView();
        }
    }

    public int getHeaderViewCount() {
        return headerViewList.size();
    }

    public int getFooterViewCount() {
        return footerVieList.size();
    }

    public View getHeaderView(final int index) {
        if (index > headerViewList.size() - 1) {
            return null;
        }
        return headerViewList.get(index);
    }

    public View getFooterView(final int index) {
        if (index > footerVieList.size() - 1) {
            return null;
        }
        return footerVieList.get(index);
    }


    @Override
    public void setAdapter(Adapter adapter) {

    }

    @Override
    public Adapter getAdapter() {
        if (mHfAdapter != null) {
            return mHfAdapter;
        }
        return super.getAdapter();
    }

    public final void setHfAdapter(final HfAdapter adapter) {
        super.setAdapter(adapter);
        mHfAdapter = adapter;
    }

    public static abstract class HfAdapter<VH extends HfViewHolder> extends Adapter<ViewHolder> {
        private final int TYPE_HEADER = Integer.MAX_VALUE;
        private final int TYPE_FOOTER = Integer.MIN_VALUE;

        private final ArrayList<View> headerViewList = new ArrayList<>();
        private final ArrayList<View> footerViewList = new ArrayList<>();

        @Override
        public final void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public final ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            final int type = getActualViewType(viewType);
            if (type != TYPE_HEADER && type != TYPE_FOOTER) {
                return onCreateHfViewHolder(parent, viewType);
            }
            if (type == TYPE_HEADER) {
                return new ViewHolder(headerViewList.get(viewType)) {
                };
            }
            return new ViewHolder(footerViewList.get(viewType - getHfItemCount() - headerViewList.size())) {
            };
        }

        public abstract @NonNull
        VH onCreateHfViewHolder(@NonNull final ViewGroup parent, final int viewType);

        @Override
        public final void onBindViewHolder(final ViewHolder holder, final int position) {
            final int type = getActualViewType(position);
            if (type != TYPE_HEADER && type != TYPE_FOOTER) {
                onBindHfViewHolder((VH) (holder), position - headerViewList.size());
            }
        }

        public abstract void onBindHfViewHolder(@NonNull final VH holder, final int position);

        private void addHeaderView(final View headerView) {
            headerViewList.add(headerView);
            notifyDataSetChanged();
        }

        private void removeHeaderView(final View headerView) {
            headerViewList.remove(headerView);
            notifyDataSetChanged();
        }

        private void addFooterView(final View footerView) {
            footerViewList.add(footerView);
            notifyDataSetChanged();
        }

        private void removeFooterView(final View footerView) {
            footerViewList.remove(footerView);
            notifyDataSetChanged();
        }

        private void removeAllHeaderView() {
            headerViewList.clear();
            notifyDataSetChanged();
        }

        private void removeAllFooterView() {
            footerViewList.clear();
            notifyDataSetChanged();
        }

        @Override
        public final int getItemCount() {
            return headerViewList.size() + getHfItemCount() + footerViewList.size();
        }

        public abstract int getHfItemCount();

        @Override
        public final int getItemViewType(final int position) {
            return position;
        }

        private int getActualViewType(final int position) {
            if (position < headerViewList.size()) {
                return TYPE_HEADER;
            }
            final int hfItemCount = getHfItemCount();
            if (position > headerViewList.size() + hfItemCount - 1) {
                return TYPE_FOOTER;
            }
            return getHfItemType(position - headerViewList.size());
        }

        @Override
        public final long getItemId(final int position) {
            return getHfItemId(position - headerViewList.size());
        }

        @Override
        public final void onViewRecycled(final ViewHolder holder) {
            try {
                onHfViewRecycled((VH) holder);
            } catch (Exception e) {
            }
        }

        @Override
        public final boolean onFailedToRecycleView(final ViewHolder holder) {
            try {
                return onHfFailedToRecycleView((VH) holder);
            } catch (Exception e) {
                return super.onFailedToRecycleView(holder);
            }
        }

        @Override
        public final void onViewAttachedToWindow(final ViewHolder holder) {
            try {
                onHfViewAttachedToWindow((VH) holder);
            } catch (Exception e) {
            }
        }

        @Override
        public final void onViewDetachedFromWindow(final ViewHolder holder) {
            try {
                onHfViewDetachedFromWindow((VH) holder);
            } catch (Exception e) {
            }
        }

        public int getHfItemType(final int position) {
            return 0;
        }

        public long getHfItemId(final int position) {
            return super.getItemId(position);
        }


        public void onHfViewRecycled(final VH holder) {

        }

        public boolean onHfFailedToRecycleView(final VH holder) {
            return false;
        }

        public void onHfViewAttachedToWindow(final VH holder) {

        }

        public void onHfViewDetachedFromWindow(final VH holder) {

        }
    }

    //为了防止上面一堆没有position的方法拿到的ViewHolder就是RecyclerView.ViewHolder,所以规定必须用这个ViewHolder
    public static class HfViewHolder extends ViewHolder {
        public HfViewHolder(View itemView) {
            super(itemView);
        }
    }
}
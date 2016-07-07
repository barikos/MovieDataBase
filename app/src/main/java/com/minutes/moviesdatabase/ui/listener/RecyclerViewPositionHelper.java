package com.minutes.moviesdatabase.ui.listener;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by barikos on 23.06.16.
 */
public class RecyclerViewPositionHelper {

    final RecyclerView mRecyclerView;
    final RecyclerView.LayoutManager mLayoutManager;

    public RecyclerViewPositionHelper(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = recyclerView.getLayoutManager();
    }

    public static RecyclerViewPositionHelper getInstance(RecyclerView recyclerView){
        if (recyclerView == null){
            throw new NullPointerException("Recycler view is null");
        }
        return new RecyclerViewPositionHelper(recyclerView);
    }

    public int getItemCount(){
        return mLayoutManager == null ? 0 : mLayoutManager.getItemCount();
    }

    public int findFirstVisibleItemPosition(){
        final View child = findOneVisibleChild(0,mLayoutManager.getChildCount(),false, true);
        return child == null ? RecyclerView.NO_POSITION : mRecyclerView.getChildAdapterPosition(child);
    }

    public int findFirstCompletelyVisibleItemPosition(){
        final View child = findOneVisibleChild(0,mLayoutManager.getChildCount(),true, false);
        return child == null ? RecyclerView.NO_POSITION : mRecyclerView.getChildAdapterPosition(child);
    }

    public int findLastVisibleItemPosition(){
        final View child = findOneVisibleChild(mLayoutManager.getChildCount() -1, -1,false, true);
        return child == null ? RecyclerView.NO_POSITION : mRecyclerView.getChildAdapterPosition(child);
    }
    public int findLastCompletelyVisibleItemPosition(){
        final View child = findOneVisibleChild(mLayoutManager.getChildCount() -1, -1,true, false);
        return child == null ? RecyclerView.NO_POSITION : mRecyclerView.getChildAdapterPosition(child);
    }

    private View findOneVisibleChild(int fromIndex, int toIndex,
                                     boolean completelyVisible,
                                     boolean acceptPartiallyVisible) {
        OrientationHelper helper;
        if (mLayoutManager.canScrollVertically()){
            helper = OrientationHelper.createVerticalHelper(mLayoutManager);
        }else {
            helper = OrientationHelper.createHorizontalHelper(mLayoutManager);
        }

        final int start = helper.getStartAfterPadding();
        final int end = helper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i!= toIndex; i+=next){
            final View child = mLayoutManager.getChildAt(i);
            final int childStart = helper.getDecoratedStart(child);
            final int childEnd = helper.getDecoratedEnd(child);
            if (childStart < end && childEnd > start){
                if (completelyVisible){
                    if (childStart >= start && childEnd <= end){
                        return child;
                    }else if (acceptPartiallyVisible && partiallyVisible == null){
                        partiallyVisible = child;
                    }
                }else {
                    return child;
                }
            }
        }

        return partiallyVisible;
    }
}

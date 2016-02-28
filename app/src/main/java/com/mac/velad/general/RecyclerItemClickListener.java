package com.mac.velad.general;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ruenzuo on 28/02/16.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public interface OnItemClickListener {
        void onItemClick(View childView, int position);
        void onItemLongPress(View childView, int position);
    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp (MotionEvent motionEvent){
            if (childView != null) {
                listener.onItemClick(childView, position);
            }
            return true;
        }

        @Override
        public void onLongPress (MotionEvent motionEvent) {
            if (childView != null) {
                listener.onItemLongPress(childView, position);
            }
            super.onLongPress(motionEvent);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }
    }

    private OnItemClickListener listener;
    private GestureDetector gestureDetector;
    private View childView;
    private int position;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        this.listener = listener;
        this.gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        childView = view.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        position = view.getChildAdapterPosition(childView);

        return childView != null && gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept) { }

}

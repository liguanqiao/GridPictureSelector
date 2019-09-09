package cc.xbridge.gridpictureselector_library.base.callback;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.animation.AccelerateDecelerateInterpolator;


import cc.xbridge.gridpictureselector_library.R;
import cc.xbridge.gridpictureselector_library.base.adapter.BaseItemAdapter;


public class BaseItemTouchHelpCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "BaseItemTouchHelpCallback";

    private BaseItemAdapter mAdapter;

    private float mMoveThreshold = 0.1f;
    private float mSwipeThreshold = 0.7f;

    public BaseItemTouchHelpCallback(BaseItemAdapter adapter){
        this.mAdapter = adapter;
    }

    /**
     * 当Item被长按的时候是否可以被拖拽
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return mAdapter.isCanDrag();
    }


    /**
     * Item是否可以被滑动(H：左右滑动，V：上下滑动)
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return mAdapter.isCanSwipe();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            mAdapter.onItemDragStart(viewHolder);
            viewHolder.itemView.animate().scaleY(1.1f).scaleX(1.1f).setDuration(100).setInterpolator(new AccelerateDecelerateInterpolator());
            viewHolder.itemView.setTag(R.id.BaseItemAdapter_dragging_support,true);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //暂无滑动
            viewHolder.itemView.setTag(R.id.BaseItemAdapter_swiping_support,true);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.itemView.getTag(R.id.BaseItemAdapter_dragging_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseItemAdapter_dragging_support)) {
            mAdapter.onItemDragEnd(viewHolder);
            viewHolder.itemView.animate().scaleY(1f).scaleX(1f).setDuration(100).setInterpolator(new AccelerateDecelerateInterpolator());
            viewHolder.itemView.setTag(R.id.BaseItemAdapter_dragging_support, false);
        }
        if (viewHolder.itemView.getTag(R.id.BaseItemAdapter_swiping_support) != null
                && (Boolean) viewHolder.itemView.getTag(R.id.BaseItemAdapter_swiping_support)) {
            viewHolder.itemView.setTag(R.id.BaseItemAdapter_swiping_support, false);
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // flag如果值是0，相当于这个功能被关闭
        if(viewHolder != null && mAdapter.getItemViewType(viewHolder.getAdapterPosition()) == BaseItemAdapter.TYPE_ADD){
            //添加Item 禁止拖动
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.ACTION_STATE_IDLE);
        }
        int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.ACTION_STATE_IDLE;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    /**
     * 当Item被拖拽的时候被回调
     *
     * @param recyclerView     recyclerView
     * @param srcViewHolder    拖拽的ViewHolder
     * @param targetViewHolder 目的地的viewHolder
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcViewHolder, RecyclerView.ViewHolder targetViewHolder ) {
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder source, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, source, fromPos, target, toPos, x, y);
        mAdapter.onItemDragMoving(source,target);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //TODO 滑动
    }

    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mMoveThreshold;
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return mSwipeThreshold;
    }

    /**
     * Set the fraction that the user should move the View to be considered as swiped.
     * The fraction is calculated with respect to RecyclerView's bounds.
     * <p>
     * Default value is .5f, which means, to swipe a View, user must move the View at least
     * half of RecyclerView's width or height, depending on the swipe direction.
     *
     * @param swipeThreshold A float value that denotes the fraction of the View size. Default value
     *                       is .8f .
     */
    public void setSwipeThreshold(float swipeThreshold) {
        mSwipeThreshold = swipeThreshold;
    }


    /**
     * Set the fraction that the user should move the View to be considered as it is
     * dragged. After a view is moved this amount, ItemTouchHelper starts checking for Views
     * below it for a possible drop.
     *
     * @param moveThreshold A float value that denotes the fraction of the View size. Default value is
     *                      .1f .
     */
    public void setMoveThreshold(float moveThreshold) {
        mMoveThreshold = moveThreshold;
    }
}

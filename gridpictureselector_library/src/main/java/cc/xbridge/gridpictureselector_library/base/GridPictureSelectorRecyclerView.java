package cc.xbridge.gridpictureselector_library.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.luck.picture.lib.entity.LocalMedia;

import java.math.BigDecimal;
import java.util.List;

import cc.xbridge.gridpictureselector_library.R;
import cc.xbridge.gridpictureselector_library.base.adapter.BaseItemAdapter;
import cc.xbridge.gridpictureselector_library.base.adapter.GridDivider;
import cc.xbridge.gridpictureselector_library.base.callback.BaseItemTouchHelpCallback;
import cc.xbridge.gridpictureselector_library.base.listener.OnAddPicClickListener;
import cc.xbridge.gridpictureselector_library.base.listener.OnDeletePicClickListener;
import cc.xbridge.gridpictureselector_library.base.listener.OnItemClickListener;
import cc.xbridge.gridpictureselector_library.base.listener.OnItemDragListener;
import cc.xbridge.gridpictureselector_library.base.util.BaseAdapterUtil;
import cc.xbridge.gridpictureselector_library.base.util.PhotoPickerUtil;

public class GridPictureSelectorRecyclerView extends RecyclerView {
    private BaseItemAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;

    private int mSpanCount;
    private int mItemWidth;
    private int mItemWhiteSpacing;
    private int mMaxItemCount;
    private int mAddImage;
    private int mDeleteImage;

    public GridPictureSelectorRecyclerView(Context context) {
        this(context,null);
    }

    public GridPictureSelectorRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GridPictureSelectorRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initDefaultAttrs();
        initCustomAttrs(context,attrs);
        init(context);
        System.out.println("mMaxItemCount : " + mMaxItemCount);
    }

    /**
     * 设置默认值
     */
    private void initDefaultAttrs() {
        mSpanCount = 3;
        mItemWidth = 0;
        mItemWhiteSpacing = BaseAdapterUtil.dp2px(4);
        mMaxItemCount = 9;
        mAddImage = R.drawable.gps_add_icon;
        mDeleteImage = R.drawable.gps_delete_icon;
    }

    /**
     * 初始化自定义属性
     * @param context
     * @param attrs
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridPictureSelectorRecyclerView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    /**
     * 初始化自定义属性
     * @param attrIndex
     * @param typedArray
     */
    private void initCustomAttr(int attrIndex, TypedArray typedArray) {
        if(attrIndex == R.styleable.GridPictureSelectorRecyclerView_span_count){
            mSpanCount = typedArray.getInteger(attrIndex,mSpanCount);
        } else if (attrIndex == R.styleable.GridPictureSelectorRecyclerView_item_width) {
            mItemWidth =typedArray.getDimensionPixelSize(attrIndex, mItemWidth);
        }else if(attrIndex == R.styleable.GridPictureSelectorRecyclerView_item_white_spacing){
            mItemWhiteSpacing = typedArray.getDimensionPixelSize(attrIndex,mItemWhiteSpacing);
        }else if(attrIndex == R.styleable.GridPictureSelectorRecyclerView_max_item_count){
            mMaxItemCount = typedArray.getInteger(attrIndex,mMaxItemCount);
        }else if(attrIndex == R.styleable.GridPictureSelectorRecyclerView_add_image){
            mAddImage = typedArray.getResourceId(attrIndex,mAddImage);
        }else if(attrIndex == R.styleable.GridPictureSelectorRecyclerView_delete_image){
            mDeleteImage = typedArray.getResourceId(attrIndex,mDeleteImage);
        }
    }

    /**
     * 初始化控件
     */
    private void init(Context context){


        setOverScrollMode(OVER_SCROLL_NEVER);
        mGridLayoutManager = new GridLayoutManager(context, mSpanCount);
        setLayoutManager(mGridLayoutManager);
        addItemDecoration(GridDivider.newInstanceWithSpacePx(mItemWhiteSpacing / 2));    //添加边距

        mAdapter = new BaseItemAdapter();
        mAdapter.setAddImage(mAddImage);
        mAdapter.setDeleteImage(mDeleteImage);
        mAdapter.setItemCount(mMaxItemCount);
        setAdapter(mAdapter);
    }

    /**
     *  动态设置宽高
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        //列数
        int spanCount = mSpanCount;
        //当前item数量
        int itemCount = mAdapter.getItemCount();
        //行数
        int rowCount = BigDecimal.valueOf(itemCount).divide(BigDecimal.valueOf(spanCount),BigDecimal.ROUND_CEILING).intValue();
        //当前设置的宽度
        int width = resolveSize(0, widthSpec);
        //item最小宽度
        int minItemWidth = BaseAdapterUtil.dp2px(60);    //item宽度最小值
        //item最大宽度
        int maxItemWidth = width / mSpanCount;
        //item最后宽度
        int itemWidth;
        if (mItemWidth != 0) {
            if(mItemWidth > maxItemWidth){
                //设置最大值
                itemWidth = maxItemWidth;
            }else if(mItemWidth < minItemWidth) {
                //设置小于最小值
                itemWidth = minItemWidth + mItemWhiteSpacing;
            }else {
                //默认
                itemWidth =mItemWidth + mItemWhiteSpacing;
            }
        }else {
            if(maxItemWidth < minItemWidth){
                itemWidth = minItemWidth + mItemWhiteSpacing;
            }else {
                itemWidth = maxItemWidth;
            }
        }

        width = itemWidth * spanCount;
        int height = itemWidth * rowCount;

        setMeasuredDimension(width, height);
    }

    public void enableDragItem(boolean longPress){
        BaseItemTouchHelpCallback helpCallback = new BaseItemTouchHelpCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(helpCallback);
        helper.attachToRecyclerView(this);

        mAdapter.enableDragItem(longPress);
    }

    public void setData(List<LocalMedia> data){
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    public void addDataAll(List<LocalMedia> data){
        mAdapter.getData().addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    public List<LocalMedia> getData(){
        return mAdapter.getData();
    }

    public void setSelectMax(int selectMax){
        mAdapter.setItemCount(selectMax);
    }

    public void setOnAddPicClickListener(OnAddPicClickListener listener){
        mAdapter.setOnAddPicClickListener(listener);
    }

    public void setOnDeletePicClickListener(OnDeletePicClickListener listener){
        mAdapter.setOnDeletePicClickListener(listener);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mAdapter.setOnItemClickListener(listener);
    }

    public void setOnItemDragListener(OnItemDragListener listener){
        mAdapter.setOnItemDragListener(listener);
    }
}

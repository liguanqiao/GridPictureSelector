package cc.xbridge.gridpictureselector_library.base.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cc.xbridge.gridpictureselector_library.R;
import cc.xbridge.gridpictureselector_library.base.BaseViewHolder;
import cc.xbridge.gridpictureselector_library.base.listener.OnAddPicClickListener;
import cc.xbridge.gridpictureselector_library.base.listener.OnDeletePicClickListener;
import cc.xbridge.gridpictureselector_library.base.listener.OnItemClickListener;
import cc.xbridge.gridpictureselector_library.base.listener.OnItemDragListener;
import cc.xbridge.gridpictureselector_library.base.util.LocalMediaUtil;
import io.reactivex.functions.Consumer;

public class BaseItemAdapter <K extends BaseViewHolder> extends RecyclerView.Adapter<K> {

    private static final String TAG = "BaseItemAdapter";
    public static final int TYPE_ADD = 1;
    public static final int TYPE_PICTURE = 2;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemDragListener mOnItemDragListener;

    private View.OnTouchListener mOnToggleViewTouchListener;
    private View.OnLongClickListener mOnToggleViewLongClickListener;


    private int mLayoutResId = R.layout.item_filter_image;
    private List<LocalMedia> mData = new ArrayList<>();
    private int mItemCount = 9;
    private int mAddImage = R.drawable.gps_add_icon;
    private int mDeleteImage = R.drawable.gps_delete_icon;
    private int mGalleryType = PictureMimeType.ofImage();

    private boolean mDragOnLongPress = true;
    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag = false;
    /**
     * 是否可以被滑动
     */
    private boolean isCanSwipe = false;
    /**
     * 点击添加图片事件
     */
    private OnAddPicClickListener mOnAddPicClickListener;
    /**
     * item点击事件
     */
    private OnItemClickListener mItemClickListener;
    /**
     *  点击删除事件
     */
    private OnDeletePicClickListener mOnDeletePicClickListener;

    public boolean isCanDrag() {
        return isCanDrag;
    }

    public boolean isCanSwipe() {
        return isCanSwipe;
    }

    /**
     * 设置是否可以拖拽
     * @param longPress
     */
    public void enableDragItem(boolean longPress) {
        this.isCanDrag = true;
        this.setDragOnLongPress(longPress);
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    public void setData(List<LocalMedia> mData) {
        this.mData = mData;
    }

    public List<LocalMedia> getData() {
        return mData;
    }

    public void setAddImage(@DrawableRes int addImage) {
        this.mAddImage = addImage;
    }

    public void setDeleteImage(@DrawableRes int deleteImage) {
        this.mDeleteImage = deleteImage;
    }

    /**
     * 设置删除点击事件
     * @param mOnDeletePicClickListener
     */
    public void setOnDeletePicClickListener(OnDeletePicClickListener mOnDeletePicClickListener) {
        this.mOnDeletePicClickListener = mOnDeletePicClickListener;
    }

    /**
     * 设置添加item事件
     */
    public void setOnAddPicClickListener(OnAddPicClickListener mOnAddPicClickListener) {
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }

    /**
     * 设置item点击事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    /**
     * 设置item拖拽事件监听
     */
    public void setOnItemDragListener(OnItemDragListener onItemDragListener){
        this.mOnItemDragListener = onItemDragListener;
    }

    @NonNull
    @Override
    public K onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);

        K baseViewHolder =null;

        baseViewHolder = onCreateDefViewHolder(parent, viewType);

        initItemClickListener();
        initAddPicClickListener();

        return baseViewHolder;
    }


    @Override
    public int getItemCount() {
        if (mData.size() < mItemCount && isCanDrag) {
            return mData.size() + 1;
        } else {
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_ADD;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     *  是否显示添加item
     */
    private boolean isShowAddItem(int position) {
        int size = mData.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(@NonNull final K viewHolder, final int position) {
        //少于8张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_ADD && isCanDrag) {
            viewHolder.getIvImg().setImageResource(mAddImage);
            viewHolder.getIvImg().setOnLongClickListener(mOnToggleViewLongClickListener);
            viewHolder.getIvImg().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAddPicClickListener.onAddPicClick();
                }
            });
            viewHolder.getLlDel().setVisibility(View.INVISIBLE);
        } else {
            if(isCanDrag) {
                viewHolder.getIvDel().setImageResource(mDeleteImage);
                viewHolder.getLlDel().setVisibility(View.VISIBLE);
                viewHolder.getLlDel().setOnLongClickListener(mOnToggleViewLongClickListener);
                viewHolder.getLlDel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = viewHolder.getAdapterPosition();
                        // 这里有时会返回-1造成数据下标越界,具体可参考getAdapterPosition()源码，
                        // 通过源码分析应该是bindViewHolder()暂未绘制完成导致，知道原因的也可联系我~感谢
                        if (index != RecyclerView.NO_POSITION) {
                            mData.remove(index);
                            notifyItemRemoved(index);
                            notifyItemRangeChanged(index, mData.size());
                            if(mOnDeletePicClickListener != null){
                                mOnDeletePicClickListener.onDeletePicClick(index);
                            }
                        }
                    }
                });
            }
            LocalMedia media = mData.get(position);
//            int mimeType = media.getMimeType();
            String path = LocalMediaUtil.extractPath(media);   //获取最终地址
            int pictureType = PictureMimeType.isPictureType(media.getPictureType());
            long duration = media.getDuration();

            //设置样式
            viewHolder.getTvDuration().setVisibility(pictureType == PictureConfig.TYPE_VIDEO
                    ? View.VISIBLE : View.GONE);
            if (pictureType == PictureMimeType.ofAudio()) {
                viewHolder.getTvDuration().setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.picture_audio);
                StringUtils.modifyTextViewDrawable(viewHolder.getTvDuration(), drawable, 0);
                viewHolder.getIvImg().setImageResource(R.drawable.audio_placeholder);
            } else {
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.video_icon);
                StringUtils.modifyTextViewDrawable(viewHolder.getTvDuration(), drawable, 0);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.color_db)
                        .error(com.luck.picture.lib.R.drawable.image_load_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(viewHolder.itemView.getContext())
                        .load(path)
                        .apply(options)
                        .into(viewHolder.getIvImg());
            }
            viewHolder.getTvDuration().setText(DateUtils.timeParse(duration));

            //itemView 的点击事件
            viewHolder.getIvImg().setOnLongClickListener(mOnToggleViewLongClickListener);
            if (mItemClickListener != null) {
                viewHolder.getIvImg().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
                    }
                });
            }
        }
    }

    /**
     * 如果使用者没有自定义
     * 则初始化item点击事件
     */
    private void initItemClickListener(){
        if (mItemClickListener == null) {
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mData.size() > 0) {
                        LocalMedia media = mData.get(position);
                        String pictureType = media.getPictureType();
                        int mediaType = PictureMimeType.pictureToVideo(pictureType);
                        switch (mediaType) {
                            case PictureConfig.TYPE_IMAGE:
                                // 预览图片 可自定长按保存路径
                                PictureSelector.create((Activity) mContext).themeStyle(R.style.picture_default_style).openExternalPreview(position, mData);
                                break;
                            case PictureConfig.TYPE_VIDEO:
                                // 预览视频
                                PictureSelector.create((Activity) mContext).externalPictureVideo(media.getPath());
                                break;
                            case PictureConfig.TYPE_AUDIO:
                                // 预览音频
                                PictureSelector.create((Activity) mContext).externalPictureAudio(media.getPath());
                                break;
                        }
                    }
                }
            });
        }
    }

    /**
     * 如果使用者没有自定义
     * 则初始化添加事件监听
     */
    private void initAddPicClickListener(){
        if (mOnAddPicClickListener == null) {
            setOnAddPicClickListener(new OnAddPicClickListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onAddPicClick() {
                    //获取写的权限
                    RxPermissions rxPermission = new RxPermissions((Activity) mContext);
                    rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Permission>() {
                                @Override
                                public void accept(Permission permission) {
                                    if (permission.granted) {// 用户已经同意该权限
                                        PictureSelector.create((Activity) mContext)
                                                .openGallery(mGalleryType)
                                                .maxSelectNum(mItemCount - mData.size())
                                                .minSelectNum(1)
                                                .imageSpanCount(4)
                                                .selectionMode(PictureConfig.MULTIPLE)
                                                .forResult(PictureConfig.CHOOSE_REQUEST);
                                    } else {
                                        Toast.makeText(mContext, "拒绝", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }
    }

    /**
     * 设置是否长按拖动
     * @param longPress
     */
    public void setDragOnLongPress(boolean longPress) {
        mDragOnLongPress = longPress;
        if (mDragOnLongPress) {
            mOnToggleViewTouchListener = null;
            mOnToggleViewLongClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            };
        } else {
            mOnToggleViewTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == MotionEvent.ACTION_DOWN && !mDragOnLongPress;
                }
            };
            mOnToggleViewLongClickListener = null;
        }
    }

    /**
     * item拖拽开始
     */
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && isCanDrag) {
            mOnItemDragListener.onItemDragStart(viewHolder, viewHolder.getAdapterPosition());
        }
    }

    /**
     * item移动更换时
     */
    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = source.getAdapterPosition();
        int to = target.getAdapterPosition();

        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(mData, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(mData, i, i - 1);
                }
            }
            notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        }

        if (mOnItemDragListener != null && isCanDrag) {
            //移动事件
            mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
    }

    /**
     * item拖拽结束
     * @param viewHolder
     */
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null && isCanDrag) {
            mOnItemDragListener.onItemDragEnd(viewHolder, viewHolder.getAdapterPosition());
        }
    }

    /**
     * 是否在移动范围内
     * @param position
     * @return
     */
    private boolean inRange(int position) {
        return position >= 0 && position < mData.size();
    }


    @SuppressWarnings("unchecked")
    protected K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new BaseViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new BaseViewHolder(view);
    }


    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mLayoutResId;
        return createBaseViewHolder(parent, layoutId);
    }

    protected K createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(getItemView(layoutResId, parent));
    }

    protected View getItemView(@LayoutRes int layoutResId, ViewGroup parent) {
        return mLayoutInflater.inflate(layoutResId, parent, false);
    }
    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

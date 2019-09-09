package cc.xbridge.gridpictureselector_library.base;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.xbridge.gridpictureselector_library.R;
import cc.xbridge.gridpictureselector_library.base.widget.SquareImageView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SquareImageView mIvImg;
    private LinearLayout mLlDel;
    private TextView mTvDuration;
    private ImageView mIvDel;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        mIvImg = itemView.findViewById(R.id.fiv);
        mLlDel = itemView.findViewById(R.id.ll_del);
        mTvDuration = itemView.findViewById(R.id.tv_duration);
        mIvDel = itemView.findViewById(R.id.iv_del);
    }

    public ImageView getIvImg() {
        return mIvImg;
    }

    public LinearLayout getLlDel() {
        return mLlDel;
    }

    public TextView getTvDuration() {
        return mTvDuration;
    }

    public ImageView getIvDel() {
        return mIvDel;
    }
}

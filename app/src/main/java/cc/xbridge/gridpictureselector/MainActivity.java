package cc.xbridge.gridpictureselector;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import cc.xbridge.gridpictureselector_library.base.GridPictureSelectorRecyclerView;
import cc.xbridge.gridpictureselector_library.base.util.LocalMediaUtil;

public class MainActivity extends AppCompatActivity {

    private GridPictureSelectorRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_main);
        initWidget();
    }

    private void initWidget() {
//        String path1 = "http://img.mp.itc.cn/upload/20160808/83856467c1954dd7b38f9afac56e5cce_th.jpg";
//        String path2 = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1567676470&di=afb2bf5e3b617dbc87ca15a25152e84b&src=http://pic1.cxtuku.com/00/15/61/b55283cc310f.jpg";
//        String path3 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        String path4 = "/storage/emulated/0/Download/49f2bd68c3f7220e63acdee488c7c75a.mp4";
//        String path5 = "/storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20190830_090424.JPEG";
//
//        mRecyclerView.addDataAll(LocalMediaUtil.create(path1,path2,path3,path4,path5));

        mRecyclerView.enableDragItem(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> localMedia;
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                localMedia = PictureSelector.obtainMultipleResult(data);

                mRecyclerView.addDataAll(localMedia);
            }
        }
    }
}

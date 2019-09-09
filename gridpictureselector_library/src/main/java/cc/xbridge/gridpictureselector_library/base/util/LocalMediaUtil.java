package cc.xbridge.gridpictureselector_library.base.util;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalMediaUtil {
//    private static String SELECTION = MediaStore.Video.Media.DATA+" like ?";
//    /**
//     * 媒体文件数据库字段
//     */
//    private static final String[] PROJECTION = {
//            MediaStore.MediaColumns.MIME_TYPE,
//            MediaStore.MediaColumns.WIDTH,
//            MediaStore.MediaColumns.HEIGHT,
//            MediaStore.MediaColumns.DURATION};

    /**
     * 创建
     * @param path
     * @return
     */
    public static LocalMedia create(String path){
        LocalMedia result = new LocalMedia();
        result.setPath(path);

        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();

            if (PictureMimeType.isHttp(path)) {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                mmr.setDataSource(path, headers);
            }else {
                mmr.setDataSource(path);
            }

            result.setPictureType(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE));   //类型
            result.setMimeType(PictureMimeType.isPictureType(result.getPictureType())); //类型
            if(result.getMimeType() != PictureMimeType.ofImage()) {
                result.setDuration(Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));  //时长
            }
            mmr.release();
        } catch (Exception e) {
            e.printStackTrace();
            if(GPSUtil.isVideoUrl(path)) {
                result.setMimeType(PictureMimeType.ofVideo());
            }else if(GPSUtil.isAudioUrl(path)) {
                result.setMimeType(PictureMimeType.ofAudio());
            }else{
                result.setMimeType(PictureMimeType.ofImage());
            }
        }
        return result;
    }

    /**
     * 创建List
     * @param paths
     * @return
     */
    public static List<LocalMedia> create(String... paths){
        List<LocalMedia> result = new ArrayList<>();
        for (String str : paths) {
            result.add(create(str));
        }
        return result;
    }

    /**
     * 创建List
     * @param paths
     * @return
     */
    public static List<LocalMedia> create(List<String> paths){
        List<LocalMedia> result = new ArrayList<>();
        for (String str : paths) {
            result.add(create(str));
        }
        return result;
    }

    public static String extractPath(LocalMedia media){
        if (media.isCut() && !media.isCompressed()) {
            // 裁剪过
            return media.getCutPath();
        } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            return media.getCompressPath();
        } else {
            return media.getPath();
        }
    }
}

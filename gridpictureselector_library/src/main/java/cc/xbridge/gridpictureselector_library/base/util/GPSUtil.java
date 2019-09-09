package cc.xbridge.gridpictureselector_library.base.util;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class GPSUtil {

    private final static Pattern IMG_URL = Pattern
            .compile(".*?(png|jpeg|gif|jpg|webp|bmp)",Pattern.CASE_INSENSITIVE);

    private final static Pattern VIDEO_URL = Pattern
            .compile(".*?(mp4|avi|3gpp|3gp|mov)",Pattern.CASE_INSENSITIVE);

    private final static Pattern AUDIO_URL = Pattern
            .compile(".*?(mp3|m4a)",Pattern.CASE_INSENSITIVE);

    /**
     * 判断是否图片url
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * 判断是否视频url
     * @param url
     * @return
     */
    public static boolean isVideoUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return VIDEO_URL.matcher(url).matches();
    }

    /**
     * 判断是否音频url
     * @param url
     * @return
     */
    public static boolean isAudioUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return AUDIO_URL.matcher(url).matches();
    }

    /**
     * 获取网络音频时长
     * @param mUri
     * @return
     */
    private static String getRingDuring(String mUri){
        String duration=null;
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

        try {
            if (mUri != null) {
                HashMap<String, String> headers=null;
                if (headers == null) {
                    headers = new HashMap<String, String>();
                    headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
                }
                mmr.setDataSource(mUri, headers);
            }

            duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
        } finally {
            mmr.release();
        }
        return duration;
    }
}

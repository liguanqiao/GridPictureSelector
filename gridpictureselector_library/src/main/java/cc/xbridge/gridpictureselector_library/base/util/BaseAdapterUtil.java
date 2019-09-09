package cc.xbridge.gridpictureselector_library.base.util;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.util.Log;
import android.util.TypedValue;

import java.util.List;

public class BaseAdapterUtil {
    private static final Application sApp;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(BaseAdapterUtil.class.getSimpleName(), "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(BaseAdapterUtil.class.getSimpleName(), "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            sApp = app;
        }
    }

    private BaseAdapterUtil() {
    }

    public static Application getApp() {
        return sApp;
    }

    public static int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getApp().getResources().getDisplayMetrics());
    }

    public static int sp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, getApp().getResources().getDisplayMetrics());
    }

    public static int getDimensionPixelOffset(@DimenRes int resId) {
        return getApp().getResources().getDimensionPixelOffset(resId);
    }

    public static int getColor(@ColorRes int resId) {
        return getApp().getResources().getColor(resId);
    }

    public static Drawable rotateBitmap(Bitmap inputBitmap) {
        Matrix matrix = new Matrix();
        matrix.setRotate(90, (float) inputBitmap.getWidth() / 2, (float) inputBitmap.getHeight() / 2);

        float outputX = inputBitmap.getHeight();
        float outputY = 0;

        final float[] values = new float[9];
        matrix.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        matrix.postTranslate(outputX - x1, outputY - y1);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getHeight(), inputBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawBitmap(inputBitmap, matrix, paint);
        return new BitmapDrawable(null, outputBitmap);
    }

    public static boolean isListNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }
}
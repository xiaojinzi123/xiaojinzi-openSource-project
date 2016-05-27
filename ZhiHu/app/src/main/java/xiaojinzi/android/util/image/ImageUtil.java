package xiaojinzi.android.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import xiaojinzi.android.util.os.ScreenUtils;

/**
 * 有关图像的工具类
 *
 * @author cxj
 */
public class ImageUtil {

    /**
     * 设置bitmap的宽和高
     *
     * @param b
     * @param width
     * @param height
     * @return
     */
    public static Bitmap setBitmap(Bitmap b, int width, int height) {
        System.out.println("width = " + width);
        System.out.println("height = " + height);
        Bitmap target = Bitmap.createBitmap(width < 1 ? 1 : width, height < 1 ? 1 : height, b.getConfig());
        Canvas canvas = new Canvas(target);
        canvas.drawBitmap(b, null, new Rect(0, 0, width < 1 ? 1 : width, height < 1 ? 1 : height), null);
        return target;
//		return Bitmap.createScaledBitmap(b, width < 1 ? 1 : width, height < 1 ? 1 : height, false);
    }

    /**
     * 获取一个自适应的图片资源
     *
     * @param bitmap
     * @param context
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap bitmap, Context context) {
        int height = ScreenUtils.getScreenHeight(context);
        int width = ScreenUtils.getScreenWidth(context);

        if (height < 480 && width < 320) {
            return Bitmap.createScaledBitmap(bitmap, 32, 32, false);
        } else if (height < 800 && width < 480) {
            return Bitmap.createScaledBitmap(bitmap, 48, 48, false);
        } else if (height < 1024 && width < 600) {
            return Bitmap.createScaledBitmap(bitmap, 72, 72, false);
        } else {
            return Bitmap.createScaledBitmap(bitmap, 96, 96, false);
        }
    }

}

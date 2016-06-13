package xiaojinzi.base.android.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import xiaojinzi.base.android.os.ScreenUtils;

/**
 * 有关图像的工具类
 * 1.加载本地图片,压缩的形式
 *
 * @author xiaojinzi
 */
public class ImageUtil {

    /**
     * 加载一个本地的图片
     *
     * @param localImagePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeLocalImage(String localImagePath, int reqWidth, int reqHeight) {
        //获取大图的参数,包括大小
        BitmapFactory.Options options = getBitMapOptions(localImagePath);
        //获取一个合适的缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        //让BitmapFactory加载真的图片,等于true的时候只加载了图片的大小
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(localImagePath, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
    }

    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
//    private static int calculateInSampleSize(BitmapFactory.Options options,
//                                             int reqWidth, int reqHeight) {
//        // 源图片的宽度
//        int width = options.outWidth;
//        int height = options.outHeight;
//        int inSampleSize = 1;
//
//        if (width > reqWidth && height > reqHeight) {
//            // 计算出实际宽度和目标宽度的比率
//            int widthRatio = Math.round((float) width / (float) reqWidth);
//            int heightRatio = Math.round((float) width / (float) reqWidth);
//            inSampleSize = Math.max(widthRatio, heightRatio);
//        }
//        return inSampleSize;
//    }
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        if (src == null) {
            return null;
        }
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * 获取本地图片的大小
     * require permission:
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *
     * @param localImagePath
     * @return
     */
    public static BitmapFactory.Options getBitMapOptions(String localImagePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //计算出了图片的大小,没有真正的加载
        BitmapFactory.decodeFile(localImagePath, options);
        return options;
    }

    /**
     * 设置bitmap的宽和高
     *
     * @param b
     * @param width
     * @param height
     * @return
     */
    public static Bitmap setBitmap(Bitmap b, int width, int height) {
        return Bitmap.createScaledBitmap(b, width < 1 ? 1 : width, height < 1 ? 1 : height, false);
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

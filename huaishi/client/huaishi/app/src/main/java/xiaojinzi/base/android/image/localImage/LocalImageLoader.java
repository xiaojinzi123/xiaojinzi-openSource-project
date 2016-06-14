package xiaojinzi.base.android.image.localImage;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import xiaojinzi.base.android.image.ImageUtil;
import xiaojinzi.base.java.util.ThreadPool;

/**
 * 本地的图片加载器
 */
public class LocalImageLoader {

    private static ThreadPool threadPool;

    /**
     * 自身
     */
    private static LocalImageLoader localImageLoader = null;

    /**
     * 图片缓存的核心类
     */
    private static LruCache<String, Bitmap> mLruCache;

    /**
     * 用于设置图片到控件上
     */
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
            ImageView imageView = holder.imageView;
            Bitmap bm = holder.bitmap;
            String path = holder.path;
            if (imageView.getTag().toString().equals(path)) {
                imageView.setImageBitmap(bm);
            } else {
            }
        }
    };

    /**
     * 获取一个实例对象
     *
     * @return
     */
    public static synchronized LocalImageLoader getInstance() {
        if (localImageLoader == null) {
            localImageLoader = new LocalImageLoader();
            threadPool = ThreadPool.getInstance();
            // 获取应用程序最大可用内存
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            mLruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }
        return localImageLoader;
    }

    public void loadImage(final String imageLocalPath, final ImageView imageView) {

        imageView.setTag(imageLocalPath);

        //从一级缓存中拿
        Bitmap bitmap = mLruCache.get(imageLocalPath);

        if (bitmap == null) {
            //让线程池去执行任务,会限制线程的数量
            threadPool.invoke(new Runnable() {
                @Override
                public void run() {
                    LayoutParams lp = imageView.getLayoutParams();
                    //获取本地的图片的压缩图
                    Bitmap bm = ImageUtil.decodeLocalImage(imageLocalPath, lp.width, lp.height);
                    if (bm != null) {
                        //添加到一级缓存
                        addBitmapToLruCache(imageLocalPath, bm);
                        ImgBeanHolder holder = new ImgBeanHolder();
                        holder.bitmap = mLruCache.get(imageLocalPath);
                        holder.imageView = imageView;
                        holder.path = imageLocalPath;
                        Message message = Message.obtain();
                        message.obj = holder;
                        h.sendMessage(message);
                    }

                }
            });
        } else {
            imageView.setImageBitmap(bitmap);
        }

    }


    /**
     * 往LruCache中添加一张图片
     *
     * @param key
     * @param bitmap
     */
    private void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (mLruCache.get(key) == null) {
            if (bitmap != null)
                mLruCache.put(key, bitmap);
        }
    }


    /**
     * 几个信息的持有者,其实就是封装一下
     */
    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }


}

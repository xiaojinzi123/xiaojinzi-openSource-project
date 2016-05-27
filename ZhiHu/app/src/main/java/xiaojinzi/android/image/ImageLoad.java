package xiaojinzi.android.image;


import android.annotation.SuppressLint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.util.LruCache;
import android.widget.ImageView;

import xiaojinzi.android.net.AsyncHttp;
import xiaojinzi.android.net.adapter.BaseDataHandlerAdapter;
import xiaojinzi.android.util.image.ImageUtil;
import xiaojinzi.android.util.log.L;
import xiaojinzi.android.util.os.SystemInfo;


/**
 * 图片的异步加载器,api 12开始使用<br>
 * 功能:<br>
 * 1.使用方式:<br>
 * <p>
 * 使用者只需要同时传入一个图片网址和一个ImageView控件,<br>
 * 框架就可以把图片网址上的图片请求下来,<br>
 * 设置到ImageView控件上 并且在程序运行期间,<br>
 * 对同一个网址进行了内存上的缓存,下一次调用方法请求同一个网址的时候,就不会请求网址,直接从缓存中取出<br>
 * </p>
 * 2.结果:<br>
 * 2.1 取的是缓存中的图片:<br>
 * <p>
 * 2.1.1
 * </p>
 * <p/>
 * 2.2 缓存中没有,需要请求网址<br>
 * <p>
 * 2.2.1网址请求失败:<br>
 * 不会反生异常,可以在调用
 * {@link ImageLoad#asyncLoadImage(ImageView, String, Integer, boolean)}
 * 指定失败的时候使用的图片的资源地址<br>
 * ImageLoad可以设置一个配置的类,这个类中描述了<br>
 * ImageLoad需要把图片持久化到本地需要用到的的文件夹和网址与图片文件相对应的关系<br>
 * 如果失败了,ImageLoad会根据网址找到对应的本地的文件,<br>
 * 如果找到,ImageLoad将会从文件中读取,然后显示到ImageView控件上,并对其进行缓存<br>
 * 2.2 网址请求成功:<br>
 * 会对图片进行缓存,这里的缓存值得是内存中的缓存,<br>
 * 当然也可以调用方法指定参数,对图片设置是否缓存到本地
 * </p>
 *
 * @author cxj
 */
public class ImageLoad {

    /**
     * 类的标识
     */
    private static final String TAG = "ImageLoad";

    /**
     * 控制是否打印
     */
    private static final boolean isLog = false;

    // 创建缓存对象,对图片进行缓存的,一级缓存
    private LruCache<String, Bitmap> mCache;

    /**
     * 是否开启二级缓存
     */
    private static boolean isOpenTwoLevelCache = true;

    /**
     * sd卡上的二级缓存
     */
    private SDCardCacheManager sdCardCacheManager = null;

    /**
     * 构造函数私有化
     */
    private ImageLoad() {
        // 获取当前应用的可用的最大内存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 缓存所用到的大小
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<String, Bitmap>(cacheSize) {

            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 在每次存入缓存的时候调用
                if (SystemInfo.apiVersion >= 12) { // 兼容低版本api8
                    return value.getByteCount();
                } else {
                    return 0;
                }
            }

        };
        //初始化二级缓存
        sdCardCacheManager = new SDCardCacheManager();
    }

    // 声明一个自己
    private static ImageLoad my = null;

    /**
     * 获取实例对象
     *
     * @return
     */
    public static synchronized ImageLoad getInstance() {
        if (my == null) {
            my = new ImageLoad();
        }
        return my;
    }

    /**
     * 根据网址在缓存中获取图片
     *
     * @param url 图片的地址
     * @return
     */
    public Bitmap getBitMap(String url) {
        return mCache.get(url);
    }

    /**
     * 加入一张图片到缓存中
     *
     * @param url    图片的地址
     * @param bitmap 需要加入的bitmap图片
     */
    public void addBitMapToCache(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }

    /**
     * 从缓存中移除一个网址对应的图片
     *
     * @param url 图片的地址
     * @return
     */
    public Bitmap removeBitmap(String url) {
        return mCache.remove(url);
    }

    /**
     * 清理一级缓存
     */
    public void removeAll() {
        mCache.evictAll();
    }

    /**
     * 清理本地的二级缓存
     */
    public void clearLocalCache() {
        sdCardCacheManager.clear();
    }

    /**
     * 异步请求框架
     */
    private static AsyncHttp<Void> asynHttp = new AsyncHttp<Void>();

    /**
     * 异步加载图片
     *
     * @param img 需要显示网上图片的ImageView控件
     * @param url 需要请求的网址
     */
    public void asyncLoadImage(ImageView img, String url) {
        asyncLoadImage(img, url, null);
    }

    /**
     * 异步加载图片,加载错误的时候会显示默认的图片defaultImageId<br>
     * 会自动适应ImageView控件的大小
     *
     * @param img            需要显示网上图片的ImageView控件
     * @param url            需要请求的网址
     * @param defaultImageId 默认的使用的图片id
     */
    public void asyncLoadImage(ImageView img, String url, Integer defaultImageId) {
        asyncLoadImage(img, url, defaultImageId, true);
    }

    /**
     * 异步加载图片,加载错误的时候会显示默认的图片defaultImageId
     *
     * @param img            需要显示网上图片的ImageView控件
     * @param url            需要请求的网址
     * @param isFitImageView 是不是需要适应ImageView控件的大小
     */
    public void asyncLoadImage(ImageView img, String url, boolean isFitImageView) {
        asyncLoadImage(img, url, null, isFitImageView);
    }

    /**
     * 异步加载图片,加载错误的时候会显示默认的图片defaultImageId
     *
     * @param img            需要显示网上图片的ImageView控件
     * @param url            需要请求的网址
     * @param defaultImageId 默认的使用的图片id
     * @param isFitImageView 是不是需要适应ImageView控件的大小
     */
    public void asyncLoadImage(ImageView img, String url, Integer defaultImageId, boolean isFitImageView) {
        asyncLoadImage(img, url, defaultImageId, isFitImageView, true, null);
    }

    /**
     * 异步加载图片,加载错误的时候会显示默认的图片defaultImageId
     *
     * @param img              需要显示网上图片的ImageView控件
     * @param url              需要请求的网址
     * @param defaultImageId   默认的使用的图片id
     * @param isFitImageView   是不是需要适应ImageView控件的大小
     * @param isCacheToLocal   是否缓存到本地
     * @param onResultListener 回掉接口,告知使用者是否成功加载
     */
    public void asyncLoadImage(final ImageView img, final String url, final Integer defaultImageId,
                               final boolean isFitImageView, final boolean isCacheToLocal, final OnResultListener onResultListener) {

        // 从缓存中获取图片
        Bitmap bitmap = mCache.get(url);

        if (bitmap == null) { // 如果是null就是没有缓存下来,那么接着从二级缓存中看看有没有

            if (isLog) {
                L.s(TAG, "一级缓存中没有,现在检测二级缓存");
            }

            //如果二级缓存有用
            if (sdCardCacheManager.isEnable() && isOpenTwoLevelCache) {
                if (isLog) {
                    L.s(TAG, "二级缓存功能可用");
                }
                byte[] cacheImage = sdCardCacheManager.getCacheImage(url);
                if (cacheImage != null) {
                    if (isLog) {
                        L.s(TAG, "二级缓存里面有图片资源,省去了网络访问");
                    }
                    setImage(cacheImage, img, url, isFitImageView);
                    if (onResultListener != null) {
                        onResultListener.onResult(true);
                    }
                    return;
                } else {
                    if (isLog) {
                        L.s(TAG, "二级缓存中没有,准备去网络请求了");
                    }
                }
            } else {
                if (isLog) {
                    L.s(TAG, "二级缓存功能不可用");
                }
            }

            // 请求图片的二进制数据
            asynHttp.get(url, AsyncHttp.BaseDataHandler.BYTEARRAYDATA, new BaseDataHandlerAdapter() {

                @Override
                public void handler(byte[] bt) { //如果请求图片成功,不仅要放在一级缓存中,还要放在二级缓存中
                    setImage(bt, img, url, isFitImageView);
                    if (onResultListener != null) {
                        onResultListener.onResult(true);
                    }
                    if (sdCardCacheManager.isEnable() && isCacheToLocal && isOpenTwoLevelCache) {
                        sdCardCacheManager.cacheImage(bt, url);
                    }
                }

                @Override
                public void error(Exception e) {
                    if (isLog) {
                        L.s(TAG, "请求网络图片错误,错误已打印,查看LogCat的警告等级下的信息");
                        e.printStackTrace();
                    }

                    // 如果有默认图片
                    if (defaultImageId != null) {
                        // 设置图片
                        img.setImageResource(defaultImageId);
                        // 如果图片要适应ImageView控件,转化图片的大小
                        if (isFitImageView) {
                            img.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }
                    if (onResultListener != null) {
                        onResultListener.onResult(false);
                    }
                }
            });
        } else { // 如果一级缓存中有
            if (isLog) {
                L.s(TAG, "一级缓存中有");
            }
            // 如果图片要适应ImageView控件,转化图片的大小
            if (isFitImageView) {
                img.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            img.setImageBitmap(bitmap);
            if (onResultListener != null) {
                onResultListener.onResult(true);
            }
        }
    }

    /**
     * 设置图片到imageview控件上
     *
     * @param bt             字节数组
     * @param img            ImageView控件
     * @param url            网址
     * @param isFitImageView 是否适应控件大小
     */
    private void setImage(byte[] bt, ImageView img, String url, final boolean isFitImageView) {
        // 对字节数组进行转化
        Bitmap bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);

        // 如果图片要适应ImageView控件,转化图片的大小
        if (isFitImageView) {
            img.setScaleType(ImageView.ScaleType.FIT_XY);
//            bitmap = ImageUtil.setBitmap(bitmap, img.getMeasuredWidth(), img.getMeasuredHeight());
        }

        // 放入缓存对象中
        mCache.put(url, bitmap);

        // 设置imageView控件的图片
        img.setImageBitmap(bitmap);

    }

    /**
     * 请求结果的监听
     *
     * @author cxj
     */
    public interface OnResultListener {
        /**
         * 通知监听的用户,是否成功
         *
         * @param isSuccess
         */
        public void onResult(boolean isSuccess);
    }


}

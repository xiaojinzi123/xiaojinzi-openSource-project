package xiaojinzi.android.image;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import xiaojinzi.android.util.log.L;
import xiaojinzi.android.util.os.SDCardUtils;
import xiaojinzi.android.util.store.FileUtil;
import xiaojinzi.java.util.StringUtil;


/**
 * Created by cxj on 2016/1/18.
 * sd卡上的图片缓存管理对象,imageload配合这个使用
 */
public class SDCardCacheManager {

    /**
     * 类的标识
     */
    private String tag = "SDCardCacheManager";

    /**
     * 是否输出信息
     */
    private boolean isLog = false;

    /**
     * sd卡上的缓存文件夹对象
     */
    private File cacheFolder = new File(Environment.getExternalStorageDirectory(), "imageLoadCache");

    /**
     * 文件夹的深度,实际要比这个多一层
     */
    private int folderDeep = 3;

    /**
     * 计算网址要保存在本地的路径 3/6/8/15
     *
     * @param url 根据这个url计算出要保存的路径
     * @return
     */
    private String computeSavePath(String url) {
        StringBuffer sb = new StringBuffer();
        //拿到url的字节数组
        byte[] bytes = url.getBytes();
        int step = bytes.length / folderDeep;
        for (int i = 0; i < bytes.length; i = i + step) {
            sb.append(bytes[i] + "/");
        }
        sb.append(step);
        return sb.toString();
    }

    /**
     * 缓存图片资源到sd卡,路径什么的是根据url计算的,形成二级缓存
     *
     * @param bts 图片的字节数组
     * @param url 图片的请求网址
     */
    public void cacheImage(byte[] bts, String url) {
        if (!isEnable()) {
            if (isLog) {
                L.s(tag, "缓存功能不可用,缓存图片失败");
            }
            return;
        }
        //获取这个url的保存路径
        String savePath = computeSavePath(url);
        File folder = new File(cacheFolder, savePath);
        //如果文件夹不存在,就创建
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }

        File file = new File(folder, StringUtil.getLastContent(url, "/"));
        if (isLog) {
            L.s(tag, "尝试缓存图片:" + file.getPath());
        }
        //异步保存图片资源
        FileUtil.getInstance().asyncSaveFile(file, bts);
    }

    /**
     * 根据url获取图片资源,前提是缓存功能可用
     *
     * @param url 请求的地址
     * @return
     */
    public byte[] getCacheImage(String url) {
        if (!isEnable()) {
            return null;
        }
        File cacheFile = getCacheFile(url);
        if (!cacheFile.exists()) {
            return null;
        }
        try {
            byte[] bytes = xiaojinzi.java.util.FileUtil.readFileToBytes(cacheFile);
            if (isLog) {
                L.s(tag, "二级缓存中拿出图片资源:" + cacheFile.getPath());
            }
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 返回缓存功能是否可用
     *
     * @return
     */
    public boolean isEnable() {
        //第一步,先判断sd卡是不是可用
        boolean sdCardEnable = SDCardUtils.isSDCardEnable();
        if (!sdCardEnable) {
            return false;
        }
        //第二步,判断缓存文件夹是否存在,如果不存在,就尝试创建
        if (!cacheFolder.exists()) {
            //如果创建文件夹失败,则缓存功能不可用
            boolean b = cacheFolder.mkdir();
            if (!b) {
                return false;
            }
        }
        //缺少权限的那种情况,交给用户自己去发现
        return true;
    }

    /**
     * 根据url,获取这个url对应的缓存文件对象
     *
     * @param url 请求的网址
     * @return 如果返回不是null, 说明二级缓存有, 如果是null, 表示功能不可用或者二级缓存中没有
     */
    public File getCacheFile(String url) {
        //如果缓存功能不可用,就返回null
        if (!isEnable()) {
            return null;
        }
        //获取到这个url的缓存路径
        String cachePath = computeSavePath(url);
        File folder = new File(cacheFolder, cachePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return new File(folder, StringUtil.getLastContent(url, "/"));
    }

    /**
     * 清理所有的缓存
     */
    public void clear() {
        if (cacheFolder.exists()) {
            File[] files = cacheFolder.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                file.delete();
            }
        }
    }

}

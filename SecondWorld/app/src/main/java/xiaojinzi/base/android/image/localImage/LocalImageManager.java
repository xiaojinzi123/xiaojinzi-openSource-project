package xiaojinzi.base.android.image.localImage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import xiaojinzi.base.java.util.StringUtil;

/**
 * Created by cxj on 2016/5/4.
 * a manager of localImage
 * require permission:<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * you can copy to your AndroidManifest.xml
 */
public class LocalImageManager {

    /**
     * png图片格式的类型
     * the mime_type of png
     */
    public static final String PNG_MIME_TYPE = "image/png";

    /**
     * png图片的后缀
     */
    public static final String PNG_SUFFIX = "png";

    /**
     * jpeg图片格式的类型
     * the mime_type of jpeg
     */
    public static final String JPEG_MIME_TYPE = "image/jpeg";

    /**
     * jpg图片格式的类型
     * the mime_type of jpg
     */
    public static final String JPG_MIME_TYPE = "image/jpeg";

    /**
     * jpeg图片的后缀
     */
    public static final String JPEG_SUFFIX = "jpeg";

    /**
     * jpg图片的后缀
     */
    public static final String JPG_SUFFIX = "jpg";

    /**
     * 上下文对象
     * the environment of app
     */
    private static Context context;

    /**
     * init
     *
     * @param context
     */
    public static void init(Context context) {
        if (LocalImageManager.context == null) {
            LocalImageManager.context = context;
        }
    }


    /**
     * 根据文件夹的路径,查询对应mime_type类型的图片路径的集合
     *
     * @param localImageInfo 可以为NULL,当为NULL的时候,不缓存
     * @param folderPath
     * @return
     */
    @Nullable
    public static List<String> queryImageByFolderPath(LocalImageInfo localImageInfo, String folderPath) {
        //图片的类型
        String[] mimeType = localImageInfo.getMimeType();
        //健壮性判断
        if (folderPath == null || "".equals(folderPath)
                || mimeType.length == 0) {
            return null;
        }
        //声明返回值
        List<String> images;

        //如果只是单纯的查询,一次性的,那么可以传入localImageInfo为空
        if (localImageInfo == null) {
            images = new ArrayList<String>();
        } else { //否则从缓存中拿出来看看
            images = localImageInfo.getImagesByFolderPath(folderPath);
        }
        //如果没有,那就自己去查询
        if (images == null) {
            //创建集合
            images = new ArrayList<String>();
            //创建文件对象爱那个
            File folder = new File(folderPath);
            //如果文件夹存在
            if (folder.exists() && folder.isDirectory()) {
                //获取所有的文件对象
                File[] files = folder.listFiles();
                //循环所有的文件对象
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    //如果是文件,而不是文件夹,并且文件的后缀匹配了
                    if (file.isFile() && isFileMatchMimeType(file, mimeType)) {
                        //添加到集合中
                        images.add(file.getPath());
                    }
                }
                if (localImageInfo != null) {
                    //集合保存起来,下次调用的时候就不会查询了,直接返回
                    localImageInfo.setImagesByFolderPath(folderPath, images);
                }
                return images;
            }
        } else {
            return images;
        }

        return null;

    }

    /**
     * 查询系统中存储的所有图片的路径和文件夹的路径
     * 其实就是
     * {@link LocalImageManager#queryImage(LocalImageInfo)}
     * 和
     * {@link LocalImageManager#queryAllFolders(LocalImageInfo)}
     * 的合体
     *
     * @param localImageInfo 本地图片的描述对象
     * @return
     */
    @Nullable
    public static LocalImageInfo queryImageWithFolder(LocalImageInfo localImageInfo) {
        String[] mimeType = localImageInfo.getMimeType();
        if (mimeType.length == 0) {
            return localImageInfo;
        }
        return queryAllFolders(queryImage(localImageInfo));
    }

    /**
     * 查询出所有图片的文件夹的路径,根据{@link LocalImageInfo#imageFiles}集合进行整理的
     * 结果放在{@link LocalImageInfo#imageFolders}
     *
     * @param localImageInfo 本地图片的一个描述信息
     * @return
     */
    public static LocalImageInfo queryAllFolders(LocalImageInfo localImageInfo) {
        //获取图片的文件夹
        List<String> imageFiles = localImageInfo.getImageFiles();
        //获取存放图片路径的文件夹集合
        Set<String> set = localImageInfo.getImageFolders();
        set.clear();
        int size = imageFiles.size();
        //循环所有的图片路径,找出所有的文件夹路径,不能重复
        for (int i = 0; i < size; i++) {
            File imageFile = new File(imageFiles.get(i));
            if (imageFile.exists()) {
                File parentFile = imageFile.getParentFile();
                if (parentFile.exists()) {
                    set.add(parentFile.getPath());
                }
            }
        }
        return localImageInfo;
    }

    /**
     * query by mime_type of image
     * 根据图片的类型进行查询,查询的是系统中存储的图片信息
     * 结果放在{@link LocalImageInfo#imageFiles}
     *
     * @param localImageInfo 本地图片的一个描述信息
     * @return
     */
    public static LocalImageInfo queryImage(LocalImageInfo localImageInfo) {

        String[] mimeType = localImageInfo.getMimeType();

        if (mimeType.length == 0) {
            return localImageInfo;
        }

        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context
                .getContentResolver();

        //查询的条件
        StringBuffer selection = new StringBuffer();
        //利用循环生成条件
        for (int i = 0; i < mimeType.length; i++) {
            //图片的类型
            String mime = mimeType[i];
            if (i == mimeType.length - 1) { //如果是最后一个
                selection.append(MediaStore.Images.Media.MIME_TYPE + " = ?");
            } else {
                selection.append(MediaStore.Images.Media.MIME_TYPE + " = ? or ");
            }
        }

        //执行查询
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                selection.toString(),
                mimeType,
                MediaStore.Images.Media.DATE_MODIFIED);

        List<String> imageFiles = localImageInfo.getImageFiles();
        imageFiles.clear();

        //循环结果集
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String imagePath = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            imageFiles.add(imagePath);
        }

        return localImageInfo;
    }

    /**
     * 文件的后缀是不是匹配mimeType类型
     *
     * @param file      不能为空 must not be NULL
     * @param mime_type 不能为空 must not be NULL
     * @return
     */
    private static boolean isFileMatchMimeType(File file, String... mime_type) {
        String SUFFIX = StringUtil.getLastContent(file.getName(), ".").toLowerCase();
        for (int i = 0; i < mime_type.length; i++) {
            switch (mime_type[i]) {
                case PNG_MIME_TYPE:
                    if (PNG_SUFFIX.equals(SUFFIX)) {
                        return true;
                    }
                    break;
                case JPEG_MIME_TYPE:
                    if (JPEG_SUFFIX.equals(SUFFIX) || JPG_SUFFIX.equals(SUFFIX)) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

}

package xiaojinzi.base.android.image.localImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cxj on 2016/5/4.
 * 本地图片的一个信息,对图片的一个描述
 */
public class LocalImageInfo {

    /**
     * 信息的图片类型的数组
     * 图片的类型
     * {@link LocalImageManager#JPEG_MIME_TYPE}
     * {@link LocalImageManager#PNG_MIME_TYPE}
     * {@link LocalImageManager#JPG_MIME_TYPE}
     */
    private String[] mimeType;

    /**
     * 一个对象的图片类型是定死的,方便维护,对象一经创建
     * 这个类型就更改不了了
     *
     * @param mimeType 图片的类型
     *                 {@link LocalImageManager#JPEG_MIME_TYPE}
     *                 {@link LocalImageManager#PNG_MIME_TYPE}
     *                 {@link LocalImageManager#JPG_MIME_TYPE}
     */
    public LocalImageInfo(String[] mimeType) {
        //如果传进来是空的,直接就挂了
        this.mimeType = new String[mimeType.length];

        for (int i = 0; i < mimeType.length; i++) {
            this.mimeType[i] = mimeType[i];
        }

    }

    /**
     * 图片文件的文件夹路径
     */
    private Set<String> imageFolders = new HashSet<String>();

    /**
     * 所有图片文件的路径
     */
    private List<String> imageFiles = new ArrayList<String>();

    /**
     * 一个map集合,用于保存对应文件夹路径对应的图片路径的集合
     */
    private Map<String, List<String>> map = new HashMap<String, List<String>>();


    /**
     * 获取一个新的类型数组,和原来的不一样
     *
     * @return
     */
    public String[] getMimeType() {
        String[] arr = new String[this.mimeType.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = this.mimeType[i];
        }
        return arr;
    }

    public Set<String> getImageFolders() {
        return imageFolders;
    }

    public void setImageFolders(Set<String> imageFolders) {
        this.imageFolders = imageFolders;
    }

    public List<String> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<String> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public List<String> getImagesByFolderPath(String folderPath) {
        return map.get(folderPath);
    }

    public void setImagesByFolderPath(String folderPath, List<String> list) {
        map.put(folderPath, list);
    }

}

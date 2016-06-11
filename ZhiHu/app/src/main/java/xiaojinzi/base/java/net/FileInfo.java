package xiaojinzi.base.java.net;

import java.io.File;

/**
 * Created by cxj on 2016/5/29.
 * post提交的时候文件的描述对象
 */
public class FileInfo {

    /**
     * 文件对象
     */
    public File file;

    /**
     * 文件对应的key,并非文件名
     */
    public String key;

    /**
     * 类型
     */
    public String contentType;

    public FileInfo(String key, File file) {
        this.file = file;
        this.key = key;
    }

    public FileInfo(String key, File file, String contentType) {
        this.file = file;
        this.key = key;
        this.contentType = contentType;
    }
}

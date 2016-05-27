package xiaojinzi.java.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;

/**
 * Created by xiaojinzi on 2015/8/25.
 */
public class FileUtil {

    /**
     * 把一个文件读取成一个字节数组
     *
     * @param f 要读取的文件
     * @return
     * @throws IOException
     */
    public static byte[] readFileToBytes(File f) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(f);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bt = new byte[1024];
        int len = -1;
        while ((len = fileInputStream.read(bt)) != -1) {
            byteArrayOutputStream.write(bt, 0, len);
        }
        //关闭资源
        fileInputStream.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return bytes;
    }

    public static File strToFile(String content, String filePath) {
        return null;
    }

    /**
     * 把一个流对象转化成一个文件
     *
     * @param is 要转化的流
     * @param f  目标文件对象
     * @return
     * @throws IOException
     */
    public static File isToFile(InputStream is, String filePath, ProgressDialog pd) throws IOException {
        return isToFile(is, new File(filePath), pd);
    }

    /**
     * 把一个流对象转化成一个文件
     *
     * @param is 要转化的流
     * @param f  目标文件对象
     * @param pd
     * @return
     * @throws IOException
     */
    public static File isToFile(InputStream is, File f, ProgressDialog pd) throws IOException {

        // 定义缓冲区
        byte[] bts = new byte[1024];

        // 定义读取的长度
        int len = -1;

        // 创建文件的输出流
        FileOutputStream out = new FileOutputStream(f);

        // 循环读取
        while ((len = is.read(bts)) != -1) {
            out.write(bts, 0, len);
            if (pd != null) {
                pd.setProgress(pd.getProgress() + len);
            }
        }

        // 关闭资源
        is.close();
        out.close();

        return f;
    }

    /**
     * 对制定的文件夹进行便利,并且筛选出某些文件,筛选的工作交给用户来做
     *
     * @param folder
     * @param fileFilter
     * @return
     */
    public static List<File> listFiles(File folder, FileFilter fileFilter) {

        if (folder == null) {
            return new ArrayList<File>();
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return new ArrayList<File>();
        }

        // 声明返回值变量
        List<File> list = new ArrayList<File>();

        for (int i = 0; i < files.length; i++) {
            File tmp_file = files[i];
            if (tmp_file.isFile()) { // 如果是文件
                if (fileFilter.doFilter(tmp_file)) {
                    list.add(tmp_file);
                }
            } else {
                List<File> fileList = listFiles(tmp_file, fileFilter);
                if (fileList != null) {
                    list.addAll(fileList);
                }
            }
        }

        return list;
    }

    /**
     * 过滤文件的接口
     *
     * @author xiaojinzi
     */
    public interface FileFilter {
        public boolean doFilter(File file);
    }

}

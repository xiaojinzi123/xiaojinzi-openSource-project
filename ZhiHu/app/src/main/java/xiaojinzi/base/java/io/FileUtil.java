package xiaojinzi.base.java.io;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojinzi on 2015/8/25.
 */
public class FileUtil {

    /**
     * 保存字节数据到一个文件
     *
     * @param f   要保存的文件的对象
     * @param bts 字节数组
     * @return
     * @throws IOException 抛出的异常
     */
    public static File saveFile(File f, byte[] bts) throws IOException {
        FileOutputStream o = new FileOutputStream(f);
        o.write(bts);
        o.close();
        return f;
    }

    /**
     * 返回文件字节数组
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static byte[] getFileBytes(File f) throws Exception {

        // 声明返回值
        byte[] bts;

        FileInputStream in = new FileInputStream(f);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        InputStreamUtil.inputStreamIterator(in, new InputStreamUtil.HanderByteArray() {

            @Override
            public void hander(byte[] bt, int len) throws Exception {
                out.write(bt, 0, len);
            }
        });

        bts = out.toByteArray();

        // 关闭资源
        in.close();
        out.close();

        return bts;
    }

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

    /**
     * 读取文件字节输出到输出流中
     *
     * @param f
     * @param outputStream
     * @throws IOException
     */
    public static void readFileToOutputStream(File f, OutputStream outputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(f);
        byte[] bt = new byte[1024];
        int len = -1;
        while ((len = fileInputStream.read(bt)) != -1) {
            outputStream.write(bt, 0, len);
        }
        //关闭资源
        fileInputStream.close();
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

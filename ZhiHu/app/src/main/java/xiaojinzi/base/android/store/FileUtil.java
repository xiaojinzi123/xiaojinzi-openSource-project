package xiaojinzi.base.android.store;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.ProgressDialog;
import android.os.Handler;

import xiaojinzi.base.android.log.L;
import xiaojinzi.base.java.io.InputStreamUtil;

/**
 * android环境下的文件工具
 *
 * @author xiaojinzi QQ:347837667
 * @date 2015年12月8日
 */
public class FileUtil implements Runnable {

    /**
     * 类的标识
     */
    private static final String TAG = "xiaojinzi.android.util.FileUtil";

    /**
     * 是否打印日志
     */
    private static final boolean isLog = false;

    /* 构造函数私有化 */
    private FileUtil() {
    }

    /**
     * 声明一个自己
     */
    private static FileUtil my = null;

    /**
     * 获取实例
     *
     * @return
     */
    public synchronized static FileUtil getInstance() {
        if (my == null) {
            my = new FileUtil();
        }
        return my;
    }

    /* 任务的集合 */
    private Vector<Task> tasks = new Vector<Task>();

    /* 结果的集合 */
    private Vector<TaskResult> results = new Vector<TaskResult>();

    /* 线程之间通信的工具 */
    private Handler h = new Handler() {
        public void handleMessage(android.os.Message msg) {

            int what = msg.what;

            // 从结果集合中拿出一个,并且删除一个
            TaskResult taskResult = results.remove(0);

            if (taskResult.fileHander == null) {
                return;
            }

            if (what == FileHander.SUCCESS) {
                taskResult.fileHander.fileHander(taskResult.f);
            } else if (what == FileHander.ERROR) {
                taskResult.fileHander.error(taskResult.f, taskResult.e);
            }

        }

        ;
    };

    /**
     * 异步保存文件,此方法可以多次调用
     *
     * @param f  要保存的文件的对象
     * @param is 输入流对象
     */
    public void asyncSaveFile(File f, InputStream is) {
        asyncSaveFile(f, is, null);
    }

    /**
     * 异步保存文件,此方法可以多次调用
     *
     * @param f  要保存的文件的对象
     * @param is 输入流对象
     * @param fh 回调接口
     */
    public void asyncSaveFile(File f, InputStream is, FileHander fh) {
        if (f == null || is == null) {
            return;
        }

        boolean b = f.getParentFile().exists();

        if (!b) {
            boolean isMkdirs = f.getParentFile().mkdirs();
            if (isMkdirs) {
                if (isLog)
                    L.s(TAG, "成功创建文件夹:" + f.getPath());
            } else {
                if (isLog)
                    L.s(TAG, "创建文件夹失败:" + f.getPath());
                return;
            }
        }

        // 添加一个任务
        tasks.add(new Task(fh, f, is));

		/* 开启子线程去处理数据 */
        new Thread(this).start();

    }

    /**
     * @param f   要保存的文件的对象
     * @param bts 字节数组
     */
    public void asyncSaveFile(File f, byte[] bts) {
        asyncSaveFile(f, bts, null);
    }

    /**
     * 异步保存一个文件
     *
     * @param f   要保存的文件的对象
     * @param bts 字节数组
     * @param fh  回调接口
     */
    public void asyncSaveFile(File f, byte[] bts, FileHander fh) {
        if (f == null || bts == null) {
            return;
        }

        boolean b = f.getParentFile().exists();

        if (!b) {
            boolean isMkdirs = f.getParentFile().mkdirs();
            if (isMkdirs) {
                if (isLog)
                    L.s(TAG, "成功创建文件夹:" + f.getPath());
            } else {
                if (isLog)
                    L.s(TAG, "创建文件夹失败:" + f.getPath());
                return;
            }
        }

        // 添加一个任务
        tasks.add(new Task(f, bts, fh));

		/* 开启子线程去处理数据 */
        new Thread(this).start();

    }



    public static File strToFile(String content, String filePath) {
        return null;
    }

    /**
     * 把一个流对象转化成一个文件
     *
     * @param is       要转化的流
     * @param filePath 目标文件路径
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
     * 一次处理的任务
     *
     * @author cxj QQ:347837667
     * @date 2015年12月8日
     */
    private class Task {
        public File f = null;
        public InputStream is = null;
        private byte[] bts = null;
        public FileHander fileHander = null;

        public Task(FileHander fileHander, File f, InputStream is) {
            super();
            this.fileHander = fileHander;
            this.f = f;
            this.is = is;
        }

        public Task(File f, byte[] bts, FileHander fileHander) {
            super();
            this.f = f;
            this.bts = bts;
            this.fileHander = fileHander;
        }

    }

    /**
     * 处理完毕的结果
     *
     * @author cxj QQ:347837667
     * @date 2015年12月8日
     */
    private class TaskResult {
        public FileHander fileHander = null;
        public File f = null;
        public Exception e;

        public TaskResult(FileHander fileHander, File f, Exception e) {
            super();
            this.fileHander = fileHander;
            this.f = f;
            this.e = e;
        }
    }

    /**
     * 文件处理的回调接口
     *
     * @author cxj QQ:347837667
     * @date 2015年12月8日
     */
    public interface FileHander {

        /**
         * 成功的标识
         */
        public static final int SUCCESS = 0;

        /**
         * 失败的标识
         */
        public static final int ERROR = 1;

        /**
         * 文件处理完毕处理的函数
         *
         * @param f 处理好的文件的对象
         */
        void fileHander(File f);

        /**
         * 处理的时候错误了
         *
         * @param f
         * @param e
         */
        void error(File f, Exception e);

    }

    @Override
    public void run() {
        // 拿到一个任务
        Task task = tasks.remove(0);
        try {
            final FileOutputStream o = new FileOutputStream(task.f);
            if (task.bts == null) {
                InputStreamUtil.inputStreamIterator(task.is, new InputStreamUtil.HanderByteArray() {
                    @Override
                    public void hander(byte[] bt, int len) throws Exception {
                        o.write(bt);
                    }
                });
            } else {
                o.write(task.bts);
            }
            o.close();
            // 结果的集合中加上一个返回对象
            results.add(new TaskResult(task.fileHander, task.f, null));
            h.sendEmptyMessage(FileHander.SUCCESS);
        } catch (Exception e) {
            // 结果的集合中加上一个返回对象
            results.add(new TaskResult(task.fileHander, task.f, e));
            h.sendEmptyMessage(FileHander.ERROR);
        }
    }

}

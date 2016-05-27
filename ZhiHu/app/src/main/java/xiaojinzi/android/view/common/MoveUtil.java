package xiaojinzi.android.view.common;

import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxj on 2016/3/21.
 * 试图滚动的时候的工具类
 */
public class MoveUtil {

    private List<Point> points = new ArrayList<Point>();


    /**
     * 手指触摸
     *
     * @param e
     */
    public void touch(MotionEvent e) {
        int action = e.getAction();
        int x = (int) e.getX();
        int y = (int) e.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            points.clear();
        } else if (action == MotionEvent.ACTION_MOVE) {
            points.add(new Point(x, y));
        }
    }

    /**
     * 返回是否水平加速
     *
     * @return
     */
    public Boolean isHorizontalAccelerate() {
        //要检测的加速区域个数
        int count = 3;
        int[] arr = new int[count];
        if (points.size() > count) {
            for (int i = 0; i < points.size() - count - 1; i++) {
                for (int j = 0; j < arr.length; j++) {
                    arr[j] = points.get(i + j + 1).x - points.get(i + j).x;
                }
                if (isAccelerate(arr)) {
                    return true;
                }
            }
            return false;
        } else {
            return null;
        }
    }

    /**
     * 数组的值是否加速了
     *
     * @param arr
     * @return
     */
    private boolean isAccelerate(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (Math.abs(arr[i]) > Math.abs(arr[i + 1])) {
                return false;
            }
        }
        return true;
    }

}

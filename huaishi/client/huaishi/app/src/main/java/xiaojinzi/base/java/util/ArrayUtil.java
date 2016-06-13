package xiaojinzi.base.java.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 一些集合的工具类
 *
 * @author xiaojinzi
 */
public class ArrayUtil {

    /**
     * 升序的排序
     */
    public static final int UP_SORT_FLAG = 0;

    /**
     * 降序的排序
     */
    public static final int DOWN_SORT_FLAG = 1;

    /**
     * 对一个List集合进行升序或者降序的排序,这里一定要注意,集合中的元素一定要实现{@link Comparable} 这个接口
     *
     * @param list 要排序的集合,集合中的元素是什么不关心
     * @param flag 升序还是降序 {@link ArrayUtil#UP_SORT_FLAG} or
     *             {@link ArrayUtil#DOWN_SORT_FLAG}
     */
    @SuppressWarnings("unchecked")
    public static <T> void sort(List<T> list, int flag) {
        if (flag == UP_SORT_FLAG) { // 如果是升序
            for (int i = 0; i < list.size() - 1; i++) {
                Comparable<T> c = (Comparable<T>) list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    if (c.compareTo(list.get(j)) > 0) { // 如果比较出来c比较大
                        swap(list, i, j);
                        c = (Comparable<T>) list.get(i);
                    }
                }
            }
        } else if (flag == DOWN_SORT_FLAG) { // 如果是降序
            for (int i = 0; i < list.size() - 1; i++) {
                Comparable<T> c = (Comparable<T>) list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    if (c.compareTo(list.get(j)) < 0) { // 如果比较出来c比较大
                        swap(list, i, j);
                        c = (Comparable<T>) list.get(i);
                    }
                }
            }
        }
    }

    /**
     * 对集合中两个下标处的元素进行换位置
     *
     * @param list
     * @param i
     * @param j
     */
    public static <T> void swap(List<T> list, int i, int j) {
        T t = list.get(i);
        list.set(i, list.get(j));
        list.set(j, t);
    }

    /**
     * 把一个List集合中的数据转化成数组的形式
     *
     * @param list
     * @return
     */
    public static Object[] listToArray(List<?> list) {
        Object[] obs = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            obs[i] = list.get(i);
        }
        return obs;
    }

    /**
     * set集合转化成List集合
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T> List<T> setToList(Set<T> set) {
        List<T> list = new ArrayList<T>();
        Iterator<T> iterator = set.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            list.add(t);
        }
        return list;
    }

    /**
     * 对Map集合进行迭代
     *
     * @param map
     */
    public static <key, value> void iteratorMap(Map<key, value> map, MapIterator<key, value> mapIterator) {
        // 拿到所有的key
        Set<Entry<key, value>> entrySet = map.entrySet();
        // 拿到key的迭代器
        Iterator<Entry<key, value>> it = entrySet.iterator();
        while (it.hasNext()) {
            Entry<key, value> entity = it.next();
            mapIterator.iterator(entity);
        }
    }

    /**
     * Map迭代回调接口
     *
     * @author cxj QQ:347837667
     * @date 2015年12月10日
     */
    public interface MapIterator<key, value> {
        /**
         * 回调的方法
         *
         * @param entity
         */
        void iterator(Entry<key, value> entity);
    }

}

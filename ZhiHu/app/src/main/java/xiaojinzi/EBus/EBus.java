package xiaojinzi.EBus;

import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import xiaojinzi.base.android.log.L;
import xiaojinzi.base.java.util.ArrayUtil;
import xiaojinzi.base.java.util.StringUtil;


/**
 * Created by cxj on 2016/1/20.
 * <p>
 * 请不要使用基本数据类型来接受参数,
 * 不支持,请使用对应的包装类
 */
public class EBus {

    /**
     * 声明一个自己
     */
    private static EBus eBus = null;

    /**
     * 构造函数私有化
     */
    private EBus() {
    }

    /**
     * 获取实例对象
     *
     * @return
     */
    public synchronized static EBus getInstance() {
        if (eBus == null) {
            eBus = new EBus();
        }
        return eBus;
    }

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (eventTasks.size() > 0) {
                EventTask eventTask = eventTasks.remove(0);
                if (eventTask.eventType == EventTask.MATCH_TAG_METHODNAME) {
                    postEvent(eventTask.tagObjectFalg, eventTask.methodName, eventTask.o);
                } else if (eventTask.eventType == EventTask.MATCH_METHODNAME) {
                    postEvent(eventTask.methodName, eventTask.o);
                } else if (eventTask.eventType == EventTask.MATCH_ALL) {
                    postEvent(eventTask.o);
                }
            }
        }
    };

    /**
     * 类的标识
     */
    public static final String TAG = "EBus";

    /**
     * 这个map用来存放引用,key为value对象的class文件的名字
     */
    private static Map<String, Object> map = new HashMap<String, Object>();

    /**
     * 延时执行的时候用到的线程安全的集合
     */
    private static Vector<EventTask> eventTasks = new Vector<EventTask>();

    /**
     * 方法的前缀
     */
    public static final String METHOD_PREFIX = "onEvent";


    /**
     * 注册自己可以被接受消息
     *
     * @param o 要注册的对象
     */
    public static void register(Object o) {
        String className = o.getClass().getName();
        className = StringUtil.getLastContent(className, ".");
        register(className, o);
    }

    /**
     * 注册自己可以被接受消息
     *
     * @param tag 唯一标识,自定义
     * @param o   要注册的对象
     */
    public static void register(String tag, Object o) {
        map.put(tag, o);
    }

    /**
     * 反注册自己,也就是注销自己
     *
     * @param o 要注销的对象
     */
    public static void unRegister(Object o) {
        String className = o.getClass().getName();
        className = StringUtil.getLastContent(className, ".");
        unRegister(className);
    }

    /**
     * 反注册自己,也就是注销自己
     *
     * @param tag 对象的标识,也就是要注销的标识
     */
    public static void unRegister(String tag) {
        map.remove(tag);
    }

    /**
     * 发送延时消息
     *
     * @param delayDuration
     */
    private void sendMessage(final Integer delayDuration) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayDuration);
                } catch (InterruptedException e) {
                }
                h.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 投出一个事件,将会匹配方法名称
     *
     * @param tagObjectFalg 需要匹配对象的标识
     * @param methodName    匹配的方法名
     * @param o             参数列表
     * @param delayDuration 延时多少时间执行
     */
    public void postEvent(Integer delayDuration, String tagObjectFalg, String methodName, Object... o) {
        eventTasks.add(new EventTask(EventTask.MATCH_TAG_METHODNAME, tagObjectFalg, methodName, o));
        sendMessage(delayDuration);
    }

    /**
     * 投出一个事件,将会匹配方法名称
     *
     * @param tagObjectFalg 需要匹配对象的标识
     * @param methodName    匹配的方法名
     * @param o             参数列表
     */
    public static void postEvent(String tagObjectFalg, final String methodName, final Object... o) {
        if (tagObjectFalg == null || "".equals(tagObjectFalg) || methodName == null || "".equals(methodName)) {
            return;
        }

        //拿到目标对象
        Object tagObject = map.get(tagObjectFalg);
        if (tagObject == null) {
            return;
        }

        //拿到Class类对象,为反射做准备
        Class<?> entityClass = tagObject.getClass();
        //获取到所有的方法
        Method[] methods = entityClass.getMethods();
        //循环
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            //获取到方法的名称
            String name = method.getName();

            //如果方法的名称是onEvent开头的
            if (name.startsWith(METHOD_PREFIX)) {
                boolean fitParamter = isFitParamter(method, o);
                if ((METHOD_PREFIX + StringUtil.firstCharToUpperCase(methodName)).equals(name) && fitParamter) {
                    try {
                        method.invoke(tagObject, o);
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.s(TAG, "EBus调用方法失败:" + name);
                    }
                }
            }
        }

    }

    /**
     * 投出一个事件,将会匹配方法名称
     *
     * @param methodName    所以注册的类中的方法名称匹配的,并且参数列表也匹配,才会调用
     * @param o             参数列表
     * @param delayDuration 延时
     */
    public void postEvent(final Integer delayDuration, final String methodName, final Object... o) {
        eventTasks.add(new EventTask(EventTask.MATCH_METHODNAME, methodName, o));
        sendMessage(delayDuration);
    }

    /**
     * 投出一个事件,将会匹配方法名称
     *
     * @param methodName 所以注册的类中的方法名称匹配的,并且参数列表也匹配,才会调用
     * @param o          参数列表
     */
    public static void postEvent(final String methodName, final Object... o) {
        if (methodName == null || "".equals(methodName)) {
            return;
        }
        //迭代map集合中的类
        ArrayUtil.iteratorMap(map, new ArrayUtil.MapIterator<String, Object>() {
            @Override
            public void iterator(Map.Entry<String, Object> entity) {
                postEvent(entity.getKey(), methodName, o);
            }
        });
    }


    /**
     * 投出一个事件,将会匹配方法名称
     *
     * @param o             参数列表
     * @param delayDuration 延时
     */
    public void postEvent(final Integer delayDuration, final Object... o) {
        eventTasks.add(new EventTask(EventTask.MATCH_ALL, o));
        sendMessage(delayDuration);
    }

    /**
     * 投出一个事件,自动匹配所有的注册者的方法,
     * 如果传递的是一个字符串,那么优先匹配该字符串和方法名
     *
     * @param o 事件传递的对象
     * @return
     */
    public static void postEvent(final Object... o) {
        if (o == null) {
            return;
        }
        //遍历所有的注册的实体对象,并便利每个对象中的方法
        ArrayUtil.iteratorMap(map, new ArrayUtil.MapIterator<String, Object>() {
            @Override
            public void iterator(Map.Entry<String, Object> entity) {
                Class<?> entityClass = entity.getValue().getClass();
                Method[] methods = entityClass.getMethods();
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    //获取到方法的名称
                    String name = method.getName();
                    //如果方法的名称是onEvent开头的
                    if (name.startsWith(METHOD_PREFIX)) {
                        boolean b = isFitParamter(method, o);
                        if (b) {
                            try {
                                method.invoke(entity.getValue(), o);
                            } catch (Exception e) {
                                L.s(TAG, "调用方法失败:" + name);
                            }
                        }
                    }
                }
            }
        });
        return;
    }

    /**
     * 判断方法m的参数是不是匹配o
     *
     * @param m 要判断的方法
     * @param o 方法是不是匹配后面的参数列表
     * @return
     */
    private static boolean isFitParamter(Method m, Object... o) {
        Class<?>[] parameterTypes = m.getParameterTypes();
        if (parameterTypes == null) {
            if (o == null) {
                return true;
            } else {
                return false;
            }
        } else {
            if (o == null) {
                return false;
            } else {
                if (o.length == parameterTypes.length) {
                    for (int i = 0; i < o.length; i++) {
                        Object p = o[i];
                        //如果其中有一个参数不是匹配的
                        if (p.getClass() != parameterTypes[i]) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

}

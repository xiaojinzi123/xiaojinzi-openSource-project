package xiaojinzi.EBus;

/**
 * Created by cxj on 2016/3/29.
 */
public class EventTask {

    /**
     * 匹配对象和方法和参数
     */
    public static final int MATCH_TAG_METHODNAME = 1;

    /**
     * 不匹配对象,匹配方法和参数
     */
    public static final int MATCH_METHODNAME = 2;

    /**
     * 不匹配对象,不匹配方法,只匹配参数
     */
    public static final int MATCH_ALL = 3;

    /**
     * 事件类型
     */
    public int eventType;

    /**
     * 目标对象的标识
     */
    public String tagObjectFalg;

    /**
     * 目标执行的方法
     */
    public String methodName;

    /**
     * 传递的数据
     */
    public Object o[];

    public EventTask(int eventType, Object[] o) {
        this.eventType = eventType;
        this.o = o;
    }

    public EventTask(int eventType, String methodName, Object[] o) {
        this.eventType = eventType;
        this.o = o;
        this.methodName = methodName;
    }

    public EventTask(int eventType, String tagObjectFalg, String methodName, Object[] o) {
        this.eventType = eventType;
        this.tagObjectFalg = tagObjectFalg;
        this.methodName = methodName;
        this.o = o;
    }
}

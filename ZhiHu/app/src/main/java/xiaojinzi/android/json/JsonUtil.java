package xiaojinzi.android.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import xiaojinzi.android.util.log.L;
import xiaojinzi.java.reflection.ReflectionUtil;
import xiaojinzi.java.reflection.ReflectionUtil.HanderFieldListener;

/**
 * json解析的工具类<br>
 * 原理分析:<br>
 * 1.拿到一个对象,迭代对象中所有属性,然后进行根据属性的名称去json中拿到对应的<br>
 * 字符串做相应的转化,如果是集合对象,利用第二点<br>
 * 2.如果是一个集合对象,Collection接口下面的都可以,拿到集合中的泛型的类型,<br>
 * 帮助用户初始化对象,然后利用第一点进行属性的填充
 *
 * @author cxj <br>
 *         QQ:347837667
 */
public class JsonUtil {

    public static final String TAG = "xiaojinzi.android.util.JsonUtil";

    private static final boolean isPrintJsonLog = false;

    /**
     * 对一个集合进行json数据的填充,只有支持Collection接口的,实现类都可以填充,比如: <br>
     * List<Student> list = new ArrayList <Student>();<br>
     * JsonUtil.parseArrayFromJson(list,Student.class,
     * "[{id:1,name:"cxj",age:12},{id:1,name:"cxj",age:12}]")
     * System.out.print(list); <br>
     * 这样子打印就能看到json数据都填充到list集合里面去了,<br>
     * 当然List里面的泛型也可以指定包装类和字符串,也就是List<String>,List<Integer>....都是可以映射的
     *
     * @param collection
     * @param classEntity
     * @param json
     * @throws Exception
     */
    public static void parseArrayFromJson(Collection<?> collection, Class<?> classEntity, String json)
            throws Exception {

        JSONArray array = new JSONArray(json);

        // 从实现类中获取add方法
        Method method = collection.getClass().getMethod("add", Object.class);

        // 如果这个类型是基本数据类型或者字符串
        if (ReflectionUtil.isBaseClassType(classEntity)) {
            // 拿到构造器是字符串的构造函数
            Constructor<?> constructor = classEntity.getConstructor(String.class);
            Object entity = null;
            for (int i = 0; i < array.length(); i++) {
                String value = array.getString(i);
                if (classEntity == char.class || classEntity == Character.class) {
                    entity = Character.valueOf("".equals(value) ? ' ' : value.charAt(0));
                } else {
                    // 创建泛型执行的对象
                    entity = constructor.newInstance(value);
                }
                method.invoke(collection, entity);
            }

        } else { // 如果不是基本数据类型
            for (int i = 0; i < array.length(); i++) {
                Object entity = classEntity.newInstance();
                parseObjectFromJson(entity, array.getString(i));
                method.invoke(collection, entity);
            }
        }
    }

    /**
     * 针对对象中一个Collection接口的字段进行json数据的填充
     *
     * @param f          实体字段
     * @param collection 集合接口
     * @param json       json字符串
     * @throws Exception 抛出的异常
     */
    private static void parseArrayFromJson(final Field f, final Collection<?> collection, String json)
            throws Exception {
        if (isPrintJsonLog)
            L.s(TAG, "开始注入集合属性" + collection.getClass().getName() + ":" + f.getName() + "\n" + json);

        // 拿到List集合的里面使用的泛型
        Class<?>[] classes = null;

        try {
            classes = ReflectionUtil.getParameterizedType(f);
        } catch (Exception e) {
            if (collectionGenericParadigmListener != null) {
                classes = collectionGenericParadigmListener.getCollectionGenericParadigm(f);
            }
        }

        // 拿到List集合的里面使用的泛型
//		Class<?>[] classes = ReflectionUtil.getParameterizedType(f);

        if (classes != null) { // 如果有泛型

            // 拿到泛型的对象的Class对象,也就是知道了泛型规定集合中存放的数据的类型
            Class<?> classEntity = classes[0];

            parseArrayFromJson(collection, classEntity, json);

        } else {
            if (isPrintJsonLog)
                L.s(TAG, "您的集合没有使用泛型,小金子无法得知您需要的类型！您的集合请带上泛型参数");
        }

    }

    /**
     * 对一个json数据完成和实体对象之间的映射,<br>
     * 没有返回值,就是对一个对象中的属性进行json数据的填充 用法:<br>
     * Student s = new Student();<br>
     * JsonUtil.parseObjectFromJson(s,"{id:1,name:"cxj",age:12}");<br>
     * System.out.print(s);<br>
     * 打印就可以看到json数据都映射到了实体对象Student中
     *
     * @param jsonEntity 实体对象
     * @param json       json数据
     * @throws JSONException 抛出的异常
     */
    public static void parseObjectFromJson(final Object jsonEntity, String json) throws JSONException {

        if (isPrintJsonLog)
            L.s(TAG, jsonEntity.getClass().getName() + "对象开始json注入");

        final JSONObject jsonObject = new JSONObject(json);

        // 获取实体对象的Class对象
        Class<? extends Object> c = jsonEntity.getClass();

        // 对对象中的字段进行迭代
        ReflectionUtil.fieldIterator(c, new HanderFieldListener() {

            @SuppressWarnings("rawtypes")
            @Override
            public void handerField(Field f, String fieldName, Class<?> clazz) throws Exception {

                // 先以字符串的形式获取字段名称的对应的json字符串,比如:{"id",1}
                String value = jsonObject.getString(fieldName);

                if (isPrintJsonLog)
                    L.s(TAG, "开始尝试注入八种基本类型属性和字符串:" + fieldName);

                // 尝试注入基本数据类型或者字符串
                boolean isSuccess = ReflectionUtil.setValue(f, jsonEntity, value);

                if (!isSuccess) { // 如果不成功,说明这个字段并不是普通的字段,是一个对象

                    if (isPrintJsonLog)
                        L.s(TAG, "不是基本属性,已经开始尝试集合属性和其他属性");

                    // 获取到对象jsonEntity中的f字段的对应的数据
                    Object fieldValue = f.get(jsonEntity);

                    // 如果这个属性是空的情况下,应该抛出异常,但是这里为了方便,做一下方便用户的事情,帮助用户初始化
                    if (fieldValue == null) {

                        if (isPrintJsonLog)
                            L.s(TAG, "对象" + jsonEntity.getClass().getName() + "中属性'" + f.getName() + "为空");

                        // 如果是List接口类型的
                        if ((f.getType() == List.class)) {
                            fieldValue = new ArrayList();

                            if (isPrintJsonLog)
                                L.s(TAG, "为空属性'" + f.getName() + "'List接口自动创建对象ArrayList对象成功");

                        } else if ((f.getType() == Set.class)) {
                            fieldValue = new HashSet();

                            if (isPrintJsonLog)
                                L.s(TAG, "为空属性'" + f.getName() + "'Set接口自动创建对象HashSet对象成功");

                        } else { // 如果是其他对象,尝试利用默认构造函数创建对象
                            fieldValue = f.getType().newInstance();
                            if (isPrintJsonLog)
                                L.s(TAG, "为空属性'" + f.getName() + "' 创建对象成功");
                        }
                        f.set(jsonEntity, fieldValue);
                    }

                    if (fieldValue instanceof Collection) { // 如果这个对象是一个集合类的对象
                        parseArrayFromJson(f, (Collection<?>) fieldValue, value);
                    } else { // 如果是其他实体对象
                        // 如果不是能判断的对象,则递归注入
                        parseObjectFromJson(fieldValue, value);
                    }
                }
            }

        });
    }

    /**
     * 传入一个实体的Class,比如要对Student实体进行映射,<br>
     * 可以直接传入一个Student.class,便可得到一个Student对象,用法:<br>
     * Student s =
     * JsonUtil.parseObjectFromJson(Student.class,"{id:1,name:"cxj",age:12}")
     *
     * @param clazz 实体的Class
     * @param json  要映射的json数据
     * @return
     * @throws InstantiationException 创建对象失败
     * @throws IllegalAccessException
     * @throws JSONException          json转化异常
     */
    public static <K> K createObjectFromJson(Class<K> clazz, String json)
            throws InstantiationException, IllegalAccessException, JSONException {
        K newInstance = clazz.newInstance();
        parseObjectFromJson(newInstance, json);
        return newInstance;
    }

    /**
     * 在注入数据的时候,有的时候,一些类中集合中写的类型是泛型,这就要用到这个接口,让用户返回类型
     */
    public interface CollectionGenericParadigmListener {
        Class<?>[] getCollectionGenericParadigm(Field f);
    }

    private static CollectionGenericParadigmListener collectionGenericParadigmListener;

    public static void setCollectionGenericParadigmListener(CollectionGenericParadigmListener collectionGenericParadigmListener) {
        JsonUtil.collectionGenericParadigmListener = collectionGenericParadigmListener;
    }
}

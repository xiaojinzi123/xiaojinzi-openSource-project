package xiaojinzi.android.util.message;

public interface MessageHandle {

    /**
     * 可以传递一个消息过来
     *
     * @param message
     */
    public Object messageHandler(String message, Object data);

}

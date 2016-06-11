package xiaojinzi.net.filter;

import android.app.ProgressDialog;

import xiaojinzi.base.java.net.HttpRequest;

/**
 * Created by cxj on 2016/5/30.
 */
public class PdHttpRequest<Parameter> extends HttpRequest<Parameter> {

    /**
     * 进度条控件
     */
    private ProgressDialog pd;

    public ProgressDialog getPd() {
        return pd;
    }

    public void setPd(ProgressDialog pd) {
        this.pd = pd;
    }

    public PdHttpRequest() {
    }

    public PdHttpRequest(String requesutUrl) {
        super(requesutUrl);
    }

    public PdHttpRequest(String requesutUrl, int responseDataStyle) {
        super(requesutUrl, responseDataStyle);
    }
}

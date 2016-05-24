package com.example.cxj.zhihu.config;

/**
 * Created by cxj on 2016/1/18.
 * 知乎日报的常量类
 */
public class Constant {

    /**
     * 关于EBus的标识
     */
    public class eBus {

        /**
         * 详情fragment的
         */
        public class detailFragment {
            /**
             * 改变标题栏透明度的标识
             */
            public static final String CHANGETITLEBAROPACITY_FLAG = "changeTitleBarOpacity";

            /**
             * 分享网址的标识
             */
            public static final String SHAERURL_FLAG = "shareUrl";

        }

        /**
         * 我的收藏界面的
         */
        public class myCollectionAct {

            /**
             * 取消收藏的EBus的标识
             */
            public static final String CANCELCOLLECT_FLAG = "cancelCollect";

            /**
             * 刷新收藏列表的eBus标识
             */
            public static final String FRESHCOLLECTDATA_FLAG = "freshCollectData";
        }

        /**
         * 在线皮肤的界面
         */
        public class myOnlineSkinAct {
            /**
             * 改变标题栏的文本
             */
            public static final String CHANGETITLE_FLAG = "changeTitle";
        }

    }

    /**
     * 故事的标识
     */
    public static final String STORY_FLAG = "storyId";

    /**
     * 请求缓存故事
     */
    public static final String REQUESTCOLLECTSTORY_FLAG = "requestCollectStory";

    /**
     * 弹出菜单标志
     */
    public static final String POPUPMENU_FLAG = "popupMenu";

    /**
     * 菜单被打开的标识
     */
    public static final String MENUOPENED_FLAG = "menuOpened";

    /**
     * 菜单被关闭的标志
     */
    public static final String MENUCLOSED_FLAG = "menuClosed";

    /**
     * 转换模式的标识
     */
    public static final String CHANGEMODE_FLAG = "changeMode";

    /**
     * 改变收藏图标的标识
     */
    public static final String CHANGECOLLECTICON_FLAG = "changeCollectIcon";

    /**
     * 只在wifi下加载
     */
    public static final int loadImageWithWifi = 1;

    /**
     * 所有情况都可以加载
     */
    public static final int loadImageWithAll = 2;


    /**
     * 有关Url常量的
     */
    public class Url {

        /**
         * 基本的路径
         */
        public static final String baseUrl = "http://42.96.134.141:8080/zhihuSkin";


        /**
         * 欢迎界面的url常量
         */
        public class WelcomeAct {

            /**
             * 启动图片的获取
             */
            public static final String startImage = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

        }

        /**
         * 主界面
         */
        public class MainAct {
            /**
             * 左边菜单中的主题日报的信息的获取
             */
            public static final String themesListUrl = "http://news-at.zhihu.com/api/4/themes";

            /**
             * 获取对应的主题日报,获取的时候需要加上对应的主题的id
             */
            public static final String themesUrl = "http://news-at.zhihu.com/api/4/theme/";

            /**
             * 最新消息的url,用户获取最新的内容
             */
            public static final String latestUrl = "http://news-at.zhihu.com/api/4/news/latest";

            /**
             * 过往消息的url,get的时候需要格外添加上时间参数
             * 20160122  --> 表示2016年1月22日之前的故事内容,也就是2016年1月21日的内容
             */
            public static final String beforeNewsUrl = "http://news.at.zhihu.com/api/4/news/before/";
        }

        /**
         * 详情的界面的常量
         */
        public class detailAct {
            /**
             * 故事详情的获取,需要跟上一个故事的id
             * +3892357
             */
            public static final String detailStoryUrl = "http://news-at.zhihu.com/api/4/news/";

            /**
             * 故事详情的额外信息,比如评论的个数等等,获取的时候需要在后面加上故事的id
             * +3892357
             */
            public static final String extraStoryInfoUrl = "http://news-at.zhihu.com/api/4/story-extra/";
        }

        /**
         * 评论的activity
         */
        public class commentAct {

            /**
             * 前缀加上id的值,然后跟上后缀
             */
            public static final String commentUrlPrefix = "http://news-at.zhihu.com/api/4/story/";

            /**
             * 长评的后缀
             */
            public static final String longCommentUrlSubfix = "/long-comments";

            /**
             * 短评的后缀
             */
            public static final String shortCommentUrlSubfix = "/short-comments";

        }

        /**
         * 选择皮肤的界面
         */
        public class skinAct {

            /**
             * 获取皮肤数据的接口
             */
            public static final String skinListUrl = baseUrl + "/back/skin/list?isReturnJson=true";

        }

    }

    /**
     * 有关轻量级储存的常量
     */
    public class SP {

        /**
         * 公用的
         */
        public class common {
            /**
             * 显示的模式
             * 1.日间模式 0
             * 2.夜晚模式 1
             */
            public static final String showMode = "showMode";

            /**
             * 是否wifi可用
             */
            public static final String isWifiEable = "isWifiEable";
        }

        /**
         * 关于设置界面的
         */
        public class settingAct {

            /**
             * 是否自动更换皮肤
             */
            public static final String isAutoChangeSkin = "isAutoChangeSkin";

            /**
             * 加载图片的模式
             */
            public static final String loadImageMode = "loadImageMode";

        }

        /**
         * 有关主页界面的
         */
        public class mainAct {
            /**
             * 主页界面是否提示了
             */
            public static final String isShowMainTip = "isShowMainTip";

        }

        /**
         * 有关详情界面的
         */
        public class detailAct {
            /**
             * 详情界面是否提示了
             */
            public static final String isShowDetailTip = "isShowDetailTip";
        }

        /**
         * 我的皮肤界面的
         */
        public class mySkinAct {
            /**
             * 正在使用的日间皮肤的id
             */
            public static final String usedDaySkinId = "usedDaySkinId";

            /**
             * 正在使用的夜间皮肤的id
             */
            public static final String usedNightSkinId = "usedDaySkinId";
        }


    }

}

package com.gd.form.net;


/**
 * <p>类描述：APP接口路径配置常量类
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public class UrlConstant {
    /**
     * 单例模式
     * @return 单例模式下的AppConstant
     */
    public static UrlConstant getInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 静态内部类
     * <p>和在getInstance方法上加同步或者双重检查锁定相比
     * <p>实现了线程安全，又避免了同步带来的性能影响
     */
    private static class LazyHolder{
        private static final UrlConstant INSTANCE=new UrlConstant();
    }

    /**
     * 拼接请求服务器地址的方法
     * @return 拼接后完整的访问路径
     */
    public String getServerUrl() {
        return  "http://49.233.51.19:8080/Spring/";
    }

}

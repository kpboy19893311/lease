package com.lyc.lease.common.login;

/**
 * @ClassName LoginUserHolder
 * @Description TODO ThreadLocal工具类
 * @Author fsh
 * @Date 2025/2/9 16:40
 * @Version 1.0
 */
public class LoginUserHolder {
    public static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        threadLocal.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}

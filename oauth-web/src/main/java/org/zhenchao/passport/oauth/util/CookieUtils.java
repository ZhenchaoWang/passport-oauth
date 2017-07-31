package org.zhenchao.passport.oauth.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Cookie工具类
 *
 * @author zhenchao.wang 2017-01-02 16:01
 * @version 1.0.0
 */
public class CookieUtils {

    /**
     * 从cookie中获取指定值
     *
     * @param request
     * @param key
     * @return
     */
    public static String get(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if(ArrayUtils.isEmpty(cookies)) {
            return StringUtils.EMPTY;
        }
        for (final Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), key)) {
                return cookie.getValue();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 创建一个新的cookie
     *
     * @param key
     * @param value
     * @return
     */
    public static Cookie create(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 3600);
        return cookie;
    }

}

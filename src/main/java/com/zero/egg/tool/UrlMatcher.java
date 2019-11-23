package com.zero.egg.tool;

/**
 * @ClassName UrlMatcher
 * @Description URL匹配接口
 * @Author lyming
 * @Date 2019/9/21 5:16 下午
 **/
public interface UrlMatcher {
    Object compile(String paramString);
    boolean pathMatchesUrl(Object paramObject, String paramString);
    String getUniversalMatchPattern();
    boolean requiresLowerCaseUrl();
}

package com.vic.enums;

/**
 * 缓存key
 * @author 罗利华
 * date: 2020/5/14 14:35
 */
public enum CacheKey {

    LOAN_CREDIT_REPEAT_SUBMIT("X50:LOAN:CREDIT:%s:%s-%s"),
    ;

    CacheKey(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return key;
    }

    /**
     * 根据keys数组逐个替换KEY中%s生成KEY
     * @param keys
     * @return
     */
    public String getKey(String... keys) {
        return String.format(key, keys);
    }

}

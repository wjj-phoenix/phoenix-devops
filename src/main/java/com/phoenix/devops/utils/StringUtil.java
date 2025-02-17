package com.phoenix.devops.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;

/**
 * @author wjj-phoenix
 * @since 2025-02-17
 */
public final class StringUtil {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!CharUtil.isBlankChar(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static String removeAll(CharSequence str, CharSequence strToRemove) {
        return isNotEmpty(str) && isNotEmpty(strToRemove) ? str.toString().replace(strToRemove, "") : str(str);
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStr(str, testStrs);
    }

    public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
        if (isNotEmpty(str) && !ArrayUtil.isEmpty(testStrs)) {
            for (CharSequence checkStr : testStrs) {
                if (null != checkStr && str.toString().contains(checkStr)) {
                    return checkStr.toString();
                }
            }
            return null;
        } else {
            return null;
        }
    }
}

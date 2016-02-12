package im.nll.data.spider.utils;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/12 下午5:04
 */
public class StringUtils {
    public static boolean isNullOrEmpty(String content) {
        return content == null || content.length() == 0
                || "null".equals(content);
    }

    public static boolean isNotNullOrEmpty(String content) {
        return (content != null) && (content.length() > 0);
    }

}

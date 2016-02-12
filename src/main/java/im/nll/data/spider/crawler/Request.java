package im.nll.data.spider.crawler;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import im.nll.data.spider.utils.Logs;
import im.nll.data.spider.utils.StringUtils;
import org.slf4j.Logger;

import java.util.Map;

/**
 * 封装的一个request请求.
 * <p/>
 * 包括url,ua,charset,timeout,header,cookie,param.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.3
 * @date 2013-7-16下午1:06:30
 */
public class Request {
    private static final Logger LOGGER = Logs.get();

    /**
     * default http request content-type
     */
    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";


    /**
     * 默认超时时间.
     */
    private int timeout = 120000;
    /**
     * 请求地址.
     */
    private String url;
    /**
     * 编码.
     */
    private String charset;

    /**
     * method default get,support GET and POST.
     */
    private String method = "GET";

    /**
     * UA.
     */
    private String userAgent;

    /**
     * cookie.
     */
    private String cookie;

    /**
     * http proxy. eg: username:password@192.168.100.100:80 use Proxy.parse(proxyString)
     */
    private Proxy proxy;

    /**
     * http post payload
     */
    private String payload;

    /**
     * http headers.
     */
    private final Map<String, String> headers = new CaseInsensitiveHashMap();

    /**
     * http post data.
     */
    private final Map<String, String> params = new CaseInsensitiveHashMap();

    public Request() {
    }

    /**
     * 创建一个request对象.
     *
     * @param url 请求地址.
     */
    public Request(String url) {
        this.url = url;
    }

    /**
     * 设置url.
     *
     * @param url
     * @return
     */
    public Request url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 返回url.
     *
     * @return
     */
    public String url() {
        return this.url;
    }

    /**
     * 设置method.
     *
     * @param method
     * @return
     */
    public Request method(String method) {
        this.method = method;
        return this;
    }

    /**
     * 返回 method.
     *
     * @return
     */
    public String method() {
        return this.method;
    }

    /**
     * 设置timeout.
     *
     * @param timeout
     * @return
     */
    public Request timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 返回timeout.
     *
     * @return
     */
    public int timeout() {
        return this.timeout;
    }

    /**
     * 设置charset.
     *
     * @param charset
     * @return
     */
    public Request charset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 返回charset.
     *
     * @return
     */
    public String charset() {
        return this.charset;
    }

    /**
     * 设置userAgent.
     *
     * @param userAgent
     * @return
     */
    public Request userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * 返回userAgent.
     *
     * @return
     */
    public String userAgent() {
        return this.userAgent;
    }

    /**
     * 添加一个header.
     *
     * @param name
     * @param value
     * @return
     */
    public Request header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * 符合 http协议规范的 header 串. 一行一条
     *
     * @param rawHeaders
     * @return
     */
    public Request header(String rawHeaders) {
        try {
            Map<String, String> headers = readHeaders(rawHeaders);
            this.headers(headers);
        } catch (Exception e) {
            LOGGER.warn("{}", e);
        }
        return this;
    }

    /**
     * 添加多个header.
     *
     * @param headers
     * @return
     */
    public Request headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * 返回headers.
     *
     * @return
     */
    public Map<String, String> headers() {
        return this.headers;
    }

    /**
     * 设置cookie.
     *
     * @param cookie
     * @return
     */
    public Request cookie(String cookie) {
        this.cookie = cookie;
        return this;
    }


    /**
     * set request proxy.
     *
     * @param proxy
     * @return
     */
    public Request proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    /**
     * set request proxy use string like 'username:password@192.168.100.100:80' use Proxy.parse(proxyString) to parse.
     *
     * @param proxy
     * @return
     */
    public Request proxy(String proxy) {
        this.proxy = Proxy.parse(proxy);
        return this;
    }

    /**
     * return proxy.
     *
     * @return
     */
    public Proxy proxy() {
        return this.proxy;
    }

    /**
     * 获取 payload.
     *
     * @return
     */
    public String payload() {
        return this.payload;
    }

    /**
     * 设置 payload，当payload 不为空的时候，只使用 payload，不使用 params。为空则只使用 params
     *
     * @param payload
     * @return
     */
    public Request payload(String payload) {
        this.payload = payload;
        return this;
    }

    /**
     * 返回cookie.
     *
     * @return
     */
    public String cookie() {
        return this.cookie;
    }

    /**
     * 添加一个param.
     * 当payload 不为空的时候，只使用 payload，不使用 params。为空则只使用 params
     *
     * @param name
     * @param value
     * @return
     */
    public Request param(String name, String value) {
        this.params.put(name, value);
        return this;
    }

    /**
     * 添加多个param.
     *
     * @param params
     * @return
     */
    public Request params(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    /**
     * 返回params.
     *
     * @return
     */
    public Map<String, String> params() {
        return this.params;
    }

    /**
     * 返回request对象.
     *
     * @return
     */
    public static Request newReq() {
        return new Request();
    }

    /**
     * 返回request对象.
     *
     * @return
     */
    public static Request newReq(String url) {
        return new Request(url);
    }

    private static Map<String, String> readHeaders(String rawHeader) throws Exception {
        if (StringUtils.isNullOrEmpty(rawHeader)) {
            return Maps.newHashMap();
        }
        Map<String, String> headers = Maps.newHashMap();
        Iterable<String> lines = Splitter.on("\n")
                .trimResults()
                .omitEmptyStrings().split(rawHeader);
        String name = null;
        StringBuffer value = null;
        for (String line : lines) {

            // Otherwise we should have normal HTTP header line
            // Parse the header name and value
            int colon = line.indexOf(":");
            if (colon < 0) {
                throw new Exception("Unable to parse header: " + line);
            }
            name = line.substring(0, colon).trim();
            value = new StringBuffer(line.substring(colon + 1).trim());
            if (name != null) {
                headers.put(name, value.toString());
            }
        }
        return headers;
    }
}
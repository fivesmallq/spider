package im.nll.data.spider.module.crawler;

import com.google.common.base.Splitter;
import im.nll.data.spider.utils.Logs;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * Parses a given proxy in form [username[:password]@](ip|hostname)[:port]
 *
 * @author daniel
 */
public class CommonProxyParser {

    private static final Logger LOG = Logs.get();

    // private static final Pattern PROXY_PATTERN =
    // Pattern.compile("^([\\w+-]+(:[^@:]+)?@)?[0-9.]+(:[0-9]+([0-9]+)*)?$");

    public Proxy parse(String proxy) {
        /* better approach for ips as well as hostnames */
        try {
            URI uri = new URI("http", "//" + proxy, null);
            URL url = uri.toURL();
            String host = url.getHost();
            int portUrl = url.getPort();
            int port = (portUrl < 1) || (portUrl > 65535) ? Proxy.DEFAULT_PORT
                    : portUrl;
            String userInfo = url.getUserInfo();
            String username = null;
            String password = null;
            if (isNotBlank(userInfo)) {
                List<String> splits = Splitter.on(":").trimResults()
                        .splitToList(userInfo);
                if (splits.size() > 2) {
                    throw new MalformedURLException(
                            "URL Authentication (UserInfo) invalid");
                }
                if (splits.size() >= 1) {
                    username = trimToNull(splits.get(0));
                    if (splits.size() == 2) {
                        password = trimToNull(splits.get(1));
                    }
                }
            }
            return new Proxy(host, port, username, password);
        } catch (URISyntaxException | MalformedURLException ex) {
            LOG.info("Invalid proxy given: {}", proxy);
        }
        return null;
    }
}

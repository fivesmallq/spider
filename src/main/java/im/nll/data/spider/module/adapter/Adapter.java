package im.nll.data.spider.module.adapter;

import im.nll.data.spider.module.extractor.Result;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/13 下午5:28
 */
public interface Adapter {
    Object to(Result result);
}

package crawler.taobaoke;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import java.net.URLEncoder;

/**
 * Created by li3-mac on 2017/11/23.
 */
public class PostCrawlerByCookie implements PageProcessor {

    protected static String cookie = "csrftoken=nD01nDLVARb8I0ag6zJePkYUJ0WRBHmnwn9LzpgrCq1fFHurT6Bqtn64pNjK64Kc; sessionid=jyq5449t7zxk7s9eoq60g8qpx74rj46b";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        page.putField("data", page.getJson().jsonPath("data.changed_msg").toString());
    }

    @Override
    public Site getSite() {
        site.addHeader("Accept",
                "application/json, text/javascript, */*; q=0.01");
        site.addHeader("Accept-Encoding", "gzip, deflate, br");
        site.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        site.addHeader("Connection", "keep-alive");
        site.addHeader("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        site.addHeader("Cookie", cookie);
        site.addHeader("Host", "www.taokelu.com");
        site.addHeader("Referer", "https://www.taokelu.com/");
        site.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:52.0) Gecko/20100101 Firefox/52.0");
        site.addHeader("X-Requested-With", "XMLHttpRequest");
        return site;
    }

    public static void main(String[] args) {
        //测试代码,需要手动写入cookie,请使用Boot
        //产品url
        String taobaourl = "https://item.taobao.com/item.htm?spm=a21cx.20121.26015.1.3226560e0aKenI&id=536568339394";
        String apiurl = "https://www.taokelu.com/user/tool/post_batch_change_token";

        DataPipeline dataPipeline = new DataPipeline();
        Request request = new Request(apiurl);
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.custom(("raw_message=" + URLEncoder.encode(taobaourl)).getBytes(), "application/x-www-form-urlencoded", "utf-8"));
        Spider.create(new PostCrawlerByCookie()).addRequest(request).addPipeline(dataPipeline).run();
        System.out.println(dataPipeline.getData());
    }
}

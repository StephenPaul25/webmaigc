package crawler.taobaoke;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.PlainText;

/**
 * Created by li3-mac on 2017/11/23.
 */
public class PostCrawlerByLogin implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000);

    @Override
    public void process(Page page) {
        page.putField("csrftoken",page.getHtml().regex("csrftoken=(.*?);",1));
        page.putField("sessionid",page.getHtml().regex("sessionid=(.*?);",1));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        //测试代码,取到cookie,请使用Boot
        String loginurl = "https://www.taokelu.com/login?next=/user/tool/batch_change_token";

        System.setProperty("selenuim_config", "./src/main/resouces/config.ini");
        SeleniumHtmlUnitDownloader downloader = new SeleniumHtmlUnitDownloader();

        Spider spider = Spider.create(new PostCrawlerByLogin()).setDownloader(downloader);
        ResultItems resultItems = spider.get(loginurl);
        String csrftoken = ((PlainText) resultItems.getAll().get("csrftoken")).get();
        String sessionid = ((PlainText) resultItems.getAll().get("sessionid")).get();
        System.out.println(csrftoken);
        System.out.println(sessionid);
        spider.close();
    }
}

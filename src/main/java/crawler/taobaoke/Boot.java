package crawler.taobaoke;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;

import java.net.URLEncoder;

/**
 * Created by li3-mac on 2017/11/23.
 */
public class Boot {

    public static void main(String[] args) {
        //商品的url
        String taobaourl = "https://item.taobao.com/item.htm?spm=a21cx.20121.26015.1.3226560e0aKenI&id=536568339394";

        //登陆url
        String loginurl = "https://www.taokelu.com/login?next=/user/tool/batch_change_token";
        //apiurl
        String apiurl = "https://www.taokelu.com/user/tool/post_batch_change_token";
        //以下三个xpath分别是账户名密码和登陆按钮所在地址
        String userXpath = "//input[@id='phone']";
        String passXpath = "//input[@id='password']";
        String loginXpath = "//button[@id='btnLogin']";
        //登陆的用户名密码
        String userName = "15578290423";
        String passWord = "qq123456";

        //System.setProperty("selenuim_config", "./src/main/resouces/config.ini");
        //1:首先要登陆,采用selenium的HtmlUnitDriver
        SeleniumHtmlUnitDownloader downloader = new SeleniumHtmlUnitDownloader(userXpath, passXpath, loginXpath, userName, passWord);
        //采用webmagic进行访问并获得两个cookie里的key
        Spider loginSpider = Spider.create(new PostCrawlerByLogin()).setDownloader(downloader);
        ResultItems resultItems = loginSpider.get(loginurl);
        String csrftoken = ((PlainText) resultItems.getAll().get("csrftoken")).get();
        String sessionid = ((PlainText) resultItems.getAll().get("sessionid")).get();
        loginSpider.close();

        //2:访问转换的url,得到转换后的链接,采用post方式
        PostCrawlerByCookie.cookie = "csrftoken=" + csrftoken + ";" + "sessionid=" + sessionid;
        DataPipeline dataPipeline = new DataPipeline();
        Request request = new Request(apiurl);
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.custom(("raw_message=" + URLEncoder.encode(taobaourl)).getBytes(), "application/x-www-form-urlencoded", "utf-8"));
        //同样采用webmagic
        Spider.create(new PostCrawlerByCookie()).addRequest(request).addPipeline(dataPipeline).run();
        //得到了最终url结果
        String tranurl = dataPipeline.getData();
        System.out.println(tranurl);
    }
}

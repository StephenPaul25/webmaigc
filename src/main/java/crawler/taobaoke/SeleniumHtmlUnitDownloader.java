package crawler.taobaoke;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

/**
 * Created by li3-mac on 2017/11/23.
 */
public class SeleniumHtmlUnitDownloader implements Downloader {

    private long sleepTime = 1000L;
    private String userXpath = "//input[@id='phone']";
    private String passXpath = "//input[@id='password']";
    private String loginXpath = "//button[@id='btnLogin']";
    private String userName = "15578290423";
    private String passWord = "qq123456";

    public SeleniumHtmlUnitDownloader(){};
    public SeleniumHtmlUnitDownloader(String userXpath, String passXpath, String loginXpath, String userName, String passWord) {
        this.userXpath = userXpath;
        this.passXpath = passXpath;
        this.loginXpath = loginXpath;
        this.userName = userName;
        this.passWord = passWord;
    }

    public Page download(Request request, Task task) {

        WebDriver webDriver = new HtmlUnitDriver();

        webDriver.get(request.getUrl());

        try {
            Thread.sleep(this.sleepTime);
        } catch (InterruptedException var9) {
            var9.printStackTrace();
        }

        webDriver.findElement(By.xpath(userXpath)).sendKeys(userName);
        webDriver.findElement(By.xpath(passXpath)).sendKeys(passWord);
        webDriver.findElement(By.xpath(loginXpath)).click();

        try {
            Thread.sleep(this.sleepTime);
        } catch (InterruptedException var9) {
            var9.printStackTrace();
        }

        String cookie = webDriver.manage().getCookies().toString();

        //WebElement webElement = webDriver.findElement(By.xpath("/html"));
        //String content = webElement.getAttribute("outerHTML");
        Page page = new Page();
        page.setRawText(cookie);
        page.setHtml(new Html(cookie, request.getUrl()));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }

    public void setThread(int thread) {}

}

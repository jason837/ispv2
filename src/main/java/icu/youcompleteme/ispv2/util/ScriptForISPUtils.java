package icu.youcompleteme.ispv2.util;

import icu.youcompleteme.ispv2.entity.IspUser;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * projectName : LSP
 * className: ScriptForISP
 * description: 为了学校的isp实现自动打卡，写个定时脚本帮助用户实现其功能。
 *
 * @author : git.jas0nch
 * date: 2022-02-05
 */
public class ScriptForISPUtils {

    private final String ID = "201910414303";

    private final String VPN_PWD = "cjwy837..";
    // isp
    private final String ISP_PWD = "037315";

    // 省市区
    private String PROVINCE = "四川省";
    private String CITY = "泸州市";
    private String AREA = "合江县";

    @Test
    public void func() throws InterruptedException {
        try {
            scriptForISP();
        } catch (UnhandledAlertException e) {
            System.out.println("您已经登录过了哦~~~");
        }
    }

    // the script method
    public void scriptForISP() throws InterruptedException {
        WebDriver webDriver = null;
        try {
            // windows, D:\JavaProjectLearning\LSP\src\main\resources\
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//            ChromeOptions chromeOptions=new ChromeOptions();
//            chromeOptions.setHeadless(Boolean.TRUE);
//            chromeOptions.addArguments("--no-sandbox","--disable-dev-shm-usage","window-size=1920x3000","--disable-gpu","--hide-scrollbars","blink-settings=imagesEnabled=false","--headless");
            webDriver = new ChromeDriver();
//            //启动一个 chrome 实例
//            // linux
//            System.setProperty("webdriver.chrome.driver", "/usr/lib64/chromium-browser/chromedriver");
//
//            webDriver = new ChromeDriver(chromeOptions);

            /*
             * @description : 打开isp地址，进行登录操作
             * @param
             * @return : void
             * @author : git.jas0nch
             * date : 2022/2/5
             **/
            webDriver.get("http://xsswzx-cdu-edu-cn-s.vpn.cdu.edu.cn:8118/ispstu/com_user/weblogin.asp");
            // get the vpn permission
            Thread.sleep(2000);
            webDriver.findElement(By.className("input-txt")).sendKeys(ID);
            webDriver.findElement(By.xpath(
                    "/html/body/div[2]/div[1]/div/div[3]/div/div[2]/div[1]/div/div/div[2]/form/div[1]/div[2]/div[1]/div/div[1]/input"
            )).sendKeys(VPN_PWD);
            //
            Thread.sleep(2000);
            webDriver.findElement(By.xpath(
                    "//*[@id=\"Calc\"]/div[3]/span"
            )).click();
            // submit
            Thread.sleep(2000);
            webDriver.findElement(By.xpath(
                    "/html/body/div[2]/div[1]/div/div[3]/div/div[2]/div[1]/div/div/div[2]/form/div[4]/button"
            )).click();
            Thread.sleep(20000);

            // get the validCode:
            String validCode = webDriver.findElement(By.xpath("//*[@id=\"form\"]/div[3]")).getText().substring(2);
            // login here:
            webDriver.findElement(By.id("username")).sendKeys(ID);
            Thread.sleep(1000);
            webDriver.findElement(By.id("userpwd")).sendKeys(ISP_PWD);
            Thread.sleep(1000);
            webDriver.findElement(By.id("code")).sendKeys(validCode);
            Thread.sleep(1000);
            // submit:
            webDriver.findElement(By.id("提交")).click();
            Thread.sleep(10000);
            // after login, to find the isp register, and to submit the information;
            webDriver.get("http://xsswzx-cdu-edu-cn-s.vpn.cdu.edu.cn:8118/ispstu/com_user/projecthealth.asp?id=d3c87b7b13b404fe5c12cad9a638208286065a4ebc69");
            Thread.sleep(1000);
//            // //*[@id="t"]/tbody/tr[6]/td/a[1]: then 点击常规登记（因为每隔5天要重置信息，所以直接常规登记。。。暂时）
            webDriver.findElement(By.xpath("/html/body/div/div[1]/div/div/table/tbody/tr[2]/td/a[1]")).click();
            Thread.sleep(1000);
            webDriver.manage().window().maximize();
            // select tag: 这里不用select选项和option进行，比较麻烦。直接input；
            // 如果用 select，可参考：https://blog.csdn.net/lmarster/article/details/86138926
            webDriver.findElement(By.xpath("//*[@id=\"province\"]")).sendKeys(PROVINCE);
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"city\"]")).sendKeys(CITY);
            Thread.sleep(500);
            webDriver.findElement(By.xpath("//*[@id=\"area\"]")).sendKeys(AREA);
            Thread.sleep(500);
            // submit：
            webDriver.findElement(By.xpath("/html/body/form/div/div/div/div[8]/div/div[2]/button[1]")).click();
            webDriver.manage().window().maximize();
            Thread.sleep(10000);
            // close
            webDriver.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (webDriver != null) {
                //退出 chrome
                webDriver.quit();
            }
        }
    }

    // with class param

    public void scriptForISP(IspUser ispUser) throws Exception {
        WebDriver webDriver = null;
        // windows, D:\JavaProjectLearning\LSP\src\main\resources\
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        webDriver = new ChromeDriver();
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setHeadless(Boolean.TRUE);
//        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage", "window-size=1920x3000", "--disable-gpu", "--hide-scrollbars", "blink-settings=imagesEnabled=false", "--headless");
//        //启动一个 chrome 实例
//        // linux
//        System.setProperty("webdriver.chrome.driver", "/usr/lib64/chromium-browser/chromedriver");

//        webDriver = new ChromeDriver(chromeOptions);

        webDriver.get("http://xsswzx-cdu-edu-cn-s.vpn.cdu.edu.cn:8118/ispstu/com_user/weblogin.asp");
        // get the vpn permission
        Thread.sleep(2000);
        webDriver.findElement(By.className("input-txt")).sendKeys(ispUser.getUserId());
        webDriver.findElement(By.xpath(
                "/html/body/div[2]/div[1]/div/div[3]/div/div[2]/div[1]/div/div/div[2]/form/div[1]/div[2]/div[1]/div/div[1]/input"
        )).sendKeys(ispUser.getPwd());
        //
        Thread.sleep(2000);
        webDriver.findElement(By.xpath(
                "//*[@id=\"Calc\"]/div[3]/span"
        )).click();
        // submit
        Thread.sleep(2000);
        webDriver.findElement(By.xpath(
                "/html/body/div[2]/div[1]/div/div[3]/div/div[2]/div[1]/div/div/div[2]/form/div[4]/button"
        )).click();
        Thread.sleep(20000);

        // get the validCode:  //*[@id="form"]/div[3]
        String validCode = webDriver.findElement(By.xpath("//*[@id=\"form\"]/div[3]")).getText().substring(2);
        // login here:
        webDriver.findElement(By.id("username")).sendKeys(ispUser.getUserId());
        Thread.sleep(1000);
        webDriver.findElement(By.id("userpwd")).sendKeys(ispUser.getIspPwd());
        Thread.sleep(1000);
        webDriver.findElement(By.id("code")).sendKeys(validCode);
        Thread.sleep(1000);
        // submit:
        webDriver.findElement(By.id("提交")).click();
        Thread.sleep(10000);
        // after login, to find the isp register, and to submit the information;
        webDriver.get("http://xsswzx-cdu-edu-cn-s.vpn.cdu.edu.cn:8118/ispstu/com_user/projecthealth.asp?id=d3c87b7b13b404fe5c12cad9a638208286065a4ebc69");
        Thread.sleep(1000);
//            // //*[@id="t"]/tbody/tr[6]/td/a[1]: then 点击常规登记（因为每隔5天要重置信息，所以直接常规登记。。。暂时）
        // /html/body/div/div[1]/div/div/table/tbody/tr[3]/td/a[1]
        webDriver.findElement(By.xpath("/html/body/div/div[1]/div/div/table/tbody/tr[3]/td/a[1]")).click();
        Thread.sleep(1000);
        webDriver.manage().window().maximize();
        // select tag: 这里不用select选项和option进行，比较麻烦。直接input；
        // 如果用 select，可参考：https://blog.csdn.net/lmarster/article/details/86138926
        String[] split = ispUser.getArea().split("/");
        PROVINCE = split[0];
        CITY = split[1];
        AREA = split[2];
        webDriver.findElement(By.xpath("//*[@id=\"province\"]")).sendKeys(PROVINCE);
        Thread.sleep(500);
        webDriver.findElement(By.xpath("//*[@id=\"city\"]")).sendKeys(CITY);
        Thread.sleep(500);
        webDriver.findElement(By.xpath("//*[@id=\"area\"]")).sendKeys(AREA);
        Thread.sleep(500);
        // submit：/html/body/form/div/div/div/div[7]/div/div/div/div/button[1]
        webDriver.findElement(By.xpath("/html/body/form/div/div/div/div[7]/div/div/div/div/button[1]")).click();
        webDriver.manage().window().maximize();
        // 正常提交
        Thread.sleep(10000);
        // close
        webDriver.close();
        webDriver.quit();
    }
}

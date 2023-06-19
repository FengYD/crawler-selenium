package com.feng.crawler.douban.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.feng.crawler.base.common.constant.RegexConstant;
import com.feng.crawler.base.common.enums.CustomExceptionEnum;
import com.feng.crawler.base.common.utils.CrawlerUtil;
import com.feng.crawler.base.common.utils.RegexUtil;
import com.feng.crawler.base.domain.SysAccount;
import com.feng.crawler.base.model.CustomException;
import com.feng.crawler.base.service.BaseLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author fengyadong
 * @date 2023/6/19 10:34
 * @Description
 */
@Slf4j
@Service
public class DoubanLoginService extends BaseLoginService {

    @Value("${download.path}")
    private String downloadPath;

    @Override
    protected void login0(List<WebDriver> webDriverList, List<SysAccount> accountList, String loginUrl) {
        LinkedList<SysAccount> stack = new LinkedList<>(accountList);
        for (WebDriver webDriver : webDriverList) {
            SysAccount account = stack.pop();
            webDriver.get(loginUrl);
            webDriver.findElement(By.className("account-tab-account")).click();
            WebElement loginForm = webDriver.findElement(By.className("account-form"));
            loginForm.findElement(By.id("username")).sendKeys(account.getUsername());
            CrawlerUtil.sleep(1000, 2000);
            loginForm.findElement(By.id("password")).sendKeys(account.getPassword());
            CrawlerUtil.sleep(1000, 2000);
            loginForm.findElement(By.className("account-form-field-submit")).click();
            CrawlerUtil.sleep(1000, 2000);
            if (CrawlerUtil.existWebElement(webDriver, By.id("tcaptcha_iframe_dy"))) {
                handleCaptcha(webDriver);
            }
        }
    }

    private void handleCaptcha(WebDriver webDriver) {
        WebElement captchaIframe = webDriver.findElement(By.id("tcaptcha_iframe_dy"));
        Integer distance = calDistance(webDriver, captchaIframe);
        System.out.println(distance);
    }

    /**
     * 计算
     *
     * @see https://www.cnblogs.com/eliwang/p/15260822.html
     */
    private Integer calDistance(WebDriver webDriver, WebElement captchaIframe) {
        webDriver.switchTo().frame(captchaIframe);
        WebElement captcha = webDriver.findElement(By.className("tc-opera"));
        // 滑块的背景
        WebElement background = captcha.findElement(By.id("slideBg"));
        // 滑块拼图小块
        List<WebElement> puzzleList = captcha.findElements(By.className("tc-fg-item"));
        WebElement puzzle = puzzleList.get(1);
        String puzzleStyle = puzzle.getAttribute("style");
        String width = RegexUtil.extract(puzzleStyle, RegexConstant.WidthPattern);
        String height = RegexUtil.extract(puzzleStyle, RegexConstant.HeightPattern);
        // 豆瓣的小块有时候出现在第二个，有时候出现在第三个 根据小块的属性确定哪个是正确的小块
        if (!width.equals(height)) {
            puzzle = puzzleList.get(2);
        }
        String bgPath = downloadImg(background);
        String puzzlePath = downloadImg(puzzle);
        Mat bgImg = handleImg(bgPath);
        Mat puzzleImg = handleImg(puzzlePath);
        Mat resultImg = bgImg.clone();
        Imgproc.matchTemplate(bgImg, puzzleImg, resultImg, Imgproc.TM_CCOEFF_NORMED);
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(resultImg);
        FileUtil.del(bgPath);
        FileUtil.del(puzzlePath);
        return (int) Math.ceil(minMaxLocResult.minLoc.x);
    }


    private String downloadImg(WebElement webElement) {
        String style = webElement.getAttribute("style");
        String url = RegexUtil.extract(style, RegexConstant.DoubanUrlPattern);
        url = StringEscapeUtils.unescapeHtml4(url);
        url = url.replace("**", "").replace("\"", "");
        HttpResponse response = HttpUtil.createGet(url).execute();
        String contentType = response.header(Header.CONTENT_TYPE);
        if (!contentType.startsWith("image")) {
            throw new CustomException(CustomExceptionEnum.ERROR_CONTENT_TYPE);
        }
        String filePath = downloadPath + UUID.randomUUID().toString() + contentType.replace("image/", ".");
        HttpUtil.downloadFile(url, filePath);
        return filePath;
    }

    /**
     * 处理图片
     *
     * @see https://www.cnblogs.com/eliwang/p/15260822.html
     */
    private Mat handleImg(String imgUrl) {
        // 读入灰度图片
        Mat image0 = Imgcodecs.imread(imgUrl, Imgcodecs.IMREAD_GRAYSCALE);
        Mat image1 = image0.clone();
        // 高斯模糊去噪
        Imgproc.GaussianBlur(image0, image1, new Size(3, 3), 0);
        // Canny算法进行边缘检测
        Mat image2 = image1.clone();
        Imgproc.Canny(image1, image2, 50, 150);
        return image2;
    }
}

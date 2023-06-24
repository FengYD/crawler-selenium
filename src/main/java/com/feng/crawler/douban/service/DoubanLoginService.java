package com.feng.crawler.douban.service;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.feng.crawler.base.common.constant.RegexConstant;
import com.feng.crawler.base.common.utils.CrawlerUtil;
import com.feng.crawler.base.common.utils.RegexUtil;
import com.feng.crawler.base.common.utils.SliderUtil;
import com.feng.crawler.base.domain.SysAccount;
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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.Duration;
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
    protected void login0(WebDriver webDriver, SysAccount account) {
        // 等待登录界面出现
        WebElement loginWrap = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("login-wrap")));
        // 点击“密码登录”
        loginWrap.findElement(By.className("account-tab-account")).click();
        // 输入用户名密码，用户名密码3个一组填入对应输入框
        WebElement loginForm = loginWrap.findElement(By.className("account-form"));
        String[] username = StrUtil.split(account.getUsername(), 3);
        loginForm.findElement(By.id("username")).sendKeys(username);
        CrawlerUtil.sleep(1000, 2000);
        String[] password = StrUtil.split(account.getPassword(), 3);
        loginForm.findElement(By.id("password")).sendKeys(password);
        CrawlerUtil.sleep(1000, 2000);
        // 点击“登录豆瓣”
        loginForm.findElement(By.className("account-form-field-submit")).click();
        CrawlerUtil.sleep(2000, 4000);
        // 检测滑块拼图
        if (CrawlerUtil.existWebElement(webDriver, By.id("tcaptcha_iframe_dy"))) {
            handleCaptcha(webDriver);
        }
    }

    /**
     * 处理验证码
     */
    private void handleCaptcha(WebDriver webDriver) {
        WebElement captchaIframe = webDriver.findElement(By.id("tcaptcha_iframe_dy"));
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
        String cutPuzzlePath = cutPuzzleImg(puzzlePath);
        Mat bgImg = handleImg(bgPath);
        Mat puzzleImg = handleImg(cutPuzzlePath);
        Mat resultImg = bgImg.clone();
        Imgproc.matchTemplate(bgImg, puzzleImg, resultImg, Imgproc.TM_CCOEFF_NORMED);
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(resultImg);
        FileUtil.del(bgPath);
        FileUtil.del(puzzlePath);
        FileUtil.del(cutPuzzlePath);
        // 页面上展示的图片有压缩
        double xPoint = minMaxLocResult.maxLoc.x / 672 * 340;
        // 滑块不在最左边，减去这个距离
        int xDistance = (int) Math.round(xPoint - 25.29);
        SliderUtil.moveButton(webDriver, puzzle, xDistance, SliderUtil.SIMULATE2);
        Actions action = new Actions(webDriver).click();
        action.perform();
        action.release();
        CrawlerUtil.sleep(1000, 2000);
        // 验证码验证失败，刷新重试
        while (CrawlerUtil.existWebElement(webDriver, By.id("tcaptcha_iframe_dy"))) {
            captcha.findElement(By.id("reload")).click();
            CrawlerUtil.sleep(2000, 3000);
            handleCaptcha(webDriver);
        }
    }


    /**
     * 下载图片
     */
    private String downloadImg(WebElement webElement) {
        String style = webElement.getAttribute("style");
        String url = RegexUtil.extract(style, RegexConstant.DoubanUrlPattern);
        url = StringEscapeUtils.unescapeHtml4(url);
        url = url.replace("**", "").replace("\"", "");
        String imgPath = downloadPath + UUID.randomUUID().toString() + ".png";
        HttpUtil.downloadFile(url, imgPath);
        return imgPath;
    }

    /**
     * puzzle块需要裁剪
     */
    private String cutPuzzleImg(String imgPath) {
        String newImgPath = downloadPath + UUID.randomUUID().toString() + ".png";
        ImgUtil.cut(
                FileUtil.file(imgPath),
                FileUtil.file(newImgPath),
                new Rectangle(140, 490, 120, 120)
        );
        return newImgPath;
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

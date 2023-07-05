package com.feng.crawlerselenium.base.common.constant;

import com.feng.crawlerselenium.base.common.utils.RegexUtil;

import java.util.regex.Pattern;

/**
 * @author fengyadong
 * @date 2023/6/19 13:54
 * @Description
 */
public class RegexConstant {


    /************************   douban  *****************************************/
    public static final Pattern DoubanUrlPattern = Pattern.compile("url\\((.+)\\)");

    /************************   common  *****************************************/
    public static final Pattern WidthPattern = Pattern.compile("width: ([.\\d]+)px;");
    public static final Pattern HeightPattern = Pattern.compile("height: ([.\\d]+)px;");


    public static void main(String[] args) {
        String s = "position: absolute; background-position: 0px 0px; background-size: 100%; width: 278px; height: 198.571px; left: 0px; top: 0px; background-repeat: no-repeat; overflow: hidden; z-index: 1; opacity: 1; background-image: url(&quot;https://t.captcha.qq.com/cap_union_new_getcapbysig?img_index=1&amp;image=027905000045e93900000009dcd49f8235c2&amp;sess=s0x9ElxNhiFz_sl-xbflo8iluHvVxvf36CC9O36k4-VijMmGaW-VvlNKUjXKCx7RennIMa_kP34zxRd03U-ZZVe7FuzD_FvNwkYitNHc4g6r_ueglgy-1jg74gY9PitDFuTzMuqRxbb8qAuq0V-KK7XoU2iuxxZRIiRcQRyxypmGLRdYv3A3CP7icAZGh334r_12P-BywGEjCirFCoWITLtJxCRI2PtQeBlewgiEgCkYToGSN_ZpbH-fnJ6-cCn4Fvkbqm2SYrmhVFXZl8i57LeEzvB8s4X94x0so2bsHx1YYKo0NOhP2YFuQkBfKEXj2_v9xUCymBmVx5vNeNSpgfLch4hMdbsVd72PeZlfszZeEAObUUYqnumw**&quot;);";
        String url = RegexUtil.extract(s, RegexConstant.DoubanUrlPattern);
        System.out.println(url);
        String width = RegexUtil.extract(s, RegexConstant.WidthPattern);
        System.out.println(width);
    }
}

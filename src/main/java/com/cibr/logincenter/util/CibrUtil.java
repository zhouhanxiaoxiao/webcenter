package com.cibr.logincenter.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;

public class CibrUtil {

    /**
     * 审核人
     */
    public static final String ROLE_TYPE_REVIEWER = "40";

    public static final String EMAIL_TITLE = "【北京脑科学与脑类研究中心-网上办事大厅】\n\n\n";
    public static final String EMAIL_SUFFIX = "\n\n\n感谢您使用网上办事大厅系统(http://seq.cibr.ac.cn/)！";

    public static String getUUID() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static int getVerificationCode() {
        Random r = new Random();
        return r.nextInt(900000) + 100000;
    }

    /**
     * 是否是Ajax异步请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {

        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1)
        {
            return true;
        }
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        {
            return true;
        }
        return false;
    }

    public static String replaceSpecialChar(String name) {
        if (name == null){
            return "";
        }
        return name.replace("(","")
                .replace(")","")
                .replace("（","")
                .replace("）","");
    }
}

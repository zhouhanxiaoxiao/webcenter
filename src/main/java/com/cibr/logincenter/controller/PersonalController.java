package com.cibr.logincenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cibr.logincenter.entity.CibrSysFile;
import com.cibr.logincenter.entity.CibrSysUser;
import com.cibr.logincenter.entity.CibrSysUserGroup;
import com.cibr.logincenter.entity.CibrUserNotice;
import com.cibr.logincenter.service.UserService;
import com.cibr.logincenter.util.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "个人中心controller")
@RestController
public class PersonalController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/personal/getUserInfo", method = RequestMethod.GET)
    public String getUserInfo(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) request.getAttribute("userId");
            CibrSysUser user = userService.getUserById(userId);
            List<CibrSysUserGroup> allDepartment = userService.findAllDepartment();
            retMap.put("user",user);
            retMap.put("groups",allDepartment);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "更新用户名")
    @ApiImplicitParam(value = "user", name = "用户信息字符串", required = true)
    @RequestMapping(value = "/personal/updateUser", method = RequestMethod.POST)
    public String updateUser(HttpServletRequest request,
                              HttpServletResponse response,
                             @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userStr = (String) requestBody.get("user");
            CibrSysUser user = JSONObject.parseObject(userStr, CibrSysUser.class);
            userService.updateUserName(user);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "更改密码时，获取验证码")
    @ApiImplicitParam(value = "user", name = "用户信息字符串", required = true)
    @RequestMapping(value = "/personal/getCode", method = RequestMethod.GET)
    public String getCode(HttpServletRequest request,
                             HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) request.getAttribute("userId");
            userService.sendCodeByUserId(userId);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "pwd", name = "新密码", required = true),
            @ApiImplicitParam(value = "validCode", name = "验证码", required = true),
    })
    @RequestMapping(value = "/personal/updatePwd", method = RequestMethod.POST)
    public String updatePwd(HttpServletRequest request,
                          HttpServletResponse response,
                            @RequestBody Map requestBody

    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) request.getAttribute("userId");
            String pwd = (String) requestBody.get("pwd");
            String validCode = (String) requestBody.get("validCode");
            String result = userService.updatePassword(userId, pwd, validCode);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "获取用户通知配置")
    @RequestMapping(value = "/personal/initNotice", method = RequestMethod.GET)
    public String initNotice(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) request.getAttribute("userId");
            CibrUserNotice notice = userService.findNoticeByUserId(userId);
            retMap.put("notice",notice);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "修改通知方式")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "email", name = "邮件通知方式", required = true),
            @ApiImplicitParam(value = "web", name = "网页通知方式", required = true),
    })
    @RequestMapping(value = "/personal/noticeUpdate", method = RequestMethod.POST)
    public String noticeUpdate(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) request.getAttribute("userId");
            Boolean email = (Boolean) requestBody.get("email");
            Boolean web = (Boolean) requestBody.get("web");
            String result = userService.updateNotice(userId,email,web);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

}

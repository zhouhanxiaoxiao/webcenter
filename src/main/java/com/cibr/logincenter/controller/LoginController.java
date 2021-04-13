package com.cibr.logincenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cibr.logincenter.entity.CibrSysUser;
import com.cibr.logincenter.entity.CibrSysUserGroup;
import com.cibr.logincenter.service.UserService;
import com.cibr.logincenter.util.CibrUtil;
import com.cibr.logincenter.util.RedisUtil;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "登录、注册controller")
@RestController
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "初始化注册页面，获取所有组别信息")
    @RequestMapping(value = "/register/initPage", method = RequestMethod.POST)
    public String getProcess(HttpServletRequest request,
                             HttpServletResponse response){
        ReturnData ret = new ReturnData();
        try {
            Map<String,Object> retMap = new HashMap<>();
            List<CibrSysUserGroup> groups = userService.findAllDepartment();
            retMap.put("groups",groups);
            ret.setCode("success");
            ret.setRetMap(retMap);
        }catch (Exception e) {
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "email",name = "邮箱",required = true)
    })
    @RequestMapping("/register/getCode")
    public String getCode(HttpServletRequest request,
                         HttpServletResponse response,
                          @RequestBody Map requestBody
                          ){
        ReturnData ret = new ReturnData();
        try {
            String email = (String) requestBody.get("email");
            Map<String,Object> retMap = new HashMap<>();
            String s = userService.sendCode(email);
            if (!"ok".equals(s)){
                ret.setCode(s);
            }else {
                ret.setCode("success");
            }
            ret.setRetMap(retMap);
        }catch (Exception e) {
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "注册方法")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "userStr", name = "用户信息字符串，包括用户名，英文名，密码，邮箱，组别，验证码",required = true)
    })
    @RequestMapping("/register/createUser")
    public String createUser(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            String userStr = (String) requestBody.get("user");
            CibrSysUser newUser = JSONObject.parseObject(userStr, CibrSysUser.class);
            Map<String,Object> retMap = new HashMap<>();
            String result = userService.createUser(newUser);
            if (!"ok".equals(result)){
                ret.setCode(result);
            }else {
                ret.setCode("success");
            }
            ret.setRetMap(retMap);
        }catch (Exception e) {
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiIgnore
    @RequestMapping("/login/handleLogin")
    public String handleLogin(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            String email = (String) requestBody.get("email");
            String password = (String) requestBody.get("password");
            boolean isPwd = (boolean) requestBody.get("isPwd");
            String code = (String) requestBody.get("code");
            Map<String,Object> retMap = new HashMap<>();
            String result = userService.handleLogin(email,password,isPwd,code);
            if (!"ok".equals(result)){
                ret.setCode(result);
            }else {
                String token = CibrUtil.getUUID();
                CibrSysUser user = userService.getUserByEmail(email);
                user.setPassword("");
                redisUtil.set(token,user);
                retMap.put("token",token);
                retMap.put("user", user);
                ret.setCode("success");
            }
            ret.setRetMap(retMap);
        }catch (Exception e) {
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiIgnore
    @RequestMapping("/login/exit")
    public String exit(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            String token = request.getHeader("token");
            redisUtil.delete(token);
            ret.setCode("success");
        }catch (Exception e) {
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }
}

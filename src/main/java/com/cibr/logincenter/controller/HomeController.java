package com.cibr.logincenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cibr.logincenter.entity.CibrSysUser;
import com.cibr.logincenter.entity.CibrSysWebsite;
import com.cibr.logincenter.service.AdminService;
import com.cibr.logincenter.service.UserService;
import com.cibr.logincenter.util.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "主页controller")
@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/home/initPage", method = RequestMethod.POST)
    public String initHomePage(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map requestBody
                               ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();

            if (request.getAttribute("userId") != null){
                String userId = (String) request.getAttribute("userId");
                CibrSysUser user = userService.getUserById(userId);
                List<CibrSysWebsite> allWebsit = adminService.findAllWebsit();
                retMap.put("webs",allWebsit);
                retMap.put("user",user);
            }
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }


}

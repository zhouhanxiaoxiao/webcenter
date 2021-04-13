package com.cibr.logincenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cibr.logincenter.entity.*;
import com.cibr.logincenter.service.AdminService;
import com.cibr.logincenter.service.UserService;
import com.cibr.logincenter.util.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "管理页面controller")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "上传websit图片")
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public String initHomePage(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            MultipartFile files = ((MultipartHttpServletRequest) request).getFile("webImg");
            String userId = "";
            if (request.getAttribute("userId") != null){
                userId = (String) request.getAttribute("userId");
            }
            CibrSysFile file = adminService.saveFile(files, userId);
            retMap.put("file",file);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "根据ID获取图片")
    @ApiImplicitParam(value = "fileId", name = "图片ID", required = true)
    @RequestMapping(value = "/file/img/{fileId}", method = RequestMethod.GET)
    public void userHead(HttpServletRequest request,
                         HttpServletResponse response,
                         @PathVariable String fileId) throws IOException {
        File userHead = adminService.getImage(fileId);
        FileInputStream fileInputStream = new FileInputStream(userHead);
        ServletOutputStream outputStream = response.getOutputStream();
        //创建存放文件内容的数组
        byte[] buff = new byte[1024];
        //所读取的内容使用n来接收
        int n;
        //当没有读取完时,继续读取,循环
        while ((n = fileInputStream.read(buff)) != -1) {
            //将字节数组的数据全部写入到输出流中
            outputStream.write(buff, 0, n);
        }
        outputStream.flush();
        outputStream.close();
        fileInputStream.close();
    }

    @ApiOperation(value = "新增站点信息")
    @ApiImplicitParam(value = "web", name = "站点信息字符串", required = true)
    @RequestMapping(value = "/admin/submitNewWebsit", method = RequestMethod.POST)
    public String initHomePage(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String web = (String) requestBody.get("web");
            CibrSysWebsite website = JSONObject.parseObject(web, CibrSysWebsite.class);
            String userId = (String) request.getAttribute("userId");
            String result = adminService.createWebsit(website, userId);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "更新站点信息")
    @ApiImplicitParam(value = "web", name = "站点信息字符串", required = true)
    @RequestMapping(value = "/admin/updateWebsit", method = RequestMethod.POST)
    public String updateWebsit(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String web = (String) requestBody.get("web");
            CibrSysWebsite website = JSONObject.parseObject(web, CibrSysWebsite.class);
            String userId = (String) request.getAttribute("userId");
            String result = adminService.updateWebsit(website, userId);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "获取所有站点信息")
    @RequestMapping(value = "/admin/webManagerInit", method = RequestMethod.GET)
    public String webManagerInit(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            List<CibrSysWebsite> allWebsit = adminService.findAllWebsit();
            retMap.put("websits",allWebsit);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "获取所有组别信息")
    @RequestMapping(value = "/admin/getAllGroups", method = RequestMethod.GET)
    public String getAllGroups(HttpServletRequest request,
                                 HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            List<CibrSysUserGroup> groups = adminService.findAllGroups();
            List<CibrSysUser> users = userService.findAllUsers();
            retMap.put("groups", groups);
            retMap.put("users", users);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "更新部门信息")
    @ApiImplicitParam(value = "groups", name = "更新部门信息", required = true)
    @RequestMapping(value = "/admin/updateGroup", method = RequestMethod.POST)
    public String updateGroup(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String groupStr = (String) requestBody.get("group");
            CibrSysUserGroup group = JSONObject.parseObject(groupStr, CibrSysUserGroup.class);
            String userId = (String) request.getAttribute("userId");
            String result = userService.updateGroups(group, userId);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "新增部门信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "groupname", name = "部门名称", required = true),
            @ApiImplicitParam(value = "groupEng", name = "部门英文名称", required = true),
            @ApiImplicitParam(value = "groupadmin", name = "部门负责人", required = true),
            @ApiImplicitParam(value = "userEmail", name = "负责人邮箱", required = true),
            @ApiImplicitParam(value = "englishName", name = "负责人英文名称", required = true),
            @ApiImplicitParam(value = "userName", name = "负责人真实名称", required = true),
            @ApiImplicitParam(value = "userPwd", name = "负责人密码", required = true),
            @ApiImplicitParam(value = "realPwd", name = "realPwd", required = true),

    })
    @RequestMapping(value = "/admin/addNewGroup", method = RequestMethod.POST)
    public String addNewGroup(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String groupname = (String) requestBody.get("groupname");
            String groupEng = (String) requestBody.get("groupEng");
            String groupadmin = (String) requestBody.get("groupadmin");
            String englishName = (String) requestBody.get("englishName");
            String userEmail = (String) requestBody.get("userEmail");
            String userName = (String) requestBody.get("userName");
            String userPwd = (String) requestBody.get("userPwd");
            String realPwd = (String) requestBody.get("realPwd");
            String userId = (String) request.getAttribute("userId");

            String result = userService.addNewGroup(groupEng,groupname, groupadmin,englishName,
                    userEmail,userName,userPwd,realPwd,userId);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }


    @ApiOperation(value = "获取所有用户信息")
    @RequestMapping(value = "/admin/getAllUser", method = RequestMethod.GET)
    public String getAllUser(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            List<CibrSysUserGroup> groups = userService.getAllGroups();
            List<CibrSysUser> users = userService.findAllUsersDetail();
            List<CibrSysRole> roles =userService.getallRoles();
            retMap.put("groups", groups);
            retMap.put("users", users);
            retMap.put("roles", roles);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParam(value = "user", name = "用户信息", required = true)
    @RequestMapping(value = "/admin/updateUser", method = RequestMethod.POST)
    public String updateUser(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userStr = (String) requestBody.get("user");
            CibrSysUser user = JSONObject.parseObject(userStr, CibrSysUser.class);
            String userId = (String) request.getAttribute("userId");
            String result = userService.updateUser(user, userId);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "删除用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "userId", name = "用户Id", required = true),
            @ApiImplicitParam(value = "roleId", name = "角色Id", required = true)
    })
    @RequestMapping(value = "/admin/deleteRole", method = RequestMethod.POST)
    public String deleteRole(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) requestBody.get("userId");
            String roleId = (String) requestBody.get("roleId");
            String result = userService.deleteRole(userId, roleId);
            ret.setRetMap(retMap);
            ret.setCode(result);
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "新增用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "userId", name = "用户Id", required = true),
            @ApiImplicitParam(value = "roleId", name = "角色Id", required = true)
    })
    @RequestMapping(value = "/admin/addNewRole", method = RequestMethod.POST)
    public String addNewRole(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestBody Map requestBody
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = (String) requestBody.get("userId");
            String roleId = (String) requestBody.get("roleId");
            String result = userService.addNewRole(userId, roleId);
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

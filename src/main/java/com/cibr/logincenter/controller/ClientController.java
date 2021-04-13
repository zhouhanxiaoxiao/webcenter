package com.cibr.logincenter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cibr.logincenter.entity.CibrSysUser;
import com.cibr.logincenter.entity.CibrSysUserGroup;
import com.cibr.logincenter.service.UserService;
import com.cibr.logincenter.util.ReturnData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "客户端获取信息")
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(HttpServletRequest request,
                              HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            CibrSysUser user = userService.getUserById(userId);
//            List<CibrSysUserGroup> allDepartment = userService.findAllDepartment();
            retMap.put("user",user);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "获取用户组信息")
    @RequestMapping(value = "/getGroupInfo", method = RequestMethod.POST)
    public String getGroupInfo(HttpServletRequest request,
                              HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            CibrSysUserGroup group = userService.getGroupInfo(userId);
//            List<CibrSysUserGroup> allDepartment = userService.findAllDepartment();
            retMap.put("group",group);
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @ApiOperation(value = "获取所有用户组信息")
    @RequestMapping(value = "/getAllGroups", method = RequestMethod.POST)
    public String getAllGroups(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("groups", userService.getAllGroups());
                }
            }
//            List<CibrSysUserGroup> allDepartment = userService.findAllDepartment();
            ret.setRetMap(retMap);
            ret.setCode("success");
        }catch (Exception e){
            ret.setCode("E500");
            ret.setErrMsg("系统异常！");
            e.printStackTrace();
        }
        return JSON.toJSONString(ret, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }


    @ApiOperation(value = "获取拥有权限的用户信息")
    @RequestMapping(value = "/selectUserByRoleType", method = RequestMethod.POST)
    public String selectUserByRoleType(HttpServletRequest request,
                               HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            String roleType = request.getParameter("roleType");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("usersByRole", userService.selectUserByRoleType(roleType));
                }
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

    @ApiOperation(value = "获取所有用户的详细信息")
    @RequestMapping(value = "/selectAllUserWithDesc", method = RequestMethod.POST)
    public String selectAllUserWithDesc(HttpServletRequest request,
                                       HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("selectAllUserWithDesc", userService.selectAllUserWithDesc());
                }
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

    @ApiOperation(value = "根据部门名获取部门信息")
    @RequestMapping(value = "/getGroupByName", method = RequestMethod.POST)
    public String getGroupByName(HttpServletRequest request,
                                        HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            String groupName = request.getParameter("groupName");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("group", userService.getGroupByName(groupName));
                }
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

    @ApiOperation(value = "根据用户ID获取部门管理员")
    @RequestMapping(value = "/getGroupAdmin", method = RequestMethod.POST)
    public String getGroupAdmin(HttpServletRequest request,
                                 HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("admin", userService.getGroupAdmin(userId));
                }
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

    @ApiOperation(value = "根据角色和部门名称获取用户")
    @RequestMapping(value = "/findUserByRoleAndGroup", method = RequestMethod.POST)
    public String findUserByRoleAndGroup(HttpServletRequest request,
                                HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String roleTypeReviewer = request.getParameter("roleTypeReviewer");
            String groupName = request.getParameter("groupName");
            String userId = request.getParameter("userId");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("users", userService.findUserByRoleAndGroup(roleTypeReviewer,groupName));
                }
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

    @ApiOperation(value = "根据用户id获取审核人信息")
    @RequestMapping(value = "/findGroupReviewer", method = RequestMethod.POST)
    public String findGroupReviewer(HttpServletRequest request,
                                         HttpServletResponse response
    ){
        ReturnData ret = new ReturnData();
        try {
            Map<String, Object> retMap = new HashMap<>();
            String userId = request.getParameter("userId");
            if (userId != null){
                CibrSysUser userById = userService.getUserById(userId);
                if (userById != null){
                    retMap.put("reviews", userService.findGroupReviewer(userId));
                }
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

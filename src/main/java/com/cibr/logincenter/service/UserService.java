package com.cibr.logincenter.service;

import com.cibr.logincenter.dao.user.*;
import com.cibr.logincenter.entity.*;
import com.cibr.logincenter.util.CibrUtil;
import com.cibr.logincenter.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private CibrSysUserMapper userMapper;

    @Autowired
    private CibrSysUserGroupMapper groupMapper;

    @Autowired
    private CibrSysUserRoleMapper userRoleMapper;

    @Autowired
    private CibrSysRoleMapper roleMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CibrSysVerificationMapper verificationMapper;

    @Autowired
    private CibrUserNoticeMapper noticeMapper;

    /**
     * 获取所有组别
     */
    public List<CibrSysUserGroup> findAllDepartment() {
        List<CibrSysUserGroup> groups = groupMapper.selectByExample(new CibrSysUserGroupExample());
        if (groups == null){
            groups = new ArrayList<>();
        }
        return groups;
    }

    @Transactional(rollbackFor = Exception.class)
    public String sendCode(String email) {
        String code = String.valueOf(CibrUtil.getVerificationCode());
        CibrSysVerification verification = new CibrSysVerification();
        verification.setId(CibrUtil.getUUID());
        verification.setCode(code);
        verification.setEmail(email);
        verification.setCreatetime(new Date());
        verificationMapper.insert(verification);

        String emailContext = CibrUtil.EMAIL_TITLE + "您的验证码为：【" + code + "】";
        CibrSysEmail sysEmail = emailService.simpleSendEmail(emailContext, email, "验证码");
        if (!"ok".equals(sysEmail)){
            return sysEmail.getEmailStatus();
        }
        return "ok";
    }

    /**
     * 创建新用户
     * @param newUser
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String createUser(CibrSysUser newUser) {
        String email = newUser.getEmail();
        //校验验证码
        Date usefulTime = TimeUtil.dateAdd(new Date(), Calendar.HOUR, -1);
        CibrSysVerificationExample verificationExample = new CibrSysVerificationExample();
        verificationExample.createCriteria().andEmailEqualTo(email).andCreatetimeGreaterThan(usefulTime);
        verificationExample.setOrderByClause("createTime desc");
        List<CibrSysVerification> codes = verificationMapper.selectByExample(verificationExample);
        if (codes == null || codes.size() == 0 || !codes.get(0).getCode().equals(newUser.getValidate())){
            return "err.codeErr";
        }
        //校验邮箱
        CibrSysUserExample userExample = new CibrSysUserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<CibrSysUser> users = userMapper.selectByExample(userExample);
        if (users != null && users.size() > 0){
            return "user.emailExist";
        }

        newUser.setId(CibrUtil.getUUID());
        newUser.setCreatetime(new Date());
        newUser.setUserstatu("00");

        userMapper.insert(newUser);

        CibrSysRoleExample roleExample = new CibrSysRoleExample();
        roleExample.createCriteria().andRoletypeEqualTo("00");
        List<CibrSysRole> roles = roleMapper.selectByExample(roleExample);
        if (roles != null && roles.size()>0){
            roles.forEach(role ->{
                if (role != null){
                    CibrSysUserRole userRole = new CibrSysUserRole();
                    userRole.setUserid(newUser.getId());
                    userRole.setRoleid(role.getId());
                    userRoleMapper.insert(userRole);
                }
            });
        }
        return "ok";
    }

    /**
     * 处理登录信息
     * @param email
     * @param password
     * @param isPwd
     * @param code
     * @return
     */
    public String handleLogin(String email, String password, boolean isPwd, String code) {
        CibrSysUserExample userExample = new CibrSysUserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<CibrSysUser> users = userMapper.selectByExample(userExample);
        if (users == null || users.size() == 0){
            return "user.noUser";
        }
        if (!"00".equals(users.get(0).getUserstatu())){
            return "err.userErr" + users.get(0).getUserstatu();
        }
        if (isPwd){
            if (!password.equals(users.get(0).getPassword())){
                return "err.pwdNotMatch";
            }
        }else {
            Date oneHourAgo = TimeUtil.dateAdd(new Date(), Calendar.HOUR, -1);
            CibrSysVerificationExample verificationExample = new CibrSysVerificationExample();
            verificationExample.createCriteria().andCodeEqualTo(code).andEmailEqualTo(email).andCreatetimeGreaterThan(oneHourAgo);
            List<CibrSysVerification> cibrSysVerifications = verificationMapper.selectByExample(verificationExample);
            if (cibrSysVerifications == null || cibrSysVerifications.size() == 0){
                return "err.codeErr";
            }

        }
        return "ok";
    }

    public CibrSysUser getUserByEmail(String email) {
        CibrSysUserExample userExample = new CibrSysUserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<CibrSysUser> users = userMapper.selectByExample(userExample);
        return users.get(0);
    }

    public CibrSysUser getUserById(String userId) {
        CibrSysUser user = userMapper.selectDetailById(userId);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserName(CibrSysUser user) {
        CibrSysUser dbuser = userMapper.selectByPrimaryKey(user.getId());
        dbuser.setName(user.getName());
        dbuser.setEnglishname(user.getEnglishname());
        userMapper.updateByPrimaryKey(dbuser);
    }

    public void sendCodeByUserId(String userId) {
        CibrSysUser user = userMapper.selectByPrimaryKey(userId);
        this.sendCode(user.getEmail());
    }

    @Transactional(rollbackFor = Exception.class)
    public String updatePassword(String userId, String pwd, String validCode) {
        CibrSysUser user = userMapper.selectByPrimaryKey(userId);
        Date oneHourAgo = TimeUtil.dateAdd(new Date(), Calendar.HOUR, -1);
        CibrSysVerificationExample verificationExample = new CibrSysVerificationExample();
        verificationExample.createCriteria().andCodeEqualTo(validCode).andEmailEqualTo(user.getEmail()).andCreatetimeGreaterThan(oneHourAgo);
        List<CibrSysVerification> cibrSysVerifications = verificationMapper.selectByExample(verificationExample);
        if (cibrSysVerifications != null && cibrSysVerifications.size() > 0){
            user.setPassword(pwd);
            userMapper.updateByPrimaryKey(user);
            return "success";
        }else {
            return "err.codeErr";
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateNotice(String userId, Boolean email, Boolean web) {
        CibrUserNoticeExample noticeExample = new CibrUserNoticeExample();
        noticeExample.createCriteria().andUseridEqualTo(userId);
        List<CibrUserNotice> notices = noticeMapper.selectByExample(noticeExample);
        if (notices == null || notices.size() == 0){
            CibrUserNotice notice = new CibrUserNotice();
            notice.setUserid(userId);
            notice.setId(CibrUtil.getUUID());
            notice.setEmail(email);
            notice.setWeb(web);
            notice.setPhone(false);
            notice.setWechat(false);
            noticeMapper.insert(notice);
        }else {
            CibrUserNotice notice = notices.get(0);
            notice.setEmail(email);
            notice.setWeb(web);
            noticeMapper.updateByPrimaryKey(notice);
        }
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    public CibrUserNotice findNoticeByUserId(String userId) {
        CibrUserNoticeExample noticeExample = new CibrUserNoticeExample();
        noticeExample.createCriteria().andUseridEqualTo(userId);
        List<CibrUserNotice> notices = noticeMapper.selectByExample(noticeExample);
        if (notices == null || notices.size() == 0){
            CibrUserNotice notice = new CibrUserNotice();
            notice.setUserid(userId);
            notice.setId(CibrUtil.getUUID());
            notice.setEmail(true);
            notice.setWeb(true);
            notice.setPhone(false);
            notice.setWechat(false);
            noticeMapper.insert(notice);
            return notice;
        }else {
            CibrUserNotice notice = notices.get(0);
            return notice;
        }
    }

    public List<CibrSysUserGroup> getAllGroups() {
        List<CibrSysUserGroup> groups = groupMapper.selectAllGroups();
        return groups;
    }

    public List<CibrSysUser> findAllUsers() {
        return userMapper.selectByExample(new CibrSysUserExample());
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateGroups(CibrSysUserGroup group, String userId) {
        groupMapper.updateByPrimaryKey(group);
        return "success";
    }


    @Transactional(rollbackFor = Exception.class)
    public String addNewGroup( String groupEng,String groupname, String groupadmin, String englishName, String userEmail, String userName, String userPwd, String realPwd, String userId) {
        CibrSysUserGroup group = new CibrSysUserGroup();
        group.setId(CibrUtil.getUUID());
        group.setGroupname(groupname);
        group.setGrouptype("00");
        group.setGroupstatu("00");
        group.setGroupenglishname(groupEng);

        if (StringUtils.isEmpty(groupadmin)){
            CibrSysUser user = new CibrSysUser();
            user.setId(CibrUtil.getUUID());
            user.setCreatetime(new Date());
            user.setEmail(userEmail);
            user.setPassword(userPwd);
            user.setName(userName);
            user.setEnglishname(englishName);
            user.setGroupid(group.getId());
            user.setUserstatu("00");
            CibrSysRoleExample roleExample = new CibrSysRoleExample();
            roleExample.createCriteria().andRoletypeIn(Arrays.asList("40","21"));
            List<CibrSysRole> cibrSysRoles = roleMapper.selectByExample(roleExample);
            for (CibrSysRole role : cibrSysRoles){
                CibrSysUserRole userRole = new CibrSysUserRole();
                userRole.setRoleid(role.getId());
                userRole.setUserid(user.getId());
                userRoleMapper.insert(userRole);
            }
            userMapper.insert(user);
            String emailContext = CibrUtil.EMAIL_TITLE + userName + "老师，您好。【" + group.getGroupname() + "】已在网上办事大厅开通，您的账号为【" +
                    userEmail +"】,初始密码为【" + realPwd + "】。" + CibrUtil.EMAIL_SUFFIX;
            emailService.simpleSendEmail(emailContext,userEmail,"网上办事大厅开通");
        }else {
            group.setGroupadmin(groupadmin);
        }
        groupMapper.insert(group);
        return "success";
    }

    public List<CibrSysUser> findAllUsersDetail() {
        return userMapper.selectusersDetal();
    }

    public List<CibrSysRole> getallRoles() {
        return roleMapper.selectByExample(new CibrSysRoleExample());
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateUser(CibrSysUser user, String userId) {
        userMapper.updateByPrimaryKey(user);
        return "success";
    }

    public String deleteRole(String userId, String roleId) {
        CibrSysUserRoleExample userRoleExample = new CibrSysUserRoleExample();
        userRoleExample.createCriteria().andUseridEqualTo(userId).andRoleidEqualTo(roleId);
        userRoleMapper.deleteByExample(userRoleExample);
        return "success";
    }

    public String addNewRole(String userId, String roleId) {
        CibrSysUserRole userRole = new CibrSysUserRole();
        userRole.setRoleid(roleId);
        userRole.setUserid(userId);
        userRoleMapper.insert(userRole);
        return "success";
    }

    public CibrSysUserGroup getGroupInfo(String userId) {
        CibrSysUser user = userMapper.selectByPrimaryKey(userId);
        CibrSysUserGroup group = groupMapper.selectByPrimaryKey(user.getGroupid());
        CibrSysUser groupAdmin = userMapper.selectByPrimaryKey(group.getGroupadmin());

        CibrSysUserExample userExample = new CibrSysUserExample();
        userExample.createCriteria().andGroupidEqualTo(group.getId());
        List<CibrSysUser> users = userMapper.selectByExample(userExample);
        group.setUsers(users);
        group.setAdmin(groupAdmin);
        return group;
    }

    public List<CibrSysUser> selectUserByRoleType(String roleType) {
        return userMapper.selectUserByRoleType(roleType);
    }

    public List<CibrSysUser> selectAllUserWithDesc() {
        return userMapper.selectusersDetal();
    }

    public Object getGroupByName(String groupName) {
        CibrSysUserGroupExample groupExample = new CibrSysUserGroupExample();
        groupExample.createCriteria().andGroupnameEqualTo(groupName);
        List<CibrSysUserGroup> groups = groupMapper.selectByExample(groupExample);
        if (groups != null && groups.size() > 0){
            return groups.get(0);
        }
        return null;
    }

    public CibrSysUser getGroupAdmin(String userId) {
        CibrSysUser user = userMapper.selectByPrimaryKey(userId);
        CibrSysUserGroup group = groupMapper.selectByPrimaryKey(user.getGroupid());
        return userMapper.selectByPrimaryKey(group.getGroupadmin());
    }

    public List<CibrSysUser> findUserByRoleAndGroup(String roleTypeReviewer, String groupName) {
        List<CibrSysUser> users = selectUserByRoleType(roleTypeReviewer);
        CibrSysUserGroupExample groupExample =  new CibrSysUserGroupExample();
        groupExample.createCriteria().andGroupnameEqualTo(groupName);
        List<CibrSysUserGroup> groups = groupMapper.selectByExample(groupExample);
        return users.stream().filter(user -> groups.get(0).getId().equals(user.getGroupid())).collect(Collectors.toList());
    }

    public List<CibrSysUser> findGroupReviewer(String userId) {
        CibrSysUser user = userMapper.selectByPrimaryKey(userId);
        List<CibrSysUser> users = selectUserByRoleType(CibrUtil.ROLE_TYPE_REVIEWER);
        return users.stream().filter(user1 -> user.getGroupid().equals(user1.getGroupid())).collect(Collectors.toList());
    }
}

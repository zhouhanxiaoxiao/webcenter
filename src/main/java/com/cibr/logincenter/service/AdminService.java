package com.cibr.logincenter.service;

import com.cibr.logincenter.dao.logincenter.CibrSysFileMapper;
import com.cibr.logincenter.dao.logincenter.CibrSysWebsiteMapper;
import com.cibr.logincenter.entity.CibrSysFile;
import com.cibr.logincenter.entity.CibrSysUserGroup;
import com.cibr.logincenter.entity.CibrSysWebsite;
import com.cibr.logincenter.entity.CibrSysWebsiteExample;
import com.cibr.logincenter.util.CibrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AdminService {

    @Autowired
    private CibrSysFileMapper fileMapper;

    @Autowired
    private CibrSysWebsiteMapper websiteMapper;

    private static String webImg = "webImg";

    @Value("${cibr.filepath}")
    private String filePath;

    @Autowired
    private UserService userService;

    public CibrSysFile saveFile(MultipartFile file, String userId) throws IOException {
        return this.saveFile(file, null, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public File saveFile(List<MultipartFile> files, String detailId, String userId) {
        File dist = null;
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new RuntimeException("上传的文件为空！");
            }
            String fileName = file.getOriginalFilename();
            File path = new File(filePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            dist = new File(filePath + File.separator + CibrUtil.getUUID() + fileName);
            String md5 = null;
            try {
                md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
                file.transferTo(dist);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            CibrUtil.replaceSpecialChar(dist.getName());
            CibrSysFile cibrSysFile = new CibrSysFile();
            cibrSysFile.setId(CibrUtil.getUUID());
            cibrSysFile.setCreater(userId);
            cibrSysFile.setCreatetime(new Date());
            cibrSysFile.setDetailid(detailId);
            cibrSysFile.setFilestatu("00");
            cibrSysFile.setMd5(md5);
            cibrSysFile.setName(CibrUtil.replaceSpecialChar(dist.getName()));
            cibrSysFile.setPath(CibrUtil.replaceSpecialChar(dist.getPath()));
            cibrSysFile.setRealname(CibrUtil.replaceSpecialChar(fileName));
            fileMapper.insert(cibrSysFile);
        }
        return dist;
    }

    @Transactional(rollbackFor = Exception.class)
    public CibrSysFile saveFile(MultipartFile file, String detailId, String userId) {
        File dist = null;
        if (file.isEmpty()) {
            throw new RuntimeException("上传的文件为空！");
        }
        String fileName = file.getOriginalFilename();
        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        dist = new File(filePath + File.separator + CibrUtil.getUUID() + fileName);
        String md5 = null;
        try {
            md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
            file.transferTo(dist);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        CibrUtil.replaceSpecialChar(dist.getName());
        CibrSysFile cibrSysFile = new CibrSysFile();
        cibrSysFile.setId(CibrUtil.getUUID());
        cibrSysFile.setCreater(userId);
        cibrSysFile.setCreatetime(new Date());
        cibrSysFile.setDetailid(detailId);
        cibrSysFile.setFilestatu("00");
        cibrSysFile.setMd5(md5);
        cibrSysFile.setName(CibrUtil.replaceSpecialChar(dist.getName()));
        cibrSysFile.setPath(CibrUtil.replaceSpecialChar(dist.getPath()));
        cibrSysFile.setRealname(CibrUtil.replaceSpecialChar(fileName));
        fileMapper.insert(cibrSysFile);
        return cibrSysFile;
    }

    public File getImage(String id) {
        CibrSysFile file = fileMapper.selectByPrimaryKey(id);
        File img = new File(file.getPath());
        return img;
    }

    @Transactional(rollbackFor = Exception.class)
    public String createWebsit(CibrSysWebsite website, String userId) {
        website.setId(CibrUtil.getUUID());
        website.setCreater(userId);
        website.setCreatetime(new Date());
        website.setCurstatu("00");
        CibrSysFile file = fileMapper.selectByPrimaryKey(website.getImgurl());
        file.setDetailid(website.getId());
        fileMapper.updateByPrimaryKey(file);
        websiteMapper.insert(website);
        return "success";
    }

    public List<CibrSysWebsite> findAllWebsit() {
        CibrSysWebsiteExample websiteExample = new CibrSysWebsiteExample();
        websiteExample.setOrderByClause("name");
        return websiteMapper.selectByExample(websiteExample);
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateWebsit(CibrSysWebsite website, String userId) {
        websiteMapper.updateByPrimaryKey(website);
        return "success";
    }

    public List<CibrSysUserGroup> findAllGroups() {
        List<CibrSysUserGroup> groups = userService.getAllGroups();
        return groups;
    }
}

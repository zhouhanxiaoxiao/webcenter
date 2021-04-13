package com.cibr.logincenter.dao.user;

import com.cibr.logincenter.entity.CibrSysUser;
import com.cibr.logincenter.entity.CibrSysUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CibrSysUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int countByExample(CibrSysUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int deleteByExample(CibrSysUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int insert(CibrSysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int insertSelective(CibrSysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    List<CibrSysUser> selectByExample(CibrSysUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    CibrSysUser selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CibrSysUser record, @Param("example") CibrSysUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CibrSysUser record, @Param("example") CibrSysUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CibrSysUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cibr_sys_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CibrSysUser record);

    CibrSysUser selectDetailById(@Param("userId") String userId);

    List<CibrSysUser> selectusersDetal();

    List<CibrSysUser> selectUserByRoleType(@Param("roleType") String roleType);

}